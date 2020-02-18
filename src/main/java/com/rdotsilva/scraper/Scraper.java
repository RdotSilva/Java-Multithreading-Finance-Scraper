package com.rdotsilva.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Scraper {
    String driverType;
    String driverLocation;
    WebDriver driver;
    Connection conn = null;

    public Scraper(String driverName) {
        if (driverName.equals("chrome")) {
            this.driverType = "webdriver.chrome.driver";
            this.driverLocation = "C:\\chromedriver\\chromedriver.exe";
            System.setProperty(driverType, driverLocation);
            this.driver = new ChromeDriver();
        } else if (driverName.equals("gecko")) {
            this.driverType = "webdriver.gecko.driver";
            this.driverLocation = "C:\\geckodriver\\geckodriver.exe";
            System.setProperty(driverType, driverLocation);
            this.driver = new FirefoxDriver();
        }
    }

    ArrayList<Stock> stockList = new ArrayList<>();

    public static void login(WebDriver driver) {
        String loginUrl = "https://login.yahoo.com/config/login?.src=fpctx&.intl=us&.lang=en-US&.done=https%3A%2F%2Fwww.yahoo.com";
        String email = "ryansilva.student@careerdevs.com";
        String password = "%x_2*xC98H;";

        String loginId = "login-username";
        String passwordId = "login-passwd";

        driver.get(loginUrl);

        driver.findElement(By.id(loginId)).sendKeys(email + Keys.RETURN);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.id(passwordId)).sendKeys(password + Keys.RETURN);
    }

    public static List<WebElement> navigateToStockData(WebDriver driver) {
        String portfolioUrl = "https://finance.yahoo.com/portfolio/p_0/view/v1";

        String stockTableTag = "tbody";
        String stockRowsClass = "simpTblRow";

        driver.get(portfolioUrl);

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement stockTable = driver.findElement(By.tagName(stockTableTag));
        List<WebElement> stockRows = stockTable.findElements(By.className(stockRowsClass));

        return stockRows;
    }

    public void buildStockList(List<WebElement> stockRows) {
        for (WebElement row : stockRows
        ) {
            String[] eachStock = row.getText().split(" ");
            String[] splitSymbol = eachStock[0].split("\\r?\\n");
            String[] splitMarketCap = eachStock[9].split("\\r?\\n");

            java.util.Date scrapeDate = new java.util.Date();

            // Convert timestamp to sql format.
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(scrapeDate.getTime());

            // Extract stock data
            String symbol = splitSymbol[0];
            String lastPrice = splitSymbol[1];
            String changeAmount = eachStock[1];
            String changePercent = eachStock[2];
            String volume = eachStock[6];
            String averageVolume = eachStock[8];
            String marketCap = splitMarketCap[0];

            // Build Stock Object with Data
            Stock stock = new Stock();
            stock.setScrapeDate(sqlDate);
            stock.setSymbol(symbol);
            stock.setLastPrice(lastPrice);
            stock.setChangeAmount(changeAmount);
            stock.setChangePercent(changePercent);
            stock.setVolume(volume);
            stock.setAverageVolume(averageVolume);
            stock.setMarketCap(marketCap);

            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/multithread_finance",);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sql = "insert into stock (scrape_date, symbol, last_price, change_amount, change_percent, volume, average_volume, market_cap)" + "values (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setTimestamp(1, sqlDate);
            preparedStatement.setString(2, symbol);
            preparedStatement.setString(3, lastPrice);
            preparedStatement.setString(4, changeAmount);
            preparedStatement.setString(5, changePercent);
            preparedStatement.setString(6, volume);
            preparedStatement.setString(7, averageVolume);
            preparedStatement.setString(8, marketCap);

            int rowNums = preparedStatement.executeUpdate();

            System.out.println("Number of rows affected: " + rowNums);


            stockList.add(stock);
        }

    }

    public void scrape(WebDriver driver) throws  IOException {
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setHeadless(true);

        login(driver);

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<WebElement> stockRows = navigateToStockData(driver);

        buildStockList(stockRows);
        driver.close();
    }

    public void printStocks() {
        for (Stock stock: stockList
             ) {
            System.out.println(stock.getSymbol());
        }
    }
}
