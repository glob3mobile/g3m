//
//  Storage_win8.hpp
//  G3MWindowsSDK
//
//  Created by F. Pulido on 26/09/14.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "pch.h"
#include "Downloader_win8_Listener.hpp"
#include "IBufferDownloadListener.hpp"
#include "IImageDownloadListener.hpp"
#include "URL.hpp"
#include "IImage.hpp"
#include "Image_win8.hpp"

Downloader_win8_Listener::Downloader_win8_Listener(IBufferDownloadListener* cppBufferListener,
												   IImageDownloadListener* cppImageListener,
												   bool deleteListener) :
	_cppBufferListener(cppBufferListener),
	_cppImageListener(cppImageListener),
	_deleteListener(deleteListener)
{
}

Downloader_win8_Listener::Downloader_win8_Listener(IBufferDownloadListener* cppBufferListener,
												   bool deleteListener){
	Downloader_win8_Listener(cppBufferListener, NULL, deleteListener);
}

Downloader_win8_Listener::Downloader_win8_Listener(IImageDownloadListener* cppImageListener,
												   bool deleteListener){
	Downloader_win8_Listener(NULL, cppImageListener, deleteListener);
}


void Downloader_win8_Listener::onDownload(const URL& url, IByteBuffer* buffer){
	
	if (_cppBufferListener) {
		_cppBufferListener->onDownload(url, buffer, false);
	}

	if (_cppImageListener) {
		IWICBitmap* bitmap = Image_win8::imageWithData(buffer);
		IImage* image = new Image_win8(bitmap, buffer);
		if (image) {
			_cppImageListener->onDownload(url, image, false);
		}
		else {
			_cppImageListener->onError(url);
		}
	}
}

void Downloader_win8_Listener::onError(const URL& url){

	if (_cppBufferListener) {
		_cppBufferListener->onError(url);
	}

	if (_cppImageListener) {
		_cppImageListener->onError(url);
	}
}

void Downloader_win8_Listener::onCancel(const URL& url){

	if (_cppBufferListener) {
		_cppBufferListener->onCancel(url);
	}

	if (_cppImageListener) {
		_cppImageListener->onCancel(url);
	}
}

void Downloader_win8_Listener::onCanceledDownload(const URL& url, IByteBuffer* buffer){

	if (_cppBufferListener) {
		_cppBufferListener->onCanceledDownload(url, buffer, false);
		delete buffer;
	}

	if (_cppImageListener) {
		IWICBitmap* bitmap = Image_win8::imageWithData(buffer);
		IImage* image = new Image_win8(bitmap, buffer);
		if (image) {
			_cppImageListener->onCanceledDownload(url, image, false);
			delete image;
		}
		else{
			ILogger::instance()->logInfo("Downloader_win8: Can't create image from data (URL= %s ) \n", url.getPath().c_str());
		}
	}
}

void Downloader_win8_Listener::dealloc(){

	if (_deleteListener) {
		if (_cppBufferListener) {
			delete _cppBufferListener;
		}

		if (_cppImageListener) {
			delete _cppImageListener;
		}
	}
}

Downloader_win8_Listener::~Downloader_win8_Listener()
{
	dealloc();
}

