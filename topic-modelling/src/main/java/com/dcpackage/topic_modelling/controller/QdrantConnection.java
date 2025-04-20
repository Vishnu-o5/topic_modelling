package com.dcpackage.topic_modelling.controller;
import com.dcpackage.topic_modelling.model.DOI;
import com.dcpackage.topic_modelling.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class QdrantConnection {
    private final static Logger logger=LoggerFactory.getLogger(QdrantConnection.class);

    @Autowired
    private RecommendationService recSer;

    @PostMapping("/getDois")
    public ArrayList<String> getRecommendations(@RequestBody Map<String, ArrayList<Double>> embedding, @RequestParam String doi) throws ExecutionException, InterruptedException {
        logger.debug("Recommendation controller called");
        ArrayList<String> recommendations=recSer.getRecSer(embedding.get("embedding"),doi);
        logger.debug("DOIS fetched without any errors");
        if(recommendations.isEmpty()){
            System.out.println("Null recommendation found");
        }

        System.out.println(recommendations);
        return recommendations;
    }
}
