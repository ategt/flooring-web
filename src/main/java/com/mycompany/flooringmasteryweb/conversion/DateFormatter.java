package com.mycompany.flooringmasteryweb.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public Date parse(final String s, final Locale locale) throws ParseException {
        return createDateFormat(locale).parse(s);
    }

    @Override
    public String print(final Date date, final Locale locale) {
        return createDateFormat(locale).format(date);
    }

    private SimpleDateFormat createDateFormat(final Locale locale){
        final String format = this.messageSource.getMessage("date.format", null, locale);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setLenient(false);

        return simpleDateFormat;
    }
}
