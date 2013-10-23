//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

#include "MeshRenderer.hpp"

#include "Mesh.hpp"
#include "Camera.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IThreadUtils.hpp"
#include "BSONParser.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONArray.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "DirectMesh.hpp"

void MeshRenderer::clearMeshes() {
  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }
  _meshes.clear();
}

MeshRenderer::~MeshRenderer() {
  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }

  _glState->_release();

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void MeshRenderer::updateGLState(const G3MRenderContext* rc) {
  const Camera* cam = rc->getCurrentCamera();

  ModelViewGLFeature* f = (ModelViewGLFeature*) _glState->getGLFeature(GLF_MODEL_VIEW);
  if (f == NULL) {
    _glState->addGLFeature(new ModelViewGLFeature(cam), true);
  }
  else {
    f->setMatrix(cam->getModelViewMatrix44D());
  }
}

void MeshRenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();
  updateGLState(rc);

  _glState->setParent(glState);

  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    const BoundingVolume* boundingVolume = mesh->getBoundingVolume();
    if ( boundingVolume->touchesFrustum(frustum) ) {
      mesh->render(rc, _glState);
    }
  }
}

void MeshRenderer::initialize(const G3MContext* context) {
  _context = context;

  if (_context != NULL) {
    drainLoadQueue();
  }
}

void MeshRenderer::drainLoadQueue() {

  const int loadQueueSize = _loadQueue.size();
  for (int i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    requestBuffer(item->_url,
                  item->_priority,
                  item->_timeToCache,
                  item->_readExpired,
                  item->_pointSize,
                  item->_deltaHeight,
                  item->_listener,
                  item->_deleteListener,
                  item->_isBSON);

    delete item;
  }

  _loadQueue.clear();
}


void MeshRenderer::loadJSONPointCloud(const URL&          url,
                                      long long           priority,
                                      const TimeInterval& timeToCache,
                                      bool                readExpired,
                                      float               pointSize,
                                      double              deltaHeight,
                                      MeshLoadListener*   listener,
                                      bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           pointSize,
                                           deltaHeight,
                                           listener,
                                           deleteListener,
                                           false /* isBson */));
  }
  else {
    requestBuffer(url,
                  priority,
                  timeToCache,
                  readExpired,
                  pointSize,
                  deltaHeight,
                  listener,
                  deleteListener,
                  false /* isBson */);
  }
}

void MeshRenderer::loadBSONPointCloud(const URL&          url,
                                      long long           priority,
                                      const TimeInterval& timeToCache,
                                      bool                readExpired,
                                      float               pointSize,
                                      double              deltaHeight,
                                      MeshLoadListener*   listener,
                                      bool                deleteListener) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           pointSize,
                                           deltaHeight,
                                           listener,
                                           deleteListener,
                                           true /* isBson */));
  }
  else {
    requestBuffer(url,
                  priority,
                  timeToCache,
                  readExpired,
                  pointSize,
                  deltaHeight,
                  listener,
                  deleteListener,
                  true /* isBson */);
  }
}

class MeshRenderer_PointCloudParserAsyncTask : public GAsyncTask {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  MeshRenderer*     _meshRenderer;
#ifdef C_CODE
  const URL          _url;
#endif
#ifdef JAVA_CODE
  public final URL _url;
#endif
  IByteBuffer*      _buffer;
  const float       _pointSize;
  const double      _deltaHeight;
  MeshLoadListener* _listener;
  const bool        _deleteListener;
  const bool        _isBSON;

  Mesh* _mesh;

  float normalize(float value,
                  float max,
                  float min) {
    return (value - min) / (max - min);
  }

  Color interpolateColor(const Color& from,
                         const Color& middle,
                         const Color& to,
                         float d) {
    if (d <= 0) {
      return from;
    }
    if (d >= 1) {
      return to;
    }
    if (d <= 0.5) {
      return from.mixedWith(middle, d * 2);
    }
    return middle.mixedWith(to, (d - 0.5f) * 2);
  }

public:

  MeshRenderer_PointCloudParserAsyncTask(MeshRenderer*     meshRenderer,
                                         const URL&        url,
                                         IByteBuffer*      buffer,
                                         float             pointSize,
                                         double            deltaHeight,
                                         MeshLoadListener* listener,
                                         bool              deleteListener,
                                         bool              isBSON,
                                         const G3MContext* context) :
  _meshRenderer(meshRenderer),
  _url(url),
  _buffer(buffer),
  _pointSize(pointSize),
  _deltaHeight(deltaHeight),
  _listener(listener),
  _deleteListener(deleteListener),
  _isBSON(isBSON),
  _context(context),
  _mesh(NULL)
  {
  }

  void runInBackground(const G3MContext* context) {
    const JSONBaseObject* jsonBaseObject = _isBSON
    /*                                         */ ? BSONParser::parse(_buffer)
    /*                                         */ : IJSONParser::instance()->parse(_buffer);

    if (jsonBaseObject != NULL) {
      const JSONObject* jsonObject = jsonBaseObject->asObject();
      if (jsonObject == NULL) {
        ILogger::instance()->logError("Invalid format for PointCloud");
      }
      else {

//      "size":48
//      ,"bounds":{
//        "lower":[7.1489565398362656,46.34361696257866,1169.652386999709]
//        ,"upper":[7.171002826337067,46.3559190937692,1381.608242625843]
//      }
//      ,"averagePoint":[7.159396937202478,46.34958505936779,1229.5747544618582]
//      ,"points":[ ]
//      ,"colors":[ ]

        const int size = (int) jsonObject->getAsNumber("size", -1);
        if (size <= 0) {
          ILogger::instance()->logError("Invalid size for PointCloud");
        }
        else {
          Geodetic3D* averagePoint = NULL;

          const JSONArray* jsonAveragePoint = jsonObject->getAsArray("averagePoint");
          if ((jsonAveragePoint != NULL) &&
              (jsonAveragePoint->size() == 3)) {
            const double lonInDegrees = jsonAveragePoint->getAsNumber(0, 0);
            const double latInDegrees = jsonAveragePoint->getAsNumber(1, 0);
            const double height       = jsonAveragePoint->getAsNumber(2, 0);

            averagePoint = new Geodetic3D(Angle::fromDegrees(latInDegrees),
                                          Angle::fromDegrees(lonInDegrees),
                                          height + _deltaHeight);
          }
          else {
            ILogger::instance()->logError("Invalid averagePoint for PointCloud");
          }

          const JSONArray* jsonPoints = jsonObject->getAsArray("points");
          if ((jsonPoints == NULL) ||
              (size*3 != jsonPoints->size())) {
            ILogger::instance()->logError("Invalid points for PointCloud");
          }
          else {
            FloatBufferBuilderFromGeodetic verticesBuilder = (averagePoint == NULL)
            /*                 */ ? FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(_context->getPlanet())
            /*                 */ : FloatBufferBuilderFromGeodetic::builderWithGivenCenter(_context->getPlanet(), *averagePoint);

            double minHeight = jsonPoints->getAsNumber(2, 0);
            double maxHeight = minHeight;

            for (int i = 0; i < size*3; i += 3) {
              const double lonInDegrees = jsonPoints->getAsNumber(i    , 0);
              const double latInDegrees = jsonPoints->getAsNumber(i + 1, 0);
              const double height       = jsonPoints->getAsNumber(i + 2, 0);

              verticesBuilder.add(Angle::fromDegrees(latInDegrees),
                                  Angle::fromDegrees(lonInDegrees),
                                  height + _deltaHeight);

              if (height < minHeight) {
                minHeight = height;
              }
              if (height > maxHeight) {
                maxHeight = height;
              }
            }

            IFloatBuffer* colors = NULL;
            const JSONArray* jsonColors = jsonObject->getAsArray("colors");
            if (jsonColors == NULL) {
              const Color fromColor   = Color::red();
              const Color middleColor = Color::green();
              const Color toColor     = Color::blue();
              FloatBufferBuilderFromColor colorsBuilder;

              for (int i = 0; i < size*3; i += 3) {
                const double height = jsonPoints->getAsNumber(i + 2, 0);

                const Color interpolatedColor = interpolateColor(fromColor, middleColor, toColor,
                                                                 normalize((float) height, (float) minHeight, (float) maxHeight));
                colorsBuilder.add(interpolatedColor);
              }

              colors = colorsBuilder.create();
            }
            else {
              FloatBufferBuilderFromColor colorsBuilder;

              const int colorsSize = jsonColors->size();
              for (int i = 0; i < colorsSize; i += 3) {
                const int red   = (int) jsonColors->getAsNumber(i    , 0);
                const int green = (int) jsonColors->getAsNumber(i + 1, 0);
                const int blue  = (int) jsonColors->getAsNumber(i + 2, 0);

                colorsBuilder.addBase255(red, green, blue, 1);
              }
              
              colors = colorsBuilder.create();
            }

            _mesh = new DirectMesh(GLPrimitive::points(),
                                   true,
                                   verticesBuilder.getCenter(),
                                   verticesBuilder.create(),
                                   1, // lineWidth
                                   _pointSize,
                                   NULL, // flatColor,
                                   colors,
                                   1,
                                   true);
          }

          delete averagePoint;
        }
      }

      delete jsonBaseObject;
    }

    delete _buffer;
    _buffer = NULL;
  }

  ~MeshRenderer_PointCloudParserAsyncTask() {
    if (_deleteListener) {
      delete _listener;
    }
    delete _buffer;
  }

  void onPostExecute(const G3MContext* context) {
    if (_mesh == NULL) {
      ILogger::instance()->logError("Error parsing PointCloud from \"%s\"", _url.getPath().c_str());
    }
    else {
      if (_listener != NULL) {
        _listener->onBeforeAddMesh(_mesh);
      }

      ILogger::instance()->logInfo("Adding PointCloud Mesh to _meshRenderer");
      _meshRenderer->addMesh(_mesh);

      if (_listener != NULL) {
        _listener->onAfterAddMesh(_mesh);
      }

      _mesh = NULL;
    }
  }

};

class MeshRenderer_PointCloudBufferDownloadListener : public IBufferDownloadListener {
private:
  MeshRenderer*       _meshRenderer;
  const float         _pointSize;
  const double        _deltaHeight;
  MeshLoadListener*   _listener;
  bool                _deleteListener;
  const IThreadUtils* _threadUtils;
  bool                _isBSON;

#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

public:

  MeshRenderer_PointCloudBufferDownloadListener(MeshRenderer*       meshRenderer,
                                                float               pointSize,
                                                double              deltaHeight,
                                                MeshLoadListener*   listener,
                                                bool                deleteListener,
                                                const IThreadUtils* threadUtils,
                                                bool                isBSON,
                                                const G3MContext*   context) :
  _meshRenderer(meshRenderer),
  _pointSize(pointSize),
  _deltaHeight(deltaHeight),
  _listener(listener),
  _deleteListener(deleteListener),
  _threadUtils(threadUtils),
  _isBSON(isBSON),
  _context(context)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    ILogger::instance()->logInfo("Downloaded PointCloud buffer from \"%s\" (%db)",
                                 url.getPath().c_str(),
                                 buffer->size());

    _threadUtils->invokeAsyncTask(new MeshRenderer_PointCloudParserAsyncTask(_meshRenderer,
                                                                             url,
                                                                             buffer,
                                                                             _pointSize,
                                                                             _deltaHeight,
                                                                             _listener,
                                                                             _deleteListener,
                                                                             _isBSON,
                                                                             _context),
                                  true);
    
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("Canceled download of \"%s\"", url.getPath().c_str());

    if (_deleteListener) {
      delete _listener;
    }
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }

};

void MeshRenderer::requestBuffer(const URL&          url,
                                 long long           priority,
                                 const TimeInterval& timeToCache,
                                 bool                readExpired,
                                 float               pointSize,
                                 double              deltaHeight,
                                 MeshLoadListener*   listener,
                                 bool                deleteListener,
                                 bool                isBSON) {
  IDownloader* downloader = _context->getDownloader();
  downloader->requestBuffer(url,
                            priority,
                            timeToCache,
                            readExpired,
                            new MeshRenderer_PointCloudBufferDownloadListener(this,
                                                                              pointSize,
                                                                              deltaHeight,
                                                                              listener,
                                                                              deleteListener,
                                                                              _context->getThreadUtils(),
                                                                              isBSON,
                                                                              _context),
                            true);
  
  
}
