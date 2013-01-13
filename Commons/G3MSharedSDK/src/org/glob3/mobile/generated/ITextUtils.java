package org.glob3.mobile.generated; 
//
//  ITextUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//

//
//  ITextUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/10/13.
//
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;

public abstract class ITextUtils
{
  private static ITextUtils _instance = null;

  public static void setInstance(ITextUtils factory)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("ITextUtils instance already set!");
	  if (_instance != null)
		  _instance.dispose();
	}
	_instance = factory;
  }

  public static ITextUtils instance()
  {
	return _instance;
  }

  public void dispose()
  {

  }

  public IImage createLabelBitmap(String label)
  {
	final float fontSize = 20F;
  
	final Color color = Color.yellow();
	final Color shadowColor = Color.black();
  
	return createLabelBitmap(label, fontSize, color, shadowColor);
  }

  public abstract IImage createLabelBitmap(String label, float fontSize, Color color, Color shadowColor);

}