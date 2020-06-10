package com.oneso;

import com.google.gson.Gson;

public class MainMyGson {

  public static void main(String[] args) throws IllegalAccessException {
    MyGson myGson = new MyGson();
    AnyObject anyObject = new AnyObject();
    String myJson = myGson.toJson(anyObject);

    System.out.println(myJson);

    Gson gson = new Gson();
    System.out.println(gson.toJson(anyObject));

    AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
    System.out.println(anyObject.equals(obj2));
  }
}
