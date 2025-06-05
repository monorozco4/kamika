package cat.uvic.teknos.dam.kamika.repositories;

import cat.uvic.teknos.dam.kamika.model.Game;

import java.util.Set;

public interface Repository<K, V> {
    V get(K key);
    void save (V item);

    Game save(Game game);

    void delete (K id);
    Set<V> getAll();
}
