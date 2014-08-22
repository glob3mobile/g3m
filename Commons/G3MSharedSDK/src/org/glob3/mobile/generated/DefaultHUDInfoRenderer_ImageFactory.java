package org.glob3.mobile.generated; 
//
//  DefaultInfoDisplay.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 11/08/14.
//
//

//
//  DefaultInfoDisplay.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 11/08/14.
//
//




public class DefaultHUDInfoRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private java.util.ArrayList<String> _infos = new java.util.ArrayList<String>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
    ICanvasUtils.drawStringsOn(_infos, canvas, width, height, HorizontalAlignment.Left, VerticalAlignment.Bottom, HorizontalAlignment.Left, Color.white(), 11, 2, Color.transparent(), Color.black(), 5);
  }

  protected final boolean isEquals(java.util.ArrayList<String> v1, java.util.ArrayList<String> v2)
  {
    final int size1 = v1.size();
    final int size2 = v2.size();
    if (size1 != size2)
    {
      return false;
    }
  
    for (int i = 0; i < size1; i++)
    {
      final String str1 = v1.get(i);
      final String str2 = v2.get(i);
      if (!str1.equals(str2))
      {
        return false;
      }
    }
    return true;
  }

  public void dispose()
  {
  }

  public final boolean setInfos(java.util.ArrayList<String> infos)
  {
    if (isEquals(_infos, infos))
    {
      return false;
    }
  
    _infos.clear();
    _infos.addAll(infos);
  
    return true;
  }
}