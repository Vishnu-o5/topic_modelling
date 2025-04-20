package com.dcpackage.topic_modelling.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class ArxivService {

    public ArxivPaperInfo getPaperInfo(String doi) throws Exception {
        String searchUrl = "https://arxiv.org/search/?query="+doi+"&searchtype=DOI";
        Document doc = Jsoup.connect(searchUrl).timeout(10000).get();
//        System.out.println(doc);
        Element result = doc.selectFirst("a.abs-button.download-pdf");
//        System.out.println(result);
        String arxivId = "";
        String viewUrl = "";
        String downloadUrl= "";
        if( result!= null){
            String href = result.attr("href"); // e.g., "/pdf/1703.10593"
            arxivId = href.replace("/pdf/", "").replace(".pdf", "").trim();
            viewUrl = "https://arxiv.org/abs/" + arxivId;
            downloadUrl = "https://arxiv.org" + href;

            System.out.println("arXiv ID: " + arxivId);
            System.out.println("View: " + viewUrl);
            System.out.println("Download: " + downloadUrl);
        }
        if (result == null) {
            throw new Exception("Paper not found for DOI: " + doi);
        }



//        String downloadUrl = "https://arxiv.org/pdf/" + arxivId + ".pdf";

        return new ArxivPaperInfo(arxivId, viewUrl, downloadUrl);
//        return new ArxivPaperInfo(arxivId, viewUrl);
    }

    public static class ArxivPaperInfo {
        private String arxivId;
        private String viewUrl;
        private String downloadUrl;
        public ArxivPaperInfo(String arxivId, String viewUrl, String downloadUrl) {
//        public ArxivPaperInfo(String arxivId, String viewUrl) {
            this.arxivId = arxivId;
            this.viewUrl = viewUrl;
            this.downloadUrl = downloadUrl;
        }

        public String getArxivId() {
            return arxivId;
        }

        public String getViewUrl() {
            return viewUrl;
        }

        public String getDownloadUrl() {
            return downloadUrl;
       }
    }
}
