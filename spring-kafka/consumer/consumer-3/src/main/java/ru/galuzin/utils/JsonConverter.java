package ru.galuzin.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class JsonConverter<T> {

    private static final Logger log = LoggerFactory.getLogger(JsonConverter.class);

    private final ObjectMapper mapper;
    private final Class<T> cls;
    private final ObjectReader objectReader;

    public JsonConverter(Class<T> cls) {
        mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        this.cls =cls;
        this.objectReader = mapper.readerFor(cls);
    }

    public byte[] toJson(T data) {
        try {
            Objects.requireNonNull(data, "to json input data");
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("to json fail", e);
            throw new RuntimeException(e);
        }
    }

    public T fromJson(byte[] data) {
        Objects.requireNonNull(data, "from json input data");
        Objects.requireNonNull(cls, "from json cls");
        try {
//            if (this.objectReader==null) {
//                this.objectReader = mapper.readerFor(cls);
//            }
            return objectReader.readValue(data, cls);
        } catch (Exception e) {
            log.error("from json fail", e);
            throw new RuntimeException(e);
        }
    }
}




//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.Objects;
//
//public class JsonConverter {
////    static final ThreadLocal<DateFormat> DATE_FORMAT =
////            new ThreadLocal<DateFormat>() {
////                @Override
////                protected DateFormat initialValue() {
////                    return new SimpleDateFormat("yyyy-MM-dd");
////                }
////            };
//    private static ObjectMapper mapper = new ObjectMapper();
//    {
//
//    }
//
//    public static <T> String toJson(T sdi) {
////        mapper.setDateFormat(DATE_FORMAT.get());
//        return mapper.writeValueAsString(sdi);
//    }
//
//    public static <T> T fromJson(String json, Class<T> cls) {
//        if (json == null) {
//            return null;
//        }
////        ObjectMapper mapper = new ObjectMapper();
////        mapper.setDateFormat(DATE_FORMAT.get());
//        return mapper.readValue(json, cls);
//    }
//
//    public <T> byte[] toJson(T sdi) throws Exception {
//        return mapper.writeValueAsBytes(sdi);
//    }
//
//    public <T> T fromJson(byte[] json, Class<T> cls) throws Exception {
//        Objects.requireNonNull(json, "json string");
//        mapper.readerFor(cls);
//        return mapper.readValue(json, cls);
//    }
//}
