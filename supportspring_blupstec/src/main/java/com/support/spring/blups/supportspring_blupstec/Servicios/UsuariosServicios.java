package com.support.spring.blups.supportspring_blupstec.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; // <- importante
import org.springframework.stereotype.Service;

import com.support.spring.blups.supportspring_blupstec.entidades.Usuarios;
import com.support.spring.blups.supportspring_blupstec.Repocitorios.RepocitorioUsuarios;

@Service
public class UsuariosServicios {

    @Autowired // <- Esta anotación inyecta la dependencia automáticamente
    private RepocitorioUsuarios repocitorioUsuarios;

    // Método para buscar un usuario por correo (email)
    public Optional<Usuarios> buscarEmail(String email) {
        return repocitorioUsuarios.findByEmail(email);
    }

    public Optional<Usuarios> buscarId(Long id){
        return repocitorioUsuarios.findById(id);
    }

    public void eliminarUsuario(Usuarios usuarios){
        repocitorioUsuarios.delete(usuarios);
    }

    public Optional<Usuarios> buscarId(long id){
        return repocitorioUsuarios.findById(id);
    }
    public void guardarUsuario(Usuarios usuario) {
        repocitorioUsuarios.save(usuario);
    }


    public List<Usuarios> traerListaUsuarios() {
        // Este método usa el repositorio para traer todos los usuarios (consulta a la BD)
        return repocitorioUsuarios.findAll();
    }


}
