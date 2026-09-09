# MSC Service

Microservicio Spring Boot para la gestión de usuarios y personas. El proyecto
usa Java 21, Maven y PostgreSQL en ejecución normal.

## Requisitos

- JDK 21
- Maven 3.9+
- PostgreSQL para los perfiles `dev` y `prod`
- Un proveedor OAuth2/OIDC que emita JWT para proteger la API

## Módulos

- `msc-dto`: contratos y validaciones de entrada/salida.
- `msc-client`: entidades, repositorios y mapeos de persistencia.
- `msc-core`: servicios, seguridad y lógica de negocio.
- `msc-service`: aplicación ejecutable, configuración y empaquetado Docker.

## Comandos

Desde la raíz del proyecto:

```bash
mvn clean verify
mvn -pl msc-service -am spring-boot:run -Dspring-boot.run.profiles=dev
mvn -pl msc-service -am package
```

La aplicación se inicia normalmente en `http://localhost:8081`. La
documentación OpenAPI, cuando está habilitada, está disponible en
`/swagger-ui.html`.

## Configuración

No se deben guardar credenciales ni tokens en Git. Configure mediante
variables de entorno los valores de base de datos y OAuth2 requeridos por el
perfil activo. Los perfiles disponibles son `dev`, `test` y `prod`.

Para ejecutar pruebas usando una base H2 en memoria:

```bash
mvn -pl msc-core -am test
```

## Contrato de API

La aplicación usa el context path `/mscServices`. Por tanto, la URL base local
es `http://localhost:8081/mscServices`.

Todas las rutas de la API, salvo las indicadas como públicas en la sección de
seguridad, requieren un token OAuth2 Bearer en el encabezado:

```http
Authorization: Bearer <jwt>
```

### Permisos por endpoint

| Método | Ruta | Permiso |
| --- | --- | --- |
| `GET` | `/api/users` | JWT con `ROLE_ADMIN` |
| `GET` | `/api/users?identification={identification}` | JWT con `ROLE_ADMIN` |
| `GET` | `/api/users/{id}` | JWT con `ROLE_ADMIN` |
| `POST` | `/api/users` | JWT con `ROLE_ADMIN` |
| `PUT` | `/api/users/{id}` | JWT con `ROLE_ADMIN` |
| `DELETE` | `/api/users/{id}` | JWT con `ROLE_ADMIN` |
| `GET` | `/api/validations/identification/{identification}` | JWT válido |
| `GET` | `/api/validations/ruc/{ruc}` | JWT válido |

El rol se obtiene del claim `roles` del JWT. El valor debe ser exactamente
`ROLE_ADMIN`; no se añade ningún prefijo automáticamente.

Endpoints públicos adicionales:

- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/actuator/health`
- `/actuator/info`

### Usuarios

`GET /api/users` acepta los parámetros de paginación `page` y `size`. `page`
inicia en `0`, `size` tiene un valor por defecto de `20` y el servicio limita
el tamaño máximo a `100`. El parámetro `sort` no se aplica actualmente.

| Parámetro | Tipo | Obligatorio | Descripción |
| --- | --- | --- | --- |
| `page` | entero | No | Página solicitada, inicia en `0`. |
| `size` | entero | No | Cantidad de registros; por defecto `20`, máximo `100`. |

Ejemplo:

```http
GET /mscServices/api/users?page=0&size=20
```

`POST /api/users` y `PUT /api/users/{id}` reciben un `UserRequest` JSON:

```json
{
	"identification": "1712345678",
	"name": "Ana",
	"lastname": "Perez",
	"address": "Av. Principal 123",
	"telephone": "0999999999",
	"gender": "F",
	"age": 30,
	"username": "ana.perez",
	"password": "secreto-123"
}
```

Restricciones de entrada:

- `identification`: obligatorio, solo dígitos, máximo 10 caracteres.
- `name` y `lastname`: obligatorios, máximo 255 caracteres.
- `address`: opcional, máximo 255 caracteres.
- `telephone`: opcional, exactamente 10 dígitos.
- `age`: opcional, entre `0` y `150`.
- `username`: obligatorio, máximo 20 caracteres.
- `password`: obligatorio, entre 8 y 60 caracteres.

Los identificadores, campos de auditoría y `status` son gestionados por el
servidor y no deben enviarse como parte del contrato funcional.

Las respuestas exitosas usan este envelope:

```json
{
	"code": 200,
	"message": "Usuario encontrado con éxito",
	"errors": null,
	"data": {
		"userId": 1,
		"username": "ana.perez",
		"name": "Ana",
		"lastname": "Perez",
		"address": "Av. Principal 123",
		"telephone": "0999999999",
		"identification": "1712345678",
		"gender": "F",
		"age": 30,
		"status": true
	}
}
```

La contraseña nunca forma parte de `UserResponse`.

| Operación | Éxito | Otros resultados |
| --- | --- | --- |
| Listar usuarios | `200`, `data` es una página; si está vacía, `message` es `No existen usuarios` | `401`, `403` |
| Buscar por identificación | `200` | `404`, `401`, `403` |
| Buscar por ID | `200` | `404`, `401`, `403` |
| Crear usuario | `201` | `400`, `409`, `401`, `403` |
| Actualizar usuario | `200` | `400`, `404`, `409`, `401`, `403` |
| Eliminar usuario | `200` | `404`, `401`, `403` |

El `DELETE` devuelve el mismo envelope, con `data` sin valor.

### Validaciones

`GET /api/validations/identification/{identification}` y
`GET /api/validations/ruc/{ruc}` devuelven un booleano en `data`:

```json
{
	"code": 200,
	"message": "Identificación validada con éxito",
	"errors": null,
	"data": true
}
```

### Errores

Los errores de negocio y validación usan `ProblemDetail` (RFC 9457), no el
envelope de éxito:

```json
{
	"type": "about:blank",
	"title": "Bad Request",
	"status": 400,
	"detail": "Error de validación en los campos",
	"errors": [
		"username: no debe estar vacío"
	]
}
```

| Estado | Significado |
| --- | --- |
| `400` | JSON, parámetros o campos no válidos |
| `401` | Falta el JWT o no es válido |
| `403` | El JWT es válido, pero no tiene el permiso requerido |
| `404` | Recurso no encontrado |
| `409` | Conflicto de integridad o identificación duplicada |
| `500` | Error inesperado del servidor |

Los errores `401` y `403` son generados por Spring Security y pueden no usar
el mismo formato `ProblemDetail` del manejador global. Los consumidores deben
usar el código HTTP como contrato principal y tratar `detail` y `errors` como
información adicional.

## Calidad

El workflow de CI ejecuta `mvn -B clean verify` con Java 21. Para SonarQube,
configure `SONAR_TOKEN` en el entorno de CI y ejecute:

```bash
mvn verify sonar:sonar -Dsonar.token="$SONAR_TOKEN"
```
