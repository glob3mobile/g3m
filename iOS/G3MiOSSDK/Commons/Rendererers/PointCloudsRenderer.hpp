//
//  PointCloudsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/19/14.
//
//

#ifndef __G3MiOSSDK__PointCloudsRenderer__
#define __G3MiOSSDK__PointCloudsRenderer__

#include "DefaultRenderer.hpp"

#include "IThreadUtils.hpp"
#include "IBufferDownloadListener.hpp"
#include "TimeInterval.hpp"
#include "Vector3D.hpp"
#include "Box.hpp"

class IDownloader;
class Sector;
class Frustum;
class DirectMesh;
class ByteBufferIterator;

class PointCloudsRenderer : public DefaultRenderer {
public:
  enum ColorPolicy {
    MIN_MAX_HEIGHT,
    MIN_AVERAGE3_HEIGHT
  };


  class PointCloudMetadataListener {
  public:
    virtual ~PointCloudMetadataListener() {
    }

    virtual void onMetadata(long long pointsCount,
                            const Sector& sector,
                            double minHeight,
                            double maxHeight,
                            double averageHeight) = 0;
  };


private:

  class PointCloud;

  class PointCloudNode : public RCObject {
  private:
    bool _rendered;
    double _projectedArea;
    long long _lastProjectedAreaTimeInMS;

  protected:

    PointCloudNode(const std::string& id) :
    _id(id),
    _rendered(false),
    _projectedArea(-1),
    _lastProjectedAreaTimeInMS(-1)
    {
    }

    virtual long long rawRender(const PointCloud* pointCloud,
                                const G3MRenderContext* rc,
                                GLState* glState,
                                const Frustum* frustum,
                                const double projectedArea,
                                double minHeight,
                                double maxHeight,
                                float pointSize,
                                long long nowInMS,
                                bool justRecalculatedProjectedArea) = 0;

    virtual ~PointCloudNode() {
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

  public:
    const std::string _id;

    virtual const Box* getBounds() = 0;

    virtual long long getPointsCount() = 0;
    virtual const Vector3D getAverage() = 0;

    long long render(const PointCloud* pointCloud,
                     const G3MRenderContext* rc,
                     GLState* glState,
                     const Frustum* frustum,
                     double minHeight,
                     double maxHeight,
                     float pointSize,
                     long long nowInMS);

    virtual bool isInner() const = 0;

    virtual void stoppedRendering(const G3MRenderContext* rc) = 0;

  };


  class PointCloudLeafNode;

  class PointCloudInnerNode : public PointCloudNode {
  private:
    PointCloudNode* _children[4];

    Box* _bounds;
    Box* calculateBounds();

    Vector3D* _average;
    long long _pointsCount;

    void calculatePointsCountAndAverage();

    Mesh* _mesh;

  protected:
    long long rawRender(const PointCloud* pointCloud,
                        const G3MRenderContext* rc,
                        GLState* glState,
                        const Frustum* frustum,
                        const double projectedArea,
                        double minHeight,
                        double maxHeight,
                        float pointSize,
                        long long nowInMS,
                        bool justRecalculatedProjectedArea);

    ~PointCloudInnerNode();

  public:
    PointCloudInnerNode(const std::string& id) :
    PointCloudNode(id),
    _bounds(NULL),
    _average(NULL),
    _pointsCount(-1),
    _mesh(NULL)
    {
      _children[0] = NULL;
      _children[1] = NULL;
      _children[2] = NULL;
      _children[3] = NULL;
    }

    void addLeafNode(PointCloudLeafNode* leafNode);

    const Box* getBounds() {
      if (_bounds == NULL) {
        _bounds = calculateBounds();
      }
      return _bounds;
    }

    long long getPointsCount() {
      if (_pointsCount <= 0 || _average == NULL) {
        calculatePointsCountAndAverage();
      }
      return _pointsCount;
    }

    const Vector3D getAverage() {
      if (_pointsCount <= 0 || _average == NULL) {
        calculatePointsCountAndAverage();
      }
      return *_average;
    }

    PointCloudInnerNode* pruneUnneededParents();

    bool isInner() const {
      return true;
    }

    void stoppedRendering(const G3MRenderContext* rc);

  };


  class PointCloudLeafNodeLevelParserTask : public GAsyncTask {
  private:
    PointCloudLeafNode* _leafNode;
    const int           _level;

    IByteBuffer* _buffer;

    IFloatBuffer* _verticesBuffer;
    IFloatBuffer* _heightsBuffer;

  public:
    PointCloudLeafNodeLevelParserTask(PointCloudLeafNode* leafNode,
                                      int level,
                                      IByteBuffer* buffer) :
    _leafNode(leafNode),
    _level(level),
    _buffer(buffer),
    _verticesBuffer(NULL),
    _heightsBuffer(NULL)
    {
      _leafNode->_retain();
    }

    ~PointCloudLeafNodeLevelParserTask() {
      _leafNode->_release();
      delete _buffer;

      delete _verticesBuffer;
      delete _heightsBuffer;
    }

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class PointCloudLeafNodeLevelListener : public IBufferDownloadListener {
  private:
    PointCloudLeafNode* _leafNode;
    const int _level;

    const IThreadUtils* _threadUtils;

  public:
    PointCloudLeafNodeLevelListener(PointCloudLeafNode* leafNode,
                                    int level,
                                    const IThreadUtils* threadUtils) :
    _leafNode(leafNode),
    _level(level),
    _threadUtils(threadUtils)
    {
      _leafNode->_retain();
    }


    ~PointCloudLeafNodeLevelListener() {
      _leafNode->_release();
    }

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
      // do nothing
    }

  };
  
  
  class PointCloudLeafNode : public PointCloudNode {
  private:
    const int  _levelsCount;
#ifdef C_CODE
    const int* _levelsPointsCount;
#endif
#ifdef JAVA_CODE
    private final int[] _levelsPointsCount;
#endif
    const Vector3D* _average;
    const Box*      _bounds;
    IFloatBuffer*   _firstPointsVerticesBuffer;
    IFloatBuffer*   _firstPointsHeightsBuffer;
    IFloatBuffer*   _firstPointsColorsBuffer;

    DirectMesh* _mesh;

    long long _pointsCount;

    int _neededLevel;
    int _neededPoints;
    int _preloadedLevel;
    int _currentLoadedLevel;
    int _loadingLevel;
    int calculateCurrentLoadedLevel() const;

    long long _loadingLevelRequestID;

    IFloatBuffer** _levelsVerticesBuffers;
    IFloatBuffer** _levelsHeightsBuffers;

    DirectMesh* createMesh(double minHeight,
                           double maxHeight,
                           float pointSize);

  protected:
    long long rawRender(const PointCloud* pointCloud,
                        const G3MRenderContext* rc,
                        GLState* glState,
                        const Frustum* frustum,
                        const double projectedArea,
                        double minHeight,
                        double maxHeight,
                        float pointSize,
                        long long nowInMS,
                        bool justRecalculatedProjectedArea);

    ~PointCloudLeafNode();

  public:
#ifdef C_CODE
    PointCloudLeafNode(const std::string& id,
                       const int          levelsCount,
                       const int*         levelsPointsCount,
                       const Vector3D*    average,
                       const Box*         bounds,
                       IFloatBuffer*      firstPointsVerticesBuffer,
                       IFloatBuffer*      firstPointsHeightsBuffer) :
    PointCloudNode(id),
    _levelsCount(levelsCount),
    _levelsPointsCount(levelsPointsCount),
    _average(average),
    _bounds(bounds),
    _firstPointsVerticesBuffer(firstPointsVerticesBuffer),
    _firstPointsHeightsBuffer(firstPointsHeightsBuffer),
    _mesh(NULL),
    _pointsCount(-1),
    _neededLevel(0),
    _neededPoints(0),
    _firstPointsColorsBuffer(NULL),
    _loadingLevel(-1),
    _loadingLevelRequestID(-1)
    {
      _currentLoadedLevel = calculateCurrentLoadedLevel();
      _preloadedLevel = _currentLoadedLevel;
      _levelsVerticesBuffers = new IFloatBuffer*[_levelsCount];
      _levelsHeightsBuffers  = new IFloatBuffer*[_levelsCount];
      for (int i = 0; i < _levelsCount; i++) {
        _levelsVerticesBuffers[i] = NULL;
        _levelsHeightsBuffers[i] = NULL;
      }
    }
#endif
#ifdef JAVA_CODE
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
#endif

    const Box* getBounds() {
      return _bounds;
    }

    long long getPointsCount() {
      if (_pointsCount <= 0) {
        _pointsCount = 0;
        for (int i = 0; i < _levelsCount; i++) {
          _pointsCount += _levelsPointsCount[i];
        }
      }
      return _pointsCount;
    }

    const Vector3D getAverage() {
      return *_average;
    }

    bool isInner() const {
      return false;
    }

    void stoppedRendering(const G3MRenderContext* rc);

    void onLevelBuffersDownload(int level,
                                IFloatBuffer* verticesBuffer,
                                IFloatBuffer* heightsBuffer);
    
    void onLevelBufferError(int level);
    void onLevelBufferCancel(int level);

  };



  class PointCloudMetadataParserAsyncTask : public GAsyncTask {
  private:
    PointCloud* _pointCloud;
    IByteBuffer* _buffer;
    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;
    double _averageHeight;

    PointCloudInnerNode* _rootNode;

    PointCloudLeafNode* parseLeafNode(ByteBufferIterator& it);

  public:
    PointCloudMetadataParserAsyncTask(PointCloud* pointCloud,
                                      IByteBuffer* buffer) :
    _pointCloud(pointCloud),
    _buffer(buffer),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0),
    _averageHeight(0),
    _rootNode(NULL)
    {
    }

    ~PointCloudMetadataParserAsyncTask();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

  };


  class PointCloudMetadataDownloadListener : public IBufferDownloadListener {
  private:
    PointCloud*  _pointCloud;
    const IThreadUtils* _threadUtils;

  public:
    PointCloudMetadataDownloadListener(PointCloud* pointCloud,
                                       const IThreadUtils* threadUtils) :
    _pointCloud(pointCloud),
    _threadUtils(threadUtils)
    {
    }

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired);

  };



  class PointCloud {
  private:
#ifdef C_CODE
    const URL         _serverURL;
#endif
#ifdef JAVA_CODE
    private final URL _serverURL;
#endif
    const std::string _cloudName;
    const float _verticalExaggeration;
    const double _deltaHeight;

    const long long    _downloadPriority;
#ifdef C_CODE
    const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
    private final TimeInterval _timeToCache;
#endif
    const bool         _readExpired;

    PointCloudMetadataListener* _metadataListener;
    bool _deleteListener;

    const ColorPolicy _colorPolicy;
    const bool _verbose;

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;
    double _averageHeight;
    const float _pointSize;

    PointCloudInnerNode* _rootNode;

    long long _lastRenderedCount;

  public:
    PointCloud(const URL& serverURL,
               const std::string& cloudName,
               float verticalExaggeration,
               double deltaHeight,
               ColorPolicy colorPolicy,
               float pointSize,
               long long downloadPriority,
               const TimeInterval& timeToCache,
               bool readExpired,
               PointCloudMetadataListener* metadataListener,
               bool deleteListener,
               bool verbose) :
    _serverURL(serverURL),
    _cloudName(cloudName),
    _verticalExaggeration(verticalExaggeration),
    _deltaHeight(deltaHeight),
    _colorPolicy(colorPolicy),
    _pointSize(pointSize),
    _downloadPriority(downloadPriority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _metadataListener(metadataListener),
    _deleteListener(deleteListener),
    _verbose(verbose),
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0),
    _averageHeight(0),
    _rootNode(NULL),
    _lastRenderedCount(0)
    {
    }

    ~PointCloud();

    const std::string getCloudName() const {
      return _cloudName;
    }

    void initialize(const G3MContext* context);

    RenderState getRenderState(const G3MRenderContext* rc);

    void errorDownloadingMetadata();

    void parsedMetadata(long long pointsCount,
                        Sector* sector,
                        double minHeight,
                        double maxHeight,
                        double averageHeight,
                        PointCloudInnerNode* rootNode);

    void render(const G3MRenderContext* rc,
                GLState* glState,
                const Frustum* frustum,
                long long nowInMS);

    long long requestBufferForLevel(const G3MRenderContext* rc,
                                    const std::string& nodeID,
                                    int level,
                                    IBufferDownloadListener *listener,
                                    bool deleteListener) const;

  };

  ITimer* _timer;

  std::vector<PointCloud*> _clouds;
  int _cloudsSize;
  std::vector<std::string> _errors;

  GLState* _glState;


protected:
  void onChangedContext();

public:

  PointCloudsRenderer();

  ~PointCloudsRenderer();

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     ColorPolicy colorPolicy,
                     float pointSize = 2.0f,
                     float verticalExaggeration = 1.0f,
                     double deltaHeight = 0,
                     PointCloudMetadataListener* metadataListener = NULL,
                     bool deleteListener = true,
                     bool verbose = false);

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     long long downloadPriority,
                     const TimeInterval& timeToCache,
                     bool readExpired,
                     ColorPolicy colorPolicy,
                     float pointSize = 2.0f,
                     float verticalExaggeration = 1.0f,
                     double deltaHeight = 0,
                     PointCloudMetadataListener* metadataListener = NULL,
                     bool deleteListener = true,
                     bool verbose = false);
  
  void removeAllPointClouds();

};

#endif
