# Flujo de Acceso — Backend

Backend de la app Android **Flujo de Acceso**, formado por **dos microservicios** Spring Boot con una base de datos MySQL cada uno. La app los consume con Retrofit.

> **Proyecto académico.** Todos los datos son **ficticios**.

**Asignatura:** DSY1105 Desarrollo de Aplicaciones Móviles, Duoc UC.
**App Android:** https://github.com/Vicente77-f/FlujoDeAcceso_

## Integrantes
- Matías Erices — `usuarioMS`
- Vicente Fernández — `turnoMS`

## Arquitectura

```
App Android (Retrofit)
   ├──► usuarioMS :8081 ──► MySQL usuarios_db
   └──► turnoMS   :8082 ──► MySQL turnos_db
              └── Feign ──► usuarioMS (verifica el rol del supervisor)
```

- **Una base de datos por microservicio.** `turnos_db` guarda solo el `usuarioId`, sin clave foránea a `usuarios_db`.
- **Comunicación entre microservicios:** antes de aceptar una corrección, `turnoMS` le pregunta a `usuarioMS` (con OpenFeign) si el usuario es `SUPERVISOR`.
- **Capas:** `controller` → `service` → `repository` → `model`, con `dto` para lo que entra y sale, y `exception` para los errores.

## Tecnologías
Java 17 · Spring Boot 4.0.10 · Spring Web MVC · Spring Data JPA (Hibernate) · Bean Validation · Lombok · MySQL (XAMPP/MariaDB) · Spring Cloud OpenFeign 2025.1.4 · springdoc-openapi 3.0.0 (Swagger) · JUnit 5 + Mockito.

## Requisitos
- JDK 17 o superior (`java -version`).
- MySQL en el puerto **3306**, usuario `root` sin contraseña (configuración por defecto de XAMPP).
- No hace falta instalar Maven: cada microservicio trae `mvnw`.

## Cómo ejecutar
1. Iniciar **MySQL** en XAMPP.
2. `usuarioMS`:
   ```
   cd usuarioMS
   .\mvnw.cmd spring-boot:run
   ```
3. En otra terminal, `turnoMS`:
   ```
   cd turnoMS
   .\mvnw.cmd spring-boot:run
   ```
Las bases de datos y las tablas se crean solas (`createDatabaseIfNotExist` y `ddl-auto: update`). Los datos de demostración se cargan solo si las tablas están vacías.

Para usar otro puerto de MySQL: `$env:DB_PORT=3307` antes de ejecutar.

## Documentación (Swagger)
- usuarioMS: http://localhost:8081/swagger-ui/index.html
- turnoMS: http://localhost:8082/swagger-ui/index.html

## Endpoints

### usuarioMS (8081)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/usuarios/login` | Inicia sesión. Responde 200 con el usuario y su rol, 401 si la clave es incorrecta o 400 si el formato es inválido |
| GET | `/api/v1/usuarios` | Lista los usuarios (sin contraseñas) |
| GET | `/api/v1/usuarios/dto/{id}` | Usuario por id (lo usa `turnoMS` por Feign) |
| GET | `/api/v1/tipos-usuario` | Tipos de usuario: EMPLEADO y SUPERVISOR |

### turnoMS (8082)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/turnos` | Crea un turno (201) |
| GET | `/api/v1/turnos?fecha=AAAA-MM-DD` | Turnos de un día |
| GET | `/api/v1/turnos/{id}` | Turno por id |
| PUT | `/api/v1/turnos/{id}/cerrar` | Cierra el turno |
| PUT | `/api/v1/turnos/{id}/exportado` | Marca el turno como exportado a CSV |
| DELETE | `/api/v1/turnos/{id}` | Elimina el turno y sus conteos (204) |
| GET | `/api/v1/turnos/{id}/conteos` | Conteos por hora de un turno |
| PUT | `/api/v1/turnos/{id}/conteos/{hora}` | Guarda el conteo de una hora (409 si el turno está cerrado) |
| PUT | `/api/v1/turnos/{id}/conteos/{hora}/correccion` | **Corrección del supervisor**: 403 si no es supervisor, 503 si `usuarioMS` no responde |
| GET | `/api/v1/conteos?fecha=AAAA-MM-DD` | Todos los conteos de un día |

## Reglas de negocio
- Un turno **cerrado** no acepta conteos nuevos. Solo un **supervisor** puede corregirlo, y al hacerlo el turno queda `corregido = true` y `exportado = false`, porque el CSV anterior ya no coincide.
- Las personas en tránsito no pueden superar a las que ingresaron. Ningún valor puede ser negativo.
- La hora del conteo debe estar dentro del turno (AM 10-15, PM 15-20).
- Las contraseñas se guardan como hash SHA-256. Ninguna respuesta las incluye.
- Los errores responden `{"mensaje": "..."}` con el código HTTP correspondiente (400, 401, 403, 404, 409, 503).

## Datos de demostración
- **Usuarios:** 8 empleados y 1 supervisor. Contraseñas: `demo1234` para los empleados y `super1234` para `supervisor@duoc.cl`.
- **Turnos:** 8 turnos del **13-oct-2026** (AM y PM en los accesos A1 a A4), con 40 conteos por hora.

## Pruebas
Con MySQL encendido (la prueba `contextLoads` arranca la aplicación completa):
```
cd usuarioMS
.\mvnw.cmd test
cd ..\turnoMS
.\mvnw.cmd test
```
Pruebas unitarias con Mockito (patrón AAA) de los servicios, los controladores y el manejo de errores, incluida la corrección del supervisor.