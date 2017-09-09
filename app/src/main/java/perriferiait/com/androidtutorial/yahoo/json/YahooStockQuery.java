package perriferiait.com.androidtutorial.yahoo.json;

import java.util.Date;

public class YahooStockQuery {

    private int count;
    private Date created;
    private YahooStockResults results;

    public Date getCreated() {
        return created;
    }

    public YahooStockResults getResults() {
        return results;
    }
}