package opcodes.opcodetexts;

import java.io.File;

public class OpCodeTextFiles {
    private String gameName;
    private String[] fileFormat;
    private String version;
    private String author;

    private File file;

    public OpCodeTextFiles(String gameName, String[] fileFormat, String version, String author, File file) {
        this.gameName = gameName;
        this.fileFormat = fileFormat;
        this.version = version;
        this.author = author;
        this.file = file;

    }


    public String getGameName() {
        return gameName;
    }

    public String[] getFileFormat() {
        return fileFormat;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public File getFile() {
        return file;
    }
}
