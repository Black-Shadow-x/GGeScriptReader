package opcodes.data;

public class DataUnsignedByte implements Data {
    private int value;

    public DataUnsignedByte(int value) {
        this.value = value & 0xff;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public String getType() {
        return "Byte";
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public void setValue(int i, int consumedValue) {
        value = consumedValue;
    }

    @Override
    public void setValue(int i, String consumedString) {

    }

    @Override
    public void setValue(int consumedValue) {
        value = consumedValue;
    }

}
