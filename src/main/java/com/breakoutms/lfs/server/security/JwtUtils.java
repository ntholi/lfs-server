package com.breakoutms.lfs.server.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.breakoutms.lfs.server.user.model.RoleClaim;
import com.breakoutms.lfs.server.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility Class for common Java Web Token operations
 */
@Component
public class JwtUtils {

	private static final String NAMES = "names";
	private static final String SYNC_NO = "sync_no";
	public static final String ROLE_PREFIX = "ROLE_";
	public static final String BEARER = "Bearer";
    private static final String ROLES_KEY = "roles";

    private final String secretKey;
    private final long validityInMilliseconds;

    @Autowired
    public JwtUtils(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}")long validityInMilliseconds) {

        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    /**
     * Create JWT string given username and roles.
     *
     * @param username
     * @param roles
     * @return jwt string
     */
    public String createToken(User user, short syncNo) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put(NAMES, user.getFullnames());
        claims.put(SYNC_NO, syncNo);
        claims.put(ROLES_KEY, user.getRoles().stream()
        		.map(RoleClaim::new)
        		.collect(Collectors.toList())
        );
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Validate the JWT String
     *
     * @param token JWT string
     * @return true if valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get the username from the token string
     *
     * @param token jwt
     * @return username
     */
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get the roles from the token string
     *
     * @param token jwt
     * @return username
     */
    @SuppressWarnings("unchecked")
	public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>>  roleClaims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().get(ROLES_KEY, List.class);
        
        List<GrantedAuthority> auths = new ArrayList<>();
        for (Map<String, String> map : roleClaims) {
			String name = map.get("name");
			String privileges = map.get("privileges");
			
			auths.add(new SimpleGrantedAuthority(ROLE_PREFIX+name));
			if(privileges != null && !privileges.isBlank()) {
				String[] array = RoleClaim.privilegesFromString(privileges);
				for (int i = 0; i < array.length; i++) {
					auths.add(new SimpleGrantedAuthority(array[i]));
				}
			}
		}
        return auths;
    }
}