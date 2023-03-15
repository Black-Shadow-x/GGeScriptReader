package opcodes.data;

public class DataInt implements Data {
    private int value;

    public DataInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
    public int getLength() {
        return 4;
    }

    @Override
    public String getType() {
        return "Dword";
    }

    @Override
    public void setValue(int consumedValue) {
        value = consumedValue;
    }
}
