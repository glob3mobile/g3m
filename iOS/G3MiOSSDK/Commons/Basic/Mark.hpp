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

class IImage;
class IFloatBuffer;
class IGLTextureId;
class MarkTouchListener;
class GLGlobalState;
class GPUProgramState;
class TextureIDReference;

#include "SurfaceElevationProvider.hpp"


class MarkUserData {
public:
  virtual ~MarkUserData() {
  }
};


class Mark : public SurfaceElevationListener {
private:
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
  const Color*      _labelFontColor;
  /**
   * The color of the text shadow.
   * Useless if the mark does not have label.
   * Default value: black
   */
  const Color*      _labelShadowColor;
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

#ifdef C_CODE
  const TextureIDReference* _textureId;
#endif
#ifdef JAVA_CODE
  private TextureIDReference _textureId;
#endif

  Vector3D* _cartesianPosition;

  bool              _textureSolved;
#ifdef C_CODE
  const IImage*     _textureImage;
#endif
#ifdef JAVA_CODE
  private IImage _textureImage;
#endif
  int               _textureWidth;
  int               _textureHeight;
  const std::string _imageID;

  bool    _renderedMark;


  GLState* _glState;
  void createGLState(const Planet* planet,
                     IFloatBuffer* billboardTexCoords);

  SurfaceElevationProvider* _surfaceElevationProvider;
  double _currentSurfaceElevation;
  AltitudeMode _altitudeMode;

  Vector3D* _normalAtMarkPosition;

public:
  /**
   * Creates a marker with icon and label
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
   * Creates a marker just with label, without icon
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
   * Creates a marker just with icon, without label
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
   * Creates a marker whith a given pre-renderer IImage
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

  ~Mark();

  const std::string getLabel() const {
    return _label;
  }

  const Geodetic3D getPosition() const {
    return *_position;
  }

  void initialize(const G3MContext* context,
                  long long downloadPriority);

  void render(const G3MRenderContext* rc,
              const Vector3D& cameraPosition);

  bool isReady() const;

  bool isRendered() const {
    return _renderedMark;
  }

  void onTextureDownloadError();

  void onTextureDownload(const IImage* image);

  int getTextureWidth() const {
    return _textureWidth;
  }

  int getTextureHeight() const {
    return _textureHeight;
  }

  Vector2I getTextureExtent() const {
    return Vector2I(_textureWidth, _textureHeight);
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
              const Vector3D& cameraPosition,
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
};

#endif
