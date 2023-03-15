package io;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScriptReader {
    private final File file;
    private ByteBuffer buffer;

    public ScriptReader(File file) {
        this.file = file;
        this.buffer = null;
    }

    public ByteBuffer getByteBuffer() {
        if (buffer == null) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                FileChannel channel = inputStream.getChannel();
                buffer = ByteBuffer.allocate((int) channel.size());
                channel.read(buffer);
                buffer.flip();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error reading file");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        return buffer;
    }
}
