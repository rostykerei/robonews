/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.google.plus.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.api.services.plus.model.Person;
import io.robonews.service.google.plus.GooglePlusService;
import io.robonews.service.google.plus.model.GooglePlusProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rosty on 13/02/15.
 */
public class GooglePlusServiceImpl implements GooglePlusService {

    private Plus service;

    public GooglePlusServiceImpl(String applicationName, String accountId, InputStream privateKey) throws GeneralSecurityException, IOException {
        final HttpTransport transport = new NetHttpTransport();
        final JacksonFactory jsonFactory = new JacksonFactory();

        PrivateKey pk = SecurityUtils.loadPrivateKeyFromKeyStore(
                SecurityUtils.getPkcs12KeyStore(),
                privateKey,
                "notasecret",
                "privatekey",
                "notasecret");

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(accountId)
                .setServiceAccountPrivateKey(pk)
                .setServiceAccountScopes(PlusScopes.all())
                .build();

        service = new Plus
                .Builder(transport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
    }

    @Override
    public List<GooglePlusProfile> searchProfiles(String query) {
        return searchProfiles(query, 10);
    }

    @Override
    public List<GooglePlusProfile> searchProfiles(String query, int limit) {
        try {
            Plus.People.Search searchPeople = service.people().search(query);
            searchPeople.setMaxResults((long) limit);

            PeopleFeed peopleFeed = searchPeople.execute();
            List<Person> people = peopleFeed.getItems();

            List<GooglePlusProfile> result = new ArrayList<>();

            for (Person person : people) {
                GooglePlusProfile profile = new GooglePlusProfile();
                profile.setId(person.getId());
                profile.setName(person.getDisplayName());

                String url = person.getUrl();

                profile.setUrl(url);

                if (url != null && url.contains("+")) {
                    profile.setPlusName(url.substring(url.indexOf('+')));
                }
                else {
                    profile.setPlusName(person.getId());
                }

                if (person.getImage() != null) {
                    profile.setImageUrl(person.getImage().getUrl());
                }

                result.add(profile);
            }

            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GooglePlusProfile getProfile(String id) {
        return null;
    }

    @Override
    public String getProfilePictureUrl(String id) {
        try {
            Person person = service.people().get(id).execute();

            if (person != null && person.getImage() != null) {
                return person.getImage().getUrl();
            }

            return null;

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
