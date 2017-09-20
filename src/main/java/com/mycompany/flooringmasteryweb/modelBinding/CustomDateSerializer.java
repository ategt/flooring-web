package com.mycompany.flooringmasteryweb.modelBinding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.FromStringDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends StdSerializer<Date> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public CustomDateSerializer(){
        this(null);
    }

    public CustomDateSerializer(Class<Date> vc){
        super(vc);
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(simpleDateFormat.format(date));
    }
}
