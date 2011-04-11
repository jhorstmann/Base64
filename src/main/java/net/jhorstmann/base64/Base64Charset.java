package net.jhorstmann.base64;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

class Base64Charset extends Charset {
    public static final String CHARSET_NAME = "BASE64";

    protected Base64Charset() {
        super(CHARSET_NAME, new String[0]);
    }

    @Override
    public boolean contains(Charset cs) {
        return cs instanceof Base64Charset;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new Base64CharsetDecoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new Base64CharsetEncoder(this);
    }
}
