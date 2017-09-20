package com.mycompany.flooringmasteryweb.modelBinding;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer extends StdDeserializer<Date> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public CustomDateDeserializer(){
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public Date deserialize(org.codehaus.jackson.JsonParser jsonParser, org.codehaus.jackson.map.DeserializationContext deserializationContext) throws IOException, org.codehaus.jackson.JsonProcessingException {
        String date = jsonParser.getText();
        try{
            return simpleDateFormat.parse(date);
        } catch (ParseException ex){
            throw new RuntimeException(ex);
        }
    }
}
