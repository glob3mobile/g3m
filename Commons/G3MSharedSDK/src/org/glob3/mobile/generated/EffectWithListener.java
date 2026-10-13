package org.glob3.mobile.generated;
//
//  EffectWithListener.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

//
//  EffectWithListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//



//class EffectListener;


// wraps any effect and tells the listener when it stops or is canceled
public class EffectWithListener extends Effect
{
  private Effect _effect;
  private EffectListener _listener;
  private final boolean _deleteListener;

  public EffectWithListener(Effect effect, EffectListener listener, boolean deleteListener)
  {
     _effect = effect;
     _listener = listener;
     _deleteListener = deleteListener;
  }

  public void dispose()
  {
    if (_effect != null)
       _effect.dispose();
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    super.dispose();
  }

  public final void start(G3MRenderContext rc, TimeInterval when)
  {
    _effect.start(rc, when);
  }

  public final void doStep(G3MRenderContext rc, TimeInterval when)
  {
    _effect.doStep(rc, when);
  }

  public final boolean isDone(G3MRenderContext rc, TimeInterval when)
  {
    return _effect.isDone(rc, when);
  }

  public final void stop(G3MRenderContext rc, TimeInterval when)
  {
    _effect.stop(rc, when);
    _listener.onStop(rc, when);
  }

  public final void cancel(TimeInterval when)
  {
    _effect.cancel(when);
    _listener.onCancel(when);
  }

}