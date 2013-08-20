package nl.rostykerei.news.service.nlp;

import java.util.Set;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;

/**
 * Created with IntelliJ IDEA on 8/20/13 at 10:28 AM
 *
 * @author Rosty Kerei
 */
public interface NamedEntityRecognizerService {

    Set<NamedEntity> getNamedEntities(String text);

}
