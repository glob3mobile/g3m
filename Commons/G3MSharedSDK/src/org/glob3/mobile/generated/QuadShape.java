package org.glob3.mobile.generated;
//
//  QuadShape.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  QuadShape.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//




//class TextureIDReference;


public class QuadShape extends AbstractMeshShape
{
  private final URL _textureURL;
  private final float _width;
  private final float _height;
  private final Color _color;

  private boolean _depthTest;
  private boolean _cullFace;
  private int _culledFace;

  private boolean _textureRequested;
  private IImage _textureImage;

  private TextureIDReference getTextureID(G3MRenderContext rc)
  {
    if (_textureImage == null)
    {
      return null;
    }
  
    final TextureIDReference texID = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _textureURL._path, false, GLTextureParameterValue.clampToEdge(), GLTextureParameterValue.clampToEdge());
  
    _textureImage = null;
    _textureImage = null;
  
    if (texID == null)
    {
      rc.getLogger().logError("Can't load texture %s", _textureURL._path);
    }
  
    return texID;
  }

  private final boolean _withNormals;

  protected final Mesh createMesh(G3MRenderContext rc)
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
  
    Color color = (_color == null) ? null : new Color(_color);
    Mesh im = null;
    if (_withNormals)
    {
      FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
      normals.add(0.0, 0.0, 1.0);
      normals.add(0.0, 0.0, 1.0);
      normals.add(0.0, 0.0, 1.0);
      normals.add(0.0, 0.0, 1.0);
  
      im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color, null, _depthTest, normals.create(), false, 0, 0, _cullFace, _culledFace); // int culledFace -  bool cullFace -  float polygonOffsetUnits -  float polygonOffsetFactor -  bool polygonOffsetFill -  const IFloatBuffer* normals -  bool depthTest -  const IFloatBuffer* colors -  const Color* flatColor -  float pointSize -  float lineWidth -  const IFloatBuffer* vertices -  const Vector3D& center -  bool owner -  const int primitive
  
      if (normals != null)
         normals.dispose();
    }
    else
    {
      im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color, null, _depthTest, null, false, 0, 0, _cullFace, _culledFace); // int culledFace -  bool cullFace -  float polygonOffsetUnits -  float polygonOffsetFactor -  bool polygonOffsetFill -  const IFloatBuffer* normals -  bool depthTest -  const IFloatBuffer* colors -  const Color* flatColor -  float pointSize -  float lineWidth -  const IFloatBuffer* vertices -  const Vector3D& center -  bool owner -  const int primitive
    }
  
    if (vertices != null)
       vertices.dispose();
  
    final TextureIDReference texID = getTextureID(rc);
    if (texID == null)
    {
      return im;
    }
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 1);
    texCoords.add(1, 1);
    texCoords.add(0, 0);
    texCoords.add(1, 0);
  
    TextureMapping texMap = new SimpleTextureMapping(texID, texCoords.create(), true, true);
  
    return new TexturedMesh(im, true, texMap, true, true);
  }

  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, URL textureURL, float width, float height, boolean withNormals)
  {
     super(position, altitudeMode);
     _textureURL = textureURL;
     _width = width;
     _height = height;
     _textureRequested = false;
     _textureImage = null;
     _color = null;
     _withNormals = withNormals;
     _depthTest = true;
     _cullFace = false;
     _culledFace = GLCullFace.back();
  
  }

  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, IImage textureImage, float width, float height, boolean withNormals)
  {
     super(position, altitudeMode);
     _textureURL = new URL(new URL("", false));
     _width = width;
     _height = height;
     _textureRequested = true;
     _textureImage = textureImage;
     _color = null;
     _withNormals = withNormals;
     _depthTest = true;
     _cullFace = false;
     _culledFace = GLCullFace.back();
  
  }

  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, float width, float height, Color color, boolean withNormals)
  {
     super(position, altitudeMode);
     _textureURL = new URL(new URL("", false));
     _width = width;
     _height = height;
     _textureRequested = false;
     _textureImage = null;
     _color = color;
     _withNormals = withNormals;
     _depthTest = true;
     _cullFace = false;
     _culledFace = GLCullFace.back();
  
  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();
  
    super.dispose();
  }

  public final void setDepthTest(boolean depthTest)
  {
    if (_depthTest != depthTest)
    {
      _depthTest = depthTest;
      cleanMesh();
    }
  }

  public final void setCullFace(boolean cullFace)
  {
    if (_cullFace != cullFace)
    {
      _cullFace = cullFace;
      cleanMesh();
    }
  }

  public final void setCulledFace(int culledFace)
  {
    if (_culledFace != culledFace)
    {
      _culledFace = culledFace;
      cleanMesh();
    }
  }

  public final void imageDownloaded(IImage image)
  {
    _textureImage = image;
  
    cleanMesh();
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
    return intersections;
  }

}