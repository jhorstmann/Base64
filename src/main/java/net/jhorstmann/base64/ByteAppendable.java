package net.jhorstmann.base64;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

final class ByteAppendable implements Appendable, Closeable {

    private OutputStream out;

    ByteAppendable(OutputStream out) {
        this.out = out;
    }

    private void appendImpl(char c) throws IOException {
        if (c > 255) {
            throw new IllegalArgumentException("Invalid character " + (int)c);
        }
        out.write(c);
    }

    public Appendable append(CharSequence csq) throws IOException {
        for (int i=0, len=csq.length(); i<len; i++) {
            appendImpl(csq.charAt(i));
        }
        return this;
    }

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        for (int i=start; i<end; i++) {
            appendImpl(csq.charAt(i));
        }
        return this;
    }

    public Appendable append(char c) throws IOException {
        appendImpl(c);
        return this;
    }

    public void close() throws IOException {
        out.close();
    }
}
