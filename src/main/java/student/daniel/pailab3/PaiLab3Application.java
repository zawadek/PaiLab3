package student.daniel.pailab3;

import student.daniel.pailab3.dao.UserDao;
import student.daniel.pailab3.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PaiLab3Application {

    @Autowired
    private UserDao dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(PaiLab3Application.class, args);
    }

    @PostConstruct
    public void init() {
        dao.save(new User("Piotr", "Piotrowski","admin",
                passwordEncoder.encode("admin")));
        dao.save(new User("Ania", "Annowska","ania",
                passwordEncoder.encode("ania")));
    }

}
