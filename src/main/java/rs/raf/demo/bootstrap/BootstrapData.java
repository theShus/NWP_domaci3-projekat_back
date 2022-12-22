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

        userService.saveRole(new Role(null, "can_search_machines"));
        userService.saveRole(new Role(null, "can_start_machines"));
        userService.saveRole(new Role(null, "can_stop_machines"));
        userService.saveRole(new Role(null, "can_restart_machines"));
        userService.saveRole(new Role(null, "can_create_machines"));
        userService.saveRole(new Role(null, "can_destroy_machines"));
        userService.saveRole(new Role(null, "can_schedule_machines"));

        userService.addUser(new User(null, "Luka", "Jeremic", "ljeremic@raf.rs", "1234"));
        userService.addUser(new User(null, "Stefan", "Antic", "santic@raf.rs", "1234"));

        userService.addRoleToUser("ljeremic@raf.rs", "can_read_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_create_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_update_users");
        userService.addRoleToUser("ljeremic@raf.rs", "can_delete_users");
        //machine roles
        userService.addRoleToUser("ljeremic@raf.rs", "can_search_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_start_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_stop_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_restart_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_create_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_destroy_machines");
        userService.addRoleToUser("ljeremic@raf.rs", "can_schedule_machines");

        userService.addRoleToUser("santic@raf.rs", "can_read_users");
        userService.addRoleToUser("santic@raf.rs", "can_update_users");
        userService.addRoleToUser("santic@raf.rs", "can_search_machines");

        machineService.createMachine("Machine1","ljeremic@raf.rs");
        machineService.createMachine("Machine2","ljeremic@raf.rs");
        machineService.createMachine("Machine3","santic@raf.rs");


        System.out.println("Data loaded");

    }
}
