package fr.ibragim.e_expense.Widgets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ibragim.abubakarov on 22/06/2018.
 */

public class DateFormat {
    public static final String DATE_DASH_FORMAT = "yyyy-mm-dd";
    public static final String DATE_FORMAT = "dd/mm/yyyy";

    public static String parseYMD(String strDate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(strDate);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }


    public static String parseDMY(String date, String inputFormat, String outputFormat){
        Date initDate = null;
        try {
            initDate = new SimpleDateFormat(inputFormat).parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
            String parsedDate = formatter.format(initDate);

            return parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

}
