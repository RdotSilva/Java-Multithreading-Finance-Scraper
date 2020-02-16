package com.rdotsilva.scraper;
import java.sql.Timestamp;
public class Stock {

    private long id;

    private Timestamp scrapeDate;
    private String symbol;
    private String lastPrice;
    private String changeAmount;
    private String changePercent;
    private String volume;
    private String averageVolume;
    private String marketCap;

}