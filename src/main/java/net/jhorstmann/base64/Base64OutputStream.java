package net.jhorstmann.base64;

import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream extends OutputStream {

    private Base64StreamEncoder encoder;
    private Appendable out;

    public Base64OutputStream(Base64StreamEncoder encoder, Appendable out) {
        this.encoder = encoder;
        this.out = out;
    }

    public Base64OutputStream(Appendable out) {
        this(new Base64StreamEncoder(), out);
    }

    public Base64OutputStream(OutputStream out) {
        this(new Base64StreamEncoder(), new ByteAppendable(out));
    }

    @Override
    public void write(byte[] b) throws IOException {
        encoder.encode(b, out);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        encoder.encode(b, off, len, out);
    }

    @Override
    public void write(int b) throws IOException {
        encoder.encode(b, out);
    }
}
