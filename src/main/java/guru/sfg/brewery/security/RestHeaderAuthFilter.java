package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestHeaderAuthFilter extends AbstractRestAuthFilter{

    public RestHeaderAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getUsername(HttpServletRequest request) {
        return request.getHeader("Api-Key");
    }

    @Override
    protected String getPassword(HttpServletRequest request) {
        return request.getHeader("Api-Secret");
    }
}
