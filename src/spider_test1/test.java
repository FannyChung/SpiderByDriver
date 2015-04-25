/**
 *
 */
package spider_test1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FannyChung
 */
public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String url = "http://www.baidu.com";

        String result = sendGet(url);
        System.out.println(result);
    }

    static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;

        try {
            URL realUrl = new URL(url);            //将string转成url对象
            URLConnection connection = realUrl.openConnection();//初始化一个链接到url的链接
            connection.connect();                            //开始实际的连接
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));// 用来临时存储抓取到的每一行的数据

            String line;                            //遍历抓取到的每一行并将其存储到result里面
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {                            //
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    static String RegexString(String targetStr, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);//定义一个样式模板
        Matcher matcher = pattern.matcher(targetStr);//定义一个matcher用来做匹配
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
