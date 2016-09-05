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
class IFloatBuffer;


class Trail {
private:


  class Position {
  public:
    const Angle  _latitude;
    const Angle  _longitude;
    const double _height;
    const double _alpha;

    Position(const Angle& latitude,
             const Angle& longitude,
             const double height,
             const double alpha) :
    _latitude(latitude),
    _longitude(longitude),
    _height(height),
    _alpha(alpha)
    {

    }

    ~Position() {

    }
  };


  class Segment {
  private:
    const Color _color;
    const float _ribbonWidth;

    bool _positionsDirty;
    std::vector<Position*> _positions;
    Position* _nextSegmentFirstPosition;
    Position* _previousSegmentLastPosition;

    Mesh* createMesh(const Planet* planet);

    Mesh* _mesh;
    Mesh* getMesh(const Planet* planet);

    const IFloatBuffer* getBearingsInRadians() const;

  public:
    Segment(const Color& color,
            float ribbonWidth);

    ~Segment();

    size_t getSize() const;

    void addPosition(const Position& position);

    void addPosition(const Angle& latitude,
                     const Angle& longitude,
                     const double height,
                     const double alpha);

    void addPosition(const Geodetic3D& position,
                     const double alpha);

    void setNextSegmentFirstPosition(const Angle& latitude,
                                     const Angle& longitude,
                                     const double height,
                                     const double alpha);

    void setPreviousSegmentLastPosition(const Position& position);

    Position getLastPosition() const;

    Position getPreLastPosition() const;

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
                   const double height,
                   const double alpha);

  void addPosition(const Geodetic3D& position,
                   const double alpha) {
    addPosition(position._latitude,
                position._longitude,
                position._height,
                alpha);
  }
  
  void clear();
  
};

#endif
