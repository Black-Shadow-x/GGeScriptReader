package opcodes;

import opcodes.data.*;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public class OpCode {
    private int hexCode;
    private int length; //This is the total length of bytes read for this opcode
    private int variables;
    private String opName       = "{unknown}";

    public List<Data> dataList = new ArrayList<>();

    public OpCode(int hexCode, int length, String opName) {
        this.hexCode    = hexCode;
        this.length     = length;
        this.opName     = opName;

        if (opName.isEmpty()){
            this.opName = "cmd_0x" + Integer.toHexString(hexCode);
        }
    }

    public OpCode(int hexCode,String opName, String variablePattern) {
        this.length     = 1;
        this.hexCode    = hexCode;
        this.opName     = opName;
        addDataToList(this.dataList, variablePattern);
        if (opName.isEmpty()){
            this.opName = "cmd_0x" + Integer.toHexString(hexCode);
        }

    }

    public OpCode(int hexCode, String opName){
        if (opName.isEmpty()){
            this.opName = "cmd_0x" + Integer.toHexString(hexCode);
        }
        new OpCode(hexCode, 1, opName);
    }

    public OpCode(int hexCode, int length){
        String sb = "{cmd_" +
                Utils.getHexValueStr(hexCode, 2, true) +
                "}";

        new OpCode(hexCode, length, sb);
    }

    public void addDataToList(List<Data> dataList, String variablePattern) {
        for (char c : variablePattern.toCharArray()) {
            switch (c) {
                case 'b':
                    length += 1;
                    dataList.add(new DataUnsignedByte(0));
                    break;
                case 's':
                    length += 2;
                    dataList.add(new DataUnsignedShort(0));
                    break;
                case 'l':
                    length += 4;
                    dataList.add(new DataInt(0));
                    break;
                case 'e':
                    length = -1;
                    dataList.add(new DataString(""));
                    break;
                case 'j':
                    length = -1;
                    dataList.add(new DataJapaneseString(""));
                    break;
            }
        }
    }

    public Data getData(int index) {
        if (index < 0 || index >= dataList.size()) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        return dataList.get(index);
    }

    public String getOpName() {
        return opName;
    }

    public int getLength() {
        return length;
    }
}
