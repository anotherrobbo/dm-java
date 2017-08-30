package com.anotherrobbo.dm.job;

import org.junit.Test;

import com.anotherrobbo.dm.external.MetadataService;

public class ManifestJobTest {

    @Test
    public void testManifestJob() {
        MetadataService service = new MetadataService() {
            public void refreshIn(long secs) {/*no-op for this test*/};
        };
        
        ManifestJob job = new ManifestJob(service);
        job.run();
    }
    
}
