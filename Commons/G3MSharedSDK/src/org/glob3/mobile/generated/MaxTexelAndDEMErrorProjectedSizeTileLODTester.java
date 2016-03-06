package org.glob3.mobile.generated; 
//
//  MaxTexelAndDEMErrorProjectedSizeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

//
//  MaxTexelAndDEMErrorProjectedSizeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//




//class Planet;
//class Camera;


public class MaxTexelAndDEMErrorProjectedSizeTileLODTester extends TileLODTester
{

  private static class PvtData extends TileData
  {

    public final Box _boundingBox;
    public PvtData(Box boundingBox)
    {
       super(DefineConstants.MaxTexelProjectedSizeTLTDataID);
       _boundingBox = boundingBox;
    }
  }

  private final double _maxAllowedPixelsForTexel;
  private final double _maxAllowedPixelsForDEMError;

  private MaxTexelAndDEMErrorProjectedSizeTileLODTester.PvtData createData(Tile tile)
  {
    Box box = tile.createBoundingBox();
    if (box != null)
    {
      PvtData data = new PvtData(box);
      tile.setData(data);
      return data;
    }
    else
    {
      tile.clearDataWithID(DefineConstants.MaxTexelProjectedSizeTLTDataID);
    }
    return null;
  }


  public MaxTexelAndDEMErrorProjectedSizeTileLODTester(double maxAllowedPixelsForTexel, double maxAllowedPixelsForDEMError)
  {
     _maxAllowedPixelsForTexel = maxAllowedPixelsForTexel;
     _maxAllowedPixelsForDEMError = maxAllowedPixelsForDEMError;
  }

  public void dispose()
  {
  }

  public final boolean meetsRenderCriteria(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
  
    PvtData data = (PvtData) tile.getData(DefineConstants.MaxTexelProjectedSizeTLTDataID);
  
    if (data == null)
    {
      data = createData(tile);
    }
  
    if (data != null)
    {
  
      final Box box = data._boundingBox;
      final Camera cam = rc.getCurrentCamera();
      final Vector3D pos = cam.getCartesianPosition();
  
      if (box.contains(pos))
      {
        return false;
      }
  
      final TileTessellatorMeshData meshData = tile.getTessellatorMeshData();
      if (meshData == null)
      {
        return true;
      }
      final IMathUtils mu = IMathUtils.instance();
  
      final double texelsPerTriangleLat = mu.sqrt(prc._texHeightSquared) / meshData._meshResLat;
      final double texelsPerTriangleLon = mu.sqrt(prc._texWidthSquared) / meshData._meshResLon;
  
      final double texelLatSize = meshData._maxTriangleLatitudeLenght / texelsPerTriangleLat;
      final double texelLonSize = meshData._maxTriangleLongitudeLenght / texelsPerTriangleLon;
  
      double texelSize = texelLatSize;
      if (texelLonSize > texelLatSize)
      {
        texelSize = texelLonSize;
      }
  
      //Position of closest possible texel
      final Vector3D closestPossiblePointOnTile = box.closestPoint(cam.getCartesianPosition());
  
      //Distance of the view plane containing texelPos
      final double sizeTexel = cam.maxScreenSizeOf(texelSize, closestPossiblePointOnTile);
  
      if (sizeTexel >= _maxAllowedPixelsForTexel)
      {
  //      ILogger::instance()->logInfo("Insufficient Texture %s", tile->_id.c_str());
        return false;
      }
      else
      {
  
        if (meshData._demDistanceToNextLoD <= 0)
        {
          return true;
        }
  
        //Checking DEM
        final double demErrorScreenSize = cam.maxScreenSizeOf(meshData._demDistanceToNextLoD, closestPossiblePointOnTile);
  
        boolean demOK = demErrorScreenSize < _maxAllowedPixelsForDEMError;
  
  //      if (!demOK){
  //        ILogger::instance()->logInfo("Insufficient DEM %s", tile->_id.c_str());
  //      }
  
        return demOK;
  
      }
    }
  
    return true;
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
  //  createData(tile);
    tile.clearDataWithID(DefineConstants.MaxTexelProjectedSizeTLTDataID);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
  }

  public final void renderStarted()
  {
  }


}