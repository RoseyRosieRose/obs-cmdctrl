package io.github.roseyrosierose.obscmdctrl.midi;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MidiManager implements Receiver {
  private final Logger logger = LogManager.getLogger();

  private volatile Consumer<Integer> keyPressCallback = null;

  public void registerCallback(Consumer<Integer> keyPressCallback) {
    this.keyPressCallback = keyPressCallback;
  }

  @PostConstruct
  public void init() {
    startMidi();
  }

  private void closeMidi() {
    try {
      MidiSystem.getTransmitter().setReceiver(null);
    } catch (MidiUnavailableException e) {
      logger.error("Error closing Midi", e);
    }
  }

  private void startMidi() {
    try {
      MidiSystem.getTransmitter().setReceiver(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {} // NO-OP

  @Override
  public void send(MidiMessage msg, long timestamp) {
    int key = msg.getMessage()[1];
    int vel = 0;
    if (msg.getLength() > 2) {
      vel = msg.getMessage()[2];
    }
    if (vel > 0) {
      logger.info("Key Pressed {}", key);
      if (keyPressCallback != null) {
        keyPressCallback.accept(key);
      }
    }
  }
}
