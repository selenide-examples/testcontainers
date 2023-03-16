package org.selenide.examples;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class SearchTestWithDockerNoVideo {
  @Rule
  public BrowserWebDriverContainer chrome =
      new BrowserWebDriverContainer()
          .withCapabilities(new ChromeOptions());

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
    open("https://duckduckgo.com/");
    $(By.name("q")).val("Selenide").pressEnter();
    $$(".results .result").shouldHave(sizeGreaterThan(3));

    for (int i = 0; i < 3; i++) {
      SelenideElement link = $(".results .result", i).find("a");
      System.out.println(link.attr("href"));
      link.click();
      back();
    }
    sleep(1000);
  }
}
