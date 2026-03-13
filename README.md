# VerifyDocs - Secure Digital Document Verification System

## Project Overview
VerifyDocs is a Spring Boot application that enables institutions to issue and verify digital documents securely using cryptographic hashing and unique verification codes.


---


### 1. Entity Relationship Diagram (ERD) - 7 Tables 

**Tables Created:**
1. **locations** - Stores all location data using Adjacency List pattern (Province, District, Sector, Cell, Village)
2. **institutions** - Stores institution information
3. **institution_profiles** - Stores detailed institution profiles
4. **users** - Stores user accounts
5. **documents** - Stores document metadata
7. **user_document_access** - Join table for Many-to-Many relationship

**Relationships Explained:**
- Location → Location: Self-referencing (Parent-Child hierarchy using Adjacency List)
- Location → Institution: One-to-Many (One village has many institutions)
---

**Explanation:**
- Before saving, it checks if a location with the same code already exists using `existsByCode()`
- The Location entity has `code`, `name`, `level`, and `parent_id` fields
- All location levels (Province, District, Sector, Cell, Village) use the same table
- Data is persisted using `locationRepository.save(location)`

**How it works:**
```java
public Location saveLocation(Location location) {
    if (locationRepository.existsByCode(location.getCode())) {
        throw new RuntimeException("Location already exists");
    }
    return locationRepository.save(location);
}
```


**Adjacency List Benefits:**
- Single table for all location levels (simpler structure)
- Easy to add new location levels
- Efficient parent-child navigation
- Follows lecturer's requirement: Institution links to Village, automatically connected to all parent levels

---

### 3. Implementation of Sorting and Pagination 
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

### 4. Implementation of Many-to-Many Relationship 

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

### 5. Implementation of One-to-Many Relationship 

**Files:** `Location.java`, `Institution.java`, `User.java`, `Document.java`, `VerificationLog.java`

**Examples:**

#### Location → Location (Self-Referencing One-to-Many):
```java
// In Location.java
@ManyToOne
@JoinColumn(name = "parent_id")
private Location parent;

@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
private List<Location> children;
```

**Explanation:**
- One Location (e.g., Province) has many child Locations (e.g., Districts)
- The `parent_id` foreign key references `locations.id`
- Enables hierarchical navigation: Village → Cell → Sector → District → Province

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

### 6. Implementation of One-to-One Relationship

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

### 7. Implementation of existBy() Method

**Files:** `LocationRepository.java`, `InstitutionRepository.java`, `UserRepository.java`, `DocumentRepository.java`

**Explanation:**
- `existBy()` methods are Spring Data JPA query methods
- They return a boolean indicating whether an entity exists with the given criteria
- Spring Data JPA automatically generates the SQL query based on method name
- The method name follows the pattern: `existsBy + PropertyName`

**Examples:**

```java
// In LocationRepository.java
boolean existsByCode(String code);
boolean existsByName(String name);

// In UserRepository.java
boolean existsByEmail(String email);

// In DocumentRepository.java
boolean existsByVerificationCode(String verificationCode);

// In InstitutionProfileRepository.java
boolean existsByInstitutionId(Long institutionId);
```

**How it works:**
- Spring Data JPA parses the method name
- Generates SQL: `SELECT COUNT(*) > 0 FROM locations WHERE code = ?`
- Returns `true` if count > 0, otherwise `false`
- More efficient than fetching the entire entity

**Usage in Service:**
```java
public Location saveLocation(Location location) {
    if (locationRepository.existsByCode(location.getCode())) {
        throw new RuntimeException("Location already exists");
    }
    return locationRepository.save(location);
}
```

---

### 8. Retrieve All Users from a Given Province 

**Files:** `UserRepository.java`, `UserService.java`, `UserController.java`

**Implementation:**

#### Using Province Code:
```java
@Query("SELECT u FROM User u " +
       "JOIN u.institution i " +
       "JOIN i.location v " +
       "JOIN v.parent c " +
       "JOIN c.parent s " +
       "JOIN s.parent d " +
       "JOIN d.parent p " +
       "WHERE p.code = :provinceCode AND p.level = 'PROVINCE'")
List<User> findAllByProvinceCode(@Param("provinceCode") String provinceCode);
```

#### Using Province Name:
```java
@Query("SELECT u FROM User u " +
       "JOIN u.institution i " +
       "JOIN i.location v " +
       "JOIN v.parent c " +
       "JOIN c.parent s " +
       "JOIN s.parent d " +
       "JOIN d.parent p " +
       "WHERE p.name = :provinceName AND p.level = 'PROVINCE'")
List<User> findAllByProvinceName(@Param("provinceName") String provinceName);
```

**Query Logic Explanation:**
- Uses JPQL (Java Persistence Query Language) with explicit JOINs
- Navigates through location hierarchy:
  - User → Institution → Location(Village) → Cell → Sector → District → Province
- Each JOIN moves up one level in the hierarchy
- `:provinceCode` is a named parameter bound using `@Param`
- Spring Data JPA translates this to SQL with proper JOINs

**Generated SQL (approximate):**
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN locations v ON i.location_id = v.id
JOIN locations c ON v.parent_id = c.id
JOIN locations s ON c.parent_id = s.id
JOIN locations d ON s.parent_id = d.id
JOIN locations p ON d.parent_id = p.id
WHERE p.code = ? AND p.level = 'PROVINCE'
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

## Adjacency List Pattern Implementation

### What is Adjacency List?
A design pattern where hierarchical data is stored in a single table with a self-referencing foreign key (`parent_id`).

### Location Table Structure:
```
locations:
- id (Primary Key)
- code (Unique)
- name
- level (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
- parent_id (Foreign Key → locations.id)
```

### Hierarchy Example:
```
Kigali (PROVINCE, parent_id=NULL)
  └─ Gasabo (DISTRICT, parent_id=1)
      └─ Remera (SECTOR, parent_id=6)
          └─ Kisimenti (CELL, parent_id=14)
              └─ Gikondo (VILLAGE, parent_id=21)
```

### Benefits:
- ✅ Single table instead of 5 separate tables
- ✅ Easier to add new location levels
- ✅ Cleaner code with single Location entity
- ✅ Follows lecturer's requirement perfectly

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
spring.datasource.username=postgres
spring.datasource.password=password
```

3. Run migration script:
```bash
psql -U postgres -d verifydocs_db -f migration_to_adjacency_list.sql
```

4. Load Rwanda location data:
```bash
psql -U postgres -d verifydocs_db -f rwanda_locations_adjacency.sql
```

5. Load sample institutions:
```bash
psql -U postgres -d verifydocs_db -f sample_institutions.sql
```

### Running the Application
1. Navigate to project directory
2. Run: `mvn clean install`
3. Run: `mvn spring-boot:run`
4. Application starts on: `http://localhost:8080`

---

## Testing with Postman

### 1. Create Location
```
POST http://localhost:8080/api/locations
Body (JSON):
{
    "code": "RTSR",
    "name": "Rutsiro",
    "level": "DISTRICT",
    "parent": {"id": 3}
}
```

### 2. Create Institution (requires villageCode)
```
POST http://localhost:8080/api/institutions?villageCode=GKND
Body (JSON):
{
    "name": "AUCA",
    "email": "info@auca.ac.rw"
}
```

### 3. Create Institution Profile (One-to-One)
```
POST http://localhost:8080/api/institutions/1/profile
Body (JSON):
{
    "street": "KG 544 St",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```

### 4. Update Institution Profile
```
PUT http://localhost:8080/api/institutions/1/profile
Body (JSON):
{
    "street": "KG 544 St Updated",
    "phone": "+250788999999",
    "website": "www.auca.ac.rw",
    "description": "Updated description"
}
```

### 5. Create User
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

### 6. Create Document
```
POST http://localhost:8080/api/documents
Body (JSON):
{
    "documentType": "Bachelor Degree Certificate",
    "recipientName": "John Doe",
    "filePath": "/documents/john_degree.pdf",
    "institution": {"id": 1}
}
```

### 7. Verify Document (Path Variable)
```
GET http://localhost:8080/api/documents/verify/3C627BD5
```

### 8. Verify Document (Query Parameter)
```
GET http://localhost:8080/api/documents/verify?verification_code=3C627BD5
```

### 9. Get Users by Province Code
```
GET http://localhost:8080/api/users/province/code/WST
```

### 10. Get Institutions by District
```
GET http://localhost:8080/api/institutions/location/district/Rubavu
```

### 11. Get Users with Pagination
```
GET http://localhost:8080/api/users/paginated?page=0&size=10
```

### 12. Get Users with Sorting
```
GET http://localhost:8080/api/users/sorted?sortBy=email
```

---

## Project Structure
```
src/main/java/com/verifydocs/
├── entity/              # JPA Entities
│   ├── Location.java (Adjacency List)
│   ├── Institution.java
│   ├── InstitutionProfile.java
│   ├── User.java
│   ├── Document.java
│   └── VerificationLog.java
├── repository/          # Spring Data JPA Repositories
│   ├── LocationRepository.java
│   ├── InstitutionRepository.java
│   ├── InstitutionProfileRepository.java
│   ├── UserRepository.java
│   ├── DocumentRepository.java
│   └── VerificationLogRepository.java
├── service/            # Business Logic
│   ├── LocationService.java
│   ├── InstitutionService.java
│   ├── UserService.java
│   └── DocumentService.java
├── controller/         # REST API Controllers
│   ├── LocationController.java
│   ├── InstitutionController.java
│   ├── UserController.java
│   └── DocumentController.java
├── exception/          # Exception Handling
│   └── GlobalExceptionHandler.java
└── VerifyDocsApplication.java  # Main Application
```

---

## Key Features Implemented

✅ 7 Entity tables with proper relationships  
✅ Adjacency List pattern for location hierarchy  
✅ Location saving functionality with validation  
✅ Pagination using Pageable  
✅ Sorting using Sort  
✅ Many-to-Many relationship (User ↔ Document)  
✅ One-to-Many relationships (Location → Location, Institution → User, Document → VerificationLog)  
✅ One-to-One relationship (Institution ↔ InstitutionProfile)  
✅ existBy() methods for existence checking  
✅ Retrieve users/institutions by any location level with JPQL queries  
✅ Document verification with SHA-256 hashing  
✅ Global exception handling with custom error messages  
✅ Profile create and update endpoints  

---



## Author
Pacifique Harerimana  
Student ID: 26937  
AUCA - Web Technology and Internet Course  
Midterm Project
