# QR Code Quick Start Guide

## 🚀 Setup (5 minutes)

### Step 1: Install Dependencies
```bash
cd "c:\Users\Pacifique Harerimana\OneDrive\Desktop\AUCA\Semester 8\Web Tech\VerifyDocs"
mvn clean install
```

### Step 2: Start Application
```bash
mvn spring-boot:run
```

---

## 📱 Test QR Code Feature

### Test 1: Generate QR Code

**1. Create a document:**
```
POST http://localhost:8080/api/documents
Body:
{
    "documentType": "Bachelor Degree Certificate",
    "recipientName": "John Doe",
    "filePath": "/documents/john_degree.pdf",
    "institution": {"id": 1}
}
```

**2. Get QR code (replace {id} with document ID from response):**
```
GET http://localhost:8080/api/documents/{id}/qrcode
```

**3. Open in browser** - QR code downloads as PNG image

---

### Test 2: Scan QR Code

**Option A: Using Web Portal**
1. Open: `http://localhost:8080/index.html`
2. Allow camera access
3. Show QR code to camera
4. See instant verification result

**Option B: Using Phone**
1. Open phone camera
2. Scan QR code
3. Click the link
4. View verification result

---

## 🎯 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/documents/{id}/qrcode` | GET | Generate simple QR code |
| `/api/documents/{id}/qrcode-detailed` | GET | Generate detailed QR code |
| `/api/documents/verify/{code}` | GET | Verify document |
| `/index.html` | GET | Web verification portal |

---

## 📊 How It Works

```
1. Institution creates document
   ↓
2. System generates verification code (A1B2C3D4)
   ↓
3. System generates QR code with URL
   ↓
4. QR code printed on certificate
   ↓
5. User scans QR code
   ↓
6. System verifies document
   ↓
7. Result: ✓ Authentic or ❌ Fraud
```

---

## 🔐 Fraud Detection with QR Code

### Scenario 1: Fake Document
- Fraudster creates fake QR code
- User scans it
- System: "FRAUD DETECTED! Code doesn't exist"

### Scenario 2: Authentic Document
- User scans real QR code
- System finds verification code
- System: "✓ VERIFIED - Document is authentic"

---

## 📸 Screenshots to Take

1. **QR Code Generation:**
   - Postman request to generate QR code
   - Downloaded QR code image

2. **Web Portal:**
   - Homepage with camera scanner
   - Scanning QR code
   - Success result (authentic document)
   - Fraud alert (fake document)

3. **Mobile Scanning:**
   - Phone camera scanning QR code
   - Verification result on phone

---

## 🎓 Viva-Voce Points

**Q: Why use QR codes?**
**A:** "QR codes make verification instant and user-friendly. Instead of manually typing an 8-character code, users just scan with their phone camera. It's faster, reduces errors, and works on any device with a camera."

**Q: How secure are QR codes?**
**A:** "The QR code only contains the verification URL, not sensitive data. The actual verification happens server-side where we check if the code exists in our secure database. Fake QR codes will fail verification because the code won't exist in our system."

**Q: Can QR codes be forged?**
**A:** "No. Each QR code links to a unique verification code stored in our database. If someone creates a fake QR code with a random code, our system will detect it doesn't exist and show 'FRAUD DETECTED'. The verification code is the security key, not the QR code itself."

---

## ✅ Testing Checklist

- [ ] Dependencies installed
- [ ] Application running
- [ ] Document created
- [ ] QR code generated and downloaded
- [ ] Web portal accessible
- [ ] Camera permission granted
- [ ] QR code scanned successfully
- [ ] Authentic document verified
- [ ] Fake code detected as fraud
- [ ] Screenshots taken

---

## 🎉 You're Ready!

Your VerifyDocs system now has:
- ✓ QR code generation
- ✓ Web-based QR scanner
- ✓ Mobile-friendly verification
- ✓ Fraud detection
- ✓ Real-time verification

**Demo Flow:**
1. Show document creation
2. Generate QR code
3. Scan with web portal
4. Show authentic result
5. Try fake code
6. Show fraud detection

Good luck with your presentation! 🚀
