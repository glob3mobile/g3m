package org.glob3.mobile.generated; 
public abstract class IDeviceInfo
{

  public void dispose()
  {

  }

  public final float getPixelsInMM(float millimeters)
  {
    return getDPI() / 25.4f * millimeters;
  }

  public abstract float getDPI();

  public abstract DeviceInfo_Platform getPlatform();

  public abstract float getQualityFactor();

}