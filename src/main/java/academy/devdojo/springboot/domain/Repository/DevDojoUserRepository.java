package academy.devdojo.springboot.domain.Repository;

import academy.devdojo.springboot.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser,Integer> {
    DevDojoUser findByUserName(String userName);
}
