package org.glob3.mobile.generated;import java.util.*;

public class PlanetTileTessellator extends TileTessellator
{
  private final boolean _skirted;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Sector _renderedSector;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Sector _renderedSector = new internal();
//#endif

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2S calculateResolution(const PlanetRenderContext* prc, const Tile* tile, const Sector& renderedSector) const
  private Vector2S calculateResolution(PlanetRenderContext prc, Tile tile, Sector renderedSector)
  {
	Sector sector = tile._sector;
	final Vector2S resolution = prc._layerTilesRenderParameters._tileMeshResolution;
  
	final double latRatio = sector._deltaLatitude._degrees / renderedSector._deltaLatitude._degrees;
	final double lonRatio = sector._deltaLongitude._degrees / renderedSector._deltaLongitude._degrees;
  
	final IMathUtils mu = IMathUtils.instance();
  
	short resX = (short) mu.ceil((resolution._x / lonRatio));
	if (resX < 2)
	{
	  resX = 2;
	}
  
	short resY = (short) mu.ceil((resolution._y / latRatio));
	if (resY < 2)
	{
	  resY = 2;
	}
  
	final Vector2S meshRes = new Vector2S(resX, resY);
	return meshRes;
  
  
	//  return rawResolution;
  
	//  /* testing for dynamic latitude-resolution */
	//  const double cos = sector._center._latitude.cosinus();
	//
	//  int resolutionY = (int) (rawResolution._y * cos);
	//  if (resolutionY < 8) {
	//    resolutionY = 8;
	//  }
	//
	//  int resolutionX = (int) (rawResolution._x * cos);
	//  if (resolutionX < 8) {
	//    resolutionX = 8;
	//  }
	//
	//  return Vector2I(resolutionX, resolutionY);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean needsEastSkirt(const Sector& tileSector) const
  private boolean needsEastSkirt(Sector tileSector)
  {
	if (_renderedSector == null)
	{
	  return true;
	}
	return _renderedSector._upper._longitude.greaterThan(tileSector._upper._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean needsNorthSkirt(const Sector& tileSector) const
  private boolean needsNorthSkirt(Sector tileSector)
  {
	if (_renderedSector == null)
	{
	  return true;
	}
	return _renderedSector._upper._latitude.greaterThan(tileSector._upper._latitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean needsWestSkirt(const Sector& tileSector) const
  private boolean needsWestSkirt(Sector tileSector)
  {
	if (_renderedSector == null)
	{
	  return true;
	}
	return _renderedSector._lower._longitude.lowerThan(tileSector._lower._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean needsSouthSkirt(const Sector& tileSector) const
  private boolean needsSouthSkirt(Sector tileSector)
  {
	if (_renderedSector == null)
	{
	  return true;
	}
	return _renderedSector._lower._latitude.lowerThan(tileSector._lower._latitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Sector getRenderedSectorForTile(const Tile* tile) const
  private Sector getRenderedSectorForTile(Tile tile)
  {
	if (_renderedSector == null)
	{
	  return tile._sector;
	}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	return tile._sector.intersection(_renderedSector);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	return tile._sector.intersection(_renderedSector);
//#endif
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double createSurfaceVertices(const Vector2S& meshResolution, const Sector& meshSector, const ElevationData* elevationData, float verticalExaggeration, FloatBufferBuilderFromGeodetic* vertices, TileTessellatorMeshData& data) const
  private double createSurfaceVertices(Vector2S meshResolution, Sector meshSector, ElevationData elevationData, float verticalExaggeration, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<TileTessellatorMeshData> data)
  {
  
	final IMathUtils mu = IMathUtils.instance();
	double minElevation = mu.maxDouble();
	double maxElevation = mu.minDouble();
	double averageElevation = 0;
  
	for (int j = 0; j < meshResolution._y; j++)
	{
	  final double v = (double) j / (meshResolution._y - 1);
  
	  for (int i = 0; i < meshResolution._x; i++)
	  {
		final double u = (double) i / (meshResolution._x - 1);
		final Geodetic2D position = meshSector.getInnerPoint(u, v);
		double elevation = 0;
  
		if (elevationData != null)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double rawElevation = elevationData->getElevationAt(position);
		  final double rawElevation = elevationData.getElevationAt(new Geodetic2D(position));
  
		  elevation = (rawElevation != rawElevation)? 0 : rawElevation * verticalExaggeration;
  
		  //MIN
		  if (elevation < minElevation)
		  {
			minElevation = elevation;
		  }
  
		  //MAX
		  if (elevation > maxElevation)
		  {
			maxElevation = elevation;
		  }
  
		  //AVERAGE
		  averageElevation += elevation;
		}
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(position, elevation);
		vertices.add(new Geodetic2D(position), elevation);
	  }
	}
  
	if (minElevation == mu.maxDouble())
	{
	  minElevation = 0;
	}
	if (maxElevation == mu.minDouble())
	{
	  maxElevation = 0;
	}
  
	data.argvalue._minHeight = minElevation;
	data.argvalue._maxHeight = maxElevation;
	data.argvalue._averageHeight = averageElevation / (meshResolution._x * meshResolution._y);
  
	return minElevation;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double createSurface(const Sector& tileSector, const Sector& meshSector, const Vector2S& meshResolution, const ElevationData* elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic* vertices, ShortBufferBuilder& indices, FloatBufferBuilderFromCartesian2D& textCoords, TileTessellatorMeshData& data) const
  private double createSurface(Sector tileSector, Sector meshSector, Vector2S meshResolution, ElevationData elevationData, float verticalExaggeration, boolean mercator, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<ShortBufferBuilder> indices, tangible.RefObject<FloatBufferBuilderFromCartesian2D> textCoords, tangible.RefObject<TileTessellatorMeshData> data)
  {
  
	//VERTICES///////////////////////////////////////////////////////////////
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double minElevation = createSurfaceVertices(Vector2S(meshResolution._x, meshResolution._y), meshSector, elevationData, verticalExaggeration, vertices, data);
	final double minElevation = createSurfaceVertices(new Vector2S(meshResolution._x, meshResolution._y), new Sector(meshSector), elevationData, verticalExaggeration, vertices, data);
  
  
	//TEX COORDINATES////////////////////////////////////////////////////////////////
  
	if (mercator) //Mercator
	{
  
	  final double mercatorLowerGlobalV = MercatorUtils.getMercatorV(tileSector._lower._latitude);
	  final double mercatorUpperGlobalV = MercatorUtils.getMercatorV(tileSector._upper._latitude);
	  final double mercatorDeltaGlobalV = mercatorLowerGlobalV - mercatorUpperGlobalV;
  
	  for (int j = 0; j < meshResolution._y; j++)
	  {
		final double v = (double) j / (meshResolution._y - 1);
  
		for (int i = 0; i < meshResolution._x; i++)
		{
		  final double u = (double) i / (meshResolution._x - 1);
  
		  Angle lat = Angle.linearInterpolation(meshSector._lower._latitude, meshSector._upper._latitude, 1.0 - v);
		  Angle lon = Angle.linearInterpolation(meshSector._lower._longitude, meshSector._upper._longitude, u);
  
		  //U
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double m_u = tileSector.getUCoordinate(lon);
		  final double m_u = tileSector.getUCoordinate(new Angle(lon));
  
		  //V
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double mercatorGlobalV = MercatorUtils::getMercatorV(lat);
		  final double mercatorGlobalV = MercatorUtils.getMercatorV(new Angle(lat));
		  final double m_v = (mercatorGlobalV - mercatorUpperGlobalV) / mercatorDeltaGlobalV;
  
		  textCoords.argvalue.add((float)m_u, (float)m_v);
		}
	  }
  
	} //No mercator
	else
	{
  
	  for (int j = 0; j < meshResolution._y; j++)
	  {
		final double v = (double) j / (meshResolution._y - 1);
		for (int i = 0; i < meshResolution._x; i++)
		{
		  final double u = (double) i / (meshResolution._x - 1);
		  textCoords.argvalue.add((float)u, (float)v);
		}
	  }
  
	}
  
	//INDEX///////////////////////////////////////////////////////////////
	for (short j = 0; j < (meshResolution._y-1); j++)
	{
	  final short jTimesResolution = (short)(j *meshResolution._x);
	  if (j > 0)
	  {
		indices.argvalue.add(jTimesResolution);
	  }
	  for (short i = 0; i < meshResolution._x; i++)
	  {
		indices.argvalue.add((short)(jTimesResolution + i));
		indices.argvalue.add((short)(jTimesResolution + i + meshResolution._x));
	  }
	  indices.argvalue.add((short)(jTimesResolution + 2 *meshResolution._x - 1));
	}
  
	return minElevation;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void createEastSkirt(const Planet* planet, const Sector& tileSector, const Sector& meshSector, const Vector2S& meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic* vertices, ShortBufferBuilder& indices, FloatBufferBuilderFromCartesian2D& textCoords) const
  private void createEastSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<ShortBufferBuilder> indices, tangible.RefObject<FloatBufferBuilderFromCartesian2D> textCoords)
  {
  
	//VERTICES///////////////////////////////////////////////////////////////
	final short firstSkirtVertex = (short)(vertices.size() / 3);
  
	final short southEastCorner = (short)((meshResolution._x * meshResolution._y) - 1);
  
	short skirtIndex = firstSkirtVertex;
	short surfaceIndex = southEastCorner;
  
	// east side
	for (short j = (short)(meshResolution._y-1); j >= 0; j--)
	{
	  final double x = 1;
	  final double y = (double)j/(meshResolution._y-1);
	  final Geodetic2D g = meshSector.getInnerPoint(x, y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(g, skirtHeight);
	  vertices.add(new Geodetic2D(g), skirtHeight);
  
	  //TEXTURE COORDS/////////////////////////////
	  Vector2D uv = textCoords.argvalue.getVector2D(surfaceIndex);
	  textCoords.argvalue.add(uv);
  
	  //INDEX///////////////////////////////////////////////////////////////
	  indices.argvalue.add(surfaceIndex);
	  indices.argvalue.add(skirtIndex);
  
	  skirtIndex++;
	  surfaceIndex -= meshResolution._x;
	}
	//Short casts are needed due to widening primitive conversions in java
	//http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
	indices.argvalue.add((short)(surfaceIndex + meshResolution._x));
	indices.argvalue.add((short)(surfaceIndex + meshResolution._x));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void createNorthSkirt(const Planet* planet, const Sector& tileSector, const Sector& meshSector, const Vector2S& meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic* vertices, ShortBufferBuilder& indices, FloatBufferBuilderFromCartesian2D& textCoords) const
  private void createNorthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<ShortBufferBuilder> indices, tangible.RefObject<FloatBufferBuilderFromCartesian2D> textCoords)
  {
  
	//VERTICES///////////////////////////////////////////////////////////////
	final short firstSkirtVertex = (short)(vertices.size() / 3);
	final short northEastCorner = (short)(meshResolution._x - 1);
  
	short skirtIndex = firstSkirtVertex;
	short surfaceIndex = northEastCorner;
  
	indices.argvalue.add(surfaceIndex);
  
	for (short i = (short)(meshResolution._x-1); i >= 0; i--)
	{
	  final double x = (double)i/(meshResolution._x-1);
	  final double y = 0;
	  final Geodetic2D g = meshSector.getInnerPoint(x, y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(g, skirtHeight);
	  vertices.add(new Geodetic2D(g), skirtHeight);
  
	  //TEXTURE COORDS/////////////////////////////
	  Vector2D uv = textCoords.argvalue.getVector2D(surfaceIndex);
	  textCoords.argvalue.add(uv);
  
	  //INDEX///////////////////////////////////////////////////////////////
	  indices.argvalue.add(surfaceIndex);
	  indices.argvalue.add(skirtIndex);
  
	  skirtIndex++;
	  surfaceIndex -= 1;
	}
  
	indices.argvalue.add((short)(surfaceIndex + 1));
	indices.argvalue.add((short)(surfaceIndex + 1));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void createWestSkirt(const Planet* planet, const Sector& tileSector, const Sector& meshSector, const Vector2S& meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic* vertices, ShortBufferBuilder& indices, FloatBufferBuilderFromCartesian2D& textCoords) const
  private void createWestSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<ShortBufferBuilder> indices, tangible.RefObject<FloatBufferBuilderFromCartesian2D> textCoords)
  {
  
	//VERTICES///////////////////////////////////////////////////////////////
	final short firstSkirtVertex = (short)(vertices.size() / 3);
  
	final short northWestCorner = (short)0;
  
	short skirtIndex = firstSkirtVertex;
	short surfaceIndex = northWestCorner;
  
	indices.argvalue.add(surfaceIndex);
  
	for (short j = 0; j < meshResolution._y; j++)
	{
	  final double x = 0;
	  final double y = (double)j/(meshResolution._y-1);
	  final Geodetic2D g = meshSector.getInnerPoint(x, y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(g, skirtHeight);
	  vertices.add(new Geodetic2D(g), skirtHeight);
  
	  //TEXTURE COORDS/////////////////////////////
	  Vector2D uv = textCoords.argvalue.getVector2D(surfaceIndex);
	  textCoords.argvalue.add(uv);
  
	  //INDEX///////////////////////////////////////////////////////////////
	  indices.argvalue.add(surfaceIndex);
	  indices.argvalue.add(skirtIndex);
  
	  skirtIndex++;
	  surfaceIndex += meshResolution._x;
	}
  
	indices.argvalue.add((short)(surfaceIndex - meshResolution._x));
	indices.argvalue.add((short)(surfaceIndex - meshResolution._x));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void createSouthSkirt(const Planet* planet, const Sector& tileSector, const Sector& meshSector, const Vector2S& meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic* vertices, ShortBufferBuilder& indices, FloatBufferBuilderFromCartesian2D& textCoords) const
  private void createSouthSkirt(Planet planet, Sector tileSector, Sector meshSector, Vector2S meshResolution, double skirtHeight, FloatBufferBuilderFromGeodetic vertices, tangible.RefObject<ShortBufferBuilder> indices, tangible.RefObject<FloatBufferBuilderFromCartesian2D> textCoords)
  {
  
	//VERTICES///////////////////////////////////////////////////////////////
	final short firstSkirtVertex = (short)(vertices.size() / 3);
  
	final short southWestCorner = (short)(meshResolution._x * (meshResolution._y-1));
  
	short skirtIndex = firstSkirtVertex;
	short surfaceIndex = southWestCorner;
  
	indices.argvalue.add(surfaceIndex);
  
	for (short i = 0; i < meshResolution._x; i++)
	{
	  final double x = (double)i/(meshResolution._x-1);
	  final double y = 1;
	  final Geodetic2D g = meshSector.getInnerPoint(x, y);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(g, skirtHeight);
	  vertices.add(new Geodetic2D(g), skirtHeight);
  
	  //TEXTURE COORDS/////////////////////////////
	  Vector2D uv = textCoords.argvalue.getVector2D(surfaceIndex);
	  textCoords.argvalue.add((float)uv._x, (float)uv._y);
  
	  //INDEX///////////////////////////////////////////////////////////////
	  indices.argvalue.add(surfaceIndex);
	  indices.argvalue.add(skirtIndex++);
	  surfaceIndex += 1;
	}
  
	indices.argvalue.add((short)(surfaceIndex - 1));
	indices.argvalue.add((short)(surfaceIndex - 1));
  }

  private static double skirtDepthForSector(Planet planet, Sector sector)
  {
  
	final Vector3D se = planet.toCartesian(sector.getSE());
	final Vector3D nw = planet.toCartesian(sector.getNW());
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double diagonalLength = nw.sub(se).length();
	final double diagonalLength = nw.sub(new Vector3D(se)).length();
	final double sideLength = diagonalLength * 0.70710678118;
	//0.707 = 1 / SQRT(2) -> diagonalLength => estimated side length
	return sideLength / 20.0;
  }


  public PlanetTileTessellator(boolean skirted, Sector sector)
  {
	  _skirted = skirted;
	  _renderedSector = sector.isEquals(Sector.fullSphere())? null : new Sector(sector);
  }

  public void dispose()
  {
	if (_renderedSector != null)
		_renderedSector.dispose();
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2S getTileMeshResolution(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
  public final Vector2S getTileMeshResolution(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
	Sector sector = getRenderedSectorForTile(tile);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return calculateResolution(prc, tile, sector);
	return calculateResolution(prc, tile, new Sector(sector));
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createTileMesh(const G3MRenderContext* rc, const PlanetRenderContext* prc, Tile* tile, const ElevationData* elevationData, TileTessellatorMeshData& data) const
  public final Mesh createTileMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, ElevationData elevationData, tangible.RefObject<TileTessellatorMeshData> data)
  {
  
	final Sector tileSector = tile._sector;
	final Sector meshSector = getRenderedSectorForTile(tile);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector2S meshResolution = calculateResolution(prc, tile, meshSector);
	final Vector2S meshResolution = calculateResolution(prc, tile, new Sector(meshSector));
  
	final Planet planet = rc.getPlanet();
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, meshSector._center);
	ShortBufferBuilder indices = new ShortBufferBuilder();
	FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
	tangible.RefObject<ShortBufferBuilder> tempRef_indices = new tangible.RefObject<ShortBufferBuilder>(indices);
	tangible.RefObject<FloatBufferBuilderFromCartesian2D> tempRef_textCoords = new tangible.RefObject<FloatBufferBuilderFromCartesian2D>(textCoords);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double minElevation = createSurface(tileSector, meshSector, meshResolution, elevationData, prc->_verticalExaggeration, tile->_mercator, vertices, indices, *textCoords, data);
	final double minElevation = createSurface(new Sector(tileSector), new Sector(meshSector), new Vector2S(meshResolution), elevationData, prc._verticalExaggeration, tile._mercator, vertices, tempRef_indices, tempRef_textCoords, data);
	indices = tempRef_indices.argvalue;
	textCoords = tempRef_textCoords.argvalue;
  
	if (_skirted)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double relativeSkirtHeight = minElevation - skirtDepthForSector(planet, tileSector);
	  final double relativeSkirtHeight = minElevation - skirtDepthForSector(planet, new Sector(tileSector));
  
	  double absoluteSkirtHeight = 0;
	  if (_renderedSector != null)
	  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
		absoluteSkirtHeight = -skirtDepthForSector(planet, _renderedSector);
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
		absoluteSkirtHeight = -skirtDepthForSector(planet, _renderedSector);
//#endif
	  }
  
	  tangible.RefObject<ShortBufferBuilder> tempRef_indices2 = new tangible.RefObject<ShortBufferBuilder>(indices);
	  tangible.RefObject<FloatBufferBuilderFromCartesian2D> tempRef_textCoords2 = new tangible.RefObject<FloatBufferBuilderFromCartesian2D>(textCoords);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: createEastSkirt(planet, tileSector, meshSector, meshResolution, needsEastSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, *textCoords);
	  createEastSkirt(planet, new Sector(tileSector), new Sector(meshSector), new Vector2S(meshResolution), needsEastSkirt(new Sector(tileSector)) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, tempRef_indices2, tempRef_textCoords2);
	  indices = tempRef_indices2.argvalue;
	  textCoords = tempRef_textCoords2.argvalue;
  
	  tangible.RefObject<ShortBufferBuilder> tempRef_indices3 = new tangible.RefObject<ShortBufferBuilder>(indices);
	  tangible.RefObject<FloatBufferBuilderFromCartesian2D> tempRef_textCoords3 = new tangible.RefObject<FloatBufferBuilderFromCartesian2D>(textCoords);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: createNorthSkirt(planet, tileSector, meshSector, meshResolution, needsNorthSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, *textCoords);
	  createNorthSkirt(planet, new Sector(tileSector), new Sector(meshSector), new Vector2S(meshResolution), needsNorthSkirt(new Sector(tileSector)) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, tempRef_indices3, tempRef_textCoords3);
	  indices = tempRef_indices3.argvalue;
	  textCoords = tempRef_textCoords3.argvalue;
  
	  tangible.RefObject<ShortBufferBuilder> tempRef_indices4 = new tangible.RefObject<ShortBufferBuilder>(indices);
	  tangible.RefObject<FloatBufferBuilderFromCartesian2D> tempRef_textCoords4 = new tangible.RefObject<FloatBufferBuilderFromCartesian2D>(textCoords);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: createWestSkirt(planet, tileSector, meshSector, meshResolution, needsWestSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, *textCoords);
	  createWestSkirt(planet, new Sector(tileSector), new Sector(meshSector), new Vector2S(meshResolution), needsWestSkirt(new Sector(tileSector)) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, tempRef_indices4, tempRef_textCoords4);
	  indices = tempRef_indices4.argvalue;
	  textCoords = tempRef_textCoords4.argvalue;
  
	  tangible.RefObject<ShortBufferBuilder> tempRef_indices5 = new tangible.RefObject<ShortBufferBuilder>(indices);
	  tangible.RefObject<FloatBufferBuilderFromCartesian2D> tempRef_textCoords5 = new tangible.RefObject<FloatBufferBuilderFromCartesian2D>(textCoords);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: createSouthSkirt(planet, tileSector, meshSector, meshResolution, needsSouthSkirt(tileSector) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, indices, *textCoords);
	  createSouthSkirt(planet, new Sector(tileSector), new Sector(meshSector), new Vector2S(meshResolution), needsSouthSkirt(new Sector(tileSector)) ? relativeSkirtHeight : absoluteSkirtHeight, vertices, tempRef_indices5, tempRef_textCoords5);
	  indices = tempRef_indices5.argvalue;
	  textCoords = tempRef_textCoords5.argvalue;
	}
  
	//Storing textCoords in Tile
	tile.setTessellatorData(new PlanetTileTessellatorData(textCoords));
  
	IFloatBuffer verticesB = vertices.create();
	IShortBuffer indicesB = indices.create();
	IFloatBuffer normals = null;
	///#warning Testing_Terrain_Normals;
	//  IFloatBuffer* normals = NormalsUtils::createTriangleStripSmoothNormals(verticesB, indicesB);
  
	Mesh result = new IndexedGeometryMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), verticesB, true, normals, true, indicesB, true);
  
	if (vertices != null)
		vertices.dispose();
  
	return result;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createTileDebugMesh(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
  public final Mesh createTileDebugMesh(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
	final Sector meshSector = getRenderedSectorForTile(tile);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector2S meshResolution = calculateResolution(prc, tile, meshSector);
	final Vector2S meshResolution = calculateResolution(prc, tile, new Sector(meshSector));
  
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(rc.getPlanet());
	TileTessellatorMeshData data = new TileTessellatorMeshData();
	tangible.RefObject<TileTessellatorMeshData> tempRef_data = new tangible.RefObject<TileTessellatorMeshData>(data);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: createSurfaceVertices(meshResolution, meshSector, tile->getElevationData(), prc->_verticalExaggeration, vertices, data);
	createSurfaceVertices(new Vector2S(meshResolution), new Sector(meshSector), tile.getElevationData(), prc._verticalExaggeration, vertices, tempRef_data);
	data = tempRef_data.argvalue;
  
	//INDEX OF BORDER///////////////////////////////////////////////////////////////
	ShortBufferBuilder indicesBorder = new ShortBufferBuilder();
	for (short j = 0; j < meshResolution._x; j++)
	{
	  indicesBorder.add(j);
	}
  
	for (short i = 2; i < meshResolution._y+1; i++)
	{
	  indicesBorder.add((short)((i * meshResolution._x)-1));
	}
  
	for (short j = (short)(meshResolution._x *meshResolution._y-2); j >= (meshResolution._x*(meshResolution._y-1)); j--)
	{
	  indicesBorder.add(j);
	}
  
	for (short j = (short)(meshResolution._x*(meshResolution._y-1)-meshResolution._x); j >= 0; j-=meshResolution._x)
	{
	  indicesBorder.add(j);
	}
  
	//INDEX OF GRID
	ShortBufferBuilder indicesGrid = new ShortBufferBuilder();
	for (short i = 0; i < meshResolution._y-1; i++)
	{
	  short rowOffset = (short)(i * meshResolution._x);
  
	  for (short j = 0; j < meshResolution._x; j++)
	  {
		indicesGrid.add((short)(rowOffset + j));
		indicesGrid.add((short)(rowOffset + j+meshResolution._x));
	  }
	  for (short j = (short)((2 *meshResolution._x)-1); j >= meshResolution._x; j--)
	  {
		indicesGrid.add((short)(rowOffset + j));
	  }
  
	}
  
	final Color levelColor = Color.blue().wheelStep(5, tile._level % 5);
	final float gridLineWidth = tile.isElevationDataSolved() || (tile.getElevationData() == null) ? 1.0f : 3.0f;
  
  
	IndexedMesh border = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesBorder.create(), true, 2.0f, 1.0f, Color.newFromRGBA(1.0f, 0.0f, 0.0f, 1.0f), null, 1.0f, false, null, true, 1.0f, 1.0f);
  
	IndexedMesh grid = new IndexedMesh(GLPrimitive.lineStrip(), vertices.getCenter(), vertices.create(), true, indicesGrid.create(), true, gridLineWidth, 1.0f, new Color(levelColor), null, 1.0f, false, null, true, 1.0f, 1.0f);
  
	if (vertices != null)
		vertices.dispose();
  
	CompositeMesh c = new CompositeMesh();
	c.addMesh(grid);
	c.addMesh(border);
  
	return c;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createTextCoords(const Vector2S& rawResolution, const Tile* tile) const
  public final IFloatBuffer createTextCoords(Vector2S rawResolution, Tile tile)
  {
  
	PlanetTileTessellatorData data = (PlanetTileTessellatorData) tile.getTessellatorData();
	if (data == null || data._textCoords == null)
	{
	  ILogger.instance().logError("Logic error on PlanetTileTessellator::createTextCoord");
	  return null;
	}
	return data._textCoords.create();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector2F getTextCoord(const Tile* tile, const Angle& latitude, const Angle& longitude) const
  public final Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude)
  {
	final Sector sector = tile._sector;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector2F linearUV = sector.getUVCoordinatesF(latitude, longitude);
	final Vector2F linearUV = sector.getUVCoordinatesF(new Angle(latitude), new Angle(longitude));
	if (!tile._mercator)
	{
	  return linearUV;
	}
  
	final double lowerGlobalV = MercatorUtils.getMercatorV(sector._lower._latitude);
	final double upperGlobalV = MercatorUtils.getMercatorV(sector._upper._latitude);
	final double deltaGlobalV = lowerGlobalV - upperGlobalV;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double globalV = MercatorUtils::getMercatorV(latitude);
	final double globalV = MercatorUtils.getMercatorV(new Angle(latitude));
	final double localV = (globalV - upperGlobalV) / deltaGlobalV;
  
	return new Vector2F(linearUV._x, (float) localV);
  }

  public final void setRenderedSector(Sector sector)
  {
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: if (_renderedSector == null || !_renderedSector->isEquals(sector))
	if (_renderedSector == null || !_renderedSector.isEquals(new Sector(sector)))
	{
	  if (_renderedSector != null)
		  _renderedSector.dispose();

	  if (sector.isEquals(Sector.fullSphere()))
	  {
		_renderedSector = null;
	  }
	  else
	  {
		_renderedSector = new Sector(sector);
	  }
	}
  }

}
