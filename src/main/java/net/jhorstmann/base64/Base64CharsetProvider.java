package net.jhorstmann.base64;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.Iterator;

public class Base64CharsetProvider extends CharsetProvider {
    private Charset charset = new Base64Charset();

    @Override
    public Iterator<Charset> charsets() {
        return Collections.singleton(charset).iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        if (Base64Charset.CHARSET_NAME.equalsIgnoreCase(charsetName)) {
            return charset;
        }
        else {
            return null;
        }
    }
}
