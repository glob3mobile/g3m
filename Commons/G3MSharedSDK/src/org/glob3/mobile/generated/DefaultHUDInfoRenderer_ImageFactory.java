package org.glob3.mobile.generated;//
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
  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
	java.util.ArrayList<String> strings = new java.util.ArrayList<String>();
  
	final int size = _info.size();
	for (int i = 0; i < size; i++)
	{
	  strings.add(_info.get(i).getText());
	}
	ICanvasUtils.drawStringsOn(strings, canvas, width, height, HorizontalAlignment.Left, VerticalAlignment.Bottom, HorizontalAlignment.Left, Color.white(), 16, 10, Color.transparent(), Color.black(), 5);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const java.util.ArrayList<const Info*>& v1, const java.util.ArrayList<const Info*>& v2) const
  protected final boolean isEquals(java.util.ArrayList<const Info> v1, java.util.ArrayList<const Info> v2)
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

  public final boolean setInfo(java.util.ArrayList<const Info> info)
  {
	_info.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_info.insert(_info.end(), info.iterator(), info.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_info.addAll(info);
//#endif
  
	return true;
  }
}
