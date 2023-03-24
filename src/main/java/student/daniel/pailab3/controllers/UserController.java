package student.daniel.pailab3.controllers;

import org.springframework.validation.BindingResult;
import student.daniel.pailab3.dao.UserDao;
import student.daniel.pailab3.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao dao;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model m) {
        m.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPagePOST(@ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || dao.findByLogin(user.getLogin()) != null){
            bindingResult.rejectValue(
                    "login",
                    "error.userController",
                    "Login już istnieje w bazie danych."
            );
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {
        m.addAttribute("user", dao.findByLogin(principal.getName()));
        return "profile";
    }

    @GetMapping("/users")
    public String getAllUsers(Model m) {
        m.addAttribute("users", dao.findAll());
        return "users";
    }

    @GetMapping("/edit")
    public String editPage(Model m, Principal principal) {
        m.addAttribute("user", dao.findByLogin(principal.getName()));
        return "edit";
    }
    @PostMapping("/edit")
    public String editUser(@ModelAttribute @Valid User user, BindingResult bindingResult, Principal principal) {
        try {
            if (bindingResult.hasErrors() || dao.findByLogin(user.getLogin()) != null) {
                bindingResult.rejectValue(
                        "login",
                        "error.userController",
                        "Login już istnieje w bazie danych."
                );
                return "edit";
            }

            User currentLoggedUser = dao.findByLogin(principal.getName());

            currentLoggedUser.setName(user.getName());
            currentLoggedUser.setSurname(user.getSurname());
            currentLoggedUser.setLogin(user.getLogin());

            if (!Objects.equals(user.getPassword(), ""))
                currentLoggedUser.setPassword(passwordEncoder.encode(user.getPassword()));

            dao.save(currentLoggedUser);

            return "redirect:/profile";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "edit";
        }
    }

    @GetMapping("/delete")
    public String deleteUser(Principal principal) {
        dao.delete(dao.findByLogin(principal.getName()));
        return "redirect:/logout";
    }
}
