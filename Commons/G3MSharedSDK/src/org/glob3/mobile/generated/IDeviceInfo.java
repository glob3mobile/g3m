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

  public abstract float getDevicePixelRatio();

  // in the widget's pixels: what the notch, island, rounded corners and system bars cover on each
  // edge; 0 where the platform reports nothing
  public abstract float getSafeAreaInsetTop();
  public abstract float getSafeAreaInsetRight();
  public abstract float getSafeAreaInsetBottom();
  public abstract float getSafeAreaInsetLeft();

}