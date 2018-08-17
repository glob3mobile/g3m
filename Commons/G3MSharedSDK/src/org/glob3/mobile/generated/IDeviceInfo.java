package org.glob3.mobile.generated;import java.util.*;

public abstract class IDeviceInfo
{

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getPixelsInMM(float millimeters) const
  public final float getPixelsInMM(float millimeters)
  {
	return getDPI() / 25.4f * millimeters;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float getDPI() const = 0;
  public abstract float getDPI();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual DeviceInfo_Platform getPlatform() const = 0;
  public abstract DeviceInfo_Platform getPlatform();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float getDevicePixelRatio() const = 0;
  public abstract float getDevicePixelRatio();

}
