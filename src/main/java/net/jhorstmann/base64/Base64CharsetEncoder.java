package net.jhorstmann.base64;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.CoderResult;

class Base64CharsetEncoder extends CharsetEncoder {

    private Base64NIODecoder coder;

    protected Base64CharsetEncoder(Base64Charset cs) {
        super(cs, 3.0f / 4.0f, 3);
        this.coder = new Base64NIODecoder();
    }

    @Override
    protected void implReset() {
        this.coder.reset();
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
        try {
            while (in.hasRemaining()) {
                if (out.remaining() < 3) {
                    return CoderResult.OVERFLOW;
                }
                coder.decode(in.get(), out);
            }
            return CoderResult.UNDERFLOW;
        } catch (IOException ex) {
            throw new CoderMalfunctionError(ex);
        }
    }
}
