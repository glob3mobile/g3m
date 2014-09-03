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

public class PointCloudsRenderer extends DefaultRenderer
{

  public abstract static class PointCloudMetadataListener
  {
    public void dispose()
    {
    }

    public abstract void onMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight);
  }


//  class PointCloudInnerNode;
//  class PointCloudLeafNode;
//
//  class PointCloudNodeVisitor {
//  public:
//    virtual ~PointCloudNodeVisitor() {
//    }
//
//    virtual void visitInnerNode(const PointCloudInnerNode* innerNode) = 0;
//    virtual void visitLeafNode(const PointCloudLeafNode* leafNode) = 0;
//  };




  private abstract static class PointCloudNode
  {
    private boolean _rendered;
    private double _projectedArea;
    private ITimer _projectedAreaTimer;


    protected PointCloudNode(String id)
    {
       _id = id;
       _rendered = false;
       _projectedArea = -1;
       _projectedAreaTimer = null;
    }

    protected abstract long rawRender(G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight);

    public final String _id;

    public void dispose()
    {
    }

    public abstract Box getBounds();

    public abstract long getPointsCount();
    public abstract Vector3D getAverage();

//    virtual void acceptVisitor(PointCloudNodeVisitor* visitor) = 0;

    public final long render(G3MRenderContext rc, GLState glState, Frustum frustum, double minHeight, double maxHeight)
    {
      final Box bounds = getBounds();
      if (bounds != null)
      {
        if (bounds.touchesFrustum(frustum))
        {
          if ((_projectedAreaTimer == null) || (_projectedAreaTimer.elapsedTimeInMilliseconds() > 500))
          {
            _projectedArea = bounds.projectedArea(rc);
            if (_projectedAreaTimer == null)
            {
              _projectedAreaTimer = rc.getFactory().createTimer();
            }
            else
            {
              _projectedAreaTimer.start();
            }
          }
    
          final double minProjectedArea = 500;
          if (_projectedArea >= minProjectedArea)
          {
            final long renderedCount = rawRender(rc, glState, frustum, _projectedArea, minHeight, maxHeight);
            _rendered = true;
            return renderedCount;
          }
        }
      }
    
      if (_rendered)
      {
        _rendered = false;
        if (_projectedAreaTimer != null)
           _projectedAreaTimer.dispose();
        _projectedAreaTimer = null;
      }
    
      return 0;
    }

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

    protected final long rawRender(G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight)
    {
      long renderedCount = 0;
      for (int i = 0; i < 4; i++)
      {
        PointCloudNode child = _children[i];
        if (child != null)
        {
          renderedCount += child.render(rc, glState, frustum, minHeight, maxHeight);
        }
      }
    
      if (renderedCount == 0)
      {
        //_bounds->render(rc, glState, _renderColor);
        if (_mesh == null)
        {
          final Vector3D average = getAverage();
    
          final float averageX = (float) average._x;
          final float averageY = (float) average._y;
          final float averageZ = (float) average._z;
    
          IFloatBuffer pointsBuffer = rc.getFactory().createFloatBuffer(3);
          pointsBuffer.put(0, (float)(average._x - averageX));
          pointsBuffer.put(1, (float)(average._y - averageY));
          pointsBuffer.put(2, (float)(average._z - averageZ));
    
          _mesh = new DirectMesh(GLPrimitive.points(), true, new Vector3D(averageX, averageY, averageZ), pointsBuffer, 1, 2, Color.newFromRGBA(1, 1, 0, 1), null, 1, false); // colorsIntensity -  colors
        }
        _mesh.render(rc, glState);
        renderedCount = 1;
      }
    
      return renderedCount;
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


    //void PointCloudsRenderer::PointCloudInnerNode::acceptVisitor(PointCloudNodeVisitor* visitor) {
    //  visitor->visitInnerNode(this);
    //
    //  if (_children[0] != NULL) { _children[0]->acceptVisitor(visitor); }
    //  if (_children[1] != NULL) { _children[1]->acceptVisitor(visitor); }
    //  if (_children[2] != NULL) { _children[2]->acceptVisitor(visitor); }
    //  if (_children[3] != NULL) { _children[3]->acceptVisitor(visitor); }
    //}
    //
    //void PointCloudsRenderer::PointCloudLeafNode::acceptVisitor(PointCloudNodeVisitor* visitor) {
    //  visitor->visitLeafNode(this);
    //}
    
    
    public void dispose()
    {
      if (_children[0] != null)
         _children[0].dispose();
      if (_children[1] != null)
         _children[1].dispose();
      if (_children[2] != null)
         _children[2].dispose();
      if (_children[3] != null)
         _children[3].dispose();
    
      if (_bounds != null)
         _bounds.dispose();
      if (_average != null)
         _average.dispose();
    
      if (_mesh != null)
         _mesh.dispose();
    
      super.dispose();
    }

    public final void addLeafNode(PointCloudLeafNode leafNode)
    {
      final int idLenght = _id.length();
      final int childIndex = leafNode._id.charAt(idLenght) - '0';
      if ((idLenght + 1) == leafNode._id.length())
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

//    void acceptVisitor(PointCloudNodeVisitor* visitor);

  }


  private static class PointCloudLeafNode extends PointCloudNode
  {
    private final int _levelsCountLenght;
    private final int[] _levelsCount;
    private final Vector3D _average;
    private final Box _bounds;
    private IFloatBuffer _firstPointsBuffer;

    private Mesh _mesh;

    private long _pointsCount;

    protected final long rawRender(G3MRenderContext rc, GLState glState, Frustum frustum, double projectedArea, double minHeight, double maxHeight)
    {
      if (_mesh == null)
      {
        _mesh = new DirectMesh(GLPrimitive.points(), false, _average, _firstPointsBuffer, 1, 2, Color.newFromRGBA(1, 1, 1, 1), null, 1, false); // colorsIntensity -  colors
      }
      _mesh.render(rc, glState);
      return _firstPointsBuffer.size();
    }


    public PointCloudLeafNode(final String       id,
                              final int          levelsCountLenght,
                              final int[]        levelsCount,
                              final Vector3D     average,
                              final Box          bounds,
                              final IFloatBuffer firstPointsBuffer) {
      super(id);
      _levelsCountLenght = levelsCountLenght;
      _levelsCount = levelsCount;
      _average = average;
      _bounds = bounds;
      _firstPointsBuffer = firstPointsBuffer;
      _mesh = null;
      _pointsCount = -1;
    }

    public void dispose()
    {
      if (_mesh != null)
         _mesh.dispose();
      if (_average != null)
         _average.dispose();
      if (_bounds != null)
         _bounds.dispose();
      if (_firstPointsBuffer != null)
         _firstPointsBuffer.dispose();
      super.dispose();
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
        for (int i = 0; i < _levelsCountLenght; i++)
        {
          _pointsCount += _levelsCount[i];
        }
      }
      return _pointsCount;
    }

    public final Vector3D getAverage()
    {
      return _average;
    }


//    void acceptVisitor(PointCloudNodeVisitor* visitor);

  }


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following type could not be found.
//  class PointCloud;


  private static class PointCloudMetadataParserAsyncTask extends GAsyncTask
  {
    private PointCloud _pointCloud;
    private IByteBuffer _buffer;
    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;

    private PointCloudInnerNode _octree;

    public PointCloudMetadataParserAsyncTask(PointCloud pointCloud, IByteBuffer buffer)
    {
       _pointCloud = pointCloud;
       _buffer = buffer;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
       _octree = null;
    }

    public void dispose()
    {
      if (_sector != null)
         _sector.dispose();
      if (_buffer != null)
         _buffer.dispose();
      if (_octree != null)
         _octree.dispose();
    }


    //class DebugVisitor : public PointCloudsRenderer::PointCloudNodeVisitor {
    //private:
    //  int _innerNodesCount;
    //  int _leafNodesCount;
    //public:
    //  DebugVisitor() :
    //  _innerNodesCount(0),
    //  _leafNodesCount(0)
    //  {
    //  }
    //
    //  void visitInnerNode(const PointCloudsRenderer::PointCloudInnerNode* innerNode) {
    //    printf("Inner: \"%s\"\n", innerNode->_id.c_str());
    //    _innerNodesCount++;
    //  }
    //
    //  void visitLeafNode(const PointCloudsRenderer::PointCloudLeafNode* leafNode) {
    //    printf(" Leaf: \"%s\"\n", leafNode->_id.c_str());
    //    _leafNodesCount++;
    //  }
    //
    //};
    
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
    
      final int leafNodesCount = it.nextInt32();
      java.util.ArrayList<PointCloudLeafNode> leafNodes = new java.util.ArrayList<PointCloudLeafNode>();
    
      for (int i = 0; i < leafNodesCount; i++)
      {
        final int idLength = it.nextUInt8();
        IStringBuilder isb = IStringBuilder.newStringBuilder();
        for (int j = 0; j < idLength; j++)
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
    
        for (int j = 0; j < byteLevelsCount; j++)
        {
          levelsCount[j] = it.nextUInt8();
        }
        for (int j = 0; j < shortLevelsCount; j++)
        {
          levelsCount[byteLevelsCount + j] = it.nextInt16();
        }
        for (int j = 0; j < intLevelsCount; j++)
        {
          levelsCount[byteLevelsCount + shortLevelsCount + j] = it.nextInt32();
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
        FloatBufferBuilderFromCartesian3D firstPointsBufferBuilder = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
        for (int j = 0; j < firstPointsCount; j++)
        {
          final float x = it.nextFloat();
          final float y = it.nextFloat();
          final float z = it.nextFloat();
          firstPointsBufferBuilder.add(x, y, z);
        }
        IFloatBuffer firstPointsBuffer = firstPointsBufferBuilder.create();
    
        if (firstPointsBufferBuilder != null)
           firstPointsBufferBuilder.dispose();
        firstPointsBufferBuilder = null;
    
        leafNodes.add(new PointCloudLeafNode(id, levelsCountLength, levelsCount, average, bounds, firstPointsBuffer));
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
    
      _octree = new PointCloudInnerNode("");
      for (int i = 0; i < leafNodesCount; i++)
      {
        _octree.addLeafNode(leafNodes.get(i));
      }
      final Box fullBounds = _octree.getBounds(); // force inner-node's bounds here, in background
      ILogger.instance().logInfo("Octree fullBounds=%s", fullBounds.description());
    
      final Vector3D average = _octree.getAverage();
      ILogger.instance().logInfo("Octree average=%s", average.description());
    
      //  PointCloudNodeVisitor* visitor = new DebugVisitor();
      //  _octree->acceptVisitor(visitor);
      //  delete visitor;
    }

    public final void onPostExecute(G3MContext context)
    {
      _pointCloud.parsedMetadata(_pointsCount, _sector, _minHeight, _maxHeight, _octree);
      _sector = null; // moves ownership to pointCloud
      _octree = null; // moves ownership to pointCloud
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


    ///#include "SurfaceElevationProvider.hpp"
    
    public final void onDownload(URL url, IByteBuffer buffer, boolean expired)
    {
      ILogger.instance().logInfo("Downloaded metadata for \"%s\" (bytes=%ld)", _pointCloud.getCloudName(), buffer.size());
    
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

    private final long _downloadPriority;
    private final TimeInterval _timeToCache;
    private final boolean _readExpired;

    private PointCloudMetadataListener _metadataListener;
    private boolean _deleteListener;

    private boolean _downloadingMetadata;
    private boolean _errorDownloadingMetadata;
    private boolean _errorParsingMetadata;

    private long _pointsCount;
    private Sector _sector;
    private double _minHeight;
    private double _maxHeight;
    private PointCloudInnerNode _octree;

    private long _lastRenderedCount;

    public PointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, PointCloudMetadataListener metadataListener, boolean deleteListener)
    {
       _serverURL = serverURL;
       _cloudName = cloudName;
       _downloadPriority = downloadPriority;
       _timeToCache = timeToCache;
       _readExpired = readExpired;
       _metadataListener = metadataListener;
       _deleteListener = deleteListener;
       _downloadingMetadata = false;
       _errorDownloadingMetadata = false;
       _errorParsingMetadata = false;
       _pointsCount = -1;
       _sector = null;
       _minHeight = 0;
       _maxHeight = 0;
       _octree = null;
       _lastRenderedCount = 0;
    }

    public void dispose()
    {
      if (_octree != null)
         _octree.dispose();
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
    //  const float verticalExaggeration = context->getSurfaceElevationProvider()->getVerticalExaggeration();
    
      final URL metadataURL = new URL(_serverURL, _cloudName + "?planet=" + planetType + "&format=binary");
    
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

    public final void parsedMetadata(long pointsCount, Sector sector, double minHeight, double maxHeight, PointCloudInnerNode octree)
    {
      _pointsCount = pointsCount;
      _sector = sector;
      _minHeight = minHeight;
      _maxHeight = maxHeight;
    
      _downloadingMetadata = false;
      _octree = octree;
    
      ILogger.instance().logInfo("Parsed metadata for \"%s\"", _cloudName);
    
      if (_metadataListener != null)
      {
        _metadataListener.onMetadata(pointsCount, sector, minHeight, maxHeight);
        if (_deleteListener)
        {
          if (_metadataListener != null)
             _metadataListener.dispose();
        }
        _metadataListener = null;
      }
    
    }

    public final void render(G3MRenderContext rc, GLState glState, Frustum frustum)
    {
      if (_octree != null)
      {
        final long renderedCount = _octree.render(rc, glState, frustum, _minHeight, _maxHeight);
    
        if (_lastRenderedCount != renderedCount)
        {
          ILogger.instance().logInfo("\"%s\": Rendered %ld points", _cloudName, renderedCount);
          _lastRenderedCount = renderedCount;
        }
      }
    }

  }



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
    final Camera camera = rc.getCurrentCamera();
  
  //  const float verticalExaggeration = rc->getSurfaceElevationProvider()->getVerticalExaggeration();
  
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
      cloud.render(rc, _glState, frustum);
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(URL serverURL, String cloudName, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
    addPointCloud(serverURL, cloudName, DownloadPriority.MEDIUM, TimeInterval.fromDays(30), true, metadataListener, deleteListener);
  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, PointCloudMetadataListener metadataListener, boolean deleteListener)
  {
    PointCloud pointCloud = new PointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, metadataListener, deleteListener);
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