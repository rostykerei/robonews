package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import org.springframework.transaction.annotation.Transactional;

public class TagDaoHibernate extends AbstractDaoHibernate<Tag, Integer> implements TagDao {

    public TagDaoHibernate() {
        super(Tag.class);
    }


    @Override
    @Transactional
    public Tag createTagWithAlternative(String tagName, Tag.Type type, String freebaseMid,
                                        boolean isAmbiguous, String altName, float altConfidence) {

        Tag tag = new Tag();

        tag.setType(type);
        tag.setFreebaseMid(freebaseMid);
        tag.setName(tagName);
        tag.setAmbiguous(isAmbiguous);

        TagAlternative tagAlternative = new TagAlternative();
        tagAlternative.setName(altName);
        tagAlternative.setType(type);
        tagAlternative.setConfidence(altConfidence);

        tagAlternative.setTag(tag);
        tag.getTagAlternatives().add(tagAlternative);

        create(tag);

        return tag;
    }

    @Override
    @Transactional
    public TagAlternative createTagAlternative(Tag tag, Tag.Type altType, String altName, float altConfidence) {
        TagAlternative tagAlternative = new TagAlternative();
        tagAlternative.setName(altName);
        tagAlternative.setType(altType);
        tagAlternative.setConfidence(altConfidence);

        //tag.getTagAlternatives().add(tagAlternative);
        tagAlternative.setTag(tag);

        getSession().save(tagAlternative);

        return tagAlternative;
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByFreebaseMind(String freebaseMid) {
        return (Tag) getSession().
                createQuery("from Tag t " +
                        "where t.freebaseMid = :freebaseMid").
                setString("freebaseMid", freebaseMid).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByAlternative(String altName, Tag.Type type) {
        return (Tag) getSession().
                createQuery("select t from Tag t " +
                        "left join t.tagAlternatives alt " +
                        "where alt.name = :altName and alt.type = :type").
                setString("altName", altName).
                setInteger("type", type.getId()).
                setMaxResults(1).
                uniqueResult();
    }
}