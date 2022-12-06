package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.services.UserService;

@Component
public class BootstrapData implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public BootstrapData(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        userService.saveRole(new Role(null, "can_read_users"));
        userService.saveRole(new Role(null, "can_create_users"));
        userService.saveRole(new Role(null, "can_update_users"));
        userService.saveRole(new Role(null, "can_delete_users"));

        userService.addUser(new User(null, "Luka", "Jeremic", "ljeremic@raf.rs", "1234"));
        userService.addUser(new User(null, "Stefan", "Antic", "santic@raf.rs", "1234"));

        userService.addRoleToUser("ljeremic@raf.rs", "can_read_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_create_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_update_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_delete_users");

        userService.addRoleToUser("santic@raf.rs", "can_read_users");
        userService.addRoleToUser("santic@raf.rs", "can_update_users");

    }
}
