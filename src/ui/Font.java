package ui;

import java.io.Serializable;

public class Font implements Serializable {
    public final String SET_PLAIN_TEXT = "\033[0;0m";
    public final String SET_BOLD_TEXT = "\033[0;1m";


    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_BLUE = "\u001B[34m";
    public final String ANSI_YELLOW = "\u001B[33m";

}
