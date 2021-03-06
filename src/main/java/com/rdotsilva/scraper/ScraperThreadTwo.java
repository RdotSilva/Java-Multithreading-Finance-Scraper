package com.rdotsilva.scraper;

import java.io.IOException;
import java.sql.SQLException;

public class ScraperThreadTwo extends ScraperThread {
    @Override
    public void run() {
        System.out.println("Scraper thread two running");
        Scraper scraper = new Scraper("gecko");
        try {
            scraper.scrape(scraper.driver);
            scraper.printStocks();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
