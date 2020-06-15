package com.oneso;

import com.google.gson.Gson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MyGson should be")
public class MyGsonTest {

  private static final Gson gson = new Gson();
  private static final MyGson myGson = new MyGson();

  private static Stream<Object> generateData() {
    return Stream.of((byte) 1, (short) 2f, 3, 4L, 5f, 6d, "aaa", 'b', new int[]{7, 8, 9}, List.of(10, 11, 12), Collections.singletonList(13));
  }

  @DisplayName("should be same")
  @ParameterizedTest
  @MethodSource("generateData")
  void toJson(Object arg) {
    assertEquals(gson.toJson(arg), myGson.toJson(arg));
  }
}
