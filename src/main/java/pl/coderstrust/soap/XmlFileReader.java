package pl.coderstrust.soap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class XmlFileReader {

    static String readFromFile(File filename) throws IOException {
        String xmlContent;

        xmlContent = readLinesFromFile(filename)
            .collect(Collectors.joining(" "));
        return xmlContent;
    }

    private static Stream<String> readLinesFromFile(File filename) throws IOException {
        return Files.lines(filename.toPath());
    }

}
