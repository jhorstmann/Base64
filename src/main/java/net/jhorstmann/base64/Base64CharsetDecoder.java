package net.jhorstmann.base64;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.CoderResult;

class Base64CharsetDecoder extends CharsetDecoder {

    private final Base64StreamEncoder coder;

    protected Base64CharsetDecoder(Charset cs) {
        super(cs, 4.0f / 3.0f, 4.0f);
        this.coder = new Base64StreamEncoder();
    }

    @Override
    protected void implReset() {
        this.coder.reset();
    }

    @Override
    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
        try {
            while (in.hasRemaining()) {
                if (out.remaining() < 3) {
                    return CoderResult.OVERFLOW;
                }
                coder.encode(in.get() & 0xFF, out);
            }
            return CoderResult.UNDERFLOW;
        } catch (IncompleteStreamException ex) {
            return CoderResult.malformedForLength(1);
        } catch (InvalidCharacterException ex) {
            return CoderResult.unmappableForLength(1);
        } catch (IOException ex) {
            throw new CoderMalfunctionError(ex);
        }
    }

    @Override
    protected CoderResult implFlush(CharBuffer out) {
        try {
            coder.encodeFinal(out);
            return CoderResult.UNDERFLOW;
        } catch (IncompleteStreamException ex) {
            return CoderResult.malformedForLength(1);
        } catch (InvalidCharacterException ex) {
            return CoderResult.unmappableForLength(1);
        } catch (IOException ex) {
            throw new CoderMalfunctionError(ex);
        }
    }
}
