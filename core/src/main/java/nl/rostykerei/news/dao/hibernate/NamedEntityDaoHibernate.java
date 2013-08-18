package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.NamedEntityDao;
import nl.rostykerei.news.domain.NamedEntity;
import nl.rostykerei.news.domain.NamedEntityType;
import org.springframework.transaction.annotation.Transactional;

public class NamedEntityDaoHibernate extends AbstractDaoHibernate<NamedEntity, Integer> implements NamedEntityDao {

    public NamedEntityDaoHibernate() {
        super(NamedEntity.class);
    }

    @Override
    @Transactional
    public NamedEntity findOrCreateNamedEntity(String name, NamedEntityType type) {
        NamedEntity existed = (NamedEntity) getSession().
                createQuery("from NamedEntity ne" +
                        " where ne.name = :name and ne.type = :type").
                setString("name", name).
                setInteger("type", type.getId()).
                setMaxResults(1).
                uniqueResult();

        if (existed != null) {
            return existed;
        }
        else {
            NamedEntity newEntity = new NamedEntity();
            newEntity.setName(name);
            newEntity.setType(type);

            create(newEntity);

            return newEntity;
        }
    }
}
