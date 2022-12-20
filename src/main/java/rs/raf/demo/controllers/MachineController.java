package rs.raf.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.services.MachineService;
import rs.raf.demo.services.UserService;

import javax.websocket.server.PathParam;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/machines")
public class MachineController {
    private final MachineService machineService;
    private final UserService userService;

    @Autowired
    public MachineController(MachineService machineService, UserService userService) {
        this.machineService = machineService;
        this.userService = userService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public Machine createMachine(@PathParam("name") String name, @PathParam("mail") String mail){
        return machineService.createMachine(name, mail);
    }

    @DeleteMapping(value = "/delete/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        machineService.destroyMachine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startMachine(@PathVariable Long id) throws InterruptedException {

        Optional<Machine> optionalMachine = machineService.findById(id);

        if(optionalMachine.isPresent() && optionalMachine.get().getStatus() == Status.STOPPED) {
            machineService.startMachine(id);
            return ResponseEntity.ok(optionalMachine.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopMachine(@PathVariable Long id) throws InterruptedException {

        Optional<Machine> optionalMachine = machineService.findById(id);

        if(optionalMachine.isPresent() && optionalMachine.get().getStatus() == Status.RUNNING) {
            machineService.stopMachine(id);
            return ResponseEntity.ok(optionalMachine.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartMachine(@PathVariable Long id) throws InterruptedException {

        Optional<Machine> optionalMachine = machineService.findById(id);

        if(optionalMachine.isPresent() && optionalMachine.get().getStatus() == Status.RUNNING) {
            machineService.restartMachine(id);
            return ResponseEntity.ok(optionalMachine.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
