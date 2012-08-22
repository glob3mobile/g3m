package org.glob3.mobile.generated; 
/**
 * Class to control the camera.
 */
public class Camera
{
  public Camera(Camera that)
  {
	  _width = that._width;
	  _height = that._height;
	  _modelMatrix = new MutableMatrix44D(that._modelMatrix);
	  _projectionMatrix = new MutableMatrix44D(that._projectionMatrix);
	  _modelViewMatrix = new MutableMatrix44D(that._modelViewMatrix);
	  _position = new MutableVector3D(that._position);
	  _center = new MutableVector3D(that._center);
	  _up = new MutableVector3D(that._up);
	  _geodeticCenterOfView = null;
	  _XYZCenterOfView = new MutableVector3D(that._XYZCenterOfView);
	  _frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
	  _frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
	  _halfFrustumInModelCoordinates = (that._halfFrustumInModelCoordinates == null) ? null : new Frustum(that._halfFrustumInModelCoordinates);
	  _halfFrustum = (that._halfFrustum == null) ? null : new Frustum(that._halfFrustum);
	  _logger = that._logger;
	  _dirtyFlags = new CameraDirtyFlags(that._dirtyFlags);
	  _frustumData = new FrustumData(that._frustumData);
	  _planet = that._planet;
	//cleanCachedValues();
  }


  public Camera(int width, int height)
  {
	  _position = new MutableVector3D(0, 0, 0);
	  _center = new MutableVector3D(0, 0, 0);
	  _up = new MutableVector3D(0, 0, 1);
	  _logger = null;
	  _frustum = null;
	  _frustumInModelCoordinates = null;
	  _halfFrustumInModelCoordinates = null;
	  _halfFrustum = null;
	  _dirtyFlags = new CameraDirtyFlags();
	  _XYZCenterOfView = new MutableVector3D(0, 0, 0);
	  _geodeticCenterOfView = null;
	  _frustumData = new FrustumData();
	resizeViewport(width, height);
  }

  public void dispose()
  {

  }

  public final void copyFrom(Camera that)
  {
	_width = that._width;
	_height = that._height;
  
	_modelMatrix = that._modelMatrix;
	_projectionMatrix = that._projectionMatrix;
	_modelViewMatrix = that._modelViewMatrix;
  
	_position = that._position;
	_center = that._center;
	_up = that._up;
  
	_frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
	_frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  
	//_dirtyCachedValues = that._dirtyCachedValues;
	_dirtyFlags = that._dirtyFlags;
  
	_logger = that._logger;
  
	//cleanCachedValues();
  }

  public final void resizeViewport(int width, int height)
  {
	_width = width;
	_height = height;
  
	//cleanCachedValues();
  }


  /*
  void Camera::calculateCachedValues() {
	const FrustumData data = calculateFrustumData();
    
	_projectionMatrix = MutableMatrix44D::createProjectionMatrix(data._left, data._right,
																 data._bottom, data._top,
																 data._znear, data._zfar);
    
	_modelMatrix = MutableMatrix44D::createModelMatrix(_position, _center, _up);
    
    
  //  _modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
    
    
	// compute center of view on planet
  #ifdef C_CODE
	if (_centerOfView) delete _centerOfView;
  #endif
	const Planet *planet = rc->getPlanet();
	const Vector3D centerV = centerOfViewOnPlanet();
	const Geodetic3D centerG = _planet->toGeodetic3D(centerV);
	_centerOfView = new Geodetic3D(centerG);
    
  #ifdef C_CODE
	if (_frustum != NULL) {
	  delete _frustum;
	}
  #endif
	_frustum = new Frustum(data._left, data._right,
						   data._bottom, data._top,
						   data._znear, data._zfar);
  
  #ifdef C_CODE    
	if (_frustumInModelCoordinates != NULL) {
	  delete _frustumInModelCoordinates;
	}
	_frustumInModelCoordinates = _frustum->_frustum->transformedBy_P(_modelMatrix.transposed());(_modelMatrix.transposed());
    
    
  >>>>>>> origin/master
	if (_halfFrustum != NULL) {
	  delete _halfFrustum;
	}
  #endif
	_halfFrustum =  new Frustum(data._left/2, data._right/2,
								data._bottom/2, data._top/2,
								data._znear, data._zfar);
    
  #ifdef C_CODE
	if (_halfFrustumInModelCoordinates != NULL) {
	  delete _halfFrustumInModelCoordinates;
	}
  #endif
	_halfFrustumInModelCoordinates = _halfFrustum->transformedBy_P(_modelMatrix.transposed());
  
  
  }*/
  
  public final void render(RenderContext rc)
  {
	_logger = rc.getLogger();
	/*
	if (_dirtyCachedValues) {
	  calculateCachedValues();
	  _dirtyCachedValues = false;
	}*/
  
	GL gl = rc.getGL();
	gl.setProjection(getProjectionMatrix());
	gl.loadMatrixf(getModelMatrix());
  
	// TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING
	if (false)
	{
	  final MutableMatrix44D inversed = getModelMatrix().inversed();
  
	  final FrustumData data = calculateFrustumData();
	  final Vector3D p0 = new Vector3D(new Vector3D(data._left/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p1 = new Vector3D(new Vector3D(data._left/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p2 = new Vector3D(new Vector3D(data._right/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
	  final Vector3D p3 = new Vector3D(new Vector3D(data._right/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
  
	  float[] vertices = { (float) p0.x(), (float) p0.y(), (float) p0.z(), (float) p1.x(), (float) p1.y(), (float) p1.z(), (float) p2.x(), (float) p2.y(), (float) p2.z(), (float) p3.x(), (float) p3.y(), (float) p3.z()};
	  int[] indices = {0, 1, 2, 3};
  
	  gl.enableVerticesPosition();
	  gl.vertexPointer(3, 0, vertices);
	  gl.lineWidth(2);
	  gl.color(1, 0, 1, 1);
	  gl.drawLineLoop(4, indices);
  
	  gl.lineWidth(1);
	  gl.color(1, 1, 1, 1);
	}
  
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2Ray(const Vector2D& pixel) const
  public final Vector3D pixel2Ray(Vector2D pixel)
  {
	final int px = (int) pixel.x();
	final int py = _height - (int) pixel.y();
	final Vector3D pixel3D = new Vector3D(px, py, 0);
  
	int[] viewport = { 0, 0, _width, _height };
  
	final Vector3D obj = getModelViewMatrix().unproject(pixel3D, viewport);
	if (obj.isNan())
	{
	  return obj;
	}
  
	return obj.sub(_position.asVector3D());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D pixel2PlanetPoint(const Vector2D& pixel) const
  public final Vector3D pixel2PlanetPoint(Vector2D pixel)
  {
	return _planet.closestIntersection(_position.asVector3D(), pixel2Ray(pixel));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D point2Pixel(const Vector3D& point) const
  public final Vector2D point2Pixel(Vector3D point)
  {
	int[] viewport = { 0, 0, _width, _height };
	Vector2D p = getModelViewMatrix().project(point, viewport);
  
	if (p.isNan())
	{
	  return p;
	}
  
	return new Vector2D(p.x(), _height-p.y());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getWidth() const
  public final int getWidth()
  {
	  return _width;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getHeight() const
  public final int getHeight()
  {
	  return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getViewPortRatio() const
  public final float getViewPortRatio()
  {
	return (float) _width / _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getPosition() const
  public final Vector3D getPosition()
  {
	  return _position.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getCenter() const
  public final Vector3D getCenter()
  {
	  return _center.asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getUp() const
  public final Vector3D getUp()
  {
	  return _up.asVector3D();
  }
  public final Geodetic3D getGeodeticCenterOfView()
  {
	  return _getGeodeticCenterOfView();
  }
  public final Vector3D getXYZCenterOfView()
  {
	  return _getXYZCenterOfView().asVector3D();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getViewDirection() const
  public final Vector3D getViewDirection()
  {
	  return _center.sub(_position).asVector3D();
  }


  //Dragging camera
  public final void dragCamera(Vector3D p0, Vector3D p1)
  {
	// compute the rotation axe
	final Vector3D rotationAxis = p0.cross(p1);
  
	// compute the angle
	//const Angle rotationDelta = Angle::fromRadians( - acos(p0.normalized().dot(p1.normalized())) );
	final Angle rotationDelta = Angle.fromRadians(-Math.asin(rotationAxis.length()/p0.length()/p1.length()));
  
	if (rotationDelta.isNan())
	{
	  return;
	}
  
	rotateWithAxis(rotationAxis, rotationDelta);
  }
  public final void rotateWithAxis(Vector3D axis, Angle delta)
  {
	applyTransform(MutableMatrix44D.createRotationMatrix(delta, axis));
  }
  public final void moveForward(double d)
  {
	final Vector3D view = _center.sub(_position).normalized().asVector3D();
	applyTransform(MutableMatrix44D.createTranslationMatrix(view.times(d)));
  }

  //Pivot
  public final void pivotOnCenter(Angle a)
  {
	final Vector3D rotationAxis = _position.sub(_center).asVector3D();
	rotateWithAxis(rotationAxis, a);
  }

  //Rotate
  public final void rotateWithAxisAndPoint(Vector3D axis, Vector3D point, Angle delta)
  {
	final MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(delta, axis, point);
  
	//m.print();
  
	applyTransform(m);
  }

  public final void print()
  {
	if (_logger != null)
	{
	  getModelMatrix().print("Model Matrix", _logger);
	  getProjectionMatrix().print("Projection Matrix", _logger);
	  getModelViewMatrix().print("ModelView Matrix", _logger);
	  _logger.logInfo("Width: %d, Height %d\n", _width, _height);
	}
  }

  public final Frustum getFrustumInModelCoordinates()
  {
	  return getFrustumMC();
  }

  public final Frustum getHalfFrustuminModelCoordinates()
  {
	  return getHalfFrustumMC();
  }

  public final void setPosition(Geodetic3D g3d)
  {
	_position = _planet.toVector3D(g3d).asMutableVector3D();
	_dirtyFlags.setAll(true);
  }


  public final Vector3D getHorizontalVector()
  {
	MutableMatrix44D M = getModelMatrix();
	return new Vector3D(M.get(0), M.get(4), M.get(8));
  }

  public final Angle compute3DAngularDistance(Vector2D pixel0, Vector2D pixel1)
  {
	final Vector3D point0 = pixel2PlanetPoint(pixel0);
	if (point0.isNan())
	{
	  return Angle.nan();
	}
  
	final Vector3D point1 = pixel2PlanetPoint(pixel1);
	if (point1.isNan())
	{
	  return Angle.nan();
	}
  
	return point0.angleBetween(point1);
  }

  public final void initialize(InitializationContext ic)
  {
	_planet = ic.getPlanet();
	_position = new MutableVector3D(_planet.getRadii().maxAxis() * 5, 0, 0);
	_dirtyFlags.setAll(true);
  }


  private int _width;
  private int _height;

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

  private MutableVector3D _position = new MutableVector3D(); // position
  private MutableVector3D _center = new MutableVector3D(); // point where camera is looking at
  private MutableVector3D _up = new MutableVector3D(); // vertical vector



  private ILogger _logger;

  private CameraDirtyFlags _dirtyFlags = new CameraDirtyFlags();

  private void applyTransform(MutableMatrix44D M)
  {
	_position = _position.transformedBy(M, 1.0);
	_center = _center.transformedBy(M, 1.0);
	_up = _up.transformedBy(M, 0.0);
  
	_dirtyFlags.setAll(true);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D centerOfViewOnPlanet() const
  private Vector3D centerOfViewOnPlanet()
  {
	final Vector3D ray = _center.sub(_position).asVector3D();
	return _planet.closestIntersection(_position.asVector3D(), ray);
  }

  // data to compute frustum
  private FrustumData _frustumData = new FrustumData();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrustumData getFrustumData() const
  private FrustumData getFrustumData()
  {
	if (_dirtyFlags._frustumData)
	{
	  _dirtyFlags._frustumData = false;
	  _frustumData = calculateFrustumData();
	}
	return _frustumData;
  }

  // opengl projection matrix 
  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getProjectionMatrix() const
  private MutableMatrix44D getProjectionMatrix()
  {
	if (_dirtyFlags._projectionMatrix)
	{
	  _dirtyFlags._projectionMatrix = false;
	  _projectionMatrix = MutableMatrix44D.createProjectionMatrix(getFrustumData());
	}
	return _projectionMatrix;
  }

  // Model matrix, computed in CPU in double precision
  private MutableMatrix44D _modelMatrix = new MutableMatrix44D();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getModelMatrix() const
  private MutableMatrix44D getModelMatrix()
  {
	if (_dirtyFlags._modelMatrix)
	{
	  _dirtyFlags._modelMatrix = false;
	  _modelMatrix = MutableMatrix44D.createModelMatrix(_position, _center, _up);
	}
	return _modelMatrix;
  }

  // multiplication of model * projection
  private MutableMatrix44D _modelViewMatrix = new MutableMatrix44D();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableMatrix44D getModelViewMatrix() const
  private MutableMatrix44D getModelViewMatrix()
  {
	if (_dirtyFlags._modelViewMatrix)
	{
	  _dirtyFlags._modelViewMatrix = false;
	  _modelViewMatrix = getProjectionMatrix().multiply(getModelMatrix());
	}
	return _modelViewMatrix;
  }

  // intersection of view direction with globe in(x,y,z)
  private MutableVector3D _XYZCenterOfView = new MutableVector3D();
  private MutableVector3D _getXYZCenterOfView()
  {
	if (_dirtyFlags._XYZCenterOfView)
	{
	  _dirtyFlags._XYZCenterOfView = false;
	  _XYZCenterOfView = centerOfViewOnPlanet().asMutableVector3D();
	}
	return _XYZCenterOfView;
  }

  // intersection of view direction with globe in geodetic
  private Geodetic3D _geodeticCenterOfView;
  private Geodetic3D _getGeodeticCenterOfView()
  {
	if (_dirtyFlags._geodeticCenterOfView)
	{
	  _dirtyFlags._geodeticCenterOfView = false;
	  _geodeticCenterOfView = new Geodetic3D(_planet.toGeodetic3D(getXYZCenterOfView()));
	}
	return _geodeticCenterOfView;
  }

  // camera frustum
  private Frustum _frustum;
  private Frustum getFrustum()
  {
	if (_dirtyFlags._frustum)
	{
	  _dirtyFlags._frustum = false;
	  _frustum = new Frustum(getFrustumData());
	}
	return _frustum;
  }

  // frustum in Model coordinates
  private Frustum _frustumInModelCoordinates;
  private Frustum getFrustumMC()
  {
	if (_dirtyFlags._frustumMC)
	{
	  _dirtyFlags._frustumMC = false;
	  _frustumInModelCoordinates = getFrustum().transformedBy_P(getModelMatrix().transposed());
	}
	return _frustumInModelCoordinates;
  }

  private int __temporal_test_for_clipping;

  private Frustum _halfFrustum; // ONLY FOR DEBUG
  private Frustum getHalfFrustum()
  {
	if (_dirtyFlags._halfFrustum)
	{
	  _dirtyFlags._halfFrustum = false;
	  FrustumData data = getFrustumData();
	  _halfFrustum = new Frustum(data._left/2, data._right/2, data._bottom/2, data._top/2, data._znear, data._zfar);
	}
	return _halfFrustum;
  }

  private Frustum _halfFrustumInModelCoordinates; // ONLY FOR DEBUG
  private Frustum getHalfFrustumMC()
  {
	if (_dirtyFlags._halfFrustumMC)
	{
	  _dirtyFlags._halfFrustumMC = false;
	  _halfFrustumInModelCoordinates = getHalfFrustum().transformedBy_P(getModelMatrix().transposed());
	}
	return _halfFrustumInModelCoordinates;
  }




//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrustumData calculateFrustumData() const
  private FrustumData calculateFrustumData()
  {
	// compute znear value
	final double maxRadius = _planet.getRadii().maxAxis();
	final double distanceToPlanetCenter = _position.length();
	final double distanceToSurface = distanceToPlanetCenter - maxRadius;

	double znear;
	if (distanceToSurface > maxRadius/5)
	{
	  znear = maxRadius / 10;
	}
	else if (distanceToSurface > maxRadius/500)
	{
	  znear = maxRadius / 1e4;
	}
	else if (distanceToSurface > maxRadius/2000)
	{
	  znear = maxRadius / 1e5;
	}
	else
	{
	  znear = maxRadius / 1e6 * 3;
	}

	// compute zfar value
	double zfar = 10000 * znear;
	if (zfar > distanceToPlanetCenter)
	{
	  zfar = distanceToPlanetCenter;
	}

	// compute rest of frustum numbers
	final double ratioScreen = (double) _height / _width;
	final double right = 0.3 / ratioScreen * znear;
	final double left = -right;
	final double top = 0.3 * znear;
	final double bottom = -top;

	return new FrustumData(left, right, bottom, top, znear, zfar);
  }


  //void calculateCachedValues();

  /*void cleanCachedValues() {
	_dirtyCachedValues = true;
	//    if (_frustum != NULL) {
	//      delete _frustum;
	//      _frustum = NULL;
	//    }
	if (_frustumInModelCoordinates != NULL) {
#ifdef C_CODE
	  delete _frustumInModelCoordinates;
#endif
	  _frustumInModelCoordinates = NULL;
	}
  }*/


}