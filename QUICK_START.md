# Quick Start Guide - VerifyDocs

## Prerequisites Checklist
- [ ] Java 17 or higher installed
- [ ] Maven 3.6+ installed
- [ ] PostgreSQL 12+ installed and running
- [ ] Postman installed (for API testing)
- [ ] IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions)

## Step-by-Step Setup

### Step 1: Database Setup (5 minutes)

1. Open PostgreSQL command line or pgAdmin

2. Create the database:
```sql
CREATE DATABASE verifydocs_db;
```

3. Verify database creation:
```sql
\l
```
(You should see verifydocs_db in the list)

### Step 2: Configure Application (2 minutes)

1. Open `src/main/resources/application.properties`

2. Update these lines with your PostgreSQL credentials:
```properties
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

Default is usually:
- Username: `postgres`
- Password: `password` (or whatever you set during PostgreSQL installation)

### Step 3: Build the Project (3 minutes)

1. Open terminal/command prompt in project root directory

2. Run Maven clean and install:
```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the code
- Run tests (if any)
- Create the JAR file

### Step 4: Run the Application (1 minute)

Run the Spring Boot application:
```bash
mvn spring-boot:run
```

You should see:
```
Started VerifyDocsApplication in X.XXX seconds
```

The application runs on: `http://localhost:8080`

### Step 5: Verify Tables Created (2 minutes)

1. Check PostgreSQL database:
```sql
\c verifydocs_db
\dt
```

You should see these tables:
- provinces
- institutions
- institution_profiles
- users
- documents
- verification_logs
- user_document_access

### Step 6: Test with Postman (10 minutes)

#### Test 1: Create Province (Location Saving)
```
POST http://localhost:8080/api/provinces
Content-Type: application/json

{
    "code": "KGL",
    "name": "Kigali"
}
```

Expected Response: 200 OK with province data

#### Test 2: Check Province Exists (existBy method)
```
GET http://localhost:8080/api/provinces/exists/KGL
```

Expected Response: `true`

#### Test 3: Create Institution
```
POST http://localhost:8080/api/institutions?provinceCode=KGL
Content-Type: application/json

{
    "name": "AUCA",
    "email": "info@auca.ac.rw"
}
```

Expected Response: 200 OK with institution data

#### Test 4: Create Institution Profile (One-to-One)
```
POST http://localhost:8080/api/institutions/1/profile
Content-Type: application/json

{
    "address": "KG 544 St, Kigali",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```

Expected Response: 200 OK with profile data

#### Test 5: Create User
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "email": "john.doe@auca.ac.rw",
    "password": "password123",
    "role": "ADMIN",
    "fullName": "John Doe",
    "institution": {"id": 1}
}
```

Expected Response: 200 OK with user data

#### Test 6: Create Another User (for testing)
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
    "email": "jane.smith@auca.ac.rw",
    "password": "password123",
    "role": "USER",
    "fullName": "Jane Smith",
    "institution": {"id": 1}
}
```

#### Test 7: Get Users with Pagination
```
GET http://localhost:8080/api/users/paginated?page=0&size=10
```

Expected Response: Page object with users array

#### Test 8: Get Users with Sorting
```
GET http://localhost:8080/api/users/sorted?sortBy=email
```

Expected Response: Users sorted by email

#### Test 9: Get Users by Province Code
```
GET http://localhost:8080/api/users/province/code/KGL
```

Expected Response: All users from Kigali province

#### Test 10: Get Users by Province Name
```
GET http://localhost:8080/api/users/province/name/Kigali
```

Expected Response: All users from Kigali province

### Step 7: Import Postman Collection (Optional)

1. Open Postman
2. Click Import
3. Select `VerifyDocs_Postman_Collection.json`
4. All API requests are now available in Postman

## Troubleshooting

### Problem: "Port 8080 already in use"
**Solution:** 
- Stop other applications using port 8080
- Or change port in `application.properties`:
```properties
server.port=8081
```

### Problem: "Could not connect to database"
**Solution:**
- Verify PostgreSQL is running
- Check username and password in `application.properties`
- Verify database `verifydocs_db` exists

### Problem: "Table already exists" error
**Solution:**
- Change `spring.jpa.hibernate.ddl-auto=update` to `create-drop` (only for development)
- Or manually drop tables and restart

### Problem: Maven build fails
**Solution:**
- Check Java version: `java -version` (should be 17+)
- Check Maven version: `mvn -version` (should be 3.6+)
- Delete `.m2/repository` folder and rebuild

### Problem: "No suitable driver found"
**Solution:**
- Verify PostgreSQL dependency in `pom.xml`
- Run `mvn clean install` again

## Testing Checklist

After setup, verify these features work:

- [ ] Create Province (Location Saving) ✓
- [ ] Check Province Exists (existBy) ✓
- [ ] Create Institution ✓
- [ ] Create Institution Profile (One-to-One) ✓
- [ ] Create User ✓
- [ ] Get Users with Pagination ✓
- [ ] Get Users with Sorting ✓
- [ ] Get Users by Province Code ✓
- [ ] Get Users by Province Name ✓
- [ ] Create Document ✓
- [ ] Verify Document ✓

## Project Structure Overview

```
VerifyDocs/
├── src/
│   ├── main/
│   │   ├── java/com/verifydocs/
│   │   │   ├── entity/          # Database entities (5 tables)
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── service/         # Business logic
│   │   │   ├── controller/      # REST API endpoints
│   │   │   └── VerifyDocsApplication.java
│   │   └── resources/
│   │       └── application.properties
├── pom.xml                      # Maven dependencies
├── README.md                    # Full documentation
├── ERD_DOCUMENTATION.md         # Database design
├── VIVA_VOCE_GUIDE.md          # Exam preparation
├── database_schema.sql          # SQL schema
└── VerifyDocs_Postman_Collection.json
```

## Next Steps

1. **Add More Data**: Create more provinces, institutions, and users
2. **Test Relationships**: Verify One-to-Many, Many-to-Many work correctly
3. **Test Pagination**: Create 20+ users and test pagination
4. **Test Sorting**: Sort by different fields (email, fullName, id)
5. **Review Code**: Understand each entity, repository, service, and controller
6. **Prepare for Viva**: Read VIVA_VOCE_GUIDE.md

## Useful Commands

### Maven Commands
```bash
mvn clean                 # Clean build directory
mvn compile              # Compile code
mvn test                 # Run tests
mvn package              # Create JAR file
mvn spring-boot:run      # Run application
```

### PostgreSQL Commands
```sql
\c verifydocs_db         # Connect to database
\dt                      # List tables
\d table_name            # Describe table structure
SELECT * FROM provinces; # View data
```

## API Endpoints Summary

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | /api/provinces | Create province |
| GET | /api/provinces/exists/{code} | Check existence |
| POST | /api/institutions | Create institution |
| POST | /api/institutions/{id}/profile | Create profile |
| POST | /api/users | Create user |
| GET | /api/users/paginated | Get users (paginated) |
| GET | /api/users/sorted | Get users (sorted) |
| GET | /api/users/province/code/{code} | Get users by province code |
| GET | /api/users/province/name/{name} | Get users by province name |
| POST | /api/documents | Create document |
| GET | /api/documents/verify/{code} | Verify document |

## Support

If you encounter issues:
1. Check the Troubleshooting section above
2. Review error messages in console
3. Verify database connection
4. Check Postman request format
5. Review README.md for detailed explanations

## Success Indicators

You'll know everything is working when:
- ✅ Application starts without errors
- ✅ All 7 tables are created in database
- ✅ You can create provinces, institutions, and users
- ✅ Pagination returns correct page size
- ✅ Sorting orders results correctly
- ✅ Province-based user retrieval works
- ✅ existBy() methods return true/false correctly

**Congratulations! Your VerifyDocs system is now running! 🎉**

---

**Time to Complete Setup: ~25 minutes**

**Ready for Demo: Yes ✓**

**Ready for Viva-Voce: Read VIVA_VOCE_GUIDE.md ✓**
