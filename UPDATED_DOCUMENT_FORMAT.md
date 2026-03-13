# 📝 Updated Document Format - Testing Guide

## ✅ Changes Made

### Before (Not Professional):
```json
{
    "title": "Bachelor of Computer Science - Alice Johnson"
}
```

### After (Professional):
```json
{
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson"
}
```

---

## 🧪 Updated API Testing Examples

### 1. Create Document (New Format)

**Endpoint**: `POST http://localhost:8080/api/documents`

**Request Body**:
```json
{
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson",
    "filePath": "/uploads/alice_degree_2024.pdf",
    "hashValue": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6",
    "institution": {
        "id": 1
    }
}
```

**Response**:
```json
{
    "id": 1,
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson",
    "verificationCode": "ABC12345",
    "issueDate": "2024-01-15T10:30:00",
    "institution": {
        "id": 1,
        "name": "AUCA",
        "email": "admin@auca.ac.rw"
    }
}
```

---

### 2. Verify Document (New Formatted Response)

**Endpoint**: `GET http://localhost:8080/api/documents/verify/ABC12345?ipAddress=192.168.1.1`

**Response**:
```
═══════════════════════════════════════════════════════
           DOCUMENT VERIFICATION RESULT
═══════════════════════════════════════════════════════

Status: ✓ SUCCESS - Document is Authentic

───────────────────────────────────────────────────────
DOCUMENT DETAILS
───────────────────────────────────────────────────────

Document Type: Bachelor of Computer Science

Awarded to: Alice Johnson

Issue Date: 2024-01-15

───────────────────────────────────────────────────────
ISSUED BY
───────────────────────────────────────────────────────

Institution: AUCA
Email: admin@auca.ac.rw

───────────────────────────────────────────────────────
VERIFICATION INFO
───────────────────────────────────────────────────────

Verification Code: ABC12345
Verified On: 2024-02-20

═══════════════════════════════════════════════════════
     Thank you for using VerifyDocs System
═══════════════════════════════════════════════════════
```

---

## 📋 More Document Examples

### Example 1: Master's Degree
```json
{
    "documentType": "Master of Business Administration",
    "recipientName": "Bob Williams",
    "filePath": "/uploads/bob_mba_2024.pdf",
    "hashValue": "b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7",
    "institution": {
        "id": 1
    }
}
```

### Example 2: Transcript
```json
{
    "documentType": "Academic Transcript",
    "recipientName": "Carol Davis",
    "filePath": "/uploads/carol_transcript_2024.pdf",
    "hashValue": "c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8",
    "institution": {
        "id": 1
    }
}
```

### Example 3: Certificate of Completion
```json
{
    "documentType": "Certificate of Completion - Web Development",
    "recipientName": "David Miller",
    "filePath": "/uploads/david_cert_2024.pdf",
    "hashValue": "d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9",
    "institution": {
        "id": 1
    }
}
```

### Example 4: Diploma
```json
{
    "documentType": "Diploma in Information Technology",
    "recipientName": "Emma Wilson",
    "filePath": "/uploads/emma_diploma_2024.pdf",
    "hashValue": "e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9d0",
    "institution": {
        "id": 1
    }
}
```

---

## 🔄 Migration Steps

### Step 1: Drop Old Documents Table
```sql
DROP TABLE IF EXISTS verification_logs CASCADE;
DROP TABLE IF EXISTS user_document_access CASCADE;
DROP TABLE IF EXISTS documents CASCADE;
```

### Step 2: Recreate with New Schema
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    document_type VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    hash_value VARCHAR(255) UNIQUE NOT NULL,
    verification_code VARCHAR(255) UNIQUE NOT NULL,
    issue_date TIMESTAMP NOT NULL,
    institution_id BIGINT NOT NULL,
    FOREIGN KEY (institution_id) REFERENCES institutions(id)
);

CREATE TABLE verification_logs (
    id BIGSERIAL PRIMARY KEY,
    verification_time TIMESTAMP NOT NULL,
    ip_address VARCHAR(255),
    result VARCHAR(255) NOT NULL,
    document_id BIGINT NOT NULL,
    FOREIGN KEY (document_id) REFERENCES documents(id)
);

CREATE TABLE user_document_access (
    user_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, document_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (document_id) REFERENCES documents(id)
);
```

---

## 🧪 Complete Testing Sequence

### 1. Create Institution
```
POST /api/institutions?provinceCode=KGL
{
    "name": "AUCA",
    "email": "admin@auca.ac.rw"
}
```

### 2. Create User
```
POST /api/users
{
    "email": "registrar@auca.ac.rw",
    "password": "secure123",
    "role": "ADMIN",
    "fullName": "John Doe",
    "institution": {"id": 1}
}
```

### 3. Issue Document (New Format)
```
POST /api/documents
{
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson",
    "filePath": "/uploads/alice_degree_2024.pdf",
    "hashValue": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
    "institution": {"id": 1}
}
```

### 4. Verify Document (See Formatted Output)
```
GET /api/documents/verify/ABC12345?ipAddress=192.168.1.1
```

---

## ✅ Benefits of New Format

1. **Professional Separation**
   - Document type is separate from recipient name
   - Cleaner data structure
   - Easier to query by document type

2. **Better Verification Display**
   - Formatted output looks professional
   - Clear sections for different information
   - Easy to read and understand

3. **Database Queries**
   - Can search by document type
   - Can search by recipient name
   - More flexible reporting

4. **Real-World Usage**
   - Matches how real certificates work
   - Professional presentation
   - Better user experience

---

## 📊 Database Schema Changes

### Old Schema:
```
documents
├─ id
├─ title (combined: "Bachelor - Alice Johnson")
├─ file_path
├─ hash_value
├─ verification_code
├─ issue_date
└─ institution_id
```

### New Schema:
```
documents
├─ id
├─ document_type ("Bachelor of Computer Science")
├─ recipient_name ("Alice Johnson")
├─ file_path
├─ hash_value
├─ verification_code
├─ issue_date
└─ institution_id
```

---

## 🎯 Next Steps

1. **Stop Application** (if running)
2. **Update Database** (run migration SQL)
3. **Restart Application**
4. **Test with New Format** (use examples above)
5. **Verify Formatted Output** (check verification response)

---

**The new format is much more professional! 🎉**
