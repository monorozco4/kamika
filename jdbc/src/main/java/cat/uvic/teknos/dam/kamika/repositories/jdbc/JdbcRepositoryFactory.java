package cat.uvic.teknos.dam.kamika.repositories.jdbc;

import cat.uvic.teknos.dam.kamika.repositories.*;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;

/**
 * Factory class that provides JDBC repository instances.
 * Implements RepositoryFactory to ensure compatibility.
 */
public class JdbcRepositoryFactory implements RepositoryFactory {

    private SingleConnectionDataSource dataSource;

    /**
     * Default constructor.
     * Initializes the factory without a data source.
     */
    public JdbcRepositoryFactory() {
        this.dataSource = new SingleConnectionDataSource(); // Inicializa el dataSource
    }

    /**
     * Constructor that receives a shared data source for all repositories.
     *
     * @param dataSource The data source used to obtain database connections.
     */
    public JdbcRepositoryFactory(SingleConnectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Sets the data source to be used by the repositories.
     *
     * @param dataSource The data source to set.
     */
    public void setDataSource(SingleConnectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public GameRepository getGameRepository() {
        return new JdbcGameRepository(dataSource);
    }

    @Override
    public DeveloperRepository getDeveloperRepository() {
        return new JdbcDeveloperRepository(dataSource);
    }

    @Override
    public PublisherRepository getPublisherRepository() {
        return new JdbcPublisherRepository(dataSource);
    }

    @Override
    public GenreRepository getGenreRepository() {
        return new JdbcGenreRepository(dataSource);
    }

    @Override
    public ConsoleRepository getConsoleRepository() {
        return new JdbcConsoleRepository(dataSource);
    }

    @Override
    public GameEditionRepository getGameEditionRepository() {
        return new JdbcGameEditionRepository(dataSource);
    }

    @Override
    public GameConsoleRepository getGameConsoleRepository() {
        return new JdbcGameConsoleRepository(dataSource);
    }
}