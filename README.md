# Usuarios API

API REST desarrollada con Spring Boot para la gestión de usuarios. Incluye operaciones CRUD completas, filtrado, ordenamiento, paginación, validación de datos y documentación automática con Swagger/OpenAPI.

## 🚀 Tecnologías Utilizadas

- **Spring Boot 3.5.7**
- **Java 17**
- **Spring Data JPA**
- **H2 Database** (base de datos en memoria)
- **Lombok**
- **Springdoc OpenAPI** (Swagger)
- **JUnit 4** y **Mockito** (testing)
- **Maven** (gestión de dependencias)

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6 o superior

## 🛠️ Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd usuarios-api
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación de la API

### Swagger UI

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de Swagger en:

```
http://localhost:8080/swagger-ui.html
```

### Documentación OpenAPI (JSON)

```
http://localhost:8080/v3/api-docs
```

## 🔌 Endpoints de la API

### Base URL
```
http://localhost:8080/api/users
```

### Endpoints disponibles

#### 1. Crear usuario
- **POST** `/api/users`
- **Body**: `UserCreateDto`
- **Respuesta**: `201 Created` - Usuario creado
- **Errores**: `400 Bad Request` (datos inválidos), `409 Conflict` (email ya existe)

#### 2. Obtener usuario por ID
- **GET** `/api/users/{id}`
- **Respuesta**: `200 OK` - Usuario encontrado
- **Errores**: `404 Not Found` (usuario no encontrado)

#### 3. Actualizar usuario
- **PUT** `/api/users/{id}`
- **Body**: `UserUpdateDto`
- **Respuesta**: `200 OK` - Usuario actualizado
- **Errores**: `400 Bad Request`, `404 Not Found`, `409 Conflict`

#### 4. Eliminar usuario
- **DELETE** `/api/users/{id}`
- **Respuesta**: `204 No Content` - Usuario eliminado
- **Errores**: `404 Not Found`

#### 5. Buscar usuarios (con filtros, orden y paginación)
- **GET** `/api/users`
- **Query Parameters**:
  - `name` - Buscar por nombre o apellido
  - `email` - Filtrar por email
  - `role` - Filtrar por rol
  - `status` - Filtrar por estado
  - `birthDateFrom` - Fecha de nacimiento desde (yyyy-MM-dd)
  - `birthDateTo` - Fecha de nacimiento hasta (yyyy-MM-dd)
  - `page` - Número de página (por defecto: 0)
  - `size` - Tamaño de página (por defecto: 10)
  - `sort` - Campo de ordenación (ej: `createdAt,desc`)
- **Respuesta**: `200 OK` - Página de usuarios

### Ejemplos de uso

#### Crear usuario
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@example.com",
    "role": "ADMIN",
    "status": "ACTIVE",
    "birthDate": "1990-01-15"
  }'
```

#### Buscar usuarios con filtros y paginación
```bash
curl "http://localhost:8080/api/users?name=Juan&role=ADMIN&page=0&size=10&sort=createdAt,desc"
```

## 🗄️ Base de Datos

La aplicación utiliza **H2 Database** (base de datos en memoria) para desarrollo y testing.

### Acceder a la consola de H2

1. Ejecutar la aplicación
2. Acceder a: `http://localhost:8080/h2-console`
3. **JDBC URL**: `jdbc:h2:mem:usersdb`
4. **Usuario**: `sa`
5. **Contraseña**: (vacía)

## 🏗️ Estructura del Proyecto

```
src/main/java/com/usuario/usuarios_api/
├── config/
│   └── OpenApiConfig.java          # Configuración de Swagger/OpenAPI
├── modules/
│   ├── shared/
│   │   ├── ErrorResponseDto.java   # DTO para respuestas de error
│   │   └── GlobalExceptionHandler.java  # Manejo global de excepciones
│   └── user/
│       ├── controllers/
│       │   └── UserController.java # Controlador REST
│       ├── dto/
│       │   ├── UserCreateDto.java  # DTO para crear usuario
│       │   ├── UserUpdateDto.java  # DTO para actualizar usuario
│       │   ├── UserResponseDto.java # DTO para respuesta
│       │   └── UserSearchRequest.java # DTO para búsqueda
│       ├── entities/
│       │   └── User.java           # Entidad JPA
│       ├── exceptions/
│       │   ├── UserNotFoundException.java
│       │   ├── UserAlreadyExistsException.java
│       │   └── InvalidUserDataException.java
│       ├── interfaces/
│       │   └── UserService.java    # Interfaz del servicio
│       ├── mappers/
│       │   ├── UserMapper.java     # Mapper para conversiones
│       │   └── UserSearchMapper.java
│       ├── repositories/
│       │   └── UserRepository.java # Repositorio JPA
│       ├── services/
│       │   └── UserServiceImpl.java # Implementación del servicio
│       └── specifications/
│           └── UserSpecifications.java # Especificaciones para búsquedas dinámicas
└── UsuariosApiApplication.java     # Clase principal
```

## 🧪 Testing

El proyecto incluye tests unitarios utilizando JUnit 4 y Mockito.

### Ejecutar tests

```bash
mvn test
```

### Cobertura de tests

Los tests cubren:
- Controladores (UserController)
- Servicios (UserServiceImpl)
- Mappers (UserMapper, UserSearchMapper)
- Validaciones de datos
- Manejo de excepciones

## 📝 Características Implementadas

- ✅ CRUD completo de usuarios
- ✅ Filtrado por múltiples criterios (nombre, email, rol, estado, fecha de nacimiento)
- ✅ Ordenamiento dinámico
- ✅ Paginación
- ✅ Validación de datos de entrada
- ✅ Manejo de errores centralizado
- ✅ Documentación automática con Swagger/OpenAPI
- ✅ Tests unitarios
- ✅ Arquitectura limpia (package-by-feature)
- ✅ DTOs para separación de capas
- ✅ Mappers para conversiones
- ✅ Especificaciones JPA para búsquedas dinámicas

## 🔒 Validaciones

### Campos obligatorios
- `firstName` - Nombre del usuario
- `lastName` - Apellido del usuario
- `email` - Correo electrónico (debe contener "@")

### Validaciones de negocio
- El email debe ser único
- El email debe tener un formato válido (contener "@")
- Los campos obligatorios no pueden ser null, vacíos o solo espacios

## 🚨 Manejo de Errores

La API utiliza un manejador global de excepciones que devuelve respuestas HTTP apropiadas:

- `400 Bad Request` - Datos inválidos
- `404 Not Found` - Recurso no encontrado
- `409 Conflict` - Conflicto (email duplicado)
- `500 Internal Server Error` - Error interno del servidor

## 📦 Dependencias Principales

Ver `pom.xml` para la lista completa de dependencias.

## 👤 Autor

Usuario

## 📄 Licencia

Este proyecto es parte de una prueba técnica.

