//
//  Factory_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 02/06/14.
//  
//

#ifndef __G3MWindowsSDK_Factory_win8__
#define __G3MWindowsSDK_Factory_win8__

#include "IFactory.hpp"

#include "DeviceInfo_win8.hpp"
#include "ByteBuffer_win8.hpp"
#include "ShortBuffer_win8.hpp"
#include "IntBuffer_win8.hpp"
#include "FloatBuffer_win8.hpp"
#include "Timer_win8.hpp"
#include "Canvas_win8.hpp"


class Factory_win8 : public IFactory {
private:
	NativeGL_win8* _ngl;
protected:
	IDeviceInfo* createDeviceInfo() const{
		return new DeviceInfo_win8(_ngl);
	}

public:

	Factory_win8(NativeGL_win8* ngl):
	_ngl(ngl)
	{

	}
	ITimer* createTimer() const {
		return new Timer_win8();
	}

	ICanvas* createCanvas() const {
		return new Canvas_win8(_ngl);
	}

	IByteBuffer* createByteBuffer(int length) const{
		return new ByteBuffer_win8(length);
	}

	IByteBuffer* createByteBuffer(unsigned char data[], int length) const{
		return new ByteBuffer_win8(data, length);
	}

	IShortBuffer* createShortBuffer(int size) const{
		return new ShortBuffer_win8(size);
	}

	IIntBuffer* createIntBuffer(int size) const{
		return new IntBuffer_win8(size);
	}

	IFloatBuffer* createFloatBuffer(int size) const{
		return new FloatBuffer_win8(size);
	}
	
	IFloatBuffer* createFloatBuffer(float f0,
		float f1,
		float f2,
		float f3,
		float f4,
		float f5,
		float f6,
		float f7,
		float f8,
		float f9,
		float f10,
		float f11,
		float f12,
		float f13,
		float f14,
		float f15) const{
		return new FloatBuffer_win8(f0,f1,
			 f2,
			 f3,
			 f4,
			 f5,
			 f6,
			 f7,
			 f8,
			 f9,
			 f10,
			 f11,
			 f12,
			 f13,
			 f14,
			 f15);
	}

	IWebSocket* createWebSocket(const URL& url,
		IWebSocketListener* listener,
		bool autodeleteListener,
		bool autodeleteWebSocket) const{

		return NULL;
	}

};







#endif