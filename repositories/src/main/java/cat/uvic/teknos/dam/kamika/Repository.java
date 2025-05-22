package cat.uvic.teknos.dam.kamika;

import java.util.Set;

public interface Repository<K, V> {
    V get(K key);
    void save (V item);
    void delete (K id);
    Set<V> getAll();
}
