package org.selenide.examples.testng;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.selenide.examples.Abi.chromeImage;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class SearchTestWithDockerTestNG {
  public BrowserWebDriverContainer chrome =
      new BrowserWebDriverContainer(chromeImage())
          .withRecordingMode(RECORD_ALL, new File("build"))
          .withCapabilities(new ChromeOptions());

  @BeforeClass
  public void startContainer() {
    chrome.start();
    WebDriverRunner.setWebDriver(chrome.getWebDriver());
  }

  @AfterClass
  public void stopContainer() {
    WebDriverRunner.closeWebDriver();
    chrome.stop();
  }

  @Test
  public void search() {
    open("https://duckduckgo.com/");
    $(By.name("q")).val("Selenide").pressEnter();
    $$("#res .g").shouldHave(sizeGreaterThan(3));

    for (int i = 0; i < 3; i++) {
      SelenideElement link = $("#res .g", i).find("a");
      System.out.println(link.attr("href"));
      link.click();
      back();
    }
    sleep(1000);
  }
}
