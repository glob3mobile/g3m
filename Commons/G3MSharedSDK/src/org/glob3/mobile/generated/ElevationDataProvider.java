package org.glob3.mobile.generated;public abstract class ElevationDataProvider
{

  protected ChangedListener _changedListener;

  protected boolean _enabled;


  public ElevationDataProvider()
  {
	  _changedListener = null;
	  _enabled = true;
  }

  public void dispose()
  {

  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void initialize(G3MContext context);

  public abstract long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener);

  public abstract void cancelRequest(long requestId);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<const Sector*> getSectors() const = 0;
  public abstract java.util.ArrayList<Sector> getSectors();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2I getMinResolution() const = 0;
  public abstract Vector2I getMinResolution();

  public final void setChangedListener(ChangedListener changedListener)
  {
	_changedListener = changedListener;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onChanged() const
  public final void onChanged()
  {
	if (_changedListener != null)
	{
	  _changedListener.changed();
	}
  }

  public final void setEnabled(boolean enabled)
  {
	if (_enabled != enabled)
	{
	  _enabled = enabled;
	  onChanged();
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEnabled() const
  public final boolean isEnabled()
  {
	return _enabled;
  }

}
