package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;
import org.springframework.transaction.annotation.Transactional;

public class TagDaoHibernate extends AbstractDaoHibernate<Tag, Integer> implements TagDao {

    public TagDaoHibernate() {
        super(Tag.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByFreebaseMid(String freebaseMid) {
        return (Tag) getSession().
                createQuery("from Tag t where t.freebaseMid = :mid").
                setString("mid", freebaseMid).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByAlternative(String altName) {
        return (Tag) getSession().
                createQuery("select t from Tag t " +
                        "left join t.tagAlternatives alt " +
                        "where alt.name = :altName").
                setString("altName", altName).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional
    public void logAmbiguous(String ambiguousName) {
        int count = getSession().createQuery("update TagAmbiguous t " +
                        "set t.effort = (t.effort + 1) " +
                        "where t.name = :name").
                    setString("name", ambiguousName).
                    executeUpdate();

        if (count < 1) {
            TagAmbiguous tagAmbiguous = new TagAmbiguous();
            tagAmbiguous.setName(ambiguousName);
            tagAmbiguous.setEffort(1);

            getSession().save(tagAmbiguous);
        }
        else {
            getSession().clear();
        }
    }

    @Override
    public TagAmbiguous findTagAmbiguous(String ambiguousName) {
        return (TagAmbiguous) getSession().
                createQuery("from TagAmbiguous ta where ta.name = :name").
                setString("name", ambiguousName).
                setMaxResults(1).
                uniqueResult();
    }
}
