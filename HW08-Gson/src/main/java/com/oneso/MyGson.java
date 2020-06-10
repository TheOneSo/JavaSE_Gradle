package com.oneso;

import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("unchecked")
public class MyGson {

  private static final List<Class<?>> number = Arrays.asList(Byte.class, Short.class, Integer.class, Long.class);
  private static final List<Class<?>> numberWithPoint = Arrays.asList(Float.class, Double.class);

  public String toJson(Object obj) throws IllegalAccessException {
    if(obj == null) {
      return null;
    }
    JsonObject jsonObject = init(obj);

    return jsonObject.toString();
  }

  private JsonObject init(Object obj) throws IllegalAccessException {
    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    JsonObjectBuilder builder = Json.createObjectBuilder();

    for (Field field : fields) {
      field.setAccessible(true);
      var name = field.getName();
      var type = field.getType();

      if (field.get(obj) != null) {
        if (type.isPrimitive()) {
          addPrimitive(field.get(obj), name, type, builder);
        } else if (type.isArray()) {
          addArrayPrimitive(field.get(obj), name, type, builder);
        } else if (Arrays.asList(type.getInterfaces()).contains(Collection.class)) {
          addCollections((List<Object>) field.get(obj), name, type, builder);
        } else if (number.contains(type)) {
          builder.add(name, Long.parseLong(field.get(obj).toString()));
        } else if (numberWithPoint.contains(type)) {
          builder.add(name, Double.parseDouble(field.get(obj).toString()));
        } else if (String.class.equals(type)) {
          builder.add(name, field.get(obj).toString());
        } else if (Boolean.class.equals(type)) {
          builder.add(name, (boolean) field.get(obj));
        }
      }
    }
    return builder.build();
  }

  private void addPrimitive(Object value, String name, Class<?> type, JsonObjectBuilder builder) {
    switch (type.getSimpleName().toLowerCase()) {
      case "byte":
        builder.add(name, (byte) value);
        break;
      case "short":
        builder.add(name, (short) value);
        break;
      case "int":
        builder.add(name, (int) value);
        break;
      case "long":
        builder.add(name, (long) value);
        break;
      case "float":
        builder.add(name, (float) value);
        break;
      case "double":
        builder.add(name, (double) value);
        break;
      case "char":
        builder.add(name, (char) value);
        break;
      case "boolean":
        builder.add(name, (boolean) value);
        break;
      default:
        throw new NoSuchElementException();
    }
  }

  private void addArrayPrimitive(Object value, String name, Class<?> type, JsonObjectBuilder builder) {
    Object[] array = new Object[Array.getLength(value)];
    for (int i = 0; i < array.length; i++)
      array[i] = Array.get(value, i);
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Object temp : array) {
      switch (type.getSimpleName().toLowerCase().replace("[]", "")) {
        case "byte":
          arrayBuilder.add((byte) temp);
          break;
        case "short":
          arrayBuilder.add((short) temp);
          break;
        case "int":
          arrayBuilder.add((int) temp);
          break;
        case "long":
          arrayBuilder.add((long) temp);
          break;
        case "float":
          arrayBuilder.add((float) temp);
          break;
        case "double":
          arrayBuilder.add((double) temp);
          break;
        case "char":
          arrayBuilder.add((char) temp);
          break;
        case "boolean":
          arrayBuilder.add((boolean) temp);
          break;
        default:
          throw new NoSuchElementException();
      }
    }
    builder.add(name, arrayBuilder);
  }

  private void addCollections(List<Object> value, String name, Class<?> type, JsonObjectBuilder builder) {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    if (number.contains(type)) {
      for (Object temp : value) {
        arrayBuilder.add(Long.parseLong(temp.toString()));
      }
    } else if (numberWithPoint.contains(type)) {
      for (Object temp : value) {
        arrayBuilder.add(Double.parseDouble(temp.toString()));
      }
    } else {
      for (Object temp : value) {
        arrayBuilder.add(temp.toString());
      }
    }
    builder.add(name, arrayBuilder);
  }
}
