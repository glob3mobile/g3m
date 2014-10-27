//
//  DeviceInfo_win8.cpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 02/06/14.
//
//

#include "DeviceInfo_win8.hpp"

#include <d2d1.h>
#include <string>




DeviceInfo_win8::DeviceInfo_win8(NativeGL_win8* ngl){

	_ngl = ngl;

	FLOAT dpiX, dpiY;
	_ngl->getD2DFactory()->GetDesktopDpi(&dpiX, &dpiY);

	_dpi = (dpiX / dpiY) / 2.0f;

}