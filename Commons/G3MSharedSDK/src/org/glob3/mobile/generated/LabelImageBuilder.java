package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final GFont _font = new GFont();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final GFont _font = new internal();
//#endif
  private final float _margin;
  private final Color _color = new Color();

  private final Color _shadowColor = new Color();
  private final float _shadowBlur;
  private final float _shadowOffsetX;
  private final float _shadowOffsetY;

  private final Color _backgroundColor = new Color();
  private final float _cornerRadius;

  private final boolean _isMutable;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String getImageName() const
  private String getImageName()
  {
	final IStringUtils su = IStringUtils.instance();
	return (_text + "/" + _font.description() + "/" + su.toString(_margin) + "/" + _color.toID() + "/" + _shadowColor.toID() + "/" + su.toString(_shadowBlur) + "/" + su.toString(_shadowOffsetX) + "/" + su.toString(_shadowOffsetY) + "/" + _backgroundColor.toID() + "/" + su.toString(_cornerRadius));
  }


  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur, float shadowOffsetX, float shadowOffsetY, Color backgroundColor, float cornerRadius)
  {
	  this(text, font, margin, color, shadowColor, shadowBlur, shadowOffsetX, shadowOffsetY, backgroundColor, cornerRadius, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur, float shadowOffsetX, float shadowOffsetY, Color backgroundColor)
  {
	  this(text, font, margin, color, shadowColor, shadowBlur, shadowOffsetX, shadowOffsetY, backgroundColor, 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur, float shadowOffsetX, float shadowOffsetY)
  {
	  this(text, font, margin, color, shadowColor, shadowBlur, shadowOffsetX, shadowOffsetY, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur, float shadowOffsetX)
  {
	  this(text, font, margin, color, shadowColor, shadowBlur, shadowOffsetX, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur)
  {
	  this(text, font, margin, color, shadowColor, shadowBlur, 0, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor)
  {
	  this(text, font, margin, color, shadowColor, 0, 0, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin, Color color)
  {
	  this(text, font, margin, color, Color.transparent(), 0, 0, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font, float margin)
  {
	  this(text, font, margin, Color.white(), Color.transparent(), 0, 0, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text, GFont font)
  {
	  this(text, font, 0, Color.white(), Color.transparent(), 0, 0, 0, Color.transparent(), 0, false);
  }
  public LabelImageBuilder(String text)
  {
	  this(text, GFont.sansSerif(), 0, Color.white(), Color.transparent(), 0, 0, 0, Color.transparent(), 0, false);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: LabelImageBuilder(const String& text, const GFont& font = GFont::sansSerif(), const float margin = 0, const Color& color = Color::white(), const Color& shadowColor = Color::transparent(), const float shadowBlur = 0, const float shadowOffsetX = 0, const float shadowOffsetY = 0, const Color& backgroundColor = Color::transparent(), const float cornerRadius = 0, const boolean isMutable = false) : _text(text), _font(font), _margin(margin), _color(color), _shadowColor(shadowColor), _shadowBlur(shadowBlur), _shadowOffsetX(shadowOffsetX), _shadowOffsetY(shadowOffsetY), _backgroundColor(backgroundColor), _cornerRadius(cornerRadius), _isMutable(isMutable)
  public LabelImageBuilder(String text, GFont font, float margin, Color color, Color shadowColor, float shadowBlur, float shadowOffsetX, float shadowOffsetY, Color backgroundColor, float cornerRadius, boolean isMutable)
  {
	  _text = text;
	  _font = new GFont(font);
	  _margin = margin;
	  _color = new Color(color);
	  _shadowColor = new Color(shadowColor);
	  _shadowBlur = shadowBlur;
	  _shadowOffsetX = shadowOffsetX;
	  _shadowOffsetY = shadowOffsetY;
	  _backgroundColor = new Color(backgroundColor);
	  _cornerRadius = cornerRadius;
	  _isMutable = isMutable;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isMutable() const
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
	  ILogger.instance().logError("Can't change text on an inmutable LabelImageBuilder");
	}
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

  public final void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener)
  {
  
	ICanvas canvas = context.getFactory().createCanvas(true);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFont(_font);
	canvas.setFont(new GFont(_font));
  
	final Vector2F textExtent = canvas.textExtent(_text);
  
	final IMathUtils mu = context.getMathUtils();
  
	final float margin2 = _margin *2;
	final int width = mu.round(textExtent._x + margin2);
	final int height = mu.round(textExtent._y + margin2);
	canvas.initialize(width, height);
  
	if (!_shadowColor.isFullTransparent())
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setShadow(_shadowColor, _shadowBlur, _shadowOffsetX, _shadowOffsetY);
	  canvas.setShadow(new Color(_shadowColor), _shadowBlur, _shadowOffsetX, _shadowOffsetY);
	}
  
	if (!_backgroundColor.isFullTransparent())
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_backgroundColor);
	  canvas.setFillColor(new Color(_backgroundColor));
	  if (_cornerRadius > 0)
	  {
		canvas.fillRoundedRectangle(0, 0, width, height, _cornerRadius);
	  }
	  else
	  {
		canvas.fillRectangle(0, 0, width, height);
	  }
	}
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: canvas->setFillColor(_color);
	canvas.setFillColor(new Color(_color));
	canvas.fillText(_text, _margin, _margin);
  
	canvas.createImage(new LabelImageBuilder_ImageListener(listener, deleteListener, getImageName()), true);
  
	if (canvas != null)
		canvas.dispose();
  }


}
