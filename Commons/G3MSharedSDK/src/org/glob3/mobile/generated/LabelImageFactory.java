package org.glob3.mobile.generated;
//
//  LabelImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

//
//  LabelImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//






public class LabelImageFactory extends AbstractImageFactory
{
  private String _text;
  private final LabelStyle _style;
  private final boolean _isMutable;

  private String getImageName()
  {
    return _text + "/" + _style.description();
  }

  public void dispose()
  {
    if (_style != null)
       _style.dispose();
    super.dispose();
  }


  public LabelImageFactory(String text, LabelStyle style)
  {
     _text = text;
     _style = new LabelStyle(style);
     _isMutable = false;
  }

  /** a mutable label accepts setText() and notifies its change listener */
  public LabelImageFactory(String text, LabelStyle style, boolean isMutable)
  {
     _text = text;
     _style = new LabelStyle(style);
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
      throw new RuntimeException("Can't change text on an inmutable LabelImageFactory");
    }
  }

  public final void create(G3MContext context, IImageFactoryListener listener, boolean deleteListener)
  {
  
    ICanvas canvas = context.getFactory().createCanvas(true);
  
    canvas.setFont(_style.getFont());
  
    final Vector2F textExtent = canvas.textExtent(_text);
  
    final Vector2F contentPos = _style.initializeCanvas(canvas, textExtent);
  
    if (_style.hasShadow())
    {
      final Vector2F shadowOffset = _style.getShadowOffset();
      canvas.setShadow(_style.getShadowColor(), _style.getShadowBlur(), shadowOffset._x, shadowOffset._y);
    }
  
    canvas.setFillColor(_style.getColor());
    canvas.fillText(_text, contentPos._x, contentPos._y);
  
    canvas.createImage(new CanvasOwnerImageListenerWrapper(canvas, new LabelImageFactory_ImageListener(listener, deleteListener, getImageName()), true), true);
  }

}