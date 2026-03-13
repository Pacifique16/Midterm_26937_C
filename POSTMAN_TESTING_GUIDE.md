# 🧪 Postman Testing Guide

## 📥 Import the Collection

1. Open Postman
2. Click **Import** button (top left)
3. Select **VerifyDocs_Complete_Postman_Collection.json**
4. Click **Import**

The collection will appear in your Collections sidebar with all APIs organized.

---

## ⚙️ Setup

### Check Base URL
The collection uses a variable `{{baseUrl}}` set to `http://localhost:8080`

To verify/change:
1. Click on the collection name
2. Go to **Variables** tab
3. Ensure `baseUrl` = `http://localhost:8080`

---

## 🚀 Testing Sequence (Follow This Order)

### ✅ Step 1: Test Province APIs (Location Saving - Rubric #2)

#### 1.1 Get All Provinces (Should show 3 existing)
```
GET {{baseUrl}}/api/provinces
```
**Expected Result**: 3 provinces (KGL, EST, WST)

#### 1.2 Check Province Exists (existBy - Rubric #7)
```
GET {{baseUrl}}/api/provinces/exists/KGL
```
**Expected Result**: `true`

#### 1.3 Create New Province
```
POST {{baseUrl}}/api/provinces
Body:
{
    "code": "SOU",
    "name": "Southern"
}
```
**Expected Result**: Province created with ID 4

#### 1.4 Check New Province Exists
```
GET {{baseUrl}}/api/provinces/exists/SOU
```
**Expected Result**: `true`

---

### ✅ Step 2: Test Institution APIs

#### 2.1 Get All Institutions
```
GET {{baseUrl}}/api/institutions
```
**Expected Result**: 2 institutions (AUCA, UR)

#### 2.2 Create Institution Profile (One-to-One - Rubric #6)
```
POST {{baseUrl}}/api/institutions/1/profile
Body:
{
    "address": "KG 544 St, Kigali",
    "phone": "+250788123456",
    "website": "www.auca.ac.rw",
    "description": "Adventist University of Central Africa"
}
```
**Expected Result**: Profile created for AUCA

#### 2.3 Create New Institution
```
POST {{baseUrl}}/api/institutions?provinceCode=KGL
Body:
{
    "name": "KIST",
    "email": "info@kist.ac.rw"
}
```
**Expected Result**: New institution created

---

### ✅ Step 3: Test User APIs

#### 3.1 Get All Users (Should show 2 existing)
```
GET {{baseUrl}}/api/users/paginated?page=0&size=10
```
**Expected Result**: 2 users (John Doe, Jane Smith)

#### 3.2 Create New User
```
POST {{baseUrl}}/api/users
Body:
{
    "email": "alice@auca.ac.rw",
    "password": "password123",
    "role": "USER",
    "fullName": "Alice Johnson",
    "institution": {
        "id": 1
    }
}
```
**Expected Result**: User created

#### 3.3 Create Another User
```
POST {{baseUrl}}/api/users
Body:
{
    "email": "bob@auca.ac.rw",
    "password": "password123",
    "role": "USER",
    "fullName": "Bob Williams",
    "institution": {
        "id": 1
    }
}
```
**Expected Result**: User created

---

### ✅ Step 4: Test Pagination (Rubric #3)

#### 4.1 Get First Page (2 users)
```
GET {{baseUrl}}/api/users/paginated?page=0&size=2
```
**Expected Result**: 
- 2 users in content
- totalPages: 2
- totalElements: 4

#### 4.2 Get Second Page (2 users)
```
GET {{baseUrl}}/api/users/paginated?page=1&size=2
```
**Expected Result**: Next 2 users

---

### ✅ Step 5: Test Sorting (Rubric #3)

#### 5.1 Sort by Email (Ascending)
```
GET {{baseUrl}}/api/users/sorted?sortBy=email
```
**Expected Result**: Users sorted alphabetically by email

#### 5.2 Sort by Full Name
```
GET {{baseUrl}}/api/users/sorted?sortBy=fullName
```
**Expected Result**: Users sorted by name

---

### ✅ Step 6: Test Combined Pagination + Sorting (Rubric #3)

```
GET {{baseUrl}}/api/users/paginated-sorted?page=0&size=2&sortBy=fullName
```
**Expected Result**: First 2 users sorted by name

---

### ✅ Step 7: Test Province-Based User Retrieval (Rubric #8)

#### 7.1 Get Users by Province Code
```
GET {{baseUrl}}/api/users/province/code/KGL
```
**Expected Result**: All 4 users (all are in Kigali institutions)

#### 7.2 Get Users by Province Name
```
GET {{baseUrl}}/api/users/province/name/Kigali
```
**Expected Result**: Same 4 users

#### 7.3 Test with Different Province
```
GET {{baseUrl}}/api/users/province/code/EST
```
**Expected Result**: Empty list (no users in Eastern province)

---

### ✅ Step 8: Test Document APIs

#### 8.1 Create Document
```
POST {{baseUrl}}/api/documents
Body:
{
    "title": "Bachelor Degree Certificate",
    "filePath": "/uploads/cert_001.pdf",
    "hashValue": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6",
    "institution": {
        "id": 1
    }
}
```
**Expected Result**: Document created with auto-generated verification code

#### 8.2 Get Documents with Pagination
```
GET {{baseUrl}}/api/documents/paginated?page=0&size=10
```
**Expected Result**: Paginated document list

#### 8.3 Verify Document (use verification code from step 8.1)
```
GET {{baseUrl}}/api/documents/verify/ABC12345?ipAddress=192.168.1.1
```
**Expected Result**: "SUCCESS - Document is authentic"

---

## 📊 Rubric Requirements Testing Checklist

### ✅ Rubric #2: Location Saving
- [ ] POST /api/provinces - Creates province
- [ ] Data stored in database
- [ ] Relationships handled (institution references province)

### ✅ Rubric #3: Sorting & Pagination
- [ ] GET /api/users/paginated - Pagination works
- [ ] GET /api/users/sorted - Sorting works
- [ ] GET /api/users/paginated-sorted - Combined works
- [ ] Same for documents

### ✅ Rubric #6: One-to-One Relationship
- [ ] POST /api/institutions/1/profile - Creates profile
- [ ] Only one profile per institution
- [ ] Unique constraint enforced

### ✅ Rubric #7: existBy() Method
- [ ] GET /api/provinces/exists/KGL - Returns true
- [ ] GET /api/provinces/exists/XXX - Returns false
- [ ] Checks existence without loading entity

### ✅ Rubric #8: Province-Based User Retrieval
- [ ] GET /api/users/province/code/KGL - Returns users
- [ ] GET /api/users/province/name/Kigali - Returns users
- [ ] JPQL query navigates relationships correctly

---

## 🐛 Troubleshooting

### Issue: "Connection refused"
**Solution**: Make sure Spring Boot application is running
```bash
mvn spring-boot:run
```

### Issue: "404 Not Found"
**Solution**: Check the URL is correct and application is running on port 8080

### Issue: "500 Internal Server Error"
**Solution**: 
- Check application logs in console
- Verify database is running
- Check request body format

### Issue: "Province already exists"
**Solution**: This is expected! The existBy() method is working. Use a different province code.

### Issue: Empty response for province query
**Solution**: Make sure users exist in institutions in that province

---

## 💡 Tips

1. **Test in Order**: Follow the sequence above to build up test data
2. **Check Responses**: Look at the response body to see data structure
3. **Save Responses**: Note IDs from responses to use in subsequent requests
4. **Use Variables**: You can create Postman variables for commonly used IDs
5. **Check Console**: Watch the Spring Boot console for SQL queries

---

## 📝 Quick Reference

### Base URL
```
http://localhost:8080
```

### Key Endpoints for Rubric

| Requirement | Endpoint | Method |
|------------|----------|--------|
| Location Saving | /api/provinces | POST |
| existBy() | /api/provinces/exists/{code} | GET |
| Pagination | /api/users/paginated?page=0&size=10 | GET |
| Sorting | /api/users/sorted?sortBy=email | GET |
| Province Query | /api/users/province/code/{code} | GET |
| One-to-One | /api/institutions/{id}/profile | POST |

---

## ✅ Success Indicators

You'll know everything is working when:
- ✅ All GET requests return 200 OK
- ✅ POST requests return 200 OK with created data
- ✅ Pagination returns correct page info
- ✅ Sorting orders results correctly
- ✅ Province query returns users from that province
- ✅ existBy returns true/false correctly
- ✅ One-to-One profile creation works

---

## 🎯 Demo Sequence for Viva-Voce

1. Show GET /api/provinces (existing data)
2. Show POST /api/provinces (location saving)
3. Show GET /api/provinces/exists/KGL (existBy)
4. Show POST /api/institutions/1/profile (One-to-One)
5. Show GET /api/users/paginated (pagination)
6. Show GET /api/users/sorted (sorting)
7. Show GET /api/users/province/code/KGL (province query)

**Total Demo Time**: ~5 minutes

---

**Happy Testing! 🚀**
