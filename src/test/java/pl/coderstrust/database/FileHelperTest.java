package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class FileHelperTest {

    private static final String resultFilePath = "src/test/resources/resultFile.txt";
    private static final String expectedFilePath = "src/test/resources/expectedFile.txt";
    private File resultFile = new File(resultFilePath);
    private File expectedFile = new File(expectedFilePath);

    @AfterEach
    void deleteTestFile() {
        if (resultFile.exists()) {
            resultFile.delete();
        }
        if (expectedFile.exists()) {
            expectedFile.delete();
        }
    }

    @Test
    void shouldCreateEmptyFile() throws IOException {
        //Given
        expectedFile = new File(expectedFilePath);
        expectedFile.createNewFile();
        List<String> expectedFileLines;
        try (Stream<String> lines = Files.lines(Paths.get(expectedFilePath))) {
            expectedFileLines = lines.collect(Collectors.toList());
        }

        //When
        new FileHelper().create(resultFile);

        //Then
        List<String> resultFileLines;
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldThrowExceptionForCreateMethod() throws IOException {
        File alreadyExistingFile = new File(resultFilePath);
        alreadyExistingFile.createNewFile();
        Exception ex = assertThrows(FileAlreadyExistsException.class, () -> new FileHelper().create(resultFile));
        assertEquals("File already exists", ex.getMessage());
        assertTrue(alreadyExistingFile.delete());
    }

    @Test
    void shouldDeleteFile() throws IOException {
        //Given
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line"));

        //When
        new FileHelper().delete(resultFile);

        //Then
        assertFalse(resultFile.exists());
    }

    @Test
    void shouldThrowExceptionForDeleteMethod() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper().delete(resultFile));
        assertEquals("Can't delete file that doesn't exist", expected.getMessage());
    }

    @Test
    void shouldReturnTrueWhenExists() throws IOException {
        //Given
        resultFile.createNewFile();

        //Then
        assertTrue(new FileHelper().exists(resultFile));
    }

    @Test
    void shouldReturnFalseWhenExists() {
        assertFalse(new FileHelper().exists(resultFile));
    }

    @Test
    void shouldReturnTrueWhenFileIsEmpty() throws IOException {
        //Given
        resultFile.createNewFile();

        //Then
        assertTrue(new FileHelper().isEmpty(resultFile));
    }

    @Test
    void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
        //Given
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());

        //Then
        assertFalse(new FileHelper().isEmpty(resultFile));
    }

    @Test
    void shouldThrowExceptionWhileCheckingIfNonExistingFileIsEmpty() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper().isEmpty(resultFile));
        assertEquals("Can't find the file", expected.getMessage());
    }

    @Test
    void shouldClearWholeFile() throws IOException {
        //Given
        expectedFile = new File(expectedFilePath);
        expectedFile.createNewFile();
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());
        List<String> resultFileLines;
        List<String> expectedFileLines;

        //When
        new FileHelper().clear(resultFile);

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        try (Stream<String> lines = Files.lines(Paths.get(expectedFilePath))) {
            expectedFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(resultFileLines, expectedFileLines);
    }

    @Test
    void shouldWriteLineWhenFileDoNotExist() throws IOException {
        //Given
        expectedFile.createNewFile();
        Files.write(Paths.get(expectedFilePath), "test line".getBytes());
        List<String> expectedFileLines;
        List<String> resultFileLines;

        //When
        new FileHelper().writeLine(resultFile, "test line");

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        try (Stream<String> lines = Files.lines(Paths.get(expectedFilePath))) {
            expectedFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldAddLineInNotEmptyFile() throws IOException {
        //Given
        expectedFile.createNewFile();
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());
        Files.write(Paths.get(expectedFilePath), Arrays.asList("first test line", "second test line"));
        List<String> expectedFileLines;
        List<String> resultFileLines;

        //When
        new FileHelper().writeLine(resultFile, "second test line");

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        try (Stream<String> lines = Files.lines(Paths.get(expectedFilePath))) {
            expectedFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldReadLinesFromFile() throws IOException {
        //Given
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line"));
        List<String> expectedFileList = new ArrayList<>();
        expectedFileList.add("first test line");
        expectedFileList.add("second test line");

        //When
        List<String> resultFileList = new FileHelper().readLinesFromFile(resultFile);

        //Then
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldThrowExceptionWhileReadingLinesForNotExistingFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper().readLinesFromFile(resultFile));
        assertEquals("Can't read lines, haven't found the file", expected.getMessage());
    }

    @Test
    void shouldReadLeadLastLineFromFile() throws IOException {
        //Given
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line", "third test line"));

        String expectedString = "third test line";

        //When
        String resultString = new FileHelper().readLastLine(resultFile);

        //Then
        assertEquals(expectedString, resultString);
    }

    @Test
    void shouldThrowExceptionForReadLastLineFromFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper().readLastLine(resultFile));
        assertEquals("Can't read last line, haven't found the file", expected.getMessage());
    }

    @Test
    void shouldRemoveLineFromFile() throws IOException {
        //Given
        expectedFile.createNewFile();
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line", "third test line", "fourth test line"));
        Files.write(Paths.get(expectedFilePath), Arrays.asList("first test line", "second test line", "fourth test line"));
        List<String> expectedFileLines;
        List<String> resultFileLines;

        //When
        new FileHelper().removeLine(resultFile, 3);

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        try (Stream<String> lines = Files.lines(Paths.get(expectedFilePath))) {
            expectedFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldThrowExceptionForRemoveLineFromNonExistentFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper().removeLine(resultFile, 2));
        assertEquals("Can't delete line, haven't found the file", expected.getMessage());
    }
}
