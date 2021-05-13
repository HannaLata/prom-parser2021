package com.mainacad.service;

import com.mainacad.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

public class PromProductParserService extends Thread {

    private static final Logger LOG = Logger.getLogger(PromProductParserService.class.getName());

    private final List<Item> items;
    private final String url;

    public PromProductParserService(List<Item> items, String url) {
        this.items = items;
        this.url = url;
    }

    @Override
    public void run() {

        try {
            Document document = Jsoup.connect(url).get();
            Element productInfo = document.getElementsByAttributeValue("data-qaid", "main_product_info").first();

            String itemId = extractItemId(productInfo);
            String name = extractName(productInfo);
            String imageUrl = extractImageUrl(document);
            String availability = extractAvailability(productInfo);
            BigDecimal price = extractPrice(productInfo);
            BigDecimal initialPrice = extractInitialPrice(productInfo, price);

            Item item = new Item(itemId, name, url, imageUrl, price, initialPrice, availability);

            items.add(item);

        } catch (IOException e) {
            LOG.severe(String.format("Item by URL %s was not extracted!", url));
        }
    }

    private BigDecimal extractInitialPrice(Element productInfo, BigDecimal price) {
        BigDecimal out = price;
        try {
            String outAsText = productInfo.
                    getElementsByAttributeValue("data-qaid", "price_without_discount").first().attr("data-qaprice");
           out = new BigDecimal(outAsText).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.severe(String.format("Item initial price by URL %s was not extracted!", url));
        }
        return out;
    }

    private BigDecimal extractPrice(Element productInfo) {
        BigDecimal out = null;
        try {
            String outAsText = productInfo.getElementsByAttributeValue("data-qaid", "product_price").first().attr("data-qaprice");
            out = new BigDecimal(outAsText).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            LOG.severe(String.format("Item price by URL %s was not extracted!", url));
        }
        return out;
    }

    private String extractAvailability(Element productInfo) {
        String out = "";
        try {
            out = productInfo.
                    getElementsByAttributeValue("data-qaid", "product_presence").first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item availability by URL %s was not extracted!", url));
        }
        return out;
    }

    private String extractImageUrl(Element document) {
        String out = "";
        try {
            out = document.getElementsByAttributeValue("property", "og:image").first().attr("content");
        } catch (Exception e) {
            LOG.severe(String.format("Item image url by URL %s was not extracted!", url));
        }
        return out;
    }


    private String extractName(Element productInfo) {
        String out = "";
        try {
            out = productInfo.
                    getElementsByAttributeValue("data-qaid", "product_name").first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item name by URL %s was not extracted!", url));
        }
        return out;
    }

    private String extractItemId(Element productInfo) {
        String out = "";
        try {
            out = productInfo.
                    getElementsByAttributeValue("data-qaid", "product-sku").first().text();
        } catch (Exception e) {
            LOG.severe(String.format("Item id by URL %s was not extracted!", url));
        }
            return out;

    }
}
