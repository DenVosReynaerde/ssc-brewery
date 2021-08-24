package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void testBCrypt15() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder(15);

        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));

        System.out.println(bCrypt.encode("tiger"));
    }

    @Test
    void testBCrypt() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();

        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));

        System.out.println(bCrypt.encode("tiger"));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();

        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));

        System.out.println(sha256.encode("password"));
    }

    /**
     * encoding using Ldap SHA encoder. each time a password is encoded the hashed value is different/
     */
    @Test
    void testLDAP() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));

        String encodedPassword = ldap.encode(PASSWORD);

        assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        //plaintext encoding of the password (no encoding)
        System.out.println(noOp.encode(PASSWORD));

    }

    /**
     * basic examples of simple MD5 hashing of passwords. not recommended for use.
     */
    @Test
    void hashingExample() {
        //just a basic hash, no salt, no changing hashes, hash is always the same for identical passwords.
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        //add salt to make the password harder to guess, still the same hash for an identical password.
        String salted = PASSWORD+"ARandomSaltValue";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));

        
    }
}
