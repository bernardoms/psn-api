package be.psncrawler.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import be.psncrawler.model.PsnContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PsnCrawler extends WebCrawler {

    public static List<PsnContent> psnDealsContents = new ArrayList<>();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();

        return !FILTERS.matcher(href).matches()
                && href.startsWith("https://store.playstation.com/en-us/") && href.contains("alldeals");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData && url.contains("ALLDEALS")) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            Document doc = Jsoup.parse(html);
            Elements titles = doc.select("div.grid-cell__body").select("span[title]");
            Elements discountPrices = doc.select("div.grid-cell__body").select("span.grid-cell__prices-container").select("div > h3.price-display__price");
            Elements originalPrices = doc.select("div.grid-cell__body").select("span.grid-cell__prices-container").select("div.price");
            List<Element> platforms =  doc.select(".grid-cell__left-details > div").stream().filter(f->f.getElementsByAttributeValue("class" , "grid-cell__left-detail grid-cell__left-detail--detail-1").size() > 0).collect(Collectors.toList());
            List<Element> types =  doc.select(".grid-cell__left-details > div").stream().filter(f->f.getElementsByAttributeValue("class" , "grid-cell__left-detail grid-cell__left-detail--detail-2").size() > 0).collect(Collectors.toList());
            List<String> img = doc.getElementsByAttributeValue("class", "product-image__img product-image__img--main").select("img[src]").stream().map(f-> f.attr("src")).collect(Collectors.toList());

            for(int i = 0; i < titles.size() ; i++){
                PsnContent psnContent = new PsnContent();
                psnContent.setTitle(titles.get(i).text());

                if(i < discountPrices.size() - 1){
                    psnContent.setDiscountPrice(discountPrices.get(i).text());
                }

                if(i < originalPrices.size() - 1){
                    psnContent.setOriginalPrice(originalPrices.get(i).text());
                }

                if(i < platforms.size() - 1){
                    psnContent.setPlatform(platforms.get(i).text());
                }

                if(i < types.size() - 1){
                    psnContent.setType(types.get(i).text());
                }

                if(i < types.size() - 1){
                    psnContent.setImg(img.get(i));
                }
                psnDealsContents.add(psnContent);
            }
            System.out.println("Text length: " + text.length());
            System.out.println("Html length: " + html.length());
            System.out.println("Number of outgoing links: " + links.size());
        }
    }
}
