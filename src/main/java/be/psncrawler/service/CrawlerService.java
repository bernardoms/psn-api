package be.psncrawler.service;

import be.psncrawler.config.CrawlerConfig;
import be.psncrawler.crawler.PsnCrawler;
import be.psncrawler.model.PsnContent;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrawlerService {

    private CrawlerConfig crawlConfig;

    public CrawlerService(CrawlerConfig crawlConfig){
        this.crawlConfig = crawlConfig;
    }

    //UPDATE DEALS AT 15 PM IN TUESDAY BECAUSE PSN UPDATES DEALS EVERY TUESDAY AT 14
    @Scheduled(cron = "0 0 15 * * TUE")
    public void crawling() throws Exception {
        String crawlStorageFolder = crawlConfig.getStorageFolder();
        int numberOfCrawlers = Integer.parseInt(crawlConfig.getNumCrawlers());

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(Integer.parseInt(crawlConfig.getMaxDepth()));

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        crawlConfig.getUrl().forEach(controller::addSeed);

        controller.start(PsnCrawler.class, numberOfCrawlers);
    }

    public List<PsnContent> getAll(PsnContent psnContentQuery){
        List<PsnContent> psnContents = PsnCrawler.psnDealsContents;

        if(psnContentQuery.getTitle() != null){
            psnContents = psnContents.stream().filter(psnContent -> psnContent.getTitle().contains(psnContentQuery.getTitle())).collect(Collectors.toList());
        }

        if(psnContentQuery.getType() != null){
            psnContents = psnContents.stream().filter(psnContent ->  psnContentQuery.getType().equals(psnContent.getType())).collect(Collectors.toList());
        }

        if(psnContentQuery.getPlatform() != null){
            psnContents = psnContents.stream().filter(psnContent -> psnContentQuery.getPlatform().equals(psnContent.getPlatform())).collect(Collectors.toList());
        }
        return psnContents;
    }
}
