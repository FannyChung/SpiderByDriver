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
            URL realUrl = new URL(url);            //��stringת��url����
            URLConnection connection = realUrl.openConnection();//��ʼ��һ�����ӵ�url������
            connection.connect();                            //��ʼʵ�ʵ�����
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));// ������ʱ�洢ץȡ����ÿһ�е�����

            String line;                            //����ץȡ����ÿһ�в�����洢��result����
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            System.out.println("����GET��������쳣��" + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("����GET��������쳣��" + e);
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
        Pattern pattern = Pattern.compile(patternStr);//����һ����ʽģ��
        Matcher matcher = pattern.matcher(targetStr);//����һ��matcher������ƥ��
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
