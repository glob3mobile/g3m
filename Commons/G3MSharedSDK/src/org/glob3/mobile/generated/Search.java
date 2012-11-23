package org.glob3.mobile.generated; 
//
//  Search.cpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  Search.h
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 23/11/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class Search
{

	private String _url;
	private ISearchDownloadListener _searchDownloadListener;


	public Search(ISearchDownloadListener searchDL, String url)
	{
		_searchDownloadListener = searchDL;
		_url = url;
	}
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void launchSearch(const String searchItem)const
	public final void launchSearch(String searchItem)
	{
		IDownloader.instance().requestBuffer(new URL(_url, false), 100000000, _searchDownloadListener, true);
	}
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	public void dispose()

}