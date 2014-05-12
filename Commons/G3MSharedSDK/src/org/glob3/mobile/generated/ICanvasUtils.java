package org.glob3.mobile.generated; 
public class ICanvasUtils
{
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, padding, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, maxFontSize, minFontSize, backgroundColor, Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize, int minFontSize)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, maxFontSize, minFontSize, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, maxFontSize, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color)
  {
     return drawStringsOn(strings, canvas, width, height, vAlign, hAlign, color, 18, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, VerticalAlignment vAlign, HorizontalAlignment hAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding, int cornerRadius)
  {
    int longestTextIndex = 0;
    int maxLength = strings.get(longestTextIndex).length();
    final int stringsSize = strings.size();
    for (int i = 1; i < stringsSize; i++)
    {
      final int itemLength = strings.get(i).length();
      if (maxLength < itemLength)
      {
        maxLength = itemLength;
        longestTextIndex = i;
      }
    }
  
    int fontSize = maxFontSize;
    final int maxWidth = width - (2 * padding);
    boolean fit = false;
    while (!fit && fontSize > minFontSize)
    {
      GFont labelFont = GFont.sansSerif(fontSize);
      final String longestText = strings.get(longestTextIndex);
      canvas.setFont(labelFont);
      final Vector2F extent = canvas.textExtent(longestText);
      if (extent._x <= maxWidth)
      {
        fit = true;
      }
      else
      {
        fontSize--;
      }
    }
  
    canvas.setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
  
    ColumnCanvasElement column = new ColumnCanvasElement(backgroundColor, 0, padding, cornerRadius); // margin
    final GFont labelFont = GFont.sansSerif(fontSize);
    for (int i = 0; i < stringsSize; i++)
    {
      column.add(new TextCanvasElement(strings.get(i), labelFont, color));
    }
  
    float top;
    float left;
    final Vector2F extent = column.getExtent(canvas);
    switch (vAlign)
    {
      case Top:
        top = 0F;
        break;
      case Middle:
        top = (height / 2) - (extent._y / 2);
        break;
      case Bottom:
        top = height - extent._y;
        break;
    }
    switch (hAlign)
    {
      case Left:
        left = 0F;
        break;
      case Center:
        left = (width / 2) - (extent._x / 2);
        break;
      case Right:
        left = width - extent._x;
        break;
    }
  
    column.drawAt(left, top, canvas);
  
    return extent;
  }
}