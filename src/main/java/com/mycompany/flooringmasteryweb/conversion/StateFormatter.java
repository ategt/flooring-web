package com.mycompany.flooringmasteryweb.conversion;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class StateFormatter implements Formatter<State> {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private StateDao stateDao;

    @Override
    public State parse(String s, Locale locale) throws ParseException {
        return stateDao.get(s);
    }

    @Override
    public String print(State state, Locale locale) {
        return (state != null ? StateUtilities.stateFromAbbr(state.getStateName()) : this.messageSource.getMessage("state.null", null, locale));
    }
}
