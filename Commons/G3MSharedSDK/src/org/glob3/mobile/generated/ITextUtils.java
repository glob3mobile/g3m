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


//class IImage;
//class Color;
//class IImageListener;


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


  public void createLabelImage(String label, IImageListener listener, boolean autodelete)
  {
    final float fontSize = 20F;
  
    final Color color = Color.white();
    final Color shadowColor = Color.black();
  
    createLabelImage(label, fontSize, color, shadowColor, listener, autodelete);
  }

  public abstract void createLabelImage(String label, float fontSize, Color color, Color shadowColor, IImageListener listener, boolean autodelete);


  public void labelImage(IImage image, String label, LabelPosition labelPosition, IImageListener listener, boolean autodelete)
  {
    final float fontSize = 20F;
  
    final Color color = Color.white();
    final Color shadowColor = Color.black();
  
    final int separation = 2;
  
    labelImage(image, label, labelPosition, separation, fontSize, color, shadowColor, listener, autodelete);
  }

  public abstract void labelImage(IImage image, String label, LabelPosition labelPosition, int separation, float fontSize, Color color, Color shadowColor, IImageListener listener, boolean autodelete);

}