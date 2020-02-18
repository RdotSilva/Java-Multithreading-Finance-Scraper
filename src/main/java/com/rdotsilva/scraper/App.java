package com.rdotsilva.scraper;

import java.io.IOException;


public class App
{
    public static void main( String[] args ) {
        ScraperThread scraperThread = new ScraperThread();
        ScraperThread scraperThreadTwo = new ScraperThreadTwo();
        scraperThread.start();
//        scraperThreadTwo.start();
    }
}

