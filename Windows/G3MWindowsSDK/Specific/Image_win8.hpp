#pragma once
#ifndef __G3MWindowsSDK_Image_win8__
#define __G3MWindowsSDK_Image_win8__

#include "IImage.hpp"
#include "IFactory.hpp"
#include "ByteBuffer_win8.hpp"
#include <Wincodec.h>

//using namespace Windows::UI::Xaml::Media::Imaging;
//using namespace Windows::Storage::Streams;



class Image_win8 : public IImage {

private:
	
	IWICBitmap* _image;
	//mutable BYTE* _sourceBuffer;
	mutable IByteBuffer* _sourceBuffer;

	Image_win8(const Image_win8 &that);

public:
	//Image_win8();
	~Image_win8();

	Image_win8(IWICBitmap* image, IByteBuffer* sourceBuffer);

	int getWidth() const;

	int getHeight() const;

	const Vector2I getExtent() const;

	IWICBitmap* getBitmap() const;

	IByteBuffer* getSourceBuffer() const;

	void releaseSourceBuffer() const;

	const std::string description() const;

	IImage* shallowCopy() const;

	IByteBuffer* createImageBuffer() const;

	//static IWICBitmap* imageWithData(BYTE* data, int dataLength);
	static IWICBitmap* imageWithData(IByteBuffer* imgData);

	int getBufferSize() const;

	//-- only for testing, remove later -----------------------------------
	static bool exportToFile(Image_win8* image);
	static Image_win8* imageFromFile(std::string path);

};

#endif