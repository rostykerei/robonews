package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.ImageDao;
import nl.rostykerei.news.domain.Image;

public class ImageDaoHibernate extends AbstractDaoHibernate<Image, Integer> implements ImageDao {

    public ImageDaoHibernate() {
        super(Image.class);
    }

    @Override
    public Image getByUrl(String url) {
        return (Image) getSession().createQuery("from Image i " +
                "where i.url = :url").
                setString("url", url).
                setMaxResults(1).
                uniqueResult();
    }
}
