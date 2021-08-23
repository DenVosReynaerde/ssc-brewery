package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AbstractRestAuthFilter extends AbstractAuthenticationProcessingFilter {

    public AbstractRestAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String username = getUsername(httpServletRequest);
        String password = getPassword(httpServletRequest);

        if(username == null) {
            username = "";
        }
        if(password == null) {
            password = "";
        }

        log.debug("Authenticating user: "+username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        if(StringUtils.hasLength(username)) {
            return this.getAuthenticationManager().authenticate(token);
        } else {
            return null;
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest HttpRequest = (HttpServletRequest) request;
        HttpServletResponse HttpResponse = (HttpServletResponse) response;


        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = attemptAuthentication(HttpRequest, HttpResponse);

            if (authResult != null) {
                successfulAuthentication(HttpRequest, HttpResponse, chain, authResult);
            } else {
                chain.doFilter(HttpRequest, HttpResponse);
            }
        } catch (AuthenticationException e) {
            log.error("Authentication Failed", e);
            unsuccessfulAuthentication(HttpRequest, HttpResponse, e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        log.trace("Failed to process authentication request", failed);
        log.trace("Cleared SecurityContextHolder");

        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    protected abstract String getUsername(HttpServletRequest request);

    protected abstract String getPassword(HttpServletRequest request);
}
