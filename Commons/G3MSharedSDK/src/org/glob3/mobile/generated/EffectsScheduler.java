package org.glob3.mobile.generated;public class EffectsScheduler
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IFactory _factory;
//#else
  private IFactory _factory;
//#endif

  private void processFinishedEffects(G3MRenderContext rc, TimeInterval when)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.ArrayList<EffectRun> effectsToStop = new java.util.ArrayList<EffectRun>();
	java.util.Iterator<EffectRun> it = _effectsRuns.iterator();
	while (it.hasNext())
	{
	  EffectRun effectRun = it.next();
  
	  boolean removed = false;
	  if (effectRun._started)
	  {
		Effect effect = effectRun._effect;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (effect->isDone(rc, when))
		if (effect.isDone(rc, new TimeInterval(when)))
		{
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
		  it = _effectsRuns.erase(it);
		  removed = true;
		  effectsToStop.add(effectRun);
		}
	  }
	  if (!removed)
	  {
	  }
	}
  
	final int removedSize = effectsToStop.size();
	for (int i = 0; i < removedSize; i++)
	{
	  EffectRun effectRun = effectsToStop.get(i);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: effectRun->_effect->stop(rc, when);
	  effectRun._effect.stop(rc, new TimeInterval(when));
  
	  if (effectRun != null)
		  effectRun.dispose();
	}
//#endif
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_effectsToStop.clear();
	final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
	while (iterator.hasNext())
	{
	  final EffectRun effectRun = iterator.next();
	  if (effectRun._started)
	  {
		if (effectRun._effect.isDone(rc, when))
		{
		  iterator.remove();
		  _effectsToStop.add(effectRun);
		}
	  }
	}
  
	final int effectsToStopSize = _effectsToStop.size();
	for (int i = 0; i < effectsToStopSize; i++)
	{
	  final EffectRun effectRun = _effectsToStop.get(i);
	  effectRun._effect.stop(rc, when);
	  effectRun.dispose();
	}
//#endif
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  private final java.util.ArrayList<EffectRun> _effectsToStop = new java.util.ArrayList<EffectRun>();
  private final java.util.ArrayList<EffectRun> _effectsToCancel = new java.util.ArrayList<EffectRun>();
//#endif

  public EffectsScheduler()
  {
	  _effectsRuns = new java.util.ArrayList<EffectRun*>();
  }

  public final void doOneCyle(G3MRenderContext rc)
  {
	if (!_effectsRuns.isEmpty())
	{
	  final TimeInterval now = _timer.now();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: processFinishedEffects(rc, now);
	  processFinishedEffects(rc, new TimeInterval(now));
  
	  // ask for _effectsRuns.size() here, as processFinishedEffects can modify the size
	  final int effectsRunsSize = _effectsRuns.size();
	  for (int i = 0; i < effectsRunsSize; i++)
	  {
		EffectRun effectRun = _effectsRuns.get(i);
		Effect effect = effectRun._effect;
		if (!effectRun._started)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: effect->start(rc, now);
		  effect.start(rc, new TimeInterval(now));
		  effectRun._started = true;
		}
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: effect->doStep(rc, now);
		effect.doStep(rc, new TimeInterval(now));
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
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.ArrayList<Integer> indicesToRemove = new java.util.ArrayList<Integer>();
  
	final int size = _effectsRuns.size();
	for (int i = 0; i < size; i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started)
	  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: effectRun->_effect->cancel(now);
		effectRun._effect.cancel(new TimeInterval(now));
	  }
	  indicesToRemove.add(i);
	}
  
	// backward iteration, to remove from bottom to top
	for (int i = indicesToRemove.size() - 1; i >= 0; i--)
	{
	  final int indexToRemove = indicesToRemove.get(i);
	  EffectRun effectRun = _effectsRuns.get(indexToRemove);
	  if (effectRun != null)
		  effectRun.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _effectsRuns.erase(_effectsRuns.iterator() + indexToRemove);
	}
//#endif
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_effectsToCancel.clear();
  
	final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
	while (iterator.hasNext())
	{
	  final EffectRun effectRun = iterator.next();
	  if (effectRun._started)
	  {
		_effectsToCancel.add(effectRun);
	  }
	  else
	  {
		effectRun.dispose();
	  }
	  iterator.remove();
	}
  
	final int effectsToCancelSize = _effectsToCancel.size();
	for (int i = 0; i < effectsToCancelSize; i++)
	{
	  final EffectRun effectRun = _effectsToCancel.get(i);
	  effectRun._effect.cancel(now);
	  effectRun.dispose();
	}
//#endif
  }

  public final void cancelAllEffectsFor(EffectTarget target)
  {
	final TimeInterval now = _timer.now();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	java.util.ArrayList<Integer> indicesToRemove = new java.util.ArrayList<Integer>();
  
	final int size = _effectsRuns.size();
	for (int i = 0; i < size; i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._target == target)
	  {
		if (effectRun._started)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: effectRun->_effect->cancel(now);
		  effectRun._effect.cancel(new TimeInterval(now));
		}
		indicesToRemove.add(i);
	  }
	}
  
	// backward iteration, to remove from bottom to top
	for (int i = indicesToRemove.size() - 1; i >= 0; i--)
	{
	  final int indexToRemove = indicesToRemove.get(i);
	  EffectRun effectRun = _effectsRuns.get(indexToRemove);
	  if (effectRun != null)
		  effectRun.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _effectsRuns.erase(_effectsRuns.iterator() + indexToRemove);
	}
//#endif
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	_effectsToCancel.clear();
  
	final java.util.Iterator<EffectRun> iterator = _effectsRuns.iterator();
	while (iterator.hasNext())
	{
	  final EffectRun effectRun = iterator.next();
	  if (effectRun._target == target)
	  {
		if (effectRun._started)
		{
		  _effectsToCancel.add(effectRun);
		}
		else
		{
		  effectRun.dispose();
		}
		iterator.remove();
	  }
	}
  
	final int effectsToCancelSize = _effectsToCancel.size();
	for (int i = 0; i < effectsToCancelSize; i++)
	{
	  final EffectRun effectRun = _effectsToCancel.get(i);
	  effectRun._effect.cancel(now);
	  effectRun.dispose();
	}
//#endif
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
