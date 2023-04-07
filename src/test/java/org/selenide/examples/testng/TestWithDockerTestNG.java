package org.selenide.examples.testng;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

public class TestWithDockerTestNG {
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
