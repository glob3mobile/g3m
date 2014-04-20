package org.glob3.mobile.generated; 
//
//  AbstractImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

//
//  AbstractImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//




public class AbstractImageBuilder implements IImageBuilder
{
  private ChangedListener _listener;

  protected final void changed()
  {
    if (_listener != null)
    {
      _listener.changed();
    }
  }

  public AbstractImageBuilder()
  {
     _listener = null;
  }

  public void dispose()
  {
  }

  public final void setChangeListener(ChangedListener listener)
  {
    if (_listener != null)
    {
      throw new RuntimeException("listener already set!");
    }
    _listener = listener;
  }

}