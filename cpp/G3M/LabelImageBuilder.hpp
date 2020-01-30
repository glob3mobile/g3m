//
//  LabelImageBuilder.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3M__LabelImageBuilder__
#define __G3M__LabelImageBuilder__

#include "AbstractImageBuilder.hpp"

#include <string>

#include "GFont.hpp"
#include "Color.hpp"
#include "Vector2F.hpp"

class ImageBackground;


class LabelImageBuilder : public AbstractImageBuilder {
private:
  std::string _text;
  const GFont _font;
  const Color _color;
  
  const Color _shadowColor;
  const float _shadowBlur;
  const Vector2F _shadowOffset;

  const ImageBackground* _background;
  
  const bool  _isMutable;
  
  const std::string getImageName() const;

protected:
  ~LabelImageBuilder();

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

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);
  
};

#endif
