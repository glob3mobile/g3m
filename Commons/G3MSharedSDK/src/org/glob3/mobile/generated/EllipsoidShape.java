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
//class FloatBufferBuilderFromGeodetic;
//class FloatBufferBuilderFromCartesian2D;
//class IGLTextureId;



public class EllipsoidShape extends AbstractMeshShape
{
  private URL _textureURL = new URL();

  private final double _radiusX;
  private final double _radiusY;
  private final double _radiusZ;

  private final short _resolution;

  private final float _borderWidth;

  private final boolean _texturedInside;

  private final boolean _mercator;

  private Color _surfaceColor;
  private Color _borderColor;

  private Mesh createBorderMesh(G3MRenderContext rc, FloatBufferBuilderFromGeodetic vertices)
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
        borderColor = Color.newFromRGBA(1, 0, 0, 1);
      }
    }
  
    return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), (_borderWidth < 1) ? 1 : _borderWidth, 1, borderColor);
  }
  private Mesh createSurfaceMesh(G3MRenderContext rc, FloatBufferBuilderFromGeodetic vertices, FloatBufferBuilderFromCartesian2D texCoords)
  {
  
    // create surface indices
    ShortBufferBuilder indices = new ShortBufferBuilder();
    short delta = (short)(2 *_resolution - 1);
  
    // create indices for textupe mapping depending on the flag _texturedInside
    if (!_texturedInside)
    {
      for (short j = 0; j<_resolution-1; j++)
      {
        if (j>0)
           indices.add((short)(j *delta));
        for (short i = 0; i<2 *_resolution-1; i++)
        {
          indices.add((short)(i+j *delta));
          indices.add((short)(i+(j+1)*delta));
        }
        indices.add((short)((2 *_resolution-2)+(j+1)*delta));
      }
    }
    else
    {
      for (short j = 0; j<_resolution-1; j++)
      {
        if (j>0)
           indices.add((short)((j+1)*delta));
        for (short i = 0; i<2 *_resolution-1; i++)
        {
          indices.add((short)(i+(j+1)*delta));
          indices.add((short)(i+j *delta));
        }
        indices.add((short)((2 *_resolution-2)+j *delta));
      }
    }
  
    // create mesh
    Color surfaceColor = (_surfaceColor == null) ? null : new Color(_surfaceColor);
    Mesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), (_borderWidth < 1) ? 1 : _borderWidth, 1, surfaceColor);
  
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
        rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), true, new EllipsoidShape_IImageDownloadListener(this), true);
      }
    }
  
    final EllipsoidalPlanet ellipsoid = new EllipsoidalPlanet(new Ellipsoid(Vector3D.zero(), new Vector3D(_radiusX, _radiusY, _radiusZ)));
    final Sector sector = new Sector(Sector.fullSphere());
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), ellipsoid, Vector3D.zero());
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
  
    final short resolution2Minus2 = (short)(2 *_resolution-2);
    final short resolutionMinus1 = (short)(_resolution-1);
  
    for (int j = 0; j < _resolution; j++)
    {
      for (int i = 0; i < 2 *_resolution-1; i++)
      {
        final double u = (double) i / resolution2Minus2;
        final double v = (double) j / resolutionMinus1;
  
        final Geodetic2D innerPoint = sector.getInnerPoint(u, v);
  
        vertices.add(innerPoint);
  
        final double vv = _mercator ? MercatorUtils.getMercatorV(innerPoint._latitude) : v;
  
        texCoords.add((float) u, (float) vv);
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

  public EllipsoidShape(Geodetic3D position, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, Color surfaceColor)
  {
     this(position, radius, resolution, borderWidth, texturedInside, mercator, surfaceColor, null);
  }
  public EllipsoidShape(Geodetic3D position, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, Color surfaceColor, Color borderColor)
  {
     super(position);
     _textureURL = new URL(new URL("", false));
     _radiusX = radius.x();
     _radiusY = radius.y();
     _radiusZ = radius.z();
     _resolution = resolution < 3 ? 3 : resolution;
     _borderWidth = borderWidth;
     _texturedInside = texturedInside;
     _mercator = mercator;
     _surfaceColor = surfaceColor;
     _borderColor = borderColor;
     _textureRequested = false;
     _textureImage = null;

  }

  public EllipsoidShape(Geodetic3D position, URL textureURL, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator)
  {
     super(position);
     _textureURL = new URL(textureURL);
     _radiusX = radius.x();
     _radiusY = radius.y();
     _radiusZ = radius.z();
     _resolution = resolution < 3 ? 3 : resolution;
     _borderWidth = borderWidth;
     _texturedInside = texturedInside;
     _mercator = mercator;
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