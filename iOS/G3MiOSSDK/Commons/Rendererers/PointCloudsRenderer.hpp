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

class PointCloudsRenderer : public DefaultRenderer {
public:

  class PointCloudMetadataListener {
  public:
    virtual ~PointCloudMetadataListener() {
    }

    virtual void onMetadata(long long pointsCount,
                            const Sector& sector,
                            double minHeight,
                            double maxHeight) = 0;
  };

  
private:


  class PointCloudNode {
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

    virtual long long rawRender(const G3MRenderContext* rc,
                                GLState* glState,
                                const Frustum* frustum,
                                const double projectedArea,
                                double minHeight,
                                double maxHeight,
                                long long nowInMS,
                                bool justRecalculatedProjectedArea) = 0;

  public:
    const std::string _id;

    virtual ~PointCloudNode() {
    }

    virtual const Box* getBounds() = 0;

    virtual long long getPointsCount() = 0;
    virtual const Vector3D getAverage() = 0;

    long long render(const G3MRenderContext* rc,
                     GLState* glState,
                     const Frustum* frustum,
                     double minHeight,
                     double maxHeight,
                     long long nowInMS);

    virtual bool isInner() const = 0;

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
    long long rawRender(const G3MRenderContext* rc,
                        GLState* glState,
                        const Frustum* frustum,
                        const double projectedArea,
                        double minHeight,
                        double maxHeight,
                        long long nowInMS,
                        bool justRecalculatedProjectedArea);

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

    virtual ~PointCloudInnerNode();

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

  };


  class PointCloudLeafNode : public PointCloudNode {
  private:
    const int  _levelsCountLenght;
#ifdef C_CODE
    const int* _levelsCount;
#endif
#ifdef JAVA_CODE
    private final int[] _levelsCount;
#endif
    const Vector3D* _average;
    const Box*      _bounds;
    IFloatBuffer*  _firstPointsBuffer;

    Mesh* _mesh;

    long long _pointsCount;

    int _neededLevel;

  protected:
    long long rawRender(const G3MRenderContext* rc,
                        GLState* glState,
                        const Frustum* frustum,
                        const double projectedArea,
                        double minHeight,
                        double maxHeight,
                        long long nowInMS,
                        bool justRecalculatedProjectedArea);

  public:
#ifdef C_CODE
    PointCloudLeafNode(const std::string& id,
                       const int          levelsCountLenght,
                       const int*         levelsCount,
                       const Vector3D*    average,
                       const Box*         bounds,
                       IFloatBuffer*      firstPointsBuffer) :
    PointCloudNode(id),
    _levelsCountLenght(levelsCountLenght),
    _levelsCount(levelsCount),
    _average(average),
    _bounds(bounds),
    _firstPointsBuffer(firstPointsBuffer),
    _mesh(NULL),
    _pointsCount(-1),
    _neededLevel(0)
    {
    }
#endif
#ifdef JAVA_CODE
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
#endif

    ~PointCloudLeafNode();

    const Box* getBounds() {
      return _bounds;
    }

    long long getPointsCount() {
      if (_pointsCount <= 0) {
        _pointsCount = 0;
        for (int i = 0; i < _levelsCountLenght; i++) {
          _pointsCount += _levelsCount[i];
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

  };


  class PointCloud;


  class PointCloudMetadataParserAsyncTask : public GAsyncTask {
  private:
    PointCloud* _pointCloud;
    IByteBuffer* _buffer;
    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;

    PointCloudInnerNode* _rootNode;

  public:
    PointCloudMetadataParserAsyncTask(PointCloud* pointCloud,
                                      IByteBuffer* buffer) :
    _pointCloud(pointCloud),
    _buffer(buffer),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0),
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

    bool _downloadingMetadata;
    bool _errorDownloadingMetadata;
    bool _errorParsingMetadata;

    long long _pointsCount;
    Sector* _sector;
    double _minHeight;
    double _maxHeight;
    PointCloudInnerNode* _rootNode;

    long long _lastRenderedCount;

  public:
    PointCloud(const URL& serverURL,
               const std::string& cloudName,
               long long downloadPriority,
               const TimeInterval& timeToCache,
               bool readExpired,
               PointCloudMetadataListener* metadataListener,
               bool deleteListener) :
    _serverURL(serverURL),
    _cloudName(cloudName),
    _downloadPriority(downloadPriority),
    _timeToCache(timeToCache),
    _readExpired(readExpired),
    _metadataListener(metadataListener),
    _deleteListener(deleteListener),
    _downloadingMetadata(false),
    _errorDownloadingMetadata(false),
    _errorParsingMetadata(false),
    _pointsCount(-1),
    _sector(NULL),
    _minHeight(0),
    _maxHeight(0),
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
                        PointCloudInnerNode* rootNode);

    void render(const G3MRenderContext* rc,
                GLState* glState,
                const Frustum* frustum,
                long long nowInMS);

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
                     PointCloudMetadataListener* metadataListener = NULL,
                     bool deleteListener = true);

  void addPointCloud(const URL& serverURL,
                     const std::string& cloudName,
                     long long downloadPriority,
                     const TimeInterval& timeToCache,
                     bool readExpired,
                     PointCloudMetadataListener* metadataListener = NULL,
                     bool deleteListener = true);
  
  void removeAllPointClouds();
  
};

#endif
