package io.github.roseyrosierose.obscmdctrl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {

  static {
    System.setProperty("spring.config.name", "config");
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    CmdCtrlJFrame ui = new CmdCtrlJFrame();
    ui.run();
  }
}
