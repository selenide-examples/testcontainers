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
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
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
import static org.selenide.examples.CdpUrl.makeCdpAvailableOnHostMachine;

@Testcontainers
class DownloadWithProxyTest {

  private static final int proxyPort = 8864;

  private SelenideProxyServer proxyServer;

  static {
    org.testcontainers.Testcontainers.exposeHostPorts(proxyPort);
  }

  private static final ChromiumOptions<?> chromeOptions = new ChromeOptions()
    .setProxy(new Proxy().setSslProxy("host.testcontainers.internal:" + proxyPort))
    .setAcceptInsecureCerts(true);

  @Container
  public BrowserWebDriverContainer<?> chrome =
    new BrowserWebDriverContainer<>(chromeImage()).withCapabilities(chromeOptions);

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

    RemoteWebDriver driver = new RemoteWebDriver(chrome.getSeleniumAddress(), chromeOptions, false);
    makeCdpAvailableOnHostMachine(chrome, driver);
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
  public void canDownloadFile() {
    open("https://selenide.org/quick-start.html");

    File selenideJar = $(byText("selenide.jar")).download(withExtension("jar"));
    assertThat(selenideJar.getName()).matches("selenide-[\\d.]+\\.jar");
  }

  @AfterEach
  public void tearDown() {
    proxyServer.shutdown();
  }
}
