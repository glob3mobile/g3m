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





//class ImageBackground;


public class LabelImageBuilder extends AbstractImageBuilder
{
  private String _text;
  private final GFont _font;
  private final Color _color ;

  private final Color _shadowColor ;
  private final float _shadowBlur;
  private final Vector2F _shadowOffset;

  private final ImageBackground _background;

  private final boolean _isMutable;

  private String getImageName()
  {
    final IStringUtils su = IStringUtils.instance();
    return (_text + "/" + _font.description() + "/" + _color.id() + "/" + _shadowColor.id() + "/" + su.toString(_shadowBlur) + "/" + _shadowOffset.description() + "/" + _background.description());
  }


  public LabelImageBuilder(String text, GFont font, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, ImageBackground background)
  {
     this(text, font, color, shadowColor, shadowBlur, shadowOffset, background, false);
  }
  public LabelImageBuilder(String text, GFont font, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset)
  {
     this(text, font, color, shadowColor, shadowBlur, shadowOffset, null, false);
  }
  public LabelImageBuilder(String text, GFont font, Color color, Color shadowColor, float shadowBlur)
  {
     this(text, font, color, shadowColor, shadowBlur, Vector2F.zero(), null, false);
  }
  public LabelImageBuilder(String text, GFont font, Color color, Color shadowColor)
  {
     this(text, font, color, shadowColor, 0, Vector2F.zero(), null, false);
  }
  public LabelImageBuilder(String text, GFont font, Color color)
  {
     this(text, font, color, Color.transparent(), 0, Vector2F.zero(), null, false);
  }
  public LabelImageBuilder(String text, GFont font)
  {
     this(text, font, Color.white(), Color.transparent(), 0, Vector2F.zero(), null, false);
  }
  public LabelImageBuilder(String text)
  {
     this(text, GFont.sansSerif(), Color.white(), Color.transparent(), 0, Vector2F.zero(), null, false);
  }
  public LabelImageBuilder(String text, GFont font, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, ImageBackground background, boolean isMutable)
  {
     _text = text;
     _font = font;
     _color = color;
     _shadowColor = shadowColor;
     _shadowBlur = shadowBlur;
     _shadowOffset = shadowOffset;
     _background = (background == null) ? new NullImageBackground() : background;
     _isMutable = isMutable;
  }

  public final boolean isMutable()
  {
    return _isMutable;
  }

  public final void setText(String text)
  {
    if (_isMutable)
    {
      if (!_text.equals(text))
      {
        _text = text;
        changed();
      }
    }
    else
    {
      throw new RuntimeException("Can't change text on an inmutable LabelImageBuilder");
    }
  }

  public void dispose()
  {
    if (_background != null)
       _background.dispose();
    super.dispose();
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = context.getFactory().createCanvas(true);
  
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final Vector2F contentPos = _background.initializeCanvas(canvas, textExtent._x, textExtent._y);
  
    if (!_shadowColor.isFullTransparent())
    {
      canvas.setShadow(_shadowColor, _shadowBlur, _shadowOffset._x, _shadowOffset._y);
    }
  
    canvas.setFillColor(_color);
    canvas.fillText(_text, contentPos._x, contentPos._y);
  
    canvas.createImage(new LabelImageBuilder_ImageListener(listener, deleteListener, getImageName()), true);
  
    if (canvas != null)
       canvas.dispose();
  }

}
