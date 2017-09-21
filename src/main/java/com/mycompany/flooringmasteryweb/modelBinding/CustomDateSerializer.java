package com.mycompany.flooringmasteryweb.modelBinding;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
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
