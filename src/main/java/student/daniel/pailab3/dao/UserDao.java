package student.daniel.pailab3.dao;

import student.daniel.pailab3.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
    public User findByLogin(String login);
}
