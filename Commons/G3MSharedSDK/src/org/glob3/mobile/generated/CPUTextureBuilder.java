package org.glob3.mobile.generated; 
//
//  CPUTextureBuilder.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CPUTextureBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 23/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CPUTextureBuilder extends TextureBuilder
{
  public final void createTextureFromImages(Vector2I textureExtent, java.util.ArrayList<IImage> images, java.util.ArrayList<RectangleF> srcRectangles, java.util.ArrayList<RectangleF> destRectangles, IImageListener listener, boolean autodelete)
  {
    IImageUtils.combine(textureExtent, images, srcRectangles, destRectangles, listener, autodelete);
  }

}