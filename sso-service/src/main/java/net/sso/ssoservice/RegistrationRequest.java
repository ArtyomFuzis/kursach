package net.sso.ssoservice;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegistrationRequest {
    private final String email;
    private final String password;
}
