package it.mat.unical.asde.project2_puzzle.components.controllers;

import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.mat.unical.asde.project2_puzzle.components.services.AccountService;

@Controller
public class UserController{
    @Autowired
    private AccountService accountService;

    @GetMapping("/creationAccount")
    public String creationAccount(@RequestParam String username,@RequestParam String password,HttpSession session,Model model){
        clearAttributes(session);
        if (!(username == "" || password == "")) {
            if (accountService.accountCreated(username, password)) {
                session.setAttribute("username", username);
                return "redirect:/";
            }
            session.setAttribute("creationFailed", "Username already used.");
        } else
            session.setAttribute("creationFailed", "Fields cannot be empty!");
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(HttpSession session,Model model){
        if (session.getAttribute("username") != null) {
            model.addAttribute(accountService.getMatches((String) session.getAttribute("username")));
            return "redirect:/lobby";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password,HttpSession session,Model model){
        clearAttributes(session);
        if (accountService.loginAccepted(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/";
        }
        session.setAttribute("loginFailed", "Username or Password wrong!");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,Model model){
        session.invalidate();
        return "redirect:/";
    }

    private void clearAttributes(HttpSession session){
        Enumeration<String> attributes = session.getAttributeNames();
        String attribute = null;
        while (attributes.hasMoreElements()) {
            attribute = attributes.nextElement();
            session.removeAttribute(attribute);
        }
    }
}