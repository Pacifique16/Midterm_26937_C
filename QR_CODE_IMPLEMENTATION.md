# QR Code Implementation in VerifyDocs

## Overview
VerifyDocs now supports QR code generation and scanning for easy document verification. Users can scan QR codes with their phone camera to instantly verify documents.

---

## Features

### 1. QR Code Generation (Backend)
- Generate QR codes for any document
- Two types: Simple (URL only) and Detailed (with document info)
- Customizable size (width x height)
- PNG format output

### 2. QR Code Scanning (Frontend)
- Web-based QR scanner using device camera
- Works on mobile and desktop
- Real-time scanning
- Manual code entry option

---

## Backend Implementation

### Dependencies Added to pom.xml:
```xml
<!-- QR Code Generation -->
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.5.2</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.2</version>
</dependency>
```

### QRCodeService.java
Handles QR code generation with two methods:

**Method 1: Simple QR Code (URL only)**
```java
public byte[] generateQRCode(String verificationCode, int width, int height)
```
- Generates QR code containing verification URL
- Example: `http://localhost:8080/api/documents/verify/A1B2C3D4`

**Method 2: Detailed QR Code (JSON data)**
```java
public byte[] generateQRCodeWithData(String verificationCode, String documentType, String recipientName, int width, int height)
```
- Generates QR code containing JSON with document details
- Example:
```json
{
  "verificationCode": "A1B2C3D4",
  "documentType": "Bachelor Degree",
  "recipientName": "John Doe",
  "verifyUrl": "http://localhost:8080/api/documents/verify/A1B2C3D4"
}
```

---

## API Endpoints

### 1. Generate Simple QR Code
```
GET /api/documents/{id}/qrcode?width=300&height=300
```

**Parameters:**
- `id` (path): Document ID
- `width` (query, optional): QR code width in pixels (default: 300)
- `height` (query, optional): QR code height in pixels (default: 300)

**Response:** PNG image

**Example:**
```
GET http://localhost:8080/api/documents/1/qrcode
GET http://localhost:8080/api/documents/1/qrcode?width=500&height=500
```

---

### 2. Generate Detailed QR Code
```
GET /api/documents/{id}/qrcode-detailed?width=400&height=400
```

**Parameters:**
- `id` (path): Document ID
- `width` (query, optional): QR code width in pixels (default: 400)
- `height` (query, optional): QR code height in pixels (default: 400)

**Response:** PNG image with embedded document details

**Example:**
```
GET http://localhost:8080/api/documents/1/qrcode-detailed
GET http://localhost:8080/api/documents/1/qrcode-detailed?width=600&height=600
```

---

## Frontend Implementation

### Access the Web Interface
```
http://localhost:8080/index.html
```

### Features:

#### 1. QR Code Scanner Tab
- **Auto-start camera** on page load
- **Real-time scanning** using device camera
- **Start/Stop controls** for camera
- **Automatic verification** when QR code detected
- **Mobile-friendly** responsive design

#### 2. Manual Entry Tab
- **Text input** for verification code
- **Validation** (8 characters, alphanumeric)
- **Manual verification** button

#### 3. Result Display
- **Success message** for authentic documents
- **Fraud alert** for fake/tampered documents
- **Detailed information** display
- **Color-coded results** (green = success, red = fraud)

---

## How It Works

### Document Creation with QR Code:

**Step 1: Create Document**
```
POST http://localhost:8080/api/documents
{
    "documentType": "Bachelor Degree",
    "recipientName": "John Doe",
    "filePath": "/docs/john.pdf",
    "institution": {"id": 1}
}

Response:
{
    "id": 5,
    "verificationCode": "A1B2C3D4",
    ...
}
```

**Step 2: Generate QR Code**
```
GET http://localhost:8080/api/documents/5/qrcode
```
- Downloads PNG image with QR code
- QR code contains: `http://localhost:8080/api/documents/verify/A1B2C3D4`

**Step 3: Print QR Code on Certificate**
- Institution prints the QR code on the physical certificate
- QR code can be placed in corner or bottom of document

---

### Document Verification with QR Code:

**Step 1: User Opens Verification Portal**
```
http://localhost:8080/index.html
```

**Step 2: Scan QR Code**
- Camera automatically starts
- Point camera at QR code on certificate
- System extracts verification code from QR

**Step 3: Automatic Verification**
- System calls: `GET /api/documents/verify/A1B2C3D4`
- Displays result instantly

**Step 4: View Results**
- ✓ **Authentic:** Shows document details, institution info, issue date
- ❌ **Fraud:** Shows fraud alert with explanation

---

## Testing QR Code Feature

### Test 1: Generate and Download QR Code

1. **Create a document:**
```
POST http://localhost:8080/api/documents
{
    "documentType": "Bachelor Degree Certificate",
    "recipientName": "John Doe",
    "filePath": "/documents/john_degree.pdf",
    "institution": {"id": 1}
}
```

2. **Note the document ID** from response (e.g., `id: 5`)

3. **Generate QR code:**
```
GET http://localhost:8080/api/documents/5/qrcode
```

4. **Open in browser** - QR code image will download

5. **Save the image** to your computer

---

### Test 2: Scan QR Code with Web Interface

1. **Open verification portal:**
```
http://localhost:8080/index.html
```

2. **Allow camera access** when prompted

3. **Show QR code to camera:**
   - Display QR code on another device screen
   - Or print QR code and hold to camera

4. **Automatic verification:**
   - System detects QR code
   - Extracts verification code
   - Verifies document
   - Shows result

---

### Test 3: Scan QR Code with Phone

1. **Open phone camera** or QR scanner app

2. **Scan the QR code**

3. **Click the link** that appears

4. **View verification result** in browser

---

## QR Code Data Formats

### Format 1: Simple URL
```
http://localhost:8080/api/documents/verify/A1B2C3D4
```
- Smallest QR code size
- Direct verification link
- Best for printing on documents

### Format 2: JSON Data
```json
{
  "verificationCode": "A1B2C3D4",
  "documentType": "Bachelor Degree Certificate",
  "recipientName": "John Doe",
  "verifyUrl": "http://localhost:8080/api/documents/verify/A1B2C3D4"
}
```
- Larger QR code size
- Contains document preview
- Useful for offline verification apps

---

## Real-World Usage Scenarios

### Scenario 1: University Issues Degree Certificate

1. **Student graduates** from AUCA
2. **University creates document** in VerifyDocs system
3. **System generates** verification code: `A1B2C3D4`
4. **University generates QR code** via API
5. **QR code printed** on physical certificate
6. **Student receives** certificate with QR code

### Scenario 2: Employer Verifies Certificate

**Option A: Using Phone Camera**
1. Employer opens phone camera
2. Scans QR code on certificate
3. Clicks verification link
4. Views document details instantly

**Option B: Using Web Portal**
1. Employer opens `http://localhost:8080/index.html`
2. Scans QR code with webcam
3. System automatically verifies
4. Views result on screen

### Scenario 3: Border Control Verifies Travel Document

1. Officer opens verification portal on tablet
2. Scans QR code on travel document
3. System verifies in real-time
4. Officer sees:
   - ✓ Document authentic
   - Issued by: Ministry of Foreign Affairs
   - Issue Date: 2024-01-15
   - Valid until: 2029-01-15

---

## Security Considerations

### QR Code Security:

1. **URL-based verification**
   - QR code only contains verification URL
   - No sensitive data in QR code
   - Verification happens server-side

2. **Cannot be forged**
   - QR code links to unique verification code
   - Verification code stored in secure database
   - Fake QR codes will fail verification

3. **Tamper-proof**
   - Changing QR code breaks verification
   - System detects non-existent codes
   - Logs all verification attempts

### Best Practices:

1. ✓ Use HTTPS in production (not HTTP)
2. ✓ Print QR codes with high quality
3. ✓ Test QR codes before printing certificates
4. ✓ Monitor verification logs for suspicious activity
5. ✓ Use appropriate QR code size (minimum 300x300px)

---

## Customization Options

### Change QR Code Size:
```
GET /api/documents/1/qrcode?width=500&height=500
```

### Change Verification URL (for production):
Edit `QRCodeService.java`:
```java
String verificationUrl = "https://verifydocs.auca.ac.rw/api/documents/verify/" + verificationCode;
```

### Customize Frontend Design:
Edit `src/main/resources/static/index.html`:
- Change colors in CSS
- Modify layout
- Add institution logo
- Customize messages

---

## Troubleshooting

### Issue 1: Camera Not Working
**Solution:**
- Allow camera permissions in browser
- Use HTTPS (required for camera access on some browsers)
- Try different browser (Chrome recommended)

### Issue 2: QR Code Not Scanning
**Solution:**
- Ensure good lighting
- Hold camera steady
- Increase QR code size (use 400x400 or larger)
- Clean camera lens

### Issue 3: QR Code Image Not Generating
**Solution:**
- Check document ID exists
- Verify ZXing dependencies installed
- Run `mvn clean install`
- Restart application

---

## Production Deployment

### Update URLs for Production:

1. **In QRCodeService.java:**
```java
String verificationUrl = "https://yourdomain.com/api/documents/verify/" + verificationCode;
```

2. **In index.html:**
```javascript
fetch(`https://yourdomain.com/api/documents/verify/${verificationCode}`)
```

3. **Enable HTTPS:**
- Configure SSL certificate
- Update application.properties
- Test QR scanning with HTTPS

---

## Viva-Voce Explanation

**Question: How does QR code verification work?**

**Answer:**
"When we create a document, the system generates a unique verification code like 'A1B2C3D4'. We then generate a QR code containing the verification URL. This QR code is printed on the physical certificate.

When someone wants to verify the document, they scan the QR code with their phone or our web portal. The QR code contains the verification URL which automatically calls our API. The system checks if the verification code exists in the database and returns the document details if authentic, or a fraud alert if fake.

The QR code makes verification instant and user-friendly - no need to manually type the verification code. It's especially useful for employers, border control, or anyone who needs to quickly verify many documents."

---

## Testing Checklist

- [ ] Install dependencies (`mvn clean install`)
- [ ] Restart application
- [ ] Create a test document
- [ ] Generate QR code via API
- [ ] Download and save QR code image
- [ ] Open web portal (`http://localhost:8080/index.html`)
- [ ] Allow camera access
- [ ] Scan QR code with webcam
- [ ] Verify result shows correctly
- [ ] Test with phone camera
- [ ] Test manual entry
- [ ] Test with fake verification code
- [ ] Check verification logs in database

---

## Author
Pacifique Harerimana  
Student ID: 26937  
AUCA - Web Technology Midterm Project
