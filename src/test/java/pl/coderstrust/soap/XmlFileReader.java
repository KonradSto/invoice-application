package pl.coderstrust.soap;

import java.io.IOException;

import org.apache.commons.io.FileUtils;

class XmlFileReader {

    static String readFromFile(String filePath) throws IOException {
        return FileUtils.readFileToString(new java.io.File(filePath));
    }
}
