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




DeviceInfo_win8::DeviceInfo_win8(){


	ID2D1Factory* pD2DFactory = NULL;
	HRESULT hr = D2D1CreateFactory(
		D2D1_FACTORY_TYPE_SINGLE_THREADED,
		&pD2DFactory
		);

	FLOAT dpiX, dpiY;
	pD2DFactory->GetDesktopDpi(&dpiX, &dpiY);

	_dpi = (dpiX / dpiY) / 2.0f;

}