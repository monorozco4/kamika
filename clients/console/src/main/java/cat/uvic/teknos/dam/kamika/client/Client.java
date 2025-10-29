package cat.uvic.teknos.dam.kamika.client;

import cat.uvic.teknos.dam.kamika.client.api.DeveloperApiClient;
import cat.uvic.teknos.dam.kamika.client.exceptions.ClientException;
import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.model.Developer;

import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * Handles the main logic and user interface for the console client application.
 * Dependencies are injected via the constructor for better testability.
 * @author Your Name
 * @version 1.6 // Refactored for DI
 */
public class Client {
    private final Scanner scanner;
    private final DeveloperApiClient developerApi;
    private final ModelFactory modelFactory;

    /**
     * Constructs the client application with its required dependencies.
     * @param scanner The Scanner instance for user input.
     * @param modelFactory The factory for creating model objects.
     * @param developerApi The client for communicating with the server API.
     */
    public Client(Scanner scanner, ModelFactory modelFactory, DeveloperApiClient developerApi) {
        this.scanner = scanner;
        this.modelFactory = modelFactory;
        this.developerApi = developerApi;
    }

    /**
     * Starts the main application loop, displays the menu, processes user input,
     * and handles potential errors.
     */
    public void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice.trim()) {
                    case "1" -> listAllDevelopers();
                    case "2" -> getDeveloperById();
                    case "3" -> addDeveloper();
                    case "4" -> updateDeveloper();
                    case "5" -> deleteDeveloper();
                    case "0" -> {
                        System.out.println("Exiting application. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option, please try again.");
                }
            } catch (ClientException e) {
                System.err.println("\n[API ERROR] " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("\n[INPUT ERROR] Invalid number format. Please enter a valid integer.");
            } catch (Exception e) {
                System.err.println("\n[UNEXPECTED ERROR] " + e.getMessage());
            }
        }
    }

    /**
     * Prints the main menu options to the console.
     */
    private void printMenu() {
        System.out.println("\n--- Kamika API Client ---");
        System.out.println("1. List all developers");
        System.out.println("2. Get developer by ID");
        System.out.println("3. Add a new developer");
        System.out.println("4. Update a developer");
        System.out.println("5. Delete a developer");
        System.out.println("-------------------------");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the "List all developers" action.
     */
    private void listAllDevelopers() {
        System.out.println("\nFetching developers...");
        Set<Developer> developers = developerApi.getAll();
        if (developers.isEmpty()) {
            System.out.println("No developers found.");
        } else {
            printDeveloperList(developers);
        }
    }

    /**
     * Handles the "Get developer by ID" action.
     */
    private void getDeveloperById() {
        System.out.print("Enter developer ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("\nFetching developer with ID " + id + "...");
        Optional<Developer> developer = developerApi.getById(id);
        if (developer.isPresent()) {
            printDeveloperList(Set.of(developer.get()));
        } else {
            System.out.println("Developer with ID " + id + " not found.");
        }
    }

    /**
     * Handles the "Add a new developer" action.
     */
    private void addDeveloper() {
        System.out.println("\n--- Add New Developer ---");
        Developer dev = modelFactory.newDeveloper();
        System.out.print("Enter name: ");
        dev.setName(scanner.nextLine());
        System.out.print("Enter country: ");
        dev.setCountry(scanner.nextLine());
        System.out.print("Enter foundation year: ");
        dev.setFoundationYear(Integer.parseInt(scanner.nextLine()));
        Developer createdDev = developerApi.create(dev);
        System.out.println("\nSuccessfully created developer:");
        printDeveloperList(Set.of(createdDev));
    }

    /**
     * Handles the "Update a developer" action.
     */
    private void updateDeveloper() {
        System.out.print("Enter ID of developer to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Developer> existingDevOpt = developerApi.getById(id);
        if (existingDevOpt.isEmpty()) {
            System.out.println("Developer with ID " + id + " not found.");
            return;
        }
        Developer devToUpdate = existingDevOpt.get();
        System.out.println("Updating developer: " + devToUpdate.getName());
        System.out.println("(Press Enter to keep current value)");
        System.out.printf("Enter new name [%s]: ", devToUpdate.getName());
        String name = scanner.nextLine();
        if (!name.isBlank()) devToUpdate.setName(name);
        System.out.printf("Enter new country [%s]: ", devToUpdate.getCountry());
        String country = scanner.nextLine();
        if (!country.isBlank()) devToUpdate.setCountry(country);
        System.out.printf("Enter new foundation year [%d]: ", devToUpdate.getFoundationYear());
        String yearStr = scanner.nextLine();
        if (!yearStr.isBlank()) devToUpdate.setFoundationYear(Integer.parseInt(yearStr));
        if (developerApi.update(devToUpdate)) {
            System.out.println("\nDeveloper updated successfully.");
        } else {
            System.out.println("\nFailed to update developer.");
        }
    }

    /**
     * Handles the "Delete a developer" action.
     */
    private void deleteDeveloper() {
        System.out.print("Enter ID of developer to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Deleting developer with ID " + id + "...");
        if (developerApi.delete(id)) {
            System.out.println("Developer successfully deleted.");
        } else {
            System.out.println("Developer with ID " + id + " not found.");
        }
    }

    /**
     * Prints a set of developers as a simple list.
     * @param developers The set of developers to print.
     */
    private void printDeveloperList(Set<Developer> developers) {
        System.out.println("--- Developer List ---");
        if (developers == null || developers.isEmpty()) {
            System.out.println(" (No developers to display) ");
        } else {
            for (Developer dev : developers) {
                System.out.printf("ID: %d | Name: %s | Country: %s | Founded: %d%n",
                        dev.getId(),
                        dev.getName() != null ? dev.getName() : "N/A",
                        dev.getCountry() != null ? dev.getCountry() : "N/A",
                        dev.getFoundationYear()
                );
            }
        }
        System.out.println("----------------------");
    }
}