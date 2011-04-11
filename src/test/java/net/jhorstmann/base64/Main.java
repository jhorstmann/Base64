package net.jhorstmann.base64;

import java.io.File;
import java.io.IOException;

public class Main {
    
    private static String seconds(long t1, long t2) {
        return String.format("%.3fs", (t2-t1)/1000.0);
    }
    public static void main(String[] args) throws IOException {
        long t1,t2;

        t1 = System.currentTimeMillis();
        new Base64StreamEncoder().encode(new File("/home/jh/Downloads/glassfish-v3-b65.zip"), new File("/tmp/test.base64"));
        t2 = System.currentTimeMillis();
        System.out.println("Encoded in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decodeMapped(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, false);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder(mapped, indirect) in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decodeMapped(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, true);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder(mapped, direct) in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64StreamDecoder().decode(new File("/tmp/test.base64"), new File("/dev/null"));
        t2 = System.currentTimeMillis();
        System.out.println("StreamDecoder in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decode(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, false, false);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder in(indirect, indirect) " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decode(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, true, false);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder(direct, indirect) in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decode(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, false, true);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder(indirect, direct) in " + seconds(t1, t2));

        t1 = System.currentTimeMillis();
        new Base64NIODecoder().decode(new File("/tmp/test.base64"), new File("/dev/null"), 32*1024, true, true);
        t2 = System.currentTimeMillis();
        System.out.println("NIODecoder(direct, direct) in " + seconds(t1, t2));
    }

}
