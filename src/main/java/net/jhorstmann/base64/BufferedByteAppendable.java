package net.jhorstmann.base64;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

public class BufferedByteAppendable implements Appendable, Closeable, Flushable {
    private OutputStream out;
    private byte[] buffer;
    private int index;

    BufferedByteAppendable(OutputStream out, int bufferSize) {
        this.out = out;
        this.buffer = new byte[bufferSize];
        this.index = 0;
    }

    private void flushImpl() throws IOException {
        out.write(buffer, 0, index);
        index = 0;
    }

    private void appendImpl(char c) throws IOException {
        if (c > 255) {
            throw new IllegalArgumentException("Invalid character " + (int)c);
        }
        buffer[index++] = (byte) c;
        if (index >= buffer.length) {
            flushImpl();
        }
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

    public void flush() throws IOException {
        flushImpl();
    }

    public void close() throws IOException {
        flushImpl();
        out.close();
    }
}
