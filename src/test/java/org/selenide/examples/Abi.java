package org.selenide.examples;

import org.testcontainers.utility.DockerImageName;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byTagAndText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Abi {
  public static DockerImageName chromeImage() {
    return isArmArchitecture() ?
      DockerImageName.parse("seleniarm/standalone-chromium")
        .asCompatibleSubstituteFor("selenium/standalone-chrome") :
      DockerImageName.parse("selenium/standalone-chrome");
  }

  public static DockerImageName firefoxImage() {
    return isArmArchitecture() ?
      DockerImageName.parse("seleniarm/standalone-firefox")
        .asCompatibleSubstituteFor("selenium/standalone-firefox") :
      DockerImageName.parse("selenium/standalone-firefox");
  }

  private static boolean isArmArchitecture() {
    return System.getProperty("os.arch").equals("aarch64");
  }

  public static void showUsersByTag(String tag, int expectedMinimumUsersCount) {
    $("#user-tags").$(byTagAndText("a", tag)).click();
    $$("#selenide-users .user").filter(visible)
      .shouldHave(sizeGreaterThanOrEqual(expectedMinimumUsersCount));
  }
}
