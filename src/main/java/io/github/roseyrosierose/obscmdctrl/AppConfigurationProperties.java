package io.github.roseyrosierose.obscmdctrl;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cmdctrl")
public class AppConfigurationProperties {

  private WebsocketProperties websocket;

  public static class WebsocketProperties {
    @NotBlank private String host;

    @Min(1)
    @Max(65000)
    private Integer port = 4444;

    private String password = null;

    public String getHost() {
      return host;
    }

    public void setHost(String host) {
      this.host = host;
    }

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  public WebsocketProperties getWebsocket() {
    return websocket;
  }

  public void setWebsocket(WebsocketProperties websocket) {
    this.websocket = websocket;
  }
}
