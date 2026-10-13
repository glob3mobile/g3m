package org.glob3.mobile.generated;
//
//  AbstractImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

//
//  AbstractImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//




public abstract class AbstractImageFactory implements IImageFactory
{
  private ChangedListener _changeListener;


  protected final void changed()
  {
    if (_changeListener != null)
    {
      _changeListener.changed();
    }
  }

  public void dispose()
  {
  }

  public AbstractImageFactory()
  {
     _changeListener = null;
  }

  public final void setChangeListener(ChangedListener changeListener)
  {
    if (_changeListener != null)
    {
      throw new RuntimeException("changeListener already set!");
    }
    _changeListener = changeListener;
  }

}