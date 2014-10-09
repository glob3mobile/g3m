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
#include "IStringUtils.hpp"
#include "StringUtils_win8.hpp"
#include "IImageDownloadListener.hpp"
#include "IBufferDownloadListener.hpp"

#include <iostream>
using namespace std;

//-------------------------------------------------------

//Platform::String^ getLocalPath(Platform::String^ name){
//
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	return Platform::String::Concat(tmpPath, name);
//}

void executeStorageTests(Storage* testStorage){

	const TimeInterval timeToExpire = TimeInterval::fromDays(20);

	//-- pruebas para guardar un string en la DB ---------------------------------------------------
	const URL stringUrl = URL("test_string");
	std::string stringContent = "esto es un string de prueba";
	int dataLength = stringContent.length() + 1;
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
	const StringUtils_win8* sUtils = (StringUtils_win8*)IStringUtils::instance();

	//std::string imgName = "tiger.jpg";
	//std::string imgName = "MARBLES.BMP";
	//std::string imgName = "MARBLES.TIF";
	std::string imgName = "tree.png";

	std::size_t pos = imgName.find(".");
	std::string name = imgName.substr(0, pos);
	//std::string ext = imgName.substr(pos+1, imgName.length());

	Platform::String^ imgHatName = sUtils->toStringHat(imgName);
	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;
	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
	Platform::String^ fileName = Platform::String::Concat(tmpPath, imgHatName);
	std::string fileUrl = sUtils->toStringStd(fileName);
	ILogger::instance()->logInfo("Image file URL: %s \n", fileUrl.c_str());
	const URL imgFileUrl = URL(fileUrl);
	const Image_win8* testImage = Image_win8::imageFromFile(imgFileUrl);

	const URL imgUrl = URL(imgName);
	ILogger::instance()->logInfo("Voy a guardar una image en la DB: \"%s\"\n", imgUrl._path.c_str());
	testStorage->saveImage(imgUrl, testImage, timeToExpire, false);
	ILogger::instance()->logInfo("Ya he guardado una image en la DB");

	ILogger::instance()->logInfo("Voy a recuperar una image de la DB");
	IImageResult iResult = testStorage->readImage(imgUrl, true);
	Image_win8* ii = (Image_win8*)(iResult._image);
	std::string desc = ii->description();
	ILogger::instance()->logInfo("Ya he recuperado una image de la DB: \"%s\"\n", desc.c_str());

	// Save the recovered image to a file
	imgHatName = sUtils->toStringHat(name + "_recovered");
	Platform::String^ recoveredFileName = Platform::String::Concat(tmpPath, imgHatName);
	std::string recoveredfileUrl = sUtils->toStringStd(recoveredFileName);
	const URL recoveredUrl = URL(recoveredfileUrl);
	Image_win8::exportToFile(recoveredUrl, ii);

}

class Test_IImageDownloadListener : public IImageDownloadListener {
private:
	const URL _imgURL;

public:

	Test_IImageDownloadListener(const URL &url) :
		_imgURL(url)
	{
	}

	void onDownload(const URL& url,
		IImage* image,
		bool expired)  {

		ILogger::instance()->logInfo("Llamando a Test_IImageDownloadListener \n");

		Image_win8* ii = (Image_win8*)(image);
		std::string desc = ii->description();
		ILogger::instance()->logInfo("Ya he descargado la imagen: \"%s\"\n", desc.c_str());

		Image_win8::exportToFile(_imgURL, ii);
		ILogger::instance()->logInfo("Ya he guardado la imagen %s \n",_imgURL.getPath().c_str());
	}

	void onError(const URL& url) {
		ILogger::instance()->logInfo("Llamando a Test_IImageDownloadListener ONERROR \n");
	}

	void onCancel(const URL& url) {
		ILogger::instance()->logInfo("Llamando a Test_IImageDownloadListener ONCANCEL \n");
	}

	void onCanceledDownload(const URL& url,
		IImage* image,
		bool expired)  {
		ILogger::instance()->logInfo("Llamando a Test_IImageDownloadListener ONCANCELDOWNLOAD \n");
	}
};

class Test_BufferDownloadListener : public IBufferDownloadListener {
private:
	const URL _bufferURL;

public:

	Test_BufferDownloadListener(const URL &url) :
		_bufferURL(url)
	{
	}

	void onDownload(const URL& url,
		IByteBuffer* buffer,
		bool expired)  {

		ILogger::instance()->logInfo("Llamando a Test_BufferDownloadListener \n");

		ILogger::instance()->logInfo("Ya he descargado el buffer %s", _bufferURL.getPath().c_str());
		ILogger::instance()->logInfo("Longitud de los datos: %d \n", buffer->size());
		//ByteBuffer_win8* sb = (ByteBuffer_win8*)buffer;
		//ILogger::instance()->logInfo("CONTENIDO: \"%s\"\n", reinterpret_cast<const char*>(sb->getPointer()));
	}

	void onError(const URL& url) {
		ILogger::instance()->logInfo("Llamando a Test_BufferDownloadListener ONERROR \n");
	}

	void onCancel(const URL& url) {
		ILogger::instance()->logInfo("Llamando a Test_BufferDownloadListener ONCANCEL \n");
	}

	void onCanceledDownload(const URL& url,
		IByteBuffer* buffer,
		bool expired)  {
		ILogger::instance()->logInfo("Llamando a Test_BufferDownloadListener ONCANCELDOWNLOAD \n");
	}
};

void executeDownloaderTests(IDownloader* downloader){

	ILogger::instance()->logInfo("Ejecutando test del downloader..");

	const TimeInterval timeToExpire = TimeInterval::fromDays(20);
	const StringUtils_win8* sUtils = (StringUtils_win8*)IStringUtils::instance();

	//-- Test downloading image and then save to a file --------
	std::string imgName = "tiger.jpg";
	//std::string imgName = "MARBLES.BMP";
	std::size_t pos = imgName.find(".");
	std::string name = imgName.substr(0, pos);
	std::string ext = imgName.substr(pos + 1, imgName.length());
	Platform::String^ imgHatName = sUtils->toStringHat(imgName);
	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;
	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");

	Windows::Storage::StorageFolder^ roamingFolder = Windows::Storage::ApplicationData::Current->RoamingFolder;
	ILogger::instance()->logInfo("RoamingFolder %s", sUtils->toStringStd(roamingFolder->Path).c_str());

	//Windows::Storage::StorageFolder^ InstallationFolder = Windows::ApplicationModel::Package::Current::InstalledLocation;

	imgHatName = sUtils->toStringHat(name + "_downloaded");
	Platform::String^ recoveredImgName = Platform::String::Concat(tmpPath, imgHatName);
	std::string recoveredImgStr = sUtils->toStringStd(recoveredImgName);
	const URL recoveredImgUrl = URL(recoveredImgStr);

	const URL requestedImgUrl = URL("http://192.168.1.26/images/" + imgName);

	downloader->requestImage(requestedImgUrl, 23, timeToExpire, true, new Test_IImageDownloadListener(recoveredImgUrl), true);


	//-- Test downloading geojson and then ??? --------
	std::string fileName = "countries.geojson";
	const URL requestedDataUrl = URL("http://192.168.1.26/" + fileName);

	downloader->requestBuffer(requestedDataUrl, 23, timeToExpire, true, new Test_BufferDownloadListener(requestedDataUrl), true);

	//-- Test downloading image from local then save to a file --------
	std::string localImgName = URL::FILE_PROTOCOL + imgName;
	const URL localImgUrl = URL(localImgName);
	//-- recovered URL
	//std::size_t rpos = imgName.find(".");
	//std::string rname = imgName.substr(0, rpos);
	std::string reFilePath = name + "_RE";
	Platform::String^ relocalPath = Platform::String::Concat(tmpPath, sUtils->toStringHat(reFilePath));
	const URL reLocalImgUrl = URL(sUtils->toStringStd(relocalPath));

	downloader->requestImage(localImgUrl, 23, timeToExpire, true, new Test_IImageDownloadListener(reLocalImgUrl), true);
	ILogger::instance()->logInfo("Terminando test del downloader");

	//-- Test downloading 15 images from local then save to a file --------
	for (int i = 1; i <= 15; i++){
		std::string nameI = name + std::to_string(i);
		std::string imgNameI = nameI + "." + ext;
		const URL requestedImgUrlI = URL("http://192.168.1.26/images/" + imgNameI);

		Platform::String^ imgHatNameI = sUtils->toStringHat(nameI + "_downloaded");
		Platform::String^ recoveredImgNameI = Platform::String::Concat(tmpPath, imgHatNameI);
		std::string recoveredImgStrI = sUtils->toStringStd(recoveredImgNameI);
		const URL recoveredImgUrlI = URL(recoveredImgStrI);

		long long rid = downloader->requestImage(requestedImgUrlI, 23, timeToExpire, true, new Test_IImageDownloadListener(recoveredImgUrlI), true);
		ILogger::instance()->logInfo("Request id image pedida: %d \n", rid);
		if (i == 5){
			ILogger::instance()->logInfo("Cancelando previous request id: %d \n", rid-1);
			downloader->cancelRequest(rid-1); 
		}
	}

	//-- Test downloading 15 geojson and then ??? --------
	for (int i = 1; i <= 15; i++){
		std::string fileGeoName = "countries" + to_string(i) + ".geojson";
		const URL requestedGeoUrl = URL("http://192.168.1.26/" + fileGeoName);
		
		long long rid = downloader->requestBuffer(requestedGeoUrl, 23, timeToExpire, true, new Test_BufferDownloadListener(requestedGeoUrl), true);
		ILogger::instance()->logInfo("Request id geojson pedido: %d \n", rid);
		if (i == 5){
			ILogger::instance()->logInfo("Cancelando previous request id: %d \n", rid - 1);
			downloader->cancelRequest(rid - 1);
		}
	}

}

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

	//executeStorageTests(testStorage); // only while testing. Remove later.
	return testStorage;
}

IDownloader* G3MBuilder_win8::createDefaultDownloader(){
	//return new Downloader_win8();
	const int MAX_CONCURRENT_OPERATION_COUNT = 8;
	IDownloader* testDownloader = new Downloader_win8(MAX_CONCURRENT_OPERATION_COUNT);

	executeDownloaderTests(testDownloader);
	return testDownloader;
}

