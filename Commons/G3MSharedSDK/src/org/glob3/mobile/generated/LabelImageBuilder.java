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
  private final String _text;
  private final GFont _font;
  private final Color _color ;
  private final float _margin;


  public LabelImageBuilder(String text, GFont font, Color color)
  {
     this(text, font, color, 1);
  }
  public LabelImageBuilder(String text, GFont font)
  {
     this(text, font, Color.white(), 1);
  }
  public LabelImageBuilder(String text)
  {
     this(text, GFont.sansSerif(), Color.white(), 1);
  }
  public LabelImageBuilder(String text, GFont font, Color color, float margin)
  {
     _text = text;
     _font = font;
     _color = new Color(color);
     _margin = margin;
//    const float fontSize = 20;
//
//    const Color color       = Color::white();
//    const Color shadowColor = Color::black();
//
//    const int separation = 2;
  }

  public void dispose()
  {
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
    final IFactory factory = context.getFactory();
  
    ICanvas canvas = factory.createCanvas();
  
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final IMathUtils mu = context.getMathUtils();
  
    canvas.initialize(mu.round(textExtent._x + _margin *2), mu.round(textExtent._y + _margin *2));
  
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