package learn.code.ppmtool.repositories;

import learn.code.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User getById(Long Id);

}
