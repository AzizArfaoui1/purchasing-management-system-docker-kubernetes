package tn.esprit.rh.achat.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rh.achat.dto.AccessTokenResponse;
import tn.esprit.rh.achat.dto.AuthResponse;
import tn.esprit.rh.achat.dto.LoginRequest;
import tn.esprit.rh.achat.dto.SignupRequest;
import tn.esprit.rh.achat.entities.Administrateur;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.entities.RefreshToken;
import tn.esprit.rh.achat.entities.Role;
import tn.esprit.rh.achat.repositories.AdministrateurRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.OperateurRepository;
import tn.esprit.rh.achat.repositories.RefreshTokenRepository;
import tn.esprit.rh.achat.security.AuthUser;
import tn.esprit.rh.achat.security.CustomUserDetailsService;
import tn.esprit.rh.achat.security.JwtService;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FournisseurRepository fournisseurRepository;
    private final OperateurRepository operateurRepository;
    private final AdministrateurRepository administrateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final long refreshTokenExpirationMs;

    public AuthService(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            FournisseurRepository fournisseurRepository,
            OperateurRepository operateurRepository,
            AdministrateurRepository administrateurRepository,
            PasswordEncoder passwordEncoder,
            @Value("${security.jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.operateurRepository = operateurRepository;
        this.administrateurRepository = administrateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        AuthUser user = (AuthUser) userDetailsService.loadUserByUsername(request.getUsername());
        return createAuthResponse(user);
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        validateSignupRequest(request);
        if (usernameExists(request.getUsername())) {
            throw new IllegalArgumentException("Nom d'utilisateur deja utilise");
        }

        AuthUser user = createUser(request);
        return createAuthResponse(user);
    }

    @Transactional
    public AccessTokenResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .filter(savedToken -> !savedToken.isRevoked())
                .filter(savedToken -> savedToken.getExpiryDate().isAfter(Instant.now()))
                .orElseThrow(() -> new IllegalArgumentException("Refresh token invalide ou expire"));

        AuthUser user = (AuthUser) userDetailsService.loadUserByUsername(resolveUsername(refreshToken));
        return new AccessTokenResponse(jwtService.generateAccessToken(user), "Bearer");
    }

    @Transactional
    public void logout(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });
    }

    private AuthResponse createAuthResponse(AuthUser user) {
        RefreshToken refreshToken = createRefreshToken(user);
        return new AuthResponse(
                jwtService.generateAccessToken(user),
                refreshToken.getToken(),
                "Bearer",
                user.getId(),
                user.getRole());
    }

    private RefreshToken createRefreshToken(AuthUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(user.getId());
        refreshToken.setRole(user.getRole());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    private String resolveUsername(RefreshToken refreshToken) {
        return userDetailsService.loadUserByUsernameById(refreshToken.getUserId(), refreshToken.getRole()).getUsername();
    }

    private void validateSignupRequest(SignupRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Nom d'utilisateur obligatoire");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Mot de passe obligatoire");
        }
        if (request.getRole() == null) {
            throw new IllegalArgumentException("Role obligatoire");
        }
    }

    private boolean usernameExists(String username) {
        return fournisseurRepository.findByNom(username).isPresent()
                || operateurRepository.findByNom(username).isPresent()
                || administrateurRepository.findByNom(username).isPresent();
    }

    private AuthUser createUser(SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Role role = request.getRole();

        switch (role) {
            case FOURNISSEUR:
                Fournisseur fournisseur = new Fournisseur();
                fournisseur.setNom(request.getUsername());
                fournisseur.setPrenom(request.getPrenom());
                fournisseur.setPassword(encodedPassword);
                Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
                return new AuthUser(savedFournisseur.getIdFournisseur(), savedFournisseur.getNom(), savedFournisseur.getPassword(), Role.FOURNISSEUR);
            case OPERATEUR:
                Operateur operateur = new Operateur();
                operateur.setNom(request.getUsername());
                operateur.setPrenom(request.getPrenom());
                operateur.setPassword(encodedPassword);
                Operateur savedOperateur = operateurRepository.save(operateur);
                return new AuthUser(savedOperateur.getIdOperateur(), savedOperateur.getNom(), savedOperateur.getPassword(), Role.OPERATEUR);
            case ADMINISTRATEUR:
                Administrateur administrateur = new Administrateur();
                administrateur.setNom(request.getUsername());
                administrateur.setPrenom(request.getPrenom());
                administrateur.setPassword(encodedPassword);
                Administrateur savedAdministrateur = administrateurRepository.save(administrateur);
                return new AuthUser(savedAdministrateur.getIdAdministrateur(), savedAdministrateur.getNom(), savedAdministrateur.getPassword(), Role.ADMINISTRATEUR);
            default:
                throw new IllegalArgumentException("Role invalide: " + role);
        }
    }
}
