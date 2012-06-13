package org.glob3.mobile.generated; 
public class EffectRun
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