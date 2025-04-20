package com.dcpackage.topic_modelling.controller;

import com.dcpackage.topic_modelling.model.Embedding;

import com.dcpackage.topic_modelling.service.EmbeddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmbeddingGenerator {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingGenerator.class);


    @Autowired
    private EmbeddingService embeddingService;


    @PostMapping("/embedding")
    public List<Double> getEmbedding(@RequestBody Map<String,String> abstractText){
        logger.info("The controller is being called");
        Embedding em= embeddingService.get_embedding(abstractText.get("abstract"));
        System.out.println("Hello this is controller");
        System.out.println(em.getEmbedding().size());
//        for(Double d:em.get_embedding()){
//            System.out.println(d);
//        }
        return em.getEmbedding().getFirst();
    }
}
