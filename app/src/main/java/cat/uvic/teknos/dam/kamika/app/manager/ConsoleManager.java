package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Provides management operations for video game consoles through a console-based interface.
 * Allows listing, viewing, creating, and deleting console records.
 */
public class ConsoleManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    /**
     * Constructs a ConsoleManager with the necessary dependencies.
     *
     * @param scanner the Scanner instance for reading user input
     * @param repositoryFactory the factory for creating console repositories
     * @param modelFactory the factory for creating console model instances
     */
    public ConsoleManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    /**
     * Displays all consoles in a formatted ASCII table.
     * The table includes columns for ID, Name, Manufacturer, and Release Year.
     */
    private void displayAllConsoles() {
        var consoles = repositoryFactory.getConsoleRepository().findAll();
        System.out.println(AsciiTable.getTable(consoles, Arrays.asList(
                new Column().header("ID").with(c -> Integer.toString(c.getId())),
                new Column().header("Name").with(c -> c.getName()),
                new Column().header("Manufacturer").with(c -> c.getManufacturer()),
                new Column().header("Release Year").with(c -> Integer.toString(c.getReleaseYear()))
        )));
    }

    /**
     * Runs the console management interface with a menu-driven approach.
     * Handles user input and executes corresponding operations for console management.
     *
     * @throws SQLException if a database access error occurs during operations
     */
    public void run() throws SQLException {
        while (true) {
            System.out.println("Console Management:");
            System.out.println("1 - Show all consoles");
            System.out.println("2 - View specific console");
            System.out.println("3 - Save new console");
            System.out.println("4 - Delete existing console");
            System.out.println("5 - Exit console menu");

            var repository = repositoryFactory.getConsoleRepository();
            var command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allConsoles = repository.findAll();
                    if (allConsoles.isEmpty()) {
                        System.out.println("No consoles available. Returning to the menu...");
                        break;
                    }
                    displayAllConsoles();
                    break;

                case "2":
                    var consolesForView = repository.findAll();
                    if (consolesForView.isEmpty()) {
                        System.out.println("No consoles available. Returning to the menu...");
                        break;
                    }
                    displayAllConsoles();
                    System.out.println("\nEnter console ID to view details: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var consoleOpt = repository.findById(id);
                        if (consoleOpt.isPresent()) {
                            var console = consoleOpt.get();
                            System.out.println("Console found:");
                            System.out.println("ID: " + console.getId());
                            System.out.println("Name: " + console.getName());
                            System.out.println("Manufacturer: " + console.getManufacturer());
                            System.out.println("Release Year: " + console.getReleaseYear());
                        } else {
                            System.out.println("Console not found. Returning to the menu...");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                    }
                    break;

                case "3":
                    var console = modelFactory.newConsole();
                    System.out.println("Name: ");
                    console.setName(scanner.nextLine());
                    System.out.println("Manufacturer: ");
                    console.setManufacturer(scanner.nextLine());
                    System.out.println("Release year: ");
                    try {
                        console.setReleaseYear(Integer.parseInt(scanner.nextLine()));
                        repository.save(console);
                        System.out.println("Console successfully saved");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid release year");
                    }
                    break;

                case "4":
                    var consolesToDelete = repository.findAll();
                    if (consolesToDelete.isEmpty()) {
                        System.out.println("No consoles available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllConsoles();
                    System.out.println("\nEnter the console ID to delete: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var consoleOpt = repository.findById(id);
                        if (consoleOpt.isPresent()) {
                            repository.delete(consoleOpt.get());
                            System.out.println("Console successfully deleted.");
                        } else {
                            System.out.println("Console not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "5":
                    System.out.println("Exiting console menu...");
                    return;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}