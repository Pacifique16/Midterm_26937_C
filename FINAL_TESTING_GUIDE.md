# 🎯 Final Testing Guide - Updated Format

## ✅ What Changed

### 1. Document Structure
**Before**: `title` (combined field)  
**After**: `documentType` + `recipientName` (separate fields)

### 2. Institution Profile Location
**Before**: Single `address` field  
**After**: Detailed location fields (street, district, sector, cell, village, province)  
**Display**: Only street, district, and province shown

### 3. Verification Response
**Before**: Complex formatted output  
**After**: Simple, clean output

---

## 🧪 Complete Testing Sequence

### Step 1: Create Institution Profile (With Full Location)

**Endpoint**: `POST http://localhost:8080/api/institutions/1/profile`

**Request Body**:
```json
{
    "street": "KG 544 St",
    "district": "Gasabo",
    "sector": "Remera",
    "cell": "Rukiri",
    "village": "Amahoro",
    "provinceName": "Kigali",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```

**What's Stored**:
- ✅ All location fields saved (street, district, sector, cell, village, province)
- ✅ Phone, website, description saved

**What's Displayed** (in verification):
- ✅ Only: Street, District, Province
- ❌ Hidden: Sector, Cell, Village

---

### Step 2: Create Document (New Format)

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
    "verificationCode": "A1B2C3D4",
    "issueDate": "2024-02-20T10:30:00",
    "institution": {
        "id": 1,
        "name": "AUCA",
        "email": "admin@auca.ac.rw"
    }
}
```

**Note the verification code**: `A1B2C3D4` (use this in next step)

---

### Step 3: Verify Document (See Clean Output)

**Endpoint**: `GET http://localhost:8080/api/documents/verify/A1B2C3D4?ipAddress=192.168.1.1`

**Expected Response**:
```
SUCCESS - Document is authentic

Document Type: Bachelor of Computer Science
Recipient Name: Alice Johnson
Issue Date: 2024-02-20

Institution: AUCA
Email: admin@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

**Perfect!** ✅ Clean, simple, professional

---

## 📋 More Document Examples

### Example 1: Master's Degree
```json
{
    "documentType": "Master of Business Administration",
    "recipientName": "Bob Williams",
    "filePath": "/uploads/bob_mba_2024.pdf",
    "hashValue": "b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7",
    "institution": {"id": 1}
}
```

### Example 2: Academic Transcript
```json
{
    "documentType": "Academic Transcript",
    "recipientName": "Carol Davis",
    "filePath": "/uploads/carol_transcript_2024.pdf",
    "hashValue": "c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8",
    "institution": {"id": 1}
}
```

### Example 3: Certificate
```json
{
    "documentType": "Certificate of Completion - Web Development",
    "recipientName": "David Miller",
    "filePath": "/uploads/david_cert_2024.pdf",
    "hashValue": "d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8c9",
    "institution": {"id": 1}
}
```

---

## 🗺️ Location Examples for Different Institutions

### AUCA (Kigali)
```json
{
    "street": "KG 544 St",
    "district": "Gasabo",
    "sector": "Remera",
    "cell": "Rukiri",
    "village": "Amahoro",
    "provinceName": "Kigali",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```

### UR (Huye)
```json
{
    "street": "KK 737 St",
    "district": "Huye",
    "sector": "Tumba",
    "cell": "Karama",
    "village": "Matyazo",
    "provinceName": "Southern",
    "phone": "+250788654321",
    "website": "www.ur.ac.rw",
    "description": "University of Rwanda"
}
```

### KIST (Kigali)
```json
{
    "street": "KN 67 St",
    "district": "Nyarugenge",
    "sector": "Kigali",
    "cell": "Nyarugenge",
    "village": "Ubumwe",
    "provinceName": "Kigali",
    "phone": "+250788111222",
    "website": "www.kist.ac.rw",
    "description": "Kigali Institute of Science and Technology"
}
```

---

## 🔄 Migration Steps

### Step 1: Stop Application
```bash
Ctrl + C
```

### Step 2: Run Migration Script
```bash
psql -U postgres -d verifydocs_db -f complete_migration.sql
```

### Step 3: Restart Application
```bash
mvn spring-boot:run
```

### Step 4: Test New Format
Follow the testing sequence above

---

## ✅ Verification Output Examples

### Example 1: Bachelor's Degree
```
SUCCESS - Document is authentic

Document Type: Bachelor of Computer Science
Recipient Name: Alice Johnson
Issue Date: 2024-02-20

Institution: AUCA
Email: admin@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

### Example 2: Master's Degree
```
SUCCESS - Document is authentic

Document Type: Master of Business Administration
Recipient Name: Bob Williams
Issue Date: 2024-02-20

Institution: AUCA
Email: admin@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

### Example 3: Certificate
```
SUCCESS - Document is authentic

Document Type: Certificate of Completion - Web Development
Recipient Name: David Miller
Issue Date: 2024-02-20

Institution: AUCA
Email: admin@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

---

## 📊 Database Schema Summary

### institution_profiles Table:
```
Columns:
- id (Primary Key)
- street (displayed)
- district (displayed)
- sector (stored, not displayed)
- cell (stored, not displayed)
- village (stored, not displayed)
- province_name (displayed)
- phone
- website
- description
- institution_id (Foreign Key, Unique)
```

### documents Table:
```
Columns:
- id (Primary Key)
- document_type (e.g., "Bachelor of Computer Science")
- recipient_name (e.g., "Alice Johnson")
- file_path
- hash_value (Unique)
- verification_code (Unique)
- issue_date
- institution_id (Foreign Key)
```

---

## 🎯 Key Benefits

### 1. Professional Separation
✅ Document type separate from recipient name  
✅ Clean data structure  
✅ Easy to query

### 2. Complete Location Data
✅ All location levels stored (province → village)  
✅ Only relevant info displayed (street, district, province)  
✅ Full data available for reports if needed

### 3. Clean Verification Output
✅ Simple, easy to read  
✅ Professional appearance  
✅ All essential information included

---

## 🧪 Complete Test Flow

```bash
# 1. Create Province
POST /api/provinces
{
    "code": "KGL",
    "name": "Kigali"
}

# 2. Create Institution
POST /api/institutions?provinceCode=KGL
{
    "name": "AUCA",
    "email": "admin@auca.ac.rw"
}

# 3. Create Institution Profile (Full Location)
POST /api/institutions/1/profile
{
    "street": "KG 544 St",
    "district": "Gasabo",
    "sector": "Remera",
    "cell": "Rukiri",
    "village": "Amahoro",
    "provinceName": "Kigali",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}

# 4. Create User
POST /api/users
{
    "email": "registrar@auca.ac.rw",
    "password": "secure123",
    "role": "ADMIN",
    "fullName": "John Doe",
    "institution": {"id": 1}
}

# 5. Issue Document
POST /api/documents
{
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson",
    "filePath": "/uploads/alice_degree_2024.pdf",
    "hashValue": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
    "institution": {"id": 1}
}

# 6. Verify Document (Note the verification code from step 5)
GET /api/documents/verify/A1B2C3D4?ipAddress=192.168.1.1

# Expected Output:
# SUCCESS - Document is authentic
# 
# Document Type: Bachelor of Computer Science
# Recipient Name: Alice Johnson
# Issue Date: 2024-02-20
# 
# Institution: AUCA
# Email: admin@auca.ac.rw
# Address: KG 544 St, Gasabo, Kigali
```

---

## ✅ Ready to Test!

1. ✅ Stop application
2. ✅ Run migration: `psql -U postgres -d verifydocs_db -f complete_migration.sql`
3. ✅ Restart application: `mvn spring-boot:run`
4. ✅ Test with Postman using examples above

**The output is now clean, simple, and professional!** 🎉
