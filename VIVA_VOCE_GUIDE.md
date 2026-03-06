# Viva-Voce Preparation Guide (7 Marks)

## 1. Entity Relationship Diagram (ERD) Questions

### Q: Explain your ERD and the relationships between tables.

**Answer:**
"My system has 6 main tables plus 1 join table:

1. **Provinces** - stores location data with code and name
2. **Institutions** - stores institution information
3. **InstitutionProfiles** - stores detailed institution profiles
4. **Users** - stores user accounts
5. **Documents** - stores document metadata
6. **VerificationLogs** - stores verification history
7. **UserDocumentAccess** - join table for Many-to-Many relationship

The relationships are:
- Province to Institution: One-to-Many (one province has many institutions)
- Institution to User: One-to-Many (one institution has many users)
- Institution to Document: One-to-Many (one institution issues many documents)
- Institution to InstitutionProfile: One-to-One (one institution has one profile)
- User to Document: Many-to-Many (users can access multiple documents)
- Document to VerificationLog: One-to-Many (one document has many logs)"

---

## 2. Location Saving Implementation

### Q: How did you implement saving location data?

**Answer:**
"I implemented location saving through the Province entity and ProvinceService:

1. The Province entity has three fields: id, code, and name
2. The ProvinceService has a saveProvince() method
3. Before saving, I check if the province already exists using existsByCode()
4. If it doesn't exist, I save it using provinceRepository.save()
5. The relationship is handled through JPA - when an Institution is saved, it references a Province via the province_id foreign key

The API endpoint is POST /api/provinces with JSON body containing code and name."

---

## 3. Sorting Implementation

### Q: How did you implement sorting functionality?

**Answer:**
"I implemented sorting using Spring Data JPA's Sort class:

1. I import Sort from org.springframework.data.domain
2. I create a Sort object using Sort.by(fieldName)
3. I can specify direction with .ascending() or .descending()
4. I pass the Sort object to repository's findAll(Sort sort) method
5. Spring Data JPA generates the SQL ORDER BY clause automatically

Example code:
```java
Sort sort = Sort.by('email').ascending();
return userRepository.findAll(sort);
```

The API endpoint is GET /api/users/sorted?sortBy=email"

---

## 4. Pagination Implementation

### Q: How did you implement pagination and why is it important?

**Answer:**
"I implemented pagination using Spring Data JPA's Pageable interface:

1. I import Pageable and PageRequest from org.springframework.data.domain
2. I create a Pageable object using PageRequest.of(page, size)
3. page is the page number (starting from 0)
4. size is the number of records per page
5. I pass Pageable to repository's findAll(Pageable pageable) method
6. It returns a Page object containing the data and metadata

Example:
```java
Pageable pageable = PageRequest.of(0, 10);
return userRepository.findAll(pageable);
```

**Why it's important:**
- Reduces memory usage by loading only needed records
- Improves response time by limiting data transfer
- Enables efficient navigation through large datasets
- Prevents server overload with large result sets

The API endpoint is GET /api/users/paginated?page=0&size=10"

---

## 5. Many-to-Many Relationship

### Q: Explain your Many-to-Many relationship implementation.

**Answer:**
"I implemented Many-to-Many between User and Document:

**In User entity:**
```java
@ManyToMany
@JoinTable(
    name = 'user_document_access',
    joinColumns = @JoinColumn(name = 'user_id'),
    inverseJoinColumns = @JoinColumn(name = 'document_id')
)
private Set<Document> accessibleDocuments;
```

**In Document entity:**
```java
@ManyToMany(mappedBy = 'accessibleDocuments')
private Set<User> authorizedUsers;
```

**How it works:**
- JPA automatically creates a join table named user_document_access
- The join table has two foreign keys: user_id and document_id
- The composite primary key is (user_id, document_id)
- This allows users to access multiple documents and documents to be accessed by multiple users
- The mappedBy attribute indicates which side owns the relationship"

---

## 6. One-to-Many Relationship

### Q: Explain your One-to-Many relationship and foreign key usage.

**Answer:**
"I have several One-to-Many relationships. Let me explain Institution to User:

**In Institution entity (One side):**
```java
@OneToMany(mappedBy = 'institution', cascade = CascadeType.ALL)
private List<User> users;
```

**In User entity (Many side):**
```java
@ManyToOne
@JoinColumn(name = 'institution_id', nullable = false)
private Institution institution;
```

**How it works:**
- The User table has a foreign key column institution_id
- This foreign key references the id column in institutions table
- mappedBy = 'institution' means User entity owns the relationship
- cascade = CascadeType.ALL means operations on Institution cascade to Users
- nullable = false ensures every user must belong to an institution
- The foreign key creates a database constraint for referential integrity"

---

## 7. One-to-One Relationship

### Q: Explain your One-to-One relationship implementation.

**Answer:**
"I implemented One-to-One between Institution and InstitutionProfile:

**In Institution entity:**
```java
@OneToOne(mappedBy = 'institution', cascade = CascadeType.ALL)
private InstitutionProfile profile;
```

**In InstitutionProfile entity:**
```java
@OneToOne
@JoinColumn(name = 'institution_id', unique = true, nullable = false)
private Institution institution;
```

**How entities are connected:**
- InstitutionProfile has a foreign key institution_id
- The unique = true constraint ensures only one profile per institution
- This is what makes it One-to-One instead of Many-to-One
- mappedBy indicates InstitutionProfile owns the relationship
- Both entities can navigate to each other
- The unique constraint is enforced at database level"

---

## 8. existBy() Method

### Q: How does the existBy() method work in Spring Data JPA?

**Answer:**
"The existBy() method is a Spring Data JPA query derivation method:

**Declaration in Repository:**
```java
boolean existsByCode(String code);
boolean existsByEmail(String email);
```

**How it works:**
1. Spring Data JPA parses the method name
2. It recognizes the 'existsBy' prefix
3. It identifies the property name after 'By' (e.g., Code, Email)
4. It automatically generates SQL: SELECT COUNT(*) > 0 FROM table WHERE field = ?
5. Returns true if count > 0, false otherwise

**Benefits:**
- More efficient than fetching the entire entity
- Only checks existence without loading data
- Automatically generated - no need to write SQL
- Type-safe at compile time

**Usage example:**
```java
if (provinceRepository.existsByCode(code)) {
    throw new RuntimeException('Province already exists');
}
```

The API endpoint is GET /api/provinces/exists/{code}"

---

## 9. Retrieve Users by Province

### Q: Explain how you retrieve all users from a given province.

**Answer:**
"I implemented two methods - one using province code and one using province name:

**Repository methods:**
```java
@Query('SELECT u FROM User u WHERE u.institution.province.code = :provinceCode')
List<User> findAllByProvinceCode(@Param('provinceCode') String provinceCode);

@Query('SELECT u FROM User u WHERE u.institution.province.name = :provinceName')
List<User> findAllByProvinceName(@Param('provinceName') String provinceName);
```

**Query Logic:**
- Uses JPQL (Java Persistence Query Language)
- u is an alias for User entity
- u.institution.province.code navigates through relationships:
  * User → Institution (via institution field)
  * Institution → Province (via province field)
  * Province → code (via code field)
- :provinceCode is a named parameter bound using @Param

**Generated SQL:**
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = ?
```

Spring Data JPA automatically creates the JOINs based on the relationship navigation.

The API endpoints are:
- GET /api/users/province/code/KGL
- GET /api/users/province/name/Kigali"

---

## 10. Spring Boot Architecture

### Q: Explain the architecture of your Spring Boot application.

**Answer:**
"My application follows the layered architecture pattern:

**1. Entity Layer (Model):**
- JPA entities representing database tables
- Contains relationship mappings
- Located in com.verifydocs.entity package

**2. Repository Layer (Data Access):**
- Interfaces extending JpaRepository
- Contains custom query methods
- Spring Data JPA provides implementation
- Located in com.verifydocs.repository package

**3. Service Layer (Business Logic):**
- Contains business logic and validation
- Calls repository methods
- Handles transactions
- Located in com.verifydocs.service package

**4. Controller Layer (Presentation):**
- REST API endpoints
- Handles HTTP requests/responses
- Calls service methods
- Located in com.verifydocs.controller package

**Flow:**
Client → Controller → Service → Repository → Database

**Benefits:**
- Separation of concerns
- Easy to test each layer independently
- Maintainable and scalable
- Follows Spring Boot best practices"

---

## 11. JPA and Hibernate

### Q: What is JPA and how does it work in your project?

**Answer:**
"JPA (Java Persistence API) is a specification for object-relational mapping:

**In my project:**
- JPA maps Java objects (entities) to database tables
- Hibernate is the JPA implementation I'm using
- @Entity annotation marks a class as a database table
- @Id marks the primary key
- @OneToMany, @ManyToOne, etc. define relationships
- JPA automatically generates SQL queries

**Benefits:**
- No need to write SQL manually
- Database-independent code
- Automatic table creation with ddl-auto=update
- Type-safe queries with JPQL
- Handles relationships automatically"

---

## 12. REST API Design

### Q: Explain your REST API design.

**Answer:**
"I followed RESTful principles:

**Naming Convention:**
- /api/provinces - collection of provinces
- /api/provinces/{id} - specific province
- /api/users/province/code/{code} - filtered collection

**HTTP Methods:**
- POST - Create new resources
- GET - Retrieve resources
- PUT - Update resources (not implemented yet)
- DELETE - Delete resources (not implemented yet)

**Response Format:**
- JSON format
- HTTP status codes (200 OK, 404 Not Found, etc.)
- ResponseEntity for flexible responses

**Query Parameters:**
- ?page=0&size=10 for pagination
- ?sortBy=email for sorting

**Benefits:**
- Stateless communication
- Standard HTTP methods
- Easy to test with Postman
- Self-documenting with Swagger"

---

## 13. Database Transactions

### Q: How are transactions handled in your application?

**Answer:**
"Transactions are handled automatically by Spring:

- @Transactional annotation can be used on service methods
- Spring manages transaction boundaries
- If an exception occurs, the transaction is rolled back
- Multiple database operations are atomic
- JPA's cascade operations work within transactions

Example: When I save an Institution with cascade, all related entities are saved in one transaction."

---

## 14. Security Considerations

### Q: What security measures did you implement?

**Answer:**
"Current security measures:

1. **Data Validation:**
   - Unique constraints on emails and codes
   - Not null constraints on critical fields
   - existBy() checks before saving

2. **Cryptographic Hashing:**
   - SHA-256 for document integrity
   - Unique verification codes

3. **Planned Security:**
   - JWT authentication (configured but not fully implemented)
   - Spring Security for authorization
   - Password encryption with BCrypt
   - Role-based access control

4. **Database Security:**
   - Foreign key constraints
   - Referential integrity
   - Cascade operations controlled"

---

## 15. Testing Strategy

### Q: How would you test this application?

**Answer:**
"Testing approach:

**1. Unit Testing:**
- Test service methods with mock repositories
- Test repository methods with in-memory database

**2. Integration Testing:**
- Test complete flow from controller to database
- Use @SpringBootTest annotation

**3. API Testing:**
- Use Postman for manual testing
- Test all CRUD operations
- Test pagination and sorting
- Test relationship operations

**4. Database Testing:**
- Verify foreign key constraints
- Test cascade operations
- Verify unique constraints

I provided a Postman collection for easy API testing."

---

## Quick Reference Card

### Key Concepts to Remember:

1. **ERD**: 6 tables + 1 join table with 3 types of relationships
2. **Location Saving**: Province entity with existsByCode() validation
3. **Pagination**: PageRequest.of(page, size) → Page<T>
4. **Sorting**: Sort.by(field).ascending() → List<T>
5. **Many-to-Many**: @JoinTable creates user_document_access
6. **One-to-Many**: Foreign key in child table
7. **One-to-One**: Foreign key with unique constraint
8. **existBy()**: Query derivation, returns boolean
9. **Province Query**: JPQL with relationship navigation
10. **Architecture**: Controller → Service → Repository → Database

### Confidence Boosters:

- "I can demonstrate this in Postman"
- "Let me show you the code"
- "The database schema shows this clearly"
- "This follows Spring Boot best practices"
- "I tested this functionality and it works"

---

## Common Follow-up Questions

**Q: Why did you choose PostgreSQL?**
A: "PostgreSQL is robust, supports complex relationships, has good performance, and is widely used in production environments."

**Q: What would you improve?**
A: "I would add JWT authentication, file upload functionality, comprehensive error handling, unit tests, and API documentation with Swagger."

**Q: How does cascade work?**
A: "Cascade operations propagate actions from parent to child entities. For example, CascadeType.ALL means if I delete an Institution, all its Users are also deleted."

**Q: What is the difference between JPQL and SQL?**
A: "JPQL queries Java objects and their properties, while SQL queries database tables and columns. JPQL is database-independent."

---

## Final Tips for Viva-Voce

1. **Be Confident**: You built this, you understand it
2. **Use Examples**: Reference specific code or API endpoints
3. **Draw Diagrams**: Sketch relationships if needed
4. **Admit Gaps**: If you don't know something, say "I would research that"
5. **Show Enthusiasm**: Demonstrate your interest in the project
6. **Be Prepared to Demo**: Have the application running and Postman ready
7. **Know Your Code**: Be able to navigate to any file quickly
8. **Explain Simply**: Use clear, non-technical language when possible
9. **Connect to Real-World**: Relate features to actual use cases
10. **Stay Calm**: Take a breath before answering complex questions

Good luck with your viva-voce! 🎓
