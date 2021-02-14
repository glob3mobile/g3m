package org.glob3.mobile.generated;
public class XPCNodeContentParserAsyncTask extends GAsyncTask
{
  private final XPCPointCloud _pointCloud;
  private XPCNode _node;

  private IByteBuffer _buffer;

  private final Planet _planet;

  private java.util.ArrayList<XPCNode> _children;
  private DirectMesh _mesh;

  public XPCNodeContentParserAsyncTask(XPCPointCloud pointCloud, XPCNode node, IByteBuffer buffer, Planet planet)
  {
     _pointCloud = pointCloud;
     _node = node;
     _buffer = buffer;
     _planet = planet;
     _children = null;
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
      ILogger.instance().logError("Unsupported format version");
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

    final int pointsCount = it.nextInt32();

    if (pointsCount <= 0)
    {
      return;
    }

    IFloatBuffer cartesianVertices = IFactory.instance().createFloatBuffer(pointsCount * 3); // X, Y, Z

    double[] heights = new double[pointsCount];

    final double deltaHeight = _pointCloud.getDeltaHeight();
    final float verticalExaggeration = _pointCloud.getVerticalExaggeration();

    final float centerLatitudeDegrees = it.nextFloat();
    final float centerLongitudeDegrees = it.nextFloat();
    final float centerHeight = it.nextFloat();

    final Vector3D cartesianCenter = _planet.toCartesian(Angle.fromDegrees(centerLatitudeDegrees), Angle.fromDegrees(centerLongitudeDegrees), ((double) centerHeight + deltaHeight) * verticalExaggeration);
    {
      MutableVector3D bufferCartesian = new MutableVector3D();

      for (int i = 0; i < pointsCount; i++)
      {
        final double latitudeDegrees = (double) it.nextFloat() + centerLatitudeDegrees;
        final double longitudeDegrees = (double) it.nextFloat() + centerLongitudeDegrees;

        final double rawHeight = (double) it.nextFloat() + centerHeight;

        heights[i] = rawHeight;

        final double scaledHeight = (rawHeight + deltaHeight) * verticalExaggeration;

        _planet.toCartesianFromDegrees(latitudeDegrees, longitudeDegrees, scaledHeight, bufferCartesian);

        cartesianVertices.rawPut((i * 3) + 0, (float)(bufferCartesian._x - cartesianCenter._x));
        cartesianVertices.rawPut((i * 3) + 1, (float)(bufferCartesian._y - cartesianCenter._y));
        cartesianVertices.rawPut((i * 3) + 2, (float)(bufferCartesian._z - cartesianCenter._z));
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

    IFloatBuffer colors = IFactory.instance().createFloatBuffer(pointsCount * 4); // R, G, B, A
    MutableColor bufferColor = new MutableColor();

    XPCPointColorizer pointsColorizer = _pointCloud.getPointsColorizer();

    for (int i = 0; i < pointsCount; i++)
    {
      if (pointsColorizer == null)
      {
        colors.rawPut((i * 4) + 0, 1); // red
        colors.rawPut((i * 4) + 1, 1); // green
        colors.rawPut((i * 4) + 2, 1); // blue
        colors.rawPut((i * 4) + 3, 1); // alpha
      }
      else
      {
        pointsColorizer.colorize(metadata, heights, dimensionsValues, i, bufferColor);
        colors.rawPut((i * 4) + 0, bufferColor._red);
        colors.rawPut((i * 4) + 1, bufferColor._green);
        colors.rawPut((i * 4) + 2, bufferColor._blue);
        colors.rawPut((i * 4) + 3, bufferColor._alpha);
      }
    }

    heights = null;

    if (dimensionsValues != null)
    {
      for (int i = 0; i < dimensionsValues.size(); i++)
      {
        if (dimensionsValues.get(i) != null)
           dimensionsValues.get(i).dispose();
      }
    }

    _mesh = new DirectMesh(GLPrimitive.points(), true, cartesianCenter, cartesianVertices, 1, _pointCloud.getDevicePointSize(), null, colors, _pointCloud.depthTest()); // flatColor
  }

  public final void onPostExecute(G3MContext context)
  {
    if (_node.isCanceled())
    {
      return;
    }

    _node.setContent(_children, _mesh);
    _children = null; // moved ownership to _node
    _mesh = null; // moved ownership to _node
  }

}