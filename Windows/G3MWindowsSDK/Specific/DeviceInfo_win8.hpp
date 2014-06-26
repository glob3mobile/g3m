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

class DeviceInfo_win8 :public IDeviceInfo{
private:
	float _dpi;

public:
	DeviceInfo_win8();

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