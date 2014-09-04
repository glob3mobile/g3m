#pragma once
#ifndef __G3MWindowsSDK_Image_win8__
#define __G3MWindowsSDK_Image_win8__

#include "IImage.hpp"
#include <Wincodec.h>

//using namespace Windows::UI::Xaml::Media::Imaging;
//using namespace Windows::Storage::Streams;



class Image_win8 : public IImage {

private:
	
	//Windows::UI::Xaml::Media::Imaging::BitmapImage^ _image;
	IWICBitmap* _image;
	mutable BYTE* _sourceBuffer;

	Image_win8(const Image_win8& that);

public:
	//Image_win8();
	~Image_win8();

	Image_win8(IWICBitmap* image, unsigned char* sourceBuffer);

	int getWidth() const;

	int getHeight() const;

	const Vector2I getExtent() const;

	IWICBitmap* getBitmap() const;

	BYTE* getSourceBuffer() const;

	void releaseSourceBuffer() const;

	const std::string description() const;

	IImage* shallowCopy() const;

	BYTE* getImageBuffer() const;

	static IWICBitmap* imageWithData(BYTE* data, int dataLength);

	int getBufferSize() const;

	//-- only for testing, remove later -----------------------------------
	static Image_win8* imageFromFile(std::string path);

};

#endif