# 🎯 QUICK REFERENCE - Postman Testing

## 📝 Copy-Paste Ready Examples

### 1️⃣ Create Institution Profile
```
POST http://localhost:8080/api/institutions/1/profile
Content-Type: application/json

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

---

### 2️⃣ Create Document
```
POST http://localhost:8080/api/documents
Content-Type: application/json

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

---

### 3️⃣ Verify Document
```
GET http://localhost:8080/api/documents/verify/[VERIFICATION_CODE]?ipAddress=192.168.1.1
```
Replace `[VERIFICATION_CODE]` with code from step 2

---

## 📋 More Document Examples

### Master's Degree
```json
{
    "documentType": "Master of Business Administration",
    "recipientName": "Bob Williams",
    "filePath": "/uploads/bob_mba_2024.pdf",
    "hashValue": "b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7",
    "institution": {"id": 1}
}
```

### Academic Transcript
```json
{
    "documentType": "Academic Transcript",
    "recipientName": "Carol Davis",
    "filePath": "/uploads/carol_transcript_2024.pdf",
    "hashValue": "c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6a7b8",
    "institution": {"id": 1}
}
```

### Certificate
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

## ✅ Expected Verification Output

```
SUCCESS - Document is authentic

Document Type: Bachelor of Computer Science
Recipient Name: Alice Johnson
Issue Date: 2024-02-20

Institution: AUCA
Email: info@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

---

## 🚀 Quick Commands

```bash
# Restart application
mvn spring-boot:run

# Check database
psql -U postgres -d verifydocs_db -c "\dt"

# View provinces
psql -U postgres -d verifydocs_db -c "SELECT * FROM provinces;"

# View institutions
psql -U postgres -d verifydocs_db -c "SELECT * FROM institutions;"
```

---

**Ready to test! 🎉**
