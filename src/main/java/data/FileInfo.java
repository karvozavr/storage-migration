package data;

import java.nio.file.Path;

public class FileInfo {

    public FileInfo(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    private String name;
    private Path path;

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }
}
