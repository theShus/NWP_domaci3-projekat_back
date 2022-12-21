package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.repositories.UserRepository;

import javax.persistence.LockModeType;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service
public class MachineService implements MachineServiceInterface{

    private MachineRepository machineRepository;
    private UserRepository userRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<Machine> findById(Long id) {
        System.out.println("finding");
        return machineRepository.findById(id);
    }

    @Override
    @Transactional
    public Collection<Machine> getMachinesByUser(String userMail) {
        return machineRepository.findAllByCreatedBy(userRepository.findByMail(userMail));
    }

    @Override
    @Transactional
    public Collection<Machine> searchMachines(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail) {
        ArrayList<Machine> allMachinesByUser = (ArrayList<Machine>) getMachinesByUser(userMail);
        ArrayList<Machine> filteredMachines = new ArrayList<>();
        int addFlag;

        for (Machine machine: allMachinesByUser){
            addFlag = 0;

            if (name != null && machine.getName().toLowerCase().contains(name.toLowerCase())) addFlag++; //flag == 1
            else if (name == null) addFlag++;

            if (statuses != null && statuses.contains(machine.getStatus().toString())) addFlag++; //flag == 2
            else if (statuses == null) addFlag++;

            if(dateFrom != null && dateTo != null && machine.getCreationDate().isAfter(dateFrom) && machine.getCreationDate().isBefore(dateTo)) addFlag++; //flag == 3
            else if (dateFrom == null || dateTo == null) addFlag++;

            if (addFlag == 3) filteredMachines.add(machine);
        }
            return filteredMachines;
    }

    @Override
    public Machine createMachine(String name, String userMail) {
        return machineRepository.save(new Machine(0L, Status.STOPPED, userRepository.findByMail(userMail), true, name, LocalDate.now()/*, 0*/));
    }

    @Override
    @Transactional
    public void destroyMachine(Long id) {
        Optional<Machine> optionalMachine = this.findById(id);
        if (optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            if (machine.getStatus() != Status.STOPPED) return;
            machine.setActive(false);
            machineRepository.save(machine);
        }
    }

    @Override
    @Async
    @Transactional
    public void startMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = this.findById(id);
        if (optionalMachine.isPresent()){
            Machine machine = optionalMachine.get();
            if (machine.getStatus() != Status.STOPPED) return;

            System.err.println("Starting machine");
            Thread.sleep((long)(Math.random() * (15000 -10000) + 10000));
            machine.setStatus(Status.RUNNING);
            machineRepository.save(machine);
            System.err.println("Machine started");
        }
    }

    @Override
    @Async
    @Transactional
    public void stopMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = this.findById(id);
        if (optionalMachine.isPresent()){
            Machine machine = optionalMachine.get();
            if (machine.getStatus() != Status.RUNNING) return;

            System.err.println("Stopping machine");
            Thread.sleep((long)(Math.random() * (15000 -10000) + 10000));
            machine.setStatus(Status.STOPPED);
            machineRepository.save(machine);
            System.err.println("Machine stopped");
        }
    }

    @Override
    @Async
    @Transactional
    public void restartMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = this.findById(id);
        if (optionalMachine.isPresent()){
            Machine machine = optionalMachine.get();
            if (machine.getStatus() != Status.RUNNING) return;

            System.err.println("Stopping machine for restart");
            Thread.sleep((long)(Math.random() * (10000 -5000) + 5000));
            machine.setStatus(Status.STOPPED);
            machineRepository.save(machine);

            machine = this.findById(id).get();

            System.err.println("Starting machine for restart");
            Thread.sleep((long)(Math.random() * (10000 -5000) + 5000));
            machine.setStatus(Status.RUNNING);
            machineRepository.save(machine);
            System.err.println("Machine restarted");
        }
    }

    @Override
    public void scheduleMachine(Long id, String date, String time, String action) throws ParseException {

    }
}
