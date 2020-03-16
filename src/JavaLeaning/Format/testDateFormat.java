package JavaLeaning.Format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @author: Anhlaidh
 * @date: 2020/3/16 0016 16:34
 */
public class testDateFormat {
    public static void main(String[] args) {
        String strDate = "2008-10-19 10:11:30.345";
        //准备第一个模板，从字符串中提取出日期数字
        String pat1 = "yyyy-MM-dd HH:mm:ss.SSS";
        //准备第二个模板，将提取后的日期数字变为指定的格式
        String pat2 = "yyyy年MM月dd日HH时mm分ss秒SSS毫秒";
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);
        Date d = null;
        try {
            d = sdf1.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(sdf2.format(d));

    }


}
