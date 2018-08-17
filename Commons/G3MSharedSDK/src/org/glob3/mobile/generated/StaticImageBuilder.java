package org.glob3.mobile.generated;import java.util.*;

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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMutable() const
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
