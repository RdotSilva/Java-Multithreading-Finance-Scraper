package com.rdotsilva.scraper;

import java.io.IOException;

public class ScraperThread extends Thread {
    @Override
    public void run() {
        System.out.println("Scraper thread one running");
        Scraper scraper = new Scraper();
        try {
            scraper.scrape();
            scraper.printStocks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
