package cat.uvic.teknos.dam.kamika.model;

public interface ModelFactory {
    Console newConsole();
    Developer newDeveloper();
    Game newGame();
    GameConsole newGameConsole();
    GameEdition newGameEdition();
    Genre newGenre();
    Publisher newPublisher();
}
