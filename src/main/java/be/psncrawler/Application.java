package be.psncrawler;

import be.psncrawler.config.CrawlerConfig;
import be.psncrawler.service.CrawlerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class Application extends SpringBootServletInitializer {
    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

    @Bean CrawlerService crawlerService() throws Exception {
        CrawlerService crawlerService = new CrawlerService(this.crawlerConfig());
        crawlerService.crawling();
        return crawlerService;
    }

    @Bean CrawlerConfig crawlerConfig(){
       return new CrawlerConfig();
    }
}
