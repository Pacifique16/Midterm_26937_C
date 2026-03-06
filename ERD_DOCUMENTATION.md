# Entity Relationship Diagram (ERD) Documentation

## Database Tables Overview

### 1. PROVINCES (Location Table)
**Purpose:** Stores location/province data

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| code | VARCHAR(255) | UNIQUE, NOT NULL |
| name | VARCHAR(255) | NOT NULL |

**Relationships:**
- One Province → Many Institutions (One-to-Many)

---

### 2. INSTITUTIONS
**Purpose:** Stores institution information

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| status | VARCHAR(255) | NOT NULL |
| province_id | BIGINT | FOREIGN KEY → provinces(id), NOT NULL |

**Relationships:**
- Many Institutions → One Province (Many-to-One)
- One Institution → Many Users (One-to-Many)
- One Institution → Many Documents (One-to-Many)
- One Institution → One InstitutionProfile (One-to-One)

---

### 3. INSTITUTION_PROFILES
**Purpose:** Stores detailed institution profile information

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| address | VARCHAR(255) | |
| phone | VARCHAR(255) | |
| website | VARCHAR(255) | |
| description | TEXT | |
| institution_id | BIGINT | FOREIGN KEY → institutions(id), UNIQUE, NOT NULL |

**Relationships:**
- One InstitutionProfile → One Institution (One-to-One)

**One-to-One Explanation:**
- The `institution_id` column has a UNIQUE constraint
- This ensures only one profile per institution
- The relationship is bidirectional (can navigate from both sides)

---

### 4. USERS
**Purpose:** Stores user account information

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| role | VARCHAR(255) | NOT NULL |
| full_name | VARCHAR(255) | |
| institution_id | BIGINT | FOREIGN KEY → institutions(id), NOT NULL |

**Relationships:**
- Many Users → One Institution (Many-to-One)
- Many Users ↔ Many Documents (Many-to-Many via user_document_access)

---

### 5. DOCUMENTS
**Purpose:** Stores document metadata and verification information

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| title | VARCHAR(255) | NOT NULL |
| file_path | VARCHAR(255) | NOT NULL |
| hash_value | VARCHAR(255) | UNIQUE, NOT NULL |
| verification_code | VARCHAR(255) | UNIQUE, NOT NULL |
| issue_date | TIMESTAMP | NOT NULL |
| institution_id | BIGINT | FOREIGN KEY → institutions(id), NOT NULL |

**Relationships:**
- Many Documents → One Institution (Many-to-One)
- Many Documents ↔ Many Users (Many-to-Many via user_document_access)
- One Document → Many VerificationLogs (One-to-Many)

---

### 6. VERIFICATION_LOGS
**Purpose:** Stores document verification history

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| verification_time | TIMESTAMP | NOT NULL |
| ip_address | VARCHAR(255) | |
| result | VARCHAR(255) | NOT NULL |
| document_id | BIGINT | FOREIGN KEY → documents(id), NOT NULL |

**Relationships:**
- Many VerificationLogs → One Document (Many-to-One)

---

### 7. USER_DOCUMENT_ACCESS (Join Table)
**Purpose:** Implements Many-to-Many relationship between Users and Documents

| Column | Type | Constraints |
|--------|------|-------------|
| user_id | BIGINT | FOREIGN KEY → users(id), NOT NULL |
| document_id | BIGINT | FOREIGN KEY → documents(id), NOT NULL |
| PRIMARY KEY | | (user_id, document_id) |

**Many-to-Many Explanation:**
- This is a join table created automatically by JPA
- Contains two foreign keys forming a composite primary key
- Allows users to access multiple documents
- Allows documents to be accessed by multiple users
- No additional columns needed for basic Many-to-Many

---

## Relationship Summary

### One-to-Many Relationships (3 examples):
1. **Province → Institution**
   - One province contains many institutions
   - Foreign Key: `institutions.province_id`

2. **Institution → User**
   - One institution has many users
   - Foreign Key: `users.institution_id`

3. **Institution → Document**
   - One institution issues many documents
   - Foreign Key: `documents.institution_id`

4. **Document → VerificationLog**
   - One document has many verification logs
   - Foreign Key: `verification_logs.document_id`

### One-to-One Relationship:
**Institution ↔ InstitutionProfile**
- One institution has exactly one profile
- Foreign Key: `institution_profiles.institution_id` (UNIQUE)

### Many-to-Many Relationship:
**User ↔ Document**
- Users can access multiple documents
- Documents can be accessed by multiple users
- Join Table: `user_document_access`
- Foreign Keys: `user_id`, `document_id`

---

## Visual ERD Representation

```
┌─────────────┐
│  PROVINCES  │
│─────────────│
│ id (PK)     │
│ code (UQ)   │
│ name        │
└──────┬──────┘
       │ 1
       │
       │ N
┌──────┴──────────┐
│  INSTITUTIONS   │
│─────────────────│
│ id (PK)         │
│ name            │
│ email (UQ)      │
│ status          │
│ province_id(FK) │
└──────┬──────────┘
       │ 1
       ├─────────────────────┐
       │                     │
       │ N                   │ 1
┌──────┴──────┐    ┌─────────┴──────────────┐
│    USERS    │    │ INSTITUTION_PROFILES   │
│─────────────│    │────────────────────────│
│ id (PK)     │    │ id (PK)                │
│ email (UQ)  │    │ address                │
│ password    │    │ phone                  │
│ role        │    │ website                │
│ full_name   │    │ description            │
│ instit..FK  │    │ institution_id (FK,UQ) │
└──────┬──────┘    └────────────────────────┘
       │ N
       │
       │ N (Many-to-Many)
       │
┌──────┴────────────────┐
│ USER_DOCUMENT_ACCESS  │
│───────────────────────│
│ user_id (FK, PK)      │
│ document_id (FK, PK)  │
└──────┬────────────────┘
       │ N
       │
       │ N
┌──────┴──────────┐
│   DOCUMENTS     │
│─────────────────│
│ id (PK)         │
│ title           │
│ file_path       │
│ hash_value (UQ) │
│ verif_code (UQ) │
│ issue_date      │
│ instit..FK      │
└──────┬──────────┘
       │ 1
       │
       │ N
┌──────┴────────────┐
│ VERIFICATION_LOGS │
│───────────────────│
│ id (PK)           │
│ verif_time        │
│ ip_address        │
│ result            │
│ document_id (FK)  │
└───────────────────┘
```

Legend:
- PK = Primary Key
- FK = Foreign Key
- UQ = Unique Constraint
- 1 = One
- N = Many

---

## Cardinality Explanation

### One-to-Many (1:N)
- One record in parent table relates to multiple records in child table
- Child table has foreign key pointing to parent
- Example: One Institution has many Users

### One-to-One (1:1)
- One record in one table relates to exactly one record in another table
- Foreign key has UNIQUE constraint
- Example: One Institution has one Profile

### Many-to-Many (N:M)
- Multiple records in one table relate to multiple records in another table
- Requires a join table with two foreign keys
- Example: Users can access many Documents, Documents can be accessed by many Users

---

## Database Integrity Rules

1. **Referential Integrity**: All foreign keys must reference existing primary keys
2. **Cascade Operations**: Deleting an Institution cascades to Users and Documents
3. **Unique Constraints**: Prevent duplicate emails, codes, and verification codes
4. **Not Null Constraints**: Ensure critical fields always have values
5. **Composite Primary Key**: user_document_access uses (user_id, document_id) as primary key

---

## Query Examples for Relationships

### One-to-Many Query (Institution → Users):
```sql
SELECT u.* FROM users u
WHERE u.institution_id = 1;
```

### One-to-One Query (Institution → Profile):
```sql
SELECT i.*, ip.* FROM institutions i
LEFT JOIN institution_profiles ip ON i.id = ip.institution_id
WHERE i.id = 1;
```

### Many-to-Many Query (Users accessing a Document):
```sql
SELECT u.* FROM users u
JOIN user_document_access uda ON u.id = uda.user_id
WHERE uda.document_id = 1;
```

### Nested Relationship Query (Users by Province):
```sql
SELECT u.* FROM users u
JOIN institutions i ON u.institution_id = i.id
JOIN provinces p ON i.province_id = p.id
WHERE p.code = 'KGL';
```
