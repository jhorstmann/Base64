package net.jhorstmann.base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Base64StreamEncoder {
    private static final char[] alphabet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    private static final int DEFAULT_LINE_WIDTH = 76;
    private static final char DEFAULT_LINE_SEPARATOR = '\n';

    private static final int STATE_EOF   = -1;
    private static final int STATE_START = 0;

    private int state;
    private int buffer;
    private int lineIdx;
    private int lineWidth = DEFAULT_LINE_WIDTH;
    private char lineSeparator = DEFAULT_LINE_SEPARATOR;

    void reset() {
        this.state = 0;
        this.buffer = 0;
        this.lineIdx = 0;
    }

    final void encode(final int ch, final Appendable out) throws IOException {
        //System.out.println(this.state + " " + ch + " " + this.buffer);
        switch (this.state) {
            case STATE_EOF:
                break;
            case STATE_START:
                if (ch == -1) {
                    this.state = STATE_EOF;
                }
                else {
                    if (this.lineIdx >= this.lineWidth) {
                        out.append(this.lineSeparator);
                        this.lineIdx = 0;
                    }
                    out.append(alphabet[(ch >>> 2)]);
                    this.lineIdx++;
                    this.buffer = (ch & 3) << 4;
                    this.state = 1;
                }
                break;
            case 1:
                if (ch == -1) {
                    out.append(alphabet[this.buffer]);
                    out.append('=');
                    out.append('=');
                    this.lineIdx += 3;
                    this.state = STATE_EOF;
                }
                else {
                    out.append(alphabet[this.buffer | (ch >>> 4)]);
                    this.lineIdx++;
                    this.buffer = (ch & 15) << 2;
                    this.state = 2;
                }
                break;
            case 2:
                if (ch == -1) {
                    out.append(alphabet[this.buffer]);
                    out.append('=');
                    this.lineIdx += 2;
                    this.state = STATE_EOF;
                }
                else {
                    out.append(alphabet[this.buffer | (ch >>> 6)]);
                    out.append(alphabet[ch & 63]);
                    this.lineIdx += 2;
                    this.state = STATE_START;
                }
                break;
            default:
                throw new IllegalStateException("Invalid State " + this.state);

        }
    }

    private int expectedLength(int length) {
        int tmp = (length+3)*4/3; // round up
        return tmp + tmp/lineWidth + 2; // add newlines and padding
    }

    public boolean isComplete() {
        return this.state == STATE_START || this.state == STATE_EOF;
    }

    public void encodeFinal(Appendable out) throws IOException {
        if (this.state != STATE_EOF) {
            encode(-1, out);
        }
    }

    public void encode(InputStream in, Appendable out) throws IOException {
        int ch;
        while ((ch = in.read()) != -1 && this.state != STATE_EOF) {
            encode(ch, out);
        }
        encodeFinal(out);
    }

    public void encode(InputStream in, Appendable out, int bufferSize) throws IOException {
        byte[] tmp = new byte[bufferSize];
        int len;
        while (-1 != (len = in.read(tmp)) && this.state != STATE_EOF) {
            encode(tmp, 0, len, out);
        }
        encodeFinal(out);
    }

    public void encode(CharSequence str, int offset, int length, Appendable out) throws IOException {
        int idx = offset;
        while (this.state != STATE_EOF && idx < offset + length) {
            int ch = str.charAt(idx);
            encode(ch, out);
            idx++;
        }
    }

    public void encode(CharSequence str, Appendable out) throws IOException {
        encode(str, 0, str.length(), out);
        encodeFinal(out);
    }

    public String encode(String str) throws IOException {
        FixedSizeStringBuilder out = new FixedSizeStringBuilder(expectedLength(str.length()));
        encode(str, out);
        return out.toString();
    }

    public String encode(byte[] in) throws IOException {
        FixedSizeStringBuilder out = new FixedSizeStringBuilder(expectedLength(in.length));
        encode(in, out);
        return out.toString();
    }

    public void encode(byte[] in, Appendable out) throws IOException {
        encode(in, 0, in.length, out);
        encodeFinal(out);
    }

    public void encode(byte[] in, int offset, int length, Appendable out) throws IOException {
        int idx = offset;
        while (this.state != STATE_EOF && idx < offset + length) {
            encode(in[idx] & 0xFF, out);
            idx++;
        }
    }

    void encodeByteBuffer(ByteBuffer in, Appendable out) throws IOException {
        while (this.state != STATE_EOF && in.hasRemaining()) {
            encode(in.get() & 0xFF, out);
        }
    }

    public void encode(ByteBuffer in, Appendable out) throws IOException {
        if (in.hasArray()) {
            int offset = in.arrayOffset();
            int length = in.limit()-offset;
            encode(in.array(), offset, length, out);
        }
        else {
            encodeByteBuffer(in, out);
        }
    }

    public void encode(File inFile, File outFile) throws IOException {
        int inBufferSize = 24*1024;
        int outBufferSize = inBufferSize/6*8;
        InputStream in = new BufferedInputStream(new FileInputStream(inFile), inBufferSize);
        try {
            BufferedByteAppendable out = new BufferedByteAppendable(new FileOutputStream(outFile), outBufferSize);
            try {
                encode(in, out, inBufferSize);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
        
    }
}
