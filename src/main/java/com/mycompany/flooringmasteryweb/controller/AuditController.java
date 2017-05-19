/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.AuditDao;
import com.mycompany.flooringmasteryweb.dto.Audit;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ATeg
 */
@Controller
@RequestMapping(value = "/audits")
public class AuditController {

    AuditDao auditDao;

    @Inject
    public AuditController(AuditDao auditDao) {
        this.auditDao = auditDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexInHtml(Map model) {        
        return "audit\\index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public List<Audit> index(@RequestParam(name = "page_num", required = false) Integer pageNumber,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "audits_per_page", required = false) Integer itemsPerPage) {

        if (page == null) {
            page = pageNumber;
        }

        if (itemsPerPage==null)
            itemsPerPage = 50;
        
        if (page == null && itemsPerPage == null) {
            return auditDao.get();
        } else {
            return auditDao.getWithPagination(page, itemsPerPage);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Audit show(@PathVariable("id") Integer auditId) {
        return auditDao.get(auditId);
    }
}
