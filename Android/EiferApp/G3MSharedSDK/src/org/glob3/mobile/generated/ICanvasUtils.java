package org.glob3.mobile.generated; 
public class ICanvasUtils
{
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, padding, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, shadowColor, 16, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, backgroundColor, Color.black(), 16, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, minFontSize, Color.transparent(), Color.black(), 16, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, maxFontSize, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color)
  {
     drawStringsOn(strings, canvas, width, height, hAlign, vAlign, textAlign, color, 18, 2, Color.transparent(), Color.black(), 16, 8);
  }
  public static void drawStringsOn(java.util.ArrayList<String> strings, ICanvas canvas, int width, int height, HorizontalAlignment hAlign, VerticalAlignment vAlign, HorizontalAlignment textAlign, Color color, int maxFontSize, int minFontSize, Color backgroundColor, Color shadowColor, int padding, int cornerRadius)
  {
  
    if (strings.isEmpty())
    {
      return;
    }
  
    final int maxWidth = width - (2 * padding);
    final int stringsSize = strings.size();
  
    int fontSize = maxFontSize;
    boolean allFit = false;
    while (!allFit && (fontSize > minFontSize))
    {
      allFit = true;
      canvas.setFont(GFont.sansSerif(fontSize));
      for (int i = 0; i < stringsSize; i++)
      {
        final Vector2F extent = canvas.textExtent(strings.get(i));
        if (extent._x > maxWidth)
        {
          allFit = false;
          fontSize--;
          continue;
        }
      }
    }
  //  canvas->setShadow(shadowColor, 1.0f, 1.0f, -1.0f);
    canvas.setShadow(shadowColor, 1.0f, 0.0f, 0.0f);
  
    ColumnCanvasElement column = new ColumnCanvasElement(backgroundColor, 0, padding, cornerRadius, textAlign); // margin
    final GFont labelFont = GFont.sansSerif(fontSize);
    for (int i = 0; i < stringsSize; i++)
    {
      column.add(new TextCanvasElement(strings.get(i), labelFont, color));
    }
  
    final Vector2F extent = column.getExtent(canvas);
    final Vector2F position = getPosition(extent, width, height, hAlign, vAlign);
    column.drawAt(position._x, position._y, canvas);
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