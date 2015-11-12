package org.glob3.mobile.generated; 
//
//  PointCloudsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//




//class IDownloader;
//class Sector;
//class Frustum;
//class DirectMesh;
//class ByteBufferIterator;

public class PointCloudsRenderer extends DefaultRenderer
{
  public enum ColorPolicy
  {
    MIN_MAX_HEIGHT,
    MIN_AVERAGE3_HEIGHT;

     public int getValue()
     {
        return this.ordinal();
     }

     public static ColorPolicy forValue(int value)
     {
        return values()[value];
     }
  }


  public abstract static class PointCloudMetadataListener
  {
    public void dispose()
    {
    }

    public abstract void onMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight, double averageHeight);
  }



//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloud;

  private abstract static class PointCloudNode extends RCObject
  {
    private boolean _rendered;
    private double _projectedArea;
    private long _lastProjectedAreaTimeInMS;


    protected PointCloudNode(String id)
    {
       _id = id;
       _rendered = false;
       _projectedArea = -1;
       _lastProjectedAreaTimeInMS = -1;
    }

    protected abstract long rawRender(PointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight, float pointSize, long nowInMS, boolean justRecalculatedProjectedArea);

    public void dispose()
    {
      super.dispose();
    }

    public final String _id;

    public abstract Box getBounds();

    public abstract long getPointsCount();
    public abstract Vector3D getAverage();

    public final long render(PointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, double minHeight, double maxHeight, float pointSize, long nowInMS)
    {
      final Box bounds = getBounds();
      if (bounds != null)
      {
        if (bounds.touchesFrustum(frustum))
        {
          boolean justRecalculatedProjectedArea = false;
          if ((_projectedArea == -1) || ((_lastProjectedAreaTimeInMS + 350) < nowInMS))
          {
            final double currentProjectedArea = bounds.projectedArea(rc);
            if (currentProjectedArea != _projectedArea)
            {
              _projectedArea = currentProjectedArea;
              _lastProjectedAreaTimeInMS = nowInMS;
              justRecalculatedProjectedArea = true;
            }
          }
    
    // #warning TODO: quality factor 1
          final double minProjectedArea = 250;
          if (_projectedArea >= minProjectedArea)
          {
            final long renderedCount = rawRender(pointCloud, rc, glState, frustum, _projectedArea, minHeight, maxHeight, pointSize, nowInMS, justRecalculatedProjectedArea);
            _rendered = true;
            return renderedCount;
          }
        }
      }
    
      if (_rendered)
      {
        stoppedRendering(rc);
        _rendered = false;
      }
    
      return 0;
    }

    public abstract boolean isInner();

    public abstract void stoppedRendering(G3MRenderContext rc);

  }


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloudLeafNode;

  private static class PointCloudInnerNode extends PointCloudNode
  {
    private PointCloudNode[] _children = new PointCloudNode[4];

    private Box _bounds;
    private Box calculateBounds()
    {
      Box bounds = null;
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          final Box childBounds = child.getBounds();
          if (childBounds == null)
          {
            throw new RuntimeException("Logic error");
          }
          if (bounds == null)
          {
            bounds = childBounds;
          }
          else
          {
            if (!childBounds.fullContainedInBox(bounds))
            {
              bounds = bounds.mergedWithBox(childBounds);
            }
          }
        }
      }
      return bounds;
    }

    private Vector3D _average;
    private long _pointsCount;

    private void calculatePointsCountAndAverage()
    {
    
      _pointsCount = 0;
      double sumX = 0;
      double sumY = 0;
      double sumZ = 0;
    
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          final long childPointsCount = child.getPointsCount();
          _pointsCount += childPointsCount;
    
          final Vector3D childAverage = child.getAverage();
          sumX += (childAverage._x * childPointsCount);
          sumY += (childAverage._y * childPointsCount);
          sumZ += (childAverage._z * childPointsCount);
        }
      }
    
      _average = new Vector3D(sumX / _pointsCount, sumY / _pointsCount, sumZ / _pointsCount);
    }

    private Mesh _mesh;

    protected final long rawRender(PointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight, float pointSize, long nowInMS, boolean justRecalculatedProjectedArea)
    {
      long renderedCount = 0;
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          renderedCount += child.render(pointCloud, rc, glState, frustum, minHeight, maxHeight, pointSize, nowInMS);
        }
      }
    
      if (renderedCount == 0)
      {
        if (_mesh == null)
        {
          final Vector3D average = getAverage();
    
          final float averageX = (float) average._x;
          final float averageY = (float) average._y;
          final float averageZ = (float) average._z;
    
          IFloatBuffer pointsBuffer = rc.getFactory().createFloatBuffer(3);
          pointsBuffer.rawPut(0, (float)(average._x - averageX));
          pointsBuffer.rawPut(1, (float)(average._y - averageY));
          pointsBuffer.rawPut(2, (float)(average._z - averageZ));
    
          _mesh = new DirectMesh(GLPrimitive.points(), true, new Vector3D(averageX, averageY, averageZ), pointsBuffer, 1, pointSize * 2, Color.newFromRGBA(1, 1, 0, 1), null, 1, true); // colorsIntensity -  colors -  flatColor
        }
        _mesh.render(rc, glState);
        renderedCount = 1;
      }
      else
      {
        if (_mesh != null)
        {
          if (_mesh != null)
             _mesh.dispose();
          _mesh = null;
        }
      }
    
      return renderedCount;
    }

    public void dispose()
    {
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          child._release();
        }
      }
    
      if (_bounds != null)
         _bounds.dispose();
      if (_average != null)
         _average.dispose();
    
      if (_mesh != null)
         _mesh.dispose();
    
      super.dispose();
    }

    public PointCloudInnerNode(String id)
    {
       super(id);
       _bounds = null;
       _average = null;
       _pointsCount = -1;
       _mesh = null;
      _children[0] = null;
      _children[1] = null;
      _children[2] = null;
      _children[3] = null;
    }

    public final void addLeafNode(PointCloudLeafNode leafNode)
    {
      final int idLength = _id.length();
      final int childIndex = leafNode._id.charAt(idLength) - '0';
      if ((idLength + 1) == leafNode._id.length())
      {
        if (_children[childIndex] != null)
        {
          throw new RuntimeException("Logic error!");
        }
        _children[childIndex] = leafNode;
      }
      else
      {
        PointCloudInnerNode innerChild = (PointCloudInnerNode)(_children[childIndex]);
        if (innerChild == null)
        {
          IStringBuilder isb = IStringBuilder.newStringBuilder();
          isb.addString(_id);
          isb.addInt(childIndex);
          final String childID = isb.getString();
          if (isb != null)
             isb.dispose();
    
          innerChild = new PointCloudInnerNode(childID);
          _children[childIndex] = innerChild;
        }
        innerChild.addLeafNode(leafNode);
      }
    }

    public final Box getBounds()
    {
      if (_bounds == null)
      {
        _bounds = calculateBounds();
      }
      return _bounds;
    }

    public final long getPointsCount()
    {
      if (_pointsCount <= 0 || _average == null)
      {
        calculatePointsCountAndAverage();
      }
      return _pointsCount;
    }

    public final Vector3D getAverage()
    {
      if (_pointsCount <= 0 || _average == null)
      {
        calculatePointsCountAndAverage();
      }
      return _average;
    }

    public final PointCloudsRenderer.PointCloudInnerNode pruneUnneededParents()
    {
      PointCloudNode onlyChild = null;
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          if (onlyChild == null)
          {
            onlyChild = child;
          }
          else
          {
            return this;
          }
        }
      }
    
      if (onlyChild != null)
      {
        if (onlyChild.isInner())
        {
          PointCloudInnerNode result = ((PointCloudInnerNode) onlyChild).pruneUnneededParents();
    
          // forget childrens to avoid deleting them from the destructor
          for (int i = 0; i < 4; i++)
          {
             _children[i] = null;
          }
          _release();
    
          return result;
        }
      }
    
      return this;
    }

    public final boolean isInner()
    {
      return true;
    }

    public final void stoppedRendering(G3MRenderContext rc)
    {
      if (_mesh != null)
         _mesh.dispose();
      _mesh = null;
    
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          child.stoppedRendering(rc);
        }
      }
    }

  }


  private static class PointCloudLeafNodeLevelParserTask extends GAsyncTask
  {
    private PointCloudLeafNode _leafNode;
    private final int _level;

    private IByteBuffer _buffer;

    private IFloatBuffer _verticesBuffer;
    private IFloatBuffer _heightsBuffer;

    public PointCloudLeafNodeLevelParserTask(PointCloudLeafNode leafNode, int level, IByteBuffer buffer)
    {
       _leafNode = leafNode;
       _level = level;
       _buffer = buffer;
       _verticesBuffer = null;
       _heightsBuffer = null;
      _leafNode._retain();
    }

    public void dispose()
    {
      _leafNode._release();
      if (_buffer != null)
         _buffer.dispose();

      if (_verticesBuffer != null)
         _verticesBuffer.dispose();
      if (_heightsBuffer != null)
         _heightsBuffer.dispose();
    }

    public final void runInBackground(G3MContext context)
    {
      ByteBufferIterator it = new ByteBufferIterator(_buffer);
    
      final int pointsCount = it.nextInt32();
    
      _verticesBuffer = IFactory.instance().createFloatBuffer(pointsCount * 3);
      for (int i = 0; i < pointsCount * 3; i++)
      {
        _verticesBuffer.rawPut(i, it.nextFloat());
      }
    
      _heightsBuffer = IFactory.instance().createFloatBuffer(pointsCount);
      for (int i = 0; i < pointsCount; i++)
      {
        _heightsBuffer.rawPut(i, it.nextFloat());
      }
    
      if (it.hasNext())
      {
        ILogger.instance().logError("Logic error");
      }
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    }

    public final void onPostExecute(G3MContext context)
    {
      _leafNode.onLevelBuffersDownload(_level, _verticesBuffer, _heightsBuffer);
      _verticesBuffer = null; // moves ownership to _leafNode
      _heightsBuffer = null; // moves ownership to _leafNode
    }

  }


  private static class PointCloudLeafNodeLevelListener extends IBufferDownloadListener
  {
    private PointCloudLeafNode _leafNode;
    private final int _level;

    private final IThreadUtils _threadUtils;

    public PointCloudLeafNodeLevelListener(PointCloudLeafNode leafNode, int level, IThreadUtils threadUtils)
    {
       _leafNode = leafNode;
       _level = level;
       _threadUtils = threadUtils;
      _leafNode._retain();
    }


    public void dispose()
    {
      _leafNode._release();
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      _threadUtils.invokeAsyncTask(new PointCloudLeafNodeLevelParserTask(_leafNode, _level, buffer), true);
    }

    public final void onError(URL url)
    {
      _leafNode.onLevelBufferError(_level);
    }

    public final void onCancel(URL url)
    {
      _leafNode.onLevelBufferCancel(_level);
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }


  private static class PointCloudLeafNode extends PointCloudNode
  {
    private final int _levelsCount;
    private final int[] _levelsPointsCount;
    private final Vector3D _average;
    private final Box _bounds;
    private IFloatBuffer _firstPointsVerticesBuffer;
    private IFloatBuffer _firstPointsHeightsBuffer;
    private IFloatBuffer _firstPointsColorsBuffer;

    private DirectMesh _mesh;

    private long _pointsCount;

    private int _neededLevel;
    private int _neededPoints;
    private int _preloadedLevel;
    private int _currentLoadedLevel;
    private int _loadingLevel;
    private int calculateCurrentLoadedLevel()
    {
      final int loadedPointsCount = _firstPointsVerticesBuffer.size() / 3;
      int accummulated = 0;
      for (int i = 0; i < _levelsCount; i++)
      {
        final int levelPointsCount = _levelsPointsCount[i];
        accummulated += levelPointsCount;
        if (accummulated == loadedPointsCount)
        {
          return i;
        }
      }
      return -1;
    }

    private long _loadingLevelRequestID;

    private IFloatBuffer[] _levelsVerticesBuffers;
    private IFloatBuffer[] _levelsHeightsBuffers;

    private DirectMesh createMesh(double minHeight, double maxHeight, float pointSize)
    {
      final int firstPointsVerticesBufferSize = _firstPointsVerticesBuffer.size();
    
      final Color baseColor = Color.magenta();
      final int wheelSize = 2147483647;
      final IMathUtils mu = IMathUtils.instance();
    
      if (_currentLoadedLevel <= _preloadedLevel)
      {
        final int firstPointsCount = firstPointsVerticesBufferSize / 3;
    
        if (_firstPointsColorsBuffer == null)
        {
          final double deltaHeight = maxHeight - minHeight;
    
          _firstPointsColorsBuffer = IFactory.instance().createFloatBuffer(firstPointsCount * 4);
          for (int i = 0; i < firstPointsCount; i++)
          {
            final float height = _firstPointsHeightsBuffer.get(i);
            final float alpha = (float)((height - minHeight) / deltaHeight);
    
            final Color color = baseColor.wheelStep(wheelSize, mu.round(wheelSize * alpha));
    
            final int i4 = i *4;
            _firstPointsColorsBuffer.rawPut(i4 + 0, color._red);
            _firstPointsColorsBuffer.rawPut(i4 + 1, color._green);
            _firstPointsColorsBuffer.rawPut(i4 + 2, color._blue);
            _firstPointsColorsBuffer.rawPut(i4 + 3, color._alpha);
          }
        }
    
        DirectMesh mesh = new DirectMesh(GLPrimitive.points(), false, _average, _firstPointsVerticesBuffer, 1, pointSize, null, _firstPointsColorsBuffer, 1, true); // colorsIntensity -  colors -  flatColor
        mesh.setRenderVerticesCount(mu.min(_neededPoints, firstPointsCount));
    
        return mesh;
      }
    
      int pointsCount = 0;
      for (int i = 0; i <= _currentLoadedLevel; i++)
      {
        pointsCount += _levelsPointsCount[i];
      }
    
      IFloatBuffer vertices = IFactory.instance().createFloatBuffer(pointsCount * 3);
    
      vertices.rawPut(0, _firstPointsVerticesBuffer);
      int cursor = firstPointsVerticesBufferSize;
      for (int level = _preloadedLevel+1; level <= _currentLoadedLevel; level++)
      {
        IFloatBuffer levelVerticesBuffers = _levelsVerticesBuffers[level];
        if (levelVerticesBuffers != null)
        {
          vertices.rawPut(cursor, levelVerticesBuffers);
          cursor += levelVerticesBuffers.size();
        }
      }
    
      IFloatBuffer colors = IFactory.instance().createFloatBuffer(pointsCount * 4);
      final double deltaHeight = maxHeight - minHeight;
      final int firstPointsCount = firstPointsVerticesBufferSize / 3;
    
      for (int i = 0; i < firstPointsCount; i++)
      {
        final float height = _firstPointsHeightsBuffer.get(i);
        final float alpha = (float)((height - minHeight) / deltaHeight);
    
        final Color color = baseColor.wheelStep(wheelSize, mu.round(wheelSize * alpha));
    
        final int i4 = i *4;
        colors.rawPut(i4 + 0, color._red);
        colors.rawPut(i4 + 1, color._green);
        colors.rawPut(i4 + 2, color._blue);
        colors.rawPut(i4 + 3, color._alpha);
      }
    
      cursor = firstPointsCount * 4;
      for (int level = _preloadedLevel+1; level <= _currentLoadedLevel; level++)
      {
        IFloatBuffer levelHeightsBuffers = _levelsHeightsBuffers[level];
        if (levelHeightsBuffers != null)
        {
          for (int i = 0; i < _levelsPointsCount[level]; i++)
          {
            final float height = levelHeightsBuffers.get(i);
            final float alpha = (float)((height - minHeight) / deltaHeight);
    
            final Color color = baseColor.wheelStep(wheelSize, mu.round(wheelSize * alpha));
    
            final int offset = cursor + i *4;
            colors.rawPut(offset + 0, color._red);
            colors.rawPut(offset + 1, color._green);
            colors.rawPut(offset + 2, color._blue);
            colors.rawPut(offset + 3, color._alpha);
          }
          cursor += _levelsPointsCount[level] * 4;
        }
      }
    
      DirectMesh mesh = new DirectMesh(GLPrimitive.points(), true, _average, vertices, 1, pointSize, null, colors, 1, true); // colorsIntensity -  colors -  flatColor
      // mesh->setRenderVerticesCount( mu->min(_neededPoints, firstPointsCount) );
      mesh.setRenderVerticesCount(pointsCount);
    
      return mesh;
    }

    protected final long rawRender(PointCloud pointCloud, G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight, float pointSize, long nowInMS, boolean justRecalculatedProjectedArea)
    {
    
      if (justRecalculatedProjectedArea)
      {
    // #warning TODO: quality factor 2
        final int intendedPointsCount = IMathUtils.instance().round((float) projectedArea * 0.09f);
        // const int intendedPointsCount = IMathUtils::instance()->round((float) projectedArea * 0.25f);
        int accummulated = 0;
        int neededLevel = -1;
        int neededPoints = -1;
        for (int i = 0; i < _levelsCount; i++)
        {
          final int levelPointsCount = _levelsPointsCount[i];
          neededPoints = accummulated;
          accummulated += levelPointsCount;
          if (accummulated > intendedPointsCount)
          {
            break;
          }
          neededLevel = i;
        }
    
        if (neededLevel != _neededLevel)
        {
    //      ILogger::instance()->logInfo("Needed Level changed for %s from=%d to=%d, needed points=%d, projectedArea=%f",
    //                                   _id.c_str(),
    //                                   _neededLevel,
    //                                   neededLevel,
    //                                   neededPoints,
    //                                   projectedArea);
          _neededLevel = neededLevel;
          _neededPoints = neededPoints;
          if (_mesh != null)
          {
            _mesh.setRenderVerticesCount(IMathUtils.instance().min(_neededPoints, _mesh.getRenderVerticesCount()));
          }
        }
      }
    
      if ((_loadingLevel >= 0) && (_neededLevel < _loadingLevel) && (_loadingLevelRequestID >= 0))
      {
        rc.getDownloader().cancelRequest(_loadingLevelRequestID);
        _loadingLevelRequestID = -1;
      }
      else
      {
        if (_neededLevel > _currentLoadedLevel)
        {
          if (_loadingLevel < 0)
          {
            _loadingLevel = _currentLoadedLevel + 1;
    
            _loadingLevelRequestID = pointCloud.requestBufferForLevel(rc, _id, _loadingLevel, new PointCloudLeafNodeLevelListener(this, _loadingLevel, rc.getThreadUtils()), true);
          }
        }
        if ((_neededLevel < _currentLoadedLevel) && (_neededLevel > _preloadedLevel))
        {
          for (int i = _neededLevel+1; i <= _currentLoadedLevel; i++)
          {
            if (_levelsVerticesBuffers[i] != null)
               _levelsVerticesBuffers[i].dispose();
            _levelsVerticesBuffers[i] = null;
            if (_levelsHeightsBuffers[i] != null)
               _levelsHeightsBuffers[i].dispose();
            _levelsHeightsBuffers[i] = null;
          }
          _currentLoadedLevel = _neededLevel;
          //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (2)", _id.c_str(), _currentLoadedLevel);
          if (_mesh != null)
             _mesh.dispose();
          _mesh = null;
        }
      }
    
    
      if (_mesh == null)
      {
        _mesh = createMesh(minHeight, maxHeight, pointSize);
      }
      _mesh.render(rc, glState);
    ///#warning remove debug code
    //  getBounds()->render(rc, glState, Color::blue());
      return _mesh.getRenderVerticesCount();
    }

    public void dispose()
    {
      for (int i = 0; i < _levelsCount; i++)
      {
        if (_levelsVerticesBuffers[i] != null)
           _levelsVerticesBuffers[i].dispose();
        if (_levelsHeightsBuffers[i] != null)
           _levelsHeightsBuffers[i].dispose();
      }
      if (_average != null)
         _average.dispose();
      if (_bounds != null)
         _bounds.dispose();
      if (_firstPointsVerticesBuffer != null)
         _firstPointsVerticesBuffer.dispose();
      if (_firstPointsHeightsBuffer != null)
         _firstPointsHeightsBuffer.dispose();
      if (_firstPointsColorsBuffer != null)
         _firstPointsColorsBuffer.dispose();
      if (_mesh != null)
         _mesh.dispose();
    
      super.dispose();
    }

    public PointCloudLeafNode(final String       id,
                              final int          levelsCount,
                              final int[]        levelsPointsCount,
                              final Vector3D     average,
                              final Box          bounds,
                              final IFloatBuffer firstPointsVerticesBuffer,
                              final IFloatBuffer firstPointsHeightsBuffer) {
      super(id);
      _levelsCount = levelsCount;
      _levelsPointsCount = levelsPointsCount;
      _average = average;
      _bounds = bounds;
      _firstPointsVerticesBuffer = firstPointsVerticesBuffer;
      _firstPointsHeightsBuffer = firstPointsHeightsBuffer;
      _mesh = null;
      _pointsCount = -1;
      _firstPointsColorsBuffer = null;
      _loadingLevel = -1;
      _loadingLevelRequestID = -1;
      _currentLoadedLevel = calculateCurrentLoadedLevel();
      _preloadedLevel = _currentLoadedLevel;
      _levelsVerticesBuffers = new IFloatBuffer[_levelsCount];
      _levelsHeightsBuffers  = new IFloatBuffer[_levelsCount];
    }

    public final Box getBounds()
    {
      return _bounds;
    }

    public final long getPointsCount()
    {
      if (_pointsCount <= 0)
      {
        _pointsCount = 0;
        for (int i = 0; i < _levelsCount; i++)
        {
          _pointsCount += _levelsPointsCount[i];
        }
      }
      return _pointsCount;
    }

    public final Vector3D getAverage()
    {
      return _average;
    }

    public final boolean isInner()
    {
      return false;
    }

    public final void stoppedRendering(G3MRenderContext rc)
    {
      if (_loadingLevelRequestID >= 0)
      {
    //    ILogger::instance()->logInfo("Canceling level request");
        rc.getDownloader().cancelRequest(_loadingLevelRequestID);
        _loadingLevelRequestID = -1;
      }
    
      if (_mesh != null)
         _mesh.dispose();
      _mesh = null;
    
      if (_firstPointsColorsBuffer != null)
         _firstPointsColorsBuffer.dispose();
      _firstPointsColorsBuffer = null;
    
      if (_currentLoadedLevel > _preloadedLevel)
      {
        _loadingLevel = -1;
        if (_loadingLevelRequestID >= 0)
        {
          rc.getDownloader().cancelRequest(_loadingLevelRequestID);
          _loadingLevelRequestID = -1;
        }
    
        for (int i = 0; i < _levelsCount; i++)
        {
          if (_levelsVerticesBuffers[i] != null)
             _levelsVerticesBuffers[i].dispose();
          _levelsVerticesBuffers[i] = null;
    
          if (_levelsHeightsBuffers[i] != null)
             _levelsHeightsBuffers[i].dispose();
          _levelsHeightsBuffers[i] = null;
        }
        _currentLoadedLevel = _preloadedLevel;
        //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (3)", _id.c_str(), _currentLoadedLevel);
      }
    }

    public final void onLevelBuffersDownload(int level, IFloatBuffer verticesBuffer, IFloatBuffer heightsBuffer)
    {
      _loadingLevel = -1;
      _loadingLevelRequestID = -1;
    
      final int levelCount = _levelsPointsCount[level];
    
      if ((verticesBuffer.size() / 3 != levelCount) || (heightsBuffer.size() != levelCount))
      {
        ILogger.instance().logError("Invalid buffer size");
        if (verticesBuffer != null)
           verticesBuffer.dispose();
        if (heightsBuffer != null)
           heightsBuffer.dispose();
      }
      else
      {
        //ILogger::instance()->logInfo("-> loaded level %s/%d (needed=%d)",  _id.c_str(), level, _neededLevel);
    
        if ((_levelsVerticesBuffers[level] != null) || (_levelsHeightsBuffers[level] != null))
        {
    //      THROW_EXCEPTION("Logic error");
    
          if (_levelsVerticesBuffers[level] != null)
             _levelsVerticesBuffers[level].dispose();
          _levelsVerticesBuffers[level] = null;
          if (_levelsHeightsBuffers[level] != null)
             _levelsHeightsBuffers[level].dispose();
          _levelsHeightsBuffers[level] = null;
        }
    
        if (_currentLoadedLevel+1 != level)
        {
          if (verticesBuffer != null)
             verticesBuffer.dispose();
          if (heightsBuffer != null)
             heightsBuffer.dispose();
          return;
        }
    
        _levelsVerticesBuffers[level] = verticesBuffer;
        _levelsHeightsBuffers[level] = heightsBuffer;
    
        _currentLoadedLevel = level;
        //ILogger::instance()->logInfo("node %s changed _currentLoadedLevel=%d (1)", _id.c_str(), _currentLoadedLevel);
    
        if (_mesh != null)
           _mesh.dispose();
        _mesh = null;
      }
    
    }

    public final void onLevelBufferError(int level)
    {
      _loadingLevel = -1;
      _loadingLevelRequestID = -1;
    }
    public final void onLevelBufferCancel(int level)
    {
      _loadingLevel = -1;
      _loadingLevelRequestID = -1;
    }

  }



  private static class PointCloudMetadataParserAsyncTask extends GAsyncTask
  {
    private PointCloud _pointCloud;
    private IByteBuffer _buffer;
    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;
    private double _averageHeight;

    private PointCloudInnerNode _rootNode;

    private PointCloudsRenderer.PointCloudLeafNode parseLeafNode(ByteBufferIterator it)
    {
      final int idLength = it.nextUInt8();
      IStringBuilder isb = IStringBuilder.newStringBuilder();
      for (int i = 0; i < idLength; i++)
      {
        isb.addInt(it.nextUInt8());
      }
      final String id = isb.getString();
      if (isb != null)
         isb.dispose();
    
      final int byteLevelsCount = it.nextUInt8();
      final int shortLevelsCount = it.nextUInt8();
      final int intLevelsCount = it.nextUInt8();
      final int levelsCountLength = (int) byteLevelsCount + shortLevelsCount + intLevelsCount;
    
      int[] levelsCount = new int[levelsCountLength];
    
      for (int i = 0; i < byteLevelsCount; i++)
      {
        levelsCount[i] = it.nextUInt8();
      }
      for (int i = 0; i < shortLevelsCount; i++)
      {
        levelsCount[byteLevelsCount + i] = it.nextInt16();
      }
      for (int i = 0; i < intLevelsCount; i++)
      {
        levelsCount[byteLevelsCount + shortLevelsCount + i] = it.nextInt32();
      }
    
      final float averageX = it.nextFloat();
      final float averageY = it.nextFloat();
      final float averageZ = it.nextFloat();
    
      final Vector3D average = new Vector3D(averageX, averageY, averageZ);
    
      final double lowerX = (double) it.nextFloat() + averageX;
      final double lowerY = (double) it.nextFloat() + averageY;
      final double lowerZ = (double) it.nextFloat() + averageZ;
      final double upperX = (double) it.nextFloat() + averageX;
      final double upperY = (double) it.nextFloat() + averageY;
      final double upperZ = (double) it.nextFloat() + averageZ;
      final Box bounds = new Box(new Vector3D(lowerX, lowerY, lowerZ), new Vector3D(upperX, upperY, upperZ));
    
      final int firstPointsCount = it.nextInt32();
      IFloatBuffer firstPointsVerticesBuffer = IFactory.instance().createFloatBuffer(firstPointsCount * 3);
      for (int i = 0; i < firstPointsCount; i++)
      {
        final float x = it.nextFloat();
        final float y = it.nextFloat();
        final float z = it.nextFloat();
        final int i3 = i * 3;
        firstPointsVerticesBuffer.rawPut(i3 + 0, x);
        firstPointsVerticesBuffer.rawPut(i3 + 1, y);
        firstPointsVerticesBuffer.rawPut(i3 + 2, z);
      }
    
      IFloatBuffer firstPointsHeightsBuffer = IFactory.instance().createFloatBuffer(firstPointsCount);
      for (int i = 0; i < firstPointsCount; i++)
      {
        firstPointsHeightsBuffer.rawPut(i, it.nextFloat());
      }
    
      return new PointCloudLeafNode(id, levelsCountLength, levelsCount, average, bounds, firstPointsVerticesBuffer, firstPointsHeightsBuffer);
    }

    public PointCloudMetadataParserAsyncTask(PointCloud pointCloud, IByteBuffer buffer)
    {
       _pointCloud = pointCloud;
       _buffer = buffer;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
       _averageHeight = 0;
       _rootNode = null;
    }

    public void dispose()
    {
      if (_sector != null)
         _sector.dispose();
      if (_buffer != null)
         _buffer.dispose();
    //  delete _rootNode;
      if (_rootNode != null)
      {
        _rootNode._release();
      }
    }

    public final void runInBackground(G3MContext context)
    {
      ByteBufferIterator it = new ByteBufferIterator(_buffer);
    
      _pointsCount = it.nextInt64();
    
      final double lowerLatitude = it.nextDouble();
      final double lowerLongitude = it.nextDouble();
      final double upperLatitude = it.nextDouble();
      final double upperLongitude = it.nextDouble();
    
      _sector = new Sector(Geodetic2D.fromRadians(lowerLatitude, lowerLongitude), Geodetic2D.fromRadians(upperLatitude, upperLongitude));
    
      _minHeight = it.nextDouble();
      _maxHeight = it.nextDouble();
      _averageHeight = it.nextDouble();
    
      final int leafNodesCount = it.nextInt32();
      java.util.ArrayList<PointCloudLeafNode> leafNodes = new java.util.ArrayList<PointCloudLeafNode>();
    
      for (int i = 0; i < leafNodesCount; i++)
      {
        leafNodes.add(parseLeafNode(it));
      }
    
      if (it.hasNext())
      {
        throw new RuntimeException("Logic error");
      }
    
      if (leafNodesCount != leafNodes.size())
      {
        throw new RuntimeException("Logic error");
      }
    
      if (_buffer != null)
         _buffer.dispose();
      _buffer = null;
    
      _rootNode = new PointCloudInnerNode("");
      for (int i = 0; i < leafNodesCount; i++)
      {
        _rootNode.addLeafNode(leafNodes.get(i));
      }
      leafNodes.clear();
    
      _rootNode = _rootNode.pruneUnneededParents();
    
      final Box fullBounds = _rootNode.getBounds(); // force inner-node's bounds here, in background
      ILogger.instance().logInfo("rootNode fullBounds=%s", fullBounds.description());
    
      final Vector3D average = _rootNode.getAverage();
      ILogger.instance().logInfo("rootNode average=%s", average.description());
    }

    public final void onPostExecute(G3MContext context)
    {
      _pointCloud.parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight, _averageHeight, _rootNode);
      _sector = null; // moves ownership to pointCloud
      _rootNode = null; // moves ownership to pointCloud
    }

  }


  private static class PointCloudMetadataDownloadListener extends IBufferDownloadListener
  {
    private PointCloud _pointCloud;
    private final IThreadUtils _threadUtils;

    public PointCloudMetadataDownloadListener(PointCloud pointCloud, IThreadUtils threadUtils)
    {
       _pointCloud = pointCloud;
       _threadUtils = threadUtils;
    }

    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%d)", _pointCloud.getCloudName(), buffer.size());
    
      _threadUtils.invokeAsyncTask(new PointCloudMetadataParserAsyncTask(_pointCloud, buffer), true);
    }

    public final void onError(URL url)
    {
      _pointCloud.errorDownloadingMetadata();
    }

    public final void onCancel(URL url)
    {
      // do nothing
    }

    public final void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      // do nothing
    }

  }



  private static class PointCloud
  {
    private final URL _serverURL;
    private final String _cloudName;
    private final float _verticalExaggeration;
    private final double _deltaHeight;

    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;

    private PointCloudMetadataListener _metadataListener;
    private boolean _deleteListener;

    private final ColorPolicy _colorPolicy;
    private final boolean _verbose;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;
    private double _averageHeight;
    private final float _pointSize;

    private PointCloudInnerNode _rootNode;

    private long _lastRenderedCount;

    public PointCloud(URL serverURL, String cloudName, float verticalExaggeration, double deltaHeight, ColorPolicy colorPolicy, float pointSize, long downloadPriority, TimeInterval timeToCache, boolean readExpired, PointCloudMetadataListener metadataListener, boolean deleteListener, boolean verbose)
    {
       _serverURL = serverURL;
       _cloudName = cloudName;
       _verticalExaggeration = verticalExaggeration;
       _deltaHeight = deltaHeight;
       _colorPolicy = colorPolicy;
       _pointSize = pointSize;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _metadataListener = metadataListener;
       _deleteListener = deleteListener;
       _verbose = verbose;
       _downloadingMetadata = false;
       _errorDownloadingMetadata = false;
       _errorParsingMetadata = false;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
       _averageHeight = 0;
       _rootNode = null;
       _lastRenderedCount = 0;
    }

    public void dispose()
    {
    //  delete _rootNode;
      if (_rootNode != null)
      {
        _rootNode._release();
      }
      if (_sector != null)
         _sector.dispose();
    }

    public final String getCloudName()
    {
      return _cloudName;
    }

    public final void initialize(G3MContext context)
    {
      _downloadingMetadata = true;
      _errorDownloadingMetadata = false;
      _errorParsingMetadata = false;
    
      final String planetType = context.getPlanet().getType();
    
      final URL metadataURL = new URL(_serverURL, _cloudName + "?planet=" + planetType + "&verticalExaggeration=" + IStringUtils.instance().toString(_verticalExaggeration) + "&deltaHeight=" + IStringUtils.instance().toString(_deltaHeight) + "&format=binary");
    
      ILogger.instance().logInfo("Downloading metadata for \"%s\"", _cloudName);
    
      context.getDownloader().requestBuffer(metadataURL, _downloadPriority, _timeToCache, _readExpired, new PointCloudsRenderer.PointCloudMetadataDownloadListener(this, context.getThreadUtils()), true);
    }

    public final RenderState getRenderState(G3MRenderContext rc)
    {
      if (_downloadingMetadata)
      {
        return RenderState.busy();
      }
    
      if (_errorDownloadingMetadata)
      {
        return RenderState.error("Error downloading metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
      }
    
      if (_errorParsingMetadata)
      {
        return RenderState.error("Error parsing metadata of \"" + _cloudName + "\" from \"" + _serverURL.getPath() + "\"");
      }
    
      return RenderState.ready();
    }

    public final void errorDownloadingMetadata()
    {
      _downloadingMetadata = false;
      _errorDownloadingMetadata = true;
    }

    public final void parsedMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight, double averageHeight, PointCloudInnerNode rootNode)
    {
      _pointsCount = pointsCount;
      _sector = sector;
      _minHeight = minHeight;
      _maxHeight = maxHeight;
      _averageHeight = averageHeight;
    
      _downloadingMetadata = false;
      _rootNode = rootNode;
    
      ILogger.instance().logInfo("Parsed metadata for \"%s\"", _cloudName);
    
      if (_metadataListener != null)
      {
        _metadataListener.onMetadata(_pointsCount, sector, _minHeight, _maxHeight, _averageHeight);
        if (_deleteListener)
        {
          if (_metadataListener != null)
             _metadataListener.dispose();
        }
        _metadataListener = null;
      }
    
    }

    public final void render(G3MRenderContext rc, GLState glState, Frustum frustum, long nowInMS)
    {
      if (_rootNode != null)
      {
    // #warning TODO: make plugable the colorization of the cloud
        final double maxHeight = (_colorPolicy == ColorPolicy.MIN_MAX_HEIGHT) ? _maxHeight : _averageHeight * 3;
    
        final long renderedCount = _rootNode.render(this, rc, glState, frustum, _minHeight, maxHeight, _pointSize, nowInMS);
        // const long long renderedCount = _rootNode->render(this, rc, glState, frustum, _minHeight, _averageHeight * 3, _pointSize, nowInMS);
        // const long long renderedCount = _rootNode->render(this, rc, glState, frustum, _minHeight, _maxHeight, _pointSize, nowInMS);
    
        if (_lastRenderedCount != renderedCount)
        {
          if (_verbose)
          {
            ILogger.instance().logInfo("\"%s\": Rendered %d points", _cloudName, renderedCount);
          }
          _lastRenderedCount = renderedCount;
        }
      }
    }

    public final long requestBufferForLevel(G3MRenderContext rc, String nodeID, int level, IBufferDownloadListener listener, boolean deleteListener)
    {
    
      final String planetType = rc.getPlanet().getType();
    
      final URL url = new URL(_serverURL, _cloudName + "/" + nodeID + "/" + IStringUtils.instance().toString(level) + "?planet=" + planetType + "&verticalExaggeration=" + IStringUtils.instance().toString(_verticalExaggeration) + "&deltaHeight=" + IStringUtils.instance().toString(_deltaHeight) + "&format=binary");
    
      //  ILogger::instance()->logInfo("Downloading metadata for \"%s\"", _cloudName.c_str());
    
      return rc.getDownloader().requestBuffer(url, _downloadPriority - level, _timeToCache, _readExpired, listener, deleteListener);
    }

  }

  private ITimer _timer;

  private java.util.ArrayList<PointCloud> _clouds = new java.util.ArrayList<PointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private GLState _glState;


  protected final void onChangedContext()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      cloud.initialize(_context);
    }
  }


  public PointCloudsRenderer()
  {
     _cloudsSize = 0;
     _glState = new GLState();
     _timer = null;
  }

  public void dispose()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
  
    _glState._release();
    if (_timer != null)
       _timer.dispose();
  
    super.dispose();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      final RenderState childRenderState = cloud.getRenderState(rc);
  
      final RenderState_Type childRenderStateType = childRenderState._type;
  
      if (childRenderStateType == RenderState_Type.RENDER_ERROR)
      {
        errorFlag = true;
  
        final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
        _errors.addAll(childErrors);
      }
      else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
      {
        busyFlag = true;
      }
    }
  
    if (errorFlag)
    {
      return RenderState.error(_errors);
    }
    else if (busyFlag)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_cloudsSize > 0)
    {
  //    const IDeviceInfo* deviceInfo = IFactory::instance()->getDeviceInfo();
  //    const float deviceQualityFactor = deviceInfo->getQualityFactor();
  ////    const double factor = _tilesRenderParameters->_texturePixelsPerInch; //UNIT: Dots / Inch^2 (ppi)
  ////    const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / factor;
  //    const double correctionFactor = (deviceInfo->getDPI() * deviceQualityFactor) / 256;
  
      if (_timer == null)
      {
        _timer = rc.getFactory().createTimer();
      }
      final long nowInMS = _timer.elapsedTimeInMilliseconds();
  
      final Camera camera = rc.getCurrentCamera();
      ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
      if (f == null)
      {
        _glState.addGLFeature(new ModelViewGLFeature(camera), true);
      }
      else
      {
        f.setMatrix(camera.getModelViewMatrix44D());
      }
  
      _glState.setParent(glState);
  
      final Frustum frustum = camera.getFrustumInModelCoordinates();
      for (int i = 0; i < _cloudsSize; i++)
      {
        PointCloud cloud = _clouds.get(i);
        cloud.render(rc, _glState, frustum, nowInMS);
      }
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, pointSize, verticalExaggeration, deltaHeight, metadataListener, deleteListener, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, pointSize, verticalExaggeration, deltaHeight, metadataListener, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, pointSize, verticalExaggeration, deltaHeight, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, pointSize, verticalExaggeration, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, pointSize, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy)
  {
     addPointCloud(serverURL, cloudName, colorPolicy, 2.0f, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener, boolean deleteListener, boolean verbose)
  {
    addPointCloud(serverURL, cloudName, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, colorPolicy, pointSize, verticalExaggeration, deltaHeight, metadataListener, deleteListener, verbose);
  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, pointSize, verticalExaggeration, deltaHeight, metadataListener, deleteListener, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, pointSize, verticalExaggeration, deltaHeight, metadataListener, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, pointSize, verticalExaggeration, deltaHeight, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, pointSize, verticalExaggeration, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, pointSize, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, colorPolicy, 2.0f, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, ColorPolicy colorPolicy, float pointSize, float verticalExaggeration, double deltaHeight, PointCloudMetadataListener metadataListener, boolean deleteListener, boolean verbose)
  {
    PointCloud pointCloud = new PointCloud(serverURL, cloudName, verticalExaggeration, deltaHeight, colorPolicy, pointSize, downloadPriority, timeToCache, readExpired, metadataListener, deleteListener, verbose);
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
  }

  public final void removeAllPointClouds()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      PointCloud cloud = _clouds.get(i);
      if (cloud != null)
         cloud.dispose();
    }
    _clouds.clear();
    _cloudsSize = _clouds.size();
  }

}