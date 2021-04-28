package io.github.roseyrosierose.obscmdctrl;

import org.springframework.stereotype.Component;

import com.budhash.cliche.Command;

@Component
public class MainMenu {
  @Command
  public String helloWorld() {
    return "helloWorld!";
  }
}
