# ms-transfer-control

Este proyecto es un microservicio desarrollado con Spring Boot para validar y procesar transferencias.

## Requisitos

- Java 17 o superior
- Maven

## Ejecutar la aplicación

Desde la raíz del proyecto, ejecuta:

### Linux/macOS

```bash
mvn spring-boot:run
```

### Windows

```powershell
mvn spring-boot:run
```

Si tu entorno incluye Maven Wrapper, también puedes usar:

```bash
./mvnw spring-boot:run
```

```powershell
mvnw.cmd spring-boot:run
```

La aplicación queda disponible en:

- http://localhost:8085

El servicio usa una base de datos H2 en memoria y carga datos iniciales automáticamente desde `src/main/resources/schema.sql` y `src/main/resources/data.sql`.

## Endpoint de la API

### POST `/api/transfers/control`

Ejemplo de solicitud:

```bash
curl -X POST http://localhost:8085/api/transfers/control \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "100",
    "sourceAccount": "001-100001",
    "targetAccount": "001-200001",
    "amount": 500.00
  }'
```

Ejemplo de respuesta:

```json
{
  "status": "APPROVED",
  "sourceAccount": "001-100001",
  "targetAccount": "001-200001",
  "amount": 500.00
}
```

## Consola de H2

La consola H2 está habilitada y se puede acceder en:

- http://localhost:8085/h2-console

Datos de conexión:

- JDBC URL: `jdbc:h2:mem:customerdb`
- Usuario: `sa`
- Contraseña: vacía

## Comandos útiles

Compilar el proyecto:

```bash
mvn clean package
```

Ejecutar pruebas:

```bash
mvn test
```
