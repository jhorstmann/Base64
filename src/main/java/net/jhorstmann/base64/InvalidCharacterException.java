package net.jhorstmann.base64;

public class InvalidCharacterException extends Base64Exception {

    public InvalidCharacterException(String msg) {
        super(msg);
    }

    public InvalidCharacterException(int ch) {
        super("Invalid Character: " + characterName(ch));
    }
}
