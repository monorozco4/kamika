package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.repositories.GameRepository;
import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import cat.uvic.teknos.dam.kamika.repositories.PublisherRepository;
import cat.uvic.teknos.dam.kamika.repositories.GenreRepository;
import cat.uvic.teknos.dam.kamika.repositories.ConsoleRepository;
import cat.uvic.teknos.dam.kamika.repositories.GameEditionRepository;
import cat.uvic.teknos.dam.kamika.repositories.GameConsoleRepository;

import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;

/**
 * Factory class that provides JDBC repository instances.
 * Facilitates centralized and decoupled creation of repositories in the system.
 */
public class RepositoryFactory {

    private final SingleConnectionDataSource dataSource;

    /**
     * Constructor that receives a shared data source for all repositories.
     *
     * @param dataSource The data source used to obtain database connections.
     */
    public RepositoryFactory(SingleConnectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns an instance of GameRepository.
     */
    public GameRepository getGameRepository() {
        return new JdbcGameRepository(dataSource);
    }

    /**
     * Returns an instance of DeveloperRepository.
     */
    public DeveloperRepository getDeveloperRepository() {
        return new JdbcDeveloperRepository(dataSource);
    }

    /**
     * Returns an instance of PublisherRepository.
     */
    public PublisherRepository getPublisherRepository() {
        return new JdbcPublisherRepository(dataSource);
    }

    /**
     * Returns an instance of GenreRepository.
     */
    public GenreRepository getGenreRepository() {
        return new JdbcGenreRepository(dataSource);
    }

    /**
     * Returns an instance of ConsoleRepository.
     */
    public ConsoleRepository getConsoleRepository() {
        return new JdbcConsoleRepository(dataSource);
    }

    /**
     * Returns an instance of GameEditionRepository.
     */
    public GameEditionRepository getGameEditionRepository() {
        return new JdbcGameEditionRepository(dataSource);
    }

    /**
     * Returns an instance of GameConsoleRepository.
     */
    public GameConsoleRepository getGameConsoleRepository() {
        return new JdbcGameConsoleRepository(dataSource);
    }
}