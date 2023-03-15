package opcodes.data;

public class DataString implements Data {
    private String value;

    public DataString(String value) {
        this.value = value;
    }

    public int getValue() {
        return -1;
    }

    @Override
    public int getLength() {
        return value.length();
    }

    @Override
    public String getType() {
        return "String";
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void setValue(int i, int consumedValue) {}

    @Override
    public void setValue(int i, String consumedString) {
        value = consumedString;
    }

    @Override
    public void setValue(int consumedValue) {}
}
