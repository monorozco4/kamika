package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class GameEditionManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public GameEditionManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    private void displayAllEditions() {
        var editions = repositoryFactory.getGameEditionRepository().findAll();
        System.out.println(AsciiTable.getTable(editions, Arrays.asList(
                new Column().header("ID").with(e -> Integer.toString(e.getId())),
                new Column().header("Edition Name").with(e -> e.getEditionName()),
                new Column().header("Game ID").with(e -> e.getGame() != null ? Integer.toString(e.getGame().getId()) : "N/A"),
                new Column().header("Price").with(e -> Double.toString(e.getPrice()))
        )));
    }

    public void run() throws SQLException {
        while (true) {
            System.out.println("Game Edition Management:");
            System.out.println("1 - Show all editions");
            System.out.println("2 - View specific edition");
            System.out.println("3 - Save new edition");
            System.out.println("4 - Delete existing edition");
            System.out.println("5 - Exit edition menu");

            var repository = repositoryFactory.getGameEditionRepository();
            var command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allEditions = repository.findAll();
                    if (allEditions.isEmpty()) {
                        System.out.println("No editions available. Returning to the menu...");
                        break;
                    }
                    displayAllEditions();
                    break;

                case "2":
                    var editionsForView = repository.findAll();
                    if (editionsForView.isEmpty()) {
                        System.out.println("No editions available. Returning to the menu...");
                        break;
                    }
                    displayAllEditions();
                    System.out.println("\nEnter edition ID to view details: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var editionOpt = repository.findById(id);
                        if (editionOpt.isPresent()) {
                            var edition = editionOpt.get();
                            System.out.println("Edition found:");
                            System.out.println("ID: " + edition.getId());
                            System.out.println("Name: " + edition.getEditionName());
                            System.out.println("Game ID: " + (edition.getGame() != null ? edition.getGame().getId() : "N/A"));
                            System.out.println("Price: " + edition.getPrice());
                        } else {
                            System.out.println("Edition not found. Returning to the menu...");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                    }
                    break;

                case "3":
                    var edition = modelFactory.newGameEdition();
                    System.out.println("Game ID: ");
                    var game = modelFactory.newGame();
                    try {
                        game.setId(Integer.parseInt(scanner.nextLine()));
                        edition.setGame(game);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid game ID. Returning to the menu...");
                        break;
                    }
                    System.out.println("Edition name: ");
                    edition.setEditionName(scanner.nextLine());
                    System.out.println("Price: ");
                    try {
                        edition.setPrice(Double.parseDouble(scanner.nextLine()));
                        repository.save(edition);
                        System.out.println("Edition successfully saved");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price. Returning to the menu...");
                    }
                    break;

                case "4":
                    var editionsToDelete = repository.findAll();
                    if (editionsToDelete.isEmpty()) {
                        System.out.println("No editions available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllEditions();
                    System.out.println("\nEnter the edition ID to delete: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var editionOpt = repository.findById(id);
                        if (editionOpt.isPresent()) {
                            repository.delete(editionOpt.get());
                            System.out.println("Edition successfully deleted.");
                        } else {
                            System.out.println("Edition not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case "5":
                    System.out.println("Exiting edition menu...");
                    return;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
    }
}