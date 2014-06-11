package org.glob3.mobile.generated; 
public class ICanvasUtils
{
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, padding, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color)
  {
     return drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, 18, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static Vector2F drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding, int cornerRadius)
  {
  
    final int stringsSize = strings.size();
    int fontSize = maxFontSize;
    if (stringsSize > 0)
    {
      int longestTextIndex = 0;
      int maxLength = strings.get(longestTextIndex).length();
      for (int i = 1; i < stringsSize; i++)
      {
        final int itemLength = strings.get(i).length();
        if (maxLength < itemLength)
        {
          maxLength = itemLength;
          longestTextIndex = i;
        }
      }
  
  
  
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
    }
    canvas.setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
  
    ColumnCanvasElement column = new ColumnCanvasElement(backgroundColor, 0, padding, cornerRadius, textAlign); // margin
    final GFont labelFont = GFont.sansSerif(fontSize);
    for (int i = 0; i < stringsSize; i++)
    {
      column.add(new TextCanvasElement(strings.get(i), labelFont, color));
    }
  
    final Vector2F extent = column.getExtent(canvas);
    final Vector2F position = getPosition(extent, width, height, hAlign, vAlign);
    column.drawAt(position._x, position._y, canvas);
  
    return extent;
  }

  public static Vector2F getPosition(Vector2F extent, int canvasWidth, int canvasHeight, HorizontalAlignment hAlign, VerticalAlignment vAlign)
  {
    float left;
    float top;
    switch (hAlign)
    {
      case Left:
        left = 0F;
        break;
      case Right:
        left = canvasWidth - extent._x;
        break;
      case Center:
      default:
        left = (canvasWidth / 2) - (extent._x / 2);
    }
    switch (vAlign)
    {
      case Top:
        top = 0F;
        break;
      case Bottom:
        top = canvasHeight - extent._y;
        break;
      case Middle:
      default:
        top = (canvasHeight / 2) - (extent._y / 2);
    }
  
    return new Vector2F(left, top);
  }
}