package org.glob3.mobile.generated;
//
//  ProjectedGridTileLODTester.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/10/26.
//
//

//
//  ProjectedGridTileLODTester.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/10/26.
//
//




//class Planet;
//class Camera;
//class Angle;


public class ProjectedGridTileLODTester extends TileLODTester
{

  private static class PvtData extends TileData
  {
    private final Vector3D _northWest ;
    private final Vector3D _north ;
    private final Vector3D _northEast ;
    private final Vector3D _west ;
    private final Vector3D _center ;
    private final Vector3D _east ;
    private final Vector3D _southWest ;
    private final Vector3D _south ;
    private final Vector3D _southEast ;

    private static Angle splitLatitude(Tile tile)
    {
      final Sector sector = tile._sector;
      return tile._mercator ? MercatorUtils.calculateSplitLatitude(sector._lower._latitude, sector._upper._latitude) : Angle.midAngle(sector._lower._latitude, sector._upper._latitude);
      /*  */
      /*  */
    }

    private static Angle splitLongitude(Tile tile)
    {
      final Sector sector = tile._sector;
      return Angle.midAngle(sector._lower._longitude, sector._upper._longitude);
    }

    private static boolean isBehind(Vector3D point, Vector3D cameraPosition, Vector3D viewDirection)
    {
      return point.sub(cameraPosition).dot(viewDirection) <= 0;
    }

    private static double squaredPixelDistance(Camera camera, Vector3D point0, Vector3D point1)
    {
      final Vector2F pixel0 = camera.point2Pixel(point0);
      final Vector2F pixel1 = camera.point2Pixel(point1);
      final double dx = pixel1._x - pixel0._x;
      final double dy = pixel1._y - pixel0._y;
      return (dx * dx) + (dy * dy);
    }

    public PvtData(Tile tile, double averageHeight, Planet planet)
    {
       super(DefineConstants.ProjectedGridTLTDataID);
       _northWest = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._upper._latitude, tile._sector._lower._longitude), averageHeight));
       _north = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._upper._latitude, splitLongitude(tile)), averageHeight));
       _northEast = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._upper._latitude, tile._sector._upper._longitude), averageHeight));
       _west = new Vector3D(planet.toCartesian(new Geodetic2D(splitLatitude(tile), tile._sector._lower._longitude), averageHeight));
       _center = new Vector3D(planet.toCartesian(new Geodetic2D(splitLatitude(tile), splitLongitude(tile)), averageHeight));
       _east = new Vector3D(planet.toCartesian(new Geodetic2D(splitLatitude(tile), tile._sector._upper._longitude), averageHeight));
       _southWest = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._lower._latitude, tile._sector._lower._longitude), averageHeight));
       _south = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._lower._latitude, splitLongitude(tile)), averageHeight));
       _southEast = new Vector3D(planet.toCartesian(new Geodetic2D(tile._sector._lower._latitude, tile._sector._upper._longitude), averageHeight));
    }

    public final boolean evaluate(Camera camera, double texHeightSquared, double texWidthSquared)
    {
      final Vector3D cameraPosition = camera.getCartesianPosition();
      final Vector3D viewDirection = camera.getViewDirection();
    
      // a tile reaching behind the eye is as close as a tile can be
      if (isBehind(_northWest, cameraPosition, viewDirection) || isBehind(_north, cameraPosition, viewDirection) || isBehind(_northEast, cameraPosition, viewDirection) || isBehind(_west, cameraPosition, viewDirection) || isBehind(_center, cameraPosition, viewDirection) || isBehind(_east, cameraPosition, viewDirection) || isBehind(_southWest, cameraPosition, viewDirection) || isBehind(_south, cameraPosition, viewDirection) || isBehind(_southEast, cameraPosition, viewDirection))
      {
        return false;
      }
    
      // every segment spans half the tile, so half the texture
      final double halfTexWidthSquared = texWidthSquared / 4;
      final double halfTexHeightSquared = texHeightSquared / 4;
    
      if ((squaredPixelDistance(camera, _northWest, _north) > halfTexWidthSquared) || (squaredPixelDistance(camera, _north, _northEast) > halfTexWidthSquared) || (squaredPixelDistance(camera, _west, _center) > halfTexWidthSquared) || (squaredPixelDistance(camera, _center, _east) > halfTexWidthSquared) || (squaredPixelDistance(camera, _southWest, _south) > halfTexWidthSquared) || (squaredPixelDistance(camera, _south, _southEast) > halfTexWidthSquared))
      {
        return false;
      }
    
      if ((squaredPixelDistance(camera, _northWest, _west) > halfTexHeightSquared) || (squaredPixelDistance(camera, _west, _southWest) > halfTexHeightSquared) || (squaredPixelDistance(camera, _north, _center) > halfTexHeightSquared) || (squaredPixelDistance(camera, _center, _south) > halfTexHeightSquared) || (squaredPixelDistance(camera, _northEast, _east) > halfTexHeightSquared) || (squaredPixelDistance(camera, _east, _southEast) > halfTexHeightSquared))
      {
        return false;
      }
    
      return true;
    }
  }

  private final double _thresholdFactorSquared;

  private ProjectedGridTileLODTester.PvtData getData(Tile tile, G3MRenderContext rc)
  {
    PvtData data = (PvtData) tile.getData(DefineConstants.ProjectedGridTLTDataID);
    if (data == null)
    {
      final double averageHeight = tile.getTileTessellatorMeshData()._averageHeight;
      data = new PvtData(tile, averageHeight, rc.getPlanet());
      tile.setData(data);
    }
    return data;
  }


  // thresholdFactor scales the allowed projected size: 1 is the texture resolution, greater asks for fewer tiles
  public ProjectedGridTileLODTester(double thresholdFactor)
  {
     _thresholdFactorSquared = thresholdFactor * thresholdFactor;
  }

  public void dispose()
  {
    super.dispose();
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    return getData(tile, rc).evaluate(rc.getCurrentCamera(), prc._texHeightSquared * _thresholdFactorSquared, prc._texWidthSquared * _thresholdFactorSquared);
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    // the points were placed at the average height of the previous mesh
    tile.clearDataWithID(DefineConstants.ProjectedGridTLTDataID);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {

  }

  public final void renderStarted()
  {

  }

}