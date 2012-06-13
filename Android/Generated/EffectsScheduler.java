package org.glob3.mobile.generated; 
public class EffectsScheduler extends Renderer
{
  private java.util.ArrayList<EffectRun> _effects = new java.util.ArrayList<EffectRun>();
  private ITimer _timer;
  private final IFactory _factory;

  private void doOneCyle(RenderContext rc)
  {
	final TimeInterval now = _timer.now();
  
  
	processFinishedEffects(rc, now);
  
  
	for (int i = 0; i < _effects.size(); i++)
	{
	  EffectRun effectRun = _effects.get(i);
  
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
	for (int i = 0; i < _effects.size(); i++)
	{
	  EffectRun effectRun = _effects.get(i);
  
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
//C++ TO JAVA CONVERTER TODO TASK: There is no direct equivalent to the STL vector 'erase' method in Java:
	  _effects.erase(_effects.iterator() + indexToRemove);
	}
  }

  public EffectsScheduler()
  {
	  _effects = new java.util.ArrayList<EffectRun>();

  }

  public void initialize(InitializationContext ic)
  {
	_factory = ic.getFactory();
	_timer = ic.getFactory().createTimer();
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

	for (int i = 0; i < _effects.size(); i++)
	{
	  EffectRun effectRun = _effects.get(i);
	  if (effectRun != null)
		  effectRun.dispose();
	}
  }

  public final void startEffect(Effect effect)
  {
	_effects.add(new EffectRun(effect));
  }
}