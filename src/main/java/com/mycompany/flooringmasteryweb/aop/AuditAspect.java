/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.AuditDao;
import com.mycompany.flooringmasteryweb.dto.Audit;
import com.mycompany.flooringmasteryweb.dto.Order;
import javax.inject.Inject;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author apprentice
 */
public class AuditAspect {

    private ApplicationContext ctx;

    @Inject
    public AuditAspect(ApplicationContextProvider applicationContext) {
        ctx = ApplicationContextProvider.getApplicationContext();
    }

    private Order processJoinPoint(JoinPoint jp) {

        Object[] args = jp.getArgs();
        Order order = null;
        if (args[0] instanceof Order) {
            order = (Order) args[0];
        }

        if (args.length > 0) {
            System.out.print("Arguments passed: ");
            for (int i = 0; i < args.length; i++) {
                System.out.print("Arg" + (i + 1) + ":" + args[i]);
            }
        }

        return order;
    }

    public void createAuditEntry(JoinPoint jp) throws Throwable {

        Order order = processJoinPoint(jp);

        if (order != null) {
            String actionName = jp.getSignature().getName();
            Audit audit = buildAuditObject(order, actionName);
            AuditDao auditDao = ctx.getBean("auditDao", AuditDao.class);
            auditDao.create(audit);
        }
    }

    private Audit buildAuditObject(Order order, String actionName) {

        Audit audit = new Audit();

        audit.setDate(order.getDate());
        audit.setOrderid(order.getId());
        audit.setOrderTotal(order.getTotal());
        audit.setOrderName(order.getName());
        
        audit.setActionPerformed(actionName);
        audit.setLogDate(new java.util.Date());
        return audit;
    }

    public ApplicationContext getCtx() {
        return ctx;
    }

    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }
}