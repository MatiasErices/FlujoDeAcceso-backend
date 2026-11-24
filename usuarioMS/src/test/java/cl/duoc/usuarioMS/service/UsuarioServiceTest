package cl.duoc.usuarioMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.usuarioMS.dto.LoginRequest;
import cl.duoc.usuarioMS.dto.UsuarioDTO;
import cl.duoc.usuarioMS.model.TipoUsuario;
import cl.duoc.usuarioMS.model.Usuario;
import cl.duoc.usuarioMS.repository.TipoUsuarioRepository;
import cl.duoc.usuarioMS.repository.UsuarioRepository;
import cl.duoc.usuarioMS.util.HashClave;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository; // repositorio simulado: no toca MySQL

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService; // service real con los mocks inyectados

    private Usuario camila;

    @BeforeEach
    void setUp() {
        TipoUsuario empleado = new TipoUsuario(1L, "EMPLEADO");
        camila = new Usuario(1L, "Camila R.", "camila.r@duoc.cl", HashClave.hash("demo1234"), empleado);
    }

    @Test
    void login_conClaveCorrecta_devuelveUsuarioConSuRol() {
        // ARRANGE
        when(usuarioRepository.findByCorreoIgnoreCase("camila.r@duoc.cl")).thenReturn(Optional.of(camila));

        // ACT
        UsuarioDTO resultado = usuarioService.login(new LoginRequest("camila.r@duoc.cl", "demo1234"));

        // ASSERT
        assertEquals("Camila R.", resultado.getNombre());
        assertEquals("EMPLEADO", resultado.getRol());
    }

    @Test
    void login_conClaveIncorrecta_lanzaSecurityException() {
        when(usuarioRepository.findByCorreoIgnoreCase("camila.r@duoc.cl")).thenReturn(Optional.of(camila));

        SecurityException error = assertThrows(SecurityException.class,
                () -> usuarioService.login(new LoginRequest("camila.r@duoc.cl", "otra-clave")));

        assertEquals(UsuarioService.MENSAJE_LOGIN_INVALIDO, error.getMessage());
    }

    @Test
    void login_conCorreoInexistente_daElMismoMensaje() {
        when(usuarioRepository.findByCorreoIgnoreCase("nadie@duoc.cl")).thenReturn(Optional.empty());

        SecurityException error = assertThrows(SecurityException.class,
                () -> usuarioService.login(new LoginRequest("nadie@duoc.cl", "demo1234")));

        assertEquals(UsuarioService.MENSAJE_LOGIN_INVALIDO, error.getMessage());
    }

    @Test
    void login_ignoraEspaciosAlrededorDelCorreo() {
        when(usuarioRepository.findByCorreoIgnoreCase("camila.r@duoc.cl")).thenReturn(Optional.of(camila));

        UsuarioDTO resultado = usuarioService.login(new LoginRequest("  camila.r@duoc.cl  ", "demo1234"));

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_inexistente_lanzaNoSuchElementException() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void hash_deDemo1234_esIgualAlDeLaApp() {
        assertEquals("0ead2060b65992dca4769af601a1b3a35ef38cfad2c2c465bb160ea764157c5d",
                HashClave.hash("demo1234"));
    }
}