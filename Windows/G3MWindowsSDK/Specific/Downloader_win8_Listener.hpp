//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef __G3MWindowsSDK_Downloader_win8_Listener__
#define __G3MWindowsSDK_Downloader_win8_Listener__

#pragma once


class IBufferDownloadListener;
class IImageDownloadListener;
class IByteBuffer;
class IImage;
class URL;


class Downloader_win8_Listener {

private:
	IBufferDownloadListener* _cppBufferListener;
	IImageDownloadListener*  _cppImageListener;
	bool                     _deleteListener;

public:
	Downloader_win8_Listener(IBufferDownloadListener* cppBufferListener,
							 IImageDownloadListener* cppImageListener,
							 bool deleteListener);

	void onDownload(const URL& url, IByteBuffer* buffer, IImage* image);

	void onError(const URL& url);

	void onCancel(const URL& url);

	void onCanceledDownload(const URL& url, IByteBuffer* buffer, IImage* image);

	void dealloc();

	~Downloader_win8_Listener();
};

#endif