package org.glob3.mobile.generated;
//
//  DefaultInfoDisplay.cpp
//  G3M
//
//  Created by Vidal Toboso on 11/08/14.
//
//

//
//  DefaultInfoDisplay.hpp
//  G3M
//
//  Created by Vidal Toboso on 11/08/14.
//
//




public class DefaultHUDInfoRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
    java.util.ArrayList<String> strings = new java.util.ArrayList<String>();
  
    final int size = _info.size();
    for (int i = 0; i < size; i++)
    {
      strings.add(_info.get(i).getText());
    }
    ICanvasUtils.drawStringsOn(strings, canvas, width, height, HorizontalAlignment.Left, VerticalAlignment.Bottom, HorizontalAlignment.Left, Color.WHITE, 16, 10, Color.TRANSPARENT, Color.BLACK, 5);
  }

  protected final boolean isEquals(java.util.ArrayList<Info> v1, java.util.ArrayList<Info> v2)
  {
    final int size1 = v1.size();
    final int size2 = v2.size();
    if (size1 != size2)
    {
      return false;
    }
  
    for (int i = 0; i < size1; i++)
    {
      final Info str1 = v1.get(i);
      final Info str2 = v2.get(i);
      if (str1 != str2)
      {
        return false;
      }
    }
    return true;
  }

  public void dispose()
  {
  }

  public final boolean setInfo(java.util.ArrayList<Info> info)
  {
    _info.clear();
    _info.addAll(info);
  
    return true;
  }
}
