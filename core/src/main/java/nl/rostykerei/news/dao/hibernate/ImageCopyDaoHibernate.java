package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.ImageCopyDao;
import nl.rostykerei.news.domain.ImageCopy;

public class ImageCopyDaoHibernate extends AbstractDaoHibernate<ImageCopy, Integer> implements ImageCopyDao {

    public ImageCopyDaoHibernate() {
        super(ImageCopy.class);
    }
}
