package UnfinishedObjectsTracker.repository;


import UnfinishedObjectsTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}
