package org.glob3.mobile.generated;
//
//  XPCMetadata.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCMetadata.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class IByteBuffer;
//class XPCPointCloud;
//class G3MRenderContext;
//class GLState;
//class Frustum;
//class XPCDimension;
//class XPCTree;


public class XPCMetadata
{

  public static XPCMetadata fromBuffer(IByteBuffer buffer)
  {
    if (buffer == null)
    {
      return null;
    }
  
    ByteBufferIterator it = new ByteBufferIterator(buffer);
  
  
    byte version = it.nextUInt8();
    if (version != 1)
    {
      ILogger.instance().logError("Unssuported format version");
      return null;
    }
  
    java.util.ArrayList<XPCDimension> dimensions = new java.util.ArrayList<XPCDimension>();
    {
      final int dimensionsCount = it.nextInt32();
      for (int i = 0; i < dimensionsCount; i++)
      {
        final String name = it.nextZeroTerminatedString();
        byte size = it.nextUInt8();
        final String type = it.nextZeroTerminatedString();
  
        dimensions.add(new XPCDimension(name, size, type));
      }
    }
  
    java.util.ArrayList<XPCTree> trees = new java.util.ArrayList<XPCTree>();
    {
      final int treesCount = it.nextInt32();
      for (int i = 0; i < treesCount; i++)
      {
        final String id = it.nextZeroTerminatedString();
  
        final double lowerLatitudeDegrees = it.nextDouble();
        final double lowerLongitudeDegrees = it.nextDouble();
        final double upperLatitudeDegrees = it.nextDouble();
        final double upperLongitudeDegrees = it.nextDouble();
  
        final Sector sector = Sector.newFromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees, upperLatitudeDegrees, upperLongitudeDegrees);
  
        final double minZ = it.nextDouble();
        final double maxZ = it.nextDouble();
  
        XPCNode rootNode = new XPCNode(id, sector, minZ, maxZ);
  
        XPCTree tree = new XPCTree(i, rootNode);
        trees.add(tree);
      }
    }
  
    if (it.hasNext())
    {
      throw new RuntimeException("Logic error");
    }
  
    return new XPCMetadata(dimensions, trees);
  }

  public void dispose()
  {
    for (int i = 0; i < _dimensions.size(); i++)
    {
      final XPCDimension dimension = _dimensions.get(i);
      if (dimension != null)
         dimension.dispose();
    }
  
    for (int i = 0; i < _trees.size(); i++)
    {
      final XPCTree tree = _trees.get(i);
      if (tree != null)
         tree.dispose();
    }
  }


  public final long render(XPCPointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
  {
  
    long renderedCount = 0;
  
    for (int i = 0; i < _treesSize; i++)
    {
      final XPCTree tree = _trees.get(i);
  
      renderedCount += tree.render(pointCloud, rc, glState, frustum, nowInMS);
    }
  
    return renderedCount;
  }

  public final int getTreesCount()
  {
    return _treesSize;
  }

  public final XPCTree getTree(int i)
  {
    return _trees.get(i);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  XPCMetadata(XPCMetadata that);

  private final java.util.ArrayList<XPCDimension> _dimensions;
  private final java.util.ArrayList<XPCTree> _trees;
  private final int _treesSize;

  private XPCMetadata(java.util.ArrayList<XPCDimension> dimensions, java.util.ArrayList<XPCTree> trees)
  {
     _dimensions = dimensions;
     _trees = trees;
     _treesSize = _trees.size();
  
  }

}