package be.psncrawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "crawler")
public class CrawlerConfig {

    @Value("url")
    private List<String> url;
    @Value("numCrawlers")
    private String numCrawlers;
    @Value("maxDepth")
    private String maxDepth;
    @Value("storageFolder")
    private String storageFolder;

    public List<String> getUrl() {
        return url;
    }

    public String getNumCrawlers() {
        return numCrawlers;
    }

    public String getMaxDepth() {
        return maxDepth;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }

    public void setNumCrawlers(String numCrawlers) {
        this.numCrawlers = numCrawlers;
    }

    public void setMaxDepth(String maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }
}
