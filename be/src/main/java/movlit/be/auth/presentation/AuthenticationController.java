package movlit.be.auth.presentation;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.AuthenticationService;
import movlit.be.auth.presentation.dto.RefreshTokenRequest;
import movlit.be.common.filter.dto.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request)
            throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/api/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return authenticationService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/api/token")
    public ResponseEntity<?> exchangeToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        return authenticationService.exchangeToken(code);
    }

}
