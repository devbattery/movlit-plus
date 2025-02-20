package movlit.be.auth.application.service;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import movlit.be.auth.domain.repository.AuthCodeStorage;
import movlit.be.auth.domain.repository.RefreshTokenStorage;
import movlit.be.common.filter.dto.AuthenticationRequest;
import movlit.be.common.filter.dto.AuthenticationResponse;
import movlit.be.common.util.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthCodeStorage authCodeStorage;
    private final RefreshTokenStorage refreshTokenStorage;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        String email = request.getEmail();
        String password = request.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new MemberNotFoundException();
        }

        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public ResponseEntity<?> refreshToken(String refreshToken) {
        String email = jwtTokenUtil.extractEmail(refreshToken);

        if (!jwtTokenUtil.validateToken(refreshToken, email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh Token");
        }

        String newAccessToken = jwtTokenUtil.generateAccessToken(email);
        return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken));
    }

    public ResponseEntity<?> exchangeToken(String code) {
        String email = authCodeStorage.fetchEmailForCode(code);

        if (Objects.isNull(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "잘못된 code입니다. code = " + code));
        }

        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);

        refreshTokenStorage.saveRefreshToken(email, refreshToken);
        authCodeStorage.removeCode(code);

        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }

}
