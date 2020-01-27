//
//  GPUAttribute.hpp
//  G3M
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

#ifndef G3M_GPUAttribute
#define G3M_GPUAttribute

#include "GPUVariable.hpp"

class GPUAttributeValue;
class GL;


class GPUAttribute : public GPUVariable {
private:
  bool _dirty;
#ifdef C_CODE
  const GPUAttributeValue* _value;
#endif
#ifdef JAVA_CODE
  private GPUAttributeValue _value;
#endif
  bool _enabled;

public:
  const int _id;
  const int _type;
  const int _size;
  const GPUAttributeKey _key;

  GPUAttribute(const std::string& name,
               int id,
               int type,
               int size);

  virtual ~GPUAttribute();

  bool wasSet() const;

  bool isEnabled() const;

  int getIndex() const;

  void unset(GL* gl);

  void put(const GPUAttributeValue* v);

  virtual void applyChanges(GL* gl);

};

#endif
