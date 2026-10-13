//
//  EffectListener.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

#ifndef G3M_EffectListener
#define G3M_EffectListener

class G3MRenderContext;
class TimeInterval;


class EffectListener {
public:
#ifdef C_CODE
  virtual ~EffectListener() {
  }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void onStop(const G3MRenderContext* rc,
                      const TimeInterval& when) = 0;

  // called while the EffectsScheduler is iterating its effects: don't start or cancel effects from here
  virtual void onCancel(const TimeInterval& when) = 0;

};

#endif
