package org.glob3.mobile.generated; 
//
//  IImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//

//
//  IImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/14.
//
//


//class G3MContext;
//class IImageBuilderListener;
//class ChangedListener;


public abstract class IImageBuilder
{
  private ChangedListener _listener;

  protected final void changed()
  {
    if (_listener != null)
    {
      _listener.changed();
    }
  }

  public IImageBuilder()
  {
     _listener = null;
  }

///#ifdef C_CODE
  public void dispose()
  {
  }
///#endif
///#ifdef JAVA_CODE
//  void dispose();
///#endif

  public abstract boolean isMutable();

  public abstract void build(G3MContext context, IImageBuilderListener listener, boolean deleteListener);

  public abstract String getImageName();

  public final void setChangeListener(ChangedListener listener)
  {
    if (_listener != null)
    {
      ILogger.instance().logError("listener already set!");
    }
    _listener = listener;
  }

}