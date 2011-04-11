package net.jhorstmann.base64;

import java.io.IOException;

public abstract class Base64Exception extends IOException {

    static String characterName(int ch) {
        if (ch == -1) {
            return "EOF";
        } else if (ch == '\r') {
            return "\\r";
        } else if (ch == '\n') {
            return "\\n";
        } else if (ch == '\t') {
            return "\\t";
        } else if (ch == '\f') {
            return "\\f";
        } else if (ch >= ' ' && ch <= '~') {
            return new StringBuilder(3).append("\'").append((char)ch).append("\'").toString();
        } else if (ch <= 255) {
            return String.format("\\x%02x", ch);
        } else {
            return String.format("\\u%04x", ch);
        }
    }

    public Base64Exception(String msg) {
        super(msg);
    }
}
