package com.oneso;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("unchecked")
public class MyGson {

  private static final List<Class<?>> number = Arrays.asList(Byte.class, Short.class, Integer.class, Long.class);
  private static final List<Class<?>> numberWithPoint = Arrays.asList(Float.class, Double.class);

  public String toJson(Object obj) {
    if (obj == null) {
      return null;
    }

    try {
      return init(obj);
    } catch (IllegalAccessException ex) {
      throw new ExceptionInInitializerError("Json hasn't made out");
    }
  }

  private String init(Object obj) throws IllegalAccessException {
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    JsonObjectBuilder builder = Json.createObjectBuilder();

    String checkValue = checkObj(obj, clazz);
    if(!checkValue.isEmpty()) {
      return checkValue;
    }


    for (Field field : fields) {
      field.setAccessible(true);
      var name = field.getName();
      var type = field.getType();

      if (field.get(obj) != null) {
        if (type.isArray()) {
          builder.add(name, addArrayPrimitive(field.get(obj)));
        } else if (Collection.class.isAssignableFrom(type)) {
          builder.add(name, addCollections((Collection<Object>) field.get(obj)));
        } else {
          builder.add(name, toJsonValue(field.get(obj), type));
        }
      }
    }
    return builder.build().toString();
  }

  private JsonValue toJsonValue(Object obj, Class<?> type) {
    if(type.isPrimitive()) {
      if (byte.class.equals(type)) {
        return Json.createValue((byte) obj);
      } else if (short.class.equals(type)) {
        return Json.createValue((short) obj);
      } else if (int.class.equals(type)) {
        return Json.createValue((int) obj);
      } else if (long.class.equals(type)) {
        return Json.createValue((long) obj);
      } else if (float.class.equals(type)) {
        return Json.createValue((float) obj);
      } else if (double.class.equals(type)) {
        return Json.createValue((double) obj);
      } else if (char.class.equals(type)) {
        return Json.createValue((char) obj);
      } else if (boolean.class.equals(type)) {
        return (boolean) obj ? JsonValue.TRUE : JsonValue.FALSE;
      } else {
        return JsonValue.NULL;
      }
    } else if(number.contains(type)) {
      return Json.createValue(Long.parseLong(obj.toString()));
    } else if(numberWithPoint.contains(type)) {
      return Json.createValue(Double.parseDouble(obj.toString()));
    } else if(String.class.equals(type) || Character.class.equals(type)) {
      return Json.createValue(obj.toString());
    }
    return JsonValue.NULL;
  }

  private String checkObj(Object obj, Class<?> clazz) {
    if(clazz.isArray()) {
      return addArrayPrimitive(obj).build().toString();
    } else if(Collection.class.isAssignableFrom(clazz)) {
      return addCollections((Collection<Object>) obj).build().toString();
    } else {
      JsonValue value = toJsonValue(obj, clazz);
      return value != JsonValue.NULL ? value.toString() : "";
    }
  }

  private JsonArrayBuilder addArrayPrimitive(Object value) {
    Object[] array = new Object[Array.getLength(value)];
    for (int i = 0; i < array.length; i++)
      array[i] = Array.get(value, i);
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Object temp : array) {
      arrayBuilder.add(toJsonValue(temp, temp.getClass()));
    }
    return arrayBuilder;
  }

  private JsonArrayBuilder addCollections(Collection<Object> value) {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for(Object temp : value) {
      arrayBuilder.add(toJsonValue(temp, temp.getClass()));
    }
    return arrayBuilder;
  }
}
