package cl.duoc.usuarioMS.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.usuarioMS.dto.LoginRequest;
import cl.duoc.usuarioMS.dto.UsuarioDTO;
import cl.duoc.usuarioMS.model.TipoUsuario;
import cl.duoc.usuarioMS.model.Usuario;
import cl.duoc.usuarioMS.repository.TipoUsuarioRepository;
import cl.duoc.usuarioMS.repository.UsuarioRepository;
import cl.duoc.usuarioMS.util.HashClave;

@Service
public class UsuarioService {

    public static final String MENSAJE_LOGIN_INVALIDO = "Correo o contraseña incorrectos";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::aDTO).toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
        return aDTO(usuario);
    }

    /** Compara el hash de la clave escrita con el hash guardado. */
    public UsuarioDTO login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(request.getCorreo().trim())
                .orElseThrow(() -> new SecurityException(MENSAJE_LOGIN_INVALIDO));

        if (!usuario.getClaveHash().equals(HashClave.hash(request.getClave()))) {
            // Mismo mensaje si falla el correo o la clave: no revela qué cuentas existen
            throw new SecurityException(MENSAJE_LOGIN_INVALIDO);
        }
        return aDTO(usuario);
    }

    public List<TipoUsuario> listarTipos() {
        return tipoUsuarioRepository.findAll();
    }

    private UsuarioDTO aDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTipoUsuario().getNombre());
    }
}