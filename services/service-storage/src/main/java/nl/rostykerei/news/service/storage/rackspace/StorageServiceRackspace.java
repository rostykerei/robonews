package nl.rostykerei.news.service.storage.rackspace;

import nl.rostykerei.news.service.storage.StorageService;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.openstack.swift.CommonSwiftAsyncClient;
import org.jclouds.openstack.swift.CommonSwiftClient;
import org.jclouds.openstack.swift.domain.SwiftObject;
import org.jclouds.rest.RestContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class StorageServiceRackspace implements StorageService {

    @Override
    public void putFile(File source, String filename, String directory, String contentType, Date deleteAfterDate) throws IOException {
        ContextBuilder contextBuilder = ContextBuilder.
                newBuilder("cloudfiles-us").
                credentials("robonews.files", "c8de7e43df9349a2a2619aaec54b7c74");

        BlobStore blobStore = contextBuilder.buildView(BlobStoreContext.class).getBlobStore();

        Blob blob = blobStore
            .blobBuilder(directory + "/" + filename)
            .payload(new FileInputStream(source))
            .expires(deleteAfterDate)
            .contentType(contentType)
            .build();

        blobStore.putBlob("img", blob);
    }
}
