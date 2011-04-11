package net.jhorstmann.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import org.junit.Assert;
import org.junit.Test;

public class Base64Test {

    private static void testEncode(String str, String expected) throws IOException {
        //ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("US-ASCII"));
        //StringWriter out = new StringWriter();
        //Base64Encoder.encode(in, out);

        Assert.assertEquals(expected, Base64Encoder.encode(str));
        Assert.assertEquals(expected, new Base64StreamEncoder().encode(str));
    }

    private static void testDecodeReader(String str, String base64) throws IOException {
        StringReader in = new StringReader(base64);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new Base64StreamDecoder().decode(in, out);
        String result = out.toString("US-ASCII");
        Assert.assertEquals(str, result);
    }

    private static void testDecodeInputStream(String str, String base64) throws IOException {
        InputStream in = new ByteArrayInputStream(base64.getBytes("US-ASCII"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new Base64StreamDecoder().decode(in, out);
        String result = out.toString("US-ASCII");
        Assert.assertEquals(str, result);
    }

    private static void testDecode(String str) throws IOException {
        String base64 = Base64Encoder.encode(str);

        testDecodeReader(str, base64);
        testDecodeInputStream(str, base64);
        //System.out.println(base64);

        Assert.assertEquals(str, new String(new Base64StreamDecoder().decode(base64), "US-ASCII"));
        Assert.assertEquals(str, new String(new Base64StreamDecoder().decode(base64.getBytes("US-ASCII")), "US-ASCII"));
        Assert.assertEquals(str, new String(new Base64StreamDecoder().decode(base64.toCharArray()), "US-ASCII"));
    }

    @Test
    public void testEncode1() throws IOException {
        testEncode("0", "MA==");
        testEncode("1", "MQ==");
        testEncode("2", "Mg==");
        testEncode("3", "Mw==");
        testEncode("4", "NA==");
        testEncode("5", "NQ==");
        testEncode("a", "YQ==");
        testEncode("b", "Yg==");
        testEncode("A", "QQ==");
        testEncode("B", "Qg==");
    }

    @Test
    public void testEncode2() throws IOException {
        testEncode("00", "MDA=");
        testEncode("01", "MDE=");
        testEncode("10", "MTA=");
        testEncode("11", "MTE=");
        testEncode("AA", "QUE=");
        testEncode("BB", "QkI=");
        testEncode("ZZ", "Wlo=");
        testEncode("aa", "YWE=");
        testEncode("bb", "YmI=");
        testEncode("zz", "eno=");
    }

    @Test
    public void testEncode3() throws IOException {
        testEncode("000", "MDAw");
        testEncode("111", "MTEx");
        testEncode("999", "OTk5");
        testEncode("AAA", "QUFB");
        testEncode("ZZZ", "Wlpa");
        testEncode("aaa", "YWFh");
        testEncode("zzz", "enp6");
    }

    @Test
    public void testEncode4() throws IOException {
        testEncode("Hallo Welt", "SGFsbG8gV2VsdA==");
        testEncode("Dies ist ein Test", "RGllcyBpc3QgZWluIFRlc3Q=");
    }

    @Test
    public void testDecode1() throws IOException {
        testDecode("0");
        testDecode("1");
        testDecode("9");
        testDecode("a");
        testDecode("b");
        testDecode("z");
        testDecode("A");
        testDecode("B");
        testDecode("Z");
    }

    @Test
    public void testDecode2() throws IOException {
        testDecode("00");
        testDecode("01");
        testDecode("11");
        testDecode("99");
        testDecode("aa");
        testDecode("ab");
        testDecode("bb");
        testDecode("zz");
        testDecode("AA");
        testDecode("AB");
        testDecode("BB");
        testDecode("ZZ");
    }

    @Test
    public void testDecode3() throws IOException {
        testDecode("000");
        testDecode("012");
        testDecode("111");
        testDecode("999");
        testDecode("aaa");
        testDecode("abc");
        testDecode("bbb");
        testDecode("zzz");
        testDecode("AAA");
        testDecode("ABC");
        testDecode("BBB");
        testDecode("ZZZ");
    }

    @Test
    public void testDecode4() throws IOException {
        testDecode("Hallo Welt");
        testDecode("Dies ist ein Test");
    }

    @Test
    public void testMultipart() throws IOException {
        char[] tmp = "SGFs\nbG8g\nV2Vs\ndA==\n".toCharArray();

        Base64StreamDecoder bd = new Base64StreamDecoder();
        Assert.assertEquals("Hallo Welt",
                new String(bd.decode(tmp, 0, 10), "US-ASCII")
                + new String(bd.decode(tmp, 10, tmp.length-10), "US-ASCII"));

        bd = new Base64StreamDecoder();
        Assert.assertEquals("Hallo Welt",
                new String(bd.decode(tmp, 0, 8), "US-ASCII")
                + new String(bd.decode(tmp, 8, tmp.length-8), "US-ASCII"));

        bd = new Base64StreamDecoder();
        Assert.assertEquals("Hallo Welt",
                new String(bd.decode(tmp, 0, 3), "US-ASCII")
                + new String(bd.decode(tmp, 3, tmp.length-3), "US-ASCII"));
    }

    @Test
    public void testMultiLine() throws IOException {
        Assert.assertEquals("Hallo Welt", new String(new Base64StreamDecoder().decode("SGFs\nbG8g\nV2Vs\ndA==\n"), "US-ASCII"));
        Assert.assertEquals("Dies ist ein Test", new String(new Base64StreamDecoder().decode("RGllcyBp\nc3QgZWlu\nIFRlc3Q=\n"), "US-ASCII"));
    }

    @Test
    public void testMultiLineIndent() throws IOException {
        Assert.assertEquals("Hallo Welt", new String(new Base64StreamDecoder().decode("    SGFs\n    bG8g\n    V2Vs\n    dA==\n"), "US-ASCII"));
        Assert.assertEquals("Dies ist ein Test", new String(new Base64StreamDecoder().decode("    RGllcyBp\n    c3QgZWlu\n    IFRlc3Q=\n"), "US-ASCII"));
    }

    @Test(expected = InvalidCharacterException.class)
    public void testInvalid() throws IOException {
        new Base64StreamDecoder().decode("SGFs\nbG8g\nV2Vs\nacb$");
    }

    @Test(expected = IncompleteStreamException.class)
    public void testIncomplete() throws IOException {
        new Base64StreamDecoder().decode("SGFs\nbG8g\nV2Vs\ndA");
    }

    @Test
    public void testCharacterName() {
        Assert.assertEquals("\\r", Base64Exception.characterName('\r'));
        Assert.assertEquals("\\n", Base64Exception.characterName('\n'));
        Assert.assertEquals("\\t", Base64Exception.characterName('\t'));
        Assert.assertEquals("\\f", Base64Exception.characterName('\f'));
        Assert.assertEquals("\\x01", Base64Exception.characterName(0x01));
        Assert.assertEquals("\\x02", Base64Exception.characterName(0x02));
        Assert.assertEquals("\\x16", Base64Exception.characterName(0x16));
        Assert.assertEquals("\\u1234", Base64Exception.characterName(0x1234));
    }
}
