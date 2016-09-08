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
#include "Mesh.hpp"

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


  static const int SEGMENT_ALPHA_STATUS_UNKNOWN;
  static const int SEGMENT_ALPHA_STATUS_FULL_HIDDEN;
  static const int SEGMENT_ALPHA_STATUS_HALF;
  static const int SEGMENT_ALPHA_STATUS_FULL_VISIBLE;


  class SegmentMeshUserData : public Mesh::MeshUserData {
  private:
    const int    _status;
    const double _visibleAlpha;

  public:
    SegmentMeshUserData(const int    status,
                        const double visibleAlpha) :
    _status(status),
    _visibleAlpha(visibleAlpha)
    {
    }

    ~SegmentMeshUserData() {
#ifdef JAVA_CODE
      super.dispose();
#endif
    }

    bool isValid(const int    status,
                 const double visibleAlpha) const;
  };


  class Segment {
  private:
    const Color _color;
    const float _ribbonWidth;
    double _minAlpha;
    double _maxAlpha;
    double _visibleAlpha;
    int _alphaStatus;

    bool _positionsDirty;
    std::vector<Position*> _positions;
    Position* _nextSegmentFirstPosition;
    Position* _previousSegmentLastPosition;

    Mesh* createMesh(const Planet* planet);

    Mesh* _mesh;
    Mesh* getMesh(const Planet* planet);

    const IFloatBuffer* getBearingsInRadians() const;

    int calculateAlphaStatus();

    bool isMeshValid() const;

  public:
    Segment(const Color& color,
            float ribbonWidth,
            double visibleAlpha);

    ~Segment();

    size_t getSize() const;

    void addPosition(const Position& position);

    void addPosition(const Angle& latitude,
                     const Angle& longitude,
                     const double height,
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

    void setVisibleAlpha(double visibleAlpha);
  };



  bool _visible;

  const Color  _color;
  const float  _ribbonWidth;
  const double _deltaHeight;
  const int    _maxPositionsPerSegment;

  double _alpha;

  std::vector<Segment*> _segments;

public:
  Trail(const Color& color,
        float ribbonWidth,
        double deltaHeight = 0.0,
        int maxPositionsPerSegment = 32);

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
  
  void setAlpha(const double alpha);
  
};

#endif
