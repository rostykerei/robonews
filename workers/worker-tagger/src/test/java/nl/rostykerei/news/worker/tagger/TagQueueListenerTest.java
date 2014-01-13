package nl.rostykerei.news.worker.tagger;

import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.messaging.domain.TagMessage;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.AmbiguousResultException;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.exception.NotFoundException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private TagQueueListener tagQueueListener = new TagQueueListener();

    @Test
    public void testCreateNewPersonTag() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("person");
        story.setDescription("new");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        Set<NamedEntity> newPersonSet = new HashSet<NamedEntity>();
        newPersonSet.add(new NamedEntity(NamedEntity.Type.PERSON, "new-person"));
        when(namedEntityRecognizerService.getNamedEntities("person. new.")).thenReturn(newPersonSet);

        FreebaseSearchResult persRes = new FreebaseSearchResult();
        persRes.setMid("pers");
        persRes.setName("freebase-person");
        persRes.setType(FreebaseSearchResult.Type.PERSON);
        persRes.setScore(1);
        when(freebaseService.searchPerson("new-person")).thenReturn(persRes);

        when(tagDao.findByFreebaseMid("pers")).thenReturn(null);
        Tag personTag = new Tag();
        personTag.setId(200L);
        when(tagDao.createTagWithAlternative("freebase-person", Tag.Type.PERSON, "pers", false, "new-person", Tag.Type.PERSON, 1f)).thenReturn(personTag);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("person. new.");
        verify(tagDao).findByAlternative("new-person", Tag.Type.PERSON);
        verify(freebaseService).searchPerson("new-person");
        verify(tagDao).createTagWithAlternative("freebase-person", Tag.Type.PERSON, "pers", false, "new-person", Tag.Type.PERSON, 1f);
        verify(storyDao).saveStoryTag(new StoryTag(story, personTag));
    }

    @Test
    public void testCreateExistentPersonTag() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("person");
        story.setDescription("existent");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        Set<NamedEntity> existentPersonSet = new HashSet<NamedEntity>();
        existentPersonSet.add(new NamedEntity(NamedEntity.Type.PERSON, "existent-person"));
        when(namedEntityRecognizerService.getNamedEntities("person. existent.")).thenReturn(existentPersonSet);

        Tag existentTag = new Tag();
        existentTag.setId(100L);
        when(tagDao.findByAlternative("existent-person", Tag.Type.PERSON)).thenReturn(existentTag);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("person. existent.");
        verify(tagDao).findByAlternative("existent-person", Tag.Type.PERSON);
        verify(storyDao).saveStoryTag(new StoryTag(story, existentTag));
    }

    @Test
    public void testCreateAltLocationTag() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("test-location");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        FreebaseSearchResult locRes = new FreebaseSearchResult();
        locRes.setMid("loc");
        locRes.setName("freebase-location");
        locRes.setType(FreebaseSearchResult.Type.LOCATION);
        locRes.setScore(1);
        when(freebaseService.searchLocation("location")).thenReturn(locRes);

        Set<NamedEntity> locSet = new HashSet<NamedEntity>();
        locSet.add(new NamedEntity(NamedEntity.Type.LOCATION, "location"));
        when(namedEntityRecognizerService.getNamedEntities("test-location.")).thenReturn(locSet);

        Tag locationTag = new Tag();
        locationTag.setId(300L);
        when(tagDao.findByFreebaseMid("loc")).thenReturn(locationTag);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("test-location.");
        verify(tagDao).findByAlternative("location", Tag.Type.LOCATION);
        verify(freebaseService).searchLocation("location");
        verify(tagDao).createTagAlternative(locationTag, Tag.Type.LOCATION, "location", 1F);
        verify(storyDao).saveStoryTag(eq(new StoryTag(story, locationTag)));
    }

    @Test
    public void testCreateAltOrganizationTag() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("test-organization");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        FreebaseSearchResult orgRes = new FreebaseSearchResult();
        orgRes.setMid("org");
        orgRes.setName("freebase-organization");
        orgRes.setType(FreebaseSearchResult.Type.ORGANIZATION);
        orgRes.setScore(1);
        when(freebaseService.searchOrganization("organization")).thenReturn(orgRes);

        Tag organizationTag = new Tag();
        organizationTag.setId(400L);
        when(tagDao.findByFreebaseMid("org")).thenReturn(organizationTag);

        Set<NamedEntity> orgSet = new HashSet<NamedEntity>();
        orgSet.add(new NamedEntity(NamedEntity.Type.ORGANIZATION, "organization"));
        when(namedEntityRecognizerService.getNamedEntities("test-organization.")).thenReturn(orgSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("test-organization.");
        verify(tagDao).findByAlternative("organization", Tag.Type.ORGANIZATION);
        verify(freebaseService).searchOrganization("organization");
        verify(tagDao).createTagAlternative(organizationTag, Tag.Type.ORGANIZATION, "organization", 1F);
        verify(storyDao).saveStoryTag(new StoryTag(story, organizationTag));
    }

    @Test
    public void testCreateAltMiscTag() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("test-misc");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        FreebaseSearchResult miscRes = new FreebaseSearchResult();
        miscRes.setMid("misc");
        miscRes.setName("freebase-misc");
        miscRes.setType(FreebaseSearchResult.Type.MISC);
        miscRes.setScore(1);
        when(freebaseService.searchMisc("misc")).thenReturn(miscRes);

        Tag miscTag = new Tag();
        miscTag.setId(500L);
        when(tagDao.findByFreebaseMid("misc")).thenReturn(miscTag);

        Set<NamedEntity> miscSet = new HashSet<NamedEntity>();
        miscSet.add(new NamedEntity(NamedEntity.Type.MISC, "misc"));
        when(namedEntityRecognizerService.getNamedEntities("test-misc.")).thenReturn(miscSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("test-misc.");
        verify(tagDao).findByAlternative("misc", Tag.Type.MISC);
        verify(freebaseService).searchMisc("misc");
        verify(tagDao).createTagAlternative(miscTag, Tag.Type.MISC, "misc", 1F);
        verify(storyDao).saveStoryTag(new StoryTag(story, miscTag));
    }

    @Test
    public void testFreebaseNotFound() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("not-found");
        story.setDescription("exception");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        when(freebaseService.searchPerson("not-found-exception")).thenThrow(new NotFoundException());

        Tag ambiguousTag = new Tag();
        ambiguousTag.setId(600L);
        when(tagDao.createTagWithAlternative("not-found-exception", Tag.Type.PERSON, null, true, "not-found-exception", Tag.Type.PERSON, 0f)).thenReturn(ambiguousTag);

        Set<NamedEntity> notFoundSet = new HashSet<NamedEntity>();
        notFoundSet.add(new NamedEntity(NamedEntity.Type.PERSON, "not-found-exception"));
        when(namedEntityRecognizerService.getNamedEntities("not-found. exception.")).thenReturn(notFoundSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("not-found. exception.");
        verify(tagDao).findByAlternative("not-found-exception", Tag.Type.PERSON);
        verify(tagDao).createTagWithAlternative("not-found-exception", Tag.Type.PERSON, null, true, "not-found-exception", Tag.Type.PERSON, 0f);
        verify(storyDao).saveStoryTag(new StoryTag(story, ambiguousTag));
    }

    @Test
    public void testFreebaseAmbiguous() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("ambiguous");
        story.setDescription("exception");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        when(freebaseService.searchLocation("ambiguous-exception")).thenThrow(new AmbiguousResultException());

        Tag ambiguousTag = new Tag();
        ambiguousTag.setId(600L);
        when(tagDao.createTagWithAlternative("ambiguous-exception", Tag.Type.LOCATION, null, true, "ambiguous-exception", Tag.Type.LOCATION, 0f)).thenReturn(ambiguousTag);

        Set<NamedEntity> ambiguousSet = new HashSet<NamedEntity>();
        ambiguousSet.add(new NamedEntity(NamedEntity.Type.LOCATION, "ambiguous-exception"));
        when(namedEntityRecognizerService.getNamedEntities("ambiguous. exception.")).thenReturn(ambiguousSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("ambiguous. exception.");
        verify(tagDao).findByAlternative("ambiguous-exception", Tag.Type.LOCATION);
        verify(tagDao).createTagWithAlternative("ambiguous-exception", Tag.Type.LOCATION, null, true, "ambiguous-exception", Tag.Type.LOCATION, 0f);
        verify(storyDao).saveStoryTag(new StoryTag(story, ambiguousTag));
    }

    @Test
    public void testFreebaseException() throws  Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("freebase");
        story.setDescription("exception");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        Set<NamedEntity> exceptionSet = new HashSet<NamedEntity>();
        exceptionSet.add(new NamedEntity(NamedEntity.Type.ORGANIZATION, "freebase-exception"));
        when(namedEntityRecognizerService.getNamedEntities("freebase. exception.")).thenReturn(exceptionSet);

        when(freebaseService.searchOrganization("freebase-exception")).thenThrow(new FreebaseServiceException());

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("freebase. exception.");
        verify(tagDao).findByAlternative("freebase-exception", Tag.Type.ORGANIZATION);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testRuntimeException() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("runtime");
        story.setDescription("exception");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        when(freebaseService.searchMisc("runtime-exception")).thenThrow(new RuntimeException());

        Set<NamedEntity> runtimeExceptionSet = new HashSet<NamedEntity>();
        runtimeExceptionSet.add(new NamedEntity(NamedEntity.Type.MISC, "runtime-exception"));
        when(namedEntityRecognizerService.getNamedEntities("runtime. exception.")).thenReturn(runtimeExceptionSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("runtime. exception.");
        verify(tagDao).findByAlternative("runtime-exception", Tag.Type.MISC);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testNullResult() throws Exception {
        Story story = new Story();
        story.setId(1000L);
        story.setTitle("null");

        when(storyDao.getByIdWithTags(1000L)).thenReturn(story);

        TagMessage tagMessage1 = new TagMessage();
        tagMessage1.setStoryId(1000L);

        when(freebaseService.searchMisc("null-result")).thenReturn(null);

        Set<NamedEntity> nullSet = new HashSet<NamedEntity>();
        nullSet.add(new NamedEntity(NamedEntity.Type.MISC, "null-result"));
        when(namedEntityRecognizerService.getNamedEntities("null.")).thenReturn(nullSet);

        // Test call
        tagQueueListener.listen(tagMessage1);

        verify(namedEntityRecognizerService).getNamedEntities("null.");
        verify(tagDao).findByAlternative("null-result", Tag.Type.MISC);

        // No tags
        verify(storyDao, times(0)).saveStoryTag(any(StoryTag.class));
    }

    @Test
    public void testListenUnknownStory() {
        TagMessage unknownStoryMessage = new TagMessage();
        unknownStoryMessage.setStoryId(8000L);

        tagQueueListener.listen(unknownStoryMessage);
    }
}
