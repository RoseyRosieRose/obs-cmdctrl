package io.github.roseyrosierose.obscmdctrl.websocket;

import java.net.URI;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class OBSWebsocketClient extends WebSocketClient {

  private final Logger logger = LogManager.getLogger();

  private final AtomicBoolean loggedIn = new AtomicBoolean(false);
  private Set<String> availableRequests = new HashSet<>();
  private final String password;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Consumer<Boolean> connectedCallback;
  private final HashMap<String, String> requestMsgTypeMap = new HashMap<>();

  public OBSWebsocketClient(URI uri, String password, Consumer<Boolean> connectedCallback) {
    super(uri);
    this.password = password;
    this.connectedCallback = connectedCallback;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    logger.info("WSCONN => {}", handshakedata.getHttpStatusMessage());
    connectedCallback.accept(true);
    loggedIn.compareAndSet(true, false);
    sendGetVersion();
  }

  private void sendRequest(String requestType, Map<String, JsonNode> params) {
    if (!availableRequests.contains(requestType)) return;
    try {
      String requestId = UUID.randomUUID().toString();
      ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
      rootNode.put("request-type", requestType);
      rootNode.put("message-id", requestId);

      if (params != null) {
        params.entrySet().forEach(entry -> rootNode.set(entry.getKey(), entry.getValue()));
      }

      String toSend = objectMapper.writeValueAsString(rootNode);
      requestMsgTypeMap.put(requestId, requestType);

      logger.info("WSTX => {}", toSend);
      send(toSend);
    } catch (Exception e) {
      logger.error("Unable to send message {}, {}", requestType, params);
    }
  }

  private void sendGetVersion() {
    availableRequests.add("GetVersion");
    sendRequest("GetVersion", null);
  }

  @Override
  public void onMessage(String message) {
    logger.info("WSRECV => {}", message);
    try {
      JsonNode root = objectMapper.readTree(message);
      String requestId = "heartbeat";
      String requestType = "Heartbeat";
      if (root.has("message-id")) {
        requestId = root.get("message-id").asText();
        requestType = requestMsgTypeMap.remove(requestId);
      }

      switch (requestType) {
        case "Heartbeat":
          logger.info(message);
          break;
        case "Authenticate":
          loggedIn.compareAndSet(false, true);
          logger.info("Sucessfully authenticated with OBS");
          sendRequest("SetHeartbeat", Map.of("enable", BooleanNode.TRUE));
          break;
        case "GetAuthRequired":
          boolean authRequired = root.get("authRequired").asBoolean();
          if (!authRequired) {
            logger.info("Authentication not required for OBS");
            loggedIn.compareAndSet(false, true);
            sendRequest("SetHeartbeat", Map.of("enable", BooleanNode.TRUE));
            break;
          }

          String challenge = root.get("challenge").asText();
          String salt = root.get("salt").asText();
          MessageDigest digest = MessageDigest.getInstance("SHA-256");

          // Swap in variable naming convention is to follow the var names used in OBS-Websocket
          // protocol docs.
          String secret_string = password + salt;
          byte[] secret_hash = digest.digest(secret_string.getBytes());
          String secret = Base64.encodeBytes(secret_hash);
          String auth_response_string = secret + challenge;
          byte[] auth_response_hash = digest.digest(auth_response_string.getBytes());
          String auth_response = Base64.encodeBytes(auth_response_hash);
          sendRequest("Authenticate", Map.of("auth", new TextNode(auth_response)));
          break;
        case "GetVersion":
          // Remember all available requests
          availableRequests =
              Arrays.stream(StringUtils.split(root.get("available-requests").asText(), ","))
                  .collect(Collectors.toSet());
          sendRequest("GetAuthRequired", null);
          break;
        default:
          logger.warn("Unknown response for messageType {}", requestType);
      }
    } catch (Exception e) {
      logger.error("Invalid JSON", e);
    }
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    connectedCallback.accept(false);
  }

  @Override
  public void onError(Exception ex) {
    logger.error("EXCEPTION", ex);
  }
}
