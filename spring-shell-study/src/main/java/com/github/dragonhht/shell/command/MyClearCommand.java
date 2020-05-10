package com.github.dragonhht.shell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Clear;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-13
 */
@ShellComponent
public class MyClearCommand implements Clear.Command {

    @ShellMethod("my clear")
    public void clear() {
        System.out.println("clear...");
    }
}
