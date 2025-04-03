package com.dcpackage.topic_modelling.controller;

import com.dcpackage.topic_modelling.service.PdfService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/extract-abstract")
    public String extractAbstract(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file temporarily
            File tempFile = File.createTempFile("uploaded_", ".pdf");
            file.transferTo(tempFile);

            // Extract the abstract
            String abstractText = pdfService.getAbstractFromPdf(tempFile.getAbsolutePath());

            // Delete temp file after processing
            tempFile.delete();

            return abstractText;
        } catch (IOException e) {
            return "Error processing file: " + e.getMessage();
        }
    }
}
