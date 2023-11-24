//
//  TexturesHandler.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 19/06/12.
//

#ifndef G3M_TexturesHandler
#define G3M_TexturesHandler

#include <string>
#include <vector>

class IImage;
class G3MRenderContext;
class TextureHolder;
class GL;
class IFactory;
class IGLTextureID;
class TextureIDReference;

class TextureSpec {
private:

  TextureSpec& operator=(const TextureSpec& that);

public:
  const std::string _id;
  const int         _width;
  const int         _height;
  const bool        _generateMipmap;
  const int         _wrapS;
  const int         _wrapT;

  TextureSpec(const std::string& id,
              const int          width,
              const int          height,
              const bool         generateMipmap,
              const int          wrapS,
              const int          wrapT):
  _id(id),
  _width(width),
  _height(height),
  _generateMipmap(generateMipmap),
  _wrapS(wrapS),
  _wrapT(wrapT)
  {

  }

  //  TextureSpec():_id(""), _width(0),_height(0), _generateMipmap(false) {}

  TextureSpec(const TextureSpec& that) :
  _id(that._id),
  _width(that._width),
  _height(that._height),
  _generateMipmap(that._generateMipmap),
  _wrapS(that._wrapS),
  _wrapT(that._wrapT)
  {

  }

  bool isEquals(const TextureSpec& that) const {
    return (
            (_id             == that._id            ) &&
            (_width          == that._width         ) &&
            (_height         == that._height        ) &&
            (_generateMipmap == that._generateMipmap) &&
            (_wrapS          == that._wrapS         ) &&
            (_wrapT          == that._wrapT         )
            );
  }

  //  bool lowerThan(const TextureSpec& that) const {
  //    if (_id < that._id) {
  //      return true;
  //    }
  //    else if (_id > that._id) {
  //      return false;
  //    }
  //
  //    if (_width < that._width) {
  //      return true;
  //    }
  //    else if (_width > that._width) {
  //      return false;
  //    }
  //
  //    return (_height < that._height);
  //  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  //#ifdef C_CODE
  //  bool operator<(const TextureSpec& that) const {
  //    return lowerThan(that);
  //  }
  //#endif

#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_id == null) ? 0 : _id.hashCode());
    result = prime * result + _width;
    result = prime * result + _height;
    result = prime * result + (_generateMipmap ? 1 : 3);
    result = prime * result + _wrapS;
    result = prime * result + _wrapT;
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
    return isEquals(other);
  }
#endif
};


class TexturesHandler {
private:
  std::vector<TextureHolder*> _textureHolders;

  GL* const _gl;
  const bool _verbose;

  const IGLTextureID* getGLTextureIDIfAvailable(const TextureSpec& textureSpec);

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
                                                  bool generateMipmap,
                                                  int wrapS,
                                                  int wrapT);


  //This two methods are supposed to be accessed only by TextureIDReference class
  void releaseGLTextureID(const IGLTextureID* glTextureID);
  void retainGLTextureID(const IGLTextureID* glTextureID);
};

#endif
