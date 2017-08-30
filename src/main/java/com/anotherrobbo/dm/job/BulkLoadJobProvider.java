package com.anotherrobbo.dm.job;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anotherrobbo.dm.entity.dao.ActivityRecordDao;
import com.anotherrobbo.dm.entity.dao.CharacterRecordDao;
import com.anotherrobbo.dm.entity.dao.PlayerRecordDao;
import com.anotherrobbo.dm.external.MetadataService;

public class BulkLoadJobProvider implements Provider<BulkLoadJob> {
	
	@Inject
	private MetadataService metadataService;
    @Inject
	private PlayerRecordDao playerRecordDao;
	@Inject
	private CharacterRecordDao characterRecordDao;
	@Inject
	private ActivityRecordDao activityRecordDao;
	
	@Override
	public BulkLoadJob get() {
		return new BulkLoadJob(metadataService, playerRecordDao, characterRecordDao, activityRecordDao);
	}

}
