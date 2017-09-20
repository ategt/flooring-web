package com.mycompany.flooringmasteryweb.modelBinding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.FromStringDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends FromStringDeserializer<Date> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public CustomDateSerializer(){
        this(null);
    }

    public CustomDateSerializer(Class<?> vc){
        super(vc);
    }

    @Override
    protected Date _deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try{
            return simpleDateFormat.parse(s);
        } catch (ParseException ex){
            throw new RuntimeException(ex);
        }
    }
}
