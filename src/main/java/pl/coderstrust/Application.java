package pl.coderstrust;

public class Application {

  public static void main(String[] args) {
    Application application = new Application();
    System.out.println(application.print("Hello world"));
  }

  public String print(String print){
    return print;
  }
}
