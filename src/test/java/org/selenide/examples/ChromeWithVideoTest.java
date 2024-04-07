package org.selenide.examples;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.selenide.examples.Abi.chromeImage;
import static org.selenide.examples.Abi.showUsersByTag;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
public class SearchTestWithDocker {

  @Container
  public BrowserWebDriverContainer chrome =
    new BrowserWebDriverContainer(chromeImage())
      .withRecordingMode(RECORD_ALL, new File("build"))
      .withCapabilities(new ChromeOptions());

  @BeforeEach
  public void setUp() {
    RemoteWebDriver driver = chrome.getWebDriver();
    WebDriverRunner.setWebDriver(driver);
  }

  @Test
  public void showSelenideUsers() {
    open("https://selenide.org/users.html");
    $("h3").shouldHave(text("Selenide users"));
    $$("#user-tags .tag").shouldHave(sizeGreaterThan(10));

    showUsersByTag("usa", 20);
    showUsersByTag("europe", 16);
    showUsersByTag("estonia", 14);
    showUsersByTag("ukraine", 6);

    sleep(1000);
  }
}
