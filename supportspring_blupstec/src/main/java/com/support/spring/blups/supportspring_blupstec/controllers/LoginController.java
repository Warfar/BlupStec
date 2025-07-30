package com.support.spring.blups.supportspring_blupstec.controllers;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // si quieres pasar datos a la vista de inicio
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.support.spring.blups.supportspring_blupstec.Servicios.UsuariosServicios;
import com.support.spring.blups.supportspring_blupstec.SupportspringBlupstecApplication;
import com.support.spring.blups.supportspring_blupstec.entidades.Usuarios;
import com.support.spring.blups.supportspring_blupstec.entidades.Usuarios.Rol;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

@Controller // Trabajamos con (TEMPLATES)
public class LoginController {

    private final SupportspringBlupstecApplication supportspringBlupstecApplication;

    LoginController(SupportspringBlupstecApplication supportspringBlupstecApplication) {
        this.supportspringBlupstecApplication = supportspringBlupstecApplication;
    }

    @GetMapping("/imagen/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path path = Paths.get(
                    "C:/Users/guada/OneDrive/Desktop/BlupS/supportspring_blupstec/src/main/resources/static/uploads")
                    .resolve(filename);
            Resource recurso = new UrlResource(path.toUri());

            if (recurso.exists() && recurso.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // cambia a IMAGE_PNG si usas .png
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public String paginaPrincipal() {
        return "inicio";
    }

    @Autowired
    private UsuariosServicios usuariosServicios;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesa el formulario de login
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {

              Optional<Usuarios> usuarioOpt = usuariosServicios.buscarEmail(email); // devuelve el usuario encontrado completo

             if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get(); // Sacamos el objeto*(Usuario) de dentro del Optional.

            // Comparación directa, sin hash
            if (usuario.getPasswordHash().equals(password)) {

                System.out.println("\nBienvenido: " + usuario.getUsername());
                System.out.println("foto perfil: " + usuario.getPerfil());
                session.setAttribute("guardarnombre", usuario.getNombre());
                session.setAttribute("usernameSession", usuario.getUsername());
                session.setAttribute("guardarRol", usuario.getRol());
                session.setAttribute("guardarfotoperfil", usuario.getPerfil());
                return "redirect:/dashboard";

            } else {
                redirectAttributes.addFlashAttribute("error", "Contraseña incorrecta");
                System.out.println("Contraseña incorrecta");
                return "redirect:/login";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            System.out.println("Usuario no encontrado");
            return "redirect:/login";
        }
        } catch (Exception e) {
            System.out.println("Error de conexion BD: "+e.getMessage());
            redirectAttributes.addFlashAttribute("error","Errro en el Servidor de la base de datos");
            return "redirect:/servidor";
        }
    }

    @GetMapping("/servidor")
    public String mostrarEstadoServidor(){
        return "servidor";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model html, HttpSession session) {

        String perfil = (String) session.getAttribute("guardarfotoperfil");
        String nombre = (String) session.getAttribute("guardarnombre");
        String Username = (String) session.getAttribute("usernameSession");
        Rol Rol = (Rol) session.getAttribute("guardarRol");

        html.addAttribute("setPerfil", perfil);
        html.addAttribute("setNombre", nombre);
        html.addAttribute("setUsername", Username);
        html.addAttribute("setRol", Rol);

        return "dashboard";
    }

    @GetMapping("/api/usuarios")
    @ResponseBody
    public List<Usuarios> obtenerUsuariosJson() {
        return usuariosServicios.traerListaUsuarios();
    }

    
    @DeleteMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarUsuario(@PathVariable long id){
    Optional<Usuarios> usuarioOpt = usuariosServicios.buscarId(id);
    if(usuarioOpt.isPresent()){
        usuariosServicios.eliminarUsuario(usuarioOpt.get());
         return ResponseEntity.ok().build();
    }else {
        return ResponseEntity.notFound().build();
    }

    }

    @GetMapping("/usuarios")
    public String mostrarUsuarios(Model html, HttpSession session) {

        String perfil = (String) session.getAttribute("guardarfotoperfil");
        String nombre = (String) session.getAttribute("guardarnombre");
        String username = (String) session.getAttribute("usernameSession");
        Rol Rol = (Rol) session.getAttribute("guardarRol");

        html.addAttribute("setNombre", nombre);
        html.addAttribute("setRol", Rol);
        html.addAttribute("setUsername", username);
        html.addAttribute("setPerfil", perfil);

        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String registrarUsuario(
            @RequestParam("nombre") String nombre,
            @RequestParam("password") String passwordHash,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("rol") Rol rol,
            @RequestParam("telefono") String telefono,
            @RequestParam("fotoperfil") MultipartFile foto,
            Model html) {

        Usuarios us = new Usuarios();

        try {

            if (!foto.isEmpty()) {
                String nombreArchivo = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/uploads", nombreArchivo);
                Files.write(ruta, foto.getBytes());
                us.setPerfil(nombreArchivo);
            } else {
                us.setPerfil("default.png");
            }

            us.setNombre(nombre);
            us.setPasswordHash(passwordHash);
            us.setEmail(email);
            us.setUsername(username);
            us.setRol(rol);
            us.setTelefono(telefono);
            us.setActivo(true); // Usuario activo por defecto
            us.setFecha(LocalDateTime.now());

            usuariosServicios.guardarUsuario(us);

        } catch (Exception e) {
            e.printStackTrace();
            html.addAttribute("Error", "Error al registrar un usuario");
            return "errro";
        }

        return "redirect:/usuarios";

    }

    @GetMapping("/tickets")
    public String mostrarTickets(Model html, HttpSession session) {

        String perfil = (String) session.getAttribute("guardarfotoperfil");
        String nombre = (String) session.getAttribute("guardarnombre");
        String usuario = (String) session.getAttribute("usernameSession");
        Rol rol = (Rol) session.getAttribute("guardarRol");

        html.addAttribute("setNombre", nombre);
        html.addAttribute("setUsuario", usuario);
        html.addAttribute("setRol", rol);
        html.addAttribute("setPerfil", perfil);

        return "tickets";
    }

}
