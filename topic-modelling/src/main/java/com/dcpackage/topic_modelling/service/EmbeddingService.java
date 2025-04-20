package com.dcpackage.topic_modelling.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dcpackage.topic_modelling.model.Embedding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmbeddingService {
    @Autowired
    private final RestTemplate template=new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingService.class);

    public Embedding get_embedding(String abstractText){
        Map<String,String> requestBody=new HashMap<>();

        System.out.println(abstractText);

        requestBody.put("abstract",abstractText);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,String>> requestEntity=new HttpEntity<>(requestBody,headers);


        logger.info("Calling HuggingFace embedding API with abstract: {}", abstractText);
        Embedding response=template.postForObject("https://22pc05-research-paper-embedding.hf.space:443/embed/",requestEntity,Embedding.class);
        if(response==null){
            System.err.println("Response is null");
        }

        return response;
    }
}
