📚 API RESTful: Gestión de Empleados

Este proyecto implementa una API RESTful para la gestión de empleados, desarrollada con Spring Boot.
El objetivo es cumplir con la Evidencia **GA7-220501096-AA5-EV03** (Diseño y desarrollo de servicios web - Proyecto).
La API incluye un módulo de seguridad con JWT y está completamente documentada con Swagger UI.

---

🚀 Tecnologías Principales

| Tecnología | Propósito |
|-----------|-----------|
| **Java 17+** | Lenguaje principal |
| **Spring Boot 3.x** | Framework backend |
| **JPA / Hibernate** | Persistencia de datos |
| **H2 Database** | BD en memoria (por defecto) |
| **Spring Security + JWT** | Seguridad y autenticación |
| **SpringDoc OpenAPI (Swagger UI)** | Documentación interactiva |

---

💻 Configuración y Ejecución

1. Requisitos Previos

Asegúrate de tener instalado:

- **JDK 17 o superior**
- **Maven**
- Un IDE (IntelliJ, VS Code o Eclipse)

---

2. Clonar el Repositorio

```bash
git clone https://github.com/tu_usuario/gestion-empleados-api_AA5_EV03.git
cd gestion-empleados-api_AA5_EV03
```

---

3. Iniciar la Aplicación

```bash
mvn clean package
java -jar target/gestion-empleados-1.0-SNAPSHOT.jar
```

La API se iniciará en: **http://localhost:8080**

---

🌐 Documentación y Servicios

1. Documentación Interactiva (Swagger UI)

Todos los endpoints están documentados con ejemplos y esquemas.

➡️ **URL:**
http://localhost:8080/swagger-ui/index.html

---

2. Endpoints de Autenticación (Públicos)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Crea un nuevo usuario |
| POST | `/api/auth/login` | Inicia sesión y devuelve un Token JWT |

---

3. Endpoints de Gestión de Empleados (Asegurados)

> **Requiere JWT en el Header:**
`Authorization: Bearer <Token>`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/empleados` | Lista todos los empleados |
| GET | `/api/empleados/{id}` | Busca un empleado por ID |
| POST | `/api/empleados` | Crea un nuevo empleado |
| PUT | `/api/empleados/{id}` | Actualización completa |
| PATCH | `/api/empleados/{id}` | Actualización parcial |
| DELETE | `/api/empleados/{id}` | Elimina un empleado |

---

🧪 Pruebas y Sustentación
(Evidencia GA7-220501096-AA5-EV04)

El testing se realizó utilizando **Postman**, verificando:

- Endpoints CRUD
- Seguridad con JWT
- Errores esperados (400, 403, 404)

📂 Artefactos de la Prueba

Incluidos en el repositorio de la EV04:

- **Colección de Postman**
- **Video del testing**
- **Documento de evidencias (pantallazos)**

---

🏷️ Historial de Versiones (Tags)

- **V1.0-EV03-Final** → Entrega de la evidencia AA5-EV03
- **V2.0-EV04-Final** → Pruebas realizadas con Postman (EV04)

---


## 👩‍💻 Autor

**Aprendiz:** Mónica Ismelia Cañas Reyes
**Programa:** Tecnólogo en Análisis y Desarrollo de Software
**Institución:** Servicio Nacional de Aprendizaje – SENA 
**Centro:** Centro Nacional de Asistencia Técnica a la Industria – ASTIN
**Evidencia:** GA7-220501096-AA5-EV04
**Fecha:** Diciembre de 2025
