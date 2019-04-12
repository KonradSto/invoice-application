package pl.coderstrust.database.file;

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
import pl.coderstrust.configuration.InFileDatabaseProperties;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Component
public class FileHelper {
    private static final String EMPTY_STRING = "";
    private File file;

    public FileHelper(InFileDatabaseProperties properties) {
        this.file = new File(properties.getFilePath());
    }

    public void create() throws IOException {
        if (this.file.exists()) {
            validateFileExistance("Failed to create new file");
        }
        Files.createFile(this.file.toPath());
    }

    public void delete() throws IOException {
        validateFileExistance("Failed to delete file");
        Files.delete(this.file.toPath());
    }

    public boolean isExist() {
        return this.file.exists();
    }

    public boolean isEmpty() throws IOException {
        validateFileExistance("Failed to check file content");
        return (this.file.length() == 0);
    }

    public void clear() throws IOException {
        validateFileExistance("Failed to clear the file content");
        writeToFile(false, EMPTY_STRING);
    }

    public void writeLine(String line) throws IOException {
        validateFileExistance("Failed to write given line");
        writeToFile(true, line);
    }

    public List<String> readLinesFromFile() throws IOException {
        validateFileExistance("Failed to read lines");
        List<String> fileLines;
        try (Stream<String> lines = Files.lines(this.file.toPath())) {
            fileLines = lines.collect(Collectors.toList());
        }
        return fileLines;
    }

    public String readLastLine() throws IOException {
        validateFileExistance("Failed to read last line");
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(this.file)) {
            return reader.readLine();
        }
    }

    public void removeLine(int lineNumber) throws IOException {
        validateFileExistance("Failed to delete line");
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

    private void validateFileExistance(String errorMessage) throws FileNotFoundException {
        if (!this.file.exists()) {
            throw new FileNotFoundException(String.format("%s. File is not exist. Path to file=[%s]", errorMessage, this.file.getAbsoluteFile()));
        }
    }

    private void writeToFile(boolean append, String content) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.file, append))) {
            if (!this.isEmpty()) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(content);
        }
    }
}
