import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * LocalDate是一个线程安全的类，它是用final修饰的，主要是对日期进行操作
 */
@SpringBootTest(classes = Application.class)
public class LocalDateTest {

    @Test
    public void test(){
        // 打印结果 2023-06-15
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        // 打印结果 20230615
        String now = String.valueOf(localDate);
        System.out.println(now.replaceAll("-", ""));

        //打印结果为2022-04-10
        LocalDate today = LocalDate.of(2023, 7, 9);
        //————————————————————————对年的操作————————————————————————————
        System.out.println("对年的操作————————————————————————————");
        //获取今年的年份，打印结果为2022
        int year = today.getYear();
        System.out.println("获取今年的年份:"+year);
        //判断今年是否为闰年,2022年不是闰年，打印结果为false
        boolean leapYear = today.isLeapYear();
        System.out.println("判断今年是否为闰年:"+leapYear);
        //获取今年有多少天，打印结果为365
        int lengthOfYear = today.lengthOfYear();
        System.out.println("获取今年有多少天:"+lengthOfYear);

        //————————————————————————对月的操作————————————————————————————
        System.out.println("对月的操作————————————————————————————");
        //获取本月份名称，打印结果为APRIL
        Month month = today.getMonth();
        System.out.println("获取本月份名称:"+month);
        //获取今年是第几个月，打印结果为4
        int value = month.getValue();
        System.out.println("获取今年是第几个月:"+value);
        //获取今天是本月的第几天
        int dayOfMonth = today.getDayOfMonth();
        System.out.println("获取今天是本月的第几天:"+dayOfMonth);
        //获取本月有多少天
        int lengthOfMonth = today.lengthOfMonth();
        System.out.println("获取本月有多少天:"+lengthOfMonth);

        //————————————————————————对周的操作————————————————————————————
        System.out.println("对周的操作————————————————————————————");
        //获取本周的日期，打印结果为SUNDAY
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        System.out.println("获取本周的日期:"+dayOfWeek);
        //获取本周第几天
        int value1 = dayOfWeek.getValue();
        System.out.println("获取本周第几天:"+value1);

        //————————————————————————对天的操作————————————————————————————
        System.out.println("对天的操作————————————————————————————");
        //获取今年的第几天，今天是2022年第100天，打印结果为100
        int dayOfYear = today.getDayOfYear();
        System.out.println("获取今年的第几天:"+dayOfYear);

        //————————————————————————通过枚举类get获取值————————————————————————————
        System.out.println("通过枚举类get获取值对年操作————————————————————");
        //java.time.LocalDate.get0  get0
        int year2 = today.get(ChronoField.YEAR);
        System.out.println("ChronoField.YEAR获取今年的年份:"+year2);

        int yearOfEra = today.get(ChronoField.YEAR_OF_ERA);
        System.out.println("ChronoField.YEAR_OF_ERA获取今年的年份:"+yearOfEra);

        int era = today.get(ChronoField.ERA);
        System.out.println("ChronoField.ERA获取今年的年份是否是真的年份1-真，0-假:"+era);

        System.out.println("通过枚举类get获取值对月操作————————————————————");
        int monthOfYear = today.get(ChronoField.MONTH_OF_YEAR);
        System.out.println("ChronoField.MONTH_OF_YEAR 获取今年是第几个月:"+monthOfYear);

        // Invalid field 'ProlepticMonth' for get() method, use getLong() instead
        // int prolepticMonth = today.get(ChronoField.PROLEPTIC_MONTH);
        // System.out.println("ChronoField.PROLEPTIC_MONTH获取本月份名称:"+prolepticMonth);

        System.out.println("通过枚举类get获取值对周操作————————————————————");
        int alignedWeekOfYear = today.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        System.out.println("ChronoField.ALIGNED_WEEK_OF_YEAR 本年第几周:"+alignedWeekOfYear);

        int alignedWeekOfMonth = today.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
        System.out.println("ChronoField.ALIGNED_WEEK_OF_MONTH 本月第几周:"+alignedWeekOfMonth);

        System.out.println("通过枚举类get获取值对天操作————————————————————");
        // Invalid field 'EpochDay' for get() method, use getLong() instead
        // int epochDay = today.get(ChronoField.EPOCH_DAY);
        // System.out.println("ChronoField.EPOCH_DAY 本年第几周:"+epochDay);

        int dayOfYear2 = today.get(ChronoField.DAY_OF_YEAR);
        System.out.println("ChronoField.DAY_OF_YEAR 本年第几天:"+dayOfYear2);

        int dayOfMonth2 = today.get(ChronoField.DAY_OF_MONTH);
        System.out.println("ChronoField.DAY_OF_MONTH 本月第几天:"+dayOfMonth2);

        int alignedDayOfWeekInYear = today.get(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);
        System.out.println("ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR 本周第几天:"+alignedDayOfWeekInYear);

        int alignedDayOfWeekInMonth = today.get(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        System.out.println("ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH 本月第几天:"+alignedDayOfWeekInMonth);

        System.out.println("with对时间进行调整————————————————————");
        //获取2034年的4月10日
        LocalDate localDate1 = today.withYear(2034);
        System.out.println("指定某年的该天:"+localDate1);
        //获取2034年的4月10日的第二种写法
        LocalDate localDate2 = today.with(ChronoField.YEAR, 2034);
        System.out.println("指定某年的该天:"+localDate2);
        //获取本周周一
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        System.out.println("获取本周周一:"+monday);

    }

    /**
     * TemporalAdjusters这个工具类
     */
    @Test
    public void testWithTemporalAdjusters(){
        LocalDate today = LocalDate.now();
        //本月第一天
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("本月第一天:"+firstDayOfMonth);
        //本月最后一天
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月最后一天:"+lastDayOfMonth);
        //下月第一天
        LocalDate firstDayOfNextMonth = today.with(TemporalAdjusters.firstDayOfNextMonth());
        System.out.println("下月第一天:"+firstDayOfNextMonth);

        //本年第一天
        LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        System.out.println("本年第一天:"+firstDayOfYear);
        //本年最后一天
        LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
        System.out.println("本年最后一天:"+lastDayOfYear);
        //下年第一天
        LocalDate firstDayOfNextYear = today.with(TemporalAdjusters.firstDayOfNextYear());
        System.out.println("下年第一天:"+firstDayOfNextYear);

        //创建一个新的日期，值为同一个月中某周的第几天
        LocalDate dayOfWeekInMonth = today.with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期，值为同一个月中某周的第几天:"+dayOfWeekInMonth);

        //创建一个新的日期,值为在本月中，第一个符合星期几的值
        LocalDate firstInMonth = today.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期,值为在本月中，第一个符合星期几的值:"+firstInMonth);

        //创建一个新的日期,值为本月中，最后一个符合星期几的值
        LocalDate lastInMonth = today.with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期,值为本月中，最后一个符合星期几的值:"+lastInMonth);

        //创建一个新的日期,并将其设定为第一个符合调整后符合星期几的值
        LocalDate next = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期,并将其设定位第一个符合调整后符合星期几的值:"+next);

        //创建一个新的日期,并将其设定为第一个符合调整前符合星期几的值
        LocalDate previous = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期,并将其设定位第一个符合调整前符合星期几的值:"+previous);

        //创建一个新的日期,并将其设定为第一个符合调整前符合星期几的值
        LocalDate previousOrSame = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        System.out.println("创建一个新的日期,并将其设定位第一个符合调整前符合星期几的值:"+previousOrSame);
    }

    @Test
    public void testLocalDate(){
        //打印结果为2022-04-10
        LocalDate today = LocalDate.of(2022, 4, 10);
        System.out.println("当前时间:"+today);
        //十年之后，打印结果为2032-04-10
        LocalDate localDate = today.plusYears(10);
        System.out.println("当前时间十年之后:"+localDate);
        //一天之前，打印结果为2022-04-09
        LocalDate localDate1 = today.minusDays(1);
        System.out.println("当前一天之前:"+localDate1);
        //一个月之后，打印结果为2022-05-10
        LocalDate localDate2 = today.plusMonths(1);
        System.out.println("当前一个月之后:"+localDate2);

        //指定格式类型
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //打印结果为 20220410
        String format = today.format(dateTimeFormatter);
        System.out.println("格式化后日期:"+format);

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate3 = localDateTime.toLocalDate();
        System.out.println("LocalDateTime 转 LocalDate:"+localDate3);
    }

    /**
     * LocalDateTime 转 Date
     * ZoneId/ZoneOffset：表示时区
     * ZoneId/ZoneOffset：表示时区
     * Instant：表示时刻，不直接对应年月日信息，需要通过时区转换
     */
    @Test
    public void change(){
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        System.out.println("当前日期:"+localDate);
        LocalTime localTime = now.toLocalTime();
        System.out.println("当前时间:"+localTime);
        //获取系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        //时区的日期和时间
        ZonedDateTime zonedDateTime = now.atZone(zoneId);
        //获取时刻
        Date date = Date.from(zonedDateTime.toInstant());
        System.out.println("格式化前：localDateTime:" + now + "  Date:" + date);
        //格式化LocalDateTime、Date
        DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("格式化后：localDateTime:" + localDateTimeFormat.format(now) + "  Date:" + dateFormat.format(date));

        //LocalDate localDate1 = LocalDate.now();
        LocalDate localDate1 = LocalDate.of(2022, 12,12);
        LocalDateTime localDateTime = localDate1.atStartOfDay();
        System.out.println("localDateTime:"+localDateTime);
        LocalDateTime localDateTime1 = localDate1.atTime(8, 20, 33);
        System.out.println("localDateTime1:"+localDateTime1);
        LocalDateTime localDateTime2 = localDate1.atTime(LocalTime.now());
        System.out.println("localDateTime2:"+localDateTime2);

        Date date1 = new Date();
        LocalDateTime localDateTime3 = date1.toInstant().atZone(zoneId).toLocalDateTime();
        System.out.println("localDateTime3:"+localDateTime3);

        LocalDate localDate2 = date1.toInstant().atZone(zoneId).toLocalDate();
        System.out.println("localDate2:"+localDate2);
    }
}
