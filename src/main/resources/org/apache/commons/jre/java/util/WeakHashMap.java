package java.util;

import java.util.HashMap;

public class WeakHashMap<K, V> extends HashMap<K, V> {
	private static final long serialVersionUID = -6255993066795301169L;

	public WeakHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public WeakHashMap(int initialCapacity) {
        super(initialCapacity);
    }

	public WeakHashMap() {
        super();
    }
}
