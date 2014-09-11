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

//-- added for testing storage, remove later ------------
#include "URL.hpp"
#include "IFactory.hpp"
#include "ByteBuffer_win8.hpp"
#include "TimeInterval.hpp"
#include "Image_win8.hpp"

//-------------------------------------------------------

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
	//return new Storage_win8("sqlite_win8_db");

	Storage* testStorage = new Storage_win8("sqlite_win8_cache");

	const TimeInterval timeToExpire = TimeInterval::fromDays(20);

	//-- pruebas para guardar un string en la DB ---------------------------------------------------
	const URL stringUrl = URL("test_string");
	std::string stringContent = "esto es un string de prueba";
	int dataLength = stringContent.length()+1;
	/*IByteBuffer* buffer = IFactory::instance()->createByteBuffer(stringLength);
	for (int i = 0; i < stringLength; i++){
		 buffer->put(i, stringContent.at(i));
	}*/
	unsigned char* sContent = new unsigned char[dataLength];
	memcpy(sContent, stringContent.c_str(), dataLength);
	IByteBuffer* buffer = IFactory::instance()->createByteBuffer(sContent, dataLength);
	
	 ILogger::instance()->logInfo("Voy a guardar un string en la DB: \"%s\"\n", stringContent.c_str());
	 testStorage->saveBuffer(stringUrl, buffer, timeToExpire, false);
	 ILogger::instance()->logInfo("Ya he guardado un string en la DB");

	 ILogger::instance()->logInfo("Voy a recuperar un string de la DB");
	 IByteBufferResult sResult = testStorage->readBuffer(stringUrl, true);
	 ByteBuffer_win8* sb = (ByteBuffer_win8*)(sResult.getBuffer());
	 ILogger::instance()->logInfo("Ya he recuperado un string de la DB: \"%s\"\n", reinterpret_cast<const char*>(sb->getPointer()));


	 //-- pruebas para guardar un doble en la DB ---------------------------------------------------
	 const URL doubleUrl = URL("test_double");
	 const double doble = 347.0;
	 dataLength = sizeof(double);
	 
	 unsigned char* dContent = new unsigned char[dataLength];
	 //unsigned char dContent[sizeof(double)];
	 memcpy(dContent, &doble, dataLength);

	 /*unsigned char* dContent = NULL;
	 dContent = reinterpret_cast<unsigned char*>(&doble);*/
	 buffer = IFactory::instance()->createByteBuffer(dContent, dataLength);

	 ILogger::instance()->logInfo("Voy a guardar un double en la DB: \"%f\"\n", doble);
	 testStorage->saveBuffer(doubleUrl, buffer, timeToExpire, false);
	 ILogger::instance()->logInfo("Ya he guardado un double en la DB");

	 ILogger::instance()->logInfo("Voy a recuperar un double de la DB");
	 IByteBufferResult dResult = testStorage->readBuffer(doubleUrl, true);
	 ByteBuffer_win8* db = (ByteBuffer_win8*)(dResult.getBuffer()); 
	 double* dValue = reinterpret_cast<double*>(db->getPointer());
	 ILogger::instance()->logInfo("Ya he recuperado un double de la DB: \"%f\"\n", *dValue);


	 //-- pruebas para guardar un bool en la DB ---------------------------------------------------
	 const URL boolUrl = URL("test_boolean");
	 const bool booleano = false;
	 dataLength = sizeof(bool);

	 unsigned char* bContent = new unsigned char[dataLength];
	 memcpy(bContent, &booleano, dataLength);
	 buffer = IFactory::instance()->createByteBuffer(bContent, dataLength);

	 ILogger::instance()->logInfo("Voy a guardar un boolean en la DB: \"%s\"\n", booleano ? "true" : "false");
	 testStorage->saveBuffer(boolUrl, buffer, timeToExpire, false);
	 ILogger::instance()->logInfo("Ya he guardado un boolean en la DB");

	 ILogger::instance()->logInfo("Voy a recuperar un boolean de la DB");
	 IByteBufferResult bResult = testStorage->readBuffer(boolUrl, true);
	 ByteBuffer_win8* bb = (ByteBuffer_win8*)(bResult.getBuffer());
	 bool* bValue = reinterpret_cast<bool*>(bb->getPointer());
	 ILogger::instance()->logInfo("Ya he recuperado un boolean de la DB: \"%s\"\n", *bValue ? "true" : "false");


	 //-- pruebas para guardar una image en la DB ---------------------------------------------------
	 const URL imgUrl = URL("tiger.jpg");
	 const Image_win8* testImage = Image_win8::imageFromFile("tiger.jpg");

	 ILogger::instance()->logInfo("Voy a guardar una image en la DB: \"%s\"\n", imgUrl.getPath().c_str());
	 testStorage->saveImage(imgUrl, testImage, timeToExpire, false);
	 ILogger::instance()->logInfo("Ya he guardado una image en la DB");

	 ILogger::instance()->logInfo("Voy a recuperar una image de la DB");
	 IImageResult iResult = testStorage->readImage(imgUrl, true);
	 Image_win8* ii = (Image_win8*)(iResult._image);
	 std::string desc = ii->description();
	 ILogger::instance()->logInfo("Ya he recuperado una image de la DB: \"%s\"\n", desc.c_str());

	return testStorage;
	//return NULL;
}

IDownloader* G3MBuilder_win8::createDefaultDownloader(){
	//return new Downloader_win8();
	return NULL;
}