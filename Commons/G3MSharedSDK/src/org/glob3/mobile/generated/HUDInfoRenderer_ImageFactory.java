package org.glob3.mobile.generated; 
public class HUDInfoRenderer_ImageFactory extends HUDImageRenderer.CanvasImageFactory
{
  private java.util.ArrayList<String> _infos = new java.util.ArrayList<String>();


  protected final void drawOn(ICanvas canvas, int width, int height)
  {
    int longestTextIndex = 0;
    int maxLength = _infos.get(longestTextIndex).length();
    final int infosSize = _infos.size();
    for (int i = 1; i < infosSize; i++)
    {
      final int itemLength = _infos.get(i).length();
      if (maxLength < itemLength)
      {
        maxLength = itemLength;
        longestTextIndex = i;
      }
    }
  
    int fontSize = 11;
    int textHeight = 0;
    final int padding = 2;
    final int maxWidth = width - (2 * padding);
    boolean fit = false;
    while (!fit && fontSize > 2)
    {
      GFont labelFont = GFont.sansSerif(fontSize);
      final String longestText = _infos.get(longestTextIndex);
      canvas.setFont(labelFont);
      final Vector2F extent = canvas.textExtent(longestText);
      if (extent._x <= maxWidth)
      {
        fit = true;
        textHeight = (int) extent._y;
      }
      else
      {
        fontSize--;
      }
    }
  
    canvas.setFillColor(Color.white());
    canvas.setShadow(Color.black(), 1.0, 1.0, -1.0);
    int cursor = textHeight + padding;
    for (int i = 0; i < infosSize; i++)
    {
      canvas.fillText(_infos.get(i), 2, height - cursor);
      cursor += textHeight;
    }
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