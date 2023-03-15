package process;

import io.DataLoaded;
import javafx.scene.control.Alert;
import opcodes.OpCode;
import opcodes.OpCodes;
import opcodes.data.Data;
import opcodes.data.DataUnsignedShort;
import util.Utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

public class ScriptParser {
    private DataLoaded data;
    private OpCodes opCodes;

    public ScriptParser(DataLoaded data) {
        this.data = data;
        this.opCodes = new OpCodes();
    }

    public String getHexValuesString() {
        StringBuilder sb = new StringBuilder();
        int startPos = data.getPosition();
        int currentPos = startPos;
        try {
            while (currentPos < data.getData().limit()) {
                int hexValue = data.getUnsignedByte();
                OpCode opCode = opCodes.getOpCode(hexValue);

                if (opCode == null) {
                    sb.append(String.format("%02X", hexValue));
                    sb.append("\n");
                } else {
                    String opName = opCode.getOpName();
                    int length = opCode.getLength();

                    if (length == -1) {
                        // opcode has variable length, read length value from data
                        length = data.getUnsignedByte();
                        opName = opName.replace("{length}", Utils.getHexValueStr(length, 2, true));
                    }

                    sb.append(opName);

                    // Skip appending Data values for OpCode with name "Return"
                    if (!opName.equals("Return")) {
                        boolean hasData = opCode.dataList != null && opCode.dataList.size() > 0;
                        if (hasData) {
                            sb.append(" (");
                        }

                        // consume length of opcode based on Data objects
                        for (Data dataObj : opCode.dataList) {
                            int bytesToRead = dataObj.getLength();
                            sb.append(" ");
                            sb.append(dataObj.getType());
                            sb.append(" = ");
                            if (dataObj.getType().equals("Short")) {
                                // read short as two bytes
                                int consumedValue = data.getUnsignedShort();
                                sb.append(Utils.getHexValueStrLE(dataObj, 2, true));
                                dataObj.setValue(0, consumedValue);
                            } else if (dataObj.getType().equals("Byte")) {
                                // read byte
                                int consumedValue = data.getUnsignedByte();
                                sb.append(String.format("%02X", consumedValue));
                                dataObj.setValue(0, consumedValue);
                            } else if (dataObj.getType().equals("Dword")) {
                                // read dword as four bytes
                                int consumedValue = data.getInt();
                                sb.append(Utils.getHexValueStrLE(dataObj, 4, true));
                                dataObj.setValue(0, consumedValue);
                            } else if (dataObj.getType().equals("String")) {
                                // read string
                                data.setPosition(data.getPosition()-1);

                                StringBuilder sbValue = new StringBuilder();
                                byte[] bytes = new byte[1];
                                boolean keepReading = true;
                                while (keepReading && data.getPosition() < data.getData().limit()) {
                                    int readByte = data.getUnsignedByte();
                                    bytes[0] = (byte) readByte;
                                    String character = new String(bytes, StandardCharsets.UTF_8);
                                    if (readByte == 0x00) {
                                        keepReading = false;
                                    } else {
                                        sbValue.append(character);
                                    }
                                }
                                String consumedValue = sbValue.toString();
                                sb.append(consumedValue);
                                dataObj.setValue(0, consumedValue);

                            } else if (dataObj.getType().equals("J_String")) {
                                // read Japanese string
                                data.setPosition(data.getPosition() - 1);

                                StringBuilder sbValue = new StringBuilder();
                                CharsetDecoder decoder = Charset.forName("Shift_JIS").newDecoder();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                CharBuffer charBuffer = CharBuffer.allocate(1);

                                boolean keepReading = true;
                                while (keepReading && data.getPosition() < data.getData().limit()) {
                                    int readByte = data.getUnsignedByte();
                                    byteBuffer.put((byte) readByte);
                                    byteBuffer.flip();
                                    decoder.decode(byteBuffer, charBuffer, false);
                                    byteBuffer.compact();
                                    charBuffer.flip();
                                    String character = charBuffer.toString();
                                    charBuffer.clear();
                                    if (readByte == 0x00) {
                                        keepReading = false;
                                    } else {
                                        sbValue.append(character);
                                    }
                                }

                                String consumedValue = sbValue.toString();
                                sb.append(consumedValue);
                                dataObj.setValue(0, consumedValue);
                            }
                            sb.append(",");
                        }
                        // Remove trailing comma and add closing parenthesis
                        if (hasData) {
                            sb.deleteCharAt(sb.length()-1);
                            sb.append(" )");
                        }
                    } else {
                        sb.append("\n");
                    }

                    sb.append("\n");
                }

                currentPos = data.getPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error processing opcodes");
        }
        data.setPosition(startPos);
        return sb.toString();
    }

    public void showAlert(String message) {
        // show an alert with the error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public DataLoaded getData() {
        return data;
    }

    public void setData(DataLoaded data) {
        this.data = data;
    }

}
