# VerifyDocs - Secure Digital Document Verification System

## Project Overview
VerifyDocs is a Spring Boot application that enables institutions to issue and verify digital documents securely using cryptographic hashing and unique verification codes.

---

## Rubric Requirements Implementation

### 1. Entity Relationship Diagram (ERD) - 5 Tables (3 Marks)

**Tables Created:**
1. **provinces** - Stores location data
2. **institutions** - Stores institution information
3. **institution_profiles** - Stores detailed institution profiles
4. **users** - Stores user accounts
5. **documents** - Stores document metadata
6. **verification_logs** - Stores verification history
7. **user_document_access** - Join table for Many-to-Many relationship

**Relationships Explained:**
- Province → Institution: One-to-Many (One province has many institutions)
- Institution → User: One-to-Many (One institution has many users)
- Institution → Document: One-to-Many (One institution issues many documents)
- Institution → InstitutionProfile: One-to-One (One institution has one profile)
- User ↔ Document: Many-to-Many (Users can access multiple documents, documents can be accessed by multiple users)
- Document → VerificationLog: One-to-Many (One document has many verification logs)

---

### 2. Implementation of Saving Location (2 Marks)

**File:** `ProvinceService.java`

**Explanation:**
- The `saveProvince()` method stores location data (Province) in the database
- Before saving, it checks if a province with the same code already exists using `existsByCode()`
- The Province entity has a `code` (unique identifier) and `name` field
- Data is persisted using `provinceRepository.save(province)`
- The relationship is handled through JPA: When an Institution is saved, it references a Province via the `province_id` foreign key

**How it works:**
```java
public Province saveProvince(Province province) {
    if (provinceRepository.existsByCode(province.getCode())) {
        throw new RuntimeException("Province already exists");
    }
    return provinceRepository.save(province);
}
```

**API Endpoint:** `POST /api/provinces`

---

### 3. Implementation of Sorting and Pagination (5 Marks)

**Files:** `UserService.java`, `DocumentService.java`, `UserController.java`, `DocumentController.java`

#### Sorting Implementation:
**Explanation:**
- Sorting is implemented using Spring Data JPA's `Sort` class
- The `Sort.by(field)` method creates a Sort object for a specific field
- `.ascending()` or `.descending()` specifies the sort direction
- The repository's `findAll(Sort sort)` method applies the sorting

**Example:**
```java
public List<User> getAllUsersSorted(String sortBy) {
    Sort sort = Sort.by(sortBy).ascending();
    return userRepository.findAll(sort);
}
```

#### Pagination Implementation:
**Explanation:**
- Pagination is implemented using Spring Data JPA's `Pageable` interface
- `PageRequest.of(page, size)` creates a Pageable object
- `page` is the page number (0-indexed)
- `size` is the number of records per page
- The repository's `findAll(Pageable pageable)` method returns a `Page<T>` object
- `Page` contains the data, total pages, total elements, and current page info

**Example:**
```java
public Page<User> getAllUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return userRepository.findAll(pageable);
}
```

#### Combined Pagination + Sorting:
```java
public Page<User> getAllUsersPaginatedAndSorted(int page, int size, String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
    return userRepository.findAll(pageable);
}
```

**Performance Benefits:**
- Reduces memory usage by loading only required records
- Improves response time by limiting data transfer
- Enables efficient navigation through large datasets

**API Endpoints:**
- `GET /api/users/paginated?page=0&size=10`
- `GET /api/users/sorted?sortBy=email`
- `GET /api/users/paginated-sorted?page=0&size=10&sortBy=fullName`

---

### 4. Implementation of Many-to-Many Relationship (3 Marks)

**Files:** `User.java`, `Document.java`

**Explanation:**
- Many-to-Many relationship exists between User and Document
- A User can access multiple Documents
- A Document can be accessed by multiple Users
- JPA creates a join table named `user_document_access` automatically
- The join table has two foreign keys: `user_id` and `document_id`

**Mapping in User Entity:**
```java
@ManyToMany
@JoinTable(
    name = "user_document_access",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "document_id")
)
private Set<Document> accessibleDocuments = new HashSet<>();
```

**Mapping in Document Entity:**
```java
@ManyToMany(mappedBy = "accessibleDocuments")
private Set<User> authorizedUsers = new HashSet<>();
```

**Join Table Structure:**
- `user_id` (Foreign Key → users.id)
- `document_id` (Foreign Key → documents.id)
- Primary Key: Composite of (user_id, document_id)

---

### 5. Implementation of One-to-Many Relationship (2 Marks)

**Files:** `Institution.java`, `User.java`, `Document.java`, `VerificationLog.java`

**Examples:**

#### Institution → User (One-to-Many):
```java
// In Institution.java
@OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
private List<User> users;

// In User.java
@ManyToOne
@JoinColumn(name = "institution_id", nullable = false)
private Institution institution;
```

**Explanation:**
- One Institution has many Users
- The `institution_id` foreign key in the `users` table references `institutions.id`
- `mappedBy = "institution"` indicates that User entity owns the relationship
- `cascade = CascadeType.ALL` means operations on Institution cascade to Users

#### Document → VerificationLog (One-to-Many):
```java
// In Document.java
@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
private List<VerificationLog> verificationLogs;

// In VerificationLog.java
@ManyToOne
@JoinColumn(name = "document_id", nullable = false)
private Document document;
```

**Foreign Key Usage:**
- The `@JoinColumn` annotation specifies the foreign key column name
- `nullable = false` ensures referential integrity
- The foreign key creates a database constraint linking the tables

---

### 6. Implementation of One-to-One Relationship (2 Marks)

**Files:** `Institution.java`, `InstitutionProfile.java`

**Explanation:**
- One Institution has exactly one InstitutionProfile
- One InstitutionProfile belongs to exactly one Institution
- The `institution_id` in `institution_profiles` table is both a foreign key and unique

**Mapping in Institution Entity:**
```java
@OneToOne(mappedBy = "institution", cascade = CascadeType.ALL)
private InstitutionProfile profile;
```

**Mapping in InstitutionProfile Entity:**
```java
@OneToOne
@JoinColumn(name = "institution_id", unique = true, nullable = false)
private Institution institution;
```

**How entities are connected:**
- The `@JoinColumn` with `unique = true` ensures one-to-one constraint
- `mappedBy = "institution"` indicates InstitutionProfile owns the relationship
- The foreign key `institution_id` in `institution_profiles` references `institutions.id`
- The `unique` constraint prevents multiple profiles for one institution

---

### 7. Implementation of existBy() Method (2 Marks)

**Files:** `ProvinceRepository.java`, `InstitutionRepository.java`, `UserRepository.java`, `DocumentRepository.java`

**Explanation:**
- `existBy()` methods are Spring Data JPA query methods
- They return a boolean indicating whether an entity exists with the given criteria
- Spring Data JPA automatically generates the SQL query based on method name
- The method name follows the pattern: `existsBy + PropertyName`

**Examples:**

```java
// In ProvinceRepository.java
boolean existsByCode(String code);
boolean existsByName(String name);

// In UserRepository.java
boolean existsByEmail(String email);

// In DocumentRepository.java
boolean existsByVerificationCode(String verificationCode);
```

**How it works:**
- Spring Data JPA parses the method name
- Generates SQL: `SELECT COUNT(*) > 0 FROM provinces WHERE code = ?`
- Returns `true` if count > 0, otherwise `false`
- More efficient than fetching the entire entity

**Usage in Service:**
```java
public Province saveProvince(Province province) {
    if (provinceRepository.existsByCode(province.getCode())) {
        throw new RuntimeException("Province already exists");
    }
    return provinceRepository.save(province);
}
```

**API Endpoint:** `GET /api/provinces/exists/{code}`

---

### 8. Retrieve All Users from a Given Province (4 Marks)

**Files:** `UserRepository.java`, `UserService.java`, `UserController.java`

**Implementation:**

#### Using Province Code:
```java
@Query("SELECT u FROM User u WHERE u.institution.province.code = :provinceCode")
List<User> findAllByProvinceCode(@Param("provinceCode") String provinceCode);
```

#### Using Province Name:
```java
@Query("SELECT u FROM User u WHERE u.institution.province.name = :provinceName")
List<User> findAllByProvinceName(@Param("provinceName") String provinceName);
```

**Query Logic Explanation:**
- Uses JPQL (Java Persistence Query Language)
- `u` is an alias for User entity
- `u.institution.province.code` navigates through relationships:
  - User → Institution (via `institution` field)
  - Institution → Province (via `province` field)
  - Province → code (via `code` field)
- `:provinceCode` is a named parameter bound using `@Param`
- Spring Data JPA translates this to SQL with proper JOINs

**Generated SQL (approximate):**
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = ?
```

**Service Layer:**
```java
public List<User> getUsersByProvinceCode(String provinceCode) {
    return userRepository.findAllByProvinceCode(provinceCode);
}

public List<User> getUsersByProvinceName(String provinceName) {
    return userRepository.findAllByProvinceName(provinceName);
}
```

**API Endpoints:**
- `GET /api/users/province/code/{provinceCode}`
- `GET /api/users/province/name/{provinceName}`

---

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Postman (for API testing)

### Database Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE verifydocs_db;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
1. Navigate to project directory
2. Run: `mvn clean install`
3. Run: `mvn spring-boot:run`
4. Application starts on: `http://localhost:8080`

### API Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/api-docs`

---

## Testing with Postman

### 1. Create Province (Location)
```
POST http://localhost:8080/api/provinces
Body (JSON):
{
    "code": "KGL",
    "name": "Kigali"
}
```

### 2. Create Institution
```
POST http://localhost:8080/api/institutions?provinceCode=KGL
Body (JSON):
{
    "name": "AUCA",
    "email": "info@auca.ac.rw",
    "status": "ACTIVE"
}
```

### 3. Create Institution Profile (One-to-One)
```
POST http://localhost:8080/api/institutions/1/profile
Body (JSON):
{
    "address": "KG 544 St",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```

### 4. Create User
```
POST http://localhost:8080/api/users
Body (JSON):
{
    "email": "user@auca.ac.rw",
    "password": "password123",
    "role": "ADMIN",
    "fullName": "John Doe",
    "institution": {"id": 1}
}
```

### 5. Get Users by Province Code
```
GET http://localhost:8080/api/users/province/code/KGL
```

### 6. Get Users with Pagination
```
GET http://localhost:8080/api/users/paginated?page=0&size=10
```

### 7. Get Users with Sorting
```
GET http://localhost:8080/api/users/sorted?sortBy=email
```

### 8. Check if Province Exists
```
GET http://localhost:8080/api/provinces/exists/KGL
```

---

## Project Structure
```
src/main/java/com/verifydocs/
├── entity/              # JPA Entities (5 tables)
│   ├── Province.java
│   ├── Institution.java
│   ├── InstitutionProfile.java
│   ├── User.java
│   ├── Document.java
│   └── VerificationLog.java
├── repository/          # Spring Data JPA Repositories
│   ├── ProvinceRepository.java
│   ├── InstitutionRepository.java
│   ├── UserRepository.java
│   ├── DocumentRepository.java
│   └── VerificationLogRepository.java
├── service/            # Business Logic
│   ├── ProvinceService.java
│   ├── InstitutionService.java
│   ├── UserService.java
│   └── DocumentService.java
├── controller/         # REST API Controllers
│   ├── ProvinceController.java
│   ├── InstitutionController.java
│   ├── UserController.java
│   └── DocumentController.java
└── VerifyDocsApplication.java  # Main Application
```

---

## Key Features Implemented

✅ 5+ Entity tables with proper relationships  
✅ Location (Province) saving functionality  
✅ Pagination using Pageable  
✅ Sorting using Sort  
✅ Many-to-Many relationship (User ↔ Document)  
✅ One-to-Many relationships (Institution → User, Document → VerificationLog)  
✅ One-to-One relationship (Institution ↔ InstitutionProfile)  
✅ existBy() methods for existence checking  
✅ Retrieve users by province code/name with JPQL queries  

---

## Viva-Voce Preparation Topics

1. **ERD Explanation**: Describe all 5 tables and their relationships
2. **Location Saving**: Explain how Province data is stored and validated
3. **Pagination**: Explain Pageable interface and Page object
4. **Sorting**: Explain Sort class and how it works with repositories
5. **Many-to-Many**: Explain join table creation and mapping
6. **One-to-Many**: Explain foreign key usage and cascade operations
7. **One-to-One**: Explain unique constraint and bidirectional mapping
8. **existBy()**: Explain Spring Data JPA query derivation
9. **Province Query**: Explain JPQL and relationship navigation
10. **Spring Boot Architecture**: Explain Controller → Service → Repository pattern

---

## Author
Pacifique Harerimana  
AUCA - Web Technology and Internet Course
