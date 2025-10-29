package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Manages developer-related operations including displaying, creating, viewing, and deleting developers.
 * Provides a console-based interface for developer management.
 */
public class DeveloperManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    /**
     * Constructs a DeveloperManager with the specified dependencies.
     *
     * @param scanner the Scanner instance for user input
     * @param repositoryFactory the RepositoryFactory for accessing developer repositories
     * @param modelFactory the ModelFactory for creating new developer instances
     */
    public DeveloperManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    /**
     * Displays all developers in a formatted ASCII table.
     * The table includes columns for ID, Name, Country, and Foundation Year.
     */
    private void displayAllDevelopers() {
        var developers = repositoryFactory.getDeveloperRepository().findAll();
        System.out.println(AsciiTable.getTable(developers, Arrays.asList(
                new Column().header("ID").with(d -> Integer.toString(d.getId())),
                new Column().header("Name").with(d -> d.getName()),
                new Column().header("Country").with(d -> d.getCountry()),
                new Column().header("Foundation Year").with(d -> Integer.toString(d.getFoundationYear()))
        )));
    }

    /**
     * Runs the developer management console interface.
     * Provides a menu with options to list, view, create, and delete developers.
     *
     * @throws SQLException if a database access error occurs
     */
    public void run() throws SQLException {
        while (true) {
            System.out.println("Developer Management:");
            System.out.println("1 - Show all developers");
            System.out.println("2 - View specific developer");
            System.out.println("3 - Save new developer");
            System.out.println("4 - Delete existing developer");
            System.out.println("5 - Exit developer menu");

            var repository = repositoryFactory.getDeveloperRepository();
            var command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allDevelopers = repository.findAll();
                    if (allDevelopers.isEmpty()) {
                        System.out.println("No developers available. Returning to the menu...");
                        break;
                    }
                    displayAllDevelopers();
                    break;

                case "2":
                    var developersForView = repository.findAll();
                    if (developersForView.isEmpty()) {
                        System.out.println("No developers available. Returning to the menu...");
                        break;
                    }
                    displayAllDevelopers();
                    System.out.println("\nEnter developer ID to view details: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var developerOpt = repository.findById(id);
                        if (developerOpt.isPresent()) {
                            var developer = developerOpt.get();
                            System.out.println("Developer found:");
                            System.out.println("ID: " + developer.getId());
                            System.out.println("Name: " + developer.getName());
                            System.out.println("Country: " + developer.getCountry());
                            System.out.println("Foundation Year: " + developer.getFoundationYear());
                        } else {
                            System.out.println("Developer not found. Returning to the menu...");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                    }
                    break;

                case "3":
                    var developer = modelFactory.newDeveloper();
                    System.out.println("Name: ");
                    developer.setName(scanner.nextLine());
                    System.out.println("Country: ");
                    developer.setCountry(scanner.nextLine());
                    System.out.println("Foundation year: ");
                    try {
                        developer.setFoundationYear(Integer.parseInt(scanner.nextLine()));
                        repository.save(developer);
                        System.out.println("Developer successfully saved");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid foundation year");
                    }
                    break;

                case "4":
                    var developers = repository.findAll();
                    if (developers.isEmpty()) {
                        System.out.println("No developers available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllDevelopers();
                    System.out.println("\nEnter the developer ID to delete or type '0' to go back: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        if (id == 0) {
                            System.out.println("Operation canceled. Returning to the menu...");
                            break;
                        }
                        var developerOpt = repository.findById(id);
                        if (developerOpt.isPresent()) {
                            repository.delete(developerOpt.get());
                            System.out.println("Developer successfully deleted.");
                        } else {
                            System.out.println("Developer not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "5":
                    System.out.println("Exiting developer menu...");
                    return;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}