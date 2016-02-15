package org.glob3.mobile.generated; 
//
//  MaxTexelProjectedSizeTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//

//
//  MaxTexelProjectedSizeTileLODTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/2/16.
//
//




//class Planet;
//class Camera;


public class MaxTexelProjectedSizeTileLODTester extends TileLODTester
{

  private static class PvtData extends TileData
  {

    public final Box _const _boundingBox;
    public PvtData(Box boundingBox)
    {
       super(DefineConstants.MaxTexelProjectedSizeTLTDataID);
       _boundingBox = boundingBox;
    }
  }

  private final double _maxAllowedPixelsForTexel;

  private MaxTexelProjectedSizeTileLODTester.PvtData createData(Tile tile)
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


  public MaxTexelProjectedSizeTileLODTester(double maxAllowedPixelsForTexel)
  {
     _maxAllowedPixelsForTexel = maxAllowedPixelsForTexel;
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
  
      if (box.contains(cam.getCartesianPosition()))
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
  
      final double texelSize = texelLatSize > texelLonSize != 0? texelLatSize : texelLonSize;
  
      //Position of closest possible texel
      final Vector3D texelPos = box.closestPoint(cam.getCartesianPosition());
  
      //Distance of the view plane containing texelPos
      final double size = cam.maxScreenSizeOf(texelSize, texelPos);
  
      return size < _maxAllowedPixelsForTexel;
    }
  
    return true;
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
    createData(tile);
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
  }

  public final void renderStarted()
  {
  }


}