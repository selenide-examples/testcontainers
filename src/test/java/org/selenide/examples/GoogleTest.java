package org.selenide.examples;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

@Ignore
public class GoogleTest {
  @Test
  public void search() {
    Configuration.browser = "chrome";
    open("https://google.com/");
    $(By.name("q")).val("Selenide").pressEnter();
    $$("#ires .g").shouldHave(CollectionCondition.sizeGreaterThan(5));

    for (int i = 0; i < 5; i++) {
      SelenideElement link = $("#ires .g", i).find("a");
      System.out.println(link.attr("href"));
      link.click();
      back();
    }
    sleep(1000);
  }
}
