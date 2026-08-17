# Multi-DB Demo — Employee & Owner (Spring Boot)

Two separate databases wired into one Spring Boot app:

| Datasource | Purpose            | Package                              |
|------------|---------------------|---------------------------------------|
| Primary    | Employee CRUD        | `com.example.multidb.employee.*`      |
| Secondary  | Owner CRUD           | `com.example.multidb.owner.*`         |

Since the two entities live in **different databases**, they can't be joined
with a normal SQL join or a JPA `@OneToMany`. Instead, `Employee` stores a
plain `ownerId` field (no physical FK), and a combined service in
`com.example.multidb.combined.*` fetches both sides and stitches them
together in Java.

## Project layout

```
src/main/java/com/example/multidb/
├── MultiDbDemoApplication.java
├── config/
│   ├── DataSourceProperties.java
│   ├── PrimaryDataSourceConfig.java     # wires the Employee (primary) datasource
│   ├── SecondaryDataSourceConfig.java   # wires the Owner (secondary) datasource
│   └── GlobalExceptionHandler.java
├── employee/
│   ├── model/Employee.java
│   ├── repository/EmployeeRepository.java
│   ├── service/EmployeeService.java
│   └── controller/EmployeeController.java
├── owner/
│   ├── model/Owner.java
│   ├── repository/OwnerRepository.java
│   ├── service/OwnerService.java
│   └── controller/OwnerController.java
└── combined/
    ├── dto/OwnerWithEmployeesDTO.java
    ├── service/OwnerEmployeeCombinedService.java
    └── controller/OwnerEmployeeCombinedController.java
```

## Running it

Requires Java 17+ and Maven.

```bash
mvn spring-boot:run
```

The app ships with two **in-memory H2 databases** (`employeedb` and
`ownerdb`) configured in `application.properties`, so it runs out of the box
with zero external setup. Tables are auto-created (`ddl-auto=update`).

H2 console (optional): http://localhost:8080/h2-console
- JDBC URL for employee db: `jdbc:h2:mem:employeedb`
- JDBC URL for owner db: `jdbc:h2:mem:ownerdb`

### Switching to real databases (e.g. MySQL/Postgres)

Edit `application.properties`:

```properties
app.datasource.primary.jdbc-url=jdbc:mysql://localhost:3306/employee_db
app.datasource.primary.username=root
app.datasource.primary.password=secret
app.datasource.primary.driver-class-name=com.mysql.cj.jdbc.Driver

app.datasource.secondary.jdbc-url=jdbc:mysql://localhost:3306/owner_db
app.datasource.secondary.username=root
app.datasource.secondary.password=secret
app.datasource.secondary.driver-class-name=com.mysql.cj.jdbc.Driver
```

...and add the MySQL/Postgres driver dependency to `pom.xml` in place of (or
alongside) H2.

## API endpoints

### Owner CRUD (secondary DB)
| Method | Path                | Body / Notes           |
|--------|----------------------|-------------------------|
| POST   | `/api/owners`         | `{ "name", "email", "companyName", "phone" }` |
| GET    | `/api/owners`         | list all |
| GET    | `/api/owners/{id}`    | one owner |
| PUT    | `/api/owners/{id}`    | update |
| DELETE | `/api/owners/{id}`    | delete |

### Employee CRUD (primary DB)
| Method | Path                          | Body / Notes |
|--------|--------------------------------|---------------|
| POST   | `/api/employees`                | `{ "name", "email", "designation", "salary", "ownerId" }` |
| GET    | `/api/employees`                | list all |
| GET    | `/api/employees/{id}`           | one employee |
| GET    | `/api/employees/by-owner/{ownerId}` | employees of one owner |
| PUT    | `/api/employees/{id}`           | update |
| DELETE | `/api/employees/{id}`           | delete |

### Combined view (joins across both DBs)
| Method | Path                                     | Notes |
|--------|--------------------------------------------|-------|
| GET    | `/api/owners-with-employees`                | every owner + their employees |
| GET    | `/api/owners-with-employees/{ownerId}`      | one owner + their employees |

## Try it end-to-end

```bash
# 1. Create an owner (secondary DB)
curl -X POST http://localhost:8080/api/owners \
  -H "Content-Type: application/json" \
  -d '{"name":"Ravi Kumar","email":"ravi@acme.com","companyName":"Acme Corp","phone":"9999999999"}'
# -> { "id": 1, "name": "Ravi Kumar", ... }

# 2. Create an employee for that owner (primary DB)
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Priya Singh","email":"priya@acme.com","designation":"Engineer","salary":75000,"ownerId":1}'

# 3. Get the owner with their employees (combined, cross-DB)
curl http://localhost:8080/api/owners-with-employees/1

# 4. Get ALL owners with their employees
curl http://localhost:8080/api/owners-with-employees
```

Response from step 3 looks like:

```json
{
  "owner": { "id": 1, "name": "Ravi Kumar", "email": "ravi@acme.com", "companyName": "Acme Corp", "phone": "9999999999" },
  "employees": [
    { "id": 1, "name": "Priya Singh", "email": "priya@acme.com", "designation": "Engineer", "salary": 75000.0, "ownerId": 1 }
  ]
}
```
