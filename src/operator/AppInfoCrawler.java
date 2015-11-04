package operator;

import dbConnection.DBConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by Ethan on 2015-09-06.
 */
public class AppInfoCrawler {
    HashMap<String, App> appList;
    boolean moreAppsToCrawl = true;

    public static void main(String[] args) {
        AppInfoCrawler crawler = new AppInfoCrawler();
        while(crawler.moreAppsToCrawl){
            crawler.moreAppsToCrawl = crawler.crawlAppInfo();
            crawler.appInfoUpdate();
        }
        DBConn.close();
    }

    private void appInfoUpdate() {
        int[] indices = getIndexToStart();
        if (indices[0] >= 0 || indices[1] >= 0) {
            DBConn.connect(0);
            Statement stmt = null;
            try {
                stmt = DBConn.con.createStatement();
                for (String key : appList.keySet()) {
                    App tempApp = appList.get(key);
                    if (tempApp.appName.equals("NOT_EXISTS")) {
                        if (indices[0] >= 0) {
                            stmt.execute("update ApplicationsProbe set appName = '" + tempApp.appName
                                    + "' where id >= " + indices[0] + " and packageName = '" + tempApp.packageName + "'");
                        }
                        if (indices[1] >= 0) {
                            stmt.execute("update RunningApplicationsProbe set appName = '" + tempApp.appName
                                    + "' where id >= " + indices[1] + " and mPackage = '" + tempApp.packageName + "'");
                        }
                    } else {
                        if (indices[0] >= 0) {
                            stmt.execute("update ApplicationsProbe set appName = '" + tempApp.appName
                                    + "', appCategory = '" + tempApp.category + "', appPrice = " + tempApp.price
                                    + " where id >= " + indices[0] + " and packageName = '" + tempApp.packageName + "'");
                        }
                        if (indices[1] >= 0) {
                            stmt.execute("update RunningApplicationsProbe set appName = '" + tempApp.appName
                                    + "', appCategory = '" + tempApp.category + "', appPrice = " + tempApp.price
                                    + " where id >= " + indices[1] + " and mPackage = '" + tempApp.packageName + "'");
                        }
                    }
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] getIndexToStart() {
        int[] indices = new int[2];
        try {
            ResultSet rs = DBConn.execQuery("select id from ApplicationsProbe where appName is null order by id asc", 0);
            if (rs.next()) {
                indices[0] = rs.getInt("id");
            } else {
                indices[0] = -1;
            }
            rs.close();

            rs = DBConn.execQuery("select id from RunningApplicationsProbe where appName is null order by id asc", 0);
            if (rs.next()) {
                indices[1] = rs.getInt("id");
            } else {
                indices[1] = -1;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indices;
    }

    private boolean crawlAppInfo() {
        boolean moreAppsToCrawl = false;
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        appList = new HashMap<String, App>();

        ResultSet rs = DBConn.execQuery("select distinct packageName from ApplicationsProbe where appName is null limit 100", 0);
        String titleStart = "<h1 class=\"document-title\" itemprop=\"name\"> <div>";
        String titleEnd = "</div>";
        String categoryStart = "<span itemprop=\"genre\">";
        String categoryEnd = "</span>";
        String priceStart = "<span>구매 ";
        String priceEnd = "</span>";
        try {
            while (rs.next()) {
                moreAppsToCrawl = true;
                String packageName = rs.getString("packageName");
                App tempApp = new App(packageName);

                try {
                    url = new URL("https://play.google.com/store/apps/details?id=" + packageName);
                    is = url.openStream();  // throws an IOException
                    br = new BufferedReader(new InputStreamReader(is));

                    tempApp.appName = "NOT_EXISTS";
                    tempApp.price = 0;
                    while ((line = br.readLine()) != null) {
                        if (line.contains(titleStart)) {
                            tempApp.appName = line.substring(line.indexOf(titleStart) + titleStart.length(),
                                    line.indexOf(titleEnd, line.indexOf(titleStart) + titleStart.length()));
                        }
                        if (line.contains(categoryStart)) {
                            tempApp.category = line.substring(line.indexOf(categoryStart) + categoryStart.length(),
                                    line.indexOf(categoryEnd, line.indexOf(categoryStart) + categoryStart.length()));
                        }
                        if (line.contains(priceStart)) {
                            String price = line.substring(line.indexOf(priceStart) + priceStart.length() + 1,
                                    line.indexOf(priceEnd, line.indexOf(priceStart) + priceStart.length()));
                            while (price.contains(",")) {
                                price = price.substring(0, price.indexOf(",")) + price.substring(price.indexOf(",") + 1);
                            }
                            tempApp.price = Integer.parseInt(price);
                        }
                        if(!tempApp.appName.equals("NOT_EXISTS") && tempApp.price > 0 && tempApp.category != null) break;
                    }
                    System.out.println(tempApp.toString());
                } catch (IOException ioe) {
                    tempApp.appName = "NOT_EXISTS";
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ioe) {
                        // nothing to see here
                    }
                }
                tempApp.symbolCheck();
                appList.put(packageName, tempApp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moreAppsToCrawl;
    }

    private class App {
        public String packageName;
        public String category;
        public String appName;
        public int price;

        public App(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public String toString() {
            return packageName + ", " + appName + ", " + category + ", " + price;
        }

        public void symbolCheck(){
            if(appName != null){
                while(appName.contains("\'")){
                    appName = appName.substring(0, appName.indexOf("\'")) + appName.substring(appName.indexOf("\'") + 1, appName.length());
                }
            }
        }
    }
}
