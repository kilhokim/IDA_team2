package project_team2.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kilho Kim
 */
public class Parser {

  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1)" +
     "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

  public static Map<String, Integer> parseAll(String url, String cssQuery)
          throws IOException {

    Document doc;

    // need http protocol
    doc = Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Accept-Encoding","gzip,deflate,sdch")
            .get();

    // get all links
    Elements categories = doc.select(cssQuery);
    Map<String, Integer> result = new HashMap<String, Integer>();
    int i = 0;
    for (Element category : categories) {
      result.put(category.text(), i++);
    }

    return result;
  }

  public static String parseCategory(String url, String packageName,
                                   String cssQuery) throws IOException {

    Document doc;

    String completeUrl = url + packageName;
    // need http protocol
    doc = Jsoup.connect(completeUrl)
            .userAgent(USER_AGENT)
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Accept-Encoding","gzip,deflate,sdch")
            .get();
            // .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:41.0) Gecko/20100101 Firefox/41.0").get();

    // get page title
    String title = doc.title();
    // System.out.println("title : " + title);

    // get all links
    Elements categories = doc.select(cssQuery);
    for (Element category : categories) {

      // get the value from category
      // System.out.println("\nlink : " + category.attr("href"));
      String cat = category.text().replaceAll("\\s", "");
      System.out.print(cat + " ");
      return cat;
    }

    return null;
  }

  public static void main(String[] args) {
    try {

      Map<String, Integer> categoryMap =
          parseAll("https://play.google.com/store/apps?hl=en",
                  "a[class=\"child-submenu-link\"]");
      System.out.println(categoryMap.toString());

      String cat1 = parseCategory("https://play.google.com/store/apps/details?hl=en&id=",
              "com.dencreak.spbook", "span[itemprop=\"genre\"]");
      System.out.println(cat1 + "=" + categoryMap.get(cat1));
      String cat2 = parseCategory("https://play.google.com/store/apps/details?hl=en&id=",
              "net.daum.android.map", "span[itemprop=\"genre\"]");
      System.out.println(cat2 + "=" + categoryMap.get(cat2));

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
