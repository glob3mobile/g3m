package org.glob3.mobile.generated; 
public class EffectsScheduler
{

  private static class EffectRun
  {
    public Effect _effect;
    public EffectTarget _target;

    public boolean _started;

    public EffectRun(Effect effect, EffectTarget target)
    {
       _effect = effect;
       _target = target;
       _started = false;
    }

    public void dispose()
    {
      if (_effect != null)
         _effect.dispose();
    }
  }


  private java.util.ArrayList<EffectRun> _effectsRuns = new java.util.ArrayList<EffectRun>();
  private ITimer _timer;
  private IFactory _factory;

  private void processFinishedEffects(G3MRenderContext rc, TimeInterval when)
  {
  
    _effectsToStop.clear();
    final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
    while (iterator.hasNext()) {
      final EffectRun effectRun = iterator.next();
      if (effectRun._started) {
        if (effectRun._effect.isDone(rc, when)) {
          iterator.remove();
          _effectsToStop.add(effectRun);
        }
      }
    }
  
    final int effectsToStopSize = _effectsToStop.size();
    for (int i = 0; i < effectsToStopSize; i++) {
      final EffectRun effectRun = _effectsToStop.get(i);
      effectRun._effect.stop(rc, when);
      effectRun.dispose();
    }
  }

  final java.util.ArrayList<EffectRun> _effectsToStop   = new java.util.ArrayList<EffectRun>();
  final java.util.ArrayList<EffectRun> _effectsToCancel = new java.util.ArrayList<EffectRun>();

  public EffectsScheduler()
  {
     _effectsRuns = new java.util.ArrayList<EffectRun>();
  }

  public final void doOneCyle(G3MRenderContext rc)
  {
    if (!_effectsRuns.isEmpty())
    {
      final TimeInterval now = _timer.now();
  
      processFinishedEffects(rc, now);
  
      // ask for _effectsRuns.size() here, as processFinishedEffects can modify the size
      final int effectsRunsSize = _effectsRuns.size();
      for (int i = 0; i < effectsRunsSize; i++)
      {
        EffectRun effectRun = _effectsRuns.get(i);
        Effect effect = effectRun._effect;
        if (!effectRun._started)
        {
          effect.start(rc, now);
          effectRun._started = true;
        }
        effect.doStep(rc, now);
      }
    }
  }

  public final void initialize(G3MContext context)
  {
    _factory = context.getFactory();
    _timer = _factory.createTimer();
  }

  public void dispose()
  {
    if (_timer != null)
       _timer.dispose();

    for (int i = 0; i < _effectsRuns.size(); i++)
    {
      EffectRun effectRun = _effectsRuns.get(i);
      if (effectRun != null)
         effectRun.dispose();
    }
  }

  public final void startEffect(Effect effect, EffectTarget target)
  {
    _effectsRuns.add(new EffectRun(effect, target));
  }

  public final void cancelAllEffects()
  {
    final TimeInterval now = _timer.now();
  
    _effectsToCancel.clear();
  
    final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
    while (iterator.hasNext()) {
      final EffectRun effectRun = iterator.next();
      if (effectRun._started) {
        _effectsToCancel.add(effectRun);
      }
      else {
        effectRun.dispose();
      }
      iterator.remove();
    }
  
    final int effectsToCancelSize = _effectsToCancel.size();
    for (int i = 0; i < effectsToCancelSize; i++) {
      final EffectRun effectRun = _effectsToCancel.get(i);
      effectRun._effect.cancel(now);
      effectRun.dispose();
    }
  }

  public final void cancelAllEffectsFor(EffectTarget target)
  {
    final TimeInterval now = _timer.now();
  
    _effectsToCancel.clear();
  
    final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
    while (iterator.hasNext()) {
      final EffectRun effectRun = iterator.next();
      if (effectRun._target == target) {
        if (effectRun._started) {
          _effectsToCancel.add(effectRun);
        }
        else {
          effectRun.dispose();
        }
        iterator.remove();
      }
    }
  
    final int effectsToCancelSize = _effectsToCancel.size();
    for (int i = 0; i < effectsToCancelSize; i++) {
      final EffectRun effectRun = _effectsToCancel.get(i);
      effectRun._effect.cancel(now);
      effectRun.dispose();
    }
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}