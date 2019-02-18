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

    void create(File file) throws IOException {
        if (file.exists()) {
            throw new FileAlreadyExistsException("File already exists");
        } else {
            file.createNewFile();
        }
    }

    void delete(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can't delete file that doesn't exist");
        }
        file.delete();
    }

    boolean exists(File file) {
        return file.exists();
    }

    boolean isEmpty(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can't find the file");
        }
        return (file.length() == 0);
    }

    void clear(File file) throws IOException {
        this.delete(file);
        this.create(file);
    }

    void writeLine(File file, String line) throws IOException {
        if (!file.exists()) {
            this.create(file);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            if (!this.isEmpty(file)) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(line);
        }
    }

    List<String> readLinesFromFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can't read lines, haven't found the file");
        }
        List<String> fileLines;
        try (Stream<String> lines = Files.lines(file.toPath())) {
            fileLines = lines.collect(Collectors.toList());
        }
        return fileLines;
    }

    String readLastLine(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can't read last line, haven't found the file");
        }
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(file)) {
            return reader.readLine();
        }
    }

    void removeLine(File file, int lineNumber) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Can't delete line, haven't found the file");
        }
        File newFile = new File((file.getParent() + "tmpFile.txt"));
        transferFilteredContent(lineNumber, file, newFile);
        replaceOriginFile(file, newFile);
    }

    private void transferFilteredContent(int lineNumber, File originFile, File newFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(originFile))) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile, true))) {
                String line;
                int currentLineNumber = 0;
                boolean firstLine = true;
                while ((line = br.readLine()) != null) {
                    currentLineNumber++;
                    if (currentLineNumber != lineNumber) {
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

    private void replaceOriginFile(File originFile, File newFile) {
        originFile.delete();
        newFile.renameTo(originFile);
    }
}
