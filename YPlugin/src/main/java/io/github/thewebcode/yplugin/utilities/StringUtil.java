package io.github.thewebcode.yplugin.utilities;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

public class StringUtil extends OutputStream {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

    public static String NEWLINE_SPLIT_REGEX = "\\r?\\n";

    protected StringBuilder text = new StringBuilder();

    public static String trimLength(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }

        return str;
    }

    public static String joinString(String[] str, String delimiter,
                                    int initialIndex) {
        if (str.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(str[initialIndex]);
        for (int i = initialIndex + 1; i < str.length; ++i) {
            buffer.append(delimiter).append(str[i]);
        }
        return buffer.toString();
    }

    public static String joinQuotedString(String[] str, String delimiter,
                                          int initialIndex, String quote) {
        if (str.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(quote);
        buffer.append(str[initialIndex]);
        buffer.append(quote);
        for (int i = initialIndex + 1; i < str.length; ++i) {
            buffer.append(delimiter).append(quote).append(str[i]).append(quote);
        }
        return buffer.toString();
    }

    public static String joinString(String[] str, String delimiter) {
        return joinString(str, delimiter, 0);
    }

    public static String joinString(Object[] str, String delimiter,
                                    int initialIndex) {
        if (str.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(str[initialIndex].toString());
        for (int i = initialIndex + 1; i < str.length; ++i) {
            buffer.append(delimiter).append(str[i].toString());
        }
        return buffer.toString();
    }

    public static String joinString(int[] str, String delimiter,
                                    int initialIndex) {
        if (str.length == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(Integer.toString(str[initialIndex]));
        for (int i = initialIndex + 1; i < str.length; ++i) {
            buffer.append(delimiter).append(Integer.toString(str[i]));
        }
        return buffer.toString();
    }

    public static String joinString(Collection<?> str, String delimiter,
                                    int initialIndex) {
        if (str.size() == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        for (Object o : str) {
            if (i >= initialIndex) {
                if (i > 0) {
                    buffer.append(delimiter);
                }

                buffer.append(o.toString());
            }
            ++i;
        }
        return buffer.toString();
    }

    public static int getLevenshteinDistance(String s, String t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length();
        int m = t.length();

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n + 1];
        int d[] = new int[n + 1];
        int _d[];
        int i;
        int j;

        char t_j;

        int cost;

        for (i = 0; i <= n; ++i) {
            p[i] = i;
        }

        for (j = 1; j <= m; ++j) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; ++i) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
                        + cost);
            }
            _d = p;
            p = d;
            d = _d;
        }
        return p[n];
    }

    public static <T extends Enum<?>> T lookup(Map<String, T> lookup, String name, boolean fuzzy) {
        String testName = name.replaceAll("[ _]", "").toLowerCase();

        T type = lookup.get(testName);
        if (type != null) {
            return type;
        }

        if (!fuzzy) {
            return null;
        }

        int minDist = Integer.MAX_VALUE;

        for (Map.Entry<String, T> entry : lookup.entrySet()) {
            final String key = entry.getKey();
            if (key.charAt(0) != testName.charAt(0)) {
                continue;
            }

            int dist = getLevenshteinDistance(key, testName);

            if (dist >= minDist) {
                minDist = dist;
                type = entry.getValue();
            }
        }

        if (minDist > 1) {
            return null;
        }

        return type;
    }

    public static String scrambleText(String text) {
        StringBuilder Scrambled = new StringBuilder();
        if (text.contains(" ")) {
            String[] Words = text.split(" ");
            for (String word : Words) {
                ArrayList<Character> chars = new ArrayList<>(word.length());
                for (char c : word.toCharArray()) {
                    chars.add(c);
                }
                Collections.shuffle(chars);
                char[] shuffled = new char[chars.size()];
                for (int i = 0; i < shuffled.length; i++) {
                    shuffled[i] = chars.get(i);
                }
                Scrambled.append(" ").append(shuffled);
            }
            return Scrambled.toString();
        } else {
            ArrayList<Character> chars = new ArrayList<>(text.length());
            for (char c : text.toCharArray()) {
                chars.add(c);
            }
            Collections.shuffle(chars);
            char[] shuffled = new char[chars.size()];
            for (int i = 0; i < shuffled.length; i++) {
                shuffled[i] = chars.get(i);
            }
            return new String(shuffled);
        }
    }

    public static String formatColorCodes(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String colorize(String str) {
        return formatColorCodes(str);
    }

    public static List<String> colorize(List<String> strings) {
        return formatColorCodes(strings);
    }

    public static String stripColor(String input) {
        if (input == null) {
            return null;
        }

        return ChatColor.stripColor(input);
    }

    public static List<String> formatColorCodes(List<String> list) {
        List<String> converted = new ArrayList<>();
        for (String s : list) {
            converted.add(formatColorCodes(s));
        }
        return converted;
    }

    public static String getTrimmedPlayerName(String playerName) {

        switch (playerName.length()) {
            case 16:
                return playerName.substring(0, playerName.length() - 3);
            case 15:
                return playerName.substring(0, playerName.length() - 2);
            case 14:
                return playerName.substring(0, playerName.length() - 1);
            default:
                return playerName;
        }
    }

    public static boolean startsWithIgnoreCase(final String string, final String prefix) throws IllegalArgumentException, NullPointerException {
        Validate.notNull(string, "Cannot check a null string for a match");
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static boolean isNumericAt(String[] strs, int index) {
        return strs.length > index && StringUtils.isNumeric(strs[index]);
    }

    public static int getNumberAt(String[] strs, int index, int def) {
        if (!isNumericAt(strs, index)) {
            return def;
        }
        return Integer.parseInt(strs[index]);
    }

    public static String[] splitOnNewline(String s) {
        return s.split(NEWLINE_SPLIT_REGEX);
    }

    public static String argStr(String[] s) {
        return argStr(s, " ", 0);
    }

    public static String argStr(String[] s, int start) {
        return argStr(s, " ", start);
    }

    public static String argStr(String[] s, String sep) {
        return argStr(s, sep, 0);
    }

    public static String argStr(String[] s, String sep, int start) {
        StringBuilder ret = new StringBuilder();
        if (s != null) {
            for (int i = start; i < s.length; ++i) {
                ret.append(s[i]);
                if (i + 1 < s.length) {
                    ret.append(sep);
                }
            }
        }
        return ret.toString();
    }

    public static String argStr(String[] s, String sep, int start, int length) {
        StringBuilder ret = new StringBuilder();
        if (s != null) {
            for (int i = start, j = 0; i < s.length && j < length; ++i, ++j) {
                ret.append(s[i]);
                if (i + 1 < s.length) {
                    ret.append(sep);
                }
            }
        }
        return ret.toString();
    }

    public static boolean isIn(String input, String[] check) {
        input = input.trim();
        for (String c : check) {
            if (input.equalsIgnoreCase(c.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(String input, String check) {
        String comms[] = check.split(",");
        input = input.trim();
        for (String c : comms) {
            if (input.equalsIgnoreCase(c.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean startIsIn(String input, String check) {
        String comms[] = check.split(",");
        for (String c : comms) {
            if (input.length() >= c.length()) {
                if (input.substring(0, c.length()).equalsIgnoreCase(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean startIsIn(String input, String[] check) {
        for (String c : check) {
            if (input.length() >= c.length()) {
                if (input.substring(0, c.length()).equalsIgnoreCase(c)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int count(String str, String find) {
        int c = 0;
        for (int i = 0; i < str.length() - find.length(); ++i) {
            if (str.substring(i, i + find.length()).equals(find)) {
                ++c;
            }
        }
        return c;
    }

    public static int count(String str, char find) {
        int c = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == find) {
                ++c;
            }
        }
        return c;
    }

    public static int countIgnoreCase(String str, String find) {
        int c = 0;
        for (int i = 0; i < str.length() - find.length(); ++i) {
            if (str.substring(i, i + find.length()).equalsIgnoreCase(find)) {
                ++c;
            }
        }
        return c;
    }

    public static int indexOf(String array[], String search) {
        if (array != null && array.length > 0) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (array[i].equals(search)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(String array[], String search) {
        for (int i = array.length - 1; i >= 0; --i) {
            if (array[i].equalsIgnoreCase(search)) {
                return i;
            }
        }
        return -1;
    }

    public static String getStackStr(Throwable err) {
        if (err == null) {
            return "";
        }
        StringUtil stackoutstream = new StringUtil();
        PrintWriter stackstream = new PrintWriter(stackoutstream);
        err.printStackTrace(stackstream);
        stackstream.flush();
        stackstream.close();
        return stackoutstream.text.toString();

    }

    public static String padRight(String str, int len) {
        return padRight(str, len, ' ');
    }

    public static String padRight(String str, int len, char pad) {
        StringBuilder ret = new StringBuilder(str);
        for (int i = str.length(); i < len; ++i) {
            ret.append(pad);
        }
        return ret.toString();
    }

    public static String padLeft(String str, int len) {
        return repeat(' ', len - str.length()) + str;
    }

    public static String padLeft(String str, int len, char pad) {
        return repeat(pad, len - str.length()) + str;
    }

    public static String padCenter(String str, int len) {
        len -= str.length();
        int prepad = len / 2;
        return repeat(' ', prepad) + str + repeat(' ', len - prepad);
    }

    public static String padCenter(String str, int len, char pad) {
        len -= str.length();
        int prepad = len / 2;
        return repeat(pad, prepad) + str + repeat(pad, len - prepad);
    }

    public static String strWordWrap(String str, int width) {
        return strWordWrap(str, width, 0, ' ');
    }

    public static String strWordWrap(String str, int width, int tab) {
        return strWordWrap(str, width, tab, ' ');
    }

    public static String strWordWrap(String str, int width, int tab, char tabChar) {
        String ret = "";
        while (str.length() > 0) {
            if (str.length() <= width) {
                return (ret.length() > 0 ? ret + "\n" + repeat(tabChar, tab) : "").concat(str);
            }
            String line1 = strTrim(str, width);
            int lastPos = line1.length() - (ret.length() > 0 && line1.length() > tab + 1 ? tab + 1 : 1);
            while (lastPos > 0 && line1.charAt(lastPos) != ' ') {
                --lastPos;
            }
            if (lastPos == 0) {
                lastPos = line1.length() - (ret.length() > 0 && line1.length() > tab + 1 ? tab + 1 : 1);
            }
            ret += (ret.length() > 0 ? "\n" + repeat(tabChar, tab) : "") + str.substring(0, lastPos);
            str = str.substring(lastPos + 1);
        }
        return ret;
    }

    public static String strWordWrapRight(String str, int width, int tab, char tabChar) {
        String ret = "";
        while (str.length() > 0) {
            if (str.length() <= width) {
                return (ret.length() > 0 ? ret + "\n" : "").concat(padLeft(str, width, tabChar));
            }
            String line1 = strTrim(str, width);
            int lastPos = line1.length() - (ret.length() > 0 && line1.length() > tab + 1 ? tab + 1 : 1);
            while (lastPos > 0 && line1.charAt(lastPos) != ' ') {
                --lastPos;
            }
            if (lastPos <= 0) {
                lastPos = line1.length() - (ret.length() > 0 && line1.length() > tab + 1 ? tab + 1 : 1);
            }
            ret += (ret.length() > 0 ? "\n" : "") + padLeft(str.substring(0, lastPos), width, tabChar);
            str = str.substring(lastPos + 1);
        }
        return ret;
    }

    public static String strWordWrapRight(String str, int width, int tab) {
        return strWordWrapRight(str, width, tab, ' ');
    }

    public static String repeat(char ch, int len) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            ret.append(ch);
        }
        return ret.toString();
    }

    public static String repeat(String str, int count) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            ret.append(str);
        }
        return ret.toString();
    }

    public static String strTrim(String str, int length) {
        if (str.length() > length) {
            int width = length;
            String ret = "";
            boolean lastCol = false;
            for (char c : str.toCharArray()) {
                if (c == '\u00A7') {
                    ret += c;
                    lastCol = true;
                } else {
                    if (!lastCol) {
                        if (width - 1 >= 0) {
                            width -= 1;
                            ret += c;
                        } else {
                            return ret;
                        }
                    } else {
                        ret += c;
                        lastCol = false;
                    }
                }
            }
        }
        return str;
    }

    public static String titleCase(String str) {
        StringBuilder ret = new StringBuilder();
        boolean st = true;
        for (char c : str.toLowerCase().toCharArray()) {
            if (st) {
                ret.append(Character.toTitleCase(c));
            } else {
                ret.append(c);
            }
            st = c == ' ';
        }
        return ret.toString();
    }

    @Override
    public void write(int b) throws IOException {
        text.append((char) b);
    }
}
