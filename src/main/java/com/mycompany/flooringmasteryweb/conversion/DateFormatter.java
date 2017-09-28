package com.mycompany.flooringmasteryweb.conversion;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {
    @Override
    public Date parse(String s, Locale locale) throws ParseException {
        return createDateFormat().parse(s);
    }

    @Override
    public String print(Date date, Locale locale) {
        return createDateFormat().format(date);
    }

    private SimpleDateFormat createDateFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        simpleDateFormat.setLenient(false);

        return simpleDateFormat;
    }
}
