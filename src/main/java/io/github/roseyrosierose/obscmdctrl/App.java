package io.github.roseyrosierose.obscmdctrl;

import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellFactory;
import io.github.roseyrosierose.obscmdctrl.menu.MainMenu;
import io.github.roseyrosierose.obscmdctrl.menu.MidiMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {

  @Autowired private MainMenu mainMenu;
  @Autowired private MidiMenu midiMenu;

  static {
    System.setProperty("spring.config.name", "config");
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Shell shell = ShellFactory.createConsoleShell("cmdctrl", null, mainMenu);
    shell.addAuxHandler(midiMenu, "midi");
    shell.commandLoop();
  }
}
