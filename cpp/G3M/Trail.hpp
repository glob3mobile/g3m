//
//  Trail.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/5/16.
//
//

#ifndef Trail_hpp
#define Trail_hpp

#include <vector>

#include "Color.hpp"
#include "Mesh.hpp"
#include "Angle.hpp"

class Planet;
class Frustum;
class G3MRenderContext;
class GLState;
class IFloatBuffer;
class MutableMatrix44D;
class Geodetic2D;
class Geodetic3D;
class FloatBufferBuilderFromCartesian3D;
class ViewportExtentGLFeature;


class Trail {
private:


  class Position {
  public:
    const Angle  _latitude;
    const Angle  _longitude;
    const double _height;
    const double _alpha;
    const Angle  _heading;

    Position(const Angle& latitude,
             const Angle& longitude,
             const double height,
             const double alpha,
             const Angle& heading) :
    _latitude(latitude),
    _longitude(longitude),
    _height(height),
    _alpha(alpha),
    _heading(heading)
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
    const float _minWidthPixels;
    const bool  _depthTest;
    const bool  _polygonOffsetFill;
    const float _polygonOffsetFactor;
    const float _polygonOffsetUnits;

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

    // ribbon mode only: the RibbonSideGLFeature attribute buffer lives here, not in the mesh
    IFloatBuffer* _ribbonSides;
    GLState*      _ribbonGLState;
    void setRibbonSides(IFloatBuffer* ribbonSides);
    const GLState* getMeshGLState(const GLState* parent);

    const IFloatBuffer* getBearingsInRadians() const;

    int calculateAlphaStatus();

    bool isMeshValid() const;

    const MutableMatrix44D createMatrix(const Angle& bearing,
                                        const Angle& latitude,
                                        const Angle& longitude,
                                        const double height,
                                        const Vector3D& rotationAxis,
                                        const Planet* planet) const;

    void addRibbonVertices(const MutableMatrix44D& matrix,
                           FloatBufferBuilderFromCartesian3D* vertices,
                           FloatBufferBuilderFromCartesian3D* sideVectors) const;

  public:
    Segment(const Color& color,
            const float  ribbonWidth,
            const float  minWidthPixels,
            const bool   depthTest,
            const bool   polygonOffsetFill,
            const float  polygonOffsetFactor,
            const float  polygonOffsetUnits,
            const double visibleAlpha);

    ~Segment();

    size_t getSize() const;

    void addPosition(const Position& position);

    void addPosition(const Angle& latitude,
                     const Angle& longitude,
                     const double height,
                     const double alpha,
                     const Angle& heading);

    void setNextSegmentFirstPosition(const Angle& latitude,
                                     const Angle& longitude,
                                     const double height,
                                     const double alpha,
                                     const Angle& heading);

    void setPreviousSegmentLastPosition(const Position& position);

    Position getLastPosition() const;

    Position getPenultimatePosition() const;

    void render(const G3MRenderContext* rc,
                const Frustum* frustum,
                const GLState* state);

    void setVisibleAlpha(double visibleAlpha);
  };



  bool _visible;

  const Color  _color;
  const float  _ribbonWidth;
  const float  _minWidthPixels;
  const bool   _depthTest;
  const bool   _polygonOffsetFill;
  const float  _polygonOffsetFactor;
  const float  _polygonOffsetUnits;
  const double _deltaHeight;
  const int    _maxPositionsPerSegment;

  double _alpha;

  std::vector<Segment*> _segments;

  // only when minWidthPixels > 0. The viewport feature must stay out of any shared state:
  // GPUProgramManager reads VIEWPORT_EXTENT alone as "billboard"
  GLState*                 _ribbonGLState;
  ViewportExtentGLFeature* _ribbonViewportExtent;
  const GLState* getSegmentsGLState(const G3MRenderContext* rc,
                                    const GLState* parent);

public:
  // minWidthPixels > 0 switches to the RibbonMesh program: the ribbon never gets thinner
  // than that on screen, no matter how far the camera is. 0 keeps the plain fixed-width mesh.
  Trail(const Color& color,
        const float  ribbonWidth,
        const bool   depthTest,
        const bool   polygonOffsetFill,
        const float  polygonOffsetFactor,
        const float  polygonOffsetUnits,
        const double deltaHeight            = 0.0,
        const int    maxPositionsPerSegment = 32,
        const float  minWidthPixels         = 0);

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
                   const double alpha,
                   const Angle& heading = Angle::nan());

  void addPosition(const Geodetic2D& position,
                   const double height,
                   const double alpha,
                   const Angle& heading = Angle::nan());

  void addPosition(const Geodetic3D& position,
                   const double alpha,
                   const Angle& heading = Angle::nan());

  void clear();
  
  void setAlpha(const double alpha);
  
};

#endif
