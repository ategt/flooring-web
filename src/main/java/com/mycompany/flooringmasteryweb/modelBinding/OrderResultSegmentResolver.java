package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderResultSegment;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import com.mycompany.flooringmasteryweb.dto.OrderSortByEnum;
import com.mycompany.flooringmasteryweb.dto.ResultSegment;
import com.mycompany.flooringmasteryweb.utilities.ControllerUtilities;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class OrderResultSegmentResolver implements HandlerMethodArgumentResolver, ApplicationContextAware {

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";
    private ApplicationContext applicationContext;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(OrderResultSegment.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        String acceptHeader = nativeWebRequest.getHeader("Accept");

        Object nativeRequest = nativeWebRequest.getNativeRequest();
        Object nativeResponse = nativeWebRequest.getNativeResponse();

        HttpServletRequest httpServletRequest = (HttpServletRequest) nativeRequest;
        HttpServletResponse response = (HttpServletResponse) nativeResponse;

        Cookie resultsPerPageCookie = null;
        Cookie sortByCookie = null;

        Cookie[] cookies = httpServletRequest.getCookies();

        if (Objects.nonNull(cookies))
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), SORT_COOKIE_NAME)) {
                    sortByCookie = cookie;
                }

                if (Objects.equals(cookie.getName(), RESULTS_COOKIE_NAME)) {
                    resultsPerPageCookie = cookie;
                }
            }

        String pageString = nativeWebRequest.getParameter("page");
        String resultsPerPageString = nativeWebRequest.getParameter("results");
        String sortByString = nativeWebRequest.getParameter("sort_by");

        Integer page = null;
        try {
            page = Integer.parseInt(pageString);
        } catch (NumberFormatException ex) {
        }

        Integer resultsPerPage = null;
        try {
            resultsPerPage = Integer.parseInt(resultsPerPageString);
        } catch (NumberFormatException ex) {
        }

        if (Objects.nonNull(acceptHeader) && acceptHeader.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            resultsPerPage = ControllerUtilities.loadAllResults(applicationContext, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadAllPageNumber(applicationContext, page);
        } else {
            resultsPerPage = ControllerUtilities.loadDefaultResults(applicationContext, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadDefaultPageNumber(applicationContext, page);
        }

        ResultSegment<OrderSortByEnum> resultProperties
                = processResultProperties(sortByString, response, sortByCookie, page, resultsPerPage);

        return new OrderResultSegment(resultProperties);
    }

    private ResultSegment<OrderSortByEnum> processResultPropertiesWithContextDefaults(
            Integer resultsPerPage,
            Cookie resultsPerPageCookie,
            Integer page,
            String sortBy,
            HttpServletResponse response,
            Cookie sortCookie
    ) throws BeansException {

        resultsPerPage = ControllerUtilities.loadDefaultResults(applicationContext, resultsPerPage, resultsPerPageCookie);
        page = ControllerUtilities.loadDefaultPageNumber(applicationContext, page);
        ResultSegment<OrderSortByEnum> resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultSegment<OrderSortByEnum> processResultPropertiesWithAllAsDefault(
            String sortBy,
            HttpServletResponse response,
            Cookie sortCookie,
            Integer page,
            Integer resultsPerPage,
            Cookie resultsPerPageCookie) {

        resultsPerPage = ControllerUtilities.loadAllResults(applicationContext, resultsPerPage, resultsPerPageCookie);
        page = ControllerUtilities.loadAllPageNumber(applicationContext, page);
        ResultSegment<OrderSortByEnum> resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
        return resultProperties;
    }

    private ResultSegment<OrderSortByEnum> processResultProperties(String sortBy, HttpServletResponse response, Cookie sortCookie, Integer page, Integer resultsPerPage) {
        OrderSortByEnum sortEnum = updateSortEnum(sortBy, response, sortCookie);

        ResultSegment<OrderSortByEnum> resultProperties = new OrderResultSegment(sortEnum, page, resultsPerPage);

        ControllerUtilities.updateResultsCookie(resultProperties.getResultsPerPage(), RESULTS_COOKIE_NAME, response);
        return resultProperties;
    }

    private OrderSortByEnum updateSortEnum(String sortBy, HttpServletResponse response, Cookie sortCookie) {
        sortBy = checkForReverseRequest(sortBy, sortCookie);

        if (sortBy != null) {
            response.addCookie(new Cookie(SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null && sortCookie.getValue() != null) {
            sortBy = sortCookie.getValue();
        }

        return OrderSortByEnum.parse(sortBy);
    }

    private static String checkForReverseRequest(String sortBy, Cookie sortCookie) {
        if (Objects.nonNull(sortBy) && Objects.nonNull(sortCookie)) {
            OrderSortByEnum sortOld = OrderSortByEnum.parse(sortCookie.getValue());
            OrderSortByEnum sortNew = OrderSortByEnum.parse(sortBy);

            if (sortOld.equals(sortNew) || sortOld.reverse().equals(sortNew)) {
                sortBy = sortOld.reverse().toString();
            }
        }
        return sortBy;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
