package tn.esprit.rh.achat.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.rh.achat.entities.Role;
import tn.esprit.rh.achat.repositories.AdministrateurRepository;
import tn.esprit.rh.achat.repositories.FournisseurRepository;
import tn.esprit.rh.achat.repositories.OperateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final FournisseurRepository fournisseurRepository;
    private final OperateurRepository operateurRepository;
    private final AdministrateurRepository administrateurRepository;

    public CustomUserDetailsService(
            FournisseurRepository fournisseurRepository,
            OperateurRepository operateurRepository,
            AdministrateurRepository administrateurRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.operateurRepository = operateurRepository;
        this.administrateurRepository = administrateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return fournisseurRepository.findByNom(username)
                .<UserDetails>map(user -> new AuthUser(user.getIdFournisseur(), user.getNom(), user.getPassword(), Role.FOURNISSEUR))
                .or(() -> operateurRepository.findByNom(username)
                        .map(user -> new AuthUser(user.getIdOperateur(), user.getNom(), user.getPassword(), Role.OPERATEUR)))
                .or(() -> administrateurRepository.findByNom(username)
                        .map(user -> new AuthUser(user.getIdAdministrateur(), user.getNom(), user.getPassword(), Role.ADMINISTRATEUR)))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
    }

    public UserDetails loadUserByUsernameById(Long userId, Role role) {
        switch (role) {
            case FOURNISSEUR:
                return fournisseurRepository.findById(userId)
                        .map(user -> new AuthUser(user.getIdFournisseur(), user.getNom(), user.getPassword(), Role.FOURNISSEUR))
                        .orElseThrow(() -> new UsernameNotFoundException("Fournisseur introuvable: " + userId));
            case OPERATEUR:
                return operateurRepository.findById(userId)
                        .map(user -> new AuthUser(user.getIdOperateur(), user.getNom(), user.getPassword(), Role.OPERATEUR))
                        .orElseThrow(() -> new UsernameNotFoundException("Operateur introuvable: " + userId));
            case ADMINISTRATEUR:
                return administrateurRepository.findById(userId)
                        .map(user -> new AuthUser(user.getIdAdministrateur(), user.getNom(), user.getPassword(), Role.ADMINISTRATEUR))
                        .orElseThrow(() -> new UsernameNotFoundException("Administrateur introuvable: " + userId));
            default:
                throw new UsernameNotFoundException("Role invalide: " + role);
        }
    }
}
