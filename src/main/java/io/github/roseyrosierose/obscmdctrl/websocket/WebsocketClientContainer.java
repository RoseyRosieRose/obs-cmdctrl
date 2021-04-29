package io.github.roseyrosierose.obscmdctrl.websocket;

import io.github.roseyrosierose.obscmdctrl.AppConfigurationProperties;
import io.github.roseyrosierose.obscmdctrl.AppConfigurationProperties.WebsocketProperties;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebsocketClientContainer {

  @Autowired private AppConfigurationProperties appProps;

  private final AtomicBoolean connected = new AtomicBoolean(false);

  private OBSWebsocketClient websocket = null;

  @PostConstruct
  public void init() {
    create();
  }

  public void create() {
    if (websocket == null) {
      WebsocketProperties websocketProps = appProps.getWebsocket();
      URI uri =
          URI.create("ws://" + websocketProps.getHost() + ":" + websocketProps.getPort() + "/");
      websocket = new OBSWebsocketClient(uri, websocketProps.getPassword(), con -> connected(con));
      websocket.connect();
    }
  }

  public void connected(boolean newState) {
    connected.set(newState);
  }
}
