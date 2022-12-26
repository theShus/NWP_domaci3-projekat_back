package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.ErrorMessageRepository;
import rs.raf.demo.repositories.MachineRepository;
import rs.raf.demo.repositories.UserRepository;

import javax.persistence.LockModeType;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class MachineService implements MachineServiceInterface {

    private MachineRepository machineRepository;
    private UserRepository userRepository;
    private TaskScheduler taskScheduler;
    private ErrorMessageRepository errorMessageRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository, TaskScheduler taskScheduler, ErrorMessageRepository errorMessageRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.errorMessageRepository = errorMessageRepository;

    }

    @Override
    @Transactional
    public Optional<Machine> findById(Long id) {
        System.err.println("finding");
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

        for (Machine machine : allMachinesByUser) {
            addFlag = 0;

            if (name != null && machine.getName().toLowerCase().contains(name.toLowerCase())) addFlag++; //flag == 1
            else if (name == null) addFlag++;

            if (statuses != null && statuses.contains(machine.getStatus().toString())) addFlag++; //flag == 2
            else if (statuses == null) addFlag++;

            if (dateFrom != null && dateTo != null && machine.getCreationDate().isAfter(dateFrom) && machine.getCreationDate().isBefore(dateTo))
                addFlag++; //flag == 3
            else if (dateFrom == null || dateTo == null) addFlag++;

            if (addFlag == 3) filteredMachines.add(machine);
        }
        return filteredMachines;
    }

    @Override
    public Machine createMachine(String name, String userMail) {
        System.err.println("creating machine");
        return machineRepository.save(new Machine(0L, Status.STOPPED, userRepository.findByMail(userMail), true, name, LocalDate.now()/*, 0*/));
    }

    @Transactional
    public Collection<ErrorMessage> findAllErrorsForUser(String userMail){
        return errorMessageRepository.findAllByMachine_CreatedBy(userRepository.findByMail(userMail));
    }

    @Override
    @Transactional
    public void destroyMachine(Long id) {
        System.err.println("destroying machine");
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
    public void startMachine(Long id, boolean scheduled) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            if(machine.isActive()) {
                if (machine.getStatus() == Status.STOPPED) {
                    System.err.println("Starting machine");
                    Thread.sleep((long) (Math.random() * (15000 - 10000) + 10000));
                    machine.setStatus(Status.RUNNING);
                    machineRepository.save(machine);
                    System.err.println("Machine started");
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The machine's status is not 'STOPPED'.", "START", LocalDate.now(), machine));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The machine is deactivated.", "START",LocalDate.now(), machine));
        }
    }


    @Override
    @Async
    @Transactional
    public void stopMachine(Long id, boolean scheduled) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            if(machine.isActive()) {
                if (machine.getStatus() == Status.RUNNING) {
                    System.err.println("Stopping machine");
                    Thread.sleep((long) (Math.random() * (15000 - 10000) + 10000));
                    machine.setStatus(Status.STOPPED);
                    machineRepository.save(machine);
                    System.err.println("Machine stopped");
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The machine's status is not 'RUNNING'.", "STOP", LocalDate.now(), machine));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The machine is deactivated.", "STOP",LocalDate.now(), machine));
        }
    }

    @Override
    @Async
    @Transactional
    public void restartMachine(Long id, boolean scheduled) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if(optionalMachine.isPresent()) {
            Machine machine = optionalMachine.get();
            if(machine.isActive()) {
                if (machine.getStatus() == Status.RUNNING) {
                    System.err.println("Stopping machine for restart");
                    Thread.sleep((long) (Math.random() * (10000 - 5000) + 5000));
                    machine.setStatus(Status.STOPPED);
                    machineRepository.save(machine);

                    machine = this.findById(id).get();

                    System.err.println("Starting machine for restart");
                    Thread.sleep((long) (Math.random() * (10000 - 5000) + 5000));
                    machine.setStatus(Status.RUNNING);
                    machineRepository.save(machine);
                    System.err.println("Machine restarted");
                } else
                if(scheduled)
                    errorMessageRepository.save(new ErrorMessage(0L, "The machine's status is not 'RUNNING'.", "RESTART", LocalDate.now(), machine));
            } else
            if(scheduled)
                errorMessageRepository.save(new ErrorMessage(0L, "The machine is deactivated.", "RESTART",LocalDate.now(), machine));
        }
    }

    @Override
    @Transactional
    public void scheduleMachine(Long id, String date, String time, String action) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date + " " + time);
        System.err.println("Machine scheduled for " + date1);

        this.taskScheduler.schedule(() -> {
            try {
                switch (action) {
                    case "Start":
                        startMachine(id, true);
                        break;
                    case "Stop":
                        stopMachine(id, true);
                        break;
                    case "Restart":
                        restartMachine(id, true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, date1);
    }

}
