package cat.uvic.teknos.dam.kamika.model.impl;

import cat.uvic.teknos.dam.kamika.model.*;

/**
 * Implementation of the ModelFactory interface.
 * Provides methods to create new instances of model entities.
 */
public class ModelFactoryImpl implements ModelFactory {

    /**
     * Default public constructor required for reflection.
     */
    public ModelFactoryImpl() {}

    /**
     * Creates a new instance of Console.
     * @return a new Console object
     */
    @Override
    public Console newConsole() {
        return new ConsoleImpl();
    }

    /**
     * Creates a new instance of Developer.
     * @return a new Developer object
     */
    @Override
    public Developer newDeveloper() {
        return new DeveloperImpl();
    }

    /**
     * Creates a new instance of Game.
     * @return a new Game object
     */
    @Override
    public Game newGame() {
        return new GameImpl();
    }

    /**
     * Creates a new instance of GameConsole.
     * @return a new GameConsole object
     */
    @Override
    public GameConsole newGameConsole() {
        return new GameConsoleImpl();
    }

    /**
     * Creates a new instance of GameEdition.
     * @return a new GameEdition object
     */
    @Override
    public GameEdition newGameEdition() {
        return new GameEditionImpl();
    }

    /**
     * Creates a new instance of Genre.
     * @return a new Genre object
     */
    @Override
    public Genre newGenre() {
        return new GenreImpl();
    }

    /**
     * Creates a new instance of Publisher.
     * @return a new Publisher object
     */
    @Override
    public Publisher newPublisher() {
        return new PublisherImpl();
    }
}