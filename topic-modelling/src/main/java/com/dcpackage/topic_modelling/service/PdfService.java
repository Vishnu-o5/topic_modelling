package com.dcpackage.topic_modelling.service;

import com.dcpackage.topic_modelling.utils.PdfExtractor;
import org.springframework.stereotype.Service;

@Service
public class PdfService {
    public String getAbstractFromPdf(String filePath) {
        return PdfExtractor.extractAbstract(filePath);
    }
}
