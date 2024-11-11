package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static store.constants.UtilContants.END_LINE;

public class FileReader {

    public List<String> read(String path) {
        Path filePath = Paths.get(path);
        String fileContent = getFileContent(filePath);
        return parseFile(fileContent);
    }

    private String getFileContent(Path filePath) {
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("[ERROR]");
        }
    }

    private List<String> parseFile(String content) {
        return new ArrayList<>(List.of(content.split(END_LINE)));
    }
}
