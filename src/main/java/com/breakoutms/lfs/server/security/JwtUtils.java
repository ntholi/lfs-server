package com.breakoutms.lfs.server.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String TOKEN_SEPERATOR = " ";
	private static final String NAMES = "names";
	private static final String BRANCH_ID = "branch_id";
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
    public String createToken(User user, Integer branchId) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put(NAMES, user.getFullName());
        claims.put(BRANCH_ID, branchId);
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
    
    public static String getAccessToken(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return Arrays.asList(request.getHeader(AUTHORIZATION_HEADER).split(TOKEN_SEPERATOR)).get(1);
        }
        return null;
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
    
    @SuppressWarnings("unchecked")
	public Integer getBranchId(String token) {
       Integer branch = Jwts.parser().setSigningKey(secretKey)
    		   .parseClaimsJws(token).getBody().get(BRANCH_ID, Integer.class);
        return branch;
    }
}