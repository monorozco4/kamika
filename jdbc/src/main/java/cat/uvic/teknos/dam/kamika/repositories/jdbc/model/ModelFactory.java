package cat.uvic.teknos.dam.kamika.repositories.jdbc.model;

import cat.uvic.teknos.dam.kamika.model.Console;
import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.Game;
import cat.uvic.teknos.dam.kamika.model.Publisher;
import cat.uvic.teknos.dam.kamika.model.Genre;
import cat.uvic.teknos.dam.kamika.model.GameEdition;
import cat.uvic.teknos.dam.kamika.model.GameConsole;

import cat.uvic.teknos.dam.kamika.model.impl.ConsoleImpl;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GameImpl;
import cat.uvic.teknos.dam.kamika.model.impl.PublisherImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GenreImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GameEditionImpl;
import cat.uvic.teknos.dam.kamika.model.impl.GameConsoleImpl;

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