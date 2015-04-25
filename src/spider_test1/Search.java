package spider_test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.HashSet;
import java.util.List;


/**
 * ģ������
 *
 * @author Fanny
 */
public class Search {

    /**
     * ��������
     *
     * @param homepageString ��ҳ��url
     * @param searchString   Ҫ����������
     * @return �������url
     */
    public String search(String homepageString, String searchString) {
        String inputReg = "input[type=text]";                        //�����Ԫ�ص�ѡ�����

        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get(homepageString);
        System.out.println("before search:\tPage title is:" + driver.getTitle() + " url: " + driver.getCurrentUrl());
        // �ҵ��ı���
        WebElement element = driver.findElement(By.cssSelector(inputReg));
        // ���������ؼ���
        element.sendKeys(searchString);
        //�ύ����
        element.submit();
        //��ȡ������url
        String curUrl = driver.getCurrentUrl();
        System.out.println("after search\t : page title is:" + driver.getTitle() + " url: " + curUrl);
        driver.close();
        return curUrl;
    }

    /**
     * ��ȡ��ҳ������ָ����Ʒ�ĳ�����
     *
     * @param searchUrl ���������ҳ
     * @return ��Ʒurl�ļ���
     */
    public HashSet<ProductUrl> getProductPage(String searchUrl) {
        String productUrlReg = "http://www.amazon.cn/.*/dp/.*";            //��Ʒurl��Ӧ�ĸ�ʽ

        HashSet<ProductUrl> products = new HashSet<ProductUrl>();
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get(searchUrl);
        List<WebElement> link = driver.findElements(By.cssSelector("[href]"));//��ȡ���г����ӵ�Ԫ��
        for (WebElement webElement : link) {
            String href = webElement.getAttribute("href");                    //��ȡ�����Ӷ�Ӧ��String
            if (href.matches(productUrlReg) && products.add(new ProductUrl(href))) {//��ȡ��Ʒurl�����뼯���У�ͬһ����Ʒ�Ķ��url���ظ����
                System.out.println(webElement.getText() + '\t' + href);
            }
        }
        System.out.println("get product url over=================================================================");
        System.out.println(products.size());
        driver.close();
        return products;
    }
}