package nl.rostykerei.news.service.syndication.rome;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import nl.rostykerei.news.service.syndication.SyndicationEntry;
import nl.rostykerei.news.service.syndication.SyndicationFeed;
import org.springframework.util.StringUtils;

public class SyndicationFeedRome implements SyndicationFeed {

    private SyndFeed syndFeed;

    public SyndicationFeedRome(SyndFeed syndFeed) {
        this.syndFeed = syndFeed;
    }

    @Override
    public String getTitle() {
        if (syndFeed.getTitle() != null) {
            return syndFeed.getTitle().trim();
        }

        return null;
    }

    @Override
    public String getDescription() {
        if (syndFeed.getDescription() != null) {
            return syndFeed.getDescription().trim();
        }

        return null;
    }

    @Override
    public String getLink() {
        if (syndFeed.getLink() != null) {
            return syndFeed.getLink().trim();
        }

        return null;
    }

    @Override
    public String getCopyright() {
        if (syndFeed.getCopyright() != null) {
            return syndFeed.getCopyright().trim();
        }

        return null;
    }

    @Override
    public String getAuthor() {
        if (syndFeed.getAuthor() != null) {
            return syndFeed.getAuthor().trim();
        }

        return null;
    }

    @Override
    public String getImageUrl() {
        if (syndFeed.getImage() != null && syndFeed.getImage().getUrl() != null) {
            return syndFeed.getImage().getUrl().trim();
        }

        return null;
    }

    @Override
    public List<SyndicationEntry> getEntries() {
        List<SyndicationEntry> list = new ArrayList<SyndicationEntry>();

        Iterator iterator = syndFeed.getEntries().iterator();
        while (iterator.hasNext()) {
            SyndEntry syndEntry = (SyndEntry) iterator.next();

            if (StringUtils.isEmpty(syndEntry.getTitle())) {
                continue;
            }

            if (StringUtils.isEmpty(syndEntry.getLink())) {
                continue;
            }

            if (StringUtils.isEmpty(syndEntry.getUri())) {
                continue;
            }

            if (syndEntry.getPublishedDate() == null) {
                syndEntry.setPublishedDate(new Date());
            }

            list.add(new SyndicationEntryRome(syndEntry));
        }

        return list;
    }

    @Override
    public double estimateVelocity() {
        int size = syndFeed.getEntries().size();
        if (size < 2) {
            return 0;
        }

        long minDate = Long.MAX_VALUE;
        long maxDate = 0;

        Iterator iterator = syndFeed.getEntries().iterator();
        while (iterator.hasNext()) {
            SyndEntry entry = (SyndEntry) iterator.next();
            long pubDate = entry.getPublishedDate().getTime();

            minDate = pubDate < minDate ? pubDate : minDate;
            maxDate = pubDate > maxDate ? pubDate : maxDate;
        }

        long dateDiff = maxDate - minDate;

        if (dateDiff > 0) {
            return (size * 3600000L) / (double) dateDiff;
        }

        return 0;
    }
}
