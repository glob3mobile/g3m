//
//  Mark.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 06/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mark_hpp
#define G3MiOSSDK_Mark_hpp

#include <string>
#include "Geodetic3D.hpp"
#include "Context.hpp"

#include "Vector3D.hpp"
#include "URL.hpp"
#include "Vector2I.hpp"

class IImage;
class IFloatBuffer;
class IGLTextureId;
class MarkTouchListener;
class GLState;

class MarkUserData {
public:
  virtual ~MarkUserData() {

  }
};

class Mark {
private:
  const std::string _label;
  const bool        _labelBottom;

  URL               _iconURL;
  const Geodetic3D  _position;
  const double      _minDistanceToCamera;

  MarkUserData* _userData;
  const bool    _autoDeleteUserData;

  MarkTouchListener* _listener;
  const bool         _autoDeleteListener;

#ifdef C_CODE
  const IGLTextureId* _textureId;
#endif
#ifdef JAVA_CODE
  private IGLTextureId _textureId;
#endif

  Vector3D* _cartesianPosition;

  IFloatBuffer* _vertices;
  IFloatBuffer* getVertices(const Planet* planet);

  bool    _textureSolved;
  IImage* _textureImage;
  int     _textureWidth;
  int     _textureHeight;

  bool    _renderedMark;

public:
  Mark(const std::string&  label,
       const URL           iconURL,
       const Geodetic3D    position,
       const bool          labelBottom=true,
       double minDistanceToCamera=4.5e+06,
       MarkUserData* userData=NULL,
       bool autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool autoDeleteListener=false);

  Mark(const std::string& label,
       const Geodetic3D   position,
       double minDistanceToCamera=4.5e+06,
       MarkUserData* userData=NULL,
       bool autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool autoDeleteListener=false);

  Mark(const URL          iconURL,
       const Geodetic3D   position,
       double minDistanceToCamera=4.5e+06,
       MarkUserData* userData=NULL,
       bool autoDeleteUserData=true,
       MarkTouchListener* listener=NULL,
       bool autoDeleteListener=false);

  ~Mark();

  const std::string getLabel() const {
    return _label;
  }

  const Geodetic3D getPosition() const {
    return _position;
  }

  void initialize(const G3MContext* context,
                  long long downloadPriority);

  void render(const G3MRenderContext* rc);

  bool isReady() const;

  bool isRendered() const {
    return _renderedMark;
  }

  void onTextureDownloadError();

  void onTextureDownload(IImage* image);

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

  Vector3D* getCartesianPosition(const Planet* planet);
  
};

#endif
