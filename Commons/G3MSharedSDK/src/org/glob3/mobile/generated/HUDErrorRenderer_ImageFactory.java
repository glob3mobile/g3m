package org.glob3.mobile.generated;import java.util.*;

public class HUDErrorRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
	canvas.setFillColor(Color.black());
	canvas.fillRectangle(0, 0, width, height);
	ICanvasUtils.drawStringsOn(_errors, canvas, width, height, HorizontalAlignment.Center, VerticalAlignment.Middle, HorizontalAlignment.Center, Color.white(), 40, 5, Color.fromRGBA(0.9f, 0.4f, 0.4f, 1.0f), Color.transparent(), 16);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const java.util.ArrayList<String>& v1, const java.util.ArrayList<String>& v2) const
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

  public final boolean setErrors(java.util.ArrayList<String> errors)
  {
	if (isEquals(_errors, errors))
	{
	  return false;
	}
  
	_errors.clear();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'insert' method in Java:
	_errors.insert(_errors.end(), errors.iterator(), errors.end());
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_errors.addAll(errors);
//#endif
	return true;
  }
}
