# spring-boot-webflux-security

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-webflux-security/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-webflux-security/actions/workflows/maven.yml)

Stateless JWT authentication for a Spring WebFlux application: users and roles live in MongoDB, clients exchange
credentials for a token on `/authorize`, and every other call is authorized from the `Authorization` header.

## Tech stack

| Component | Version                          |
|-----------|----------------------------------|
| Java      | 25                               |
| Spring Boot | 4.1.1                          |
| Spring WebFlux + Spring Security | reactive stack |
| MongoDB   | 8.0.1 (reactive driver)          |
| JJWT      | 0.13.0                           |
| Maven     | wrapper included (`./mvnw`)      |

## Prerequisites

- JDK 25
- Docker (for MongoDB)

## Running

Start MongoDB:

```bash
docker compose up -d
```

Then run the application:

```bash
./mvnw spring-boot:run
```

The app listens on `http://localhost:8080`. On startup it seeds the `webflux` database with two roles
(`ROLE_ADMIN`, `ROLE_USER`) and two users — the seeding is idempotent, so restarts never duplicate data.

| Username | Password   | Roles                   |
|----------|------------|-------------------------|
| `admin`  | `password` | `ROLE_ADMIN`, `ROLE_USER` |
| `user`   | `password` | `ROLE_USER`             |

## API

### Get a token

```bash
curl -X POST http://localhost:8080/authorize \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"password"}'
```

Response — the token is also returned in the `Authorization` response header:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTc4OTM2MTU0NH0..."
}
```

Bad credentials return `401 Unauthorized`. Tokens are signed with HS512 and are valid for 24 hours.

### Call a secured endpoint

```bash
curl "http://localhost:8080/api/hello?name=world" \
  -H "Authorization: Bearer ${TOKEN}"
```

Returns the login of the authenticated user:

```
admin
```

Without a valid token the same call returns `401 Unauthorized`.

### Public and admin-only paths

- Public: `/authorize/**`, `/actuator/health`, `/actuator/info`, `/resources/**`, `/webjars/**`, `/favicon.ico`
- `ROLE_ADMIN` only: every other actuator endpoint
- Authenticated: everything else

## Build and test

```bash
./mvnw clean package
```

The `contextLoads` test boots the full application context, so a MongoDB instance must be reachable on
`localhost:27017`. CI starts one as a service container — see `.github/workflows/maven.yml`.

## Configuration

`src/main/resources/application.properties`:

```properties
spring.mongodb.host=localhost
spring.mongodb.port=27017
spring.mongodb.database=webflux
```

When you start the app with Docker Compose support enabled (the default, via `spring-boot-docker-compose`),
Spring Boot starts `compose.yml` itself and injects the connection details, credentials included.
