package be.psncrawler.controller;


import be.psncrawler.model.PsnContent;
import be.psncrawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/psn")
public class PsnContentController {

    private CrawlerService crawlerService;

    @Autowired
    public PsnContentController(CrawlerService crawlerService){
        this.crawlerService = crawlerService;
    }

    @GetMapping("/deals") public ResponseEntity<List<PsnContent>> getDeals(PsnContent psnContent){
        return  ResponseEntity.ok(crawlerService.getAll(psnContent));
    }
}
