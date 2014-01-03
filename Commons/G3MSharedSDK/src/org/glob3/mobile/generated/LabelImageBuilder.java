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




public class LabelImageBuilder implements IImageBuilder
{
  private String _text;
  private final GFont _font;
  private final Color _color ;
  private final float _margin;


  public LabelImageBuilder(String text, GFont font, Color color)
  {
     this(text, font, color, 0);
  }
  public LabelImageBuilder(String text, GFont font)
  {
     this(text, font, Color.white(), 0);
  }
  public LabelImageBuilder(String text)
  {
     this(text, GFont.sansSerif(), Color.white(), 0);
  }
  public LabelImageBuilder(String text, GFont font, Color color, float margin)
  {
     _text = text;
     _font = font;
     _color = new Color(color);
     _margin = margin;
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
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = context.getFactory().createCanvas();
  
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final IMathUtils mu = context.getMathUtils();
  
    final float margin2 = _margin *2;
    canvas.initialize(mu.round(textExtent._x + margin2), mu.round(textExtent._y + margin2));
  
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