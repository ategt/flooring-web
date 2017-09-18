package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

//@ControllerAdvice
public class ModelBinders {

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";

    //@ModelAttribute("buildOrderSearchRequest")
    public OrderSearchRequest buildOrderSearchRequest(
            @RequestParam(name = "searchBy", required = false) String searchBy,
            @RequestParam Map<String, String> params,
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "searchText", required = false) String searchText,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response,
            HttpServletRequest request,
            Map model
    ) {

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();

        if (Objects.nonNull(searchText)) {
            orderSearchRequest.setSearchText(searchText);
        }

        if (Objects.nonNull(searchBy)) {
            OrderSearchByOptionEnum orderSearchByOptionEnum = OrderSearchByOptionEnum.parse(searchBy);
            orderSearchRequest.setSearchBy(orderSearchByOptionEnum);
        }

        return orderSearchRequest;
    }
}