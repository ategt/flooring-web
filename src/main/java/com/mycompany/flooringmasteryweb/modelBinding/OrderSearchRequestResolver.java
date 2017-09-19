package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class OrderSearchRequestResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(OrderSearchRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();

        Map<String, String[]> params;

        if (methodParameter.getParameterType().equals(OrderSearchRequest.class)){

            params = nativeWebRequest.getParameterMap();

            if (params.containsKey("searchText")){
                String searchText = params.get("searchText")[0];
                orderSearchRequest.setSearchText(searchText);
            }

            if (params.containsKey("searchBy")){
                String searchBy = params.get("searchBy")[0];

                OrderSearchByOptionEnum orderSearchByOptionEnum = OrderSearchByOptionEnum.parse(searchBy);
                orderSearchRequest.setSearchBy(orderSearchByOptionEnum);
            }
        }

        return orderSearchRequest;
    }
}
