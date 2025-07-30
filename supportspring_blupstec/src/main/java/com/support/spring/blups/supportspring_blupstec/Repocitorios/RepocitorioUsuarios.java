package com.support.spring.blups.supportspring_blupstec.Repocitorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.support.spring.blups.supportspring_blupstec.entidades.Usuarios;




public interface RepocitorioUsuarios extends JpaRepository<Usuarios,Long>{

        Optional<Usuarios> findByEmail(String email);
}
