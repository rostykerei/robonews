/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.service.storage.rackspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import nl.rostykerei.news.service.storage.StorageService;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;

public class StorageServiceRackspace implements StorageService {

    private String apiUsername;

    private String apiKey;

    private String container;

    public StorageServiceRackspace(String apiUsername, String apiKey, String container) {
        this.apiUsername = apiUsername;
        this.apiKey = apiKey;
        this.container = container;
    }

    @Override
    public void putFile(File source, String filename, String directory, String contentType, Date deleteAfterDate) throws IOException {
        ContextBuilder contextBuilder = ContextBuilder.
                newBuilder("cloudfiles-us").
                credentials(this.apiUsername, this.apiKey);

        BlobStore blobStore = contextBuilder.buildView(BlobStoreContext.class).getBlobStore();

        Blob blob = blobStore
            .blobBuilder(directory + "/" + filename)
            .payload(new FileInputStream(source))
            .expires(deleteAfterDate)
            .contentType(contentType)
            .build();

        blobStore.putBlob(this.container, blob);
    }
}
