package org.glob3.mobile.generated;public class DeviceAttitudeCameraHandler extends CameraEventHandler
{

  private MutableMatrix44D _localRM = new MutableMatrix44D();
  private MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
  private MutableMatrix44D _camRM = new MutableMatrix44D();

  private boolean _updateLocation;

  private ILocationModifier _locationModifier;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void setPositionOnNextCamera(Camera* nextCamera, Geodetic3D& pos) const
  private void setPositionOnNextCamera(Camera nextCamera, tangible.RefObject<Geodetic3D> pos)
  {
	  if (nextCamera.hasValidViewDirection())
	  {
		  nextCamera.setGeodeticPosition(pos.argvalue);
	  }
	  else
	  {
		  ILogger.instance().logWarning("Trying to set position of unvalid camera. ViewDirection: %s", nextCamera.getViewDirection().description().c_str());
	  }
  }


  public DeviceAttitudeCameraHandler(boolean updateLocation)
  {
	  this(updateLocation, null);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: DeviceAttitudeCameraHandler(boolean updateLocation, ILocationModifier* locationModifier = null): _updateLocation(updateLocation), _locationModifier(locationModifier)
  public DeviceAttitudeCameraHandler(boolean updateLocation, ILocationModifier locationModifier)
  {
	  _updateLocation = updateLocation;
	  _locationModifier = locationModifier;
  
  }

  public void dispose()
  {
	  IDeviceAttitude.instance().stopTrackingDeviceOrientation();
	  IDeviceLocation.instance().stopTrackingLocation();
  
	  _locationModifier = null;
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  
	  IDeviceAttitude devAtt = IDeviceAttitude.instance();
	  Camera nextCamera = rc.getNextCamera();
  
	  //Updating location
	  if (_updateLocation)
	  {
  
		  IDeviceLocation loc = IDeviceLocation.instance();
  
		  boolean isTracking = loc.isTrackingLocation();
		  if (!isTracking)
		  {
			  isTracking = loc.startTrackingLocation();
		  }
  
		  if (isTracking)
		  {
			  Geodetic3D g = loc.getLocation();
			  if (!g.isNan())
			  {
  
				  //Changing current location
				  if (_locationModifier == null)
				  {
					  tangible.RefObject<Geodetic3D> tempRef_g = new tangible.RefObject<Geodetic3D>(g);
					  setPositionOnNextCamera(nextCamera, tempRef_g);
					  g = tempRef_g.argvalue;
				  }
				  else
				  {
					  Geodetic3D g2 = _locationModifier.modify(g);
					  if (!g2.isNan())
					  {
						  tangible.RefObject<Geodetic3D> tempRef_g2 = new tangible.RefObject<Geodetic3D>(g2);
						  setPositionOnNextCamera(nextCamera, tempRef_g2);
						  g2 = tempRef_g2.argvalue;
					  }
				  }
			  }
		  }
  
	  }
  
	  if (devAtt == null)
	  {
		  THROW_EXCEPTION("IDeviceAttitude not initilized");
	  }
  
	  if (!devAtt.isTracking())
	  {
		  devAtt.startTrackingDeviceOrientation();
	  }
  
	  //Getting Global Rotation
	  tangible.RefObject<MutableMatrix44D> tempRef__attitudeMatrix = new tangible.RefObject<MutableMatrix44D>(_attitudeMatrix);
	  IDeviceAttitude.instance().copyValueOfRotationMatrix(tempRef__attitudeMatrix);
	  _attitudeMatrix = tempRef__attitudeMatrix.argvalue;
	  if (!_attitudeMatrix.isValid())
	  {
		  return;
	  }
  
	  Geodetic3D camPosition = nextCamera.getGeodeticPosition();
  
	  //Getting interface orientation
	  InterfaceOrientation ori = IDeviceAttitude.instance().getCurrentInterfaceOrientation();
  
	  //Getting Attitude Matrix
	  CoordinateSystem camCS = IDeviceAttitude.instance().getCameraCoordinateSystemForInterfaceOrientation(ori);
  
	  //Transforming global rotation to local rotation
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: CoordinateSystem local = rc->getPlanet()->getCoordinateSystemAt(camPosition);
	  CoordinateSystem local = rc.getPlanet().getCoordinateSystemAt(new Geodetic3D(camPosition));
	  tangible.RefObject<MutableMatrix44D> tempRef__localRM = new tangible.RefObject<MutableMatrix44D>(_localRM);
	  local.copyValueOfRotationMatrix(tempRef__localRM);
	  _localRM = tempRef__localRM.argvalue;
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);
	  _camRM.copyValueOfMultiplication(new MutableMatrix44D(_localRM), new MutableMatrix44D(_attitudeMatrix));
  
	  //Applying to Camera CS
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: CoordinateSystem finalCS = camCS.applyRotation(_camRM);
	  CoordinateSystem finalCS = camCS.applyRotation(new MutableMatrix44D(_camRM));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: nextCamera->setCameraCoordinateSystem(finalCS);
	  nextCamera.setCameraCoordinateSystem(new CoordinateSystem(finalCS));
  
  }

  public boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	  return false;
  }


  public void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void setDebugMeshRenderer(MeshRenderer meshRenderer)
  {
  }

  public final void onMouseWheel(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void setLocationModifier(ILocationModifier lm)
  {
	if (_locationModifier != null && _locationModifier != lm)
	{
	  _locationModifier = null;
	}
	_locationModifier = lm;
  }

}
