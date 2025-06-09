package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class GameManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public GameManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    /**
     * Displays all games in a formatted ASCII table.
     * Shows ID, title, release date, developer ID, publisher ID, PEGI, and multiplayer.
     */
    private void displayAllGames() {
        var games = repositoryFactory.getGameRepository().findAll();
        System.out.println(AsciiTable.getTable(games, Arrays.asList(
                new Column().header("ID").with(g -> Integer.toString(g.getId())),
                new Column().header("Title").with(g -> g.getTitle()),
                new Column().header("Release").with(g -> g.getReleaseDate().toString()),
                new Column().header("DevID").with(g -> g.getDeveloper() != null ? Integer.toString(g.getDeveloper().getId()) : "N/A"),
                new Column().header("PubID").with(g -> g.getPublisher() != null ? Integer.toString(g.getPublisher().getId()) : "N/A"),
                new Column().header("PEGI").with(g -> g.getPegiRating()),
                new Column().header("Multi").with(g -> g.isMultiplayer() ? "Yes" : "No")
        )));
    }

    /**
     * Runs the game management interface.
     * Menu: 1. Show all games, 2. View game details, 3. Create new game, 4. Delete game, 5. Exit.
     */
    public void run() throws SQLException {
        while (true) {
            System.out.println("Game Management:");
            System.out.println("1 - Show all games");
            System.out.println("2 - View specific game");
            System.out.println("3 - Save new game");
            System.out.println("4 - Delete existing game");
            System.out.println("5 - Exit game menu");

            var repository = repositoryFactory.getGameRepository();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allGames = repository.findAll();
                    if (allGames.isEmpty()) {
                        System.out.println("No games available. Returning to the menu...");
                        break;
                    }
                    displayAllGames();
                    break;

                case "2":
                    var gamesForView = repository.findAll();
                    if (gamesForView.isEmpty()) {
                        System.out.println("No games available. Returning to the menu...");
                        break;
                    }
                    displayAllGames();
                    System.out.println("\nEnter game ID to view details: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var gameOpt = repository.findById(id);
                        if (gameOpt.isPresent()) {
                            var game = gameOpt.get();
                            System.out.println("Game found:");
                            System.out.println("ID: " + game.getId());
                            System.out.println("Title: " + game.getTitle());
                            System.out.println("Release: " + game.getReleaseDate());
                            System.out.println("Developer ID: " + (game.getDeveloper() != null ? game.getDeveloper().getId() : "N/A"));
                            System.out.println("Publisher ID: " + (game.getPublisher() != null ? game.getPublisher().getId() : "N/A"));
                            System.out.println("PEGI: " + game.getPegiRating());
                            System.out.println("Multiplayer: " + (game.isMultiplayer() ? "Yes" : "No"));
                        } else {
                            System.out.println("Game not found. Returning to the menu...");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                    }
                    break;

                case "3":
                    try {
                        var game = modelFactory.newGame();
                        System.out.println("Title: ");
                        game.setTitle(scanner.nextLine());
                        System.out.println("Release date (yyyy-MM-dd): ");
                        game.setReleaseDate(java.time.LocalDate.parse(scanner.nextLine()));

                        System.out.println("Developer ID: ");
                        var devId = Integer.parseInt(scanner.nextLine());
                        var devOpt = repositoryFactory.getDeveloperRepository().findById(devId);
                        if (devOpt.isEmpty()) {
                            System.out.println("Developer not found. Aborting.");
                            break;
                        }
                        game.setDeveloper(devOpt.get());

                        System.out.println("Publisher ID: ");
                        var pubId = Integer.parseInt(scanner.nextLine());
                        var pubOpt = repositoryFactory.getPublisherRepository().findById(pubId);
                        if (pubOpt.isEmpty()) {
                            System.out.println("Publisher not found. Aborting.");
                            break;
                        }
                        game.setPublisher(pubOpt.get());

                        System.out.println("PEGI: ");
                        game.setPegiRating(scanner.nextLine());
                        System.out.println("Is multiplayer? (y/N): ");
                        String multi = scanner.nextLine().trim().toLowerCase();
                        game.setMultiplayer(multi.equals("y"));

                        repository.save(game);
                        System.out.println("Game successfully saved");
                    } catch (Exception e) {
                        System.out.println("Error saving game: " + e.getMessage());
                    }
                    break;

                case "4":
                    var gamesToDelete = repository.findAll();
                    if (gamesToDelete.isEmpty()) {
                        System.out.println("No games available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllGames();
                    System.out.println("\nEnter game ID to delete: ");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var gameOpt = repository.findById(id);
                        if (gameOpt.isPresent()) {
                            repository.delete(gameOpt.get());
                            System.out.println("Game successfully deleted");
                        } else {
                            System.out.println("Game not found");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID");
                    }
                    break;

                case "5":
                    System.out.println("Exiting game menu...");
                    return;

                default:
                    System.out.println("Invalid command");
            }
            System.out.println("\nChoose an option:");
        }
    }
}