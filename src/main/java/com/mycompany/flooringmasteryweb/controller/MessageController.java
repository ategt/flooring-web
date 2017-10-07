/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Controller
@RequestMapping(value = "/message")
public class MessageController implements MessageSourceAware {

    private MessageSource messageSource;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{messageCode}")
    public String show(@PathVariable("messageCode") String messageCode) {
        Locale locale = LocaleContextHolder.getLocale();
        messageCode = messageCode.replace("-", ".");

        return messageSource.getMessage(messageCode, null, locale);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
