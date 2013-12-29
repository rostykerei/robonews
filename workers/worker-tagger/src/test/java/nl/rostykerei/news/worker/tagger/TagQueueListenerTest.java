package nl.rostykerei.news.worker.tagger;

import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.messaging.domain.NewStoryMessage;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.AmbiguousResultException;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.exception.NotFoundException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagQueueListenerTest {

    @Mock
    private StoryDao storyDao;

    @Mock
    private TagDao tagDao;

    @Mock
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Mock
    private FreebaseService freebaseService;

    @InjectMocks
    private TagQueueListener tagQueueListener;

    private Tag existentTag;

    private Tag persTag;
    private Tag locTag;
    private Tag orgTag;
    private Tag miscTag;
    private Tag ambiguousTag;

    @Before
    public void setUp()  throws Exception {

        // Freebase service mocks

        FreebaseSearchResult persRes = new FreebaseSearchResult();
        persRes.setMid("pers");
        persRes.setName("freebase-person");
        persRes.setType(FreebaseSearchResult.Type.PERSON);
        persRes.setScore(1);
        when(freebaseService.searchPerson("new-person")).thenReturn(persRes);

        FreebaseSearchResult locRes = new FreebaseSearchResult();
        locRes.setMid("loc");
        locRes.setName("freebase-location");
        locRes.setType(FreebaseSearchResult.Type.LOCATION);
        locRes.setScore(1);
        when(freebaseService.searchLocation("location")).thenReturn(locRes);

        FreebaseSearchResult orgRes = new FreebaseSearchResult();
        orgRes.setMid("org");
        orgRes.setName("freebase-organization");
        orgRes.setType(FreebaseSearchResult.Type.ORGANIZATION);
        orgRes.setScore(1);
        when(freebaseService.searchOrganization("organization")).thenReturn(orgRes);

        FreebaseSearchResult miscRes = new FreebaseSearchResult();
        miscRes.setMid("misc");
        miscRes.setName("freebase-misc");
        miscRes.setType(FreebaseSearchResult.Type.MISC);
        miscRes.setScore(1);
        when(freebaseService.searchMisc("misc")).thenReturn(miscRes);

        when(freebaseService.searchPerson("not-found-exception")).thenThrow(new NotFoundException());
        when(freebaseService.searchLocation("ambiguous-exception")).thenThrow(new AmbiguousResultException());
        when(freebaseService.searchOrganization("freebase-exception")).thenThrow(new FreebaseServiceException());
        when(freebaseService.searchMisc("runtime-exception")).thenThrow(new RuntimeException());
        when(freebaseService.searchMisc("null-result")).thenReturn(null);

        // TagDao mocks

        // existent
        existentTag = new Tag();
        existentTag.setId(100L);
        when(tagDao.findByAlternative("existent-person", Tag.Type.PERSON)).thenReturn(existentTag);

        // person tag (new)
        when(tagDao.findByFreebaseMind("pers")).thenReturn(null);
        persTag = new Tag();
        persTag.setId(200L);
        when(tagDao.createTagWithAlternative("freebase-person", Tag.Type.PERSON, "pers", false, "new-person", Tag.Type.PERSON, 1f)).thenReturn(persTag);

        // location tag
        locTag = new Tag();
        locTag.setId(300L);
        when(tagDao.findByFreebaseMind("loc")).thenReturn(locTag);

        // organization tag
        orgTag = new Tag();
        orgTag.setId(400L);
        when(tagDao.findByFreebaseMind("org")).thenReturn(orgTag);

        // misc tag
        miscTag = new Tag();
        miscTag.setId(500L);
        when(tagDao.findByFreebaseMind("misc")).thenReturn(miscTag);

        // ambiguous
        ambiguousTag = new Tag();
        ambiguousTag.setId(600L);
        when(tagDao.createTagWithAlternative("not-found-exception", Tag.Type.PERSON, null, true, "not-found-exception", Tag.Type.PERSON, 0f)).thenReturn(ambiguousTag);
        when(tagDao.createTagWithAlternative("ambiguous-exception", Tag.Type.LOCATION, null, true, "ambiguous-exception", Tag.Type.LOCATION, 0f)).thenReturn(ambiguousTag);

        // NLP service

        Set<NamedEntity> newPersonSet = new HashSet<NamedEntity>();
        newPersonSet.add(new NamedEntity(NamedEntity.Type.PERSON, "new-person"));
        when(namedEntityRecognizerService.getNamedEntities("person. new")).thenReturn(newPersonSet);

        Set<NamedEntity> existentPersonSet = new HashSet<NamedEntity>();
        existentPersonSet.add(new NamedEntity(NamedEntity.Type.PERSON, "existent-person"));
        when(namedEntityRecognizerService.getNamedEntities("person. existent")).thenReturn(existentPersonSet);

        Set<NamedEntity> locSet = new HashSet<NamedEntity>();
        locSet.add(new NamedEntity(NamedEntity.Type.LOCATION, "location"));
        when(namedEntityRecognizerService.getNamedEntities("location. ")).thenReturn(locSet);

        Set<NamedEntity> orgSet = new HashSet<NamedEntity>();
        orgSet.add(new NamedEntity(NamedEntity.Type.ORGANIZATION, "organization"));
        when(namedEntityRecognizerService.getNamedEntities("organization. ")).thenReturn(orgSet);

        Set<NamedEntity> miscSet = new HashSet<NamedEntity>();
        miscSet.add(new NamedEntity(NamedEntity.Type.MISC, "misc"));
        when(namedEntityRecognizerService.getNamedEntities("misc. ")).thenReturn(miscSet);

        Set<NamedEntity> notFoundSet = new HashSet<NamedEntity>();
        notFoundSet.add(new NamedEntity(NamedEntity.Type.PERSON, "not-found-exception"));
        when(namedEntityRecognizerService.getNamedEntities("not-found. exception")).thenReturn(notFoundSet);

        Set<NamedEntity> ambiguousSet = new HashSet<NamedEntity>();
        ambiguousSet.add(new NamedEntity(NamedEntity.Type.LOCATION, "ambiguous-exception"));
        when(namedEntityRecognizerService.getNamedEntities("ambiguous. exception")).thenReturn(ambiguousSet);

        Set<NamedEntity> exceptionSet = new HashSet<NamedEntity>();
        exceptionSet.add(new NamedEntity(NamedEntity.Type.ORGANIZATION, "freebase-exception"));
        when(namedEntityRecognizerService.getNamedEntities("freebase. exception")).thenReturn(exceptionSet);

        Set<NamedEntity> runtimeExceptionSet = new HashSet<NamedEntity>();
        runtimeExceptionSet.add(new NamedEntity(NamedEntity.Type.MISC, "runtime-exception"));
        when(namedEntityRecognizerService.getNamedEntities("runtime. exception")).thenReturn(runtimeExceptionSet);

        Set<NamedEntity> nullSet = new HashSet<NamedEntity>();
        nullSet.add(new NamedEntity(NamedEntity.Type.MISC, "null-result"));
        when(namedEntityRecognizerService.getNamedEntities("null. ")).thenReturn(nullSet);
    }

    @Test
    public void testCreateNewPersonTag() throws Exception {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("person");
        when(story.getDescription()).thenReturn("new");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("person. new");
        verify(tagDao).findByAlternative("new-person", Tag.Type.PERSON);
        verify(freebaseService).searchPerson("new-person");
        verify(tagDao).createTagWithAlternative("freebase-person", Tag.Type.PERSON, "pers", false, "new-person", Tag.Type.PERSON, 1f);
        verify(storyDao).saveStoryTag(new StoryTag(story, persTag));
    }

    @Test
    public void testCreateExistentPersonTag() throws Exception {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("person");
        when(story.getDescription()).thenReturn("existent");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("person. existent");
        verify(tagDao).findByAlternative("existent-person", Tag.Type.PERSON);
        verify(storyDao).saveStoryTag(new StoryTag(story, existentTag));
    }

    @Test
    public void testCreateAltLocationTag() throws Exception {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("location");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("location. ");
        verify(tagDao).findByAlternative("location", Tag.Type.LOCATION);
        verify(freebaseService).searchLocation("location");
        verify(tagDao).createTagAlternative(locTag, Tag.Type.LOCATION, "location", 1F);
        verify(storyDao).saveStoryTag(new StoryTag(story, locTag));
    }

    @Test
    public void testCreateAltOrganizationTag() throws Exception {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("organization");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("organization. ");
        verify(tagDao).findByAlternative("organization", Tag.Type.ORGANIZATION);
        verify(freebaseService).searchOrganization("organization");
        verify(tagDao).createTagAlternative(orgTag, Tag.Type.ORGANIZATION, "organization", 1F);
        verify(storyDao).saveStoryTag(new StoryTag(story, orgTag));
    }

    @Test
    public void testCreateAltMiscTag() throws Exception {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("misc");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("misc. ");
        verify(tagDao).findByAlternative("misc", Tag.Type.MISC);
        verify(freebaseService).searchMisc("misc");
        verify(tagDao).createTagAlternative(miscTag, Tag.Type.MISC, "misc", 1F);
        verify(storyDao).saveStoryTag(new StoryTag(story, miscTag));
    }

    @Test
    public void testFreebaseNotFound() {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("not-found");
        when(story.getDescription()).thenReturn("exception");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("not-found. exception");
        verify(tagDao).findByAlternative("not-found-exception", Tag.Type.PERSON);
        verify(tagDao).createTagWithAlternative("not-found-exception", Tag.Type.PERSON, null, true, "not-found-exception", Tag.Type.PERSON, 0f);
        verify(storyDao).saveStoryTag(new StoryTag(story, ambiguousTag));
    }

    @Test
    public void testFreebaseAmbiguous() {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("ambiguous");
        when(story.getDescription()).thenReturn("exception");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("ambiguous. exception");
        verify(tagDao).findByAlternative("ambiguous-exception", Tag.Type.LOCATION);
        verify(tagDao).createTagWithAlternative("ambiguous-exception", Tag.Type.LOCATION, null, true, "ambiguous-exception", Tag.Type.LOCATION, 0f);
        verify(storyDao).saveStoryTag(new StoryTag(story, ambiguousTag));
    }

    @Test
    public void testFreebaseException() {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("freebase");
        when(story.getDescription()).thenReturn("exception");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("freebase. exception");
        verify(tagDao).findByAlternative("freebase-exception", Tag.Type.ORGANIZATION);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testRuntimeException() {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("runtime");
        when(story.getDescription()).thenReturn("exception");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("runtime. exception");
        verify(tagDao).findByAlternative("runtime-exception", Tag.Type.MISC);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testNullResult() {
        Story story = mock(Story.class);
        when(story.getId()).thenReturn(1000L);
        when(story.getTitle()).thenReturn("null");

        when(storyDao.getById(1000L)).thenReturn(story);

        NewStoryMessage newStoryMessage1 = new NewStoryMessage();
        newStoryMessage1.setId(1000L);

        // Test call
        tagQueueListener.listen(newStoryMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("null. ");
        verify(tagDao).findByAlternative("null-result", Tag.Type.MISC);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testListenUnknownStory() {
        NewStoryMessage unknownStoryMessage = new NewStoryMessage();
        unknownStoryMessage.setId(8000L);

        tagQueueListener.listen(unknownStoryMessage);
    }
}
