package org.glob3.mobile.generated; 
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





//class Planet;
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
    
      final double angleInRadians = Vector3D.angleInRadiansBetween(a, b);
      final double halfAngleSin = java.lang.Math.sin(angleInRadians / 2);
      final double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
      return (arcSegmentRatio * arcSegmentRatio);
    }

    private double _northArcSegmentRatioSquared;
    private double _southArcSegmentRatioSquared;
    private double _eastArcSegmentRatioSquared;
    private double _westArcSegmentRatioSquared;

    private final Vector3D _northWestPoint ;
    private final Vector3D _northEastPoint ;
    private final Vector3D _southWestPoint ;
    private final Vector3D _southEastPoint ;


    public PvtData(Tile tile, double mediumHeight, Planet planet)
    {
       super();
       _northWestPoint = new Vector3D(planet.toCartesian(tile._sector.getNW(), mediumHeight));
       _northEastPoint = new Vector3D(planet.toCartesian(tile._sector.getNE(), mediumHeight));
       _southWestPoint = new Vector3D(planet.toCartesian(tile._sector.getSW(), mediumHeight));
       _southEastPoint = new Vector3D(planet.toCartesian(tile._sector.getSE(), mediumHeight));
      final Vector3D normalNW = planet.centricSurfaceNormal(_northWestPoint);
      final Vector3D normalNE = planet.centricSurfaceNormal(_northEastPoint);
      final Vector3D normalSW = planet.centricSurfaceNormal(_southWestPoint);
      final Vector3D normalSE = planet.centricSurfaceNormal(_southEastPoint);
    
      _northArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalNE);
      _southArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalSW, normalSE);
      _eastArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNE, normalSE);
      _westArcSegmentRatioSquared = getSquaredArcSegmentRatio(normalNW, normalSW);
    }

    public final boolean evaluate(Camera camera, double texHeightSquared, double texWidthSquared)
    {
    
      final double distanceInPixelsNorth = camera.getEstimatedPixelDistance(_northWestPoint, _northEastPoint);
      final double distanceInPixelsSouth = camera.getEstimatedPixelDistance(_southWestPoint, _southEastPoint);
      final double distanceInPixelsWest = camera.getEstimatedPixelDistance(_northWestPoint, _southWestPoint);
      final double distanceInPixelsEast = camera.getEstimatedPixelDistance(_northEastPoint, _southEastPoint);
    
      final double distanceInPixelsSquaredArcNorth = (distanceInPixelsNorth * distanceInPixelsNorth) * _northArcSegmentRatioSquared;
      final double distanceInPixelsSquaredArcSouth = (distanceInPixelsSouth * distanceInPixelsSouth) * _southArcSegmentRatioSquared;
      final double distanceInPixelsSquaredArcWest = (distanceInPixelsWest * distanceInPixelsWest) * _westArcSegmentRatioSquared;
      final double distanceInPixelsSquaredArcEast = (distanceInPixelsEast * distanceInPixelsEast) * _eastArcSegmentRatioSquared;
    
      return ((distanceInPixelsSquaredArcNorth <= texHeightSquared) && (distanceInPixelsSquaredArcSouth <= texHeightSquared) && (distanceInPixelsSquaredArcWest <= texWidthSquared) && (distanceInPixelsSquaredArcEast <= texWidthSquared));
    }
  }


  private ProjectedCornersDistanceTileLODTester.PvtData getData(Tile tile, G3MRenderContext rc)
  {
    PvtData data = (PvtData) tile.getData(_id);
    if (data == null)
    {
      final double mediumHeight = tile.getTessellatorMeshData()._averageHeight;
      data = new PvtData(tile, mediumHeight, rc.getPlanet());
      tile.setData(_id, data);
    }
    return data;
  }


  public ProjectedCornersDistanceTileLODTester()
  {
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    return getData(tile, rc).evaluate(rc.getCurrentCamera(), prc._texHeightSquared, prc._texWidthSquared);
  }

  public final void onTileHasChangedMesh(Tile tile)
  {

  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {

  }

  public final void renderStarted()
  {

  }

}