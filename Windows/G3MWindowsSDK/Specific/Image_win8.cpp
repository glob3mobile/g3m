#include "Image_win8.hpp"
//#include <wrl.h>
//#include <robuffer.h>
#include <exception>

//using namespace Windows::Storage::Streams;
//using namespace Microsoft::WRL;



Image_win8::Image_win8(IWICBitmap* image, unsigned char* sourceBuffer)
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
	//return (_image == nullptr) ? 0 : (int)_image->PixelWidth;
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
	//return (_image == nullptr) ? 0 : (int)_image->PixelHeight;
}

const Vector2I Image_win8::getExtent() const {
	return Vector2I(getWidth(), getHeight());
}

inline void ThrowIfFailed(HRESULT hr)
{
	if (FAILED(hr))
	{
		throw Platform::Exception::CreateException(hr);
	}
}

//unsigned char* getPointerToPixelData(IBuffer^ buffer){
//
//	ComPtr<IInspectable> insp(reinterpret_cast<IInspectable*>(buffer));
//
//	// Query the IBufferByteAccess interface.
//	ComPtr<IBufferByteAccess> bufferByteAccess;
//	ThrowIfFailed(insp.As(&bufferByteAccess));
//
//	// Retrieve the buffer data.
//	byte* pixels = nullptr;
//	ThrowIfFailed(bufferByteAccess->Buffer(&pixels));
//	return pixels;
//}


BYTE* Image_win8::getSourceBuffer() const {
	return _sourceBuffer;
}

void Image_win8::releaseSourceBuffer() const {
	_sourceBuffer = nullptr;
}

IWICBitmap* Image_win8::getBitmap() const{
	return _image;
}

const std::string Image_win8::description() const{
	IStringBuilder* isb = IStringBuilder::newStringBuilder();
	isb->addString("Image_win8 ");
	isb->addInt(getWidth());
	isb->addString("x");
	isb->addInt(getHeight());
	isb->addString(", _image=(");
	//isb->addString([[_image description] cStringUsingEncoding:NSUTF8StringEncoding]); TODO:
	isb->addString(")");
	std::string desc = isb->getString();
	delete isb;
	return desc;
}

IImage* Image_win8::shallowCopy() const{
	return new Image_win8(_image, _sourceBuffer);
}

BYTE* Image_win8::getImageBuffer() const{

	IWICImagingFactory *pFactory = NULL;
	IWICBitmap *pBitmap = NULL;
	BYTE *pv = NULL;

	UINT uiWidth = getWidth();
	UINT uiHeight = getHeight();
	WICPixelFormatGUID formatGUID = GUID_WICPixelFormat32bppRGBA;

	WICRect rcLock = { 0, 0, uiWidth, uiHeight };
	IWICBitmapLock *pLock = NULL;

	HRESULT hr = CoCreateInstance(
		CLSID_WICImagingFactory,
		NULL,
		CLSCTX_INPROC_SERVER,
		IID_IWICImagingFactory,
		(LPVOID*)&pFactory
		);

	if (SUCCEEDED(hr))
	{
		hr = pFactory->CreateBitmap(uiWidth, uiHeight, formatGUID, WICBitmapCacheOnDemand, &pBitmap);
	}
	else{
		return NULL;
	}

	if (SUCCEEDED(hr))
	{
		hr = pBitmap->Lock(&rcLock, WICBitmapLockWrite, &pLock);

		if (SUCCEEDED(hr))
		{
			UINT cbBufferSize = 0;
			UINT cbStride = 0;
			//BYTE *pv = NULL;

			hr = pLock->GetStride(&cbStride);

			if (SUCCEEDED(hr))
			{
				hr = pLock->GetDataPointer(&cbBufferSize, &pv);
			}

			// Release the bitmap lock.
			pLock->Release();
		}
	}

	if (pBitmap)
	{
		pBitmap->Release();
	}

	if (pFactory)
	{
		pFactory->Release();
	}

	return pv;
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

IWICBitmap* Image_win8::imageWithData(BYTE* data, int dataLength){

	//char* dataArr = (char*)data;
	//int dataLength = strlen(dataArr);//TODO: works??

	// WIC interface pointers.
	IWICStream *pIWICStream = NULL;
	IWICBitmapDecoder *pIDecoder = NULL;
	IWICBitmapFrameDecode *pIDecoderFrame = NULL;
	IWICImagingFactory *pFactory = NULL;
	IWICBitmap *pBitmap = NULL;
	IWICBitmapEncoder *pEncoder = NULL;

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
	else{
		return NULL;
	}

	// Initialize the stream with the memory pointer and size.
	if (SUCCEEDED(hr)){
		hr = pIWICStream->InitializeFromMemory(
			data,
			dataLength);
	}
	else{
		return NULL;
	}
	
	// Create a decoder for the stream.
	if (SUCCEEDED(hr)){
		hr = pFactory->CreateDecoderFromStream(
			pIWICStream,                   // The stream to use to create the decoder
			NULL,                          // Do not prefer a particular vendor
			WICDecodeMetadataCacheOnLoad,  // Cache metadata when needed
			&pIDecoder);                   // Pointer to the decoder
	}
	else{
		return NULL;
	}

	//// Create a encoder for the stream.
	//if (SUCCEEDED(hr)){
	//	hr = pFactory->CreateEncoder(
	//		GUID_ContainerFormatBmp,
	//		0, // vendor
	//		&pEncoder);
	//}
	//else{
	//	return NULL;
	//}

	//pEncoder->Initialize(
	//	pIWICStream,
	//	WICBitmapEncoderNoCache);

	UINT bpp = 0;
	WICPixelFormatGUID pPixelFormat;

	if (SUCCEEDED(hr)){
		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);

		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
		if (SUCCEEDED(hr)){
			bpp = getbppFromPixelFormat(pPixelFormat);
			ILogger::instance()->logInfo("PIXEL FORMAT OK: \"%#x, %#x, %#x \"\n", pPixelFormat.Data1, pPixelFormat.Data2, pPixelFormat.Data3);
		}
		else{
			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
		}
	}
	else{
		return NULL;
	}

	UINT width = 0;
	UINT height = 0;
	if (SUCCEEDED(hr)){
		pIDecoderFrame->GetSize(&width, &height);
	}
	else{
		return NULL;
	}

	//UINT stride = width * 4;
	UINT stride = getStride(width, bpp);
	UINT bufferSize = stride * height;

	//hr = pFactory->CreateBitmapFromMemory(width, height, GUID_WICPixelFormat32bppRGBA, stride, bufferSize, data, &pBitmap);
	hr = pFactory->CreateBitmapFromMemory(width, height, pPixelFormat, stride, bufferSize, data, &pBitmap);

	if (SUCCEEDED(hr)){
		return pBitmap;//TODO: faltan los releases
	}

	return NULL;
}

int Image_win8::getBufferSize() const{

	//return this->getWidth() * this->getHeight() * 4;
	return  getStride(this->getWidth(),24) * this->getHeight(); //TODO:
}


//-- only for testing, remove later -----------------------------------

Platform::String^ toStringHat(std::string str)
{
	std::wstring wid_str = std::wstring(str.begin(), str.end());
	const wchar_t* w_char = wid_str.c_str();
	Platform::String^ p_string = ref new Platform::String(w_char);
	return p_string;
}

Image_win8* Image_win8::imageFromFile(std::string name){

	// WIC interface pointers.
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

	Windows::Storage::StorageFolder^ localFolder = Windows::Storage::ApplicationData::Current->LocalFolder;
	Platform::String^ folderPath = localFolder->Path;
	Platform::String^ tmpPath = Platform::String::Concat(folderPath, "\\");
	Platform::String^ path = Platform::String::Concat(tmpPath, toStringHat(name));

	hr = pFactory->CreateDecoderFromFilename(
		path->Data(),
		nullptr,
		GENERIC_READ,
		WICDecodeMetadataCacheOnDemand,
		&pIDecoder);
	

	if (SUCCEEDED(hr)){
		hr = pIDecoder->GetFrame(0, &pIDecoderFrame);
		//IWICBitmapDecoderInfo* pDecoderInfo = NULL;
		//hr = pIDecoder->GetDecoderInfo(&pDecoderInfo);
		//pDecoderInfo->GetFriendlyName();
		//GUID containerFormat;
		//hr = pIDecoder->GetContainerFormat(&containerFormat);
		//
		//if (SUCCEEDED(hr)){
		//	ILogger::instance()->logInfo("DECODER INFO OK ");
		//	//pDecoderInfo->g
		//	//ILogger::instance()->logInfo("DECODER INFO: \"%l, %h, %h, %hh \"\n", pDecoderInfo->GetComponentType->Data1, pDecoderInfo->GetComponentType->Data2, pDecoderInfo->GetComponentType->Data3, pDecoderInfo->GetComponentType->Data4);
		//	ILogger::instance()->logInfo("DECODER INFO: \"%#x, %#x, %#x \"\n", containerFormat.Data1, containerFormat.Data2, containerFormat.Data3);
		//}
		//else{
		//	ILogger::instance()->logInfo("DECODER INFO UNKNOWN");
		//}
	}
	else{
		return NULL;
	}

	UINT bpp = 0;
	WICPixelFormatGUID pPixelFormat;
	UINT width = 0;
	UINT height = 0;
	if (SUCCEEDED(hr)){
		pIDecoderFrame->GetSize(&width, &height);
		
		hr = pIDecoderFrame->GetPixelFormat(&pPixelFormat);
		if (SUCCEEDED(hr)){
			bpp = getbppFromPixelFormat(pPixelFormat);
			ILogger::instance()->logInfo("PIXEL FORMAT OK: \"%#x, %#x, %#x \"\n", pPixelFormat.Data1, pPixelFormat.Data2, pPixelFormat.Data3);
		}
		else{
			ILogger::instance()->logInfo("PIXEL FORMAT UNKNOWN");
		}
	}
	else{
		return NULL;
	}

	//UINT stride = width * 4;
	UINT stride = getStride(width, bpp);
	UINT bufferSize = stride * height;
	
	BYTE* imageData = (BYTE*)malloc(bufferSize);

	/*WICRect prc;
	prc.X = 0;
	prc.Y = 0;
	prc.Width = width;
	prc.Height = height;
	hr = pIDecoderFrame->CopyPixels(&prc, stride, bufferSize, imageData);*/

	hr = pIDecoderFrame->CopyPixels(0, // entire bitmap
									stride, //
									bufferSize, //
									imageData);

	if (SUCCEEDED(hr)){
		//hr = pFactory->CreateBitmapFromMemory(width, height, GUID_WICPixelFormat32bppRGBA, stride, bufferSize, imageData, &pBitmap);
		hr = pFactory->CreateBitmapFromMemory(width, height, pPixelFormat, stride, bufferSize, imageData, &pBitmap);

		if (SUCCEEDED(hr)){
			return new Image_win8(pBitmap, imageData);
		}
		return NULL;
	}

	return NULL;
}