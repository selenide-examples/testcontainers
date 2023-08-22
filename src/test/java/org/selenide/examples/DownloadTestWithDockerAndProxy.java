package org.selenide.examples;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.FileDownloadMode.HTTPGET;
import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.selenide.examples.Abi.chromeImage;
import static org.selenide.examples.Abi.showUsersByTag;

@org.testcontainers.junit.jupiter.Testcontainers
class DownloadTestWithDockerAndProxy {

  private static final int proxyPort = 8864;

  private SelenideProxyServer proxyServer;

  static {
    Testcontainers.exposeHostPorts(proxyPort);
  }

  @Container
  public BrowserWebDriverContainer chrome =
    new BrowserWebDriverContainer(chromeImage())
      .withCapabilities(new ChromeOptions().setProxy(new Proxy()
          .setSslProxy("host.testcontainers.internal:" + proxyPort))
        .setAcceptInsecureCerts(true));

  @BeforeEach
  public void setUp() {
    Configuration.proxyEnabled = true;
    Configuration.fileDownload = PROXY;

    SelenideConfig config = new SelenideConfig()
      .proxyHost("host.testcontainers.internal")
      .proxyPort(proxyPort)
      .proxyEnabled(true);
    proxyServer = new SelenideProxyServer(config, null);
    proxyServer.start();

    RemoteWebDriver driver = chrome.getWebDriver();
    WebDriverRunner.setWebDriver(driver, proxyServer);
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

  @Test
  public void canDownloadFile() throws FileNotFoundException {
    open("https://selenide.org/quick-start.html");

    File selenideJar = $(byText("selenide.jar")).download(withExtension("jar"));
    assertThat(selenideJar.getName()).matches("selenide-[\\d.]+\\.jar");
  }

  @AfterEach
  public void tearDown() {
    proxyServer.shutdown();
    WebDriverRunner.closeWebDriver();
    Configuration.fileDownload = HTTPGET;
    Configuration.proxyEnabled = false;
    Configuration.proxyHost = null;
    Configuration.proxyPort = 0;
  }
}
