//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mark
#define G3MiOSSDK_Mark

#include <string>
#include "Geodetic3D.hpp"
#include "Context.hpp"

#include "Vector3D.hpp"
#include "URL.hpp"
#include "Vector2I.hpp"
#include "Color.hpp"
#include "GLState.hpp"
#include "SurfaceElevationProvider.hpp"
#include "MutableVector3D.hpp"
#include "GTask.hpp"
#include "PeriodicalTask.hpp"
#include "IImageBuilderListener.hpp"

class IImage;
class IFloatBuffer;
class IGLTextureId;
class MarkTouchListener;
class GLGlobalState;
class GPUProgramState;
class TextureIDReference;
class EffectTarget;
class IImageBuilder;

class MarkUserData {
public:
  virtual ~MarkUserData() {
  }
};


class Mark : public SurfaceElevationListener {
private:

  IImageBuilder* _imageBuilder;

  /**
   * The text the mark displays.
   * Useless if the mark does not have label.
   */
  const std::string _label;
  /**
   * Flag to know if the label will be located under the icon (if TRUE) or on its right (if FALSE).
   * Useless if the mark does not have label or icon.
   * Default value: TRUE
   */
  const bool        _labelBottom;
  /**
   * The font size of the text.
   * Useless if the mark does not have label.
   * Default value: 20
   */
  const float       _labelFontSize;


  /**
   * The color of the text.
   * Useless if the mark does not have label.
   * Default value: white
   */
#ifdef C_CODE
  const Color*      _labelFontColor;
#endif
#ifdef JAVA_CODE
  private Color     _labelFontColor;
#endif

  /**
   * The color of the text shadow.
   * Useless if the mark does not have label.
   * Default value: black
   */
#ifdef C_CODE
  const Color*      _labelShadowColor;
#endif
#ifdef JAVA_CODE
  private Color     _labelShadowColor;
#endif

  /**
   * The number of pixels between the icon and the text.
   * Useless if the mark does not have label or icon.
   * Default value: 2
   */
  const int         _labelGapSize;
  /**
   * The URL to get the image file.
   * Useless if the mark does not have icon.
   */
  URL               _iconURL;
  /**
   * The point where the mark will be geo-located.
   */
  Geodetic3D*  _position;
  /**
   * The minimun distance (in meters) to show the mark. If the camera is further than this, the mark will not be displayed.
   * Default value: 4.5e+06
   */
  double      _minDistanceToCamera;
  /**
   * The extra data to be stored by the mark.
   * Usefull to store data such us name, URL...
   */
  MarkUserData*     _userData;
  /**
   * Flag to know if the mark is the owner of _userData and thus it must delete it on destruction.
   * Default value: TRUE
   */
  const bool        _autoDeleteUserData;
  /**
   * Interface for listening to the touch event.
   */
  MarkTouchListener* _listener;
  /**
   * Flag to know if the mark is the owner of _listener and thus it must delete it on destruction.
   * Default value: FALSE
   */
  const bool        _autoDeleteListener;

  std::string _token = "";

#ifdef C_CODE
  const TextureIDReference* _textureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureId;
#endif

  Vector3D* _cartesianPosition;

  bool           _textureSolved;
#ifdef C_CODE
  const IImage*  _textureImage;
#endif
#ifdef JAVA_CODE
  private IImage _textureImage;
#endif
  float          _textureWidth;
  float          _textureHeight;
  float          _textureWidthProportion;
  float          _textureHeightProportion;
  bool           _textureSizeSetExternally;
  bool           _textureProportionSetExternally;
  std::string    _imageID;
  
  bool  _hasTCTransformations;
  float _translationTCX, _translationTCY;
  float _scalingTCX, _scalingTCY;

  bool    _renderedMark;


  GLState* _glState;
  void createGLState(const Planet* planet,
                     IFloatBuffer* billboardTexCoords);

  SurfaceElevationProvider* _surfaceElevationProvider;
  double _currentSurfaceElevation;
  AltitudeMode _altitudeMode;

  Vector3D* _normalAtMarkPosition;
  
  TextureGLFeature* _textureGLF;
  
  void clearGLState();

  MutableVector3D _markCameraVector;
  
  float _anchorU;
  float _anchorV;
  BillboardGLFeature* _billboardGLF;

  bool _initialized;

  bool _zoomInAppears;
  EffectsScheduler* _effectsScheduler;
  bool _firstRender;


  EffectTarget* _effectTarget;
  EffectTarget* getEffectTarget();

public:


  class ImageBuilderListener : public IImageBuilderListener {
  private:
    Mark* _mark;

  public:
    ImageBuilderListener(Mark* mark) :
    _mark(mark)
    {

    }

    ~ImageBuilderListener() {

    }

    void imageCreated(const IImage*      image,
                      const std::string& imageName);

    void onError(const std::string& error);
    
  };
  
  
  /**
   * Creates a mark with icon and label
   */
  Mark(const std::string& label,
       const URL&         iconURL,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       const bool         labelBottom=true,
       const float        labelFontSize=20,
       const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
       const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
       const int          labelGapSize=2,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark just with label, without icon
   */
  Mark(const std::string& label,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       const float        labelFontSize=20,
       const Color*       labelFontColor=Color::newFromRGBA(1, 1, 1, 1),
       const Color*       labelShadowColor=Color::newFromRGBA(0, 0, 0, 1),
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark just with icon, without label
   */
  Mark(const URL&         iconURL,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark whith a given pre-renderer IImage
   */
  Mark(const IImage*      image,
       const std::string& imageID,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  /**
   * Creates a mark whith a IImageBuilder, in future versions it'll be the only constructor
   */
  Mark(IImageBuilder*     imageBuilder,
       const Geodetic3D&  position,
       AltitudeMode       altitudeMode,
       double             minDistanceToCamera=4.5e+06,
       MarkUserData*      userData=NULL,
       bool               autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool               autoDeleteListener=false);

  ~Mark();

  bool isInitialized() const {
    return _initialized;
  }

  const std::string getLabel() const {
    return _label;
  }

  const Geodetic3D getPosition() const {
    return *_position;
  }

  void initialize(const G3MContext* context,
                  long long downloadPriority);

  void render(const G3MRenderContext* rc,
              const MutableVector3D& cameraPosition);

  bool isReady() const;

  bool isRendered() const {
    return _renderedMark;
  }

  void onTextureDownloadError();

  void onTextureDownload(const IImage* image);

  void onImageCreated(const IImage* image,
                      const std::string& imageName);

  void onImageCreationError(const std::string& error);

  float getTextureWidth() const {
    return _textureWidth;
  }

  float getTextureHeight() const {
    return _textureHeight;
  }

  Vector2F getTextureExtent() const {
    return Vector2F(_textureWidth, _textureHeight);
  }

  const MarkUserData* getUserData() const {
    return _userData;
  }

  void setUserData(MarkUserData* userData) {
    if (_autoDeleteUserData) {
      delete _userData;
    }
    _userData = userData;
  }

  bool touched();

  void setMinDistanceToCamera(double minDistanceToCamera);
  double getMinDistanceToCamera();

  Vector3D* getCartesianPosition(const Planet* planet);

  void render(const G3MRenderContext* rc,
              const MutableVector3D& cameraPosition,
              double cameraHeight,
              const GLState* parentGLState,
              const Planet* planet,
              GL* gl,
              IFloatBuffer* billboardTexCoords);

  void elevationChanged(const Geodetic2D& position,
                        double rawElevation,            //Without considering vertical exaggeration
                        double verticalExaggeration);

  void elevationChanged(const Sector& position,
                        const ElevationData* rawElevationData, //Without considering vertical exaggeration
                        double verticalExaggeration) {}

  void setPosition(const Geodetic3D& position);
  
  void setOnScreenSizeOnPixels(int width, int height);
  void setOnScreenSizeOnProportionToImage(float width, float height);
  
  void setTextureCoordinatesTransformation(const Vector2F& translation,
                                           const Vector2F& scaling);
  
  void setMarkAnchor(float anchorU, float anchorV);

  void setToken(const std::string& token) {
    _token = token;
  }

  const std::string getToken() const {
    return _token;
  }

  void setZoomInAppears(bool zoomInAppears) {
    _zoomInAppears = zoomInAppears;
  }

  bool getZoomInAppears() const {
    return _zoomInAppears;
  }

};

class TextureAtlasMarkAnimationTask: public PeriodicalTask{
  
  class TextureAtlasMarkAnimationGTask: public GTask{
    Mark* _mark;
    int _cols;
    int _rows;
    int _nFrames;
    
    int _currentFrame;
    
    float _scaleX;
    float _scaleY;
  public:
    
    ~TextureAtlasMarkAnimationGTask(){}
    
    TextureAtlasMarkAnimationGTask(Mark* mark, int nColumn, int nRows, int nFrames):
    _mark(mark), _currentFrame(0), _cols(nColumn), _rows(nRows), _nFrames(nFrames)
    {
      //    _mark->setOnScreenSize(Vector2F(100,100));
      
      _scaleX = 1.0f / _cols;
      _scaleY = 1.0f / _rows;
    }
    
    
    virtual void run(const G3MContext* context){
      int row = _currentFrame / _cols;
      int col = _currentFrame % _cols;
      
      float transX = col * (1.0f / _cols);
      float transY = row * (1.0f / _rows);
      //        printf("FRAME:%d, R:%d, C:%d -> %f %f\n", _currentFrame, row, col, transX, transY);
      
      _mark->setTextureCoordinatesTransformation(Vector2F(transX,transY), Vector2F(_scaleX, _scaleY));
      _currentFrame = (_currentFrame+1) % _nFrames;
    }
    
  };
  
  
public:
  TextureAtlasMarkAnimationTask(Mark* mark, int nColumn, int nRows, int nFrames, const TimeInterval& frameTime):
  PeriodicalTask(frameTime, new TextureAtlasMarkAnimationGTask(mark, nColumn, nRows, nFrames))
  {
  }
};




#endif
