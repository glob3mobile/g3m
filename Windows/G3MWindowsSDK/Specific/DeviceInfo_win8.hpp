//
//  DeviceInfo_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 02/06/14.
//
//

#ifndef __G3MWindowsSDK_DeviceInfo_win8__
#define __G3MWindowsSDK_DeviceInfo_win8__

#include "IDeviceInfo.hpp"
#include "NativeGL_win8.hpp"

class DeviceInfo_win8 :public IDeviceInfo{
private:
	float _dpi;
	NativeGL_win8* _ngl;

public:
	DeviceInfo_win8(NativeGL_win8* ngl);

	float getDPI() const {
		return _dpi;
	}

	DeviceInfo_Platform getPlatform() const {
		return DEVICE_win8;
	}

	float getQualityFactor() const {
		return 1;
	}



};


#endif