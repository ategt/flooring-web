/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.TimingDao;
import com.mycompany.flooringmasteryweb.dto.Timing;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author apprentice
 */
public class TimingAspect {

    private ApplicationContext ctx;

    public TimingAspect() {
        ctx = com.mycompany.flooringmasteryweb.aop.ApplicationContextProvider.getApplicationContext();
    }

    public Object logStartAndStopTimeForMethod(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = jp.proceed(jp.getArgs());

        long stopTime = System.currentTimeMillis();
        long differenctTime = stopTime - startTime;

        Signature signature = jp.getSignature();
        
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setStopTime(stopTime);
        timing.setDifferenceTime(differenctTime);
        //timing.setInvokingClassName(signature.getDeclaringTypeName());
        timing.setInvokingClassName(jp.getTarget().toString());
        timing.setInvokingMethodName(signature.getName());
        timing.setModifiers(signature.getModifiers());

        TimingDao timingDao = ctx.getBean("timingDao", TimingDao.class);

        timingDao.create(timing);
        
        return result;
    }
}
