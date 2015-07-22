package org.glob3.mobile.generated; 
//
//  NonOverlappingMarksRenderer.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//

//
//  NonOverlappingMarksRenderer.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/1/15.
//
//



//class IImageBuilder;
//class Geodetic3D;
//class Vector2D;
//class Camera;
//class Planet;
//class GLState;
//class IImage;
//class TextureIDReference;
//class Geometry2DGLFeature;
//class ViewportExtentGLFeature;
//class TexturesHandler;
//class NonOverlappingMark;
//class SimpleTextureMapping;

public abstract class NonOverlappingMarkTouchListener
{
  public void dispose()
  {
  }

  public abstract boolean touchedMark(NonOverlappingMark mark, Vector2F touchedPixel);
}