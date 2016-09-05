//
//  Trail.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

#ifndef Trail_hpp
#define Trail_hpp

#include <vector>
#include "Color.hpp"

#include "Geodetic3D.hpp"

class Mesh;
class Planet;
class Frustum;
class G3MRenderContext;
class GLState;



class Trail {
private:

  class Segment {
  private:
    const Color _color;
    const float _ribbonWidth;

    bool _positionsDirty;
    std::vector<Geodetic3D*> _positions;
    Geodetic3D* _nextSegmentFirstPosition;
    Geodetic3D* _previousSegmentLastPosition;

    Mesh* createMesh(const Planet* planet);

    Mesh* _mesh;
    Mesh* getMesh(const Planet* planet);


  public:
    Segment(Color color,
            float ribbonWidth) :
    _color(color),
    _ribbonWidth(ribbonWidth),
    _positionsDirty(true),
    _mesh(NULL),
    _nextSegmentFirstPosition(NULL),
    _previousSegmentLastPosition(NULL)
    {
    }

    ~Segment();

    size_t getSize() const {
      return _positions.size();
    }

    void addPosition(const Angle& latitude,
                     const Angle& longitude,
                     const double height);

    void addPosition(const Geodetic3D& position);

    void setNextSegmentFirstPosition(const Angle& latitude,
                                     const Angle& longitude,
                                     const double height);

    void setPreviousSegmentLastPosition(const Geodetic3D& position);

    Geodetic3D getLastPosition() const;

    Geodetic3D getPreLastPosition() const;

    void render(const G3MRenderContext* rc,
                const Frustum* frustum,
                const GLState* state);

  };



  bool _visible;

  const Color  _color;
  const float  _ribbonWidth;
  const double _deltaHeight;
  const int    _maxPositionsPerSegment;

  std::vector<Segment*> _segments;

public:
  Trail(const Color& color,
        float ribbonWidth,
        double deltaHeight = 0.0,
        int maxPositionsPerSegment = 64);

  ~Trail();

  void render(const G3MRenderContext* rc,
              const Frustum* frustum,
              const GLState* state);

  void setVisible(bool visible) {
    _visible = visible;
  }

  bool isVisible() const {
    return _visible;
  }

  void addPosition(const Angle& latitude,
                   const Angle& longitude,
                   const double height);

  void addPosition(const Geodetic3D& position) {
    addPosition(position._latitude,
                position._longitude,
                position._height);
  }
  
  void clear();
  
};

#endif
