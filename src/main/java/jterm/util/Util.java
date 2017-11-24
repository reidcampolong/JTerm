package jterm.util;

import jterm.JTerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    /**
     * getRuntime() String
     * <br></br><br></br>
     * Takes an interval of time in milliseconds, and returns amount of time it represents.
     * Useful for determining runtime of a program in a more readable format.
     *
     * @param interval milliseconds to convert to dd, hh, mm, ss, SSS format
     * @return time converted to readable format
     */
    public static String getRunTime(long interval) {
        long seconds = interval / 1000;
        StringBuilder builder = new StringBuilder();

        if ((seconds / 86400) >= 1) {
            builder.append(String.valueOf(seconds / 86400)).append(" days, ");
        }
        if ((seconds / 3600) >= 1) {
            builder.append(String.valueOf((seconds / 3600) % 24)).append(" hours, ");
        }
        if ((seconds / 60) >= 1) {
            builder.append(String.valueOf((seconds / 60) % 60)).append(" minutes, ");
        }

        builder.append(String.valueOf(seconds % 60)).append(" seconds, ");
        builder.append(String.valueOf(interval % 1000)).append(" millis");

        return builder.toString();
    }

    /**
     * clearLine() void
     * <br></br><br></br>
     * Clears a line in the console of size line.length().
     *
     * @param line        line to be cleared
     * @param clearPrompt choose to clear PROMPT along with line (only use true if PROMPT exists)
     */
    public static void clearLine(String line, int cursorPos, boolean clearPrompt) {
        if (JTerm.isHeadless()) {

            for (int i = 0; i < cursorPos + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
                JTerm.out.print('\b');
            for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
                JTerm.out.print(' ');
            for (int i = 0; i < line.length() + (clearPrompt ? JTerm.PROMPT.length() : 0); i++)
                JTerm.out.print('\b');

        } else {
            JTerm.getTerminal().clearLine(line, clearPrompt);
        }
    }

    public static ArrayList<String> getAsArray(String options) {
        return new ArrayList<>(Arrays.asList(options.split(" ")));
    }

    public static String getAsString(List<String> options) {
        return options.toString().replaceAll("[,\\[\\]]", "");
    }

    public static String getFullPath(String fileName) {
        return !fileName.startsWith("/") ? String.format("%s/%s", JTerm.currentDirectory, fileName) : fileName;
    }
}
