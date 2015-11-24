package org.glob3.mobile.generated; 
public class HUDErrorRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
    canvas.setFillColor(Color.black());
    canvas.fillRectangle(0, 0, width, height);
    ICanvasUtils.drawStringsOn(_errors, canvas, width, height, HorizontalAlignment.Center, VerticalAlignment.Middle, HorizontalAlignment.Center, Color.white(), 20, 5, Color.fromRGBA(0.9f, 0.4f, 0.4f, 1.0f), Color.transparent(), 16);
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

  public final boolean setErrors(java.util.ArrayList<String> errors)
  {
    if (isEquals(_errors, errors))
    {
      return false;
    }
  
    _errors.clear();
    _errors.addAll(errors);
    return true;
  }
}