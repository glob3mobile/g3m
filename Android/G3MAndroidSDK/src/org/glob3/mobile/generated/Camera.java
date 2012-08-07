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
	  _position = new MutableVector3D(that._position);
	  _center = new MutableVector3D(that._center);
	  _up = new MutableVector3D(that._up);
	  _centerOfView = null;
	  _frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
	  _frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
	  _halfFrustumInModelCoordinates = (that._halfFrustumInModelCoordinates == null) ? null : new Frustum(that._halfFrustumInModelCoordinates);
	  _halfFrustum = (that._halfFrustum == null) ? null : new Frustum(that._halfFrustum);
	  _logger = that._logger;
	  _dirtyCachedValues = that._dirtyCachedValues;
	  _planet = that._planet;
	cleanCachedValues();
  }


  public Camera(Planet planet, int width, int height)
  {
	  _position = new MutableVector3D((planet == null) ? 0 : planet.getRadii().maxAxis() * 5, 0, 0);
	  _center = new MutableVector3D(0, 0, 0);
	  _up = new MutableVector3D(0, 0, 1);
	  _logger = null;
	  _frustum = null;
	  _dirtyCachedValues = true;
	  _frustumInModelCoordinates = null;
	  _halfFrustumInModelCoordinates = null;
	  _halfFrustum = null;
	  _centerOfView = null;
	  _planet = planet;
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
  //  _modelViewMatrix  = that._modelViewMatrix;
  
	_position = that._position;
	_center = that._center;
	_up = that._up;
  
	_frustum = (that._frustum == null) ? null : new Frustum(that._frustum);
	_frustumInModelCoordinates = (that._frustumInModelCoordinates == null) ? null : new Frustum(that._frustumInModelCoordinates);
  
	_dirtyCachedValues = that._dirtyCachedValues;
  
	_logger = that._logger;
  
	cleanCachedValues();
  }

  public final void resizeViewport(int width, int height)
  {
	_width = width;
	_height = height;
  
	cleanCachedValues();
  }

  public final void render(RenderContext rc)
  {
	_logger = rc.getLogger();
  
	if (_dirtyCachedValues)
	{
	  calculateCachedValues(rc);
	  _dirtyCachedValues = false;
	}
  
	GL gl = rc.getGL();
	gl.setProjection(_projectionMatrix);
	gl.loadMatrixf(_modelMatrix);
  
  
  
	// TEMP: TEST TO SEE HALF SIZE FRUSTUM CLIPPING
	if (false)
	{
	  final MutableMatrix44D inversed = _modelMatrix.inversed();
  
	  FrustumData data = calculateFrustumData(rc);
	  Vector3D p0 = new Vector3D(new Vector3D(data._left/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
	  Vector3D p1 = new Vector3D(new Vector3D(data._left/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
	  Vector3D p2 = new Vector3D(new Vector3D(data._right/2, data._bottom/2, -data._znear-10).transformedBy(inversed, 1));
	  Vector3D p3 = new Vector3D(new Vector3D(data._right/2, data._top/2, -data._znear-10).transformedBy(inversed, 1));
  
	  float[] vertices = { (float) p0.x(), (float) p0.y(), (float) p0.z(), (float) p1.x(), (float) p1.y(), (float) p1.z(), (float) p2.x(), (float) p2.y(), (float) p2.z(), (float) p3.x(), (float) p3.y(), (float) p3.z()};
	  int[] indices = {0, 1, 2, 3};
  
  //    GL *gl = rc.getGL();
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
  
	final MutableMatrix44D modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
	int[] viewport = { 0, 0, _width, _height };
  
	final Vector3D obj = modelViewMatrix.unproject(pixel3D, viewport);
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
	final MutableMatrix44D modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
	int[] viewport = { 0, 0, _width, _height };
	final Vector2D p = modelViewMatrix.project(point, viewport);
  
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic3D getCenterOfView() const
  public final Geodetic3D getCenterOfView()
  {
	  return _centerOfView;
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
	final Angle rotationDelta = Angle.fromRadians(- Math.acos(p0.normalized().dot(p1.normalized())));
  
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
	Vector3D view = _center.sub(_position).normalized().asVector3D();
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void print() const
  public final void print()
  {
	if (_logger != null)
	{
	  _modelMatrix.print("Model Matrix", _logger);
	  _projectionMatrix.print("Projection Matrix", _logger);
  //    _modelViewMatrix.print("ModelView Matrix", _logger);
	  _logger.logInfo("Width: %d, Height %d\n", _width, _height);
	}
  }

  public final Frustum getFrustumInModelCoordinates()
  {
	return _frustumInModelCoordinates;
  }

  public final void setPosition(Geodetic3D g3d)
  {
	_position = _planet.toVector3D(g3d).asMutableVector3D();
  }


  public int __temporal_test_for_clipping;
  // TEMP TEST
  public Frustum _halfFrustum; // ONLY FOR DEBUG
  public Frustum _halfFrustumInModelCoordinates;

  public int __to_ask_diego;
  public final void updateModelMatrix()
  {
	  _modelMatrix = MutableMatrix44D.createModelMatrix(_position, _center, _up);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D centerOfViewOnPlanet() const
  public final Vector3D centerOfViewOnPlanet()
  {
	final Vector3D ray = _center.sub(_position).asVector3D();
	return _planet.closestIntersection(_position.asVector3D(), ray);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getHorizontalVector() const
  public final Vector3D getHorizontalVector()
  {
	final MutableMatrix44D M = MutableMatrix44D.createModelMatrix(_position, _center, _up);
	return new Vector3D(M.get(0), M.get(4), M.get(8));
  }

  public final void applyTransform(MutableMatrix44D M)
  {
	_position = _position.transformedBy(M, 1.0);
	_center = _center.transformedBy(M, 1.0);
	_up = _up.transformedBy(M, 0.0);
  
	cleanCachedValues();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle compute3DAngularDistance(const Vector2D& pixel0, const Vector2D& pixel1) const
  public final Angle compute3DAngularDistance(Vector2D pixel0, Vector2D pixel1)
  {
	Vector3D point0 = pixel2PlanetPoint(pixel0);
	if (point0.isNan())
		return Angle.nan();
	Vector3D point1 = pixel2PlanetPoint(pixel1);
	if (point1.isNan())
		return Angle.nan();
	return point0.angleBetween(point1);
  }


  private int _width;
  private int _height;

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

  private MutableMatrix44D _modelMatrix = new MutableMatrix44D(); // Model matrix, computed in CPU in double precision
  private MutableMatrix44D _projectionMatrix = new MutableMatrix44D(); // opengl projection matrix

  private MutableVector3D _position = new MutableVector3D(); // position
  private MutableVector3D _center = new MutableVector3D(); // point where camera is looking at
  private MutableVector3D _up = new MutableVector3D(); // vertical vector
  private Geodetic3D _centerOfView; // intersection of view direction with globe

  private Frustum _frustum;
  private Frustum _frustumInModelCoordinates;


  private ILogger _logger;


  private boolean _dirtyCachedValues;


  private FrustumData calculateFrustumData(RenderContext rc)
  {
	// compute znear value
	final double maxRadius = rc.getPlanet().getRadii().maxAxis();
	final double distanceToPlanetCenter = _position.length();
	final double distanceToSurface = distanceToPlanetCenter - maxRadius;

//    double znear;
//    if (distanceToSurface > maxRadius/5) {
//      znear = maxRadius / 10; 
//    }
//    else if (distanceToSurface > maxRadius/500) {
//      znear = maxRadius / 1e3;
//    }
//    else if (distanceToSurface > maxRadius/2000) {
//      znear = maxRadius / 1e5;
//    }
//    else {
//      znear = maxRadius / 1e6 * 3;
//    }

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


  private void calculateCachedValues(RenderContext rc)
  {
	final FrustumData data = calculateFrustumData(rc);
  
	_projectionMatrix = MutableMatrix44D.createProjectionMatrix(data._left, data._right, data._bottom, data._top, data._znear, data._zfar);
  
	_modelMatrix = MutableMatrix44D.createModelMatrix(_position, _center, _up);
  
  
  //  _modelViewMatrix = _projectionMatrix.multiply(_modelMatrix);
  
  
	// compute center of view on planet
	final Planet planet = rc.getPlanet();
	final Vector3D centerV = centerOfViewOnPlanet();
	final Geodetic3D centerG = planet.toGeodetic3D(centerV);
	_centerOfView = new Geodetic3D(centerG);
  
	_frustum = new Frustum(data._left, data._right, data._bottom, data._top, data._znear, data._zfar);
  
	_frustumInModelCoordinates = _frustum.transformedBy_P(_modelMatrix.transposed());
  
	_halfFrustum = new Frustum(data._left/2, data._right/2, data._bottom/2, data._top/2, data._znear, data._zfar);
  
	_halfFrustumInModelCoordinates = _halfFrustum.transformedBy_P(_modelMatrix.transposed());
  
  
  }

  private void cleanCachedValues()
  {
	_dirtyCachedValues = true;
	//    if (_frustum != NULL) {
	//      delete _frustum;
	//      _frustum = NULL;
	//    }
	if (_frustumInModelCoordinates != null)
	{
	  _frustumInModelCoordinates = null;
	}
  }


}