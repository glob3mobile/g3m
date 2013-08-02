package org.glob3.mobile.generated; 
//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





//class IFloatBuffer;
//class IShortBuffer;
//class IGLTextureId;

public abstract class SimplePlanetRenderer extends LeafRenderer
{

//  const std::string _textureFilename;
//  const int _texWidth, _texHeight;
  private IImage _image;

  private final int _latRes;
  private final int _lonRes;

  private Mesh _mesh;


  private IFloatBuffer createVertices(Planet planet)
  {
    //Vertices with Center in zero
  //  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(), planet, Vector3D::zero);
    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(planet, Vector3D.zero);
  
    final double lonRes1 = (double)(_lonRes-1);
    final double latRes1 = (double)(_latRes-1);
    for(double i = 0.0; i < _lonRes; i++)
    {
      final Angle lon = Angle.fromDegrees((i * 360 / lonRes1) -180);
      for (double j = 0.0; j < _latRes; j++)
      {
        final Angle lat = Angle.fromDegrees((j * 180.0 / latRes1) -90.0);
        final Geodetic2D g = new Geodetic2D(lat, lon);
  
        vertices.add(g);
      }
    }
  
    return vertices.create();
  }
  private IShortBuffer createMeshIndex()
  {
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int res = _lonRes;
    for (int j = 0; j < res - 1; j++)
    {
      if (j > 0)
      {
        indices.add((short)(j * res));
      }
      for (int i = 0; i < res; i++)
      {
        indices.add((short)(j * res + i));
        indices.add((short)(j * res + i + res));
      }
      indices.add((short)(j * res + 2 * res - 1));
    }
  
    return indices.create();
  }
  private IFloatBuffer createTextureCoordinates()
  {
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    final double lonRes1 = (double)(_lonRes-1);
    final double latRes1 = (double)(_latRes-1);
    //int p = 0;
    for(double i = 0.0; i < _lonRes; i++)
    {
      final double u = (i / lonRes1);
      for (double j = 0.0; j < _latRes; j++)
      {
        final double v = 1.0 - (j / latRes1);
        texCoords.add((float)u, (float)v);
      }
    }
  
    return texCoords.create();
  }

  private Mesh createMesh(G3MRenderContext rc)
  {
    IShortBuffer indices = createMeshIndex();
    IFloatBuffer vertices = createVertices(rc.getPlanet());
  
    //COLORS PER VERTEX
    IFloatBuffer vertexColors = null;
    final boolean colorPerVertex = false;
    if (colorPerVertex)
    {
      final IMathUtils mu = IMathUtils.instance();
      FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
      final int numVertices = _lonRes * _lonRes * 4;
      for (int i = 0; i < numVertices; i++)
      {
        final float val = (float)(0.5 + mu.sin((float)(2.0 * DefineConstants.PI * ((float) i) / numVertices)) / 2.0);
  
        colors.add(val, (float)0.0, (float)(1.0 - val), (float)1.0);
      }
      vertexColors = colors.create();
    }
  
    //FLAT COLOR
    Color flatColor = null;
    //  if (false) {
    //    flatColor = new Color( Color::fromRGBA(0.0, 1.0, 0.0, 1.0) );
    //  }
  
    IndexedMesh indexedMesh = new IndexedMesh(GLPrimitive.triangleStrip(), true, Vector3D.zero, vertices, indices, 1, 1, flatColor, vertexColors);
  
    //TEXTURED
    final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(_image, GLFormat.rgba(), "SimplePlanetRenderer-Texture", false);
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't load texture to GPU");
      if (indexedMesh != null)
         indexedMesh.dispose();
      return null;
    }
  
    // the image is not needed as it's already uploaded to the GPU
    IFactory.instance().deleteImage(_image);
    _image = null;
  
    IFloatBuffer texCoords = createTextureCoordinates();
  
    TextureMapping textureMapping = new SimpleTextureMapping(texId, texCoords, true, false);
  
    return new TexturedMesh(indexedMesh, true, textureMapping, true, false);
  }

  public SimplePlanetRenderer(IImage image)
  {
     _image = image;
     _latRes = 30;
     _lonRes = 30;
     _mesh = null;
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();
    IFactory.instance().deleteImage(_image);
  }

  public final void initialize(G3MContext context)
  {
  
  }

  public final void render(G3MRenderContext rc)
  {
    if (_mesh == null)
    {
      _mesh = createMesh(rc);
    }
    if (_mesh != null)
    {
      _mesh.render(rc, null);
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}