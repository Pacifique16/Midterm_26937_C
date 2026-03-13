# Fraud Detection in VerifyDocs

## Overview
VerifyDocs uses **cryptographic hashing (SHA-256)** and **unique verification codes** to detect fraudulent documents. This document explains how the system identifies fake and tampered documents.

---

## How Fraud Detection Works

### 1. Document Creation Process
When an institution issues a document:

```java
// Step 1: Generate unique verification code
verificationCode = "A1B2C3D4" // 8-character unique code

// Step 2: Create document content string
content = documentType + recipientName + filePath + timestamp

// Step 3: Generate SHA-256 hash
hashValue = SHA256(content)
// Example: "a7f3e9d2c1b8f4e6d9a2c5b8e1f4d7a9..."

// Step 4: Store in database
- verification_code: "A1B2C3D4"
- hash_value: "a7f3e9d2c1b8f4e6d9a2c5b8e1f4d7a9..."
- document_type: "Bachelor Degree"
- recipient_name: "John Doe"
```

---

## Fraud Detection Scenarios

### Scenario 1: Completely Fake Document (Non-existent Verification Code)

**What happens:**
- Someone creates a fake certificate with a random verification code: "FAKE1234"
- They try to verify it

**API Call:**
```
GET http://localhost:8080/api/documents/verify/FAKE1234
```

**System Response:**
```
FRAUD DETECTED!

This verification code does not exist in our system.
The document is FAKE or the verification code is incorrect.
```

**How it's detected:**
- System searches database for verification code "FAKE1234"
- No matching record found
- Returns fraud alert
- Logs failed verification attempt with IP address

**Database Log:**
```sql
INSERT INTO verification_logs (document_id, result, ip_address, verification_time)
VALUES (NULL, 'FAILED - Document not found. Invalid verification code.', '192.168.1.100', NOW());
```

---

### Scenario 2: Tampered Document (Modified Content)

**Original Document:**
- Verification Code: "A1B2C3D4"
- Content: "Bachelor Degree - John Doe - /docs/john.pdf"
- Hash: "a7f3e9d2c1b8f4e6d9a2c5b8e1f4d7a9..."

**Fraudster Action:**
- Changes "Bachelor Degree" to "Master Degree"
- Keeps same verification code "A1B2C3D4"

**New Content:**
- Content: "Master Degree - John Doe - /docs/john.pdf"
- New Hash: "x9y8z7w6v5u4t3s2r1q0p9o8n7m6l5k4..." (COMPLETELY DIFFERENT!)

**API Call:**
```
POST http://localhost:8080/api/documents/verify-with-content?verificationCode=A1B2C3D4
Body: "Master Degree - John Doe - /docs/john.pdf"
```

**System Response:**
```
FRAUD DETECTED!

This document has been TAMPERED or MODIFIED.

Original Hash: a7f3e9d2c1b8f4e6...
Current Hash:  x9y8z7w6v5u4t3s2...

The document content does not match our records.
This document is NOT AUTHENTIC.
```

**How it's detected:**
1. System finds document with verification code "A1B2C3D4"
2. Retrieves stored hash: "a7f3e9d2c1b8f4e6..."
3. Recomputes hash from provided content: "x9y8z7w6v5u4t3s2..."
4. Compares: stored hash ≠ computed hash
5. Returns fraud alert

**Key Insight:**
Even changing a single character changes the entire hash! This is the power of SHA-256.

---

### Scenario 3: Authentic Document (Successful Verification)

**API Call:**
```
GET http://localhost:8080/api/documents/verify/A1B2C3D4
```

**System Response:**
```
✓ VERIFIED - Document is AUTHENTIC

Document Type: Bachelor Degree Certificate
Recipient Name: John Doe
Issue Date: 2024-01-15
Verification Code: A1B2C3D4
Document Hash: a7f3e9d2c1b8f4e6...

Issued By:
Institution: AUCA
Email: info@auca.ac.rw
Address: KG 544 St, Gasabo, Kigali

This document has been verified against our secure database.
```

---

## Two Verification Methods

### Method 1: Basic Verification (Verification Code Only)
**Use Case:** Quick verification to check if document exists

**Endpoint:**
```
GET /api/documents/verify/{verificationCode}
```

**What it checks:**
- ✓ Does verification code exist in database?

**Detects:**
- ✓ Completely fake documents (non-existent codes)

**Does NOT detect:**
- ✗ Tampered documents (modified content)

---

### Method 2: Advanced Verification (Verification Code + Document Content)
**Use Case:** Full verification to detect tampering

**Endpoint:**
```
POST /api/documents/verify-with-content?verificationCode={code}
Body: {document_content}
```

**What it checks:**
- ✓ Does verification code exist in database?
- ✓ Does document content match stored hash?

**Detects:**
- ✓ Completely fake documents (non-existent codes)
- ✓ Tampered documents (modified content)

---

## SHA-256 Hash Properties

### Why SHA-256 is Secure:

1. **Deterministic:** Same input always produces same hash
   ```
   "Hello" → "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
   "Hello" → "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
   ```

2. **Avalanche Effect:** Tiny change = completely different hash
   ```
   "Hello"  → "185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969"
   "hello"  → "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
   ```

3. **One-Way Function:** Cannot reverse hash to get original content
   ```
   Hash → Content (IMPOSSIBLE)
   Content → Hash (EASY)
   ```

4. **Collision Resistant:** Nearly impossible to find two different inputs with same hash
   ```
   Probability of collision: 1 in 2^256 (practically impossible)
   ```

---

## Verification Logs

Every verification attempt is logged:

**Database Table: verification_logs**
```sql
id | document_id | result                                    | ip_address    | verification_time
---|-------------|-------------------------------------------|---------------|-------------------
1  | 5           | SUCCESS - Document is authentic           | 192.168.1.50  | 2024-01-15 10:30:00
2  | NULL        | FAILED - Document not found               | 192.168.1.100 | 2024-01-15 11:45:00
3  | 5           | FAILED - Document has been tampered       | 192.168.1.200 | 2024-01-15 14:20:00
```

**Benefits:**
- Track verification attempts
- Identify suspicious activity (multiple failed attempts from same IP)
- Audit trail for security
- Analytics on document usage

---

## Testing Fraud Detection

### Test 1: Verify Authentic Document
```bash
# Create document first
POST http://localhost:8080/api/documents
{
    "documentType": "Bachelor Degree",
    "recipientName": "John Doe",
    "filePath": "/docs/john.pdf",
    "institution": {"id": 1}
}

# Response includes verification code: "A1B2C3D4"

# Verify it
GET http://localhost:8080/api/documents/verify/A1B2C3D4

# Expected: SUCCESS - Document is authentic
```

### Test 2: Verify Fake Document
```bash
# Try to verify non-existent code
GET http://localhost:8080/api/documents/verify/FAKE1234

# Expected: FRAUD DETECTED! This verification code does not exist
```

### Test 3: Detect Tampered Document
```bash
# Get original document content
Original: "Bachelor Degree - John Doe - /docs/john.pdf"

# Try to verify with modified content
POST http://localhost:8080/api/documents/verify-with-content?verificationCode=A1B2C3D4
Body: "Master Degree - John Doe - /docs/john.pdf"

# Expected: FRAUD DETECTED! Document has been TAMPERED
```

---

## Real-World Application

### Scenario: Employer Verifying Job Applicant's Degree

1. **Applicant submits certificate** with verification code "A1B2C3D4"

2. **Employer visits verification portal** and enters code

3. **System checks:**
   - Does code exist? ✓ Yes
   - Document details match? ✓ Yes
   
4. **Result:** Certificate is authentic, issued by AUCA on 2024-01-15

### Scenario: Fraudster Submits Fake Certificate

1. **Fraudster creates fake certificate** with random code "FAKE1234"

2. **Employer tries to verify**

3. **System checks:**
   - Does code exist? ✗ No
   
4. **Result:** FRAUD DETECTED - Code doesn't exist in system

### Scenario: Fraudster Modifies Real Certificate

1. **Fraudster gets real certificate** (Bachelor Degree, code "A1B2C3D4")

2. **Fraudster changes "Bachelor" to "Master"** using Photoshop

3. **Employer uses advanced verification** with document content

4. **System checks:**
   - Does code exist? ✓ Yes
   - Hash matches? ✗ No (content was modified)
   
5. **Result:** FRAUD DETECTED - Document has been tampered

---

## Security Best Practices

### For Institutions:
1. ✓ Never share verification codes publicly
2. ✓ Store documents securely
3. ✓ Monitor verification logs for suspicious activity
4. ✓ Use HTTPS for all API calls
5. ✓ Implement rate limiting to prevent brute force attacks

### For Verifiers:
1. ✓ Always verify documents through official system
2. ✓ Check institution details match
3. ✓ Use advanced verification for critical documents
4. ✓ Report suspicious documents to institution

---

## Viva-Voce Explanation Points

**Question: How does your system detect fraud?**

**Answer:**
"Our system uses two-layer fraud detection:

1. **Verification Code Check:** Every document gets a unique 8-character code stored in our database. If someone presents a fake document with a random code, our system immediately detects it doesn't exist.

2. **Hash Verification:** We use SHA-256 cryptographic hashing. When a document is created, we generate a unique hash from its content. If someone modifies the document (e.g., changes Bachelor to Master), the hash changes completely. By comparing the stored hash with a recomputed hash, we detect tampering.

Even changing a single character changes the entire hash - this is called the avalanche effect. SHA-256 is one-way, meaning you cannot reverse it, and collision-resistant, meaning two different documents won't have the same hash."

**Question: What happens if someone creates a fake document?**

**Answer:**
"If someone creates a completely fake document with a random verification code, when they try to verify it, our system searches the database and finds no matching record. It returns 'FRAUD DETECTED - This verification code does not exist in our system.' We also log this failed attempt with the IP address for security monitoring."

**Question: Can someone modify a real document?**

**Answer:**
"They can try, but our advanced verification will catch it. If someone takes a real Bachelor degree certificate and changes it to Master degree, the verification code stays the same but the content changes. When we recompute the hash from the modified content, it won't match the stored hash. Our system detects this mismatch and alerts 'FRAUD DETECTED - Document has been tampered.'"

---

## Author
Pacifique Harerimana  
Student ID: 26937  
AUCA - Web Technology Midterm Project
