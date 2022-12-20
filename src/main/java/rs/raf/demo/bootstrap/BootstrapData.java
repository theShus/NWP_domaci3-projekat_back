package rs.raf.demo.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.demo.model.*;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.UserService;

import java.time.LocalDate;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final MachineService machineService;

    @Autowired
    public BootstrapData(MachineService machineService, UserService userService) {
        this.userService = userService;
        this.machineService = machineService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading data");

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

        machineService.addMachine(new Machine(0L, Status.STOPPED, userService.getUserByMail("ljeremic@raf.rs"), true, "Machine1", LocalDate.now(), 0));
        machineService.addMachine(new Machine(0L, Status.RUNNING, userService.getUserByMail("ljeremic@raf.rs"), true, "Machine2", LocalDate.now(), 0));

        System.out.println("Data loaded");

    }
}
