package net.jhorstmann.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Base64InputStream extends InputStream {

    static class OutputStreamBuffer extends ByteArrayOutputStream {

        private int pos;

        int remaining() {
            return count - pos;
        }

        boolean hasRemaining() {
            return pos < count;
        }

        void clear() {
            pos = 0;
        }

        int read() {
            if (hasRemaining()) {
                int res = buf[pos++] & 0xFF;
                if (pos == count) {
                    pos = 0;
                    count = 0;
                }
                return res;
            }
            else {
                return -1;
            }
        }

        int read(byte[] b, int off, int len) {
            if (hasRemaining()) {
                int max = Math.min(b.length, remaining());
                System.arraycopy(buf, pos, b, 0, max);
                pos += max;
                return max;
            } else {
                return -1;
            }
        }
    }
    private Base64StreamDecoder decoder;
    private Reader reader;
    private OutputStreamBuffer buffer;

    public Base64InputStream(Base64StreamDecoder decoder, Reader reader) throws IOException {
        this.decoder = decoder;
        this.reader = reader;
        this.buffer = new OutputStreamBuffer();
    }

    public Base64InputStream(InputStream in) throws IOException {
        this(new InputStreamReader(in, "US-ASCII"));
    }

    public Base64InputStream(Reader reader) throws IOException {
        this(new Base64StreamDecoder(), reader);
    }

    @Override
    public int read() throws IOException {
        while (!decoder.isEOF() && !buffer.hasRemaining()) {
            int ch = reader.read();
            decoder.decode(ch, buffer);
        }
        return buffer.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        while (!decoder.isEOF() && buffer.remaining() < len - off) {
            int ch = reader.read();
            decoder.decode(ch, buffer);
        }
        return buffer.read(b, off, len);
    }
}
