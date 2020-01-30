package org.glob3.mobile.generated;
//
//  AbstractImageBuilder.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

//
//  AbstractImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//




public abstract class AbstractImageBuilder implements IImageBuilder
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

  public AbstractImageBuilder()
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
