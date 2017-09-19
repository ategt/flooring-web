package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.*;
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
import java.util.Objects;

public class AddressResultSegmentResolver implements HandlerMethodArgumentResolver, ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(AddressResultSegment.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        
        Object nativeRequest = nativeWebRequest.getNativeRequest();
        HttpServletRequest httpServletRequest = (HttpServletRequest) nativeRequest;

        Object nativeResponse = nativeWebRequest.getNativeResponse();
        HttpServletResponse httpServletResponse = (HttpServletResponse) nativeResponse;

        Cookie resultsPerPageCookie = null;
        Cookie sortByCookie = null;

        Cookie[] cookies = httpServletRequest.getCookies();

        if (Objects.nonNull(cookies))
            for (Cookie cookie : cookies) {
                if (Objects.equals(cookie.getName(), ControllerUtilities.SORT_COOKIE_NAME)) {
                    sortByCookie = cookie;
                } else if (Objects.equals(cookie.getName(), ControllerUtilities.RESULTS_COOKIE_NAME)) {
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
        
        String acceptHeader = nativeWebRequest.getHeader("Accept");

        if (Objects.nonNull(acceptHeader) && acceptHeader.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            resultsPerPage = ControllerUtilities.loadAllResults(applicationContext, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadAllPageNumber(applicationContext, page);
        } else {
            resultsPerPage = ControllerUtilities.loadDefaultResults(applicationContext, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadDefaultPageNumber(applicationContext, page);
        }

        ResultSegment<AddressSortByEnum> resultProperties
                = processResultProperties(sortByString, httpServletResponse, sortByCookie, page, resultsPerPage);

        return new AddressResultSegment(resultProperties);
    }

    private ResultSegment<AddressSortByEnum> processResultProperties(String sortBy, HttpServletResponse response, Cookie sortCookie, Integer page, Integer resultsPerPage) {
        AddressSortByEnum sortEnum = updateSortEnum(sortBy, response, sortCookie);

        ResultSegment<AddressSortByEnum> resultProperties = new AddressResultSegment(sortEnum, page, resultsPerPage);

        ControllerUtilities.updateResultsCookie(resultProperties.getResultsPerPage(), ControllerUtilities.RESULTS_COOKIE_NAME, response);
        return resultProperties;
    }

    private AddressSortByEnum updateSortEnum(String sortBy, HttpServletResponse response, Cookie sortCookie) {
        sortBy = checkForReverseRequest(sortBy, sortCookie);

        if (sortBy != null) {
            response.addCookie(new Cookie(ControllerUtilities.SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null && sortCookie.getValue() != null) {
            sortBy = sortCookie.getValue();
        }

        return AddressSortByEnum.parse(sortBy);
    }

    private static String checkForReverseRequest(String sortBy, Cookie sortCookie) {
        if (Objects.nonNull(sortBy) && Objects.nonNull(sortCookie)) {
            AddressSortByEnum sortOld = AddressSortByEnum.parse(sortCookie.getValue());
            AddressSortByEnum sortNew = AddressSortByEnum.parse(sortBy);

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
