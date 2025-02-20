package movlit.be.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetailsService;
import movlit.be.common.util.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MyMemberDetailsService myMemberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Optional<String> jwtOptional = extractJwtFromHeader(request);

        if (jwtOptional.isPresent()) {
            String jwt = jwtOptional.get();
            Optional<String> emailOptional = extractEmail(jwt, response);

            if (emailOptional.isEmpty()) {
                return;
            }

            String email = emailOptional.get();

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = myMemberDetailsService.loadUserByUsername(email);
                if (!authenticateUser(userDetails, jwt, request, response)) {
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private Optional<String> extractJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return Optional.of(authorizationHeader.substring(7));
        }

        return Optional.empty();
    }

    private Optional<String> extractEmail(String jwt, HttpServletResponse response) throws IOException {
        try {
            return Optional.ofNullable(jwtTokenUtil.extractEmail(jwt));
        } catch (ExpiredJwtException e) {
            setUnauthorizedResponse(response, "Token Expired");
        } catch (Exception e) {
            setUnauthorizedResponse(response, "Invalid Token");
        }

        return Optional.empty();
    }

    private boolean authenticateUser(UserDetails userDetails, String jwt,
                                     HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (jwtTokenUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken token = createAuthenticationToken(userDetails, request);
                SecurityContextHolder.getContext().setAuthentication(token);
                return true;
            } else {
                setUnauthorizedResponse(response, "Invalid Token");
                return false;
            }
        } catch (ExpiredJwtException e) {
            setUnauthorizedResponse(response, "Token Expired");
            return false;
        }
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails,
                                                                          HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return token;
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

}
