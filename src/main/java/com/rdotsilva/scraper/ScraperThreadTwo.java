package com.rdotsilva.scraper;

import java.io.IOException;

public class ScraperThreadTwo extends ScraperThread {
    @Override
    public void run() {
        System.out.println("Scraper thread two running");
        Scraper scraper = new Scraper();
        try {
            scraper.scrape();
            scraper.printStocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
