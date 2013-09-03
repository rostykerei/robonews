package nl.rostykerei.news.service.nlp.impl;

import nl.rostykerei.news.service.core.NamedEntityType;

/**
 * Created with IntelliJ IDEA on 8/20/13 at 10:28 AM
 *
 * @author Rosty Kerei
 */
public class NamedEntity {

    private NamedEntityType type;
    private String name;

    public NamedEntity(NamedEntityType type, String name) {
        this.type = type;
        this.name = name;
    }

    public NamedEntityType getType() {
        return type;
    }

    public void setType(NamedEntityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamedEntity that = (NamedEntity) o;

        if (name != null ? !name.equalsIgnoreCase(that.name) : that.name != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.toLowerCase().hashCode() : 0);
        return result;
    }
}
