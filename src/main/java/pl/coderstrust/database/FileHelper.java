package pl.coderstrust.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.input.ReversedLinesFileReader;

class FileHelper {

    private File file;

    FileHelper(String filePath) {
        this.file = new File(filePath);
    }

    void create() throws IOException {
        if (this.file.exists()) {
            throw new FileAlreadyExistsException("File already exists");
        } else {
            this.file.createNewFile();
        }
    }

    void delete() throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't delete file that doesn't exist");
        }
        this.file.delete();
    }

    boolean exists() {
        return this.file.exists();
    }

    boolean isEmpty() throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't verify the file content, haven't found the file");
        }
        return (this.file.length() == 0);
    }

    void clear() throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't clear the file content, haven't found the file");
        }
        this.delete();
        this.create();
    }

    void writeLine(String line) throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't write given line, haven't found the file");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.file, true))) {
            if (!this.isEmpty()) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(line);
        }
    }

    List<String> readLinesFromFile() throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't read lines, haven't found the file");
        }
        List<String> fileLines;
        try (Stream<String> lines = Files.lines(this.file.toPath())) {
            fileLines = lines.collect(Collectors.toList());
        }
        return fileLines;
    }

    String readLastLine() throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't read last line, haven't found the file");
        }
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(this.file)) {
            return reader.readLine();
        }
    }

    void removeLine(int lineNumber) throws IOException {
        if (!this.file.exists()) {
            throw new FileNotFoundException("Can't delete line, haven't found the file");
        }
        File newFile = new File((this.file.getParent() + "tmpFile.txt"));
        transferRemainingFileContent(lineNumber, newFile);
        moveFile(newFile);
    }

    private void transferRemainingFileContent(int lineNumberToErase, File newFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile, true))) {
                String line;
                int currentLineNumber = 0;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    currentLineNumber++;
                    if (currentLineNumber != lineNumberToErase) {
                        if (!firstLine) {
                            bw.newLine();
                        }
                        bw.write(line);
                        firstLine = false;
                    }
                }
            }
        }
    }

    private void moveFile(File newFile) {
        this.file.delete();
        newFile.renameTo(this.file);
    }
}
