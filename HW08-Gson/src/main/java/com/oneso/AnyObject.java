package com.oneso;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("all")
public class AnyObject {

  public int anyInt = 10;
  private double anyDouble = 100.1;

  public Integer anyInteger = 50;
  private Float anyFloat = 50.5f;

  public String anyText = "Hello! Im text";

  public List<String> listStr = Arrays.asList("q", "w", "e");
  public int[] arrayInt = {1, 2, 3};

  public boolean aBoolean = true;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AnyObject anyObject = (AnyObject) o;
    return anyInt == anyObject.anyInt &&
        Double.compare(anyObject.anyDouble, anyDouble) == 0 &&
        aBoolean == anyObject.aBoolean &&
        Objects.equals(anyInteger, anyObject.anyInteger) &&
        Objects.equals(anyFloat, anyObject.anyFloat) &&
        Objects.equals(anyText, anyObject.anyText) &&
        Objects.equals(listStr, anyObject.listStr) &&
        Arrays.equals(arrayInt, anyObject.arrayInt);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(anyInt, anyDouble, anyInteger, anyFloat, anyText, listStr, aBoolean);
    result = 31 * result + Arrays.hashCode(arrayInt);
    return result;
  }
}
