package pl.javastart.library.app;

public class LibraryApp {
    public static void main(String[] args) {
        final String APP_NAME = "Biblioteka v1.8";
        System.out.println(APP_NAME);
        LibraryControl libControl = new LibraryControl();
        libControl.controlLoop();
    }
}
