package com.dcpackage.topic_modelling.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfExtractor {

    public static String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);
            System.out.println("Extracted PDF Text:\n" + extractedText); // Debugging
            return extractedText;
        }
    }

    public static String extractAbstract(String filePath) {
        try {
            String extractedText = extractTextFromPdf(filePath);  // First extract text
            System.out.println("Full Extracted Text:\n" + extractedText);  // Debug print

            Pattern pattern = Pattern.compile(
                    "(?i)\\bAbstract\\b[:\\s\\n]*(.*?)\\n*(?=\\bIntroduction\\b)",
                    Pattern.DOTALL);
            Matcher matcher = pattern.matcher(extractedText);

            if (matcher.find()) {
                String abstractText = matcher.group(1).trim();
                System.out.println("✅ Extracted Abstract:\n" + abstractText);
                return abstractText;
            } else {
                System.out.println("Abstract not found. Possible format issue.");
                return "Abstract not found.";
            }
        } catch (IOException e) {
            return "Error reading PDF: " + e.getMessage();
        }
    }

}

