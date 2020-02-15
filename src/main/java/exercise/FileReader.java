package exercise;

import java.io.InputStream;

public class FileReader {

    public InputStream getFileAsStream(String path) {
        return getClass()
                .getClassLoader().getResourceAsStream(path);
    }
}
