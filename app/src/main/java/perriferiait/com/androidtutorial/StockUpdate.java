package perriferiait.com.androidtutorial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import perriferiait.com.androidtutorial.yahoo.json.YahooStockQuote;

/**
 * Created by admin on 31/08/17.
 */

public class StockUpdate implements Serializable {

    private Integer Id;
    private final String stockSymbol;
    private final BigDecimal price;
    private final Date date;

    public StockUpdate(String stockSymbol, BigDecimal price, Date date) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.date = date;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public static StockUpdate create(YahooStockQuote r) {
        return new StockUpdate(r.getSymbol(), r.getLastTradePriceOnly(), new Date());
    }

    public Integer getId() {
        return Id;
    }
}
