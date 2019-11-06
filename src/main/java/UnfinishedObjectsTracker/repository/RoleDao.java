package UnfinishedObjectsTracker.repository;

import UnfinishedObjectsTracker.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleDao extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

}
