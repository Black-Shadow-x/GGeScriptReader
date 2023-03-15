package opcodes.data;

public class DataUnsignedShort implements Data {
    private int value;

    public DataUnsignedShort(int value) {
        this.value = value & 0xFFFF;
    }

    public int getValue() {
        return value & 0xFFFF;
    }

    @Override
    public int getLength() {
        return 2;
    }

    @Override
    public String getType() {
        return "Short";
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

