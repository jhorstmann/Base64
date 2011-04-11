package net.jhorstmann.base64;

public class IncompleteStreamException extends Base64Exception {

    public IncompleteStreamException(String msg) {
        super(msg);
    }

    public IncompleteStreamException(int ch) {
        super("Incomplete Base64 Stream: " + characterName(ch));
    }
}
