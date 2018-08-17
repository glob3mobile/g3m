package org.glob3.mobile.generated;//
//  ElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  ElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2I;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Ellipsoid;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Interpolator;

public abstract class ElevationData
{
  private Interpolator _interpolator;
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Interpolator* getInterpolator() const
  private Interpolator getInterpolator()
  {
	if (_interpolator == null)
	{
	  _interpolator = new BilinearInterpolator();
	}
	return _interpolator;
  }


  protected final int _width;
  protected final int _height;
  protected Sector _sector;
  protected final Geodetic2D _resolution = new Geodetic2D();



  public ElevationData(Sector sector, Vector2I extent)
  {
	  _sector = new Sector(sector);
	  _width = extent._x;
	  _height = extent._y;
	  _resolution = new Geodetic2D(sector._deltaLatitude.div(extent._y), sector._deltaLongitude.div(extent._x));
	  _interpolator = null;
  }

  public void dispose()
  {
	if (_interpolator != null)
		_interpolator.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Vector2I getExtent() const
  public Vector2I getExtent()
  {
	return new Vector2I(_width, _height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getExtentWidth() const
  public int getExtentWidth()
  {
	return _width;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int getExtentHeight() const
  public int getExtentHeight()
  {
	return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Geodetic2D getResolution() const
  public final Geodetic2D getResolution()
  {
	return _resolution;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double getElevationAt(int x, int y) const = 0;
  public abstract double getElevationAt(int x, int y);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getElevationAt(const Vector2I& position) const
  public final double getElevationAt(Vector2I position)
  {
	return getElevationAt(position._x, position._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const String description(boolean detailed) const = 0;
  public abstract String description(boolean detailed);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Vector3D getMinMaxAverageElevations() const = 0;
  public abstract Vector3D getMinMaxAverageElevations();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const Planet* planet, float verticalExaggeration, const Geodetic3D& positionOffset, float pointSize) const
  public Mesh createMesh(Planet planet, float verticalExaggeration, Geodetic3D positionOffset, float pointSize)
  {
  
	final Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
	final double minElevation = minMaxAverageElevations._x;
	final double maxElevation = minMaxAverageElevations._y;
	final double deltaElevation = maxElevation - minElevation;
	final double averageElevation = minMaxAverageElevations._z;
  
	ILogger.instance().logInfo("Elevations: average=%f, min=%f max=%f delta=%f", averageElevation, minElevation, maxElevation, deltaElevation);
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(planet);
	FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
	final Geodetic2D positionOffset2D = positionOffset.asGeodetic2D();
  
	for (int x = 0; x < _width; x++)
	{
	  final double u = (double) x / (_width - 1);
  
	  for (int y = 0; y < _height; y++)
	  {
		final double elevation = getElevationAt(x, y);
		if ((elevation != elevation))
		{
		  continue;
		}
  
		final float alpha = (float)((elevation - minElevation) / deltaElevation);
		final float r = alpha;
		final float g = alpha;
		final float b = alpha;
		colors.add(r, g, b, 1);
  
		final double v = 1.0 - ((double) y / (_height - 1));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Geodetic2D position = _sector->getInnerPoint(u, v).add(positionOffset2D);
		final Geodetic2D position = _sector.getInnerPoint(u, v).add(new Geodetic2D(positionOffset2D));
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(position, positionOffset._height + (elevation * verticalExaggeration));
		vertices.add(new Geodetic2D(position), positionOffset._height + (elevation * verticalExaggeration));
  
	  }
	}
  
	final float lineWidth = 1F;
	Color flatColor = null;
  
	Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create(), 0, false);
								  //GLPrimitive::lineStrip(),
  
	if (vertices != null)
		vertices.dispose();
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const Planet* planet, float verticalExaggeration, const Geodetic3D& positionOffset, float pointSize, const Sector& sector, const Vector2I& resolution) const
  public Mesh createMesh(Planet planet, float verticalExaggeration, Geodetic3D positionOffset, float pointSize, Sector sector, Vector2I resolution)
  {
	final Vector3D minMaxAverageElevations = getMinMaxAverageElevations();
	final double minElevation = minMaxAverageElevations._x;
	final double maxElevation = minMaxAverageElevations._y;
	final double deltaElevation = maxElevation - minElevation;
	final double averageElevation = minMaxAverageElevations._z;
  
	ILogger.instance().logInfo("Elevations: average=%f, min=%f max=%f delta=%f", averageElevation, minElevation, maxElevation, deltaElevation);
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, sector._center);
  
	FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
	final Geodetic2D positionOffset2D = positionOffset.asGeodetic2D();
  
	final int width = resolution._x;
	final int height = resolution._y;
	for (int x = 0; x < width; x++)
	{
	  final double u = (double) x / (width - 1);
  
	  for (int y = 0; y < height; y++)
	  {
		final double v = 1.0 - ((double) y / (height - 1));
  
		final Geodetic2D position = sector.getInnerPoint(u, v);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double elevation = getElevationAt(position);
		final double elevation = getElevationAt(new Geodetic2D(position));
		if ((elevation != elevation))
		{
		  continue;
		}
  
		final float alpha = (float)((elevation - minElevation) / deltaElevation);
		final float r = alpha;
		final float g = alpha;
		final float b = alpha;
		colors.add(r, g, b, 1);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(position.add(positionOffset2D), positionOffset._height + (elevation * verticalExaggeration));
		vertices.add(position.add(new Geodetic2D(positionOffset2D)), positionOffset._height + (elevation * verticalExaggeration));
	  }
	}
  
	final float lineWidth = 1F;
	Color flatColor = null;
  
	Mesh result = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor, colors.create(), 0, false);
  
	if (vertices != null)
		vertices.dispose();
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual const Sector getSector() const
  public Sector getSector()
  {
	return _sector;
  }

  public final void setSector(Sector sector)
  {
	  if (_sector != null)
		  _sector.dispose();
	  _sector = new Sector(sector);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean hasNoData() const = 0;
  public abstract boolean hasNoData();


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getElevationAt(const Angle& latitude, const Angle& longitude) const
  public final double getElevationAt(Angle latitude, Angle longitude)
  {
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector2D uv = _sector->getUVCoordinates(latitude, longitude);
	final Vector2D uv = _sector.getUVCoordinates(new Angle(latitude), new Angle(longitude));
	final double u = uv._x;
	final double v = uv._y;
  
	if (u < 0 || u > 1 || v < 0 || v > 1)
	{
	  return NAND;
	}
  
	final double dX = u * (_width - 1);
	final double dY = (1.0 - v) * (_height - 1);
  
	final int x = (int) dX;
	final int y = (int) dY;
	final int nextX = x + 1;
	final int nextY = y + 1;
	final double alphaY = dY - y;
	final double alphaX = dX - x;
  
	double result;
	if (x == dX)
	{
	  if (y == dY)
	  {
		// exact on grid point
		result = getElevationAt(x, y);
	  }
	  else
	  {
		// linear on Y
		final double heightY = getElevationAt(x, y);
		if ((heightY != heightY))
		{
		  return NAND;
		}
  
		final double heightNextY = getElevationAt(x, nextY);
		if ((heightNextY != heightNextY))
		{
		  return NAND;
		}
  
		//result = IMathUtils::instance()->linearInterpolation(heightNextY, heightY, alphaY);
		result = IMathUtils.instance().linearInterpolation(heightY, heightNextY, alphaY);
	  }
	}
	else
	{
	  if (y == dY)
	  {
		// linear on X
		final double heightX = getElevationAt(x, y);
		if ((heightX != heightX))
		{
		  return NAND;
		}
		final double heightNextX = getElevationAt(nextX, y);
		if ((heightNextX != heightNextX))
		{
		  return NAND;
		}
  
		result = IMathUtils.instance().linearInterpolation(heightX, heightNextX, alphaX);
	  }
	  else
	  {
		// bilinear
		final double valueNW = getElevationAt(x, y);
		if ((valueNW != valueNW))
		{
		  return NAND;
		}
		final double valueNE = getElevationAt(nextX, y);
		if ((valueNE != valueNE))
		{
		  return NAND;
		}
		final double valueSE = getElevationAt(nextX, nextY);
		if ((valueSE != valueSE))
		{
		  return NAND;
		}
		final double valueSW = getElevationAt(x, nextY);
		if ((valueSW != valueSW))
		{
		  return NAND;
		}
  
		result = getInterpolator().interpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
	  }
	}
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getElevationAt(const Geodetic2D& position) const
  public final double getElevationAt(Geodetic2D position)
  {
	return getElevationAt(position._latitude, position._longitude);
  }

  //  bool isEquivalentTo(const ElevationData* ed) {
  //    bool equivalent = true;
  //    const int width  = 3;
  //    const int height = 3;
  //    for (int x = 0; x < width; x++) {
  //      const double u = (double) x / (width  - 1);
  //
  //      for (int y = 0; y < height; y++) {
  //        const double v = 1.0 - ( (double) y / (height - 1) );
  //
  //        const Geodetic2D position = _sector.getInnerPoint(u, v);
  //
  //        const double elevation = getElevationAt(position);
  //        const double elevation2 = ed->getElevationAt(position);
  //
  //        if (elevation != elevation2) {
  //          printf("%s -> %f != %f\n", position.description().c_str(), elevation, elevation2);
  //          equivalent = false;
  //        }
  //      }
  //    }
  //    return equivalent;
  //  }

}
