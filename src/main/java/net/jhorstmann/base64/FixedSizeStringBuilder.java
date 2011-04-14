package net.jhorstmann.base64;

import java.io.IOException;

final class FixedSizeStringBuilder implements Appendable {

    private char[] array;
    private int index;

    FixedSizeStringBuilder(int length) {
        array = new char[length];
    }

    private void appendImpl(char c) {
        if (index >= array.length) {
            throw new IllegalStateException("Capacity (" + array.length + ") exceeded");
        }
        array[index++] = c;
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

    @Override
    public String toString() {
        return new String(array, 0, index);
    }
}
