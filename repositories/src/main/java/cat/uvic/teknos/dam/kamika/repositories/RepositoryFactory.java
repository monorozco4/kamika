package cat.uvic.teknos.dam.kamika.repositories;

public interface RepositoryFactory {
    ConsoleRepository getConsoleRepository();
    DeveloperRepository getDeveloperRepository();
    GameConsoleRepository getGameConsoleRepository();
    GameEditionRepository getGameEditionRepository();
    GameRepository getGameRepository();
    GenreRepository getGenreRepository();
    PublisherRepository getPublisherRepository();
}
