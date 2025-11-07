package cat.uvic.teknos.dam.kamika.client;

import cat.uvic.teknos.dam.kamika.client.api.DeveloperApiClient;
import cat.uvic.teknos.dam.kamika.client.exceptions.ClientException;
import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.model.Developer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Handles the main logic and user interface for the console client application.
 * Dependencies are injected for testability.
 * Includes an inactivity monitor to automatically disconnect after a timeout.
 * @author Your Name
 * @version 2.0 // Added Inactivity Monitor
 */
public class Client {

    // Dependencies injected via constructor
    private final Scanner scanner; // Used for parsing user input strings
    private final ModelFactory modelFactory;
    private final DeveloperApiClient developerApi;

    // Internal components for non-blocking input and inactivity timer
    private final BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
    private final ScheduledExecutorService inactivityScheduler;
    private ScheduledFuture<?> inactivityFuture; // Holds the scheduled disconnect task

    // 2 minutes in milliseconds
    private static final long INACTIVITY_TIMEOUT_MS = 120_000;

    /**
     * Constructs the client application with its required dependencies.
     * @param scanner The Scanner instance for parsing user input.
     * @param modelFactory The factory for creating model objects.
     * @param developerApi The client for communicating with the server API.
     */
    public Client(Scanner scanner, ModelFactory modelFactory, DeveloperApiClient developerApi) {
        this.scanner = scanner;
        this.modelFactory = modelFactory;
        this.developerApi = developerApi;

        // Create a daemon thread for the inactivity monitor
        this.inactivityScheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread t = new Thread(runnable, "InactivityMonitorThread");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Starts the main application loop.
     * This loop handles user input in a non-blocking way to allow
     * the inactivity timer to function concurrently.
     */
    public void run() {
        System.out.println("Welcome to the Kamika API Client.");
        System.out.println("Application will auto-disconnect after 2 minutes of inactivity.");

        try {
            while (true) {
                printMenu();

                // 1. Schedule the disconnect task
                resetInactivityTimer();

                String choice = "";
                try {
                    // 2. Wait for user input in a non-blocking way
                    while (!userIn.ready()) {
                        Thread.sleep(200); // Poll every 200ms
                    }
                    choice = userIn.readLine(); // Read the full line

                } catch (IOException e) {
                    System.err.println("\n[INPUT ERROR] " + e.getMessage());
                    continue; // Continue loop
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Main input thread interrupted, shutting down.");
                    break; // Exit loop
                }

                // 3. If we get here, user provided input. Cancel the scheduled disconnect.
                if (inactivityFuture != null) {
                    inactivityFuture.cancel(false);
                }

                // 4. Process the user's choice
                if (choice == null) break; // End of stream

                switch (choice.trim()) {
                    case "1" -> listAllDevelopers();
                    case "2" -> getDeveloperById();
                    case "3" -> addDeveloper();
                    case "4" -> updateDeveloper();
                    case "5" -> deleteDeveloper();
                    case "0" -> {
                        System.out.println("Exiting application. Goodbye!");
                        shutdown();
                        return; // Exit run() method
                    }
                    default -> System.out.println("Invalid option, please try again.");
                }
            }
        } catch (ClientException e) {
            System.err.println("\n[API ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\n[UNEXPECTED ERROR] " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            shutdown();
        }
    }

    /**
     * Resets the 2-minute inactivity timer.
     * This is called before waiting for new user input.
     */
    private void resetInactivityTimer() {
        // Cancel any previous timer
        if (inactivityFuture != null) {
            inactivityFuture.cancel(false);
        }
        // Schedule a new disconnect task
        inactivityFuture = inactivityScheduler.schedule(
                this::handleInactivityDisconnect,
                INACTIVITY_TIMEOUT_MS,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * This method is called by the inactivityScheduler if the timeout is reached.
     * It sends a disconnect message to the server and exits the application.
     */
    private void handleInactivityDisconnect() {
        System.out.println("\n\n[INACTIVITY] 2 minutes of inactivity detected. Sending disconnect message...");
        try {
            if (developerApi.sendDisconnect()) {
                System.out.println("[INACTIVITY] Server acknowledged disconnect. Client exiting.");
            } else {
                System.out.println("[INACTIVITY] Server did not acknowledge disconnect. Exiting anyway.");
            }
        } catch (ClientException e) {
            System.err.println("[INACTIVITY] Error during disconnect: " + e.getMessage());
        } finally {
            // Force the application to exit
            System.exit(0);
        }
    }

    /**
     * Cleans up resources (scheduler, input reader) before exiting.
     */
    private void shutdown() {
        inactivityScheduler.shutdownNow(); // Stop the inactivity timer thread
        try {
            userIn.close();
            scanner.close(); // Close the scanner we are using for parsing
        } catch (IOException e) {
            System.err.println("Error closing input stream: " + e.getMessage());
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

    // --- Mètodes d'Acció (Iguals que abans) ---

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
        for (Developer dev : developers) {
            System.out.printf("ID: %d | Name: %s | Country: %s | Founded: %d%n",
                    dev.getId(),
                    dev.getName(),
                    dev.getCountry(),
                    dev.getFoundationYear());
        }
        System.out.println("----------------------");
    }
}