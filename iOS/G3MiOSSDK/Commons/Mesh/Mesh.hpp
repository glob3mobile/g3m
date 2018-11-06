//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

#ifndef G3MiOSSDK_Mesh
#define G3MiOSSDK_Mesh

#include <stddef.h>
#include <string>

class Vector3D;
class BoundingVolume;
class G3MRenderContext;
class GLState;


class Mesh {
public:
  class MeshUserData {
  public:
    virtual ~MeshUserData() {
    }
  };

private:
  bool _enable;

  MeshUserData* _userData;

  std::string _token = "";

protected:
  Mesh();

public:
  void setToken(const std::string& token) {
    _token = token;
  }

  const std::string getToken() const {
    return _token;
  }

  void setEnable(bool enable);

  bool isEnable() const;

  virtual ~Mesh();

  MeshUserData* getUserData() const;

  void setUserData(MeshUserData* userData);

  virtual size_t getVertexCount() const = 0;

  virtual const Vector3D getVertex(const size_t index) const = 0;

  virtual BoundingVolume* getBoundingVolume() const = 0;

  virtual bool isTransparent(const G3MRenderContext* rc) const = 0;

  virtual void rawRender(const G3MRenderContext* rc,
                         const GLState* parentGLState) const = 0;

  void render(const G3MRenderContext* rc,
              const GLState* parentGLState) const;
  
};

#endif
