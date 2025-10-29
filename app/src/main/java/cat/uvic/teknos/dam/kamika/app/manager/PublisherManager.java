package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.repositories.RepositoryFactory;
import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class PublisherManager {
    private final ModelFactory modelFactory;
    private final RepositoryFactory repositoryFactory;
    private final Scanner scanner;

    public PublisherManager(Scanner scanner, RepositoryFactory repositoryFactory, ModelFactory modelFactory) {
        this.scanner = scanner;
        this.repositoryFactory = repositoryFactory;
        this.modelFactory = modelFactory;
    }

    private void displayAllPublishers(){
        var publishers = repositoryFactory.getPublisherRepository().findAll();
        System.out.println(AsciiTable.getTable(publishers, Arrays.asList(
                new Column().header("ID").with(p -> Integer.toString(p.getId())),
                new Column().header("Name").with(p -> p.getName()),
                new Column().header("Country").with(p -> p.getCountry())
        )));
    }

    public void run() throws SQLException {
        while (true) {
            System.out.println("Publisher Management:");
            System.out.println("1 - View publisher details");
            System.out.println("2 - Save new publisher");
            System.out.println("3 - Delete publisher");
            System.out.println("4 - Exit menu");

            var repository = repositoryFactory.getPublisherRepository();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    var allPublishers = repository.findAll();
                    if (allPublishers.isEmpty()) {
                        System.out.println("No publishers available. Returning to the menu...");
                        break;
                    }
                    displayAllPublishers();
                    break;

                case "2":
                    Publisher publisher = modelFactory.newPublisher();
                    System.out.println("Name:");
                    publisher.setName(scanner.nextLine());
                    System.out.println("Country:");
                    publisher.setCountry(scanner.nextLine());
                    repository.save(publisher);
                    System.out.println("Publisher saved successfully");
                    break;

                case "3":
                    var publishers = repository.findAll();
                    if (publishers.isEmpty()) {
                        System.out.println("No publishers available to delete. Returning to the menu...");
                        break;
                    }
                    displayAllPublishers(publishers);
                    System.out.println("\nEnter the name of the publisher to delete:");
                    try{
                        var id = Integer.parseInt(scanner.nextLine());
                        var delPublisher = repository.findById(id);
                        if (delPublisher.isPresent()) {
                            repository.delete(delPublisher.get());
                            System.out.println("Publisher deleted successfully");
                        } else {
                            System.out.println("Publisher not found");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID. Returning to the menu...");
                        break;
                    }
                    break;

                case "4":
                    System.out.println("Exiting publisher menu...");
                    return;

                default:
                    System.out.println("Invalid command");
            }
            System.out.println("\nChoose an option:");
        }
    }

    private void displayAllPublishers(Set<Publisher> allPublishers) {

    }
}