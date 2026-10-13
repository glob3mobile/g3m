package org.glob3.mobile.generated;
//
//  StaticImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//

//
//  StaticImageFactory.h
//  G3M
//
//  Created by Diego Gomez Deck on 2/13/15.
//
//


//class IImage;

public class StaticImageFactory extends AbstractImageFactory
{
  private final IImage _image;
  private final String _imageName;

  public void dispose()
  {
    if (_image != null)
       _image.dispose();
  
    super.dispose();
  }

  public StaticImageFactory(IImage image, String imageName)
  {
     _image = image;
     _imageName = imageName;

  }


  public final boolean isMutable()
  {
    return false;
  }

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    listener.imageCreated(_image.shallowCopy(), _imageName);
  
    if (deleteListener)
    {
      if (listener != null)
         listener.dispose();
    }
  }

}