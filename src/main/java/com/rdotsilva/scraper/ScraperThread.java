package com.rdotsilva.scraper;

import java.io.IOException;
import java.sql.SQLException;

public class ScraperThread extends Thread {
    @Override
    public void run() {
        System.out.println("Scraper thread one running");
        Scraper scraper = new Scraper("chrome");
        try {
            scraper.scrape(scraper.driver);
            scraper.printStocks();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
