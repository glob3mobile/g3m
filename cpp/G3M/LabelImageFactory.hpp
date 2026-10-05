//
//  LabelImageFactory.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3M__LabelImageFactory__
#define __G3M__LabelImageFactory__

#include "AbstractImageFactory.hpp"

#include <string>

#include "LabelStyle.hpp"


class LabelImageFactory : public AbstractImageFactory {
private:
  std::string       _text;
  const LabelStyle* _style;
  const bool        _isMutable;

  const std::string getImageName() const;

protected:
  ~LabelImageFactory();

public:

  LabelImageFactory(const std::string& text,
                    const LabelStyle&  style);

  /** a mutable label accepts setText() and notifies its change listener */
  LabelImageFactory(const std::string& text,
                    const LabelStyle&  style,
                    const bool         isMutable);

  bool isMutable() const {
    return _isMutable;
  }
  
  void setText(const std::string& text);

  void create(const G3MContext* context,
             IImageFactoryListener* listener,
             bool deleteListener);
  
};

#endif
