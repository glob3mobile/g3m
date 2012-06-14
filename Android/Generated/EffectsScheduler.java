package org.glob3.mobile.generated; 
public class EffectsScheduler extends Renderer
{

  private static class EffectRun
  {
	public Effect _effect;
	public boolean _started;

	public EffectRun(Effect effect)
	{
		_effect = effect;
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
  private final IFactory _factory;

  private void doOneCyle(RenderContext rc)
  {
	final TimeInterval now = _timer.now();
  
  
	processFinishedEffects(rc, now);
  
  
	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started == false)
	  {
		effectRun._effect.start(rc, now);
		effectRun._started = true;
	  }
  
	  effectRun._effect.doStep(rc, now);
	}
  }

  private void processFinishedEffects(RenderContext rc, TimeInterval now)
  {
	java.util.ArrayList<Integer> indicesToRemove = new java.util.ArrayList<Integer>();
	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
  
	  if (effectRun._started == true)
	  {
		if (effectRun._effect.isDone(rc, now))
		{
		  effectRun._effect.stop(rc, now);
  
		  indicesToRemove.add(i);
		}
	  }
	}
  
	// backward iteration, to remove from bottom to top
	for (int i = indicesToRemove.size() - 1; i >= 0; i--)
	{
	  final int indexToRemove = indicesToRemove.get(i);
	  if (_effectsRuns.get(indexToRemove) != null)
		  _effectsRuns.get(indexToRemove).dispose();
  
	  // AGUSTIN NOTE: CAREFUL WHIT THE FOLLOWING SENTENCE
	  //There is no direct equivalent to the STL vector 'erase' method in Java:
	  // AND ALSO OPERATION + IS NOT ALLOWED HERE IN JAVA
	  int __dgd_answer_to_agustin;
	  // In Java:
	  //_effectsRuns.remove(indexToRemove);
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _effectsRuns.remove(indexToRemove);
	}
  }

  public EffectsScheduler()
  {
	  _effectsRuns = new java.util.ArrayList<EffectRun>();

  }

  public void initialize(InitializationContext ic)
  {
	_factory = ic.getFactory();
	_timer = _factory.createTimer();
  }

  public int render(RenderContext rc)
  {
	doOneCyle(rc);
  
	return 99999;
  }

  public boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public boolean onResizeViewportEvent(int width, int height)
  {
	return false;
  }

  public void dispose()
  {
	_factory.deleteTimer(_timer);

	for (int i = 0; i < _effectsRuns.size(); i++)
	{
	  EffectRun effectRun = _effectsRuns.get(i);
	  if (effectRun != null)
		  effectRun.dispose();
	}
  }

  public final void startEffect(Effect effect)
  {
	_effectsRuns.add(new EffectRun(effect));
  }
}