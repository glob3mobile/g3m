//
//  TexturesHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 19/06/12.
//

#ifndef G3MiOSSDK_TexturesHandler
#define G3MiOSSDK_TexturesHandler

#include <string>
#include <vector>

#include "INativeGL.hpp"
#include "GLConstants.hpp"

class IImage;
class G3MRenderContext;
class TextureHolder;
class GL;
class IFactory;
class IGLTextureId;
class TextureIDReference;

class TextureSpec {
private:
  const std::string _id;

  const int         _width;
  const int         _height;
  const bool        _generateMipmap;
  const int         _wrapMode;

  TextureSpec& operator=(const TextureSpec& that);

public:
  TextureSpec(const std::string& id,
              const int          width,
              const int          height,
              const bool         generateMipmap):
  _id(id),
  _width(width),
  _height(height),
  _generateMipmap(generateMipmap),
  _wrapMode(GLTextureParameterValue::clampToEdge())
  {

  }

  TextureSpec(const std::string& id,
              const int          width,
              const int          height,
              const bool         generateMipmap,
			  const int          wrapMode):
  _id(id),
  _width(width),
  _height(height),
  _generateMipmap(generateMipmap),
  _wrapMode(wrapMode)
  {

  }

  TextureSpec():_id(""), _width(0),_height(0), _generateMipmap(false), _wrapMode(GLTextureParameterValue::clampToEdge()) {}

  TextureSpec(const TextureSpec& that):
  _id(that._id),
  _width(that._width),
  _height(that._height),
  _generateMipmap(that._generateMipmap),
  _wrapMode(that._wrapMode)
  {

  }

  bool generateMipmap() const {
    return _generateMipmap;
  }

  int getWidth() const {
    return _width;
  }

  int getHeight() const {
    return _height;
  }

  int getWrapMode() const {
    return _wrapMode;
  }

  bool equalsTo(const TextureSpec& that) const {
    return ((_id.compare(that._id) == 0) &&
            (_width  == that._width) &&
            (_height == that._height) &&
			(_wrapMode == that._wrapMode));
  }

  bool lowerThan(const TextureSpec& that) const {
    if (_id < that._id) {
      return true;
    }
    else if (_id > that._id) {
      return false;
    }

    if (_width < that._width) {
      return true;
    }
    else if (_width > that._width) {
      return false;
    }

    return (_height < that._height);
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

#ifdef C_CODE
  bool operator<(const TextureSpec& that) const {
    return lowerThan(that);
  }
#endif

#ifdef JAVA_CODE
  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _height;
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		result = prime * result + _width;
		result = prime * result + _wrapMode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextureSpec other = (TextureSpec) obj;
		if (_height != other._height)
			return false;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		if (_width != other._width)
			return false;
		if (_wrapMode != other._wrapMode)
			return false;
		return true;
	}
#endif
};


class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;

  GL* const _gl;
  const bool _verbose;
  //void showHolders(const std::string& message) const;

  const IGLTextureId* getGLTextureIdIfAvailable(const TextureSpec& textureSpec);

public:

  TexturesHandler(GL* const  gl,
                  bool verbose):
  _gl(gl),
  _verbose(verbose)
  {
  }

  ~TexturesHandler();
    
  const TextureIDReference* getTextureIDReference(const IImage* image,
                                                    int format,
                                                    const std::string& name,
                                                    bool generateMipmap);

  const TextureIDReference* getTextureIDReference(const IImage* image,
                                                  int format,
                                                  const std::string& name,
                                                  bool generateMipmap,
                                                  int wrapMode);


  //This two methods are supposed to be accessed only by TextureIDReference class
  void releaseGLTextureId(const IGLTextureId* glTextureId);
  void retainGLTextureId(const IGLTextureId* glTextureId);
};

#endif
