package org.glob3.mobile.generated;import java.util.*;

public abstract class IDeviceAttitude
{
  private static IDeviceAttitude _instance;

  protected CoordinateSystem _camCSPortrait;

  protected CoordinateSystem _camCSPortraitUD;

  protected CoordinateSystem _camCSLL;

  protected CoordinateSystem _camCSLR;

  public IDeviceAttitude()
  {
	  _camCSPortrait = null;
	  _camCSPortraitUD = null;
	  _camCSLL = null;
	  _camCSLR = null;
  
  }

  public static void setInstance(IDeviceAttitude deviceAttitude)
  {
	if (_instance != null)
	{
	  ILogger.instance().logWarning("ILooger instance already set!");
	  if (_instance != null)
		  _instance.dispose();
	}
	_instance = deviceAttitude;
  }

  //Singleton
  public static IDeviceAttitude instance()
  {
	return _instance;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	if (_camCSLL != null)
		_camCSLL.dispose();
	if (_camCSLR != null)
		_camCSLR.dispose();
	if (_camCSPortrait != null)
		_camCSPortrait.dispose();
	if (_camCSPortraitUD != null)
		_camCSPortraitUD.dispose();
//#endif
  }

  /**
   Must be called before any other operation
   **/

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void startTrackingDeviceOrientation() const = 0;
  public abstract void startTrackingDeviceOrientation();

  /**
   Must be called to stop operations
   **/

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void stopTrackingDeviceOrientation() const = 0;
  public abstract void stopTrackingDeviceOrientation();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isTracking() const = 0;
  public abstract boolean isTracking();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void copyValueOfRotationMatrix(MutableMatrix44D& rotationMatrix) const = 0;
  public abstract void copyValueOfRotationMatrix(tangible.RefObject<MutableMatrix44D> rotationMatrix);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual InterfaceOrientation getCurrentInterfaceOrientation() const = 0;
  public abstract InterfaceOrientation getCurrentInterfaceOrientation();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CoordinateSystem getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation) const
  public final CoordinateSystem getCameraCoordinateSystemForInterfaceOrientation(InterfaceOrientation orientation)
  {
  
	if (_camCSLL == null)
	{
	  _camCSPortrait = new CoordinateSystem(new Vector3D(1,0,0), new Vector3D(0,0,-1), new Vector3D(0,1,0), Vector3D.zero); //Z -> Up - Y -> View Direction - X
  
	  _camCSPortraitUD = new CoordinateSystem(new Vector3D(1,0,0), new Vector3D(0,0,-1), new Vector3D(0,-1,0), Vector3D.zero); //Z -> Up - Y -> View Direction - X
  
	  _camCSLL = new CoordinateSystem(new Vector3D(0,1,0), new Vector3D(0,0,-1), new Vector3D(-1,0,0), Vector3D.zero); //Z -> Up - Y -> View Direction - X
  
	  _camCSLR = new CoordinateSystem(new Vector3D(0,1,0), new Vector3D(0,0,-1), new Vector3D(1,0,0), Vector3D.zero); //Z -> Up - Y -> View Direction - X
	}
  
	switch (orientation)
	{
	  case PORTRAIT:
	  {
		return _camCSPortrait;
	  }
  
	  case PORTRAIT_UPSIDEDOWN:
	  {
		return _camCSPortraitUD;
	  }
  
	  case LANDSCAPE_LEFT:
	  {
		return _camCSLL;
	  }
  
	  case LANDSCAPE_RIGHT:
	  {
		return _camCSLR;
	  }
  
	  default:
	  {
		//Landscape right
		return _camCSLR;
	  }
	}
  }

}
