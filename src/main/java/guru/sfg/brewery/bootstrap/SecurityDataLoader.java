package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityDataLoader(AuthorityRepository authorityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }


    private void loadSecurityData() {

        log.debug("Loading admin role");
        Authority adminAuth = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());

        log.debug("Loading user role");
        Authority userAuth = authorityRepository.save(Authority.builder().role("ROLE_USER").build());

        log.debug("Loading customer role");
        Authority customerAuth = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());

        log.debug("Loading foxtrot user");
        userRepository.save(User.builder().username("foxtrot").password(passwordEncoder.encode("secret")).authority(adminAuth).build());

        log.debug("Loading user user");
        userRepository.save(User.builder().username("user").password(passwordEncoder.encode("password")).authority(userAuth).build());

        log.debug("Loading scott user");
        userRepository.save(User.builder().username("scott").password(passwordEncoder.encode("tiger")).authority(customerAuth).build());
    }

}
