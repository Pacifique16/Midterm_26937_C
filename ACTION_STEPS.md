# 🚀 ACTION STEPS - Apply Changes

## ✅ Changes Made

### 1. Document Entity
- ✅ Changed from `title` to `documentType` + `recipientName`
- ✅ More professional structure

### 2. InstitutionProfile Entity
- ✅ Added detailed location fields: street, district, sector, cell, village, province
- ✅ All fields stored, only street + district + province displayed

### 3. Verification Response
- ✅ Simple, clean format
- ✅ Shows: Document Type, Recipient Name, Issue Date, Institution, Email, Address

---

## 📋 Steps to Apply Changes

### Step 1: Stop Application ⏹️
If your application is running, stop it:
```bash
Ctrl + C
```

### Step 2: Run Database Migration 🗄️
```bash
psql -U postgres -d verifydocs_db -f complete_migration.sql
```

This will:
- Drop old tables (documents, institution_profiles, verification_logs, user_document_access)
- Recreate with new structure
- Preserve provinces, institutions, users tables

### Step 3: Restart Application 🚀
```bash
mvn spring-boot:run
```

Wait for: `Started VerifyDocsApplication in X.XXX seconds`

### Step 4: Test in Postman 🧪

#### Test 1: Create Institution Profile
```
POST http://localhost:8080/api/institutions/1/profile

Body:
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

#### Test 2: Create Document
```
POST http://localhost:8080/api/documents

Body:
{
    "documentType": "Bachelor of Computer Science",
    "recipientName": "Alice Johnson",
    "filePath": "/uploads/alice_degree_2024.pdf",
    "hashValue": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
    "institution": {"id": 1}
}
```

Note the `verificationCode` in the response!

#### Test 3: Verify Document
```
GET http://localhost:8080/api/documents/verify/[VERIFICATION_CODE]?ipAddress=192.168.1.1
```

Replace `[VERIFICATION_CODE]` with the code from Test 2.

**Expected Output**:
```
SUCCESS - Document is authentic

Document Type: Bachelor of Computer Science
Recipient Name: Alice Johnson
Issue Date: 2024-02-20

Institution: AUCA
Email: admin@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali
```

---

## ✅ Success Indicators

You'll know it's working when:
- ✅ Institution profile accepts all location fields
- ✅ Document creation accepts documentType and recipientName
- ✅ Verification shows clean, simple output
- ✅ Address shows: Street, District, Province (not sector, cell, village)

---

## 🐛 Troubleshooting

### Issue: "Column 'title' does not exist"
**Solution**: Run the migration script again
```bash
psql -U postgres -d verifydocs_db -f complete_migration.sql
```

### Issue: "Column 'address' does not exist"
**Solution**: Migration script will fix this - run it

### Issue: Application won't start
**Solution**: 
1. Check console for errors
2. Make sure database migration completed
3. Try: `mvn clean install` then `mvn spring-boot:run`

---

## 📁 Files Created/Updated

### Updated Files:
1. ✅ `Document.java` - New fields
2. ✅ `InstitutionProfile.java` - Location fields
3. ✅ `DocumentService.java` - New verification format
4. ✅ `database_schema.sql` - Updated schema
5. ✅ `SecurityConfig.java` - Disable auth for testing

### New Files:
1. ✅ `complete_migration.sql` - Migration script
2. ✅ `FINAL_TESTING_GUIDE.md` - Testing guide
3. ✅ `ACTION_STEPS.md` - This file

---

## 🎯 Quick Commands

```bash
# Stop application
Ctrl + C

# Run migration
psql -U postgres -d verifydocs_db -f complete_migration.sql

# Restart application
mvn spring-boot:run

# Test in Postman
# Use examples from FINAL_TESTING_GUIDE.md
```

---

## ⏱️ Estimated Time

- Migration: 1 minute
- Restart: 2 minutes
- Testing: 5 minutes
- **Total: ~8 minutes**

---

## 📞 Need Help?

Check these files:
- `FINAL_TESTING_GUIDE.md` - Complete testing examples
- `FIX_401_ERROR.md` - If you get 401 errors
- `POSTMAN_TESTING_GUIDE.md` - General Postman help

---

**Ready? Let's do this! 🚀**

1. Stop app
2. Run migration
3. Restart app
4. Test!
