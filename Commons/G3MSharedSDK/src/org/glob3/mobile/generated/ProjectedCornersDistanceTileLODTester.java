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




//class BoundingVolume;
//class Planet;
//class Camera;


public class ProjectedCornersDistanceTileLODTester extends TileLODTesterResponder
{

  protected static class PCDTesterData extends TileLODTesterData
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


    public BoundingVolume _bvol;

    public PCDTesterData(Tile tile, double mediumHeight, Planet planet)
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
    
      //Computing Bounding Volume
    
      final Mesh mesh = tile.getCurrentTessellatorMesh();
      if (mesh == null)
      {
        ILogger.instance().logError("Problem computing BVolume in ProjectedCornersDistanceTileLODTesterData");
        _bvol = null;
      }
      else
      {
        _bvol = mesh.getBoundingVolume(); //BV is deleted by mesh
      }
    
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

  protected final void _onTileHasChangedMesh(Tile tile)
  {
    //Recomputing data when tile changes tessellator mesh
    tile.setDataForLODTester(_id, null);
  }

  protected final ProjectedCornersDistanceTileLODTester.PCDTesterData getData(Tile tile, G3MRenderContext rc)
  {
    PCDTesterData data = (PCDTesterData) tile.getDataForLODTester(_id);
    if (data == null)
    {
      final double mediumHeight = tile.getTessellatorMeshData()._averageHeight;
      data = new PCDTesterData(tile, mediumHeight, rc.getPlanet());
      tile.setDataForLODTester(_id, data);
    }
    return data;
  }

  protected final boolean _meetsRenderCriteria(Tile tile, G3MRenderContext rc, TilesRenderParameters tilesRenderParameters, ITimer lastSplitTimer, double texWidthSquared, double texHeightSquared, long nowInMS)
  {
    PCDTesterData data = getData(tile, rc);
  
    return data.evaluate(rc.getCurrentCamera(), texHeightSquared, texWidthSquared);
  }

  protected final boolean _isVisible(Tile tile, G3MRenderContext rc)
  {
    PCDTesterData data = getData(tile, rc);
    return data._bvol.touchesFrustum(rc.getCurrentCamera().getFrustumInModelCoordinates());
  }

  protected final void _onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
  }


  public ProjectedCornersDistanceTileLODTester(TileLODTester nextTesterRightLOD, TileLODTester nextTesterWrongLOD, TileLODTester nextTesterVisible, TileLODTester nextTesterNotVisible)
  {
     super(nextTesterRightLOD, nextTesterWrongLOD, nextTesterVisible, nextTesterNotVisible);
  }

  public void dispose()
  {
    super.dispose();
  }

}