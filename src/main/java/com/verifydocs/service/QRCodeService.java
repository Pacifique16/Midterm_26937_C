package com.verifydocs.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QRCodeService {
    
    public byte[] generateQRCode(String verificationCode, int width, int height) {
        try {
            // Create verification URL
            String verificationUrl = "http://localhost:8080/api/documents/verify/" + verificationCode;
            
            // Generate QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(verificationUrl, BarcodeFormat.QR_CODE, width, height);
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }
    
    public byte[] generateQRCodeWithData(String verificationCode, String documentType, String recipientName, int width, int height) {
        try {
            // Create JSON data for QR code
            String qrData = String.format(
                "{\"verificationCode\":\"%s\",\"documentType\":\"%s\",\"recipientName\":\"%s\",\"verifyUrl\":\"http://localhost:8080/api/documents/verify/%s\"}",
                verificationCode, documentType, recipientName, verificationCode
            );
            
            // Generate QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, width, height);
            
            // Convert to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR code", e);
        }
    }
}
