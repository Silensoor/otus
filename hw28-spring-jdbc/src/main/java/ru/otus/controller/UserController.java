package ru.otus.controller;

import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.User;
import ru.otus.repository.UserRepository;
import ru.otus.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("randomUser", userService.getRandomUser());
        model.addAttribute("user", new User());
        return "users";
    }

    @PostMapping
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/api/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/api/users";
    }
    @GetMapping("/find/")
    public String getUserById(@RequestParam Long id, Model model) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("randomUser", userService.getRandomUser());
            model.addAttribute("user", new User());
            model.addAttribute("fullUser", user);
        } else {
            model.addAttribute("errorMessage", "Пользователь с указанным id не найден");
        }
        return "users";
    }

}