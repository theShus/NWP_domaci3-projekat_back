package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    public Collection<Machine> findAllByCreatedBy(User user);

}
