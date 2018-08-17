package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImageBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GLState;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geometry2DGLFeature;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ViewportExtentGLFeature;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TexturesHandler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class NonOverlappingMark;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class SimpleTextureMapping;

public abstract class NonOverlappingMarkTouchListener
{
  public void dispose()
  {
  }

  public abstract boolean touchedMark(NonOverlappingMark mark, Vector2F touchedPixel);
}
