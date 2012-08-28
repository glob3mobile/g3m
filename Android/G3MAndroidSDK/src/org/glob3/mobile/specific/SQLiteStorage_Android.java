package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ByteBuffer;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.URL;

import android.os.storage.StorageManager;

public class SQLiteStorage_Android implements IStorage {
	
	SQLiteStorage_Android(String path){
		
	}

	@Override
	public boolean contains(URL url) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void save(URL url, ByteBuffer buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public ByteBuffer read(URL url) {
		// TODO Auto-generated method stub
		return null;
	}

}
