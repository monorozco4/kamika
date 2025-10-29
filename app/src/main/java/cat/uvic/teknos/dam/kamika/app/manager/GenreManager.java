package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class GenreManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public GenreManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    private void displayAllGenres() {
        var genres = repositoryFactory.getGenreRepository().findAll();
        System.out.println(AsciiTable.getTable(genres, Arrays.asList(
                new Column().header("ID").with(g -> Integer.toString(g.getId())),
                new Column().header("Name").with(g -> g.getName()),
                new Column().header("Description").with(g -> g.getDescription())
        )));
    }

    public void run() throws SQLException {
        while (true) {
            System.out.println("Genre Management:");
            System.out.println("1 - View genre details");
            System.out.println("2 - Save new genre");
            System.out.println("3 - Delete genre");
            System.out.println("4 - Exit menu");

            var repository = repositoryFactory.getGenreRepository();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allGenres = repository.findAll();
                    if (allGenres.isEmpty()) {
                        System.out.println("No genres available. Returning to the menu...");
                        break;
                    }
                    displayAllGenres();
                    break;

                case "2":
                    var genre = modelFactory.newGenre();
                    System.out.println("Name:");
                    genre.setName(scanner.nextLine());
                    System.out.println("Description:");
                    genre.setDescription(scanner.nextLine());
                    repository.save(genre);
                    System.out.println("Genre saved successfully");
                    break;

                case "3":
                    var genresToDelete = repository.findAll();
                    if (genresToDelete.isEmpty()) {
                        System.out.println("No genres available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllGenres();
                    System.out.println("\nEnter the name of the genre to delete:");
                    try {
                        var id = Integer.parseInt(scanner.nextLine());
                        var genreOpt = repository.findById(id);
                        if (genreOpt.isPresent()) {
                            repository.delete(genreOpt.get());
                            System.out.println("Genre deleted successfully");
                        } else {
                            System.out.println("Genre not found");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                        break;
                    }
                    break;

                case "4":
                    System.out.println("Exiting genre menu...");
                    return;

                default:
                    System.out.println("Invalid command");
            }
            System.out.println("\nChoose an option:");
        }
    }


}