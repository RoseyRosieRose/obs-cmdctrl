package io.github.roseyrosierose.obscmdctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellFactory;

@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {

  @Autowired private MainMenu mainMenu;

  static {
    System.setProperty("spring.config.name", "config");
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    Shell shell = ShellFactory.createConsoleShell("cmdctrl", null, mainMenu);
    shell.commandLoop();
  }
}
