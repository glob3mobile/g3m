package org.glob3.mobile.generated;
//
//  EffectListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//


//class G3MRenderContext;
//class TimeInterval;


public interface EffectListener
{
  void dispose();

  void onStop(G3MRenderContext rc, TimeInterval when);

  // called while the EffectsScheduler is iterating its effects: don't start or cancel effects from here
  void onCancel(TimeInterval when);

}