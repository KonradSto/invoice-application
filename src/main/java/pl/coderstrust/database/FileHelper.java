package pl.coderstrust.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Component
public class FileHelper {

    private File file;

    public FileHelper(String filePath) {
        this.file = new File(filePath);
    }

    void create() throws IOException {
        if (this.file.exists()) {
            validateFileExistence("Failed to create new file");
        }
        Files.createFile(this.file.toPath());
    }

    void delete() throws IOException {
        validateFileExistence("Failed to delete file");
        Files.delete(this.file.toPath());
    }

    boolean exists() {
        return this.file.exists();
    }

    boolean isEmpty() throws IOException {
        validateFileExistence("Failed to check file content");
        return (this.file.length() == 0);
    }

    void clear() throws IOException {
        validateFileExistence("Failed to clear the file content");
        this.delete();
        this.create();
    }

    void writeLine(String line) throws IOException {
        validateFileExistence("Failed to write given line");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.file, true))) {
            if (!this.isEmpty()) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(line);
        }
    }

    List<String> readLinesFromFile() throws IOException {
        validateFileExistence("Failed to read lines");
        List<String> fileLines;
        try (Stream<String> lines = Files.lines(this.file.toPath())) {
            fileLines = lines.collect(Collectors.toList());
        }
        return fileLines;
    }

    String readLastLine() throws IOException {
        validateFileExistence("Failed to read last line");
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(this.file)) {
            return reader.readLine();
        }
    }

    void removeLine(int lineNumber) throws IOException {
        validateFileExistence("Failed to delete line");
        File newFile = new File((this.file.getParent() + "temporaryFile.txt"));
        transferRemainingFileContent(lineNumber, newFile);
        moveFile(newFile);
    }

    private void transferRemainingFileContent(int lineNumberToErase, File newFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true))) {
                String line;
                int currentLineNumber = 0;
                boolean firstLine = true;
                while ((line = reader.readLine()) != null) {
                    currentLineNumber++;
                    if (currentLineNumber != lineNumberToErase) {
                        if (!firstLine) {
                            writer.newLine();
                        }
                        writer.write(line);
                        firstLine = false;
                    }
                }
            }
        }
    }

    private void moveFile(File newFile) throws IOException {
        Files.delete(this.file.toPath());
        newFile.renameTo(this.file);
    }

    private void validateFileExistence(String errorMessage) throws FileNotFoundException {
        if (!this.file.exists()) {
            throw new FileNotFoundException(String.format("%s. File is not exist. Path to file=[%s]", errorMessage, this.file.getAbsoluteFile()));
        }
    }
}
