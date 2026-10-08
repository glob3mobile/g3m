package org.glob3.mobile.generated;
//
//  FittedLabelImageFactory.cpp
//  G3M
//

//
//  FittedLabelImageFactory.hpp
//  G3M
//





//class ICanvas;
//class GFont;


/**
 A label that fits a maximum width: when the text is too wide it is split in
 two lines at the blank that leaves them most even; when still too wide the
 font shrinks, one point at a time, down to minFontSizeFactor times its size.
 The maximum width is given in pixels or as a reference text measured with the
 style's font ("Washington, D.C.").
 */
public class FittedLabelImageFactory extends AbstractImageFactory
{
  private final String _text;
  private final LabelStyle _style;
  private final String _maxWidthText;
  private final float _maxWidth;
  private final float _minFontSizeFactor;
  private final int _lineSeparation;
  private final HorizontalAlignment _linesAlignment;

  private float maxWidth(ICanvas canvas)
  {
    if (_maxWidthText.length() == 0)
    {
      return _maxWidth;
    }
    canvas.setFont(_style.getFont());
    return widthOf(canvas, _maxWidthText);
  }
  private float minFontSize()
  {
    final float fontSize = _style.getFont().getSize();
    if ((_minFontSizeFactor < 0.1) || (_minFontSizeFactor > 0.99))
    {
      return fontSize;
    }
    return IMathUtils.instance().floor(fontSize * _minFontSizeFactor);
  }

  private static float widthOf(ICanvas canvas, String text)
  {
    return canvas.textExtent(text)._x;
  }

  private static java.util.ArrayList<String> twoLines(ICanvas canvas, String text)
  {
    final IStringUtils su = IStringUtils.instance();
    final IMathUtils mu = IMathUtils.instance();
  
    final String normalized = su.replaceAll(su.trim(text), "  ", " ");
  
    java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
  
    final java.util.ArrayList<Integer> blanks = blanksPositions(normalized);
    if (blanks.isEmpty())
    {
      lines.add(normalized);
      return lines;
    }
  
    String bestLeft = "";
    String bestRight = "";
    float bestDiff = 0F;
    for (int i = 0; i < blanks.size(); i++)
    {
      final int blank = blanks.get(i);
  
      final String left = su.trim(su.left(normalized, blank));
      final String right = su.trim(su.substring(normalized, blank + 1, normalized.length()));
  
      final float diff = mu.abs(widthOf(canvas, left) - widthOf(canvas, right));
      if ((i == 0) || (diff < bestDiff))
      {
        bestDiff = diff;
        bestLeft = left;
        bestRight = right;
      }
    }
  
    lines.add(bestLeft);
    lines.add(bestRight);
    return lines;
  }

  private static java.util.ArrayList<Integer> blanksPositions(String text)
  {
    final IStringUtils su = IStringUtils.instance();
  
    java.util.ArrayList<Integer> result = new java.util.ArrayList<Integer>();
    int position = su.indexOf(text, " ");
    while (position >= 0)
    {
      result.add(position);
      position = su.indexOf(text, " ", position + 1);
    }
    return result;
  }

  private IImageFactory createLines(java.util.ArrayList<String> lines, LabelStyle style)
  {
    if (lines.size() == 1)
    {
      return new LabelImageFactory(lines.get(0), style);
    }
  
    final LabelStyle lineStyle = style.copyWithoutBackground();
    return new ColumnLayoutImageFactory(new LabelImageFactory(lines.get(0), lineStyle), new LabelImageFactory(lines.get(1), lineStyle), style.copyBackground(), _lineSeparation, _linesAlignment);
  }

  private IImageFactory createFitted(ICanvas canvas)
  {
    final float maxWidth = this.maxWidth(canvas);
    final float minFontSize = this.minFontSize();
  
    float fontSize = _style.getFont().getSize();
    while (true)
    {
      final GFont font = _style.getFont().copyWithSize(fontSize);
      canvas.setFont(font);
  
      java.util.ArrayList<String> lines = new java.util.ArrayList<String>();
      float width = widthOf(canvas, _text);
      if (width <= maxWidth)
      {
        lines.add(_text);
      }
      else
      {
        lines = twoLines(canvas, _text);
        width = 0F;
        for (int i = 0; i < lines.size(); i++)
        {
          width = IMathUtils.instance().max(width, widthOf(canvas, lines.get(i)));
        }
      }
  
      final boolean fits = (width <= maxWidth);
      final boolean canShrink = ((fontSize - 1) >= minFontSize);
      if (fits || !canShrink)
      {
        return createLines(lines, _style.copyWithFont(font));
      }
      fontSize--;
    }
  }

  public void dispose()
  {
    if (_style != null)
       _style.dispose();
    super.dispose();
  }


  /** minFontSizeFactor in [0.1, 0.99] allows shrinking the font; any other value keeps its size */
  public FittedLabelImageFactory(String text, LabelStyle style, String maxWidthText, float minFontSizeFactor, int lineSeparation)
  {
     this(text, style, maxWidthText, minFontSizeFactor, lineSeparation, HorizontalAlignment.Center);
  }
  public FittedLabelImageFactory(String text, LabelStyle style, String maxWidthText, float minFontSizeFactor, int lineSeparation, HorizontalAlignment linesAlignment)
  {
     _text = text;
     _style = new LabelStyle(style);
     _maxWidthText = maxWidthText;
     _maxWidth = 0F;
     _minFontSizeFactor = minFontSizeFactor;
     _lineSeparation = lineSeparation;
     _linesAlignment = linesAlignment;
  }

  public FittedLabelImageFactory(String text, LabelStyle style, float maxWidth, float minFontSizeFactor, int lineSeparation)
  {
     this(text, style, maxWidth, minFontSizeFactor, lineSeparation, HorizontalAlignment.Center);
  }
  public FittedLabelImageFactory(String text, LabelStyle style, float maxWidth, float minFontSizeFactor, int lineSeparation, HorizontalAlignment linesAlignment)
  {
     _text = text;
     _style = new LabelStyle(style);
     _maxWidthText = "";
     _maxWidth = maxWidth;
     _minFontSizeFactor = minFontSizeFactor;
     _lineSeparation = lineSeparation;
     _linesAlignment = linesAlignment;
  }

  public final boolean isMutable()
  {
    return false;
  }

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
    ICanvas measuringCanvas = context.getFactory().createCanvas(true);
    IImageFactory fitted = createFitted(measuringCanvas);
    if (measuringCanvas != null)
       measuringCanvas.dispose();
  
    fitted.create(context, new FittedLabelImageFactory_Listener(fitted, listener, deleteListener), true);
  }

}