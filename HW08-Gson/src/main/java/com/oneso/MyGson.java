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

    JsonValue value = toJsonValue(obj, obj.getClass(), true);
    if(!value.equals(JsonValue.NULL)) {
      return value.toString();
    }

    return init(obj).toString();
  }

  private JsonObject init(Object obj) {
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    JsonObjectBuilder builder = Json.createObjectBuilder();

    for (Field field : fields) {
      field.setAccessible(true);
      var name = field.getName();
      var type = field.getType();

      try {
        if (field.get(obj) != null) {
          builder.add(name, toJsonValue(field.get(obj), type, false));
        }
      } catch (IllegalAccessException e) {
        throw new ExceptionInInitializerError("Couldn't get field");
      }
    }
    return builder.build();
  }

  private JsonValue toJsonValue(Object obj, Class<?> type, boolean firstObj) {
    if (type.isPrimitive()) {
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
      }
    } else if (number.contains(type)) {
      return Json.createValue(Long.parseLong(obj.toString()));
    } else if (numberWithPoint.contains(type)) {
      return Json.createValue(Double.parseDouble(obj.toString()));
    } else if (String.class.equals(type) || Character.class.equals(type)) {
      return Json.createValue(obj.toString());
    } else if (Collection.class.isAssignableFrom(type)) {
      return addCollections((Collection<Object>) obj).build();
    } else if (type.isArray()) {
      return addArrayPrimitive(obj).build();
    } else if (!firstObj) {
      return Json.createObjectBuilder(init(obj)).build();
    }
    return JsonValue.NULL;
  }

  private JsonArrayBuilder addArrayPrimitive(Object value) {
    Object[] array = new Object[Array.getLength(value)];
    for (int i = 0; i < array.length; i++)
      array[i] = Array.get(value, i);
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Object temp : array) {
      arrayBuilder.add(toJsonValue(temp, temp.getClass(), false));
    }
    return arrayBuilder;
  }

  private JsonArrayBuilder addCollections(Collection<Object> value) {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Object temp : value) {
      arrayBuilder.add(toJsonValue(temp, temp.getClass(), false));
    }
    return arrayBuilder;
  }
}
