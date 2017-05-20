/*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.TimingDao;
import com.mycompany.flooringmasteryweb.dto.Timing;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/timing")
public class TimingController {

    @Autowired
    TimingDao timingDao;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getLast(Map model) {
        model.put("timing", timingDao.getLast());
        return "timing\\show";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/", headers = "Accept=application/json")
    @ResponseBody
    public Timing getLast() {
        return timingDao.getLast();       
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public String show(@PathVariable("id") Integer timingId, Map model) {
        model.put("timing", timingDao.get(timingId));
        return "timing\\show";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Timing show(@PathVariable("id") Integer timingId) {
        return timingDao.get(timingId);        
    }
}
