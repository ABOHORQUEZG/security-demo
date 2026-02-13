package com.example.security_demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final MessageSource messageSource;

    public WebController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Página principal con i18n
    @GetMapping("/")
    public String index(@RequestParam(name = "lang", required = false) String lang, Model model, Locale locale) {
        
        model.addAttribute("welcome", messageSource.getMessage("welcome", null, locale));
        model.addAttribute("language", messageSource.getMessage("language", null, locale));
        model.addAttribute("spanish", messageSource.getMessage("spanish", null, locale));
        model.addAttribute("english", messageSource.getMessage("english", null, locale));
        model.addAttribute("portuguese", messageSource.getMessage("portuguese", null, locale));
        model.addAttribute("message", messageSource.getMessage("message", null, locale));
        
        return "index";
    }

    // Página pública
    @GetMapping("/public")
    public String publicPage(Model model, Locale locale) {
        addCommonMessages(model, locale);
        model.addAttribute("public_title", messageSource.getMessage("public.title", null, locale));
        model.addAttribute("public_welcome", messageSource.getMessage("public.welcome", null, locale));
        model.addAttribute("public_description", messageSource.getMessage("public.description", null, locale));
        model.addAttribute("public_tryaccess", messageSource.getMessage("public.tryaccess", null, locale));
        model.addAttribute("public_useraccess", messageSource.getMessage("public.useraccess", null, locale));
        model.addAttribute("public_adminaccess", messageSource.getMessage("public.adminaccess", null, locale));
        return "public";
    }

    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model, Locale locale) {
        model.addAttribute("username", authentication.getName());
        addCommonMessages(model, locale);
        model.addAttribute("user_title", messageSource.getMessage("user.title", null, locale));
        model.addAttribute("user_welcome", messageSource.getMessage("user.welcome", null, locale));
        model.addAttribute("user_access", messageSource.getMessage("user.access", null, locale));
        model.addAttribute("user_roles", messageSource.getMessage("user.roles", null, locale));
        model.addAttribute("user_permission", messageSource.getMessage("user.permission", null, locale));
        model.addAttribute("navbar_username", messageSource.getMessage("navbar.username", null, locale));

        // Filtrar solo las autoridades que son roles (empiezan con ROLE_)
        List<String> roles = authentication.getAuthorities().stream()
               // .map(authority -> authority.getAuthority())
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.replace("ROLE_", ""))
                .toList();

        model.addAttribute("roles", roles);
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model, Locale locale) {
        model.addAttribute("username", authentication.getName());
        addCommonMessages(model, locale);
        model.addAttribute("admin_title", messageSource.getMessage("admin.title", null, locale));
        model.addAttribute("admin_welcome", messageSource.getMessage("admin.welcome", null, locale));
        model.addAttribute("admin_fullaccess", messageSource.getMessage("admin.fullaccess", null, locale));
        model.addAttribute("admin_users", messageSource.getMessage("admin.users", null, locale));
        model.addAttribute("admin_sessions", messageSource.getMessage("admin.sessions", null, locale));
        model.addAttribute("admin_uptime", messageSource.getMessage("admin.uptime", null, locale));
        model.addAttribute("admin_permission", messageSource.getMessage("admin.permission", null, locale));
        model.addAttribute("navbar_username", messageSource.getMessage("navbar.username", null, locale));
        return "admin";
    }

    @GetMapping("/login")
    public String loginPage(Model model, Locale locale) {
        model.addAttribute("login_title", messageSource.getMessage("login.title", null, locale));
        model.addAttribute("login_username", messageSource.getMessage("login.username", null, locale));
        model.addAttribute("login_password", messageSource.getMessage("login.password", null, locale));
        model.addAttribute("login_signin", messageSource.getMessage("login.signin", null, locale));
        model.addAttribute("login_error", messageSource.getMessage("login.error", null, locale));
        model.addAttribute("login_logout", messageSource.getMessage("login.logout", null, locale));
        model.addAttribute("login_testusers", messageSource.getMessage("login.testusers", null, locale));
        model.addAttribute("login_role", messageSource.getMessage("login.role", null, locale));
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage(Model model, Locale locale) {
        addCommonMessages(model, locale);
        model.addAttribute("accessdenied_title", messageSource.getMessage("accessdenied.title", null, locale));
        model.addAttribute("accessdenied_denied", messageSource.getMessage("accessdenied.denied", null, locale));
        model.addAttribute("accessdenied_description", messageSource.getMessage("accessdenied.description", null, locale));
        model.addAttribute("accessdenied_reason", messageSource.getMessage("accessdenied.reason", null, locale));
        model.addAttribute("accessdenied_contact", messageSource.getMessage("accessdenied.contact", null, locale));
        model.addAttribute("accessdenied_profile", messageSource.getMessage("accessdenied.profile", null, locale));
        model.addAttribute("accessdenied_home", messageSource.getMessage("accessdenied.home", null, locale));
        return "access-denied";
    }

    private void addCommonMessages(Model model, Locale locale) {
        model.addAttribute("navbar_public", messageSource.getMessage("navbar.public", null, locale));
        model.addAttribute("navbar_user", messageSource.getMessage("navbar.user", null, locale));
        model.addAttribute("navbar_admin", messageSource.getMessage("navbar.admin", null, locale));
        model.addAttribute("navbar_logout", messageSource.getMessage("navbar.logout", null, locale));
        model.addAttribute("navbar_login", messageSource.getMessage("navbar.login", null, locale));
    }
}