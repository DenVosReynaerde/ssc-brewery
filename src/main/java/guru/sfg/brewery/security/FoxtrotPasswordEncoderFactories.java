package guru.sfg.brewery.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * custom password encoder factory class, this is a copy of the default factory, but with all unused encoders removed.
 * Also added a custom bcrypt15 encoder
 */
public class FoxtrotPasswordEncoderFactories {

    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt11";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder(11));
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    //don't instantiate class
    private FoxtrotPasswordEncoderFactories() {
    }
}
