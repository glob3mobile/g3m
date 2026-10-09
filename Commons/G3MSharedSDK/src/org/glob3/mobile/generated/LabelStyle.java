package org.glob3.mobile.generated;
//
//  LabelStyle.cpp
//  G3M
//

//
//  LabelStyle.hpp
//  G3M
//




//class ImageBackground;
//class ICanvas;


/**
 How a label's text is rendered: font, color, shadow and the background behind
 the text. A value: copies are independent (the background is copied too).
 */
public class LabelStyle
{
  private final GFont _font;
  private final Color _color ;
  private final Color _shadowColor ;
  private final float _shadowBlur;
  private final Vector2F _shadowOffset;
  private final ImageBackground _background;

  private static ImageBackground backgroundOrNull(ImageBackground background)
  {
    return (background == null) ? new NullImageBackground() : background;
  }


  /** takes ownership of background; NULL means no background */
  public LabelStyle(GFont font, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset, ImageBackground background)
  {
     _font = font;
     _color = color;
     _shadowColor = shadowColor;
     _shadowBlur = shadowBlur;
     _shadowOffset = shadowOffset;
     _background = backgroundOrNull(background);
  }

  public LabelStyle(LabelStyle that)
  {
     _font = that._font;
     _color = that._color;
     _shadowColor = that._shadowColor;
     _shadowBlur = that._shadowBlur;
     _shadowOffset = that._shadowOffset;
     _background = that._background.copy();
  }

  public void dispose()
  {
    if (_background != null)
       _background.dispose();
  }

  public static LabelStyle plain(GFont font, Color color)
  {
    return new LabelStyle(font, color, Color.TRANSPARENT, 0, Vector2F.ZERO, null);
  }

  public static LabelStyle shadowed(GFont font, Color color, Color shadowColor, float shadowBlur, Vector2F shadowOffset)
  {
    return new LabelStyle(font, color, shadowColor, shadowBlur, shadowOffset, null);
  }

  /** text over a filled box with rounded corners, with padding between the text and the box */
  public static LabelStyle boxed(GFont font, Color color, Vector2F padding, Color backgroundColor, float cornerRadius)
  {
    return new LabelStyle(font, color, Color.TRANSPARENT, 0, Vector2F.ZERO, new BoxImageBackground(Vector2F.ZERO, 0, Color.TRANSPARENT, padding, backgroundColor, cornerRadius)); // borderColor -  borderWidth -  margin
  }

  public final GFont getFont()
  {
    return _font;
  }

  public final Color getColor()
  {
    return _color;
  }

  public final boolean hasShadow()
  {
    return !_shadowColor.isFullTransparent();
  }

  public final Color getShadowColor()
  {
    return _shadowColor;
  }

  public final float getShadowBlur()
  {
    return _shadowBlur;
  }

  public final Vector2F getShadowOffset()
  {
    return _shadowOffset;
  }

  /** draws the background sized to the text and answers where the text goes */
  public final Vector2F initializeCanvas(ICanvas canvas, Vector2F textExtent)
  {
    return _background.initializeCanvas(canvas, textExtent);
  }

  /** a new background, owned by the caller */
  public final ImageBackground copyBackground()
  {
    return _background.copy();
  }

  public final LabelStyle copyWithFont(GFont font)
  {
    return new LabelStyle(font, _color, _shadowColor, _shadowBlur, _shadowOffset, _background.copy());
  }

  public final LabelStyle copyWithoutBackground()
  {
    return new LabelStyle(_font, _color, _shadowColor, _shadowBlur, _shadowOffset, null);
  }


  // IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_font.description());
    isb.addString("/");
    isb.addString(_color.id());
    isb.addString("/");
    isb.addString(_shadowColor.id());
    isb.addString("/");
    isb.addFloat(_shadowBlur);
    isb.addString("/");
    isb.addString(_shadowOffset.description());
    isb.addString("/");
    isb.addString(_background.description());
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  @Override
  public String toString() {
    return description();
  }

}