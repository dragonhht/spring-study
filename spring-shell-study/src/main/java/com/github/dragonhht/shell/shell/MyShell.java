package com.github.dragonhht.shell.shell;

import com.github.dragonhht.shell.model.Model;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.validation.constraints.Size;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-13
 */
@ShellComponent
public class MyShell {

    @ShellMethod(value = "求和函数")
    public int add(@ShellOption(help = "第一个数", defaultValue = "0") int n1,
                   @ShellOption(help = "第二个数", defaultValue = "5") int n2) {
        return n1 + n2;
    }

    @ShellMethod(value = "求和函数", key = "sum", prefix = "-")
    public int sum(@ShellOption(help = "第一个数", defaultValue = "0") int a,
                   @ShellOption(help = "第二个数", defaultValue = "5", value = "-two") int b) {
        return a + b;
    }

    @ShellMethod(value = "求和函数")
    public int sum1(@ShellOption(arity = 4) int[] arrs) {
        int sum = 0;
        for (int n : arrs) {
            sum += n;
        }
        return sum;
    }

    @ShellMethod("布尔型")
    public String bool(@ShellOption boolean yes) {
        return "yes is " + yes;
    }

    @ShellMethod("str")
    public String echo(@Size(min = 3) String str) {
        return "str is " + str;
    }

    @ShellMethod("Converter")
    public String converter(Model model) {
        return model.toString();
    }

}
