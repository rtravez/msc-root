# MSC Users Service

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

## Calidad

El workflow de CI ejecuta `mvn -B clean verify` con Java 21. Para SonarQube,
configure `SONAR_TOKEN` en el entorno de CI y ejecute:

```bash
mvn verify sonar:sonar -Dsonar.token="$SONAR_TOKEN"
```
