package org.glob3.mobile.client.generated; 
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
  private IFactory _factory; // FINAL WORD REMOVE BY CONVERSOR RULE

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
	if (_effectsRuns.size() == 0)
	{
	  return DefineConstants.MAX_TIME_TO_RENDER;
	}
	else
	{
	  return 0;
	}
  }

  public boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public void onResizeViewportEvent(int width, int height)
  {
  
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