package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaussic on 2017/3/21.
 * 从文档中读取新闻，格式为 url \t docno \t title \t content
 */
public class SogouNewsFromFile {

    // 读取一行新闻
    public Map<String, String> readNews(String news) {
        Map<String, String> newsMap = new HashMap<>();

        String[] newss = news.split("\t");
        newsMap.put("title", newss[0]);
        newsMap.put("url", newss[1]);
        newsMap.put("docno", newss[2]);
        newsMap.put("content", newss[3]);

        return newsMap;

    }



    public static void maint(String[] args) throws Exception {
        MysqlConnector conn = new MysqlConnector();

        SogouNewsFromFile newsFromFile = new SogouNewsFromFile();
        FileInputStream fis = new FileInputStream("output_all.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String strLine;
        int totalNum = 0;
        while ((strLine = br.readLine()) != null) {
            totalNum++;
            Map<String, String> news = newsFromFile.readNews(strLine);
            try {
                if(totalNum <= 1130000)
                    continue;
                conn.insertNews(news);
            } catch (Exception e) {
                System.out.println("插入失败。");
            }

            if (totalNum % 5000 == 0) {
                // System.out.println(news.get("url") + "  " + news.get("docno") + "  " + news.get("title"));
                System.out.println(totalNum);
            }
        }

        conn.readNewsCount();
        conn.closeDatabase();
    }

    public static void main(String[] args) throws Exception {
        MysqlConnector conn = new MysqlConnector();

        SogouNewsFromFile newsFromFile = new SogouNewsFromFile();
        FileInputStream fis = new FileInputStream("output_new.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String strLine;
        int totalNum = 0;
        while ((strLine = br.readLine()) != null) {
            totalNum++;
            if(totalNum <= 1090000)
                continue;
            try {
                Map<String, String> news = newsFromFile.readNews(strLine);
                conn.updateNews(news.get("title"), news.get("content"), news.get("docno"));
            } catch (Exception e) {
                System.out.println("插入失败。" + totalNum);
            }

            if (totalNum % 5000 == 0) {
                // System.out.println(news.get("url") + "  " + news.get("docno") + "  " + news.get("title"));
                System.out.println(totalNum);
            }
        }

        conn.readNewsCount();
        conn.closeDatabase();
    }
}
