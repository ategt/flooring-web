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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class ResultSegmentResolver implements HandlerMethodArgumentResolver, ApplicationContextAware{

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";
    private ApplicationContext applicationContext;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(ResultSegment.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        String acceptHeader = nativeWebRequest.getHeader("Accept");

        nativeWebRequest.

        Integer defaultResultsPerPageCount=null;
        if (acceptHeader.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)){
            defaultResultsPerPageCount = ControllerUtilities.loadAllResults(applicationContext, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadAllPageNumber(ctx, page);
        }
        Map<String, String[]> params;

        if (methodParameter.getParameterType().equals(OrderSearchRequest.class)) {

            params = nativeWebRequest.getParameterMap();

            if (params.containsKey("searchText")) {
                String searchText = params.get("searchText")[0];
                orderSearchRequest.setSearchText(searchText);
            }
        }
    }

        private ResultSegment<OrderSortByEnum> processResultPropertiesWithContextDefaults(
                Integer resultsPerPage,
                Integer resultsPerPageCookie,
                Integer page,
                String sortBy,
                HttpServletResponse response,
                String sortCookie
    ) throws BeansException {

            resultsPerPage = ControllerUtilities.loadDefaultResults(ctx, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadDefaultPageNumber(ctx, page);
            ResultSegment<OrderSortByEnum> resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
            return resultProperties;
        }

        private ResultSegment<OrderSortByEnum> processResultPropertiesWithAllAsDefault(
                String sortBy,
                HttpServletResponse response,
                String sortCookie,
                Integer page,
                Integer resultsPerPage,
                Integer resultsPerPageCookie) {

            resultsPerPage = ControllerUtilities.loadAllResults(ctx, resultsPerPage, resultsPerPageCookie);
            page = ControllerUtilities.loadAllPageNumber(ctx, page);
            ResultSegment<OrderSortByEnum> resultProperties = processResultProperties(sortBy, response, sortCookie, page, resultsPerPage);
            return resultProperties;
        }

        private ResultSegment<OrderSortByEnum> processResultProperties(String sortBy, HttpServletResponse response, String sortCookie, Integer page, Integer resultsPerPage) {
            OrderSortByEnum sortEnum = updateSortEnum(sortBy, response, sortCookie);

            ResultSegment<OrderSortByEnum> resultProperties = new OrderResultSegment(sortEnum, page, resultsPerPage);

            ControllerUtilities.updateResultsCookie(resultProperties.getResultsPerPage(), RESULTS_COOKIE_NAME, response);
            return resultProperties;
        }

    private OrderSortByEnum updateSortEnum(String sortBy, HttpServletResponse response, String sortCookie) {
        sortBy = checkForReverseRequest(sortBy, sortCookie);

        if (sortBy != null) {
            response.addCookie(new Cookie(SORT_COOKIE_NAME, sortBy));
        } else if (sortCookie != null) {
            sortBy = sortCookie;
        }

        return OrderSortByEnum.parse(sortBy);
    }

    private static String checkForReverseRequest(String sortBy, String sortCookie) {
        if (Objects.nonNull(sortBy)) {
            OrderSortByEnum sortOld = OrderSortByEnum.parse(sortCookie);
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
