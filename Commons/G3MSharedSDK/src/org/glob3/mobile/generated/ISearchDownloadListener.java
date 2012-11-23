package org.glob3.mobile.generated; 
//
//  ISearchDownloadListener.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class ISearchDownloadListener implements IBufferDownloadListener
{

	private Object _unknownObject;


	public ISearchDownloadListener(Object unknownObject)
	{
		_unknownObject = unknownObject;

	}

	public abstract void onDownload(URL url, IByteBuffer buffer);

	public abstract void onError(URL url);

	public abstract void onCancel(URL url);

	public abstract void onCanceledDownload(URL url, IByteBuffer data);

	public abstract void updateResults(JSONArray json);

	public void dispose()
	{
	}

}