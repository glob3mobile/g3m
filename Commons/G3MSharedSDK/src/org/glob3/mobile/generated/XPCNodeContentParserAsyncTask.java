package org.glob3.mobile.generated;
public class XPCNodeContentParserAsyncTask extends GAsyncTask
{
  private final XPCPointCloud _pointCloud;
  private XPCNode _node;

  private IByteBuffer _buffer;

  private final Planet _planet;

  private java.util.ArrayList<XPCNode> _children;
  private java.util.ArrayList<XPCPoint> _points;
  private DirectMesh _mesh;

  public XPCNodeContentParserAsyncTask(XPCPointCloud pointCloud, XPCNode node, IByteBuffer buffer, Planet planet)
  {
     _pointCloud = pointCloud;
     _node = node;
     _buffer = buffer;
     _planet = planet;
     _children = null;
     _points = null;
     _mesh = null;
    _pointCloud._retain();
    _node._retain();
  }

  public void dispose()
  {
    if (_buffer != null)
       _buffer.dispose();

    if (_children != null)
    {
      for (int i = 0; i < _children.size(); i++)
      {
        XPCNode child = _children.get(i);
        child._release();
      }
      _children = null;
    }

    if (_points != null)
    {
      for (int i = 0; i < _points.size(); i++)
      {
        XPCPoint point = _points.get(i);
        if (point != null)
           point.dispose();
      }
      _points = null;
    }

    if (_mesh != null)
       _mesh.dispose();

    _node._release();
    _pointCloud._release();
  }

  public final void runInBackground(G3MContext context)
  {
    if (_node.isCanceled())
    {
      return;
    }

    ByteBufferIterator it = new ByteBufferIterator(_buffer);

    byte version = it.nextUInt8();
    if (version != 1)
    {
      ILogger.instance().logError("Unssuported format version");
      return;
    }

    {
      _children = new java.util.ArrayList<XPCNode>();

      final int childrenCount = it.nextInt32();
      for (int i = 0; i < childrenCount; i++)
      {
        XPCNode child = XPCNode.fromByteBufferIterator(it);
        _children.add(child);
      }
    }

    {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning TODO____ points should be a floatbuffer + center

      _points = new java.util.ArrayList<XPCPoint>();

      final int pointsCount = it.nextInt32();
      if (pointsCount > 0)
      {
        final float centerLatitudeDegrees = it.nextFloat();
        final float centerLongitudeDegrees = it.nextFloat();
        final float centerHeight = it.nextFloat();
        for (int i = 0; i < pointsCount; i++)
        {
          XPCPoint point = XPCPoint.fromByteBufferIterator(it, centerLatitudeDegrees, centerLongitudeDegrees, centerHeight);
          _points.add(point);
        }
      }
    }

    final XPCMetadata metadata = _pointCloud.getMetadada();

    final java.util.ArrayList<IByteBuffer> dimensionsValues;

    final IIntBuffer dimensionIndices = _pointCloud.getRequiredDimensionIndices();
    if (dimensionIndices == null)
    {
      dimensionsValues = null;
    }
    else
    {

      final int dimensionsCount = dimensionIndices.size();
      dimensionsValues = new java.util.ArrayList<IByteBuffer>();
      for (int j = 0; j < dimensionsCount; j++)
      {
        final int dimensionIndex = dimensionIndices.get(j);

        final XPCDimension dimension = metadata.getDimension(dimensionIndex);

        final IByteBuffer dimensionValues = dimension.readValues(it);

        dimensionsValues.add(dimensionValues);
      }
    }

    if (it.hasNext())
    {
      throw new RuntimeException("Logic error");
    }

    XPCPointColorizer pointsColorizer = _pointCloud.getPointsColorizer();
    final float deltaHeight = _pointCloud.getDeltaHeight();
    final float verticalExaggeration = _pointCloud.getVerticalExaggeration();

    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_planet);
    FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

    final int pointsSize = _points.size();
    for (int i = 0; i < pointsSize; i++)
    {
      XPCPoint point = _points.get(i);
      vertices.addDegrees(point._latitudeDegrees, point._longitueDegrees, (point._height * verticalExaggeration) + deltaHeight);

      if (pointsColorizer != null)
      {
        final Color color = pointsColorizer.colorize(metadata, _points, dimensionsValues, i);

        colors.add(color);
      }
      else
      {
        colors.add(1, 1, 1, 1);
      }
    }

    if (dimensionsValues != null)
    {
      for (int i = 0; i < dimensionsValues.size(); i++)
      {
        if (dimensionsValues.get(i) != null)
           dimensionsValues.get(i).dispose();
      }
    }

    _mesh = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1, _pointCloud.getDevicePointSize(), null, colors.create(), false); // depthTest -  const IFloatBuffer* colors -  flatColor

    if (vertices != null)
       vertices.dispose();
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_node.isCanceled())
    {
      return;
    }

    _node.setContent(_children, _points, _mesh);
    _children = null; // moved ownership to _node
    _points = null; // moved ownership to _node
    _mesh = null; // moved ownership to _node
  }

}