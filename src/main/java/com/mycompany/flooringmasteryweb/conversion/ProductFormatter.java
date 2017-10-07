package com.mycompany.flooringmasteryweb.conversion;

import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class ProductFormatter implements Formatter<Product> {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductDao productDao;

    @Override
    public Product parse(final String s, final Locale locale) throws ParseException {
        return productDao.get(s);
    }

    @Override
    public String print(final Product product, final Locale locale) {
        return (product != null ? product.getProductName() : this.messageSource.getMessage("product.null", null, locale));
    }
}
