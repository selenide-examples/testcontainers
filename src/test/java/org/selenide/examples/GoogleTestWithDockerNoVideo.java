package org.selenide.examples;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;

public class GoogleTestWithDockerNoVideo {
  @Rule
  public BrowserWebDriverContainer chrome =
      new BrowserWebDriverContainer()
          .withCapabilities(DesiredCapabilities.chrome());

  @Before
  public void setUp() {
    RemoteWebDriver driver = chrome.getWebDriver();
    WebDriverRunner.setWebDriver(driver);
  }

  @After
  public void tearDown() {
    WebDriverRunner.closeWebDriver();
  }

  @Test
  public void search() {
    open("https://google.com/ncr");
    $(By.name("q")).val("Selenide").pressEnter();
    $$("#res .g").shouldHave(sizeGreaterThan(5));

    for (int i = 0; i < 5; i++) {
      SelenideElement link = $("#res .g", i).find("a");
      System.out.println(link.attr("href"));
      link.click();
      back();
    }
    sleep(1000);
  }
}
