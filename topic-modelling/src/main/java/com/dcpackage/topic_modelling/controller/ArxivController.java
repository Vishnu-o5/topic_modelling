package com.dcpackage.topic_modelling.controller;

import com.dcpackage.topic_modelling.service.ArxivService;
import com.dcpackage.topic_modelling.service.ArxivService.ArxivPaperInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arxiv")
public class ArxivController {

    @Autowired
    private ArxivService arxivService;

    @GetMapping("/info")
    public ArxivPaperInfo getPaperLinks(@RequestParam String doi) {
        try {
            return arxivService.getPaperInfo(doi);
        } catch (Exception e) {
            throw new RuntimeException("Paper not found or error occurred: " + e.getMessage());
        }
    }
}
