#pragma once
#include "IImage.hpp"

using namespace Windows::UI::Xaml::Media::Imaging;
using namespace Windows::Storage::Streams;


class Image_win8 : public IImage {

private:
	//Windows::UI::Xaml::Media::Imaging::WriteableBitmap^ _image;
	//mutable Windows::Storage::Streams::IBuffer^ _sourceBuffer;
	Windows::UI::Xaml::Media::Imaging::BitmapImage^ _image;
	//mutable Windows::Storage::Streams::IBuffer^ _sourceBuffer;
	unsigned char* _sourceBuffer;

	Image_win8(const Image_win8& that);

public:
	//Image_win8();
	~Image_win8();

	Image_win8(BitmapImage^ image, unsigned char* sourceBuffer);

	int getWidth() const {
		//return (_image == NULL) ? 0 : (int)_image.size.width;
		return (_image == nullptr) ? 0 : (int)_image->PixelWidth;
	}

	int getHeight() const {
		//return (_image == NULL) ? 0 : (int)_image.size.height;
		return (_image == nullptr) ? 0 : (int)_image->PixelHeight;
	}

	const Vector2I getExtent() const {
		return Vector2I(getWidth(), getHeight());
	}

	unsigned char* getSourceBuffer() const;

	const std::string description() const;

	IImage* shallowCopy() const;
};

