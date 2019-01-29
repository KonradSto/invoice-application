package pl.coderstrust;

public class Application {

  public static void main(String[] args) {
    Application application = new Application();
    System.out.println(application.getMessage("Hello world"));
  }

  public String getMessage(String message){
    return message;
  }
}
