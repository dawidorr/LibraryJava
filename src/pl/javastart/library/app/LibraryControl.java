package pl.javastart.library.app;

import pl.javastart.library.exception.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.*;
import pl.javastart.library.model.comparator.AlphabeticalTitleComparator;


import java.util.Comparator;
import java.util.InputMismatchException;

class LibraryControl {
    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;

    private Library library;

    LibraryControl(){

        fileManager = new FileManagerBuilder(printer,dataReader).build();
        try{
            library = fileManager.importData();
            printer.printLine("Zaimportowane dane z pliku");
        }catch (DataImportException | InvalidDataException e){
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę");
            library = new Library();
        }
    }

    void controlLoop() {
        Option option;

        do{
            printOptions();
            option = getOption();
            switch(option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Nie ma takiej opcji, wprowadź ponownie:");

            }
        }while(option != Option.EXIT);

    }

    private Option getOption(){
        boolean optionOK = false;
        Option option = null;
        while(!optionOK) {
            try{
                option = Option.createFromInt(dataReader.getInt());
                optionOK = true;
            }catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", podaj ponownie:");
            }catch (InputMismatchException ignored) {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie: ");
            }
        }
        return option;
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję:");
        for(Option option : Option.values()){
            printer.printLine(option.toString());
        }
    }

    private void addBook() {
        try{
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyć książkim niepoprawne dane");
        }catch (ArrayIndexOutOfBoundsException e){
            printer.printLine("Osiągnieto limit pojemnośći, nie można dodać kolejnej książki");
        }
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane");
        }catch (ArrayIndexOutOfBoundsException e){
            printer.printLine("Osiągnieto limit pojemnośći, nie można dodać kolejego magazynu");
        }
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try{
            library.addUser(libraryUser);
        }catch (UserAlreadyExistsException e){
            printer.printLine(e.getMessage());
        }
    }

    private void printBooks() {
        printer.printBooks(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER))
        );
    }

    private void printMagazines() {
        printer.printMagazines(library.getSortedPublications(
                Comparator.comparing(Publication::getTitle,String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
                Comparator.comparing(User::getLastName,String.CASE_INSENSITIVE_ORDER)
                )
        );
    }

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji:  ");
        String title = dataReader.getString();
        String notFoundMessage = "Brak publikacji o takim tytule";
        library.findPublicationByTitle(title)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println,()-> System.out.println(notFoundMessage));
    }



    private void deleteMagazine(){
        try{
            Magazine magazine = dataReader.readAndCreateMagazine();
            if(library.removePublication(magazine))
                printer.printLine("Usunięto magazyn");
            else
                printer.printLine("Brak wskazanego magazynu");
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyc magazynu,niepoprawne dane");
        }
    }

    private void deleteBook(){
        try{
            Book book = dataReader.readAndCreateBook();
            if(library.removePublication(book))
                System.out.println("Usunieto ksiązke");
            else
                printer.printLine("Brak wskazanej książki.");
        }catch(InputMismatchException e){
            printer.printLine("Nie udało sie utworzyć książki, niepoprawne dane");
        }
    }

    private void exit() {
        try{
            fileManager.exportData(library);
            printer.printLine("Export danych do pliku zakończony powodzeniem");
        }catch(DataExportException e){
            printer.printLine(e.getMessage());
        }
        dataReader.close();
        printer.printLine("Koniec programu elo ");

    }

    private enum Option {
        EXIT(0,"Wyjście z programu"),
        ADD_BOOK(1,"Dodanie ksiązki"),
        ADD_MAGAZINE(2,"Dodanie magazynu"),
        PRINT_BOOKS(3,"Wyświetlanie dostepnych książek"),
        PRINT_MAGAZINES(4,"Wyświetlanie dostępnych magazynów/gazet"),
        DELETE_BOOK(5,"Usuń książkę"),
        DELETE_MAGAZINE(6,"Usun magazyn"),
        ADD_USER (7,"Dodaj czytelnika"),
        PRINT_USERS(8,"Wyświetl czytelnika"),
        FIND_BOOK(9,"Wyszukaj książkę");

        private int value;
        private String description;


        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public String toString() {
            return value + " " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            }catch (ArrayIndexOutOfBoundsException e){
                throw new NoSuchOptionException("Brak opcji o id" + option);
            }
        }
    }


}
