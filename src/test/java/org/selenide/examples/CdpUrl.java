package org.selenide.examples;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.ContainerState;

import java.util.regex.Pattern;

/**
 * This is a workaround for an issue with CDP when webdriver is running in Docker container.
 * When Grid is running in Docker on port 4444, it's bound to some random port on host machine (say, 63647).
 *
 * On startup, ChromeDriver gets capability "se:cdp"="ws://172.17.0.3:4444/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
 * But it's available only inside docker container.
 *
 * To use CDP on host machine, we need to use URL "ws://localhost:63647/session/11cf1e54378f5491ed8cdc552ec8c99c/se/cdp".
 */
public class CdpUrl {
  private static final Logger log = LoggerFactory.getLogger(CdpUrl.class);
  private static final Pattern RE_CDP_URL = Pattern.compile("ws://.+:4444/(.+)");

  public static void makeCdpAvailableOnHostMachine(ContainerState container, RemoteWebDriver driver) {
    MutableCapabilities capabilities = (MutableCapabilities) driver.getCapabilities();
    String currentCdpUrl = (String) capabilities.getCapability("se:cdp");
    if (currentCdpUrl == null) {
      log.warn("Alarm! CDP url is null. Webdriver: {}. Capabilities: {}. Container: {}:{}",
        driver, capabilities, container.getHost(), container.getMappedPort(4444));
    }
    else {
      String cdpUrlForHostMachine = cdpUrlForHostMachine(currentCdpUrl, container);
      capabilities.setCapability("se:cdp", cdpUrlForHostMachine);
      log.info("Replaced CDP url {} by {}", currentCdpUrl, cdpUrlForHostMachine);
    }
  }

  static String cdpUrlForHostMachine(String cdpUrlInsideDocker, ContainerState container) {
    String newUrl = "ws://%s:%d/$1".formatted(container.getHost(), container.getMappedPort(4444));
    return RE_CDP_URL.matcher(cdpUrlInsideDocker).replaceFirst(newUrl);
  }
}
