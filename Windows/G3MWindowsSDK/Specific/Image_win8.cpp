#include "Image_win8.hpp"
#include "IStringUtils.hpp"
#include "StringUtils_win8.hpp"

#include <exception>
#include <Windows.h>
#include <WinBase.h>
//#include <Shlwapi.h>

//using namespace Windows;
//using namespace Windows::Storage::Streams;
//using namespace Microsoft::WRL;


inline void ThrowIfFailed(HRESULT hr){
	if (FAILED(hr))
	{
		throw Platform::Exception::CreateException(hr);
	}
}

//Platform::String^ toStringHat(std::string str){
//	std::wstring wid_str = std::wstring(str.begin(), str.end());
//	const wchar_t* w_char = wid_str.c_str();
//	Platform::String^ p_string = ref new Platform::String(w_char);
//	return p_string;
//}

//std::string ToStringStd(Platform::String^ str_hat){
//	std::wstring wstr(str_hat->Data());
//	std::string stdStr(wstr.begin(), wstr.end());
//	return stdStr;
//}

std::string getContainerFormatExtension(GUID containerFormat){

	if (containerFormat == GUID_ContainerFormatJpeg){
		return "jpg";
	}
	else if (containerFormat == GUID_ContainerFormatPng){
		return "png";
	}
	else if (containerFormat == GUID_ContainerFormatBmp){
		return "bmp";
	}
	else if (containerFormat == GUID_ContainerFormatTiff){
		return "tif";
	}
	else if (containerFormat == GUID_ContainerFormatGif){
		return "gif";
	}
	else if (containerFormat == GUID_ContainerFormatDds){
		return "dds";
	}
	else if (containerFormat == GUID_ContainerFormatIco){
		return "ico";
	}
	else if (containerFormat == GUID_ContainerFormatWmp){
		return "wmp";
	}

	return NULL;
}

UINT getbppFromPixelFormat(WICPixelFormatGUID pPixelFormat){

	if (GUID_WICPixelFormatDontCare == pPixelFormat){
		return 0;
	}
	else if (GUID_WICPixelFormat1bppIndexed == pPixelFormat || GUID_WICPixelFormatBlackWhite == pPixelFormat){
		return 1;
	}
	else if (GUID_WICPixelFormat2bppIndexed == pPixelFormat || GUID_WICPixelFormat2bppGray == pPixelFormat){
		return 2;
	}
	else if (GUID_WICPixelFormat4bppIndexed == pPixelFormat || GUID_WICPixelFormat4bppGray == pPixelFormat){
		return 4;
	}
	else if (GUID_WICPixelFormat8bppIndexed == pPixelFormat || GUID_WICPixelFormat8bppGray == pPixelFormat || GUID_WICPixelFormat8bppAlpha == pPixelFormat ||
		GUID_WICPixelFormat8bppY == pPixelFormat || GUID_WICPixelFormat8bppCb == pPixelFormat || GUID_WICPixelFormat8bppCr == pPixelFormat){
		return 8;
	}
	else if (GUID_WICPixelFormat16bppBGR555 == pPixelFormat || GUID_WICPixelFormat16bppBGR565 == pPixelFormat || GUID_WICPixelFormat16bppBGRA5551 == pPixelFormat ||
		GUID_WICPixelFormat16bppGray == pPixelFormat || GUID_WICPixelFormat16bppGrayFixedPoint == pPixelFormat || GUID_WICPixelFormat16bppGrayHalf == pPixelFormat ||
		GUID_WICPixelFormat16bppCbCr == pPixelFormat){
		return 16;
	}
	else if (GUID_WICPixelFormat24bppBGR == pPixelFormat || GUID_WICPixelFormat24bppRGB == pPixelFormat || GUID_WICPixelFormat24bpp3Channels == pPixelFormat){
		return 24;
	}
	else if (GUID_WICPixelFormat32bppBGR == pPixelFormat || GUID_WICPixelFormat32bppBGRA == pPixelFormat || GUID_WICPixelFormat32bppPBGRA == pPixelFormat ||
		GUID_WICPixelFormat32bppGrayFloat == pPixelFormat || GUID_WICPixelFormat32bppRGB == pPixelFormat || GUID_WICPixelFormat32bppRGBA == pPixelFormat ||
		GUID_WICPixelFormat32bppPRGBA == pPixelFormat || GUID_WICPixelFormat32bppBGR101010 == pPixelFormat || GUID_WICPixelFormat32bppCMYK == pPixelFormat ||
		GUID_WICPixelFormat32bppRGBE == pPixelFormat || GUID_WICPixelFormat32bppGrayFixedPoint == pPixelFormat || GUID_WICPixelFormat32bppRGBA1010102 == pPixelFormat ||
		GUID_WICPixelFormat32bppRGBA1010102XR == pPixelFormat || GUID_WICPixelFormat32bpp4Channels == pPixelFormat || GUID_WICPixelFormat32bpp3ChannelsAlpha == pPixelFormat){
		return 32;
	}
	else if (GUID_WICPixelFormat40bpp5Channels == pPixelFormat || GUID_WICPixelFormat40bpp4ChannelsAlpha == pPixelFormat){
		return 40;
	}
	else if (GUID_WICPixelFormat48bppRGB == pPixelFormat || GUID_WICPixelFormat48bppBGR == pPixelFormat || GUID_WICPixelFormat48bppRGBFixedPoint == pPixelFormat ||
		GUID_WICPixelFormat48bppBGRFixedPoint == pPixelFormat || GUID_WICPixelFormat48bppRGBHalf == pPixelFormat || GUID_WICPixelFormat48bpp6Channels == pPixelFormat ||
		GUID_WICPixelFormat48bpp3Channels == pPixelFormat || GUID_WICPixelFormat48bpp5ChannelsAlpha == pPixelFormat){
		return 48;
	}
	else if (GUID_WICPixelFormat56bpp7Channels == pPixelFormat || GUID_WICPixelFormat56bpp6ChannelsAlpha == pPixelFormat){
		return 56;
	}
	else if (GUID_WICPixelFormat64bppRGBA == pPixelFormat || GUID_WICPixelFormat64bppBGRA == pPixelFormat || GUID_WICPixelFormat64bppPRGBA == pPixelFormat ||
		GUID_WICPixelFormat64bppPBGRA == pPixelFormat || GUID_WICPixelFormat64bppRGB == pPixelFormat || GUID_WICPixelFormat64bppRGBAFixedPoint == pPixelFormat ||
		GUID_WICPixelFormat64bppBGRAFixedPoint == pPixelFormat || GUID_WICPixelFormat64bppRGBFixedPoint == pPixelFormat || GUID_WICPixelFormat64bppRGBAHalf == pPixelFormat ||
		GUID_WICPixelFormat64bppPRGBAHalf == pPixelFormat || GUID_WICPixelFormat64bppRGBHalf == pPixelFormat || GUID_WICPixelFormat64bppCMYK == pPixelFormat ||
		GUID_WICPixelFormat64bpp8Channels == pPixelFormat || GUID_WICPixelFormat64bpp4Channels == pPixelFormat || GUID_WICPixelFormat64bpp7ChannelsAlpha == pPixelFormat ||
		GUID_WICPixelFormat64bpp3ChannelsAlpha == pPixelFormat){
		return 64;
	}
	else if (GUID_WICPixelFormat72bpp8ChannelsAlpha == pPixelFormat){
		return 72;
	}
	else if (GUID_WICPixelFormat80bpp5Channels == pPixelFormat || GUID_WICPixelFormat80bppCMYKAlpha == pPixelFormat || GUID_WICPixelFormat80bpp4ChannelsAlpha == pPixelFormat){
		return 80;
	}
	else if (GUID_WICPixelFormat96bppRGBFixedPoint == pPixelFormat || GUID_WICPixelFormat96bppRGBFloat == pPixelFormat || GUID_WICPixelFormat96bpp6Channels == pPixelFormat ||
		GUID_WICPixelFormat96bpp5ChannelsAlpha == pPixelFormat){
		return 96;
	}
	else if (GUID_WICPixelFormat112bpp7Channels == pPixelFormat || GUID_WICPixelFormat112bpp6ChannelsAlpha == pPixelFormat){
		return 112;
	}
	else if (GUID_WICPixelFormat128bppRGBAFloat == pPixelFormat || GUID_WICPixelFormat128bppPRGBAFloat == pPixelFormat || GUID_WICPixelFormat128bppRGBFloat == pPixelFormat ||
		GUID_WICPixelFormat128bppRGBAFixedPoint == pPixelFormat || GUID_WICPixelFormat128bppRGBFixedPoint == pPixelFormat || GUID_WICPixelFormat128bpp8Channels == pPixelFormat ||
		GUID_WICPixelFormat128bpp7ChannelsAlpha == pPixelFormat){
		return 128;
	}
	else if (GUID_WICPixelFormat40bppCMYKAlpha == pPixelFormat){
		return 140;
	}
	else if (GUID_WICPixelFormat144bpp8ChannelsAlpha == pPixelFormat){
		return 144;
	}
	else{
		return 0;
	}

}

UINT getStride(
	const UINT width, // image width in pixels
	const UINT bitCount) { // bits per pixel
	//ASSERT(0 == bitCount % 8);

	const UINT byteCount = bitCount / 8;
	const UINT stride = (width * byteCount + 3) & ~3;

	//ASSERT(0 == stride % sizeof(DWORD));
	return stride;
}


IByteBuffer* getBitmapBuffer(IWICBitmap* bitmap) {

	if (bitmap == nullptr){
		return NULL;
	}

	UINT uiWidth = 0;
	UINT uiHeight = 0;
	IWICImagingFactory *pFactory = NULL;
	
	IWICBitmapLock *pLock = NULL;
	BYTE *pv = NULL;
	UINT cbBufferSize = 0;

	HRESULT	hr = CoCreateInstance(
			CLSID_WICImagingFactory,
			NULL,
			CLSCTX_INPROC_SERVER,
			IID_IWICImagingFactory,
			(LPVOID*)&pFactory
			);

	if (SUCCEEDED(hr)){
		hr = bitmap->GetSize(&uiWidth, &uiHeight);
	}

	if (SUCCEEDED(hr)){
		WICRect rcLock;
		rcLock = WICRect{ 0, 0, uiWidth, uiHeight };
		
		if (SUCCEEDED(hr)){
			hr = bitmap->Lock(&rcLock, WICBitmapLockRead, &pLock);
		}

		if (SUCCEEDED(hr)){
			hr = pLock->GetDataPointer(&cbBufferSize, &pv);
			// Release the bitmap lock.
			pLock->Release();
		}
	}

	if (!SUCCEEDED(hr)){
		ILogger::instance()->logError("Unnable to retrieve bitmap data buffer");
		return NULL;
	}

	if (pFactory){
		pFactory->Release();
	}

	IByteBuffer* bitmapBuffer = IFactory::instance()->createByteBuffer(pv, cbBufferSize);
	return bitmapBuffer;
}


Image_win8::Image_win8(IWICBitmap* image, IByteBuffer* sourceBuffer)
{
	_image = image;
	_sourceBuffer = sourceBuffer;
}


Image_win8::~Image_win8()
{
	_image = nullptr;
	_sourceBuffer = nullptr;
}

int Image_win8::getWidth() const {

	if (_image == nullptr){
		return 0;
	}
	UINT with;
	UINT height;
	HRESULT hr = _image->GetSize(&with, &height);
	if (SUCCEEDED(hr)){
		return (int)with;
	}

	return 0;
}

int Image_win8::getHeight() const {

	if (_image == nullptr){
		return 0;
	}
	UINT with;
	UINT height;
	HRESULT hr = _image->GetSize(&with, &height);
	if (SUCCEEDED(hr)){
		return (int)height;
	}

	return 0;
}

const Vector2I Image_win8::getExtent() const {

	return Vector2I(getWidth(), getHeight());
}



IByteBuffer* Image_win8::getSourceBuffer() const {

	return _sourceBuffer;
	//return ((ByteBuffer_win8*)_sourceBuffer)->getPointer();
}

void Image_win8::releaseSourceBuffer() const {

	_sourceBuffer = nullptr;
}

IWICBitmap* Image_win8::getBitmap() const{

	return _image;
}

const std::string Image_win8::description() const{
	IStringBuilder* isb = IStringBuilder::newStringBuilder();
	isb->addString("Image_win8: ");
	isb->addInt(getWidth());
	isb->addString("x");
	isb->addInt(getHeight());
	isb->addString(", _image=(");
	isb->addString(")");
	std::string desc = isb->getString();
	delete isb;
	return desc;
}

IImage* Image_win8::shallowCopy() const{

	return new Image_win8(_image, _sourceBuffer);
}



IByteBuffer* Image_win8::createImageBuffer() const{
	
	//-- This workaround creates image buffer compressed in png format, but without metadata.
	if (_sourceBuffer != nullptr){
		return _sourceBuffer;
	}

	if (_image == nullptr){
		return NULL;
	}

	// WIC interface pointers.
	IWICImagingFactory* pFactory = NULL;
	IWICStream* pOutputStream = NULL;
	IWICBitmapEncoder* pIEncoder = NULL;
	IWICBitmapFrameEncode* pIEncoderFrame = NULL;

	HRESULT hr = CoCreateInstance(
		CLSID_WICImagingFactory,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IWICImagingFactory,
		(LPVOID*)&pFactory
		);

	// Create input and output stream
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateStream(&pOutputStream);
	}
	 
	// getBitmapBuffer
	IByteBuffer* bitmapBuffer = getBitmapBuffer(_image);
	
	//-- reserve memory for output data
	int outputLength = bitmapBuffer->size();
	BYTE* outputData = new BYTE[outputLength];

	// Initialize the output stream with the memory pointer and size.
	if (SUCCEEDED(hr)){
		hr = pOutputStream->InitializeFromMemory(outputData, outputLength);
	}

	// Create a encoder for the output stream.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateEncoder(
			GUID_ContainerFormatPng, // png compression
			0, // vendor
			&pIEncoder);
	}

	// Init enconder
	if (SUCCEEDED(hr)){
		hr = pIEncoder->Initialize(
			pOutputStream,
			WICBitmapEncoderNoCache);
	}

	//Get and create image frame
	if (SUCCEEDED(hr)){
		hr = pIEncoder->CreateNewFrame(&pIEncoderFrame, nullptr);
	}

	if (SUCCEEDED(hr)){
		hr = pIEncoderFrame->Initialize(NULL);
	}

	// Set frame size
	if (SUCCEEDED(hr)){
		UINT width, height;
		_image->GetSize(&width, &height);
		hr = pIEncoderFrame->SetSize(width, height);
	}

	// Set resolution
	if (SUCCEEDED(hr)){
		double pDpiX, pDpiY;
		_image->GetResolution(&pDpiX, &pDpiY);
		hr = pIEncoderFrame->SetResolution(pDpiX, pDpiY);
	}

	// Set pixel format
	if (SUCCEEDED(hr)){
		WICPixelFormatGUID pixelFormat;
		_image->GetPixelFormat(&pixelFormat);
		hr = pIEncoderFrame->SetPixelFormat(&pixelFormat);
	}
	
	//-- Image metadata should also be copied, but no
	//-- workaround has already been found to do it.

	if (SUCCEEDED(hr)){
		hr = pIEncoderFrame->WriteSource(_image, nullptr);
		hr = pIEncoderFrame->Commit();
		hr = pIEncoder->Commit();
	}

	//-- calculate output stream length
	int bufferLength = 0;
	if (SUCCEEDED(hr)){
		ULARGE_INTEGER newPos;
		LARGE_INTEGER offsetZero = {0, 0};
		hr = pOutputStream->Seek(offsetZero, STREAM_SEEK_CUR, &newPos);
		bufferLength = newPos.LowPart;
	}

	//-- release 
	if (pFactory){
		pFactory->Release();
	}

	if (pIEncoder){
		pIEncoder->Release();
	}

	if (pIEncoderFrame){
		pIEncoderFrame->Release();
	}

	if (pOutputStream){
		pOutputStream->Release();
	}

	if (!SUCCEEDED(hr)){
		ILogger::instance()->logError("Unnable to create image data buffer");
		return NULL;
	}
	
	// copy data to buffer of the right size and release old buffer
	BYTE* bufferData = new BYTE[bufferLength];
	memcpy(bufferData, outputData, bufferLength);
	delete[] outputData;

	IByteBuffer* imageBuffer = IFactory::instance()->createByteBuffer(bufferData, bufferLength);
	return imageBuffer;
}



IWICBitmap* Image_win8::imageWithData(IByteBuffer* imgData){
	
	// WIC interface pointers.
	IWICStream* pIWICStream = NULL;
	IWICBitmapDecoder *pIDecoder = NULL;
	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
	IWICImagingFactory *pFactory = NULL;
	IWICBitmap *pBitmap = NULL;

	HRESULT hr = CoCreateInstance(
		CLSID_WICImagingFactory,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IWICImagingFactory,
		(LPVOID*)&pFactory
		);
	
	// Create a WIC stream to map onto the memory.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateStream(&pIWICStream);
	}

	BYTE* data = ((ByteBuffer_win8*)imgData)->getPointer();
	int dataLength = imgData->size();

	// Initialize the stream with the memory pointer and size.
	if (SUCCEEDED(hr)){
		hr = pIWICStream->InitializeFromMemory(
			data,
			dataLength); 
	}

	// Create a decoder for the stream.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateDecoderFromStream(
			pIWICStream,                   // The stream to use to create the decoder
			NULL,                          // Do not prefer a particular vendor
			WICDecodeMetadataCacheOnLoad,  // Cache metadata when needed
			&pIDecoder);                   // Pointer to the decoder
	}


	if (SUCCEEDED(hr)){
		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
	}

	if (SUCCEEDED(hr)){
		hr = pFactory->CreateBitmapFromSource(pIDecoderFrame, WICBitmapCacheOnDemand, &pBitmap);
	}

	//-- release 
	if (pFactory){
		pFactory->Release();
	}

	if (pIDecoder){
		pIDecoder->Release();
	}

	if (pIDecoderFrame){
		pIDecoderFrame->Release();
	}

	if (pIWICStream){
		pIWICStream->Release();
	}

	if (!SUCCEEDED(hr)){
		ILogger::instance()->logError("Unnable to create bitmap from source data");
		return NULL;
	}

	return pBitmap;
}


int Image_win8::getBufferSize() const{

	return (_sourceBuffer == nullptr) ? 0 : _sourceBuffer->size();
}


//-- only for testing, remove later -----------------------------------

Image_win8* Image_win8::imageFromFile(const URL& fileUrl){

	const StringUtils_win8* sUtils = (StringUtils_win8*)IStringUtils::instance();
	Platform::String^ path = sUtils->toStringHat(fileUrl._path);

	// WIC interface pointers.
	IWICImagingFactory *pFactory = NULL;
	IWICBitmapDecoder *pIDecoder = NULL;
	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
	IWICStream *pIWICStream = NULL;
	IWICBitmap *pBitmap = NULL;

	HRESULT hr = CoCreateInstance(
		CLSID_WICImagingFactory,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IWICImagingFactory,
		(LPVOID*)&pFactory
		);

	if (SUCCEEDED(hr)){
		hr = pFactory->CreateStream(&pIWICStream);
	}

	if (SUCCEEDED(hr)){
		hr = pIWICStream->InitializeFromFilename(path->Data(), GENERIC_READ);
	}

	
	IByteBuffer* imgBuffer = NULL;
	int dataLength = 0;
	BYTE* imageData = NULL;

	//-- calculate stream length
	if (SUCCEEDED(hr)){
		//STATSTG pStatstg;
		ULARGE_INTEGER newPos;
		LARGE_INTEGER offsetZero = {0, 0};
		hr = pIWICStream->Seek(offsetZero, STREAM_SEEK_END, &newPos);
		dataLength = newPos.LowPart;

		imageData = new BYTE[dataLength];

		//-- read data from stream
		ULONG pcbRead;
		hr = pIWICStream->Seek(offsetZero, STREAM_SEEK_SET, NULL); //reset pointer to the beginning of data
		hr = pIWICStream->Read(imageData, dataLength, &pcbRead);
	}

	if (SUCCEEDED(hr)){
		imgBuffer = IFactory::instance()->createByteBuffer(imageData, dataLength);
	}

	if (SUCCEEDED(hr)){
		hr = pFactory->CreateDecoderFromFilename(
			path->Data(),
			nullptr,
			GENERIC_READ,
			WICDecodeMetadataCacheOnDemand,
			&pIDecoder);
	}

	if (SUCCEEDED(hr)){
		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
	}

	if (SUCCEEDED(hr)){
		hr = pFactory->CreateBitmapFromSource(pIDecoderFrame, WICBitmapCacheOnDemand, &pBitmap); 
	}

	//-- release 
	if (pFactory){
		pFactory->Release();
	}

	if (pIDecoder){
		pIDecoder->Release();
	}

	if (pIDecoderFrame){
		pIDecoderFrame->Release();
	}

	if (pIWICStream){
		pIWICStream->Release();
	}

	if (!SUCCEEDED(hr)){
		ILogger::instance()->logError("Unnable to create bitmap from file");
		return NULL;
	}
	
	return new Image_win8(pBitmap, imgBuffer);
	//return new Image_win8(pBitmap, NULL); //-- only for test createImageBuffer. Remove later
}


bool Image_win8::exportToFile(const URL& fileUrl, const Image_win8* image){

	// WIC interface pointers.
	IWICImagingFactory* pFactory = NULL;
	IWICStream* pIWICStream = NULL;
	IWICBitmapDecoder* pIDecoder = NULL;

	HRESULT hr = CoCreateInstance(
		CLSID_WICImagingFactory,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IWICImagingFactory,
		(LPVOID*)&pFactory
		);

	// Create a WIC stream to map onto the memory.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateStream(&pIWICStream);
	}

	// get image data and initialize stream
	ByteBuffer_win8* buffer = (ByteBuffer_win8*)image->getSourceBuffer();
	if (buffer == NULL){
		buffer = (ByteBuffer_win8*)image->createImageBuffer();
	}
	BYTE* data = buffer->getPointer();
	int dataLength = buffer->size();

	// Initialize the stream with the memory pointer and size.
	if (SUCCEEDED(hr)){
		hr = pIWICStream->InitializeFromMemory(
			data,
			dataLength);
	}

	// Create a decoder for the stream.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateDecoderFromStream(
			pIWICStream,                   // The stream to use to create the decoder
			NULL,                          // Do not prefer a particular vendor
			WICDecodeMetadataCacheOnLoad,  // Cache metadata when needed
			&pIDecoder);                   // Pointer to the decoder
	}

	// Build file name according compression type
	const StringUtils_win8* sUtils = (StringUtils_win8*)IStringUtils::instance();
	GUID containerFormat;
	hr = pIDecoder->GetContainerFormat(&containerFormat);
	std::string ext = getContainerFormatExtension(containerFormat);
	Platform::String^ extension = Platform::String::Concat(".", sUtils->toStringHat(ext));
	Platform::String^ fileName = Platform::String::Concat(sUtils->toStringHat(fileUrl._path), extension);

	// Create a output stream onto file
	IWICStream* outStream;
	pFactory->CreateStream(&outStream);
	hr = outStream->InitializeFromFilename(fileName->Data(), GENERIC_WRITE);

	LARGE_INTEGER offsetZero = { 0, 0 };
	hr = pIWICStream->Seek(offsetZero, STREAM_SEEK_SET, NULL);
	BYTE* tmpBuffer = new BYTE[dataLength];
	// Copy data from input to output stream
	if (SUCCEEDED(hr)){
		hr = pIWICStream->Read(tmpBuffer, dataLength, NULL);
	}

	if (SUCCEEDED(hr)){
		hr = outStream->Write(tmpBuffer, dataLength, NULL);
	}

	if (SUCCEEDED(hr)){
		outStream->Commit(STGC_DEFAULT);
	}
	delete[] tmpBuffer;

	if (!SUCCEEDED(hr)){
		ILogger::instance()->logError("Unnable to export image to file");
		return false;
	}

	return true;
}



//==============================================================================================================================================================================================
//==			EXPERIMENTOS
//==============================================================================================================================================================================================

//Image_win8* Image_win8::imageFromFile(std::string name){
//
//	// WIC interface pointers.
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ path = Platform::String::Concat(tmpPath, toStringHat(name));
//
//	hr = pFactory->CreateDecoderFromFilename(
//		path->Data(),
//		nullptr,
//		GENERIC_READ,
//		WICDecodeMetadataCacheOnDemand,
//		&pIDecoder);
//
//	if (SUCCEEDED(hr)){
//		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
//	}
//	else{
//		return NULL;
//	}
//
//	UINT bpp = 0;
//	WICPixelFormatGUID pPixelFormat;
//	UINT width = 0;
//	UINT height = 0;
//	if (SUCCEEDED(hr)){
//		pIDecoderFrame->GetSize(&width, &height);
//
//		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
//		if (SUCCEEDED(hr)){
//			bpp = getbppFromPixelFormat(pPixelFormat);
//			ILogger::instance()->logInfo("PIXEL FORMAT OK: \"%#x, %#x, %#x\", bpp: %d \n", pPixelFormat.Data1, pPixelFormat.Data2, pPixelFormat.Data3, bpp);
//		}
//		else{
//			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
//		}
//	}
//	else{
//		return NULL;
//	}
//
//	hr = pFactory->CreateBitmap(width, height, pPixelFormat, WICBitmapCacheOnDemand, &pBitmap);
//	
//	WICRect rcLock = { 0, 0, width, height };
//	IWICBitmapLock *pLock = NULL;
//	BYTE *pv = NULL;
//
//	if (SUCCEEDED(hr))
//	{
//		hr = pBitmap->Lock(&rcLock, WICBitmapLockRead, &pLock);
//		if (SUCCEEDED(hr))
//		{
//			UINT cbBufferSize = 0;
//			UINT cbStride = 0;
//
//			hr = pLock->GetStride(&cbStride);
//			ILogger::instance()->logInfo("STRIDE: \"%d\"\n", cbStride);
//
//			if (SUCCEEDED(hr))
//			{
//				hr = pLock->GetDataPointer(&cbBufferSize, &pv);
//				ILogger::instance()->logInfo("BUFFER SIZE: \"%d\"\n", cbBufferSize);
//			}
//
//			// Release the bitmap lock.
//			pLock->Release();
//		}
//	}
//
//	if (SUCCEEDED(hr)){
//
//		return new Image_win8(pBitmap, pv);
//	}
//
//	return NULL; //TODO: faltan todos los releases
//}


//IWICBitmap* Image_win8::imageWithData(BYTE* data, int dataLength){
//	// WIC interface pointers.
//	IWICStream *pIWICStream = NULL;
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//
//	BYTE* imageData = (BYTE*)HeapAlloc(GetProcessHeap(), HEAP_NO_SERIALIZE | HEAP_GENERATE_EXCEPTIONS, dataLength);
//	memcpy(imageData, data, dataLength);
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//
//	// Create a WIC stream to map onto the memory.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateStream(&pIWICStream);
//	}
//	else{
//		return NULL;
//	}
//
//	// Initialize the stream with the memory pointer and size.
//	if (SUCCEEDED(hr)){
//		hr = pIWICStream->InitializeFromMemory(
//			imageData,
//			dataLength);
//	}
//	else{
//		return NULL;
//	}
//
//	// Create a decoder for the stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateDecoderFromStream(
//			pIWICStream,                   // The stream to use to create the decoder
//			NULL,                          // Do not prefer a particular vendor
//			WICDecodeMetadataCacheOnLoad,  // Cache metadata when needed
//			&pIDecoder);                   // Pointer to the decoder
//	}
//	else{
//		return NULL;
//	}
//
//	UINT bpp = 0;
//	WICPixelFormatGUID pPixelFormat;
//
//	if (SUCCEEDED(hr)){
//		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
//
//		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
//		if (SUCCEEDED(hr)){
//			bpp = getbppFromPixelFormat(pPixelFormat);
//			ILogger::instance()->logInfo("PIXEL FORMAT OK: \"%#x, %#x, %#x \"\n", pPixelFormat.Data1, pPixelFormat.Data2, pPixelFormat.Data3);
//		}
//		else{
//			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
//		}
//	}
//	else{
//		return NULL;
//	}
//
//	UINT width = 0;
//	UINT height = 0;
//	if (SUCCEEDED(hr)){
//		pIDecoderFrame->GetSize(&width, &height);
//	}
//	else{
//		return NULL;
//	}
//
//	UINT stride = getStride(width, bpp);
//	UINT bufferSize = stride * height;
//
//	//hr = pFactory->CreateBitmapFromMemory(width, height, GUID_WICPixelFormat32bppRGBA, stride, bufferSize, data, &pBitmap);
//	hr = pFactory->CreateBitmapFromMemory(width, height, pPixelFormat, stride, bufferSize, data, &pBitmap);
//
//	//HeapFree(GetProcessHeap(), HEAP_NO_SERIALIZE | HEAP_GENERATE_EXCEPTIONS, imageData);
//
//	//-------------------------------------------------------
//	IWICStream* outStream;
//	// Create a stream for the encoder
//	pFactory->CreateStream(&outStream);
//	// Initialize the stream using the output file path
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ outputPath = Platform::String::Concat(tmpPath, "tiger_recovered.jpg");
//	hr = outStream->InitializeFromFilename(outputPath->Data(), GENERIC_WRITE);
//	ULARGE_INTEGER cb;
//	cb.HighPart = 0;
//	cb.LowPart = dataLength;
//	LARGE_INTEGER ini;
//	ini.HighPart = 0;
//	ini.LowPart = 0;
//
//	pIWICStream->Seek(ini, STREAM_SEEK_SET, NULL);
//	//hr = pIWICStream->CopyTo(outStream, cb, NULL, NULL);
//	BYTE* buffer = new BYTE[dataLength];
//	hr = pIWICStream->Read(buffer, dataLength, NULL);
//	hr = outStream->Write(buffer, dataLength, NULL);
//
//	if (SUCCEEDED(hr)){
//		outStream->Commit(STGC_DEFAULT);
//		ILogger::instance()->logInfo("Stream de salida OK");
//	}
//	else{
//		ILogger::instance()->logInfo("Pos no tira el stream de salida !");
//	}
//	outStream->Commit(STGC_DEFAULT);
//	//-------------------------------------------------------
//
//	if (SUCCEEDED(hr)){
//		return pBitmap;//TODO: faltan todos los releases
//	}
//
//	return NULL;
//}


//IWICBitmap* Image_win8::imageWithData(BYTE* data, int dataLength){
//
//	// WIC interface pointers.
//	IWICStream *pIWICStream = NULL;
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//	IWICBitmapEncoder *pEncoder = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	// Create a WIC stream to map onto the memory.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateStream(&pIWICStream);
//	}
//	else{
//		return NULL;
//	}
//
//	// Initialize the stream with the memory pointer and size.
//	if (SUCCEEDED(hr)){
//		hr = pIWICStream->InitializeFromMemory(
//			data,
//			dataLength);
//	}
//	else{
//		return NULL;
//	}
//
//	// Create a encoder for the stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateEncoder(
//			GUID_ContainerFormatPng,
//			0, // vendor
//			&pEncoder);
//	}
//	else{
//		return NULL;
//	}
//
//	if (SUCCEEDED(hr)){
//		pEncoder->Initialize(
//			pIWICStream,
//			WICBitmapEncoderNoCache);
//	}
//	else{
//		return NULL;
//	}
//
//	IWICBitmapFrameEncode* frameEncode;
//	IWICBitmapSource* bitmapSource = NULL;
//
//	if (SUCCEEDED(hr)){
//		hr = pEncoder->CreateNewFrame(&frameEncode, nullptr);
//	}
//	else{
//		return NULL;
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = frameEncode->WriteSource(bitmapSource, nullptr);
//	}
//	else{
//		return NULL;
//	}
//
//	hr = pFactory->CreateBitmapFromSource(bitmapSource, WICBitmapCacheOnDemand, &pBitmap);
//	
//	if (SUCCEEDED(hr)){
//		return pBitmap;//TODO: faltan todos los releases
//	}
//
//	return NULL;
//}


//Image_win8* Image_win8::imageFromFile(std::string name){
//
//	// WIC interface pointers.
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ path = Platform::String::Concat(tmpPath, toStringHat(name));
//
//	hr = pFactory->CreateDecoderFromFilename(
//		path->Data(),
//		nullptr,
//		GENERIC_READ,
//		WICDecodeMetadataCacheOnDemand,
//		&pIDecoder);
//
//	if (SUCCEEDED(hr)){
//		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
//	}
//	else{
//		return NULL;
//	}
//
//	WICPixelFormatGUID pPixelFormat;
//
//	if (SUCCEEDED(hr)){
//		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
//
//		if (SUCCEEDED(hr)){
//			/*IWICBitmapSource* pBitmapSource = NULL;
//			hr = WICConvertBitmapSource(pPixelFormat, pIDecoderFrame, &pBitmapSource);*/
//			hr = pFactory->CreateBitmapFromSource(pIDecoderFrame, WICBitmapCacheOnDemand, &pBitmap);
//			return new Image_win8(pBitmap, NULL);
//		}
//		else{
//			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
//		}
//	}
//
//	return NULL; //TODO: faltan todos los releases
//}



//Image_win8* Image_win8::imageFromFile(std::string name){
//
//	// WIC interface pointers.
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ path = Platform::String::Concat(tmpPath, toStringHat(name));
//
//	hr = pFactory->CreateDecoderFromFilename(
//		path->Data(),
//		nullptr,
//		GENERIC_READ,
//		WICDecodeMetadataCacheOnDemand,
//		&pIDecoder);
//	
//	if (SUCCEEDED(hr)){
//		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
//
//		/*DWORD* capability = 0;
//		pIDecoder->QueryCapability(,capability);*/
//
//		//IWICBitmapDecoderInfo* pDecoderInfo = NULL;
//		//hr = pIDecoder->GetDecoderInfo(&pDecoderInfo);
//		////pDecoderInfo->GetFriendlyName();
//		//GUID containerFormat;
//		//hr = pIDecoder->GetContainerFormat(&containerFormat);
//		//
//		//if (SUCCEEDED(hr)){
//		//	ILogger::instance()->logInfo("DECODER INFO OK ");
//		//	//pDecoderInfo->g
//		//	//ILogger::instance()->logInfo("DECODER INFO: \"%l, %h, %h, %hh \"\n", pDecoderInfo->GetComponentType->Data1, pDecoderInfo->GetComponentType->Data2, pDecoderInfo->GetComponentType->Data3, pDecoderInfo->GetComponentType->Data4);
//		//	ILogger::instance()->logInfo("DECODER INFO: \"%#x, %#x, %#x \"\n", containerFormat.Data1, containerFormat.Data2, containerFormat.Data3);
//		//}
//		//else{
//		//	ILogger::instance()->logInfo("DECODER INFO UNKNOWN");
//		//}
//	}
//	else{
//		return NULL;
//	}
//
//	UINT bpp = 0;
//	WICPixelFormatGUID pPixelFormat;
//	UINT width = 0;
//	UINT height = 0;
//	if (SUCCEEDED(hr)){
//		pIDecoderFrame->GetSize(&width, &height);
//		
//		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
//
//		/*IWICBitmapSource* pBitmapSource = NULL;
//		hr = WICConvertBitmapSource(pPixelFormat, pIDecoderFrame, &pBitmapSource);
//		hr = pFactory->CreateBitmapFromSource(pBitmapSource, WICBitmapCacheOnDemand, &pBitmap);*/
//
//		if (SUCCEEDED(hr)){
//			bpp = getbppFromPixelFormat(pPixelFormat);
//			ILogger::instance()->logInfo("PIXEL FORMAT OK: \"%#x, %#x, %#x\", bpp: %d \n", pPixelFormat.Data1, pPixelFormat.Data2, pPixelFormat.Data3, bpp);
//		}
//		else{
//			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
//		}
//	}
//	else{
//		return NULL;
//	}
//
//	UINT stride = getStride(width, bpp);
//	ILogger::instance()->logInfo("stride: %d \n", stride);
//	UINT bufferSize = stride * height;
//	
//	BYTE* imageData = (BYTE*)malloc(bufferSize);
//
//	hr = pIDecoderFrame->CopyPixels(0, // entire bitmap
//									stride, //
//									bufferSize, //
//									imageData);
//
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateBitmapFromMemory(width, height, pPixelFormat, stride, bufferSize, imageData, &pBitmap);
//
//		if (SUCCEEDED(hr)){
//			return new Image_win8(pBitmap, imageData);
//		}
//		return NULL;
//	}
//
//	return NULL; //TODO: faltan todos los releases
//}


//Image_win8* Image_win8::imageFromFile(std::string finaName){
//
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ path = Platform::String::Concat(tmpPath, toStringHat(finaName));
//
//	// WIC interface pointers.
//	IWICBitmapDecoder *pIDecoder = NULL;
//	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
//	IWICStream *pIWICStream = NULL;
//	IWICImagingFactory *pFactory = NULL;
//	IWICBitmap *pBitmap = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	hr = pFactory->CreateStream(&pIWICStream);
//	pIWICStream->InitializeFromFilename(path->Data(), GENERIC_READ);
//
//	//-- calculate stream length
//	STATSTG* pStatstg = new STATSTG();
//	ULARGE_INTEGER* newPos = new ULARGE_INTEGER();
//	LARGE_INTEGER ini;
//	ini.HighPart = 0;
//	ini.LowPart = 0;
//	pIWICStream->Seek(ini, STREAM_SEEK_END, newPos);
//	pIWICStream->Stat(pStatstg, STATFLAG_DEFAULT);
//	ULARGE_INTEGER streamSize = pStatstg->cbSize;
//
//	int dataLength = streamSize.LowPart;
//	//BYTE* imageData = (BYTE*)malloc(dataLength);
//	BYTE* imageData = new BYTE[dataLength];
//	//HGLOBAL hGlobal = GlobalAlloc(GMEM_MOVEABLE | GMEM_NODISCARD, streamSize);
//	//BYTE* imageData = (BYTE*)HeapAlloc(GetProcessHeap(), HEAP_NO_SERIALIZE | HEAP_GENERATE_EXCEPTIONS, dataLength);
//	//IStream* stream = NULL;
//	//hr = CreateStreamOnHGlobal(imageData, TRUE, &stream);
//	//IStream* stream = SHCreateMemStream(imageData, dataLength);
//
//	pIWICStream->Seek(ini, STREAM_SEEK_SET, newPos); //set pointer to the beginning of data
//	//ULARGE_INTEGER* pcbWritten = new ULARGE_INTEGER();
//	//pIWICStream->CopyTo(stream, streamSize, NULL, pcbWritten);
//	ULONG* pcbRead = new ULONG();
//	hr = pIWICStream->Read(imageData, dataLength, pcbRead);
//
//	/*if (pcbWritten->LowPart >= streamSize.LowPart){
//	ILogger::instance()->logInfo("POS YA ESTA COPIAO EL STREAM !");
//	stream->Commit(STGC_DEFAULT);
//	}*/
//
//	hr = pFactory->CreateDecoderFromFilename(
//		path->Data(),
//		nullptr,
//		GENERIC_READ,
//		WICDecodeMetadataCacheOnDemand,
//		&pIDecoder);
//
//	if (SUCCEEDED(hr)){
//		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
//	}
//	else{
//		return NULL;
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateBitmapFromSource(pIDecoderFrame, WICBitmapCacheOnDemand, &pBitmap);
//
//		if (SUCCEEDED(hr)){
//			//stream->Commit(STGC_DEFAULT);
//			return new Image_win8(pBitmap, imageData);
//		}
//		else{
//			ILogger::instance()->logInfo("UNNABLE TO CREATE BITMAP FROM SOURCE");
//		}
//	}
//	else{
//		ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
//	}
//
//	return NULL; //TODO: faltan todos los releases
//}


//BYTE* Image_win8::getImageBuffer() const{
//
//	if (_image == nullptr){
//		return NULL;
//	}
//
//	IWICImagingFactory *pFactory = NULL;
//	//IWICBitmap *pBitmap = NULL;
//	BYTE *pv = NULL;
//
//	UINT uiWidth = getWidth();
//	UINT uiHeight = getHeight();
//	//WICPixelFormatGUID formatGUID = GUID_WICPixelFormat32bppRGBA;
//
//	WICRect rcLock = { 0, 0, uiWidth, uiHeight };
//	IWICBitmapLock *pLock = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	/*if (SUCCEEDED(hr))
//	{
//	hr = pFactory->CreateBitmap(uiWidth, uiHeight, formatGUID, WICBitmapCacheOnDemand, &pBitmap);
//	}
//	else{
//	return NULL;
//	}*/
//
//	if (SUCCEEDED(hr))
//	{
//		hr = _image->Lock(&rcLock, WICBitmapLockWrite, &pLock);
//
//		if (SUCCEEDED(hr))
//		{
//			UINT cbBufferSize = 0;
//			//UINT cbStride = 0;
//			//hr = pLock->GetStride(&cbStride);
//
//			if (SUCCEEDED(hr))
//			{
//				hr = pLock->GetDataPointer(&cbBufferSize, &pv);
//			}
//
//			// Release the bitmap lock.
//			pLock->Release();
//		}
//	}
//
//	if (pFactory)
//	{
//		pFactory->Release();
//	}
//
//	return pv;
//}


//IByteBuffer* Image_win8::createImageBuffer() const{
//
//	if (_sourceBuffer != nullptr){
//		return _sourceBuffer;
//	}
//
//	if (_image == nullptr){
//		return NULL;
//	}
//
//	// WIC interface pointers.
//	IWICImagingFactory* pFactory = NULL;
//	IWICStream* pInputStream = NULL;
//	IWICStream* pOutputStream = NULL;
//	IWICBitmapDecoder* pIDecoder = NULL;
//	IWICBitmapEncoder* pIEncoder = NULL;
//	IWICBitmapFrameDecode* pIDecoderFrame = NULL;
//	IWICBitmapFrameEncode* pIEncoderFrame = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	IWICBitmapSource* pBitmapSource = NULL;
//	IWICBitmap* pNewBitmap = NULL;
//	hr = WICConvertBitmapSource(GUID_WICPixelFormat32bppRGBA, _image, &pBitmapSource);
//	hr = pFactory->CreateBitmapFromSource(pBitmapSource, WICBitmapCacheOnDemand, &pNewBitmap);
//
//	IByteBuffer* imgData = getBitmapBuffer(pNewBitmap);
//	BYTE* data = ((ByteBuffer_win8*)imgData)->getPointer();
//	int dataLength = imgData->size();
//
//	IByteBuffer* imgBuffer = IFactory::instance()->createByteBuffer(data, dataLength);
//	return imgBuffer;
//}



//IByteBuffer* Image_win8::createImageBuffer() const{
//
//	if (_sourceBuffer != nullptr){
//		return _sourceBuffer;
//	}
//
//	if (_image == nullptr){
//		return NULL;
//	}
//
//	// WIC interface pointers.
//	IWICImagingFactory* pFactory = NULL;
//	IWICStream* pInputStream = NULL;
//	IWICStream* pOutputStream = NULL;
//	IWICBitmapEncoder* pIEncoder = NULL;
//	IWICBitmapFrameEncode* pIEncoderFrame = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	// Create a stream for the encoder
//	pFactory->CreateStream(&pOutputStream);
//	// Initialize the stream using the output file path
//	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
//	Platform::String^ folderPath = localFolder->Path;
//	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
//	Platform::String^ outputPath = Platform::String::Concat(tmpPath, "tiger_tmp.png");
//	//Platform::String^ outputPath = Platform::String::Concat(tmpPath, "MARBLES_RECOVERED.BMP");
//	hr = pOutputStream->InitializeFromFilename(outputPath->Data(), GENERIC_WRITE);
//
//	// Create a encoder for the output stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateEncoder(
//			GUID_ContainerFormatPng, // png compression
//			0, // vendor
//			&pIEncoder);
//	}
//
//	// Init enconder
//	if (SUCCEEDED(hr)){
//		hr = pIEncoder->Initialize(
//			pOutputStream,
//			WICBitmapEncoderNoCache);
//	}
//
//	//Get and create image frame
//	if (SUCCEEDED(hr)){
//		hr = pIEncoder->CreateNewFrame(&pIEncoderFrame, nullptr);
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = pIEncoderFrame->Initialize(NULL);
//	}
//
//	// Set frame size
//	if (SUCCEEDED(hr)){
//		UINT width, height;
//		_image->GetSize(&width, &height);
//		hr = pIEncoderFrame->SetSize(width, height);
//	}
//
//	// Set resolution
//	if (SUCCEEDED(hr)){
//		double pDpiX, pDpiY;
//		_image->GetResolution(&pDpiX, &pDpiY);
//		hr = pIEncoderFrame->SetResolution(pDpiX, pDpiY);
//	}
//
//	// Set pixel format
//	if (SUCCEEDED(hr)){
//		WICPixelFormatGUID pixelFormat;
//		_image->GetPixelFormat(&pixelFormat);
//		hr = pIEncoderFrame->SetPixelFormat(&pixelFormat);
//	}
//
//	/*IWICMetadataQueryReader* pMetaQueryReader = NULL;
//	IWICMetadataQueryWriter* pMetaQueryWriter = NULL;
//	if (SUCCEEDED(hr)){
//	pIDecoderFrame->GetMetadataQueryReader(&pMetaQueryReader);
//	pIEncoderFrame->GetMetadataQueryWriter(&pMetaQueryWriter);
//	}
//
//	if (SUCCEEDED(hr)){
//	IEnumString* pIEnumString;
//	pMetaQueryReader->GetEnumerator(&pIEnumString);
//	LPOLESTR psz;
//	while (S_OK == pIEnumString->Next(1, &psz, NULL))
//	{
//	pMetaQueryWriter->SetMetadataByName(psz, NULL);
//	}
//	pIEnumString->Release();
//	pMetaQueryReader->Release();
//	pMetaQueryWriter->Release();
//	}*/
//
//	if (SUCCEEDED(hr)){
//		hr = pIEncoderFrame->WriteSource(_image, nullptr);
//		hr = pIEncoderFrame->Commit();
//		hr = pIEncoder->Commit();
//		//hr = pOutputStream->Commit(STGC_DEFAULT);
//	}
//
//	//-- calculate stream length
//	if (SUCCEEDED(hr)){
//		/*ULARGE_INTEGER newPos;
//		LARGE_INTEGER offset = { 0, 0 };
//		hr = pOutputStream->Seek(offset, STREAM_SEEK_CUR, &newPos);*/
//		STATSTG pStatstg;
//		hr = pOutputStream->Stat(&pStatstg, STATFLAG_DEFAULT);
//		int outputLength = pStatstg.cbSize.LowPart;
//	}
//
//	//-- release 
//	if (pFactory){
//		pFactory->Release();
//	}
//
//	if (pIEncoder){
//		pIEncoder->Release();
//	}
//
//	if (pIEncoderFrame){
//		pIEncoderFrame->Release();
//	}
//
//	if (pInputStream){
//		pInputStream->Release();
//	}
//	if (pOutputStream){
//		pOutputStream->Release();
//	}
//
//	if (!SUCCEEDED(hr)){
//		ILogger::instance()->logError("Unnable to create image data buffer");
//		return NULL;
//	}
//
//	/*IByteBuffer* imgBuffer = IFactory::instance()->createByteBuffer(outputData, outputLength);
//	return imgBuffer;*/
//	return NULL;
//}



//IByteBuffer* Image_win8::createImageBuffer() const{
//
//	if (_sourceBuffer != nullptr){
//		return _sourceBuffer;
//	}
//
//	if (_image == nullptr){
//		return NULL;
//	}
//
//	// WIC interface pointers.
//	IWICImagingFactory* pFactory = NULL;
//	IWICStream* pInputStream = NULL;
//	IWICStream* pOutputStream = NULL;
//	IWICBitmapDecoder* pIDecoder = NULL;
//	IWICBitmapEncoder* pIEncoder = NULL;
//	IWICBitmapFrameDecode* pIDecoderFrame = NULL;
//	IWICBitmapFrameEncode* pIEncoderFrame = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	// Create input and output stream
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateStream(&pInputStream);
//		hr = pFactory->CreateStream(&pOutputStream);
//	}
//
//	// getBitmapBuffer
//	IByteBuffer* imgData = getBitmapBuffer(_image);
//	BYTE* inputData = ((ByteBuffer_win8*)imgData)->getPointer();
//	int inputLength = imgData->size();
//
//	/*IStream* stream = NULL;
//	if (SUCCEEDED(hr)){
//		hr = CreateStreamOnHGlobal(inputdata, TRUE, &stream);
//	}
//	hr = pInputStream->InitializeFromIStream(stream);*/
//
//	// Initialize the stream with the memory pointer and size.
//	if (SUCCEEDED(hr)){
//		hr = pInputStream->InitializeFromMemory(
//			inputData,
//			inputLength);
//	}
//	
//	// Create a decoder for the stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateDecoderFromStream(
//			pInputStream,                   // The stream to use to create the decoder
//			NULL,                          // Do not prefer a particular vendor
//			WICDecodeMetadataCacheOnLoad,  // Cache metadata when needed
//			&pIDecoder);                   // Pointer to the decoder
//		/*hr = pFactory->CreateDecoder(GUID_ContainerFormatBmp, NULL, &pIDecoder);
//		hr = pIDecoder->Initialize(pInputStream, WICDecodeMetadataCacheOnLoad);*/
//	}
//	
//	//-- reserve memory for output stream (assume compression reduces sizes in 75%)
//	int outputLength = inputLength;
//	BYTE* outputData = new BYTE[outputLength];
//
//	// Initialize the output stream with the memory pointer and size.
//	if (SUCCEEDED(hr)){
//		hr = pOutputStream->InitializeFromMemory(outputData, outputLength);
//	}
//
//	// Create a encoder for the output stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateEncoder(
//			GUID_ContainerFormatPng, // png compression
//			0, // vendor
//			&pIEncoder);
//	}
//
//	// Init enconder
//	if (SUCCEEDED(hr)){
//		pIEncoder->Initialize(
//			pOutputStream,
//			WICBitmapEncoderNoCache);
//	}
//
//	//Get and create image frame
//	if (SUCCEEDED(hr)){
//		pIDecoder->GetFrame(0, &pIDecoderFrame);
//	}
//	if (SUCCEEDED(hr)){
//		pIEncoder->CreateNewFrame(&pIEncoderFrame, nullptr);
//	}
//
//	if (SUCCEEDED(hr)){
//		pIEncoderFrame->Initialize(NULL);
//	}
//
//	//Set frame size
//	if (SUCCEEDED(hr)){
//		UINT width, height;
//		pIDecoderFrame->GetSize(&width, &height);
//		pIEncoderFrame->SetSize(width, height);
//	}
//
//	//Set pixel format
//	if (SUCCEEDED(hr)){
//		WICPixelFormatGUID pixelFormat;
//		pIDecoderFrame->GetPixelFormat(&pixelFormat);
//		pIEncoderFrame->SetPixelFormat(&pixelFormat);
//	}
//
//	IWICMetadataQueryReader* pMetaQueryReader = NULL;
//	IWICMetadataQueryWriter* pMetaQueryWriter = NULL;
//	if (SUCCEEDED(hr)){
//		pIDecoderFrame->GetMetadataQueryReader(&pMetaQueryReader);
//		pIEncoderFrame->GetMetadataQueryWriter(&pMetaQueryWriter);
//	}
//
//	if (SUCCEEDED(hr)){
//		IEnumString* pIEnumString;
//		pMetaQueryReader->GetEnumerator(&pIEnumString);
//		LPOLESTR psz;
//		while (S_OK == pIEnumString->Next(1, &psz, NULL))
//		{
//			pMetaQueryWriter->SetMetadataByName(psz, NULL);
//		}
//		pIEnumString->Release();
//		pMetaQueryReader->Release();
//		pMetaQueryWriter->Release();
//	}
//
//	if (SUCCEEDED(hr)){
//		pIEncoderFrame->WriteSource(static_cast<IWICBitmapSource*> (pIDecoderFrame), nullptr);
//		pIEncoderFrame->Commit();
//		pIEncoder->Commit();
//	}
//
//	//-- calculate stream length
//	if (SUCCEEDED(hr)){
//		/*STATSTG pStatstg;
//		hr = pOutputStream->Stat(&pStatstg, STATFLAG_DEFAULT);
//		outputLength = pStatstg.cbSize.LowPart;*/
//		ULARGE_INTEGER newPos;
//		LARGE_INTEGER offset = { 0, 0 };
//		hr = pOutputStream->Seek(offset, STREAM_SEEK_CUR, &newPos);
//		outputLength = newPos.LowPart;
//	}
//
//	if (pFactory){
//		pFactory->Release();
//	}
//
//	if (pIDecoder){
//		pIDecoder->Release();
//	}
//
//	if (pIEncoder){
//		pIEncoder->Release();
//	}
//
//	if (pIDecoderFrame){
//		pIDecoderFrame->Release();
//	}
//
//	if (pIEncoderFrame){
//		pIEncoderFrame->Release();
//	}
//
//	if (pInputStream){
//		pInputStream->Release();
//	}
//	if (pOutputStream){
//		pOutputStream->Release();
//	}
//
//	if (!SUCCEEDED(hr)){
//		ILogger::instance()->logError("Unnable to create image data buffer");
//		return NULL;
//	}
//
//	IByteBuffer* imgBuffer = IFactory::instance()->createByteBuffer(outputData, outputLength);
//	return imgBuffer;
//}



//IByteBuffer* Image_win8::createImageBuffer() const{
//
//	//-- This workaround creates image buffer compressed in png format, but metadata are not included.
//	if (_sourceBuffer != nullptr){
//		return _sourceBuffer;
//	}
//
//	if (_image == nullptr){
//		return NULL;
//	}
//
//	// WIC interface pointers.
//	IWICImagingFactory* pFactory = NULL;
//	IWICStream* pOutputStream = NULL;
//	IWICBitmapEncoder* pIEncoder = NULL;
//	IWICBitmapFrameEncode* pIEncoderFrame = NULL;
//
//	HRESULT hr = CoCreateInstance(
//		CLSID_WICImagingFactory,
//		NULL,
//		CLSCTX_INPROC_SERVER,
//		IID_IWICImagingFactory,
//		(LPVOID*)&pFactory
//		);
//
//	// Create input and output stream
//	if (SUCCEEDED(hr)){
//		//hr = pFactory->CreateStream(&pInputStream);
//		hr = pFactory->CreateStream(&pOutputStream);
//	}
//
//	// getBitmapBuffer
//	IByteBuffer* bitmapBuffer = getBitmapBuffer(_image);
//
//	//-- reserve memory for output data
//	int outputLength = bitmapBuffer->size();
//	BYTE* outputData = new BYTE[outputLength];
//	//BYTE* outputData = (BYTE*)HeapAlloc(GetProcessHeap(), HEAP_NO_SERIALIZE | HEAP_GENERATE_EXCEPTIONS | HEAP_ZERO_MEMORY, imgLength);
//	/*IStream* stream = NULL;
//	if (SUCCEEDED(hr)){
//	hr = CreateStreamOnHGlobal(outputData, TRUE, &stream);
//	}*/
//
//	// Initialize the output stream with the memory pointer and size.
//	if (SUCCEEDED(hr)){
//		hr = pOutputStream->InitializeFromMemory(outputData, outputLength);
//	}
//	/*if (SUCCEEDED(hr)){
//	hr = pOutputStream->InitializeFromIStream(stream);
//	}*/
//
//	// Create a encoder for the output stream.
//	if (SUCCEEDED(hr)){
//		hr = pFactory->CreateEncoder(
//			GUID_ContainerFormatPng, // png compression
//			0, // vendor
//			&pIEncoder);
//	}
//
//	// Init enconder
//	if (SUCCEEDED(hr)){
//		hr = pIEncoder->Initialize(
//			pOutputStream,
//			WICBitmapEncoderNoCache);
//	}
//
//	//Get and create image frame
//	if (SUCCEEDED(hr)){
//		hr = pIEncoder->CreateNewFrame(&pIEncoderFrame, nullptr);
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = pIEncoderFrame->Initialize(NULL);
//	}
//
//	// Set frame size
//	if (SUCCEEDED(hr)){
//		UINT width, height;
//		_image->GetSize(&width, &height);
//		hr = pIEncoderFrame->SetSize(width, height);
//	}
//
//	// Set resolution
//	if (SUCCEEDED(hr)){
//		double pDpiX, pDpiY;
//		_image->GetResolution(&pDpiX, &pDpiY);
//		hr = pIEncoderFrame->SetResolution(pDpiX, pDpiY);
//	}
//
//	// Set pixel format
//	if (SUCCEEDED(hr)){
//		WICPixelFormatGUID pixelFormat;
//		_image->GetPixelFormat(&pixelFormat);
//		hr = pIEncoderFrame->SetPixelFormat(&pixelFormat);
//	}
//
//	//-- This part attemps to copy metadata to output data, but
//	//-- a workaround to do it has not already been found.
//	//--
//	//IWICComponentInfo* pComponentInfo = NULL;
//	//IWICBitmapDecoderInfo* pDecoderInfo = NULL;
//	//IWICBitmapSource* pBitmapSource = NULL;
//	//hr = pFactory->CreateComponentInfo(CLSID_, &pComponentInfo);
//	//hr = _image->QueryInterface(IID_IWICBitmapSource, (void**)&pBitmapSource);
//	//hr = _image->QueryInterface(IID_IWICBitmapDecoderInfo, (void**)&pDecoderInfo);
//
//	/*IWICMetadataQueryReader* pMetaQueryReader = NULL;
//	IWICMetadataQueryWriter* pMetaQueryWriter = NULL;
//	if (SUCCEEDED(hr)){
//	pIDecoderFrame->GetMetadataQueryReader(&pMetaQueryReader);
//	pIEncoderFrame->GetMetadataQueryWriter(&pMetaQueryWriter);
//	}
//
//	if (SUCCEEDED(hr)){
//	IEnumString* pIEnumString;
//	pMetaQueryReader->GetEnumerator(&pIEnumString);
//	LPOLESTR psz;
//	while (S_OK == pIEnumString->Next(1, &psz, NULL))
//	{
//	pMetaQueryWriter->SetMetadataByName(psz, NULL);
//	}
//	pIEnumString->Release();
//	pMetaQueryReader->Release();
//	pMetaQueryWriter->Release();
//	}*/
//
//	if (SUCCEEDED(hr)){
//		hr = pIEncoderFrame->WriteSource(_image, nullptr);
//		hr = pIEncoderFrame->Commit();
//		hr = pIEncoder->Commit();
//	}
//
//	//-- calculate output stream length
//	int bufferLength = 0;
//	if (SUCCEEDED(hr)){
//		ULARGE_INTEGER newPos;
//		LARGE_INTEGER offsetZero = { 0, 0 };
//		hr = pOutputStream->Seek(offsetZero, STREAM_SEEK_CUR, &newPos);
//		bufferLength = newPos.LowPart;
//	}
//
//	//-- release 
//	if (pFactory){
//		pFactory->Release();
//	}
//
//	if (pIEncoder){
//		pIEncoder->Release();
//	}
//
//	if (pIEncoderFrame){
//		pIEncoderFrame->Release();
//	}
//
//	if (pOutputStream){
//		pOutputStream->Release();
//	}
//
//	if (!SUCCEEDED(hr)){
//		ILogger::instance()->logError("Unnable to create image data buffer");
//		return NULL;
//	}
//
//	// copy data to buffer of the right size and release old buffer
//	BYTE* bufferData = new BYTE[bufferLength];
//	memcpy(bufferData, outputData, bufferLength);
//	delete[] outputData;
//	//delete bitmapBuffer;
//
//	IByteBuffer* imageBuffer = IFactory::instance()->createByteBuffer(bufferData, bufferLength);
//	return imageBuffer;
//}


//int getBitmapSize(IWICBitmap* bitmap) {
//
//	if (bitmap == nullptr){
//		return 0;
//	}
//
//	UINT uiWidth = 0;
//	UINT uiHeight = 0;
//	IWICImagingFactory *pFactory = NULL;
//	WICRect* rcLock = NULL;
//	IWICBitmapLock *pLock = NULL;
//	BYTE *pv = NULL;
//	UINT cbBufferSize = 0;
//
//	HRESULT hr = bitmap->GetSize(&uiWidth, &uiHeight);
//
//	if (SUCCEEDED(hr)){
//		rcLock = new WICRect{ 0, 0, uiWidth, uiHeight };
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = CoCreateInstance(
//			CLSID_WICImagingFactory,
//			NULL,
//			CLSCTX_INPROC_SERVER,
//			IID_IWICImagingFactory,
//			(LPVOID*)&pFactory
//			);
//	}
//
//	if (SUCCEEDED(hr)){
//		hr = bitmap->Lock(rcLock, WICBitmapLockRead, &pLock);
//
//		if (SUCCEEDED(hr)){
//			if (SUCCEEDED(hr)){
//				hr = pLock->GetDataPointer(&cbBufferSize, &pv);
//			}
//			// Release the bitmap lock.
//			pLock->Release();
//		}
//	}
//
//	if (pFactory){
//		pFactory->Release();
//	}
//
//	return cbBufferSize;
//}


