package io.github.roseyrosierose.obscmdctrl;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class CmdCtrlJFrame extends JFrame implements Runnable {

  private static final long serialVersionUID = -5904408360087675942L;
  private Logger logger = LogManager.getLogger();

  private volatile boolean running = true;
  private TerminalScreen screen;

  double interpolation = 0;
  final int TICKS_PER_SECOND = 25;
  final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
  final int MAX_FRAMESKIP = 5;
  int loops = 0;

  @Override
  public void run() {
    SwingTerminal terminal = new SwingTerminal();
    this.setTitle("OBS Cmd-Ctrl");
    this.setSize(800, 600);
    this.add(terminal);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    try {
      screen = new TerminalScreen(terminal);
      screen.startScreen();
      screen.clear();
      screen.refresh();
      terminal.enterPrivateMode();
      logger.info("GUI Init Complete..");

      terminal.putCharacter('H');
      terminal.putCharacter('e');
      terminal.putCharacter('l');
      terminal.putCharacter('l');
      terminal.putCharacter('o');
      terminal.putCharacter('\n');
      terminal.flush();

      while (running) {
        sleepForFPS();

        screen.doResizeIfNecessary();

        KeyStroke input = screen.pollInput();
        if (input != null) {
          logger.info("input {}", input);
        }

        TerminalSize terminalSize = screen.getTerminalSize();
        for (int column = 0; column < terminalSize.getColumns(); column++) {
          for (int row = 0; row < terminalSize.getRows(); row++) {
            int charVal = 32 + ((int) (Math.random() * 94)) % 94;
            screen.setCharacter(
                column,
                row,
                TextCharacter.fromCharacter(
                    (char) charVal, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0]);
          }
        }

        // Make update if any
        screen.refresh();
      }
      screen.stopScreen();
    } catch (Throwable t) {
      logger.error("Uncaught error", t);
    }
  }

  private void sleepForFPS() {
    try {
      // roughly 30fps, it's text, we don't need precision.
      Thread.sleep(34L);
    } catch (InterruptedException ie) {
      Thread.interrupted();
    }
  }
}
