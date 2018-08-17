package org.glob3.mobile.generated;import java.util.*;

//
//  ProjectedCornersDistanceTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

//
//  ProjectedCornersDistanceTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;


public class ProjectedCornersDistanceTileLODTester extends TileLODTester
{

  private static class PvtData extends TileData
  {
	private static double getSquaredArcSegmentRatio(Vector3D a, Vector3D b)
	{
	  /*
	   Arco = ang * Cuerda / (2 * sen(ang/2))
	   */
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double angleInRadians = Vector3D::angleInRadiansBetween(a, b);
	  final double angleInRadians = Vector3D.angleInRadiansBetween(new Vector3D(a), new Vector3D(b));
	//C++ TO JAVA CONVERTER TODO TASK: The #define macro SIN was defined in alternate ways and cannot be replaced in-line:
	  final double halfAngleSin = SIN(angleInRadians / 2);
	  final double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
	  return (arcSegmentRatio * arcSegmentRatio);
	}

	private double _northArcSegmentRatioSquared;
	private double _southArcSegmentRatioSquared;
	private double _eastArcSegmentRatioSquared;
	private double _westArcSegmentRatioSquared;

	private final Vector3D _northWestPoint = new Vector3D();
	private final Vector3D _northEastPoint = new Vector3D();
	private final Vector3D _southWestPoint = new Vector3D();
	private final Vector3D _southEastPoint = new Vector3D();


	public PvtData(Tile tile, double mediumHeight, Planet planet)
	{
		super(DefineConstants.ProjectedCornersDistanceTLTDataID);
		_northWestPoint = new Vector3D(planet.toCartesian(tile._sector.getNW(), mediumHeight));
		_northEastPoint = new Vector3D(planet.toCartesian(tile._sector.getNE(), mediumHeight));
		_southWestPoint = new Vector3D(planet.toCartesian(tile._sector.getSW(), mediumHeight));
		_southEastPoint = new Vector3D(planet.toCartesian(tile._sector.getSE(), mediumHeight));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normalNW = planet->centricSurfaceNormal(_northWestPoint);
	  final Vector3D normalNW = planet.centricSurfaceNormal(new Vector3D(_northWestPoint));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normalNE = planet->centricSurfaceNormal(_northEastPoint);
	  final Vector3D normalNE = planet.centricSurfaceNormal(new Vector3D(_northEastPoint));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normalSW = planet->centricSurfaceNormal(_southWestPoint);
	  final Vector3D normalSW = planet.centricSurfaceNormal(new Vector3D(_southWestPoint));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D normalSE = planet->centricSurfaceNormal(_southEastPoint);
	  final Vector3D normalSE = planet.centricSurfaceNormal(new Vector3D(_southEastPoint));
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
	  _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(new Vector3D(normalNW), new Vector3D(normalNE));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
	  _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(new Vector3D(normalSW), new Vector3D(normalSE));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _eastArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNE, normalSE);
	  _eastArcSegmentRatioSquared = getSquaredArcSegmentRatio(new Vector3D(normalNE), new Vector3D(normalSE));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: _westArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalSW);
	  _westArcSegmentRatioSquared = getSquaredArcSegmentRatio(new Vector3D(normalNW), new Vector3D(normalSW));
	}

	public final boolean evaluate(Camera camera, double texHeightSquared, double texWidthSquared)
	{
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double distanceInPixelsNorth = camera->getEstimatedPixelDistance(_northWestPoint, _northEastPoint);
	  final double distanceInPixelsNorth = camera.getEstimatedPixelDistance(new Vector3D(_northWestPoint), new Vector3D(_northEastPoint));
	  final double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
	  if (distanceInPixelsSquaredArcNorth > texWidthSquared)
	  {
		return false;
	  }
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double distanceInPixelsSouth = camera->getEstimatedPixelDistance(_southWestPoint, _southEastPoint);
	  final double distanceInPixelsSouth = camera.getEstimatedPixelDistance(new Vector3D(_southWestPoint), new Vector3D(_southEastPoint));
	  final double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
	  if (distanceInPixelsSquaredArcSouth > texWidthSquared)
	  {
		return false;
	  }
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double distanceInPixelsWest = camera->getEstimatedPixelDistance(_northWestPoint, _southWestPoint);
	  final double distanceInPixelsWest = camera.getEstimatedPixelDistance(new Vector3D(_northWestPoint), new Vector3D(_southWestPoint));
	  final double distanceInPixelsSquaredArcWest = (distanceInPixelsWest * distanceInPixelsWest) * _westArcSegmentRatioSquared;
	  if (distanceInPixelsSquaredArcWest > texHeightSquared)
	  {
		return false;
	  }
    
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double distanceInPixelsEast = camera->getEstimatedPixelDistance(_northEastPoint, _southEastPoint);
	  final double distanceInPixelsEast = camera.getEstimatedPixelDistance(new Vector3D(_northEastPoint), new Vector3D(_southEastPoint));
	  final double distanceInPixelsSquaredArcEast = (distanceInPixelsEast * distanceInPixelsEast) * _eastArcSegmentRatioSquared;
	  if (distanceInPixelsSquaredArcEast > texHeightSquared)
	  {
		return false;
	  }
    
	  return true;
	}
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ProjectedCornersDistanceTileLODTester::PvtData* getData(const Tile* tile, const G3MRenderContext* rc) const
  private ProjectedCornersDistanceTileLODTester.PvtData getData(Tile tile, G3MRenderContext rc)
  {
	PvtData data = (PvtData) tile.getData(DefineConstants.ProjectedCornersDistanceTLTDataID);
	if (data == null)
	{
	  final double mediumHeight = tile.getTessellatorMeshData()._averageHeight;
	  data = new PvtData(tile, mediumHeight, rc.getPlanet());
	  tile.setData(data);
	}
	return data;
  }


  public ProjectedCornersDistanceTileLODTester()
  {
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean meetsRenderCriteria(const G3MRenderContext* rc, const PlanetRenderContext* prc, const Tile* tile) const
  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
	return getData(tile, rc).evaluate(rc.getCurrentCamera(), prc._texHeightSquared, prc._texWidthSquared);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void onTileHasChangedMesh(const Tile* tile) const
  public final void onTileHasChangedMesh(Tile tile)
  {

  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void renderStarted() const
  public final void renderStarted()
  {

  }

}
