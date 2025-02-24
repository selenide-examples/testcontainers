package org.selenide.examples;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.codeborne.selenide.FileDownloadMode.HTTPGET;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestSetupExtension implements BeforeEachCallback, AfterEachCallback {
  @Override
  public void afterEach(ExtensionContext context) {
    closeWebDriver();
    Configuration.fileDownload = HTTPGET;
    Configuration.proxyEnabled = false;
    Configuration.proxyHost = null;
    Configuration.proxyPort = 0;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    afterEach(context);
  }
}
