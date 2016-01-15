

package org.glob3.mobile.generated;

public class DeviceAttitudeCameraHandler
         extends
            CameraEventHandler {

   private final MutableMatrix44D _localRM        = new MutableMatrix44D();
   private final MutableMatrix44D _attitudeMatrix = new MutableMatrix44D();
   private final MutableMatrix44D _camRM          = new MutableMatrix44D();

   private final boolean          _updateLocation;

   private ILocationModifier      _locationModifier;


   private void setPositionOnNextCamera(final Camera nextCamera,
                                        final Geodetic3D pos) {
      if (nextCamera.hasValidViewDirection()) {
         nextCamera.setGeodeticPosition(pos);
      }
      else {
         ILogger.instance().logWarning("Trying to set position of unvalid camera. ViewDirection: %s",
                  nextCamera.getViewDirection().description());
      }
   }


   public DeviceAttitudeCameraHandler(final boolean updateLocation) {
      this(updateLocation, null);
   }


   public DeviceAttitudeCameraHandler(final boolean updateLocation,
                                      final ILocationModifier locationModifier) {
      _updateLocation = updateLocation;
      _locationModifier = locationModifier;

   }


   @Override
   public void dispose() {
      IDeviceAttitude.instance().stopTrackingDeviceOrientation();
      IDeviceLocation.instance().stopTrackingLocation();

      _locationModifier = null;
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final CameraContext cameraContext) {

      final IDeviceAttitude devAtt = IDeviceAttitude.instance();
      final Camera nextCamera = rc.getNextCamera();

      if (devAtt == null) {
         throw new RuntimeException("IDeviceAttitude not initilized");
      }

      if (!devAtt.isTracking()) {
         devAtt.startTrackingDeviceOrientation();
      }

      //Getting Global Rotation
      IDeviceAttitude.instance().copyValueOfRotationMatrix(_attitudeMatrix);
      if (!_attitudeMatrix.isValid()) {
         return;
      }

      final Geodetic3D camPosition = nextCamera.getGeodeticPosition();

      //Getting interface orientation
      final InterfaceOrientation ori = IDeviceAttitude.instance().getCurrentInterfaceOrientation();

      //Getting Attitude Matrix
      final CoordinateSystem camCS = IDeviceAttitude.instance().getCameraCoordinateSystemForInterfaceOrientation(ori);

      //Transforming global rotation to local rotation
      final CoordinateSystem local = rc.getPlanet().getCoordinateSystemAt(camPosition);
      local.copyValueOfRotationMatrix(_localRM);
      _camRM.copyValueOfMultiplication(_localRM, _attitudeMatrix);

      //Applying to Camera CS
      final CoordinateSystem finalCS = camCS.applyRotation(_camRM);
      nextCamera.setCameraCoordinateSystem(finalCS);

      //Updating location
      if (_updateLocation) {

         final IDeviceLocation loc = IDeviceLocation.instance();

         boolean isTracking = loc.isTrackingLocation();
         if (!isTracking) {
            isTracking = loc.startTrackingLocation();
         }

         if (isTracking) {
            final Geodetic3D g = loc.getLocation();
            if (!g.isNan()) {

               //Changing current location
               if (_locationModifier == null) {
                  setPositionOnNextCamera(nextCamera, g);
               }
               else {
                  final Geodetic3D g2 = _locationModifier.modify(g);
                  setPositionOnNextCamera(nextCamera, g2);
               }
            }
         }

      }

   }


   @Override
   public boolean onTouchEvent(final G3MEventContext eventContext,
                               final TouchEvent touchEvent,
                               final CameraContext cameraContext) {
      return false;
   }


   @Override
   public void onDown(final G3MEventContext eventContext,
                      final TouchEvent touchEvent,
                      final CameraContext cameraContext) {
   }


   @Override
   public void onMove(final G3MEventContext eventContext,
                      final TouchEvent touchEvent,
                      final CameraContext cameraContext) {
   }


   @Override
   public void onUp(final G3MEventContext eventContext,
                    final TouchEvent touchEvent,
                    final CameraContext cameraContext) {
   }


   public final void setDebugMeshRenderer(final MeshRenderer meshRenderer) {
   }


   public final void onMouseWheel(final G3MEventContext eventContext,
                                  final TouchEvent touchEvent,
                                  final CameraContext cameraContext) {
   }

}
