package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@ControllerAdvice
public class ModelBinders {

    private final String RESULTS_COOKIE_NAME = "results_cookie";
    private final String SORT_COOKIE_NAME = "sort_cookie";

    //    @InitBinder
//    public void orderSearchByInitBinder(WebDataBinder binder){
//        binder.
//    }

    @ModelAttribute("buildOrderSearchRequest")
    public OrderSearchRequest buildOrderSearchRequest(
            @RequestParam Map<String, String> params,
            @CookieValue(value = SORT_COOKIE_NAME, defaultValue = "id") String sortCookie,
            @CookieValue(value = RESULTS_COOKIE_NAME, required = false) Integer resultsPerPageCookie,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "results", required = false) Integer resultsPerPage,
            HttpServletResponse response,
            Map model
    ){
        System.out.println("This");
        return null;
    }


}