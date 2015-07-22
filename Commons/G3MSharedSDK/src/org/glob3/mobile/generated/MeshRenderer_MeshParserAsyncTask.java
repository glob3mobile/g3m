package org.glob3.mobile.generated; 
public class MeshRenderer_MeshParserAsyncTask extends GAsyncTask
{
  private G3MContext _context;

  private MeshRenderer _meshRenderer;
  public final URL _url;
  private IByteBuffer _buffer;
  private final float _pointSize;
  private final double _deltaHeight;
  private Color     _color;
  private MeshLoadListener _listener;
  private final boolean _deleteListener;
  private final boolean _isBSON;
  private final MeshType _meshType;

  private Mesh _mesh;

  private float normalize(float value, float max, float min)
  {
    return (value - min) / (max - min);
  }

  private Color interpolateColor(Color from, Color middle, Color to, float d)
  {
    if (d <= 0)
    {
      return from;
    }
    if (d >= 1)
    {
      return to;
    }
    if (d <= 0.5)
    {
      return from.mixedWith(middle, d * 2);
    }
    return middle.mixedWith(to, (d - 0.5f) * 2);
  }

  private void parsePointCloudMesh(JSONBaseObject jsonBaseObject)
  {
    final JSONObject jsonObject = jsonBaseObject.asObject();
    if (jsonObject == null)
    {
      ILogger.instance().logError("Invalid format for PointCloud");
    }
    else
    {
      final int size = (int) jsonObject.getAsNumber("size", -1);
      if (size <= 0)
      {
        ILogger.instance().logError("Invalid size for PointCloud");
      }
      else
      {
        Geodetic3D averagePoint = null;

        final JSONArray jsonAveragePoint = jsonObject.getAsArray("averagePoint");
        if ((jsonAveragePoint != null) && (jsonAveragePoint.size() == 3))
        {
          final double lonInDegrees = jsonAveragePoint.getAsNumber(0, 0);
          final double latInDegrees = jsonAveragePoint.getAsNumber(1, 0);
          final double height = jsonAveragePoint.getAsNumber(2, 0);

          averagePoint = new Geodetic3D(Angle.fromDegrees(latInDegrees), Angle.fromDegrees(lonInDegrees), height + _deltaHeight);
        }
        else
        {
          ILogger.instance().logError("Invalid averagePoint for PointCloud");
        }

        final JSONArray jsonPoints = jsonObject.getAsArray("points");
        if ((jsonPoints == null) || (size *3 != jsonPoints.size()))
        {
          ILogger.instance().logError("Invalid points for PointCloud");
        }
        else
        {
          FloatBufferBuilderFromGeodetic verticesBuilder = (averagePoint == null) ? FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_context.getPlanet()) : FloatBufferBuilderFromGeodetic.builderWithGivenCenter(_context.getPlanet(), averagePoint);
          /*                 */
          /*                 */

          double minHeight = jsonPoints.getAsNumber(2, 0);
          double maxHeight = minHeight;

          for (int i = 0; i < size *3; i += 3)
          {
            final double lonInDegrees = jsonPoints.getAsNumber(i, 0);
            final double latInDegrees = jsonPoints.getAsNumber(i + 1, 0);
            final double height = jsonPoints.getAsNumber(i + 2, 0);

            verticesBuilder.add(Angle.fromDegrees(latInDegrees), Angle.fromDegrees(lonInDegrees), height + _deltaHeight);

            if (height < minHeight)
            {
              minHeight = height;
            }
            if (height > maxHeight)
            {
              maxHeight = height;
            }
          }

          IFloatBuffer colors = null;
          final JSONArray jsonColors = jsonObject.getAsArray("colors");
          if (jsonColors == null)
          {
            final Color fromColor = Color.red();
            final Color middleColor = Color.green();
            final Color toColor = Color.blue();
            FloatBufferBuilderFromColor colorsBuilder = new FloatBufferBuilderFromColor();

            for (int i = 0; i < size *3; i += 3)
            {
              final double height = jsonPoints.getAsNumber(i + 2, 0);

              final Color interpolatedColor = interpolateColor(fromColor, middleColor, toColor, normalize((float) height, (float) minHeight, (float) maxHeight));
              colorsBuilder.add(interpolatedColor);
            }

            colors = colorsBuilder.create();
          }
          else
          {
            FloatBufferBuilderFromColor colorsBuilder = new FloatBufferBuilderFromColor();

            final int colorsSize = jsonColors.size();
            for (int i = 0; i < colorsSize; i += 3)
            {
              final int red = (int) jsonColors.getAsNumber(i, 0);
              final int green = (int) jsonColors.getAsNumber(i + 1, 0);
              final int blue = (int) jsonColors.getAsNumber(i + 2, 0);

              colorsBuilder.addBase255(red, green, blue, 1);
            }

            colors = colorsBuilder.create();
          }

          _mesh = new DirectMesh(GLPrimitive.points(), true, verticesBuilder.getCenter(), verticesBuilder.create(), 1, _pointSize, null, colors, 1, true); // flatColor, -  lineWidth

          if (verticesBuilder != null)
             verticesBuilder.dispose();
        }

        if (averagePoint != null)
           averagePoint.dispose();
      }
    }
  }

  private void parseMesh(JSONBaseObject jsonBaseObject)
  {
    final JSONObject jsonObject = jsonBaseObject.asObject();
    if (jsonObject == null)
    {
      ILogger.instance().logError("Invalid format for 'base object' of \"%s\"", _url._path);
    }
    else
    {
      final JSONArray jsonCoordinates = jsonObject.getAsArray("coordinates");
      if (jsonCoordinates == null)
      {
        ILogger.instance().logError("Invalid format for 'coordinates' of \"%s\"", _url._path);
        ILogger.instance().logInfo("\"%s\"", jsonObject.description());
      }
      else
      {
        FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_context.getPlanet());

        final int coordinatesSize = jsonCoordinates.size();
        for (int i = 0; i < coordinatesSize; i += 3)
        {
          final double latInDegrees = jsonCoordinates.getAsNumber(i, 0);
          final double lonInDegrees = jsonCoordinates.getAsNumber(i + 1, 0);
          final double height = jsonCoordinates.getAsNumber(i + 2, 0);

          vertices.add(Angle.fromDegrees(latInDegrees), Angle.fromDegrees(lonInDegrees), height);
        }

        final JSONArray jsonNormals = jsonObject.getAsArray("normals");
        final int normalsSize = jsonNormals.size();
        IFloatBuffer normals = IFactory.instance().createFloatBuffer(normalsSize);
        for (int i = 0; i < normalsSize; i++)
        {
          normals.put(i, (float) jsonNormals.getAsNumber(i, 0));
        }

        final JSONArray jsonIndices = jsonObject.getAsArray("indices");
        final int indicesSize = jsonIndices.size();
        IShortBuffer indices = IFactory.instance().createShortBuffer(indicesSize);
        for (int i = 0; i < indicesSize; i++)
        {
          indices.put(i, (short) jsonIndices.getAsNumber(i, 0));
        }

        _mesh = new IndexedMesh(GLPrimitive.triangles(), true, vertices.getCenter(), vertices.create(), indices, 1, 1, _color, null, 1, true, normals); // depthTest, -  colorsIntensity, -  colors, -  flatColor -  pointSize -  lineWidth

        if (vertices != null)
           vertices.dispose();

        _color = null;
      }
    }
  }


  public MeshRenderer_MeshParserAsyncTask(MeshRenderer meshRenderer, URL url, IByteBuffer buffer, float pointSize, double deltaHeight, Color color, MeshLoadListener listener, boolean deleteListener, boolean isBSON, MeshType meshType, G3MContext context)
  {
     _meshRenderer = meshRenderer;
     _url = url;
     _buffer = buffer;
     _pointSize = pointSize;
     _deltaHeight = deltaHeight;
     _listener = listener;
     _deleteListener = deleteListener;
     _isBSON = isBSON;
     _meshType = meshType;
     _context = context;
     _mesh = null;
     _color = color;
  }

  public final void runInBackground(G3MContext context)
  {
    final JSONBaseObject jsonBaseObject = _isBSON ? BSONParser.parse(_buffer) : IJSONParser.instance().parse(_buffer);
    /*                                         */
    /*                                         */

    if (jsonBaseObject != null)
    {
      switch (_meshType)
      {
        case POINT_CLOUD:
          parsePointCloudMesh(jsonBaseObject);
          break;
        case MESH:
          parseMesh(jsonBaseObject);
          break;
      }

      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }

    if (_buffer != null)
       _buffer.dispose();
    _buffer = null;
  }

  public void dispose()
  {
    if (_deleteListener)
    {
      if (_listener != null)
         _listener.dispose();
    }
    if (_buffer != null)
       _buffer.dispose();
    _color = null;
    super.dispose();
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_mesh == null)
    {
      ILogger.instance().logError("Error parsing Mesh from \"%s\"", _url._path);
    }
    else
    {
      if (_listener != null)
      {
        _listener.onBeforeAddMesh(_mesh);
      }

      ILogger.instance().logInfo("Adding Mesh to _meshRenderer");
      _meshRenderer.addMesh(_mesh);

      if (_listener != null)
      {
        _listener.onAfterAddMesh(_mesh);
      }

      _mesh = null;
    }
  }

}