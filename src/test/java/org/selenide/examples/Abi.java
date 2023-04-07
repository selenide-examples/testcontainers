package org.selenide.examples;

import org.testcontainers.utility.DockerImageName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class Abi {
  @Nonnull
  @CheckReturnValue
  public static DockerImageName chromeImage() {
    return isArmArchitecture() ?
      DockerImageName.parse("seleniarm/standalone-chromium")
        .asCompatibleSubstituteFor("selenium/standalone-chrome") :
      DockerImageName.parse("selenium/standalone-chrome");
  }

  @Nonnull
  @CheckReturnValue
  public static DockerImageName firefoxImage() {
    return isArmArchitecture() ?
      DockerImageName.parse("seleniarm/standalone-firefox")
        .asCompatibleSubstituteFor("selenium/standalone-firefox") :
      DockerImageName.parse("selenium/standalone-firefox");
  }

  private static boolean isArmArchitecture() {
    return System.getProperty("os.arch").equals("aarch64");
  }

}
