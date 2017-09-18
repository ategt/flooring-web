package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import org.springframework.core.convert.converter.Converter;

public class OrderSearchRequestConverter implements Converter<String[], OrderSearchRequest> {

    @Override
    public OrderSearchRequest convert(String[] strings) {
        return null;
    }
}
