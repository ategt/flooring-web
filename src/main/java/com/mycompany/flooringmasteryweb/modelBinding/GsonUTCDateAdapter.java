package com.mycompany.flooringmasteryweb.modelBinding;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GsonUTCDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final DateFormat dateFormat;
    private final DateFormat utcDateFormat;
    private final DateFormat deserializeDate;

    public GsonUTCDateAdapter(){
        utcDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        deserializeDate = new SimpleDateFormat("yyyy-MM-dd");
        deserializeDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    }

    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return utcDateFormat.parse(jsonElement.getAsString());
        } catch (ParseException ex){}

        try {
            return deserializeDate.parse(jsonElement.getAsString());
        } catch (ParseException ex){
            throw new JsonParseException(ex);
        }
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        String dateString = dateFormat.format(date);
        return new JsonPrimitive(dateString);
    }
}