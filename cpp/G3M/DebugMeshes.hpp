//
//  DebugMeshes.hpp
//  G3M
//

#ifndef DebugMeshes_hpp
#define DebugMeshes_hpp

class Mesh;
class Color;
class Box;
class Sphere;
class Ray;
class CoordinateSystem;


class DebugMeshes {
private:
  DebugMeshes() {
  }

public:
  static Mesh* createBoxWireframe(const Box& box,
                                  const Color& color);

  static Mesh* createSphereWireframe(const Sphere& sphere,
                                     const Color& color);

  static Mesh* createRayMesh(const Ray& ray,
                             const Color& color,
                             float lineWidth);

  static Mesh* createCoordinateSystemMesh(const CoordinateSystem& coordinateSystem,
                                          double size,
                                          const Color& xColor,
                                          const Color& yColor,
                                          const Color& zColor);

};

#endif
