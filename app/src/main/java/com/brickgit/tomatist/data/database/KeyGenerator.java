package com.brickgit.tomatist.data.database;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Created by Daniel Lin on 2019/3/13. */
public class KeyGenerator {

  public static String gen(String prefix) {
    return prefix
        + Hashing.sha256()
            .hashString(UUID.randomUUID().toString(), StandardCharsets.UTF_8)
            .toString();
  }
}
