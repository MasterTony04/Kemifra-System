package com.fahamutech.adminapp.database.connector;

import com.fahamutech.adminapp.database.DataBaseCallback;
import com.fahamutech.adminapp.model.ITestimony;
import com.fahamutech.adminapp.model.Testimony;

public interface TestimonyDataSource {

    void createTestimony(Testimony testimony, DataBaseCallback... callbacks);

    void deleteTestimony(String docId, DataBaseCallback... callbacks);
}
