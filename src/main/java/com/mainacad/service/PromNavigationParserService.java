package com.mainacad.service;

import com.mainacad.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class PromNavigationParserService extends Thread {

    private static final Logger LOG = Logger.getLogger(PromNavigationParserService.class.getName());

    private final List<Item> items;
    private final String url;
    private final List<Thread> threads;

    public PromNavigationParserService(List<Item> items, String url, List<Thread> threads) {
        this.items = items;
        this.url = url;
        this.threads = threads;
    }

    @Override
    public void run() {

        //product link extraction
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
            Element productGallery = document.getElementsByAttributeValue("data-qaid", "product_gallery").first();

            Elements productElements = productGallery.getElementsByAttributeValue("data-qaid", "product_link");
            Set<String> itemLinks = new HashSet<>();
            productElements.forEach(it -> itemLinks.add(it.attr("href")));

            int counter = 0;
            for (String link: itemLinks) {
                if(counter>3) {
                    break;
                }
                if(link != null) {
                    PromProductParserService promProductParserService = new PromProductParserService(items, url);
                    threads.add(promProductParserService);
                    promProductParserService.start();
                    counter++;
                }
            }

        } catch (IOException e) {
            LOG.severe(String.format("Products by URL %s were not extracted!", url));
        }

        //pagination
        try {
            if(!url.contains("pages=")) {
                Element lastPageElement = document.getElementsByAttributeValue("data-qaid", "pages").last();
                if(lastPageElement != null) {
                    Integer lastPage = Integer.valueOf(lastPageElement.text());
                    for (int i = 2; i <= lastPage ; i++) {
                        String nextPageUrl = url + "&page=" + i;
                        PromNavigationParserService promNavigationParserService =
                                new PromNavigationParserService(items, nextPageUrl,threads);
                        threads.add(promNavigationParserService);
                        promNavigationParserService.start();
                    }
                }
            }
        } catch (Exception e) {
            LOG.severe(String.format("Pages by URL %s were not extracted!", url));
        }

    }
}
