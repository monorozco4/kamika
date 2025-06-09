package cat.uvic.teknos.dam.kamika.app;

import cat.uvic.teknos.dam.kamika.app.manager.*;
import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        var scanner = new Scanner(System.in);

        Banner.show();

        var diManager = new DIManager();
        RepositoryFactory repositoryFactory = diManager.get("repository_factory", RepositoryFactory.class);
        ModelFactory modelFactory = diManager.get("model_factory", ModelFactory.class);

        ConsoleManager consoleManager = new ConsoleManager(scanner, repositoryFactory, modelFactory);
        DeveloperManager developerManager = new DeveloperManager(scanner, repositoryFactory, modelFactory);
        GameEditionManager gameEditionManager = new GameEditionManager(scanner, repositoryFactory, modelFactory);
        GameManager gameManager = new GameManager(scanner, repositoryFactory, modelFactory);
        GenreManager genreManager = new GenreManager(scanner, repositoryFactory, modelFactory);
        PublisherManager publisherManager = new PublisherManager(scanner, repositoryFactory, modelFactory);

        while (true) {
            System.out.println("Main menu:");
            System.out.println("1 - Manage consoles");
            System.out.println("2 - Manage developers");
            System.out.println("3 - Manage game editions");
            System.out.println("4 - Manage games");
            System.out.println("5 - Manage genres");
            System.out.println("6 - Manage publishers");
            System.out.println("0 - Exit");
            System.out.println("What would you like to check? Enter your choice: ");
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    consoleManager.run();
                    break;
                case "2":
                    developerManager.run();
                    break;
                case "3":
                    gameEditionManager.run();
                    break;
                case "4":
                    gameManager.run();
                    break;
                case "5":
                    genreManager.run();
                    break;
                case "6":
                    publisherManager.run();
                    break;
                case "0":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}