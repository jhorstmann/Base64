package net.jhorstmann.base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Base64Encoder {

    static final char[] alphabet =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();


    private static final int readAndEncode(InputStream in, Writer out) throws IOException {
        int ch1 = in.read();
        if (ch1 == -1) {
            return 0;
        } else {
            out.write(alphabet[(ch1 >> 2)]);
            int ch2 = in.read();
            if (ch2 == -1) {
                out.write(alphabet[(ch1 & 3) << 4]);
                out.write('=');
                out.write('=');
                return 1;
            } else {
                out.write(alphabet[((ch1 & 3) << 4) | (ch2 >> 4)]);

                int ch3 = in.read();
                if (ch3 == -1) {
                    out.write(alphabet[((ch2 & 15) << 2)]);
                    out.write('=');
                    return 2;
                } else {
                    out.write(alphabet[((ch2 & 15) << 2) | (ch3 >> 6)]);
                    out.write(alphabet[(ch3 & 63)]);
                    return 3;
                }
            }
        }
    }

    public static final void encode(InputStream in, Writer out) throws IOException {
        encode(in, out, 76);
    }

    public static final void encode(InputStream in, Writer out, int lineLength) throws IOException {
        for (int i = 0; readAndEncode(in, out) != 0; i += 4) {
            if (i >= lineLength) {
                i = 0;
                out.write('\n');
            }
        }
    }

    public static final void encode(InputStream in, OutputStream out) throws IOException {
        encode(in, new OutputStreamWriter(out, "US-ASCII"));
    }

    public static String encode(byte[] bytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        StringWriter out = new StringWriter();
        encode(in, out);
        return out.toString();
    }

    public static String encode(String str) throws IOException {
        return encode(str.getBytes("US-ASCII"));
    }
}
