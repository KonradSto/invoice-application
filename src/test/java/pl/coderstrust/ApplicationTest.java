package pl.coderstrust;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    public void shouldReturnCorrectString() {
        //given
        String expected = "Hello world!!";

        //when
        String actual = new Application().getMessage("Hello world!!");

        //then
        assertEquals(actual, expected);
    }
}
