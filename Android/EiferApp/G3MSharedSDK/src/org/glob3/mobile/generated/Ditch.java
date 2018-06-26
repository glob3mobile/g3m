package org.glob3.mobile.generated;

/**
 * Created by chano on 16/5/18.
 */

public class Ditch {

    private Geodetic3D _start;
    private Geodetic3D _end;
    private final double _width;

    public Ditch(Geodetic3D start, Geodetic3D end, final double width ){
        _start = new Geodetic3D(start);
        _end = new Geodetic3D(end);
        _width = width;
    }

    public Mesh createComplexDitchMesh(JSONArray covers, final Planet planet){
        FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();

        for (int i=0;i<covers.size()-1;i++) {
            JSONArray coverA = covers.getAsArray(i);
            JSONArray coverB = covers.getAsArray(i + 1);

            for (int j=0;j<coverA.size();j++){
                JSONArray pointA = coverA.getAsArray(j);
                JSONArray pointB = coverB.getAsArray(j);

                double lat = pointA.getAsNumber(1).value();
                double lon = pointA.getAsNumber(0).value();
                double hgt = pointA.getAsNumber(2).value(); // OJO: Elemento corrector debería ir fuera, donde pasamos de JSON de Android a JSON de globo
                Vector3D pA = planet.toCartesian(Angle.fromDegrees(lat),Angle.fromDegrees(lon),hgt);

                lat = pointB.getAsNumber(1).value();
                lon = pointB.getAsNumber(0).value();
                hgt = pointB.getAsNumber(2).value();
                Vector3D pB = planet.toCartesian(Angle.fromDegrees(lat),Angle.fromDegrees(lon),hgt);

                fbb.add(pA); fbb.add(pB);
            }
        }

        DirectMesh dm = new DirectMesh(GLPrimitive.triangleStrip(),
                true,
                fbb.getCenter(),
                fbb.create(),
                2.0f,
                100.0f,
                Color.newFromRGBA(0.0f, 0.0f, 0.0f, 1.0f),
                null,
                0.0f,
                true);
        return dm;
    }

    public Mesh createMesh(final Color color, final int nSegments, final Planet planet, final ElevationData ed){
        double o1 = (ed == null)? 0.0 : ed.getElevationAt(_start.asGeodetic2D());
        double o2 = (ed == null)? 0.0 : ed.getElevationAt(_end.asGeodetic2D());

        Vector3D start = planet.toCartesian(_start._latitude, _start._longitude, o1);
        Vector3D end = planet.toCartesian(_end._latitude, _end._longitude, o2);
        Vector3D normal = planet.centricSurfaceNormal(start);
        Vector3D axis = end.sub(start);
        MutableVector3D p = new MutableVector3D(start.add(axis.cross(normal).normalized().times(_width / 2.0)));

        FloatBufferBuilderFromCartesian3D fbb = FloatBufferBuilderFromCartesian3D.builderWithFirstVertexAsCenter();

        MutableMatrix44D m = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(180.0 / (double)nSegments), axis, start);


        final IMathUtils mu = IMathUtils.instance();

        for (int i = 0; i <= nSegments; ++i){
            double r = 2.0 * mu.abs(((double)i / (double)nSegments) - 0.5);

            Vector3D pDir = p.sub(start).normalized();
            double dist = r * _width + (1.0-r) * (o1 - _start._height);
            Vector3D ini = start.add(pDir.times(dist));
            fbb.add(ini);

            dist = r * _width + (1.0-r) * (o2 - _end._height);
            Vector3D fin = end.add(pDir.times(dist));
            fbb.add(fin);

            p = p.transformedBy(m, 1.0);
        }

        DirectMesh dm = new DirectMesh(GLPrimitive.triangleStrip(),
                true,
                fbb.getCenter(),
                fbb.create(),
                2.0f,
                100.0f,
                Color.newFromRGBA(0.0f, 0.0f, 0.0f, 1.0f),
                null,
                0.0f,
                true);
        return dm;
    }
}
