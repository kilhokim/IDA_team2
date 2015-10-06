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

    public static void main(String[] arge) {
        AppInfoCrawler crawler = new AppInfoCrawler();
        crawler.crawlAppInfo();
        crawler.appInfoUpdate();
    }

    private void appInfoUpdate() {
        int[] indices = getIndexToStart();
        if (indices[0] >= 0 || indices[1] >= 0) {
            DBConn.connect(false);
            Statement stmt = null;
            try {
                stmt = DBConn.con.createStatement();
                for (String key : appList.keySet()) {
                    App tempApp = appList.get(key);
                    if (tempApp.appName.equals("NOT_EXISTS")) {
                        if (indices[0] >= 0) {
                            stmt.execute("update ApplicationsProbe set appName = '" + tempApp.appName
                                    + "' where id >= " + indices[0] + " packageName = '" + tempApp.packageName + "'");
                        }
                        if (indices[1] >= 0) {
                            stmt.execute("update RunningApplicationsProbe set appName = '" + tempApp.appName
                                    + "' where id >= " + indices[1] + " packageName = '" + tempApp.packageName + "'");
                        }

                    } else {
                        if (indices[0] >= 0) {
                            stmt.execute("update ApplicationsList set appName = '" + tempApp.appName
                                    + "', category = '" + tempApp.category + "', appPrice = " + tempApp.price
                                    + "' where id >= " + indices[0] + " packageName = '" + tempApp.packageName + "'");
                        }
                        if (indices[1] >= 0) {
                            stmt.execute("update RunningApplicationsList set appName = '" + tempApp.appName
                                    + "', category = '" + tempApp.category + "', appPrice = " + tempApp.price
                                    + "' where id >= " + indices[1] + " packageName = '" + tempApp.packageName + "'");
                        }
                    }
                }
                stmt.close();
                DBConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] getIndexToStart() {
        int[] indices = new int[2];
        try {
            ResultSet rs = DBConn.execQuery("select id from ApplicationsProbe where appName is null order by asc", false);
            if (rs.next()) {
                indices[0] = rs.getInt("id");
            } else {
                indices[0] = -1;
            }
            rs.close();

            rs = DBConn.execQuery("select id from RunningApplicationsProbe where appName is null order by asc", false);
            if (rs.next()) {
                indices[1] = rs.getInt("id");
            } else {
                indices[1] = -1;
            }
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indices;
    }

    private void crawlAppInfo() {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        appList = new HashMap<String, App>();

        ResultSet rs = DBConn.execQuery("select distinct packageName from ApplicationsProbe where appName is null limit 100", false);
        String titleStart = "<h1 class=\"document-title\" itemprop=\"name\"> <div>";
        String titleEnd = "</div>";
        String categoryStart = "<span itemprop=\"genre\">";
        String categoryEnd = "</span>";
        String priceStart = "<span>���� ?";
        String priceEnd = "</span>";
        try {
            while (rs.next()) {
                String packageName = rs.getString("packageName");
                App tempApp = new App(packageName);

                try {
                    url = new URL("https://play.google.com/store/apps/details?id=" + packageName);
                    is = url.openStream();  // throws an IOException
                    br = new BufferedReader(new InputStreamReader(is));

                    while ((line = br.readLine()) != null) {
                        if (line.contains(titleStart)) {
                            tempApp.appName = line.substring(line.indexOf(titleStart) + titleStart.length(),
                                    line.indexOf(titleEnd, line.indexOf(titleStart) + titleStart.length()));
                            if (line.contains(categoryStart)) {
                                tempApp.category = line.substring(line.indexOf(categoryStart) + categoryStart.length(),
                                        line.indexOf(categoryEnd, line.indexOf(categoryStart) + categoryStart.length()));
                            }
                            if (line.contains(priceStart)) {
                                String price = line.substring(line.indexOf(priceStart) + priceStart.length(),
                                        line.indexOf(priceEnd, line.indexOf(priceStart) + priceStart.length()));
                                while (price.contains(",")) {
                                    price = price.substring(0, price.indexOf(",")) + price.substring(price.indexOf(",") + 1);
                                }
                                tempApp.price = Integer.parseInt(price);
                            } else {
                                tempApp.price = 0;
                            }
                            System.out.println(tempApp.toString());
                        } else {
                            tempApp.appName = "NOT_EXISTS";
                        }
                    }
                } catch (IOException ioe) {
                    tempApp.appName = "NOT_EXISTS";
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException ioe) {
                        // nothing to see here
                    }
                }
                appList.put(packageName, tempApp);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConn.close();
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
    }
}
