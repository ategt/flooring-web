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

    public void logStartTime(JoinPoint jp) {
        long timeInMills = Calendar.getInstance().getTimeInMillis();

        System.out.println("Start Time in Mills:" + timeInMills);

    }

    public void logStopTime(JoinPoint jp) {
        long timeInMills = Calendar.getInstance().getTimeInMillis();

        System.out.println("Stop Time in Mills:" + timeInMills);

    }

    public void logStartAndStopTimeForDoNothing(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Start time is " + startTime + " milliseconds.");
        System.out.println("Spring AOP: Around advice");
        Object[] args = jp.getArgs();
        if (args.length > 0) {
            System.out.print("Arguments passed: ");
            for (int i = 0; i < args.length; i++) {
                System.out.print("Arg" + (i + 1) + ":" + args[i]);
                args[i] = ":Spring AOP removed the argument";
            }
        }
        
        Object result = jp.proceed(args);
        long stopTime = System.currentTimeMillis();
        System.out.println("Stop time is " + stopTime+ " milliseconds.");
        long differenctTime = stopTime - startTime;
        System.out.println("\tThe Difference was " + differenctTime + " milliseconds.");
    }
    
    
    public void logStartAndStopTimeForEncode(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Start time is " + startTime + " milliseconds.");
        
        Object result = jp.proceed(jp.getArgs());
        //Object result = jp.proceed(args);
        long stopTime = System.currentTimeMillis();
        System.out.println("Stop time is " + stopTime+ " milliseconds.");
        long differenctTime = stopTime - startTime;
        System.out.println("\tThe Difference was " + differenctTime + " milliseconds.");
        //return result.toString() + " :Result is also modified";
    }
    
    public void logStartAndStopTimeForDecode(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Start time is " + startTime + " milliseconds.");

        Object result = jp.proceed(jp.getArgs());
        long stopTime = System.currentTimeMillis();
        System.out.println("Stop time is " + stopTime+ " milliseconds.");
        long differenctTime = stopTime - startTime;
        System.out.println("\tThe Difference was " + differenctTime + " milliseconds.");
        
    }
    
//    public Map<String, T> logStartAndStopTimeForDecode(ProceedingJoinPoint jp) throws Throwable {
//        System.out.println("Spring AOP: Around advice");
//        Object[] args = jp.getArgs();
//        if (args.length > 0) {
//            System.out.print("Arguments passed: ");
//            for (int i = 0; i < args.length; i++) {
//                System.out.print("Arg" + (i + 1) + ":" + args[i]);
//                args[i] = ":Spring AOP removed the argument";
//            }
//        }
//        Object result = jp.proceed(args);
//        return result.toString() + " :Result is also modified";
//    }

    public void logStartAndStopTimeForMethod(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        System.out.println("Start time is " + startTime + " milliseconds.");
        
        Object result = jp.proceed(jp.getArgs());
        long stopTime = System.currentTimeMillis();
        System.out.println("Stop time is " + stopTime+ " milliseconds.");
        long differenctTime = stopTime - startTime;
        System.out.println("\tThe Difference was " + differenctTime + " milliseconds.");
        
        String aString = jp.toString();
        String shortString = jp.toShortString();
        String longString = jp.toLongString();
        
        Signature signature = jp.getSignature();
        String kind = jp.getKind();
        StaticPart spart = jp.getStaticPart();
        //spart.
       
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setStopTime(stopTime);
        timing.setDifferenceTime(differenctTime);
        
        TimingDao timingDao = ctx.getBean("timingDao", TimingDao.class);
        
        timingDao.create(timing);        
    }
}
