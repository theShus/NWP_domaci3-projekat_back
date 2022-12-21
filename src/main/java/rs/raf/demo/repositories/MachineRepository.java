package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;

import javax.crypto.Mac;
import javax.persistence.LockModeType;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Collection<Machine> findAllByCreatedBy(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<Machine> findById(Long id);

}
