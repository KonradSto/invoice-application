package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class FileHelperTest {

    private final String resultFilePath = "src/test/resources/resultFile.txt";
    private final String expectedFilePath = "src/test/resources/expectedFile.txt";
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
        List<String> expectedFileList = new ArrayList<>();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedFile));
        while ((line = bufferedReader.readLine()) != null) {
            expectedFileList.add(line);
        }
        bufferedReader.close();

        //When
        new FileHelper().create(resultFile);

        //Then
        List<String> resultFileList = new ArrayList<>();
        bufferedReader = new BufferedReader(new FileReader(resultFile));
        while ((line = bufferedReader.readLine()) != null) {
            resultFileList.add(line);
        }
        bufferedReader.close();
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldThrowExceptionForCreateMethod() throws IOException {
        File alreadyExistingFile = new File(resultFilePath);
        alreadyExistingFile.createNewFile();
        try {
            new FileHelper().create(resultFile);
        } catch (FileAlreadyExistsException expected) {
            assertEquals("File already exists", expected.getMessage());
        }
        assertTrue(alreadyExistingFile.delete());
    }

    @Test
    void shouldDeleteFile() throws IOException {
        //Given
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("first test line\nsecond test line");
        }

        //When
        new FileHelper().delete(resultFile);

        //Then
        assertFalse(resultFile.exists());
    }

    @Test
    void shouldThrowExceptionForDeleteMethod() throws IOException {
        try {
            new FileHelper().delete(resultFile);
        } catch (FileNotFoundException expected) {
            assertEquals("Can't delete file that doesn't exist", expected.getMessage());
        }
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
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("test line");
        }

        //Then
        assertFalse(new FileHelper().isEmpty(resultFile));
    }

    @Test
    void shouldThrowExceptionForIsEmptyMethod() throws IOException {
        try {
            new FileHelper().isEmpty(resultFile);
        } catch (FileNotFoundException expected) {
            assertEquals("Can't find the file", expected.getMessage());
        }
    }

    @Test
    void shouldClearWholeFile() throws IOException {
        //Given
        expectedFile = new File(expectedFilePath);
        expectedFile.createNewFile();
        resultFile.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("test line");
        }
        List<String> expectedFileList = new ArrayList<>();
        List<String> resultFileList = new ArrayList<>();
        String line;

        //When
        new FileHelper().clear(resultFile);

        //Then
        BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedFile));
        while ((line = bufferedReader.readLine()) != null) {
            expectedFileList.add(line);
        }
        bufferedReader.close();
        bufferedReader = new BufferedReader(new FileReader(resultFile));
        while ((line = bufferedReader.readLine()) != null) {
            resultFileList.add(line);
        }
        bufferedReader.close();
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldWriteLineWhenFileDoNotExist() throws IOException {
        //Given
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(expectedFile))) {
            bufferedWriter.write("test line");
        }
        List<String> expectedList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        String line;

        //When
        new FileHelper().writeLine(resultFile, "test line");

        //Then
        BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedFile));
        while ((line = bufferedReader.readLine()) != null) {
            expectedList.add(line);
        }
        bufferedReader.close();
        bufferedReader = new BufferedReader(new FileReader(resultFile));
        while ((line = bufferedReader.readLine()) != null) {
            resultList.add(line);
        }
        bufferedReader.close();
        assertEquals(expectedList, resultList);
    }

    @Test
    void shouldAddLineInNotEmptyFile() throws IOException {
        //Given
        expectedFile.createNewFile();
        resultFile.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("first test line");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(expectedFile))) {
            bufferedWriter.write("first test line\nsecond test line");
        }
        List<String> expectedFileList = new ArrayList<>();
        List<String> resultFileList = new ArrayList<>();
        String line;

        //When
        new FileHelper().writeLine(resultFile, "second test line");

        //Then
        BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedFile));
        while ((line = bufferedReader.readLine()) != null) {
            expectedFileList.add(line);
        }
        bufferedReader.close();
        bufferedReader = new BufferedReader(new FileReader(resultFile));
        while ((line = bufferedReader.readLine()) != null) {
            resultFileList.add(line);
        }
        bufferedReader.close();
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldReadLinesFromFile() throws IOException {
        //Given
        resultFile.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("first test line\nsecond test line");
        }
        List<String> expectedFileList = new ArrayList<>();
        expectedFileList.add("first test line");
        expectedFileList.add("second test line");

        //When
        List<String> resultFileList = new FileHelper().readLinesFromFile(resultFile);

        //Then
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldThrowExceptionForReadLinesFromFile() throws IOException {
        try {
            new FileHelper().readLinesFromFile(resultFile);
        } catch (FileNotFoundException expected) {
            assertEquals("Can't read lines, haven't found the file", expected.getMessage());
        }
    }

    @Test
    void shouldReadLeadLastLineFromFile() throws IOException {
        //Given
        resultFile.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("first test line\nsecond test line\nthird test line");
        }
        String expectedString = "third test line";

        //When
        String resultString = new FileHelper().readLastLine(resultFile);

        //Then
        assertEquals(expectedString, resultString);
    }

    @Test
    void shouldThrowExceptionForReadLastLineFromFile() throws IOException {
        try {
            new FileHelper().readLastLine(resultFile);
        } catch (FileNotFoundException expected) {
            assertEquals("Can't read last line, haven't found the file", expected.getMessage());
        }
    }

    @Test
    void shouldRemoveLineFromFile() throws IOException {
        //Given
        expectedFile.createNewFile();
        resultFile.createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFile))) {
            bufferedWriter.write("first test line\nsecond test line\nthird test line\nfourth test line");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(expectedFile))) {
            bufferedWriter.write("first test line\nsecond test line\nfourth test line");
        }
        List<String> expectedFileList = new ArrayList<>();
        List<String> resultFileList = new ArrayList<>();
        String line;

        //When
        new FileHelper().removeLine(resultFile, 3);

        //Then
        BufferedReader bufferedReader = new BufferedReader(new FileReader(expectedFile));
        while ((line = bufferedReader.readLine()) != null) {
            expectedFileList.add(line);
        }
        bufferedReader.close();
        bufferedReader = new BufferedReader(new FileReader(resultFile));
        while ((line = bufferedReader.readLine()) != null) {
            resultFileList.add(line);
        }
        bufferedReader.close();
        assertEquals(expectedFileList, resultFileList);
    }

    @Test
    void shouldThrowExceptionForRemoveLineFromFile() throws IOException {
        try {
            new FileHelper().removeLine(expectedFile, 4);
        } catch (FileNotFoundException expected) {
            assertEquals("Can't delete line, haven't found the file", expected.getMessage());
        }

    }
}
