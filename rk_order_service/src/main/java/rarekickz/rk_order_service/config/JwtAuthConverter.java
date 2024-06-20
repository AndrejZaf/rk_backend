package rarekickz.rk_order_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public JwtAuthConverter() {
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt jwt) {
        final Set<GrantedAuthority> authorities = Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractUserRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Set<? extends GrantedAuthority> extractUserRoles(final Jwt jwt) {
        final Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        final List<String> realmRoles = (List<String>) realmAccess.get("roles");
        if (isNotEmpty(realmRoles)) {
            return realmRoles.stream()
                    .map(role -> new SimpleGrantedAuthority(String.format("%s%s", ROLE_PREFIX, role.toUpperCase())))
                    .collect(Collectors.toSet());
        }

        return emptySet();
    }
}
