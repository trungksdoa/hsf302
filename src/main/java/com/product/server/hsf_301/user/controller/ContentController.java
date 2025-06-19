package com.product.server.hsf_301.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class ContentController {
    @GetMapping("/req/login")
        public String login(Model model){
        //model.addAttribute("content", "login");
        return "login";
   }


    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

   @GetMapping("/home")
    public String home(){
        return "home";
   }

}
