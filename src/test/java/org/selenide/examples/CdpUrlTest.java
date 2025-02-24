package org.selenide.examples;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ContainerState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.selenide.examples.CdpUrl.cdpUrlForHostMachine;

class CdpUrlTest {
  @Test
  void makesCdpUrlUsableFromHostMachine() {
    ContainerState container = mock();
    when(container.getHost()).thenReturn("my.macbook");
    when(container.getMappedPort(4444)).thenReturn(92837);
    assertThat(cdpUrlForHostMachine("ws://172.17.0.3:4444/session/0798a8c4ce1839c94e56a6e35c1e5ebf/se/cdp", container))
      .isEqualTo("ws://my.macbook:92837/session/0798a8c4ce1839c94e56a6e35c1e5ebf/se/cdp");
  }
}
