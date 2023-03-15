package util;

import opcodes.data.Data;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {

    /**
     * Returns a string representation of the given hexadecimal value, with optional zero-padding and a prefix.
     * @param hex the hexadecimal value to convert to a string
     * @param length the desired length of the resulting string, with leading zeros if necessary
     * @param prepend whether to prepend the string with "0x"
     * @return the string representation of the hexadecimal value
     */
    public static String getHexValueStr(int hex, int length, boolean prepend){
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(hex).toUpperCase());
        while (sb.length()> length){
            sb.insert(0, "0");
        }
        if (prepend){
            sb.insert(0, "0x");
        }
        return sb.toString();
    }

    public static String getHexValueStrLE(Data data, int length, boolean withPrefix) {
        // Get the little-endian byte order representation of the data value

        // Convert the bytes to a hex string with leading zeros
        String hexString = String.format("%0" + (length * 2) + "X", new BigInteger(Integer.toHexString(data.getValue()), 16));

        // Add the "0x" prefix if requested
        if (withPrefix) {
            hexString = "0x" + hexString;
        }

        return hexString;
    }
}
