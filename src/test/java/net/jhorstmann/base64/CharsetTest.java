package net.jhorstmann.base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class CharsetTest {

    private static void assertEquals(byte[] expected, byte[] actual) {
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            //System.out.println(expected[i] + " " + actual[i]);
            Assert.assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    public void testProvider() {
        Charset cs = Charset.forName("BASE64");
        Assert.assertNotNull(cs);
        Assert.assertEquals("BASE64", cs.name());
    }

    @Test
    public void testGetBytes() throws UnsupportedEncodingException {
        assertEquals("JH".getBytes("US-ASCII"), "Skg=".getBytes("BASE64"));
        assertEquals("Hallo".getBytes("US-ASCII"), "SGFsbG8=".getBytes("BASE64"));
        assertEquals("Test 123".getBytes("US-ASCII"), "VGVzdCAxMjM=".getBytes("BASE64"));
    }

    @Test
    public void testNewString() throws UnsupportedEncodingException {
        Assert.assertEquals("Skg=", new String(new byte[]{'J', 'H'}, "BASE64"));
        Assert.assertEquals("SGFsbG8=", new String(new byte[]{'H', 'a', 'l', 'l', 'o'}, "BASE64"));
        Assert.assertEquals("VGVzdCAxMjM=", new String(new byte[]{'T', 'e', 's', 't', ' ', '1', '2', '3'}, "BASE64"));
    }

    private void test(String str) throws IOException {
        byte[] data = str.getBytes("UTF-8");
        test(data);
    }

    private void test(byte[] data) throws IOException {
        String base64 = new Base64StreamEncoder().encode(data);
        Assert.assertEquals(base64, new String(data, "BASE64"));
        assertEquals(data, base64.getBytes("BASE64"));
    }

    @Test
    public void test1() throws IOException {
        test("a");
        test("b");
        test("c");
        test("A");
        test("B");
        test("C");
        test("0");
        test("1");
    }

    @Test
    public void test2() throws IOException {
        test("ab");
        test("bc");
        test("cd");
        test("AB");
        test("BC");
        test("CD");
        test("01");
        test("12");
    }

    @Test
    public void test3() throws IOException {
        test("abc");
        test("bcd");
        test("cde");
        test("ABC");
        test("BCD");
        test("CDE");
        test("012");
        test("123");
    }

    @Test
    public void test4() throws IOException {
        test("abcd");
        test("bcde");
        test("cdef");
        test("ABCD");
        test("BCDE");
        test("CDEF");
        test("0123");
        test("1245");
    }

    @Test
    public void testHallo() throws IOException {
        test("Hallo Welt");
        test("Dies ist ein Test");
    }

    @Test
    public void testRandomSmall() throws IOException {
        Random random = new Random();
        int step = 13;
        int max = 1<<15;
        for (int i=step; i<max; i+=step) {
            byte[] data = new byte[i];
            random.nextBytes(data);
            test(data);
        }
    }

    @Test
    public void testRandomLarge() throws IOException {
        Random random = new Random();
        for (int i=8; i<25; i++) {
            byte[] data = new byte[(1<<i) + random.nextInt(1<<(i-1))];
            random.nextBytes(data);
            test(data);
        }
    }

}
