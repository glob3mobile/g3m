//
//  G3MBuilder_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/06/14.
//
//

#include "G3MBuilder_win8.hpp"

#include "ThreadUtils_win8.hpp"
#include "Storage_win8.hpp"
#include "Downloader_win8.hpp"
#include "G3MWidget_win8.hpp"
#include "D3DRenderer.hpp"

G3MBuilder_win8::G3MBuilder_win8(){
	_nativeWidget = new G3MWidget_win8();
}

G3MWidget_win8* G3MBuilder_win8::createWidget(){
	setGL(_nativeWidget->getGL());
	_nativeWidget->setWidget(create());
	return _nativeWidget;
}

IThreadUtils* G3MBuilder_win8::createDefaultThreadUtils(){
	//return new ThreadUtils_win8();
	return NULL;
}

Storage* G3MBuilder_win8::createDefaultStorage(){
	return new Storage_win8("sqlite_win8_db");
	//return NULL;
}

IDownloader* G3MBuilder_win8::createDefaultDownloader(){
	//return new Downloader_win8();
	return NULL;
}