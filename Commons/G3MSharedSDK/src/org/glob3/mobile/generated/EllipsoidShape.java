package org.glob3.mobile.generated; 
//
//  EllipsoidShape.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//


//
//  EllipsoidShape.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//



//class Color;
//class FloatBufferBuilderFromCartesian3D;
//class FloatBufferBuilderFromCartesian2D;
//class IGLTextureId;



public class EllipsoidShape extends AbstractMeshShape
{
  private final URL _textureURL = new URL();

  private final double _radiusX;
  private final double _radiusY;
  private final double _radiusZ;

  private final short _resolution;

  private final float _borderWidth;

  private final boolean _cozzi;

  private Color _surfaceColor;
  private Color _borderColor;

  private Mesh createBorderMesh(G3MRenderContext rc, FloatBufferBuilderFromCartesian3D vertices)
  {
  
    // create border indices for horizontal lines
    ShortBufferBuilder indices = new ShortBufferBuilder();
    short delta = (short)(2 *_resolution - 1);
    for (short j = 1; j<_resolution-1; j++)
    {
      for (short i = 0; i<2 *_resolution-2; i++)
      {
        indices.add((short)(i+j *delta));
        indices.add((short)(i+1+j *delta));
      }
    }
  
    // create border indices for vertical lines
    for (short i = 0; i<2 *_resolution-2; i++)
    {
      for (short j = 0; j<_resolution-1; j++)
      {
        indices.add((short)(i+j *delta));
        indices.add((short)(i+(j+1)*delta));
      }
    }
  
    Color borderColor;
    if (_borderColor != null)
    {
      borderColor = new Color(_borderColor);
    }
    else
    {
      if (_surfaceColor != null)
      {
        borderColor = new Color(_surfaceColor);
      }
      else
      {
        borderColor = Color.newFromRGBA(1, 1, 1, 1);
      }
    }
  
    return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, 1, borderColor);
  }
  private Mesh createSurfaceMesh(G3MRenderContext rc, FloatBufferBuilderFromCartesian3D vertices, FloatBufferBuilderFromCartesian2D texCoords)
  {
  
    // create surface indices
    ShortBufferBuilder indices = new ShortBufferBuilder();
    short delta = 2 *_resolution - 1;
    for (short j = 0; j<_resolution-1; j++)
    {
      if (j>0)
         indices.add(j *delta);
      for (short i = 0; i<2 *_resolution-1; i++)
      {
        indices.add(i+j *delta);
        indices.add(i+(j+1)*delta);
      }
      indices.add((j+2)*delta-1);
    }
  
    Color surfaceColor = (_surfaceColor == null) ? null : new Color(_surfaceColor);
  
    Mesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), _borderWidth, 1, surfaceColor);
  
    final IGLTextureId texId = getTextureId(rc);
    if (texId == null)
    {
      return im;
    }
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
    return new TexturedMesh(im, true, texMap, true, true);
  
  }

  private boolean _textureRequested;
  private IImage _textureImage;
  private IGLTextureId getTextureId(G3MRenderContext rc)
  {
    if (_textureImage == null)
    {
      return null;
    }
  
    final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureURL.getPath(), false);
  
    rc.getFactory().deleteImage(_textureImage);
    _textureImage = null;
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't load texture %s", _textureURL.getPath());
    }
  
    return texId;
  }

  protected final Mesh createMesh(G3MRenderContext rc)
  {
    if (!_textureRequested)
    {
      _textureRequested = true;
      if (_textureURL.getPath().length() != 0)
      {
        rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), new EllipsoidShape_IImageDownloadListener(this), true);
      }
    }
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
  
    final double pi = IMathUtils.instance().pi();
    final double incAngle = pi/(_resolution-1);
    for (int j = 0; j<_resolution; j++)
    {
      final double lat = pi/2 - j *incAngle;
      final double s = Math.sin(lat);
      final double c = Math.cos(lat);
      final double z = _radiusZ * s;
      for (int i = 0; i<2 *_resolution-1; i++)
      {
        final double lon = -pi + i *incAngle;
        final double x = _radiusX * c * Math.cos(lon);
        final double y = _radiusY * c * Math.sin(lon);
        vertices.add(x, y, z);
  
        final float u = (float) i / (2 *_resolution-2);
        final float v = (_cozzi)? (float)(1-s)/2 : (float)j/(_resolution-1);
        texCoords.add(u, v);
      }
    }
  
    Mesh surfaceMesh = createSurfaceMesh(rc, vertices, texCoords);
  
    if (_borderWidth > 0)
    {
      CompositeMesh compositeMesh = new CompositeMesh();
      compositeMesh.addMesh(surfaceMesh);
      compositeMesh.addMesh(createBorderMesh(rc, vertices));
      return compositeMesh;
    }
  
    return surfaceMesh;
  }

  public EllipsoidShape(Geodetic3D position, Vector3D radius, short resolution, float borderWidth, boolean cozzi, Color surfaceColor)
  {
     this(position, radius, resolution, borderWidth, cozzi, surfaceColor, null);
  }
  public EllipsoidShape(Geodetic3D position, Vector3D radius, short resolution, float borderWidth, boolean cozzi, Color surfaceColor, Color borderColor)
  {
     super(position);
     _textureURL = new URL(new URL("", false));
     _radiusX = radius.x();
     _radiusY = radius.y();
     _radiusZ = radius.z();
     _resolution = resolution < 3 ? 3 : resolution;
     _borderWidth = borderWidth;
     _cozzi = cozzi;
     _surfaceColor = surfaceColor;
     _borderColor = borderColor;
     _textureRequested = false;
     _textureImage = null;

  }

  public EllipsoidShape(Geodetic3D position, URL textureURL, Vector3D radius, short resolution, float borderWidth, boolean cozzi)
  {
     super(position);
     _textureURL = new URL(textureURL);
     _radiusX = radius.x();
     _radiusY = radius.y();
     _radiusZ = radius.z();
     _resolution = resolution < 3 ? 3 : resolution;
     _borderWidth = borderWidth;
     _cozzi = cozzi;
     _surfaceColor = null;
     _borderColor = null;
     _textureRequested = false;
     _textureImage = null;

  }


  public void dispose()
  {
    if (_surfaceColor != null)
       _surfaceColor.dispose();
    if (_borderColor != null)
       _borderColor.dispose();
  }

  public final void imageDownloaded(IImage image)
  {
    _textureImage = image;
  
    cleanMesh();
  }

}