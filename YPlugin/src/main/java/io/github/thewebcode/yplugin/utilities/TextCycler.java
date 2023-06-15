package io.github.thewebcode.yplugin.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class TextCycler {
    protected final String textPrefix;
    protected final String originalText;
    protected final int trimLength;
    protected int currentTrimIndex = 0;
    private String cachedValue = null;

    public TextCycler(String text, int trimLength) {
        this(null, text, trimLength);
    }

    public int getTrimLength() {
        return trimLength;
    }

    public TextCycler(String prefix, String text, int trimLength) {
        if (prefix == null) {
            prefix = StringUtils.EMPTY;
        }
        Validate.notEmpty(text, "Text must be specified.");
        Validate.isTrue(trimLength > 0, "The length to trim to must be positive.");
        this.trimLength = trimLength;
        originalText = text;
        textPrefix = prefix.trim();
        if (originalText.length() <= this.trimLength - textPrefix.length()) {
            cachedValue = textPrefix + originalText;
        }
    }

    public String getPrefix() {
        return textPrefix;
    }

    public String getText() {
        return originalText;
    }

    public String tick() {
        String val = toString();
        currentTrimIndex = (currentTrimIndex + 1) % (originalText.length());
        return val;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + currentTrimIndex;
        result = prime * result
                + ((originalText == null) ? 0 : originalText.hashCode());
        result = prime * result + ((textPrefix == null) ? 0 : textPrefix.hashCode());
        result = prime * result + trimLength;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TextCycler)) {
            return false;
        }
        TextCycler other = (TextCycler) obj;
        if (currentTrimIndex != other.currentTrimIndex) {
            return false;
        }
        if (originalText == null) {
            if (other.originalText != null) {
                return false;
            }
        } else if (!originalText.equals(other.originalText)) {
            return false;
        }
        if (textPrefix == null) {
            if (other.textPrefix != null) {
                return false;
            }
        } else if (!textPrefix.equals(other.textPrefix)) {
            return false;
        }
        if (trimLength != other.trimLength) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (cachedValue != null) {
            return cachedValue;
        }
        StringBuilder display = new StringBuilder(originalText.substring(currentTrimIndex, Math.min(currentTrimIndex + trimLength, originalText.length())));
        if (display.length() < trimLength) {
            int add = trimLength - display.length();
            display.append(originalText.substring(0, Math.min(add, originalText.length())));
        }
        if (textPrefix.length() > 0) {
            int newLen = trimLength - textPrefix.length();
            display.replace(newLen, display.length(), "");
            display.insert(0, textPrefix);
        }
        return display.toString();
    }
}
