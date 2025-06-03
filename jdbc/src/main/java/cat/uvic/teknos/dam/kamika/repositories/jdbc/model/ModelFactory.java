package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.repositories.Console;
import cat.uvic.teknos.dam.kamika.repositories.Developer;
import cat.uvic.teknos.dam.kamika.repositories.Game;
import cat.uvic.teknos.dam.kamika.repositories.Publisher;
import cat.uvic.teknos.dam.kamika.repositories.Genre;
import cat.uvic.teknos.dam.kamika.repositories.GameEdition;
import cat.uvic.teknos.dam.kamika.repositories.GameConsole;

import cat.uvic.teknos.dam.kamika.repositories.impl.ConsoleImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GenreImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.repositories.impl.GameConsoleImpl;

/**
 * Factory class responsible for creating model entity instances.
 * Decouples object creation from concrete implementations.
 */
public class ModelFactory {

    /**
     * Creates a new instance of a Game entity.
     *
     * @return a new Game instance
     */
    public Game createGame() {
        return new GameImpl();
    }

    /**
     * Creates a new instance of a Developer entity.
     *
     * @return a new Developer instance
     */
    public Developer createDeveloper() {
        return new DeveloperImpl();
    }

    /**
     * Creates a new instance of a Publisher entity.
     *
     * @return a new Publisher instance
     */
    public Publisher createPublisher() {
        return new PublisherImpl();
    }

    /**
     * Creates a new instance of a Genre entity.
     *
     * @return a new Genre instance
     */
    public Genre createGenre() {
        return new GenreImpl();
    }

    /**
     * Creates a new instance of a Console entity.
     *
     * @return a new Console instance
     */
    public Console createConsole() {
        return new ConsoleImpl();
    }

    /**
     * Creates a new instance of a GameEdition entity.
     *
     * @return a new GameEdition instance
     */
    public GameEdition createGameEdition() {
        return new GameEditionImpl();
    }

    /**
     * Creates a new instance of a GameConsole relation entity.
     *
     * @return a new GameConsole instance
     */
    public GameConsole createGameConsole() {
        return new GameConsoleImpl();
    }
}