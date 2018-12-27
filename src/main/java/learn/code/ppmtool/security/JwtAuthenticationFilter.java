package learn.code.ppmtool.security;

import learn.code.ppmtool.domain.User;
import learn.code.ppmtool.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private String getJwtFromRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(SecurityConstants.HEADER_STRING);

        if (StringUtils.hasText(token) && StringUtils.startsWithIgnoreCase(token, SecurityConstants.TOKEN_PREFIX)) {
            return token.split(" ")[1];
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = this.getJwtFromRequest(httpServletRequest);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Long userId = jwtTokenProvider.getUserIdFromJwt(token);
                User user = customUserDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        } catch (Exception e) {
            logger.error("Could not set user authentication");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
