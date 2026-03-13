# 🔧 Fix for 401 Unauthorized Error

## Problem
You're getting a **401 Unauthorized** error when testing APIs in Postman.

## Cause
Spring Security is enabled by default and requires authentication.

## ✅ Solution Applied
I've created a `SecurityConfig.java` file that disables authentication for testing purposes.

---

## 🚀 Steps to Fix

### Step 1: Stop the Running Application
If your Spring Boot application is running, stop it:
- Press `Ctrl + C` in the terminal/console where it's running
- Or close the terminal window

### Step 2: Restart the Application
```bash
mvn spring-boot:run
```

### Step 3: Wait for Application to Start
Look for this message in the console:
```
Started VerifyDocsApplication in X.XXX seconds
```

### Step 4: Test Again in Postman
Now try your POST request again:
```
POST http://localhost:8080/api/provinces
Body:
{
    "code": "SOU",
    "name": "Southern"
}
```

**Expected Result**: 200 OK with province data

---

## 🧪 Quick Test Sequence

### Test 1: Check if Application is Running
```
GET http://localhost:8080/api/provinces
```
Should return the 3 existing provinces (no 401 error)

### Test 2: Create New Province
```
POST http://localhost:8080/api/provinces
Body:
{
    "code": "SOU",
    "name": "Southern"
}
```
Should return 200 OK with created province

### Test 3: Check Province Exists
```
GET http://localhost:8080/api/provinces/exists/SOU
```
Should return `true`

---

## 📝 What Changed

### Before:
- Spring Security was blocking all requests
- Required authentication credentials
- Returned 401 Unauthorized

### After:
- Security configuration added: `SecurityConfig.java`
- All requests are now permitted without authentication
- CSRF protection disabled for API testing
- All endpoints accessible

---

## 🔍 Verify Security Config is Loaded

When you restart the application, look for this in the console:
```
o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [...]
```

This confirms Spring Security is configured.

---

## 💡 Alternative: Test Without Restarting

If you can't restart right now, you can test with basic auth:

1. In Postman, go to **Authorization** tab
2. Select **Type**: Basic Auth
3. Username: `user`
4. Password: Check your console for auto-generated password like:
   ```
   Using generated security password: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
   ```

But **restarting is the better solution** since we've disabled security for testing.

---

## ✅ Success Indicators

After restarting, you should see:
- ✅ No 401 errors in Postman
- ✅ GET requests work without authentication
- ✅ POST requests work without authentication
- ✅ All APIs accessible

---

## 🎯 Next Steps After Fix

1. **Restart Application** (most important!)
2. Test GET /api/provinces (should work)
3. Test POST /api/provinces (should work)
4. Continue with full testing sequence in POSTMAN_TESTING_GUIDE.md

---

## 🐛 Still Getting 401?

### Check 1: Application Restarted?
Make sure you stopped and restarted the application after adding SecurityConfig.java

### Check 2: Correct Port?
Verify application is running on port 8080:
```
GET http://localhost:8080/api/provinces
```

### Check 3: SecurityConfig Compiled?
Run:
```bash
mvn clean install
mvn spring-boot:run
```

### Check 4: Check Console Logs
Look for any errors in the Spring Boot console when starting.

---

## 📞 Quick Commands

### Stop Application:
`Ctrl + C` in the terminal

### Restart Application:
```bash
mvn spring-boot:run
```

### Clean and Rebuild:
```bash
mvn clean install
mvn spring-boot:run
```

---

**After restarting, your APIs should work perfectly! 🚀**
