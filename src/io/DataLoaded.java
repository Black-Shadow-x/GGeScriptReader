package io;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataLoaded {
    private ByteBuffer data;

    public DataLoaded(ByteBuffer data) {
        this.data = data;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public int getInt() {
        return data.getInt();
    }

    public void putInt(int value) {
        data.putInt(value);
    }

    public long getLong() {
        return data.getLong();
    }

    public void putLong(long value) {
        data.putLong(value);
    }

    public short getUnsignedByte() {
        return (short) (data.get() & 0xff);
    }

    public void putUnsignedByte(short value) {
        data.put((byte) (value & 0xff));
    }

    public int getUnsignedShort() {
        return data.getShort() & 0xffff;
    }

    public void putUnsignedShort(int value) {
        data.putShort((short) (value & 0xffff));
    }

    public void setPosition(int position) {
        data.position(position);
    }

    public int getPosition() {
        return data.position();
    }
}
