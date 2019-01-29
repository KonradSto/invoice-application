package pl.coderstrust;

import static org.apache.maven.surefire.booter.ForkedBooter.main;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  public void shouldReturnCorrectString(){
    //given
    String expected = "Hello world!!";
    Application.main(new String[2]);

    //when
    String actual = new Application().print("Hello world!!");

    //then
    assertEquals(actual,expected);
  }
}
