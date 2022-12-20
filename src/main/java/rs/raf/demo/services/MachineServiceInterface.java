package rs.raf.demo.services;

import org.springframework.data.jpa.repository.Modifying;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MachineServiceInterface {
    Machine createMachine(String name, String userMail);
    Optional<Machine> findById(Long id);
    List<Machine> getMachinesByUser(String username);
    List<Machine> searchMachines(String name, String status, LocalDate dateFrom, LocalDate dateTo, User id);
    void destroyMachine(Long id);
    void startMachine(Long id) throws InterruptedException;
    void stopMachine(Long id) throws InterruptedException;
    void restartMachine(Long id) throws InterruptedException;
    void scheduleMachine(Long id, String date, String time, String action) throws ParseException;


//    void addMachineToUser(String mail, Long id);
//    MachineResponse callStartMachine(Long id);
//    MachineResponse callStopMachine(Long id);
//    MachineResponse callRestartMachine(Long id);

//    //Error history
//    void addToErrorHistory(Long id, LocalDateTime date, String action, String message);
//    ErrorHistroy[] getErrorsForUser(User user);
}
