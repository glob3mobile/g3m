

package org.glob3.mobile.generated;

//
//  TexturedBoxShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//

//
//  TexturedBoxShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/16/12.
//
//


public class TexturedBoxShape
         extends
            AbstractMeshShape {
   private double         _extentX;
   private double         _extentY;
   private double         _extentZ;

   private final Quadric  _frontQuadric;
   private final Quadric  _backQuadric;
   private final Quadric  _leftQuadric;
   private final Quadric  _rightQuadric;
   private final Quadric  _topQuadric;
   private final Quadric  _bottomQuadric;

   private float          _borderWidth;

   private final IImage   _image;
   private final String   _imageName;
   private Color          _borderColor;
   private final Vector3F _textureRepetitions;


   private Mesh createBorderMesh(final G3MRenderContext rc) {
      final float lowerX = (float) -(_extentX / 2);
      final float upperX = (float) +(_extentX / 2);
      final float lowerY = (float) -(_extentY / 2);
      final float upperY = (float) +(_extentY / 2);
      final float lowerZ = (float) -(_extentZ / 2);
      final float upperZ = (float) +(_extentZ / 2);

      final float[] v = { lowerX, lowerY, lowerZ, lowerX, upperY, lowerZ, lowerX, upperY, upperZ, lowerX, lowerY, upperZ, upperX,
               lowerY, lowerZ, upperX, upperY, lowerZ, upperX, upperY, upperZ, upperX, lowerY, upperZ };

      final int numIndices = 48;
      final short[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2,
               2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };

      final FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final ShortBufferBuilder indices = new ShortBufferBuilder();

      final int numVertices = 8;
      for (int n = 0; n < numVertices; n++) {
         vertices.add(v[n * 3], v[(n * 3) + 1], v[(n * 3) + 2]);
      }

      for (int n = 0; n < numIndices; n++) {
         indices.add(i[n]);
      }

      final Color borderColor = (_borderColor != null) ? new Color(_borderColor) : new Color(Color.red());

      final Mesh result = new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(),
               true, (_borderWidth > 1) ? _borderWidth : 1, 1, borderColor);

      if (vertices != null) {
         vertices.dispose();
      }

      return result;
   }


   //  Mesh* createSurfaceMesh(const G3MRenderContext* rc);

   private Mesh createSurfaceMeshWithNormals(final G3MRenderContext rc) {
      final float lowerX = (float) -(_extentX / 2);
      final float upperX = (float) +(_extentX / 2);
      final float lowerY = (float) -(_extentY / 2);
      final float upperY = (float) +(_extentY / 2);
      final float lowerZ = (float) -(_extentZ / 2);
      final float upperZ = (float) +(_extentZ / 2);

      final FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      final FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();

      final float[] v = { lowerX, upperY, lowerZ, lowerX, upperY, upperZ, upperX, upperY, lowerZ, upperX, upperY, lowerZ, lowerX,
               upperY, upperZ, upperX, upperY, upperZ, lowerX, lowerY, lowerZ, lowerX, lowerY, upperZ, upperX, lowerY, lowerZ,
               upperX, lowerY, lowerZ, lowerX, lowerY, upperZ, upperX, lowerY, upperZ, lowerX, lowerY, upperZ, lowerX, upperY,
               upperZ, upperX, lowerY, upperZ, upperX, lowerY, upperZ, lowerX, upperY, upperZ, upperX, upperY, upperZ, lowerX,
               lowerY, lowerZ, lowerX, upperY, lowerZ, upperX, lowerY, lowerZ, upperX, lowerY, lowerZ, lowerX, upperY, lowerZ,
               upperX, upperY, lowerZ, upperX, lowerY, lowerZ, upperX, lowerY, upperZ, upperX, upperY, lowerZ, upperX, upperY,
               lowerZ, upperX, lowerY, upperZ, upperX, upperY, upperZ, lowerX, lowerY, lowerZ, lowerX, lowerY, upperZ, lowerX,
               upperY, lowerZ, lowerX, upperY, lowerZ, lowerX, lowerY, upperZ, lowerX, upperY, upperZ };
      //FACE 1
      //FACE 2
      //FACE 3
      //FACE 4
      //FACE 5
      //FACE 6


      final float[] texCoords = { 0, 0, 0, _textureRepetitions._y, _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0,
               _textureRepetitions._y, _textureRepetitions._x, _textureRepetitions._y, 0, 0, 0, _textureRepetitions._y,
               _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0, _textureRepetitions._y, _textureRepetitions._x,
               _textureRepetitions._y, 0, 0, 0, _textureRepetitions._z, _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0,
               _textureRepetitions._z, _textureRepetitions._x, _textureRepetitions._z, 0, 0, 0, _textureRepetitions._z,
               _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0, _textureRepetitions._z, _textureRepetitions._x,
               _textureRepetitions._z, 0, 0, 0, _textureRepetitions._y, _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0,
               _textureRepetitions._y, _textureRepetitions._x, _textureRepetitions._y, 0, 0, 0, _textureRepetitions._y,
               _textureRepetitions._x, 0, _textureRepetitions._x, 0, 0, _textureRepetitions._y, _textureRepetitions._x,
               _textureRepetitions._y };
      //FACE 1
      //FACE 2
      //FACE 3
      //FACE 4
      //FACE 5
      //FACE 6

      final float[] n = { 0, 1, 0, 0, -1, 0, 0, 0, 1, 0, 0, -1, 1, 0, 0, -1, 0, 0 };
      //FACE 1
      //FACE 2
      //FACE 3
      //FACE 4
      //FACE 5
      //FACE 6


      final int numFaces = 6;
      final int numVertices = 6 * numFaces;

      for (int i = 0; i < numVertices; i++) {
         vertices.add(v[i * 3], v[(i * 3) + 1], v[(i * 3) + 2]);
      }

      final FloatBufferBuilderFromCartesian2D texC = new FloatBufferBuilderFromCartesian2D();
      for (int i = 0; i < numVertices; i++) {
         texC.add(texCoords[i * 2], texCoords[(i * 2) + 1]);

      }

      final TextureIDReference texID = rc.getTexturesHandler().getTextureIDReference(_image, GLFormat.rgba(), _imageName, true,
               GLTextureParameterValue.repeat());

      final TextureMapping tm = new SimpleTextureMapping(texID, texC.create(), true, false);

      for (int i = 0; i < numFaces; i++) {
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
         normals.add(n[i * 3], n[(i * 3) + 1], n[(i * 3) + 2]);
      }


      final Mesh result = new DirectMesh(GLPrimitive.triangles(), true, vertices.getCenter(), vertices.create(),
               (_borderWidth > 1) ? _borderWidth : 1, 1, null, null, 1, true, normals.create());

      if (vertices != null) {
         vertices.dispose();
      }
      if (normals != null) {
         normals.dispose();
      }

      final TexturedMesh texMesh = new TexturedMesh(result, true, tm, true, false);


      return texMesh;
   }


   @Override
   protected final Mesh createMesh(final G3MRenderContext rc) {

      final Mesh surface = createSurfaceMeshWithNormals(rc);

      if (_borderWidth > 0) {
         final CompositeMesh compositeMesh = new CompositeMesh();
         compositeMesh.addMesh(surface);
         compositeMesh.addMesh(createBorderMesh(rc));
         return compositeMesh;
      }

      return surface;
   }


   public TexturedBoxShape(final Geodetic3D position,
                           final AltitudeMode altitudeMode,
                           final Vector3D extent,
                           final float borderWidth,
                           final IImage image,
                           final String imageName,
                           final Vector3F textureRepetitions) {
      this(position, altitudeMode, extent, borderWidth, image, imageName, textureRepetitions, null);
   }


   public TexturedBoxShape(final Geodetic3D position,
                           final AltitudeMode altitudeMode,
                           final Vector3D extent,
                           final float borderWidth,
                           final IImage image,
                           final String imageName,
                           final Vector3F textureRepetitions,
                           final Color borderColor) {
      super(position, altitudeMode);
      _extentX = extent._x;
      _extentY = extent._y;
      _extentZ = extent._z;
      _frontQuadric = Quadric.fromPlane(1, 0, 0, -extent._x / 2);
      _backQuadric = Quadric.fromPlane(-1, 0, 0, -extent._x / 2);
      _leftQuadric = Quadric.fromPlane(0, -1, 0, -extent._y / 2);
      _rightQuadric = Quadric.fromPlane(0, 1, 0, -extent._y / 2);
      _topQuadric = Quadric.fromPlane(0, 0, 1, -extent._z / 2);
      _bottomQuadric = Quadric.fromPlane(0, 0, -1, -extent._z / 2);
      _borderWidth = borderWidth;
      _borderColor = borderColor;
      _image = image;
      _imageName = imageName;
      _textureRepetitions = new Vector3F(textureRepetitions);

   }


   @Override
   public void dispose() {
      if (_image != null) {
         _image.dispose();
      }
      if (_borderColor != null) {
         _borderColor.dispose();
      }

      super.dispose();
   }


   public final void setExtent(final Vector3D extent) {
      if ((_extentX != extent._x) || (_extentY != extent._y) || (_extentZ != extent._z)) {
         _extentX = extent._x;
         _extentY = extent._y;
         _extentZ = extent._z;
         cleanMesh();
      }
   }


   public final Vector3D getExtent() {
      return new Vector3D(_extentX, _extentY, _extentZ);
   }


   public final void setBorderColor(final Color color) {
      if (_borderColor != null) {
         _borderColor.dispose();
      }
      _borderColor = color;
      cleanMesh();
   }


   public final void setBorderWidth(final float borderWidth) {
      if (_borderWidth != borderWidth) {
         _borderWidth = borderWidth;
         cleanMesh();
      }
   }


   @Override
   public final java.util.ArrayList<Double> intersectionsDistances(final Planet planet,
                                                                   final Vector3D origin,
                                                                   final Vector3D direction) {
      final java.util.ArrayList<Double> distances = new java.util.ArrayList<Double>();

      double tmin = -1e10;
      double tmax = 1e10;
      double t1;
      double t2;
      // transform 6 planes
      final MutableMatrix44D M = createTransformMatrix(planet);
      final Quadric transformedFront = _frontQuadric.transformBy(M);
      final Quadric transformedBack = _backQuadric.transformBy(M);
      final Quadric transformedLeft = _leftQuadric.transformBy(M);
      final Quadric transformedRight = _rightQuadric.transformBy(M);
      final Quadric transformedTop = _topQuadric.transformBy(M);
      final Quadric transformedBottom = _bottomQuadric.transformBy(M);
      if (M != null) {
         M.dispose();
      }

      // intersecction with X planes
      final java.util.ArrayList<Double> frontDistance = transformedFront.intersectionsDistances(origin, direction);
      final java.util.ArrayList<Double> backDistance = transformedBack.intersectionsDistances(origin, direction);
      if ((frontDistance.size() == 1) && (backDistance.size() == 1)) {
         if (frontDistance.get(0) < backDistance.get(0)) {
            t1 = frontDistance.get(0);
            t2 = backDistance.get(0);
         }
         else {
            t2 = frontDistance.get(0);
            t1 = backDistance.get(0);
         }
         if (t1 > tmin) {
            tmin = t1;
         }
         if (t2 < tmax) {
            tmax = t2;
         }
      }

      // intersections with Y planes
      final java.util.ArrayList<Double> leftDistance = transformedLeft.intersectionsDistances(origin, direction);
      final java.util.ArrayList<Double> rightDistance = transformedRight.intersectionsDistances(origin, direction);
      if ((leftDistance.size() == 1) && (rightDistance.size() == 1)) {
         if (leftDistance.get(0) < rightDistance.get(0)) {
            t1 = leftDistance.get(0);
            t2 = rightDistance.get(0);
         }
         else {
            t2 = leftDistance.get(0);
            t1 = rightDistance.get(0);
         }
         if (t1 > tmin) {
            tmin = t1;
         }
         if (t2 < tmax) {
            tmax = t2;
         }
      }

      // intersections with Z planes
      final java.util.ArrayList<Double> topDistance = transformedTop.intersectionsDistances(origin, direction);
      final java.util.ArrayList<Double> bottomDistance = transformedBottom.intersectionsDistances(origin, direction);
      if ((topDistance.size() == 1) && (bottomDistance.size() == 1)) {
         if (topDistance.get(0) < bottomDistance.get(0)) {
            t1 = topDistance.get(0);
            t2 = bottomDistance.get(0);
         }
         else {
            t2 = topDistance.get(0);
            t1 = bottomDistance.get(0);
         }
         if (t1 > tmin) {
            tmin = t1;
         }
         if (t2 < tmax) {
            tmax = t2;
         }
      }

      if (tmin < tmax) {
         distances.add(tmin);
         distances.add(tmax);
      }

      return distances;
   }

}
