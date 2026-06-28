package tn.esprit.rh.achat.dto;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.rh.achat.entities.Role;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String prenom;
    private String password;
    private Role role;
}
