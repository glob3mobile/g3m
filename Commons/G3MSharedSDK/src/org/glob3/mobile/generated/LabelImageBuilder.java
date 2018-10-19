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
  private final Vector2F _margin;
  private final Color _color ;

  private final Color _shadowColor ;
  private final float _shadowBlur;
  private final Vector2F _shadowOffset;

  private final Color _backgroundColor ;
  private final float _cornerRadius;

  private final boolean _isMutable;

  private String getImageName()
  {
    final IStringUtils su = IStringUtils.instance();
    return (_text + "/" + _font.description() + "/" + _margin.description() + "/" + _color.id() + "/" + _shadowColor.id() + "/" + su.toString(_shadowBlur) + "/" + _shadowOffset.description() + "/" + _backgroundColor.id() + "/" + su.toString(_cornerRadius));
  }


  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, Color backgroundColor, float cornerRadius)
  {
     this(text, font, margin, color, shadowColor, shadowBlur, shadowOffset, backgroundColor, cornerRadius, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, Color backgroundColor)
  {
     this(text, font, margin, color, shadowColor, shadowBlur, shadowOffset, backgroundColor, 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset)
  {
     this(text, font, margin, color, shadowColor, shadowBlur, shadowOffset, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor, float shadowBlur)
  {
     this(text, font, margin, color, shadowColor, shadowBlur, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor)
  {
     this(text, font, margin, color, shadowColor, 0, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color)
  {
     this(text, font, margin, color, Color.transparent(), 0, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin)
  {
     this(text, font, margin, Color.white(), Color.transparent(), 0, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font)
  {
     this(text, font, Vector2F.zero(), Color.white(), Color.transparent(), 0, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text)
  {
     this(text, GFont.sansSerif(), Vector2F.zero(), Color.white(), Color.transparent(), 0, Vector2F.zero(), Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, Vector2F margin, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, Color backgroundColor, float cornerRadius, boolean isMutable)
  {
     _text = text;
     _font = font;
     _margin = margin;
     _color = new Color(color);
     _shadowColor = new Color(shadowColor);
     _shadowBlur = shadowBlur;
     _shadowOffset = shadowOffset;
     _backgroundColor = new Color(backgroundColor);
     _cornerRadius = cornerRadius;
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
    super.dispose();
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = context.getFactory().createCanvas(true);
  
    canvas.setFont(_font);
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final IMathUtils mu = context.getMathUtils();
  
    final int width = (int) mu.ceil(textExtent._x + (_margin._x * 2));
    final int height = (int) mu.ceil(textExtent._y + (_margin._y * 2));
    canvas.initialize(width, height);
  
    if (!_backgroundColor.isFullTransparent())
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
  
    if (!_shadowColor.isFullTransparent())
    {
      canvas.setShadow(_shadowColor, _shadowBlur, _shadowOffset._x, _shadowOffset._y);
    }
  
    canvas.setFillColor(_color);
    canvas.fillText(_text, _margin._x, _margin._y);
  
    canvas.createImage(new LabelImageBuilder_ImageListener(listener, deleteListener, getImageName()), true);
  
    if (canvas != null)
       canvas.dispose();
  }


}
