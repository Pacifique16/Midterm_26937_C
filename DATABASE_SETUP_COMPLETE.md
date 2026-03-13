# ✅ Database Setup Complete!

## Database Information
- **Database Name**: verifydocs_db
- **PostgreSQL User**: postgres
- **Status**: ✅ Successfully Created

---

## Tables Created (7 tables)

1. ✅ **provinces** - Location data
2. ✅ **institutions** - Institution information
3. ✅ **institution_profiles** - Institution profiles (One-to-One)
4. ✅ **users** - User accounts
5. ✅ **documents** - Document metadata
6. ✅ **verification_logs** - Verification history
7. ✅ **user_document_access** - Join table (Many-to-Many)

---

## Sample Data Inserted

### Provinces (3 records)
| ID | Code | Name    |
|----|------|---------|
| 1  | KGL  | Kigali  |
| 2  | EST  | Eastern |
| 3  | WST  | Western |

### Institutions (2 records)
| ID | Name | Email            | Status | Province |
|----|------|------------------|--------|----------|
| 1  | AUCA | info@auca.ac.rw  | ACTIVE | Kigali   |
| 2  | UR   | info@ur.ac.rw    | ACTIVE | Kigali   |

### Users (2 records)
| ID | Email             | Password    | Role  | Full Name  | Institution |
|----|-------------------|-------------|-------|------------|-------------|
| 1  | admin@auca.ac.rw  | password123 | ADMIN | John Doe   | AUCA        |
| 2  | user@auca.ac.rw   | password123 | USER  | Jane Smith | AUCA        |

---

## Next Steps

### 1. Run the Spring Boot Application
```bash
mvn spring-boot:run
```

### 2. Test the APIs with Postman

#### Test Province Query (Rubric Requirement #8)
```
GET http://localhost:8080/api/users/province/code/KGL
```
Expected: Returns both users (John Doe and Jane Smith)

#### Test Pagination
```
GET http://localhost:8080/api/users/paginated?page=0&size=10
```

#### Test Sorting
```
GET http://localhost:8080/api/users/sorted?sortBy=email
```

#### Test existBy Method
```
GET http://localhost:8080/api/provinces/exists/KGL
```
Expected: Returns `true`

---

## Database Connection Details

Update these in `application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/verifydocs_db
spring.datasource.username=postgres
spring.datasource.password=password
```

---

## Useful PostgreSQL Commands

### Connect to database:
```bash
psql -U postgres -d verifydocs_db
```

### List all tables:
```sql
\dt
```

### View table structure:
```sql
\d table_name
```

### View all provinces:
```sql
SELECT * FROM provinces;
```

### View all users with their institutions:
```sql
SELECT u.*, i.name as institution_name 
FROM users u 
JOIN institutions i ON u.institution_id = i.id;
```

### View users by province (Test Query):
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = 'KGL';
```

---

## Troubleshooting

### If you need to reset the database:
```bash
psql -U postgres -c "DROP DATABASE verifydocs_db;"
psql -U postgres -c "CREATE DATABASE verifydocs_db;"
psql -U postgres -d verifydocs_db -f database_schema.sql
```

### Or use the batch script:
```bash
setup_database.bat
```

---

## ✅ Ready for Testing!

Your database is now set up with:
- All 7 tables created
- Sample data for testing
- Proper relationships configured
- Ready for Spring Boot application

**You can now run your application and test all the rubric requirements!** 🚀
