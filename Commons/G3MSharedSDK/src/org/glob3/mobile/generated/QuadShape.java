package org.glob3.mobile.generated; 
public class QuadShape
{
   public TextureIDReference getTextureId(G3MRenderContext rc)
   {
     if (_textureImage == null)
     {
       return null;
     }
   
     final TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _textureURL._path, false);
   
     _textureImage = null;
     _textureImage = null;
   
     if (texId == null)
     {
       rc.getLogger().logError("Can't load texture %s", _textureURL._path);
     }
   
     return texId;
   }
   public void imageDownloaded(IImage image)
   {
     _textureImage = image;
   
     cleanMesh();
   }
   public void dispose()
   {
     _color = null;
   
     super.dispose();
   
   }
   public Mesh createMesh(G3MRenderContext rc)
   {
     if (!_textureRequested)
     {
       _textureRequested = true;
       if (_textureURL._path.length() != 0)
       {
         rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), true, new QuadShape_IImageDownloadListener(this), true);
       }
     }
   
     final float halfWidth = _width / 2.0f;
     final float halfHeight = _height / 2.0f;
   
     final float left = -halfWidth;
     final float right = +halfWidth;
     final float bottom = -halfHeight;
     final float top = +halfHeight;
   
     FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
     vertices.add(left, bottom, 0);
     vertices.add(right, bottom, 0);
     vertices.add(left, top, 0);
     vertices.add(right, top, 0);
   
     Color color = (_color == null) ? null : new Color(*_color);
     Mesh im = null;
     if (_withNormals)
     {
       FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
       normals.add(0.0, 0.0, 1.0);
       normals.add(0.0, 0.0, 1.0);
       normals.add(0.0, 0.0, 1.0);
       normals.add(0.0, 0.0, 1.0);
   
       im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color, null, (float)1.0, true, normals.create());
   
       if (normals != null)
          normals.dispose();
     }
     else
     {
       im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color);
     }
   
     if (vertices != null)
        vertices.dispose();
   
     final TextureIDReference texId = getTextureId(rc);
     if (texId == null)
     {
       return im;
     }
   
     FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
     texCoords.add(0, 1);
     texCoords.add(1, 1);
     texCoords.add(0, 0);
     texCoords.add(1, 0);
   
     TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
   
     return new TexturedMesh(im, true, texMap, true, true);
   }
}