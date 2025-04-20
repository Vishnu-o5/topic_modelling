package com.dcpackage.topic_modelling.service;

import com.dcpackage.topic_modelling.model.DOI;

import com.google.common.util.concurrent.ListenableFuture;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.UpdateResult;
import io.qdrant.client.grpc.Points.PointId;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.stream.Collectors;

import static io.qdrant.client.VectorFactory.vector;
import static io.qdrant.client.VectorsFactory.namedVectors;



@Service
public class RecommendationService {
    private final static Logger logger= LoggerFactory.getLogger(RecommendationService.class);
    String apiKey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3MiOiJtIn0.-931yGqaMeFLFdrijVaSPWqFBsTCvP1hTEvVV5MBRXc";

    @Autowired
    private final RestTemplate template=new RestTemplate();

    public ArrayList<String> getRecSer(List<Double> embedding,String doi) throws ExecutionException, InterruptedException {
        logger.debug("Starting service");
        QdrantClient client = new QdrantClient(
                QdrantGrpcClient.newBuilder(
                                "20604c4d-4674-4818-a91e-6bc6188d59ec.us-west-1-0.aws.cloud.qdrant.io",
                                6334,
                                true
                        )
                        .withApiKey(apiKey)
                        .build()
        );

        logger.debug("Connected to qdrant without errors");
        List<Float> floatembedding=embedding.stream()
                .map(Double::floatValue)
                .toList();


        //searching for points with given embedding
        List<Points.ScoredPoint> points=client.searchAsync(
                        Points.SearchPoints.newBuilder()
                                .setCollectionName("arxiv_collection")
                                .addAllVector(floatembedding)
                                .setLimit(5)
                                .setWithPayload(Points.WithPayloadSelector.newBuilder().setEnable(true).build())
                                .build())
                .get();
        ArrayList<String> recdoi=new ArrayList<>();

        for(Points.ScoredPoint score:points){
            float sim=score.getScore();//this is similarity score

            System.out.println(sim);

            Map<String, JsonWithInt.Value> recomDois=score.getPayloadMap();

            System.out.println(recomDois.isEmpty());

            if(recomDois.containsKey("DOI")){
                String recoms=recomDois.get("DOI").getStringValue();
                recdoi.add(recoms);
                System.out.println(recoms+" "+sim);
            }else{
                System.out.println("No DOI found for point");
            }

        }

        logger.info("Recommendation points retrieved");

        //constructing builders to insert the research paper for future purpose
        Points.Vector vector= Points.Vector.newBuilder()
                .addAllData(floatembedding)
                .build();

        System.out.println(floatembedding);

        UUID uuid= UUID.randomUUID();

        Map<String,JsonWithInt.Value> payload=new HashMap<>();

        payload.put(
                "DOI",
                JsonWithInt.Value.newBuilder()
                        .setStringValue(doi)
                        .build());

        PointStruct point=PointStruct.newBuilder()
                .setId(PointId.newBuilder().setUuid(uuid.toString()).build())
                .setVectors(Points.Vectors.newBuilder()
                        .setVector(vector)
                        .build()
                )
                .putAllPayload(payload)
                .build();

        UpdateResult response=client.upsertAsync(
                "arxiv_collection",
                List.of(point)
        ).get();

        logger.debug("Successfully put the point");
        return recdoi;
    }
}



//some unwanted information
//        Points.Filter filter= Points.Filter.newBuilder()
//                .addMust(
//                        Points.Condition.newBuilder()
//                                .setField(
//                                        Points.FieldCondition.newBuilder()
//                                                .setKey("DOI")
//                                                .setMatch(
//                                                        Points.Match.newBuilder()
//                                                                .setText(doi)
//                                                                .build()
//                                                )
//                                )
//                ).build();
//
//
//        Points.SearchPoints search= Points.SearchPoints.newBuilder()
//                .setCollectionName("arxiv_collection")
//                .setLimit(5)
//                .setFilter(filter)
//                .setWithPayload(Points.WithPayloadSelector.newBuilder().setEnable(true).build())
//                .build();
//
//        CompletableFuture<List<Points.ScoredPoint>> future= (CompletableFuture<List<Points.ScoredPoint>>) client.searchAsync(search);