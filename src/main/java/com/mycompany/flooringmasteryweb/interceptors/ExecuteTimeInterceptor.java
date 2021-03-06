package com.mycompany.flooringmasteryweb.interceptors;

import com.mycompany.flooringmasteryweb.dao.TimingDao;
import com.mycompany.flooringmasteryweb.dto.Timing;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ExecuteTimeInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler)
            throws Exception {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    //after the handler is executed
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
            throws Exception {

        long startTime = (Long) request.getAttribute("startTime");

        long endTime = System.currentTimeMillis();

        long executeTime = endTime - startTime;

        Timing timing = buildTiming(handler, request, startTime, endTime, executeTime);

        if (Objects.nonNull(modelAndView))
            modelAndView.addObject("executionTiming", timing);

        TimingDao timingDao = applicationContext.getBean("timingDao", TimingDao.class);

        if (!timing.getInvokingClassName().toLowerCase().contains("timing"))
            timingDao.create(timing);

    }

    public Timing buildTiming(Object handler, HttpServletRequest request, long startTime, long endTime, long executeTime) {
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setStopTime(endTime);
        timing.setDifferenceTime(executeTime);
        timing.setInvokingClassName(handler.toString());
        timing.setInvokingMethodName(request.getMethod());
        return timing;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}