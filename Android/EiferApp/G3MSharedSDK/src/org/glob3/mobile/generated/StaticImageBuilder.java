package org.glob3.mobile.generated; 
//
//  StaticImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

//
//  StaticImageBuilder.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//


//class IImage;

public class StaticImageBuilder extends AbstractImageBuilder
{
  private final IImage _image;
  private final String _imageName;

  public StaticImageBuilder(IImage image, String imageName)
  {
     _image = image;
     _imageName = imageName;

  }

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
  
    super.dispose();
  }

  public final boolean isMutable()
  {
    return false;
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
    listener.imageCreated(_image.shallowCopy(), _imageName);
  
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

}