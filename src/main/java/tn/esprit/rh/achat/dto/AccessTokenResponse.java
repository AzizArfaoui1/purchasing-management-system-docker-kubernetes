package tn.esprit.rh.achat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
    private String tokenType;
}
