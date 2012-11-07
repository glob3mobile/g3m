package org.glob3.mobile.generated; 
//
//  IStorage.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 05/11/12.
//
//

//
//  Storage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 26/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class InitializationContext;

public abstract class IStorage
{
	private static IStorage _instance = null;


	public static void setInstance(IStorage storage)
	{
		if (_instance != null)
		{
			ILogger.instance().logWarning("Warning, IStorage instance set twice\n");
		}
		_instance = storage;
	}

	public static IStorage instance()
	{
		return _instance;
	}

  public abstract boolean containsBuffer(URL url);

  public abstract void saveBuffer(URL url, IByteBuffer buffer, boolean saveInBackground);

  public abstract IByteBuffer readBuffer(URL url);


  public abstract boolean containsImage(URL url);

  public abstract void saveImage(URL url, IImage image, boolean saveInBackground);

  public abstract IImage readImage(URL url);


  public abstract void onResume(InitializationContext ic);

  public abstract void onPause(InitializationContext ic);

  public abstract boolean isAvailable();


}