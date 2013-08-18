package nl.rostykerei.news.nlp;

import nl.rostykerei.news.domain.NamedEntityType;

public class TextNamedEntity {

    private String name;

    private NamedEntityType type;

    public TextNamedEntity(String name, NamedEntityType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NamedEntityType getType() {
        return type;
    }

    public void setType(NamedEntityType type) {
        this.type = type;
    }

    public String toString() {
        return "[" + type.toString() + ": " + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextNamedEntity that = (TextNamedEntity) o;

        if (name != null ? !name.equalsIgnoreCase(that.name) : that.name != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.toUpperCase().hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
