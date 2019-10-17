package org.selenide.examples;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;

public class GoogleTestWithFirefox {
  @Rule
  public BrowserWebDriverContainer browser =
      new BrowserWebDriverContainer()
          .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"))
          .withCapabilities(DesiredCapabilities.firefox());

  @Before
  public void setUp() {
    RemoteWebDriver driver = browser.getWebDriver();
    System.out.println(browser.getVncAddress());
    WebDriverRunner.setWebDriver(driver);
  }

  @After
  public void tearDown() {
    WebDriverRunner.closeWebDriver();
  }

  @Test
  public void search() {
    open("https://google.com/ncr");
    $(By.name("q")).val("codeborne").pressEnter();
    $$("#res .g").shouldHave(sizeGreaterThan(5));
  }
}
