package io.robonews.dao.hibernate;

import io.robonews.dao.CountryDao;
import io.robonews.domain.Country;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rosty on 27/04/15.
 */
public class CountryDaoHibernate extends AbstractDaoHibernate<Country, Integer> implements CountryDao {

    public CountryDaoHibernate() {
        super(Country.class);
    }

    @Override
    public Integer create(Country o) {
        throw new RuntimeException("Intentionally not implemented");
    }

    @Override
    public void update(Country o) {
        throw new RuntimeException("Intentionally not implemented");
    }

    @Override
    public void createOrUpdate(Country o) {
        throw new RuntimeException("Intentionally not implemented");
    }

    @Override
    public void delete(Country o) {
        throw new RuntimeException("Intentionally not implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public Country getByIsoCode2(String isoCode2) {
        return (Country) getSession()
                .createQuery("from Country where isoCode2 = :isoCode2")
                .setString("isoCode2", isoCode2)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Country getByIsoCode3(String isoCode3) {
        return (Country) getSession()
                .createQuery("from Country where isoCode3 = :isoCode3")
                .setString("isoCode3", isoCode3)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<Country> getAll() {
        return getSession().
                createQuery("from Country order by name asc").
                list();
    }
}
