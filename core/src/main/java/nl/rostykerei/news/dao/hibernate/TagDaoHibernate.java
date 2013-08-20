package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Tag;
import org.springframework.transaction.annotation.Transactional;

public class TagDaoHibernate extends AbstractDaoHibernate<Tag, Integer> implements TagDao {

    public TagDaoHibernate() {
        super(Tag.class);
    }

    @Override
    @Transactional
    public Tag findOrCreateNamedEntity(String name, Tag.Type type) {
        Tag existed = (Tag) getSession().
                createQuery("from Tag ne" +
                        " where ne.name = :name and ne.type = :type").
                setString("name", name).
                setInteger("type", type.getId()).
                setMaxResults(1).
                uniqueResult();

        if (existed != null) {
            return existed;
        }
        else {
            Tag newEntity = new Tag();
            newEntity.setName(name);
            newEntity.setType(type);

            super.create(newEntity);

            return newEntity;
        }
    }

    @Override
    public Integer create(Tag o) {
        throw new IllegalArgumentException("Use TagDao.findOrCreateNamedEntity instead");
    }

    @Override
    public void createOrUpdate(Tag o) {
        throw new IllegalArgumentException("Use TagDao.findOrCreateNamedEntity instead");
    }
}
