//
//  EffectWithListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#ifndef G3M_EffectWithListener
#define G3M_EffectWithListener

#include "Effects.hpp"

class EffectListener;


// wraps any effect and tells the listener when it stops or is canceled
class EffectWithListener : public Effect {
private:
  Effect*         _effect;
  EffectListener* _listener;
  const bool      _deleteListener;

public:
  EffectWithListener(Effect*         effect,
                     EffectListener* listener,
                     const bool      deleteListener) :
  _effect(effect),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~EffectWithListener();

  void start(const G3MRenderContext* rc,
             const TimeInterval& when);

  void doStep(const G3MRenderContext* rc,
              const TimeInterval& when);

  bool isDone(const G3MRenderContext* rc,
              const TimeInterval& when);

  void stop(const G3MRenderContext* rc,
            const TimeInterval& when);

  void cancel(const TimeInterval& when);

};

#endif
