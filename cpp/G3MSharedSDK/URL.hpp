//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//

#ifndef G3MiOSSDK_URL
#define G3MiOSSDK_URL

#include <string>
#include "IStringUtils.hpp"

class URL {
private:
  URL& operator=(const URL& that);

  static const std::string concatenatePath(const URL& parent,
                                           const std::string& path) {
    const IStringUtils* iu = IStringUtils::instance();

    std::string result = parent._path + "/" + path;
    while (iu->indexOf(result, "//") >= 0) {
      result = iu->replaceAll(result, "//", "/");
    }

    if (iu->beginsWith(result, "http:/")) {
#ifdef C_CODE
      result = "http://" + iu->substring(result, 6);
#endif
#ifdef JAVA_CODE
      result = "http://" + iu.substring(result, 6);
#endif
    }

    if (iu->beginsWith(result, "https:/")) {
#ifdef C_CODE
      result = "https://" + iu->substring(result, 7);
#endif
#ifdef JAVA_CODE
      result = "https://" + iu.substring(result, 7);
#endif
    }

    if (iu->beginsWith(result, "ws:/")) {
#ifdef C_CODE
      result = "ws://" + iu->substring(result, 4);
#endif
#ifdef JAVA_CODE
      result = "ws://" + iu.substring(result, 4);
#endif
    }

    if (iu->beginsWith(result, "wss:/")) {
#ifdef C_CODE
      result = "wss://" + iu->substring(result, 5);
#endif
#ifdef JAVA_CODE
      result = "wss://" + iu.substring(result, 5);
#endif
    }

    if (iu->beginsWith(result, "file:/")) {
#ifdef C_CODE
      result = "file:///" + iu->substring(result, 6);
#endif
#ifdef JAVA_CODE
      result = "file:///" + iu.substring(result, 6);
#endif
    }

    return result;
  }

public:
  const std::string _path;

  URL(const URL& that) :
  _path(that._path)
  {
  }

  URL():_path("") {
  }

  /**
   Creates an URL.

   @param escapePath Escape the given path (true) or take it as it is given (false)
   */
  explicit URL(const std::string& path,
               const bool escapePath=false) :
  _path(  escapePath ? escape(path) : path  )
  {
  }

  URL(const URL& parent,
      const std::string& path,
      const bool escapePath=false) :
  _path( concatenatePath(parent, escapePath ? escape(path) : path) )
  {
  }

  ~URL() {
  }

//  const std::string getPath() const {
//    return _path;
//  }

  static URL nullURL() {
    return URL("__NULL__", false);
  }

  bool isNull() const {
    return (_path == "__NULL__");
  }

  bool isEquals(const URL& that) const {
    return (_path == that._path);
  }

  static const std::string FILE_PROTOCOL;

  bool isFileProtocol() const;

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

  static const std::string escape(const std::string& path);

#ifdef C_CODE
  bool operator<(const URL& that) const {
    if (_path < that._path) {
      return true;
    }
    return false;
  }
#endif

#ifdef JAVA_CODE
  @Override
  public int hashCode() {
    return _path.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final URL other = (URL) obj;
    if (_path.equals(other._path)) {
      return true;
    }
    return false;
  }
#endif
  
};


#endif
