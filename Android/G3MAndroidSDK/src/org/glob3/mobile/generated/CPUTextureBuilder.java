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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createTextureFromImages(GL * gl, const java.util.ArrayList<const IImage*>& images, int width, int height) const
  public final int createTextureFromImages(GL gl, java.util.ArrayList<IImage> images, int width, int height)
  {
	final int imagesSize = images.size();
  
	if (imagesSize == 0)
	{
	  return -1;
	}
  
	IImage im = images.get(0);
	IImage im2 = null;
	for (int i = 1; i < imagesSize; i++)
	{
	  IImage imTrans = images.get(i);
	  im2 = im.combineWith(imTrans, width, height);
	  if (i > 1)
	  {
		if (im != null)
			im.dispose();
	  }
	  im = im2;
	}
  
	int texID = gl.uploadTexture(im, width, height);
  
	if (imagesSize > 1)
	{
	  if (im != null)
		  im.dispose();
	}
  
	return texID;
  
  //  const int imagesSize = images.size();
  //
  //  if (imagesSize > 0) {
  //    const IImage* im = images[0], *im2 = NULL;
  //    for (int i = 1; i < imagesSize; i++) {
  //      const IImage* imTrans = images[i];
  //      im2 = im->combineWith(*imTrans, width, height);
  //      if (i > 1) {
  //        delete im;
  //      }
  //      im = im2;
  //    }
  //
  //    int texID = gl->uploadTexture(im, width, height);
  //
  //    if (imagesSize > 1){
  //      delete im;
  //    }
  //    return texID;
  //  }
  //  else {
  //    return -1;
  //  }
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int createTextureFromImages(GL * gl, const IFactory* factory, const java.util.ArrayList<const IImage*>& vImages, const java.util.ArrayList<const Rectangle*>& vRectangles, int width, int height) const
  public final int createTextureFromImages(GL gl, IFactory factory, java.util.ArrayList<IImage> vImages, java.util.ArrayList<Rectangle> vRectangles, int width, int height)
  {
	IImage base;
	int i = 0; //First image to merge
	Rectangle baseRec = new Rectangle(0,0, width, height);
	if (vRectangles.get(0).equalTo(baseRec))
	{
	  base = vImages.get(0);
	  i = 1;
	}
	else
	{
	  base = factory.createImageFromSize(width, height);
	}
  
	for (; i < vImages.size(); i++)
	{
  
	  IImage image = vImages.get(i);
	  final Rectangle rect = vRectangles.get(i);
  
	  IImage im2 = base.combineWith(image, rect, width, height);
  
	  if (base != vImages.get(0))
	  {
		if (base != null)
			base.dispose();
	  }
	  base = im2;
	}
  
	int texID = gl.uploadTexture(base, width, height);
  
	if (base != vImages.get(0))
	{
	  if (base != null)
		  base.dispose();
	}
  
	return texID;
  }

}