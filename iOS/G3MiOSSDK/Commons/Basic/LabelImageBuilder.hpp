//
//  LabelImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3MiOSSDK__LabelImageBuilder__
#define __G3MiOSSDK__LabelImageBuilder__

#include "AbstractImageBuilder.hpp"

#include <string>

#include "GFont.hpp"
#include "Color.hpp"
#include "Vector2F.hpp"

class ImageBackground;


class LabelImageBuilder : public AbstractImageBuilder {
private:
  std::string _text;
#ifdef C_CODE
  const GFont _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const Color _color;
  
  const Color _shadowColor;
  const float _shadowBlur;
#ifdef C_CODE
  const Vector2F _shadowOffset;
#endif
#ifdef JAVA_CODE
  private final Vector2F _shadowOffset;
#endif
  
  const ImageBackground* _background;
  
  const bool  _isMutable;
  
  const std::string getImageName() const;
  
public:
  
  LabelImageBuilder(const std::string&     text,
                    const GFont&           font         = GFont::sansSerif(),
                    const Color&           color        = Color::white(),
                    const Color&           shadowColor  = Color::transparent(),
                    const float            shadowBlur   = 0,
                    const Vector2F&        shadowOffset = Vector2F::zero(),
                    const ImageBackground* background   = NULL,
                    const bool             isMutable    = false);
  
  bool isMutable() const {
    return _isMutable;
  }
  
  void setText(const std::string& text);
  
  ~LabelImageBuilder();
  
  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
