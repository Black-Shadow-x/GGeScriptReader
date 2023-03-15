package opcodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OpCodes {
    private final Map<Integer, OpCode> opCodes;

    public OpCodes() {
        opCodes = new HashMap<>();

        setUpOpCodes();
    }
    public OpCodes(File inputFile) {
        opCodes = new HashMap<>();

        setUpOpCodes(inputFile);
    }


    private void setUpOpCodes() {
        opCodes.clear();
        Set<String> ignorePrefixes = new HashSet<>(Arrays.asList("#", "/", "GameName", "FileFormats", "Version", "Author"));
        try (BufferedReader reader = new BufferedReader(new FileReader("opcodes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() ||
                        ignorePrefixes.stream().anyMatch(trimmedLine::startsWith))
                    continue;

                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",\\s*");
                    int opcodeValue = Integer.parseInt(parts[0].substring(2), 16);
                    String opcodeName = parts[1].replaceAll("\"", "").trim();
                    String operandTypes = parts[2].replaceAll("\"", "").trim();

                    opCodes.put(opcodeValue, new OpCode(opcodeValue, opcodeName, operandTypes));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setUpOpCodes(File opCodeTxt) {
        opCodes.clear();

        Set<String> ignorePrefixes = new HashSet<>(Arrays.asList("#", "/", "GameName", "FileFormats", "Version", "Author"));
        try (BufferedReader reader = new BufferedReader(new FileReader(opCodeTxt))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() ||
                        ignorePrefixes.stream().anyMatch(trimmedLine::startsWith))
                    continue;

                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",\\s*");
                    int opcodeValue = Integer.parseInt(parts[0].substring(2), 16);
                    String opcodeName = parts[1].replaceAll("\"", "").trim();
                    String operandTypes = parts[2].replaceAll("\"", "").trim();

                    opCodes.put(opcodeValue, new OpCode(opcodeValue, opcodeName, operandTypes));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OpCode getOpCode(int hexCode){
        return opCodes.get(hexCode);
    }
}
