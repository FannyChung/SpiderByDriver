package spider_test1;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class ReivewWebDriver {
    HtmlUnitDriver driver = new HtmlUnitDriver();
    private static Vector<Review> reviews = new Vector<Review>(); // ֻ��Ҫ�������ӣ�����ʹ��vector
    private WritableWorkbook book;

    /**
     * ��ȡ��һҳ����
     * ����������Ϣ
     *
     * @param thisPage
     */
    public void nextPage(String thisPage) {
        String nextp = null;
        String reviewReg = "table[id=productReviews]>tbody>tr>td>div";// ������Ϣ��ѡ�����
        String textReg = ".reviewText"; // �����ı���ѡ�����
        String levelReg = "span";// ���۵ȼ�
        String titleReg = "b"; // ���۱���
        String timeReg = "nobr"; // ����ʱ��
        String dateFormat = "yyyy��MM��dd��"; // ʱ���ʽ

        driver.get(thisPage);

        // ����������Ϣ
        List<WebElement> ele1 = driver
                .findElementsByCssSelector(reviewReg);
        for (WebElement element : ele1) {// ��ȡ����Ԫ�أ�����review�У�ÿ��Ԫ�������ۡ����ߡ�ʱ���
            Review review = new Review();
            WebElement textElements = element.findElement(By
                    .cssSelector(textReg));// ��ȡ�����ı�
            String cString = textElements.getText();
            System.out.println(cString);
            review.setText(cString);

            WebElement levelElements = element.findElement(By
                    .cssSelector(levelReg));// ��ȡ�Ǽ���Ϣ
            cString = levelElements.getText();
            int level = cString.charAt(2) - '0';
            // System.out.println(level);
            review.setLevel(level);

            WebElement titleEle = element.findElement(By.cssSelector(titleReg));
            cString = titleEle.getText(); // ����
            // System.out.println(cString);
            review.setReTitle(cString);

            WebElement timeEle = element.findElement(By.cssSelector(timeReg));
            cString = timeEle.getText(); // ��ȡ����
            // System.out.println(cString);
            SimpleDateFormat s = new SimpleDateFormat(dateFormat);
            Date d = null;
            try {
                long t = s.parse(cString).getTime();
                d = new Date(t);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            review.setTime(d);

            reviews.add(review);
        }

        //��ȡ��һҳ��������
        WebElement element = null;
        try {
            element = driver.findElementByPartialLinkText("��һҳ");
        } catch (Exception e) {
            System.err.println("��ǰ��" + thisPage + "û����һҳ\n");
            return;
        }
        nextp = element.getAttribute("href");
        System.out.println(nextp);
        nextPage(nextp);
    }

    /**
     * ����Ʒҳ����ת������ҳ��
     *
     * @param productPage
     * @return
     */
    public String getReviewPage(String productPage) {
        driver.get(productPage);
        System.out.println("Page title is:" + driver.getTitle() + " url: "
                + driver.getCurrentUrl());
        WebElement element = driver
                .findElementByCssSelector("div[id=revF]>div>a");
        element.click();
        String url = driver.getCurrentUrl();
        System.out.println("Page title is:" + driver.getTitle() + " url: "
                + url);
        return url;
    }

    /**
     * ��ӡ���е�����
     *
     * @param reviews
     * @param sheet   xls�ļ��е�һ��sheet��
     * @throws RowsExceededException
     * @throws WriteException
     */
    public void printReviews(Vector<Review> reviews, WritableSheet sheet)
            throws RowsExceededException, WriteException {
        int col = 0;
        Label newLabel;
        // newLabel=new Label(0,0,"�ı�");
        for (Review review : reviews) {
            newLabel = new Label(0, col, review.getText());
            sheet.addCell(newLabel);
            newLabel = new Label(1, col, review.getLevel() + "");
            sheet.addCell(newLabel);
            newLabel = new Label(2, col, review.getReTitle());
            sheet.addCell(newLabel);
            newLabel = new Label(3, col, review.getTime().toString());
            sheet.addCell(newLabel);
            newLabel = new Label(4, col, review.getUserName());
            sheet.addCell(newLabel);
            col++;
            System.out.println("excel--------------------------------" + col);
        }
    }

    /**
     * ��excel�ļ�
     *
     * @param fileName
     */
    public void openFile(String fileName) {
        File file = new File(fileName);
        try {
            book = Workbook.createWorkbook(file);
        } catch (IOException e) {
            System.err.println("excel���ʧ��");
            e.printStackTrace();
        }
    }

    /**
     * �ر�excel�ļ�
     */
    public void closeFile() {
        try {
            book.write();
            book.close();
        } catch (IOException e) {
            System.err.println("excel��д��ʧ��");
            e.printStackTrace();
        } catch (WriteException e) {
            System.err.println("excel��ر�ʧ��");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("û�д�����");
        }
    }

    /**
     * ��ȡexcel�����
     *
     * @return
     */
    public WritableWorkbook getBook() {
        return book;
    }

    public static void main(String[] args) {
        Search search = new Search();
        String searchStr="�ֻ�";
		String str= URLEncoder.encode(searchStr);

		String s = search.search("http://www.amazon.cn/ref=nav_logo", searchStr);//������ҳ����������
        HashSet<ProductUrl> productsStrings = search.getProductPage(s);//��ȡ������õ���������Ʒurl
        ReivewWebDriver nDriver = new ReivewWebDriver();

        WritableSheet sheet = null;
        nDriver.openFile("t.xls");
        int i = 0;
        int productNum = 5;//��Ҫ����Ʒ��Ŀ
        for (ProductUrl productUrl : productsStrings) {
            if (i == productNum) {
                break;
            }
            nDriver.nextPage(nDriver.getReviewPage(productUrl.getString()));
            sheet = nDriver.getBook().createSheet(
                    productUrl.getString().substring(0,10), i);// ���ñ����ֺͱ��
            try {
                nDriver.printReviews(reviews, sheet);
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            reviews.clear();
            i++;
        }

        nDriver.closeFile();
        nDriver.driver.close();
    }
}
