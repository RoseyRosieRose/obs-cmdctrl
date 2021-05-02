package io.github.roseyrosierose.obscmdctrl;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class CmdCtrlJFrame extends JFrame implements Runnable {

  private static final long serialVersionUID = -5904408360087675942L;
  private Logger logger = LogManager.getLogger();

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
      TerminalScreen screen = new TerminalScreen(terminal);
      WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
      screen.startScreen();
      terminal.enterPrivateMode();
      logger.info("GUI Init Complete..");
      // Loop here
      while (true) {}
    } catch (Throwable t) {
      logger.error("", t);
    }
  }
}
