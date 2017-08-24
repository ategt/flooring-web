/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.aop;

import com.mycompany.flooringmasteryweb.dao.TimingDao;
import com.mycompany.flooringmasteryweb.dto.Timing;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ControllerAdvice;

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

        Timing timing = timingBuilder(stopTime, startTime, jp);

        TimingDao timingDao = ctx.getBean("timingDao", TimingDao.class);

        timingDao.create(timing);
        
        return result;
    }

    private Timing timingBuilder(long stopTime, long startTime, ProceedingJoinPoint jp) {
        long differenctTime = stopTime - startTime;
        Signature signature = jp.getSignature();
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setStopTime(stopTime);
        timing.setDifferenceTime(differenctTime);
        timing.setInvokingClassName(jp.getTarget().toString());
        timing.setInvokingMethodName(signature.getName());
        timing.setModifiers(signature.getModifiers());
        return timing;
    }
}
