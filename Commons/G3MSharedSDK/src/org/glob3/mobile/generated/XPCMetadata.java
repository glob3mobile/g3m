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
  
    ByteBufferIterator iterator = new ByteBufferIterator(buffer);
  
  
    byte version = iterator.nextUInt8();
    if (version != 1)
    {
      ILogger.instance().logError("Unssuported format version");
      return null;
    }
  
    java.util.ArrayList<XPCDimension> dimensions = new java.util.ArrayList<XPCDimension>();
    {
      final int dimensionsCount = iterator.nextInt32();
      for (int i = 0; i < dimensionsCount; i++)
      {
        final String name = iterator.nextZeroTerminatedString();
        byte size = iterator.nextUInt8();
        final String type = iterator.nextZeroTerminatedString();
  
        dimensions.add(new XPCDimension(name, size, type));
      }
    }
  
    java.util.ArrayList<XPCTree> trees = new java.util.ArrayList<XPCTree>();
    {
      final int treesCount = iterator.nextInt32();
      for (int i = 0; i < treesCount; i++)
      {
        final String id = iterator.nextZeroTerminatedString();
  
        final double lowerLatitudeDegrees = iterator.nextDouble();
        final double lowerLongitudeDegrees = iterator.nextDouble();
        final double upperLatitudeDegrees = iterator.nextDouble();
        final double upperLongitudeDegrees = iterator.nextDouble();
  
        final Sector sector = Sector.newFromDegrees(lowerLatitudeDegrees, lowerLongitudeDegrees, upperLatitudeDegrees, upperLongitudeDegrees);
  
        final double minZ = iterator.nextDouble();
        final double maxZ = iterator.nextDouble();
  
        XPCNode rootNode = new XPCNode(id, sector, minZ, maxZ);
  
        XPCTree tree = new XPCTree(i, rootNode);
        trees.add(tree);
      }
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