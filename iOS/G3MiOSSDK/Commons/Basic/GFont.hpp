//
//  GFont.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/10/13.
//
//

#ifndef __G3MiOSSDK__GFont__
#define __G3MiOSSDK__GFont__

#include <string>


class GFont {
private:
  static const std::string SERIF;
  static const std::string SANS_SERIF;
  static const std::string MONOSPACED;

  const std::string _name;
  const float       _size;
  const bool        _bold;
  const bool        _italic;

  GFont(const std::string& name,
        const float        size,
        const bool         bold,
        const bool         italic) :
  _name(name),
  _size(size),
  _bold(bold),
  _italic(italic)
  {

  }
  

public:

  static const GFont serif(float size   = 20,
                           bool  bold   = false,
                           bool  italic = false) {
    return GFont(GFont::SERIF, size, bold, italic);
  }

  static const GFont sansSerif(float size   = 20,
                               bool  bold   = false,
                               bool  italic = false) {
    return GFont(GFont::SANS_SERIF, size, bold, italic);
  }

  static const GFont monospaced(float size   = 20,
                                bool  bold   = false,
                                bool  italic = false) {
    return GFont(GFont::MONOSPACED, size, bold, italic);
  }

  GFont(const GFont& that) :
  _name(that._name),
  _size(that._size),
  _bold(that._bold),
  _italic(that._italic)
  {

  }

  ~GFont() {
  }

  bool isSerif() const;
  bool isSansSerif() const;
  bool isMonospaced() const;

  float getSize() const {
    return _size;
  }

  bool isBold() const {
    return _bold;
  }

  bool isItalic() const {
    return _italic;
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
