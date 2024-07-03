package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/login")
    public String login(Login login) {
        if ("admin".equals(login.getUsuario()) && "admin".equals(login.getSenha())) {
            return "redirect:/unidade.html";
        } else {
            return "redirect:/login.html";
        }
    }
}