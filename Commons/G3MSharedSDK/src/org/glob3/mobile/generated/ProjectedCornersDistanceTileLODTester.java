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




public class ProjectedCornersDistanceTileLODTester extends TileLODTesterResponder
{

  protected static class ProjectedCornersDistanceTileLODTesterData extends TileLODTesterData
  {

    private double getSquaredArcSegmentRatio(Vector3D a, Vector3D b)
    {
      /*
       Arco = ang * Cuerda / (2 * sen(ang/2))
       */

      final double angleInRadians = Vector3D.angleInRadiansBetween(a, b);
      final double halfAngleSin = java.lang.Math.sin(angleInRadians / 2);
      final double arcSegmentRatio = (halfAngleSin == 0) ? 1 : angleInRadians / (2 * halfAngleSin);
      return (arcSegmentRatio * arcSegmentRatio);
    }

    public double _northArcSegmentRatioSquared;
    public double _southArcSegmentRatioSquared;
    public double _eastArcSegmentRatioSquared;
    public double _westArcSegmentRatioSquared;

    public Vector3D _northWestPoint ;
    public Vector3D _northEastPoint ;
    public Vector3D _southWestPoint ;
    public Vector3D _southEastPoint ;

    public BoundingVolume _bvol;

    public ProjectedCornersDistanceTileLODTesterData(Tile tile, double mediumHeight, Planet planet)
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

  protected final void _onTileHasChangedMesh(int testerLevel, Tile tile)
  {
    //Recomputing data when tile changes tessellator mesh
    tile.setDataForLoDTester(testerLevel, null);
  }

  protected final ProjectedCornersDistanceTileLODTesterData getData(Tile tile, int testerLevel, G3MRenderContext rc)
  {
    ProjectedCornersDistanceTileLODTesterData data = (ProjectedCornersDistanceTileLODTesterData) tile.getDataForLoDTester(testerLevel);
    if (data == null)
    {
      final double mediumHeight = tile.getTessellatorMeshData()._averageHeight;
      data = new ProjectedCornersDistanceTileLODTesterData(tile, mediumHeight, rc.getPlanet());
      tile.setDataForLoDTester(testerLevel, data);
    }
    return data;
  }

  protected final boolean _meetsRenderCriteria(int testerLevel, Tile tile, G3MRenderContext rc)
  {

    if (_texHeightSquared < 0 || _texHeightSquared < 0)
    {
      return true;
    }

    ProjectedCornersDistanceTileLODTesterData data = getData(tile, testerLevel, rc);

    return data.evaluate(rc.getCurrentCamera(), _texHeightSquared, _texWidthSquared);
  }

  protected final boolean _isVisible(int testerLevel, Tile tile, G3MRenderContext rc)
  {
    ProjectedCornersDistanceTileLODTesterData data = getData(tile, testerLevel, rc);
    return data._bvol.touchesFrustum(rc.getCurrentCamera().getFrustumInModelCoordinates());
  }

  protected double _texHeightSquared;
  protected double _texWidthSquared;

  protected final void _onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
    if (ltrp != null)
    {
      _texWidthSquared = ltrp._tileTextureResolution._x * ltrp._tileTextureResolution._x;
      _texHeightSquared = ltrp._tileTextureResolution._y * ltrp._tileTextureResolution._y;
    }
    else
    {
      _texWidthSquared = -1;
      _texHeightSquared = -1;
    }
  }


  public ProjectedCornersDistanceTileLODTester(double textureWidth, double textureHeight, TileLODTester nextTesterRightLoD, TileLODTester nextTesterWrongLoD, TileLODTester nextTesterVisible, TileLODTester nextTesterNotVisible)
  {
     super(nextTesterRightLoD, nextTesterWrongLoD, nextTesterVisible, nextTesterNotVisible);
     _texHeightSquared = textureHeight * textureHeight;
     _texWidthSquared = textureWidth * textureWidth;
  }


  public void dispose()
  {
  }

}