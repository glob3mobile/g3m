package org.glob3.mobile.generated; 
//
//  IImage.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//



//class MutableColor255;


public abstract class IImage
{
  public void dispose()
  {
  }

  public abstract int getWidth();
  public abstract int getHeight();
  public abstract Vector2I getExtent();

  public abstract String description();
  @Override
  public String toString() {
    return description();
  }

  public abstract boolean isPremultiplied();

  public abstract IImage shallowCopy();

  public abstract void getPixel(int x, int y, MutableColor255 pixel);
}