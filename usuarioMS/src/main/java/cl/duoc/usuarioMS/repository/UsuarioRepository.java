package cl.duoc.usuarioMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.usuarioMS.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring genera la consulta a partir del nombre: WHERE LOWER(correo) = LOWER(?)
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}