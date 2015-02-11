package org.glob3.mobile.generated; 
//
//  Shape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  Shape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



//class MutableMatrix44D;





//class ShapePendingEffect;
//class GPUProgramState;

public abstract class Shape implements SurfaceElevationListener, EffectTarget
{
  private Geodetic3D _position;
  private AltitudeMode _altitudeMode;

  private Angle _heading;
  private Angle _pitch;
  private Angle _roll;

  private double _scaleX;
  private double _scaleY;
  private double _scaleZ;

  private double _translationX;
  private double _translationY;
  private double _translationZ;

//  const Planet* _planet;

  private MutableMatrix44D _transformMatrix;
  private MutableMatrix44D getTransformMatrix(Planet planet)
  {
    if (_transformMatrix == null)
    {
      _transformMatrix = createTransformMatrix(planet);
      _glState.clearGLFeatureGroup(GLFeatureGroupName.CAMERA_GROUP);
      _glState.addGLFeature(new ModelTransformGLFeature(_transformMatrix.asMatrix44D()), false);
    }
    return _transformMatrix;
  }

  private java.util.ArrayList<ShapePendingEffect> _pendingEffects = new java.util.ArrayList<ShapePendingEffect>();

  private boolean _enable;

  private GLState _glState;

  private SurfaceElevationProvider _surfaceElevationProvider;
  private double _surfaceElevation;

  protected void cleanTransformMatrix()
  {
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    _transformMatrix = null;
  }



  public final MutableMatrix44D createTransformMatrix(Planet planet)
  {
  
    double altitude = _position._height;
    if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
    {
      altitude += _surfaceElevation;
    }
  
    Geodetic3D positionWithSurfaceElevation = new Geodetic3D(_position._latitude, _position._longitude, altitude);
  
    final MutableMatrix44D geodeticTransform = (_position == null) ? MutableMatrix44D.identity() : planet.createGeodeticTransformMatrix(positionWithSurfaceElevation);
  
    final MutableMatrix44D headingRotation = MutableMatrix44D.createRotationMatrix(_heading, Vector3D.downZ());
    final MutableMatrix44D pitchRotation = MutableMatrix44D.createRotationMatrix(_pitch, Vector3D.upX());
    final MutableMatrix44D rollRotation = MutableMatrix44D.createRotationMatrix(_roll, Vector3D.upY());
    final MutableMatrix44D scale = MutableMatrix44D.createScaleMatrix(_scaleX, _scaleY, _scaleZ);
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(_translationX, _translationY, _translationZ);
    final MutableMatrix44D localTransform = headingRotation.multiply(pitchRotation).multiply(rollRotation).multiply(translation).multiply(scale);
  
    return new MutableMatrix44D(geodeticTransform.multiply(localTransform));
  }

  public Shape(Geodetic3D position, AltitudeMode altitudeMode)
  {
     _position = position;
     _altitudeMode = altitudeMode;
     _heading = new Angle(Angle.zero());
     _pitch = new Angle(Angle.zero());
     _roll = new Angle(Angle.zero());
     _scaleX = 1;
     _scaleY = 1;
     _scaleZ = 1;
     _translationX = 0;
     _translationY = 0;
     _translationZ = 0;
     _transformMatrix = null;
     _enable = true;
     _surfaceElevation = 0;
     _glState = new GLState();
     _surfaceElevationProvider = null;

  }

  public void dispose()
  {
    final int pendingEffectsCount = _pendingEffects.size();
    for (int i = 0; i < pendingEffectsCount; i++)
    {
      ShapePendingEffect pendingEffect = _pendingEffects.get(i);
      if (pendingEffect != null)
         pendingEffect.dispose();
    }
  
    if (_position != null)
       _position.dispose();
  
    if (_heading != null)
       _heading.dispose();
    if (_pitch != null)
       _pitch.dispose();
    if (_roll != null)
       _roll.dispose();
  
    if (_transformMatrix != null)
       _transformMatrix.dispose();
  
    _glState._release();
  
    if (_surfaceElevationProvider != null)
    {
      if (!_surfaceElevationProvider.removeListener(this))
      {
        ILogger.instance().logError("Couldn't remove shape as listener of Surface Elevation Provider.");
      }
    }
  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final Angle getHeading()
  {
    return _heading;
  }

  public final Angle getPitch()
  {
    return _pitch;
  }

  public final Angle getRoll()
  {
    return _roll;
  }

//  void setPosition(Geodetic3D* position,
//                   AltitudeMode altitudeMode);


  //void Shape::setPosition(Geodetic3D* position,
  //                        AltitudeMode altitudeMode) {
  //  delete _position;
  //  _position = position;
  //  _altitudeMode = altitudeMode;
  //  cleanTransformMatrix();
  //}
  
  public final void setPosition(Geodetic3D position)
  {
    if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
    {
      throw new RuntimeException("Position change with (_altitudeMode == RELATIVE_TO_GROUND) not supported");
    }
  
    if (_position != null)
       _position.dispose();
    _position = position;
    cleanTransformMatrix();
  }

  public final void addShapeEffect(Effect effect)
  {
    _pendingEffects.add(new ShapePendingEffect(effect, false));
  }

  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position)
  {
     setAnimatedPosition(duration, position, false);
  }
  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position, boolean linearInterpolation)
  {
    Effect effect = new ShapePositionEffect(duration, this, _position, position, linearInterpolation);
    addShapeEffect(effect);
  }

  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position, Angle pitch, Angle heading, Angle roll, boolean linearInterpolation, boolean forceToPositionOnCancel)
  {
     setAnimatedPosition(duration, position, pitch, heading, roll, linearInterpolation, forceToPositionOnCancel, true);
  }
  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position, Angle pitch, Angle heading, Angle roll, boolean linearInterpolation)
  {
     setAnimatedPosition(duration, position, pitch, heading, roll, linearInterpolation, true, true);
  }
  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position, Angle pitch, Angle heading, Angle roll)
  {
     setAnimatedPosition(duration, position, pitch, heading, roll, false, true, true);
  }
  public final void setAnimatedPosition(TimeInterval duration, Geodetic3D position, Angle pitch, Angle heading, Angle roll, boolean linearInterpolation, boolean forceToPositionOnCancel, boolean forceToPositionOnStop)
  {
    Effect effect = new ShapeFullPositionEffect(duration, this, _position, position, _pitch, pitch, _heading, heading, _roll, roll, linearInterpolation, forceToPositionOnCancel, forceToPositionOnStop);
    addShapeEffect(effect);
  }

  public final void setAnimatedPosition(Geodetic3D position)
  {
     setAnimatedPosition(position, false);
  }
  public final void setAnimatedPosition(Geodetic3D position, boolean linearInterpolation)
  {
    setAnimatedPosition(TimeInterval.fromSeconds(3), position, linearInterpolation);
  }

  public final void setHeading(Angle heading)
  {
    _heading = heading;
    cleanTransformMatrix();
  }

  public final void setPitch(Angle pitch)
  {
    _pitch = pitch;
    cleanTransformMatrix();
  }

  public final void setRoll(Angle roll)
  {
    _roll = roll;
    cleanTransformMatrix();
  }

  public final void setScale(double scale)
  {
    setScale(scale, scale, scale);
  }

  public final void setTranslation(Vector3D translation)
  {
    setTranslation(translation._x, translation._y, translation._z);
  }

  public final void setTranslation(double translationX, double translationY, double translationZ)
  {
    _translationX = translationX;
    _translationY = translationY;
    _translationZ = translationZ;
    cleanTransformMatrix();
  }

  public final void setScale(double scaleX, double scaleY, double scaleZ)
  {
    _scaleX = scaleX;
    _scaleY = scaleY;
    _scaleZ = scaleZ;
    cleanTransformMatrix();
  }

  public final void setScale(Vector3D scale)
  {
    setScale(scale._x, scale._y, scale._z);
  }

  public final Vector3D getScale()
  {
    return new Vector3D(_scaleX, _scaleY, _scaleZ);
  }

  public final void setAnimatedScale(TimeInterval duration, double scaleX, double scaleY, double scaleZ)
  {
    Effect effect = new ShapeScaleEffect(duration, this, _scaleX, _scaleY, _scaleZ, scaleX, scaleY, scaleZ);
    addShapeEffect(effect);
  }

  public final void setAnimatedScale(double scaleX, double scaleY, double scaleZ)
  {
    setAnimatedScale(TimeInterval.fromSeconds(1), scaleX, scaleY, scaleZ);
  }

  public final void setAnimatedScale(Vector3D scale)
  {
    setAnimatedScale(scale._x, scale._y, scale._z);
  }

  public final void setAnimatedScale(TimeInterval duration, Vector3D scale)
  {
    setAnimatedScale(duration, scale._x, scale._y, scale._z);
  }

  public final void orbitCamera(TimeInterval duration, double fromDistance, double toDistance, Angle fromAzimuth, Angle toAzimuth, Angle fromAltitude, Angle toAltitude)
  {
    Effect effect = new ShapeOrbitCameraEffect(duration, this, fromDistance, toDistance, fromAzimuth, toAzimuth, fromAltitude, toAltitude);
    _pendingEffects.add(new ShapePendingEffect(effect, true));
  }

  public final boolean isEnable()
  {
    return _enable;
  }

  public final void setEnable(boolean enable)
  {
    _enable = enable;
  }

  public final void render(G3MRenderContext rc, GLState parentGLState, boolean renderNotReadyShapes)
  {
    if (renderNotReadyShapes || isReadyToRender(rc))
    {
      final int pendingEffectsCount = _pendingEffects.size();
      if (pendingEffectsCount > 0)
      {
        EffectsScheduler effectsScheduler = rc.getEffectsScheduler();
        for (int i = 0; i < pendingEffectsCount; i++)
        {
          ShapePendingEffect pendingEffect = _pendingEffects.get(i);
          if (pendingEffect != null)
          {
            EffectTarget target = pendingEffect._targetIsCamera ? rc.getNextCamera().getEffectTarget() : this;
            effectsScheduler.cancelAllEffectsFor(target);
            effectsScheduler.startEffect(pendingEffect._effect, target);
  
            if (pendingEffect != null)
               pendingEffect.dispose();
          }
        }
        _pendingEffects.clear();
      }
  
      getTransformMatrix(rc.getPlanet()); //Applying transform to _glState
      _glState.setParent(parentGLState);
      rawRender(rc, _glState, renderNotReadyShapes);
    }
  }

  public void initialize(G3MContext context)
  {
    if (_altitudeMode == AltitudeMode.RELATIVE_TO_GROUND)
    {
      _surfaceElevationProvider = context.getSurfaceElevationProvider();
      if (_surfaceElevationProvider != null)
      {
        _surfaceElevationProvider.addListener(_position._latitude, _position._longitude, this);
      }
    }
  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void rawRender(G3MRenderContext rc, GLState parentGLState, boolean renderNotReadyShapes);

  public abstract boolean isTransparent(G3MRenderContext rc);

  public final void elevationChanged(Geodetic2D position, double rawElevation, double verticalExaggeration)
  {
  
    if ((rawElevation != rawElevation))
    {
      _surfaceElevation = 0; //USING 0 WHEN NO ELEVATION DATA
    }
    else
    {
      _surfaceElevation = rawElevation * verticalExaggeration;
    }
  
    if (_transformMatrix != null)
       _transformMatrix.dispose();
    _transformMatrix = null;
  }

  public final void elevationChanged(Sector position, ElevationData rawElevationData, double verticalExaggeration) // Without considering vertical exaggeration
  {
  }

  public abstract java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction);


}