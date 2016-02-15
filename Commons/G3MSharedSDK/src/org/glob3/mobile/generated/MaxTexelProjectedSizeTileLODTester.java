

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


public class MaxTexelProjectedSizeTileLODTester
         extends
            TileLODTester {

   private static class PvtData
            extends
               TileData {

      public final Box _boundingBox;


      public PvtData(final Box boundingBox) {
         super(DefineConstants.MaxTexelProjectedSizeTLTDataID);
         _boundingBox = boundingBox;
      }
   }

   private final double _maxAllowedPixelsForTexel;


   private MaxTexelProjectedSizeTileLODTester.PvtData createData(final Tile tile) {
      final Box box = tile.createBoundingBox();
      if (box != null) {
         final PvtData data = new PvtData(box);
         tile.setData(data);
         return data;
      }
      else {
         tile.clearDataWithID(DefineConstants.MaxTexelProjectedSizeTLTDataID);
      }
      return null;
   }


   public MaxTexelProjectedSizeTileLODTester(final double maxAllowedPixelsForTexel) {
      _maxAllowedPixelsForTexel = maxAllowedPixelsForTexel;
   }


   @Override
   public void dispose() {
   }


   @Override
   public final boolean meetsRenderCriteria(final G3MRenderContext rc,
                                            final PlanetRenderContext prc,
                                            final Tile tile) {

      PvtData data = (PvtData) tile.getData(DefineConstants.MaxTexelProjectedSizeTLTDataID);

      if (data == null) {
         data = createData(tile);
      }

      if (data != null) {

         final Box box = data._boundingBox;
         final Camera cam = rc.getCurrentCamera();

         if (box.contains(cam.getCartesianPosition())) {
            return false;
         }

         final TileTessellatorMeshData meshData = tile.getTessellatorMeshData();
         if (meshData == null) {
            return true;
         }
         final IMathUtils mu = IMathUtils.instance();

         final double texelsPerTriangleLat = mu.sqrt(prc._texHeightSquared) / meshData._meshResLat;
         final double texelsPerTriangleLon = mu.sqrt(prc._texWidthSquared) / meshData._meshResLon;

         final double texelLatSize = meshData._maxTriangleLatitudeLenght / texelsPerTriangleLat;
         final double texelLonSize = meshData._maxTriangleLongitudeLenght / texelsPerTriangleLon;

         final double texelSize = (texelLatSize > texelLonSize) != 0 ? texelLatSize : texelLonSize;

         //Position of closest possible texel
         final Vector3D texelPos = box.closestPoint(cam.getCartesianPosition());

         //Distance of the view plane containing texelPos
         final double size = cam.maxScreenSizeOf(texelSize, texelPos);

         return size < _maxAllowedPixelsForTexel;
      }

      return true;
   }


   @Override
   public final void onTileHasChangedMesh(final Tile tile) {
      createData(tile);
   }


   @Override
   public final void onLayerTilesRenderParametersChanged(final LayerTilesRenderParameters ltrp) {
   }


   @Override
   public final void renderStarted() {
   }


}
