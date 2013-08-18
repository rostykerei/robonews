package nl.rostykerei.news.domain;

import java.util.HashMap;
import java.util.Map;

public enum NamedEntityType {

    PERSON(1),
    LOCATION(2),
    ORGANIZATION(3),
    MISC(4);

    private int id;

    private static final Map<Integer, NamedEntityType> lookup = new HashMap<Integer, NamedEntityType>();

    static {
        for (NamedEntityType item : NamedEntityType.values()) {
            lookup.put(item.getId(), item);
        }
    }

    private NamedEntityType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static NamedEntityType getType(int id) {
        return lookup.get(id);
    }
}
