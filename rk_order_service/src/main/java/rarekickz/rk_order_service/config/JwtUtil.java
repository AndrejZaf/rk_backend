package rarekickz.rk_order_service.config;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class JwtUtil {

    public static String getJwtClaim(final String key) {
        if (nonNull(SecurityContextHolder.getContext().getAuthentication()) && SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } else if (isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return "SYSTEM";
        }

        final Map<String, Object> claims = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims();
        return (String) claims.get(key);
    }
}
