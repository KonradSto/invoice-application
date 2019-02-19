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
        new FileHelper(resultFilePath).create();

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
        Exception ex = assertThrows(FileAlreadyExistsException.class, () -> new FileHelper(resultFilePath).create());
        assertEquals("File already exists", ex.getMessage());
        assertTrue(alreadyExistingFile.delete());
    }

    @Test
    void shouldDeleteFile() throws IOException {
        //Given
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line"));

        //When
        new FileHelper(resultFilePath).delete();

        //Then
        assertFalse(resultFile.exists());
    }

    @Test
    void shouldThrowExceptionForDeleteMethod() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).delete());
        assertEquals("Can't delete file that doesn't exist", expected.getMessage());
    }

    @Test
    void shouldReturnTrueWhenExists() throws IOException {
        //Given
        resultFile.createNewFile();

        //Then
        assertTrue(new FileHelper(resultFilePath).exists());
    }

    @Test
    void shouldReturnFalseWhenExists() {
        assertFalse(new FileHelper(resultFilePath).exists());
    }

    @Test
    void shouldReturnTrueWhenFileIsEmpty() throws IOException {
        //Given
        resultFile.createNewFile();

        //Then
        assertTrue(new FileHelper(resultFilePath).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
        //Given
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());

        //Then
        assertFalse(new FileHelper(resultFilePath).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhileCheckingIfNonExistingFileIsEmpty() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).isEmpty());
        assertEquals("Can't verify the file content, haven't found the file", expected.getMessage());
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
        new FileHelper(resultFilePath).clear();

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
    void shouldAddLineInNotEmptyFile() throws IOException {
        //Given
        expectedFile.createNewFile();
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());
        Files.write(Paths.get(expectedFilePath), Arrays.asList("first test line", "second test line"));
        List<String> expectedFileLines;
        List<String> resultFileLines;

        //When
        new FileHelper(resultFilePath).writeLine("second test line");

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
    void shouldThrowExceptionWhileWritingToNonExistingFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).writeLine("test line"));
        assertEquals("Can't write given line, haven't found the file", expected.getMessage());
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
        List<String> resultFileList = new FileHelper(resultFilePath).readLinesFromFile();

        //Then
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldThrowExceptionWhileReadingLinesForNotExistingFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).readLinesFromFile());
        assertEquals("Can't read lines, haven't found the file", expected.getMessage());
    }

    @Test
    void shouldReadLeadLastLineFromFile() throws IOException {
        //Given
        resultFile.createNewFile();
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line", "third test line"));

        String expectedString = "third test line";

        //When
        String resultString = new FileHelper(resultFilePath).readLastLine();

        //Then
        assertEquals(expectedString, resultString);
    }

    @Test
    void shouldThrowExceptionForReadLastLineFromFile() {
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).readLastLine());
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
        new FileHelper(resultFilePath).removeLine(3);

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
        Exception expected = assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).removeLine(2));
        assertEquals("Can't delete line, haven't found the file", expected.getMessage());
    }
}
