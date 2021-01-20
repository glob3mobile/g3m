package org.glob3.mobile.generated;
public class XPCNodeContentParserAsyncTask extends GAsyncTask
{
  private XPCNode _node;

  private IByteBuffer _buffer;

  private final Planet _planet;
  private final float _verticalExaggeration;
  private final float _deltaHeight;


  private java.util.ArrayList<XPCNode> _children;
  private java.util.ArrayList<XPCPoint> _points;
  private DirectMesh _mesh;

  public XPCNodeContentParserAsyncTask(XPCNode node, IByteBuffer buffer, Planet planet, float verticalExaggeration, float deltaHeight)
  {
     _node = node;
     _buffer = buffer;
     _planet = planet;
     _verticalExaggeration = verticalExaggeration;
     _deltaHeight = deltaHeight;
     _children = null;
     _points = null;
     _mesh = null;
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
      _points = new java.util.ArrayList<XPCPoint>();

      final int pointsCount = it.nextInt32();
      for (int i = 0; i < pointsCount; i++)
      {
        XPCPoint point = XPCPoint.fromByteBufferIterator(it);
        _points.add(point);
      }
    }


    if (it.hasNext())
    {
      throw new RuntimeException("Logic error");
    }


    FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(_planet);

    final int pointsSize = _points.size();
    for (int i = 0; i < pointsSize; i++)
    {
      XPCPoint point = _points.get(i);
      vertices.addDegrees(point._y, point._x, _deltaHeight + (point._z * _verticalExaggeration));
    }

    _mesh = new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), 1, 1, Color.newFromRGBA(1, 1, 1, 1)); // flatColor

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