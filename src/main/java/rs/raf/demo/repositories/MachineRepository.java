package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.raf.demo.model.Machine;
import rs.raf.demo.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

//    public Collection<Machine> findAllByNameContains(String name);

    public Collection<Machine> findAllByCreatedBy(User user);

    @Query("SELECT M FROM Machine M WHERE (M.name = :name OR :name IS NULL) AND (M.status = :status OR :status IS NULL) AND (:dateFrom IS NULL OR :dateTo IS NULL OR(M.creationDate >= :dateFrom OR M.creationDate <= :dateTo)) AND M.createdBy = :id")
    public Collection<Machine> searchMachine (String name, String status, LocalDate dateFrom, LocalDate dateTo, User id);

}
