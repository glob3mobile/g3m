package org.glob3.mobile.generated; 
//
//  LabelImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

//
//  LabelImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//




public class LabelImageBuilder extends AbstractImageBuilder
{
  private String _text;
  private final GFont _font;
  private final float _margin;
  private final Color _color ;
  private final Color _backgroundColor ;
  private final float _cornerRadius;


  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color backgroundColor)
  {
     this(text, font, margin, color, backgroundColor, 0);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color)
  {
     this(text, font, margin, color, Color.transparent(), 0);
  }
  public LabelImageBuilder(String text, GFont font, float margin)
  {
     this(text, font, margin, Color.white(), Color.transparent(), 0);
  }
  public LabelImageBuilder(String text, GFont font)
  {
     this(text, font, 0, Color.white(), Color.transparent(), 0);
  }
  public LabelImageBuilder(String text)
  {
     this(text, GFont.sansSerif(), 0, Color.white(), Color.transparent(), 0);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color backgroundColor, float cornerRadius)
  {
     _text = text;
     _font = font;
     _margin = margin;
     _color = new Color(color);
     _backgroundColor = new Color(backgroundColor);
     _cornerRadius = cornerRadius;
  }

  public final boolean isMutable()
  {
    return true;
  }

  public final void setText(String text)
  {
    if (!_text.equals(text))
    {
      _text = text;
      changed();
    }
  }

  public void dispose()
  {
    super.dispose();
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = context.getFactory().createCanvas();
  
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final IMathUtils mu = context.getMathUtils();
  
    final float margin2 = _margin *2;
    final int width = mu.round(textExtent._x + margin2);
    final int height = mu.round(textExtent._y + margin2);
    canvas.initialize(width, height);
  
    if (!_backgroundColor.isTransparent())
    {
      canvas.setFillColor(_backgroundColor);
      if (_cornerRadius > 0)
      {
        canvas.fillRoundedRectangle(0, 0, width, height, _cornerRadius);
      }
      else
      {
        canvas.fillRectangle(0, 0, width, height);
      }
    }
  
    canvas.setFillColor(_color);
    canvas.fillText(_text, _margin, _margin);
  
    canvas.createImage(new LabelImageBuilder_ImageListener(listener, deleteListener, getImageName()), true);
  
    if (canvas != null)
       canvas.dispose();
  }

  public final String getImageName()
  {
    return (_text + "/" + _font.description() + "/" + IStringUtils.instance().toString(_margin));
  }

}