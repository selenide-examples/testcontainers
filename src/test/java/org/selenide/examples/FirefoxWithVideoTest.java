package org.selenide.examples;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static org.selenide.examples.Abi.firefoxImage;
import static org.selenide.examples.Abi.showUsersByTag;
import static org.selenide.examples.CdpUrl.makeCdpAvailableOnHostMachine;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Testcontainers
public class FirefoxWithVideoTest {
  private static final Logger log = LoggerFactory.getLogger(FirefoxWithVideoTest.class);
  private static final FirefoxOptions options = new FirefoxOptions();

  @Container
  public BrowserWebDriverContainer<?> browser =
    new BrowserWebDriverContainer<>(firefoxImage())
      .withRecordingMode(RECORD_ALL, new File("build"))
      .withCapabilities(options);

  @BeforeEach
  public void setUp() {
    RemoteWebDriver driver = new RemoteWebDriver(browser.getSeleniumAddress(), options, false);
    log.info("Firefox VNS address: {}", browser.getVncAddress());
    makeCdpAvailableOnHostMachine(browser, driver);
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
