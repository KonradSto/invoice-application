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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileHelperTest {

    private static final String resultFilePath = "src/test/resources/resultFile.txt";
    private File resultFile;

    @BeforeEach
    void initializeTestFile() {
        resultFile = new File(resultFilePath);
    }

    @AfterEach
    void deleteTestFile() {
        if (resultFile.exists()) {
            assertTrue(resultFile.delete());
        }
    }

    @Test
    void shouldCreateEmptyFile() throws IOException {
        //Given
        List<String> expectedFileLines = new ArrayList<>();

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
        assertTrue(alreadyExistingFile.createNewFile());
        assertThrows(FileAlreadyExistsException.class, () -> new FileHelper(resultFilePath).create());
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
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).delete());
    }

    @Test
    void shouldReturnTrueWhenExists() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());

        //Then
        assertTrue(new FileHelper(resultFilePath).exists());
    }

    @Test
    void shouldReturnFalseWhenFileDoNotExists() {
        assertFalse(new FileHelper(resultFilePath).exists());
    }

    @Test
    void shouldReturnTrueWhenFileIsEmpty() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());

        //Then
        assertTrue(new FileHelper(resultFilePath).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenFileIsNotEmpty() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());

        //Then
        assertFalse(new FileHelper(resultFilePath).isEmpty());
    }

    @Test
    void shouldThrowExceptionWhileCheckingIfNonExistingFileIsEmpty() {
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).isEmpty());
    }

    @Test
    void shouldClearWholeFile() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());
        List<String> resultFileLines;
        List<String> expectedFileLines = new ArrayList<>();

        //When
        new FileHelper(resultFilePath).clear();

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(resultFileLines, expectedFileLines);
    }

    @Test
    void shouldAddLineInNotEmptyFile() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
        Files.write(Paths.get(resultFilePath), "first test line".getBytes());
        List<String> expectedFileLines = Arrays.asList("first test line", "second test line");
        List<String> resultFileLines;

        //When
        new FileHelper(resultFilePath).writeLine("second test line");

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldThrowExceptionWhileWritingToNonExistingFile() {
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).writeLine("test line"));
    }

    @Test
    void shouldReadLinesFromFile() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
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
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).readLinesFromFile());
    }

    @Test
    void shouldReadLeadLastLineFromFile() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line", "third test line"));

        String expectedString = "third test line";

        //When
        String resultString = new FileHelper(resultFilePath).readLastLine();

        //Then
        assertEquals(expectedString, resultString);
    }

    @Test
    void shouldThrowExceptionForReadLastLineFromFile() {
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).readLastLine());
    }

    @Test
    void shouldRemoveLineFromFile() throws IOException {
        //Given
        assertTrue(resultFile.createNewFile());
        Files.write(Paths.get(resultFilePath), Arrays.asList("first test line", "second test line", "third test line", "fourth test line"));
        List<String> expectedFileLines = Arrays.asList("first test line", "second test line", "fourth test line");
        List<String> resultFileLines;

        //When
        new FileHelper(resultFilePath).removeLine(3);

        //Then
        try (Stream<String> lines = Files.lines(Paths.get(resultFilePath))) {
            resultFileLines = lines.collect(Collectors.toList());
        }
        assertEquals(expectedFileLines, resultFileLines);
    }

    @Test
    void shouldThrowExceptionForRemoveLineFromNonExistentFile() {
        assertThrows(FileNotFoundException.class, () -> new FileHelper(resultFilePath).removeLine(2));
    }
}
