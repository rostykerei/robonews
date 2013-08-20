package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.transaction.annotation.Transactional;

public class TagDaoHibernate extends AbstractDaoHibernate<Tag, Integer> implements TagDao {

    public TagDaoHibernate() {
        super(Tag.class);
    }

    @Override
    @Transactional
    public Tag findOrCreateTagWithAlternative(String tagName, String alternativeName,
                                              float alternativeConfidence,
                                              String freebaseMid, Tag.Type type) {
        Tag tag = (Tag) getSession().
                createQuery("from Tag t where t.freebaseMid = :mid").
                setString("mid", freebaseMid).
                setMaxResults(1).
                uniqueResult();

        if (tag == null) {
            tag = new Tag();

            tag.setType(type);
            tag.setFreebaseMid(freebaseMid);
            tag.setName(tagName);
        }

        TagAlternative tagAlternative = new TagAlternative();
        tagAlternative.setName(alternativeName);
        tagAlternative.setConfidence(alternativeConfidence);

        tagAlternative.setTag(tag);
        tag.getTagAlternatives().add(tagAlternative);

        try {
            createOrUpdate(tag);
        }
        catch (RuntimeException e) {
            throw  e;
        }


        return tag;
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
    @Transactional(readOnly = true)
    public TagAmbiguous findTagAmbiguous(String ambiguousName) {
        return (TagAmbiguous) getSession().
                createQuery("from TagAmbiguous ta where ta.name = :name").
                setString("name", ambiguousName).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional
    public void createTagAmbiguous(String ambiguousName) {
        TagAmbiguous tagAmbiguous = new TagAmbiguous();
        tagAmbiguous.setName(ambiguousName);
        tagAmbiguous.setEffort(1);

        saveTagAmbiguous(tagAmbiguous);
    }

    @Override
    @Transactional
    public void saveTagAmbiguous(TagAmbiguous tagAmbiguous) {
        getSession().saveOrUpdate(tagAmbiguous);
    }
}
