package opcodes.data;

public interface Data {
    int getLength();

    String getType();

    int getValue();

    String toString();

    void setValue(int i, int consumedValue);
    void setValue(int i, String consumedString);

    void setValue(int consumedValue);
}
