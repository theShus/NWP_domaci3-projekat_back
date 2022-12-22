package rs.raf.demo.services;

import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MachineServiceInterface {
    Machine createMachine(String name, String userMail);
    Optional<Machine> findById(Long id);
    Collection<Machine> getMachinesByUser(String userMail);
    Collection<Machine> searchMachines(String name, List<String> statuses, LocalDate dateFrom, LocalDate dateTo, String userMail);
    void destroyMachine(Long id);
    void startMachine(Long id,boolean scheduled) throws InterruptedException;
    void stopMachine(Long id, boolean scheduled) throws InterruptedException;
    void restartMachine(Long id, boolean scheduled) throws InterruptedException, ParseException;
    void scheduleMachine(Long id, String date, String time, String action) throws ParseException;


//    void addMachineToUser(String mail, Long id);
//    MachineResponse callStartMachine(Long id);
//    MachineResponse callStopMachine(Long id);
//    MachineResponse callRestartMachine(Long id);

//    //Error history
//    void addToErrorHistory(Long id, LocalDateTime date, String action, String message);
//    ErrorHistroy[] getErrorsForUser(User user);
}
