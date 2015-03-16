/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.twitter.impl;

import io.robonews.service.twitter.TwitterService;
import io.robonews.service.twitter.model.TwitterProfile;

import java.util.ArrayList;
import java.util.List;

import org.springframework.social.twitter.api.Twitter;

public class TwitterServiceImpl implements TwitterService {

    Twitter twitter;

    public TwitterServiceImpl(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public List<TwitterProfile> searchProfiles(String query) {
        return searchProfiles(query, 10);
    }

    @Override
    public List<TwitterProfile> searchProfiles(String query, int limit) {
        List<TwitterProfile> result = new ArrayList<>();
        List<org.springframework.social.twitter.api.TwitterProfile> twitterResult = twitter.userOperations().searchForUsers(query, 1, limit);

        if (twitterResult != null) {
            for (org.springframework.social.twitter.api.TwitterProfile p : twitterResult) {
                TwitterProfile profile = new TwitterProfile();

                profile.setId(p.getId());
                profile.setScreenName(p.getScreenName());
                profile.setName(p.getName());
                profile.setUrl(p.getUrl());
                profile.setProfileImageUrl(p.getProfileImageUrl());
                profile.setDescription(p.getDescription());
                profile.setLocation(p.getLocation());
                profile.setCreatedDate(p.getCreatedDate());
                profile.setLanguage(p.getLanguage());
                profile.setStatusesCount(p.getStatusesCount());
                profile.setFriendsCount(p.getFriendsCount());
                profile.setFollowersCount(p.getFollowersCount());
                profile.setFavoritesCount(p.getFavoritesCount());
                profile.setListedCount(p.getListedCount());
                profile.setFollowing(p.isFollowing());
                profile.setFollowRequestSent(p.isFollowRequestSent());
                profile.setProtected(p.isProtected());
                profile.setNotificationsEnabled(p.isNotificationsEnabled());
                profile.setVerified(p.isVerified());
                profile.setGeoEnabled(p.isGeoEnabled());
                profile.setContributorsEnabled(p.isContributorsEnabled());
                profile.setTranslator(p.isTranslator());
                profile.setTimeZone(p.getTimeZone());
                profile.setUtcOffset(p.getUtcOffset());
                profile.setSidebarBorderColor(p.getSidebarBorderColor());
                profile.setSidebarFillColor(p.getSidebarFillColor());
                profile.setBackgroundColor(p.getBackgroundColor());
                profile.setUseBackgroundImage(p.useBackgroundImage());
                profile.setBackgroundImageUrl(p.getBackgroundImageUrl());
                profile.setBackgroundImageTiled(p.isBackgroundImageTiled());
                profile.setTextColor(p.getTextColor());
                profile.setLinkColor(p.getLinkColor());
                profile.setShowAllInlineMedia(p.showAllInlineMedia());

                result.add(profile);
            }
        }

        return result;
    }

    @Override
    public TwitterProfile getProfile(String id) {
        return null;
    }

    @Override
    public String getProfilePictureUrl(String id) {
        try {
            org.springframework.social.twitter.api.TwitterProfile
                    profile = twitter.userOperations().getUserProfile(id);

            return profile.getProfileImageUrl();
        }
        catch (RuntimeException e) {
            return null;
        }
    }
}
