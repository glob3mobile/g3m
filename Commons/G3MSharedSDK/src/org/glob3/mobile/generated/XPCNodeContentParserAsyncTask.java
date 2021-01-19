package org.glob3.mobile.generated;
public class XPCNodeContentParserAsyncTask extends GAsyncTask
{
  private XPCNode _node;
  private IByteBuffer _buffer;

  private java.util.ArrayList<XPCNode> _children;

  public XPCNodeContentParserAsyncTask(XPCNode node, IByteBuffer buffer)
  {
     _node = node;
     _buffer = buffer;
     _children = null;
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

//    delete _points;
  }

  public final void runInBackground(G3MContext context)
  {
    ByteBufferIterator it = new ByteBufferIterator(_buffer);

    byte version = it.nextUInt8();
    if (version != 1)
    {
      ILogger.instance().logError("Unssuported format version");
      return;
    }

    _children = new java.util.ArrayList<XPCNode>();

    final int childrenCount = it.nextInt32();
    for (int i = 0; i < childrenCount; i++)
    {
      XPCNode child = XPCNode.fromByteBufferIterator(it);
      _children.add(child);
    }

    if (it.hasNext())
    {
      throw new RuntimeException("Logic error");
    }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning _______TODO: PARSE POINTS
  }

  public final void onPostExecute(G3MContext context)
  {
//    _node->parsedContent( _children, _points );
    _node.setContent(_children);
    _children = null; // moved ownership to _node
//    _points   = NULL; // moved ownership to _node
  }

}