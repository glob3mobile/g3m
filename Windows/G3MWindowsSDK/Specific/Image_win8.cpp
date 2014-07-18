#include "Image_win8.hpp"
#include <wrl.h>
#include <robuffer.h>
#include <exception>

using namespace Windows::Storage::Streams;
using namespace Microsoft::WRL;
//using namespace System::Drawing::Image;


Image_win8::Image_win8(BitmapImage^ image, unsigned char* sourceBuffer)
{
	_image = image;
	_sourceBuffer = sourceBuffer;
	//_image->SetSource();
}


Image_win8::~Image_win8()
{
	_image = nullptr;
	_sourceBuffer = nullptr;
}


unsigned char* getPointerToPixelData(IBuffer^ buffer){

	ComPtr<IInspectable> insp(reinterpret_cast<IInspectable*>(buffer));

	// Query the IBufferByteAccess interface.
	ComPtr<IBufferByteAccess> bufferByteAccess;
	ThrowIfFailed(insp.As(&bufferByteAccess));

	// Retrieve the buffer data.
	byte* pixels = nullptr;
	ThrowIfFailed(bufferByteAccess->Buffer(&pixels));
	return pixels;
}

inline void ThrowIfFailed(HRESULT hr)
{
	if (FAILED(hr))
	{
		throw Platform::Exception::CreateException(hr);
	}
}

unsigned char* Image_win8::getSourceBuffer() const {
	return _sourceBuffer;
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