package com.mycompany.flooringmasteryweb.modelBinding;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mycompany.flooringmasteryweb.validation.UnparsableDateException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer extends StdDeserializer<Date> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public CustomDateDeserializer(){
        this(null);
    }

    public CustomDateDeserializer(Class<Date> vc){
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String date = jsonParser.getText();
        try{
            return simpleDateFormat.parse(date);
        } catch (ParseException ex){
            throw new RuntimeException(new UnparsableDateException(ex, date, jsonParser.getCurrentName(), simpleDateFormat.toPattern()));
        }
    }
}
