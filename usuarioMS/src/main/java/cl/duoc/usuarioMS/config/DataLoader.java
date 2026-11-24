package cl.duoc.usuarioMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.duoc.usuarioMS.model.TipoUsuario;
import cl.duoc.usuarioMS.model.Usuario;
import cl.duoc.usuarioMS.repository.TipoUsuarioRepository;
import cl.duoc.usuarioMS.repository.UsuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    // SHA-256 de "demo1234" y de "super1234": en el código solo aparece el hash
    private static final String HASH_DEMO = "0ead2060b65992dca4769af601a1b3a35ef38cfad2c2c465bb160ea764157c5d";
    private static final String HASH_SUPERVISOR = "e80b9b5a428eaa804690ad5096312c0aa713f6253da6aa23784b1b2867e37472";

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {

        if (tipoUsuarioRepository.count() == 0) {

            TipoUsuario empleado = tipoUsuarioRepository.save(new TipoUsuario(null, "EMPLEADO"));
            TipoUsuario supervisor = tipoUsuarioRepository.save(new TipoUsuario(null, "SUPERVISOR"));

            // El orden importa: los ids 1 a 9 coinciden con la app y con los turnos demo de turnoMS
            usuarioRepository.save(new Usuario(null, "Camila R.", "camila.r@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Diego M.", "diego.m@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Supervisora Demo", "supervisor@duoc.cl", HASH_SUPERVISOR, supervisor));
            usuarioRepository.save(new Usuario(null, "Sofía L.", "sofia.l@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Tomás V.", "tomas.v@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Javiera P.", "javiera.p@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Benjamín O.", "benjamin.o@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Martina C.", "martina.c@duoc.cl", HASH_DEMO, empleado));
            usuarioRepository.save(new Usuario(null, "Ignacio S.", "ignacio.s@duoc.cl", HASH_DEMO, empleado));

            System.out.println("✅ usuarioMS — datos de demostración cargados");
        } else {
            System.out.println("ℹ️ usuarioMS — los datos ya existían, no se insertaron");
        }
    }
}