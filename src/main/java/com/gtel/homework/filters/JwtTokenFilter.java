package com.gtel.homework.filters;

import com.gtel.homework.entity.User;
import com.gtel.homework.exception.ApplicationException;
import com.gtel.homework.repository.UserRepository;
import com.gtel.homework.utils.ERROR_CODE;
import com.gtel.homework.utils.JwtTokenUtil;

import com.gtel.homework.utils.USER_STATUS;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        }

        // Once we get the token. Validate it
        getTokenAndValidate(username, request, jwtToken);

        filterChain.doFilter(request, response);
    }

    private void getTokenAndValidate(String username, HttpServletRequest request, String jwtToken) {
        if (Objects.nonNull(username)) {
            Optional<String> loggedUsernameOpt = Optional
                    .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .map(Authentication::getName);

            if (loggedUsernameOpt.isEmpty() || !loggedUsernameOpt.get().equals(username)) {
                Optional<User> opt = userRepository.findByUsername(username);

                if (opt.isPresent()) {
                    if (opt.get().getStatus().equals(USER_STATUS.INACTIVE.getValue())) {
                        throw new ApplicationException(ERROR_CODE.ACCOUNT_DISABLED);
                    }
                    Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + opt.get().getRole().name().toUpperCase()));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    opt.get(),
                                    null,
                                    authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
    }
}
