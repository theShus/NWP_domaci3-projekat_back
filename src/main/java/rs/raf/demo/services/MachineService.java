package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import rs.raf.demo.model.enums.Status;
import rs.raf.demo.repositories.MachineRepository;

import javax.crypto.Mac;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MachineService implements MachineServiceInterface{

    private MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    public Machine addMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    public Machine saveMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<Machine> findById(Long id) {
        System.out.println("finding");
        return machineRepository.findById(id);
    }

    @Override
    public List<Machine> getMachinesByUser(String username) {
        return null;
    }

    @Override
    public List<Machine> searchMachines(String name, String status, LocalDate dateFrom, LocalDate dateTo, User id) {
        return null;
    }

    @Override
    public void deleteMachine(Long id) {

    }

    @Override
    @Async
    public void startMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
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
    public void stopMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
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
    @Modifying
    public void restartMachine(Long id) throws InterruptedException {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        if (optionalMachine.isPresent()){
            Machine machine = optionalMachine.get();
            if (machine.getStatus() != Status.RUNNING) return;

            System.err.println("Stopping machine for restart");
            Thread.sleep((long)(Math.random() * (10000 -5000) + 5000));
            machine.setStatus(Status.STOPPED);
            machineRepository.save(machine);

            machine = machineRepository.findById(id).get();

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
