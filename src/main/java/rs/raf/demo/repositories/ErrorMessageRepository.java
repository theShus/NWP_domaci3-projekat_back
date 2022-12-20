package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;

import java.util.Collection;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {
    public Collection<ErrorMessage> findAllByMachine_CreatedBy (User user);
}
