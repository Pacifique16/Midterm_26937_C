# Viva-Voce Presentation Outline

## 🎯 Presentation Structure (Recommended 10-15 minutes)

---

## PART 1: Project Overview (2 minutes)

### Slide 1: Introduction
"Good morning/afternoon. I'm presenting VerifyDocs, a Secure Digital Document Verification System."

**Key Points:**
- Purpose: Enable institutions to issue and verify digital documents
- Technology: Spring Boot, PostgreSQL, REST API
- Features: Cryptographic hashing, unique verification codes

### Slide 2: System Architecture
"The system follows a layered architecture:"
- **Entity Layer**: 6 entities representing database tables
- **Repository Layer**: Data access with Spring Data JPA
- **Service Layer**: Business logic and validation
- **Controller Layer**: REST API endpoints

---

## PART 2: Database Design - ERD (3 minutes)

### Slide 3: Entity Relationship Diagram
"I have implemented 6 main entities creating 7 database tables:"

**Show ERD_DOCUMENTATION.md diagram**

**Tables:**
1. Provinces (Location data)
2. Institutions
3. Institution_Profiles
4. Users
5. Documents
6. Verification_Logs
7. User_Document_Access (Join table)

### Slide 4: Relationships Explained
"Three types of relationships are implemented:"

**One-to-Many (4 examples):**
- Province → Institution
- Institution → User
- Institution → Document
- Document → VerificationLog

**One-to-One:**
- Institution ↔ InstitutionProfile

**Many-to-Many:**
- User ↔ Document (via join table)

---

## PART 3: Core Features Demonstration (5 minutes)

### Feature 1: Location Saving (30 seconds)
**Show in Postman:**
```
POST http://localhost:8080/api/provinces
{
    "code": "KGL",
    "name": "Kigali"
}
```

**Explain:**
"The saveProvince() method first checks if the province exists using existsByCode(), then saves it using JPA. The data is stored in the provinces table with an auto-generated ID."

### Feature 2: existBy() Method (30 seconds)
**Show in Postman:**
```
GET http://localhost:8080/api/provinces/exists/KGL
Response: true
```

**Explain:**
"This is a Spring Data JPA query derivation method. It generates SQL 'SELECT COUNT(*) > 0' and returns a boolean. It's more efficient than fetching the entire entity."

### Feature 3: One-to-One Relationship (1 minute)
**Show in Postman:**
```
POST http://localhost:8080/api/institutions/1/profile
{
    "address": "KG 544 St",
    "phone": "+250788123456"
}
```

**Explain:**
"Institution and InstitutionProfile have a One-to-One relationship. The institution_id in the profile table has a UNIQUE constraint, ensuring only one profile per institution."

**Show in Database:**
```sql
SELECT * FROM institution_profiles;
```

### Feature 4: Pagination (1 minute)
**Show in Postman:**
```
GET http://localhost:8080/api/users/paginated?page=0&size=2
```

**Explain:**
"Pagination uses Spring Data JPA's Pageable interface. PageRequest.of(page, size) creates a pageable object. The response includes the data, total pages, and total elements."

**Benefits:**
- Reduces memory usage
- Improves response time
- Enables efficient navigation

### Feature 5: Sorting (1 minute)
**Show in Postman:**
```
GET http://localhost:8080/api/users/sorted?sortBy=email
```

**Explain:**
"Sorting uses Spring Data JPA's Sort class. Sort.by('email').ascending() creates a sort object that Spring translates to SQL ORDER BY."

**Show Combined:**
```
GET http://localhost:8080/api/users/paginated-sorted?page=0&size=5&sortBy=fullName
```

### Feature 6: Province-Based User Retrieval (1.5 minutes)
**Show in Postman:**
```
GET http://localhost:8080/api/users/province/code/KGL
```

**Explain:**
"This uses a custom JPQL query that navigates relationships:"
```java
@Query("SELECT u FROM User u WHERE u.institution.province.code = :provinceCode")
```

**Navigation:**
- User → Institution (via institution field)
- Institution → Province (via province field)
- Province → code (via code field)

**Show Generated SQL:**
"Spring Data JPA automatically creates JOINs:"
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = 'KGL'
```

**Also works with province name:**
```
GET http://localhost:8080/api/users/province/name/Kigali
```

---

## PART 4: Relationships Deep Dive (3 minutes)

### Many-to-Many Relationship (1 minute)
**Show Code:**
```java
// In User.java
@ManyToMany
@JoinTable(
    name = "user_document_access",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "document_id")
)
private Set<Document> accessibleDocuments;
```

**Show Database:**
```sql
SELECT * FROM user_document_access;
```

**Explain:**
"JPA automatically creates the join table with two foreign keys. The composite primary key is (user_id, document_id). This allows users to access multiple documents and vice versa."

### One-to-Many Relationship (1 minute)
**Show Code:**
```java
// In Institution.java
@OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
private List<User> users;

// In User.java
@ManyToOne
@JoinColumn(name = "institution_id", nullable = false)
private Institution institution;
```

**Show Database:**
```sql
SELECT u.*, i.name as institution_name 
FROM users u 
JOIN institutions i ON u.institution_id = i.id;
```

**Explain:**
"The users table has a foreign key institution_id referencing institutions.id. The cascade operation means if I delete an institution, all its users are also deleted."

### One-to-One Relationship (1 minute)
**Show Database:**
```sql
\d institution_profiles
```

**Point out:**
"Notice the UNIQUE constraint on institution_id. This is what makes it One-to-One instead of Many-to-One."

---

## PART 5: Technical Questions Preparation (2 minutes)

### Be Ready to Answer:

**Q: Why did you use PostgreSQL?**
A: "PostgreSQL is robust, supports complex relationships, has excellent performance, and is widely used in production environments."

**Q: What is the difference between JPQL and SQL?**
A: "JPQL queries Java objects and their properties, while SQL queries database tables and columns. JPQL is database-independent."

**Q: How does cascade work?**
A: "Cascade operations propagate actions from parent to child entities. CascadeType.ALL means operations like delete cascade to related entities."

**Q: What would you improve?**
A: "I would add JWT authentication, file upload functionality, comprehensive error handling, unit tests, and more detailed API documentation."

**Q: How do you ensure data integrity?**
A: "Through foreign key constraints, unique constraints, not-null constraints, and validation in the service layer using existBy() methods."

---

## PART 6: Live Demonstration Checklist

### Before Starting:
- [ ] Application is running (mvn spring-boot:run)
- [ ] PostgreSQL is running
- [ ] Postman is open with collection loaded
- [ ] Database client is open (pgAdmin or psql)
- [ ] Code editor is open with project

### Demonstration Flow:
1. **Show running application** (console output)
2. **Show database tables** (\dt in PostgreSQL)
3. **Create Province** (Postman)
4. **Check exists** (Postman)
5. **Show in database** (SELECT * FROM provinces)
6. **Create Institution** (Postman)
7. **Create Profile** (Postman - One-to-One)
8. **Create 3-5 Users** (Postman)
9. **Show pagination** (Postman)
10. **Show sorting** (Postman)
11. **Show province query** (Postman)
12. **Show join table** (SELECT * FROM user_document_access)
13. **Show foreign keys** (Database schema)

---

## PART 7: Code Walkthrough (if requested)

### Be Ready to Navigate to:

**Entity Example:**
- Open `Province.java`
- Explain @Entity, @Table, @Id, @GeneratedValue
- Show @OneToMany relationship

**Repository Example:**
- Open `UserRepository.java`
- Explain JpaRepository extension
- Show existsByEmail() method
- Show @Query with JPQL

**Service Example:**
- Open `ProvinceService.java`
- Explain saveProvince() method
- Show existsByCode() usage
- Explain validation logic

**Controller Example:**
- Open `ProvinceController.java`
- Explain @RestController, @RequestMapping
- Show @PostMapping, @GetMapping
- Explain ResponseEntity

---

## PART 8: Closing (1 minute)

### Summary:
"In summary, I have implemented:"
- ✅ 6 entities with 7 database tables
- ✅ All three types of relationships
- ✅ Location saving with validation
- ✅ Pagination and sorting
- ✅ Custom JPQL queries
- ✅ existBy() methods
- ✅ RESTful API design
- ✅ Complete documentation

### Confidence Statement:
"The project meets all 30 marks requirements and is fully functional. I'm ready to answer any questions."

---

## 🎯 Quick Reference Card (Keep Handy)

### Key Numbers:
- **6 Entities**: Province, Institution, InstitutionProfile, User, Document, VerificationLog
- **7 Tables**: Including user_document_access join table
- **4 One-to-Many**: Province→Institution, Institution→User, Institution→Document, Document→Log
- **1 One-to-One**: Institution↔Profile
- **1 Many-to-Many**: User↔Document
- **4 existBy()**: In Province, Institution, User, Document repositories
- **2 Province Queries**: By code and by name

### Key Endpoints:
- POST /api/provinces - Location saving
- GET /api/provinces/exists/{code} - existBy
- POST /api/institutions/{id}/profile - One-to-One
- GET /api/users/paginated - Pagination
- GET /api/users/sorted - Sorting
- GET /api/users/province/code/{code} - Province query

### Key Concepts:
- **Pagination**: PageRequest.of(page, size) → Page<T>
- **Sorting**: Sort.by(field).ascending() → List<T>
- **existBy()**: Query derivation, returns boolean
- **JPQL**: u.institution.province.code (relationship navigation)
- **Join Table**: user_document_access with composite PK

---

## 💡 Tips for Success

### Do's:
✅ Speak clearly and confidently
✅ Use technical terms correctly
✅ Reference specific code files
✅ Show working examples in Postman
✅ Explain the "why" not just the "what"
✅ Draw diagrams if helpful
✅ Admit if you don't know something

### Don'ts:
❌ Rush through explanations
❌ Use vague terms like "it just works"
❌ Skip over important concepts
❌ Get defensive about questions
❌ Forget to demonstrate features
❌ Ignore the rubric requirements

---

## 🎤 Sample Opening Statement

"Good morning/afternoon. Today I'm presenting VerifyDocs, a Secure Digital Document Verification System built with Spring Boot and PostgreSQL.

The system addresses the real-world problem of document forgery by using cryptographic hashing and unique verification codes. Institutions can issue documents, and third parties can verify their authenticity.

I have implemented all 30 marks requirements including:
- An ERD with 6 entities and 7 database tables
- All three types of relationships: One-to-Many, One-to-One, and Many-to-Many
- Location saving through the Province entity
- Pagination and sorting using Spring Data JPA
- Custom JPQL queries for province-based user retrieval
- existBy() methods for validation

The application is fully functional and ready for demonstration. Let me start by showing you the database structure..."

---

## 🎤 Sample Closing Statement

"In conclusion, the VerifyDocs system successfully implements all required features:
- Complete ERD with proper relationships
- Location saving with validation
- Efficient pagination and sorting
- Complex queries with relationship navigation
- RESTful API design following best practices

The system is production-ready and demonstrates a solid understanding of Spring Boot, JPA, and database design principles.

Thank you for your time. I'm ready to answer any questions you may have."

---

## 📋 Final Checklist Before Presentation

- [ ] Application runs without errors
- [ ] Database has sample data
- [ ] Postman collection is loaded
- [ ] All endpoints tested and working
- [ ] Code is clean and commented
- [ ] Documentation is accessible
- [ ] Confident about all concepts
- [ ] Ready to navigate code quickly
- [ ] Prepared for follow-up questions
- [ ] Relaxed and confident

---

**Remember**: You built this system. You understand it. Be confident! 🎓

**Good luck with your presentation! 🚀**
