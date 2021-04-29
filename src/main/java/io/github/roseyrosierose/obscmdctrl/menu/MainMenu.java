package io.github.roseyrosierose.obscmdctrl.menu;

import com.budhash.cliche.Command;
import org.springframework.stereotype.Component;

@Component
public class MainMenu {
  @Command
  public String helloWorld() {
    return "helloWorld!";
  }
}
