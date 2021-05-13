package com.mainacad;

import com.mainacad.model.Item;
import com.mainacad.service.PromProductParserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class AppRunner {

    public static final Logger LOG = Logger.getLogger(AppRunner.class.getName());

    public static void main( String[] args ) {

        List<Item> items = Collections.synchronizedList(new ArrayList<>());;
        String url = "https://prom.ua/p1217220386-noutbuk-omen-dc1025nc.html";

        PromProductParserService promProductParserService =
                new PromProductParserService(items, url);
        promProductParserService.start();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!items.isEmpty()) {
            LOG.info(items.get(0).toString());
        }


    }
}
