package com.mycompany.flooringmasteryweb.modelBinding;

import com.mycompany.flooringmasteryweb.dto.OrderSearchByOptionEnum;
import com.mycompany.flooringmasteryweb.dto.OrderSearchRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderSearchRequestWebArgumentResolver implements WebArgumentResolver, HttpMessageConverter {
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  NativeWebRequest nativeWebRequest) throws Exception {

        OrderSearchRequest orderSearchRequest = new OrderSearchRequest();

        Map<String, String[]> params;

        if (methodParameter.getParameterType().equals(OrderSearchRequest.class)) {

            params = nativeWebRequest.getParameterMap();

            System.out.println(params.size());

            if (params.containsKey("searchText")) {
                String searchText = params.get("searchText")[0];
                orderSearchRequest.setSearchText(searchText);
            }

            if (params.containsKey("searchBy")) {
                String searchBy = params.get("searchBy")[0];

                OrderSearchByOptionEnum orderSearchByOptionEnum = OrderSearchByOptionEnum.parse(searchBy);
                orderSearchRequest.setSearchBy(orderSearchByOptionEnum);
            }
        }

        return orderSearchRequest;
    }

    @Override
    public boolean canRead(Class aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class aClass, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN});
    }

    @Override
    public Object read(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        throw new HttpMessageNotWritableException("asdf");
    }
}
