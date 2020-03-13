package HomeWork.Chapter01.TimeTool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 计算时间
 * @author: Anhlaidh
 * @date: 2020/2/5 0005 15:31
 */
public class TimeTool {
    private static final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.SSS");
    public interface Task{
        void execute();
    }
    public static void check(String title,Task task){
        if (task==null) return;
        title = (title==null)?"":("【"+title+"】");
        System.out.println(title);
        System.out.println("Start Time:"+fmt.format(new Date()));
        long begin=System.currentTimeMillis();
        task.execute();
        long end=System.currentTimeMillis();
        System.out.println("End Time:"+fmt.format(new Date()));
        double delta = (end-begin)/1000.0;
        System.out.println("Total:"+delta+"s");
        System.out.println("----------------------------------------------------");
    }
}
