package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.FloatBufferBuilder;
import org.glob3.mobile.generated.FloatBufferBuilderFromCartesian2D;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.GLFormat;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleTextureMapping;
import org.glob3.mobile.generated.TextureIDReference;
import org.glob3.mobile.generated.TextureMapping;
import org.glob3.mobile.generated.TexturedMesh;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector3D;

/**
 * Created by chano on 10/11/17.
 */

public class HoleCoverHelper {

        private static Color groundColor, wallColor, coverColor;
        private static float lineWidth,pointWidth;
        private static int HOLE_DEPTH = 20;
        private static IImage holeimage, coverimage;

        public static void loadHoleImage(final IDownloader downloader, final IThreadUtils utils, final String path){
            utils.invokeInBackground(new GTask() {
                @Override
                public void run(G3MContext context) {
                    downloader.requestImage(new URL(path, false), 1000, TimeInterval.fromDays(30), true, new IImageDownloadListener() {
                        @Override
                        public void onDownload(URL url, IImage image, boolean expired) {
                            holeimage = image;
                        }

                        @Override
                        public void onError(URL url) {}

                        @Override
                        public void onCancel(URL url) {}

                        @Override
                        public void onCanceledDownload(URL url, IImage image, boolean expired) {}
                    }, true);
                }
            },true);
        }

    public static void loadCoverImage(final IDownloader downloader, final IThreadUtils utils, final String path){
        utils.invokeInBackground(new GTask() {
            @Override
            public void run(G3MContext context) {
                downloader.requestImage(new URL(path, false), 1000, TimeInterval.fromDays(30), true, new IImageDownloadListener() {
                    @Override
                    public void onDownload(URL url, IImage image, boolean expired) {
                        coverimage = image;
                    }

                    @Override
                    public void onError(URL url) {}

                    @Override
                    public void onCancel(URL url) {}

                    @Override
                    public void onCanceledDownload(URL url, IImage image, boolean expired) {}
                }, true);
            }
        },true);
    }

        public static void generateHoleCover(Sector sector, Vector3D center, Planet p, G3MRenderContext rc,
                                             ElevationData elevData, MeshRenderer holeRenderer){

            Sector holeSector = generateSector(center,p,9);
            Sector outerSector = generateSector(center,p,15);
            lineWidth = 8;
            pointWidth = 8;
            //Color.fromRGBA255(128,0,0,255);
            wallColor = Color.fromRGBA255(153,76,0,255); //Color.yellow().muchLighter();
            coverColor = wallColor.lighter();
            groundColor = wallColor.darker();
            holeRenderer.clearMeshes();

            generateHole(holeSector,p,rc,elevData,holeRenderer);
            generateOuterCover(holeSector,outerSector,p,rc,elevData,holeRenderer);

        }

        private static Sector generateSector(Vector3D v1, Planet p, double offset){
            Vector3D v2 = new Vector3D(v1._x - offset, v1._y - offset, v1._z);
            Vector3D v3 = new Vector3D(v1._x + offset, v1._y + offset, v1._z);
            Geodetic3D l = p.toGeodetic3D(v2);
            Geodetic3D u = p.toGeodetic3D(v3);
            Geodetic2D lower = Geodetic2D.fromDegrees(
                    Math.min(l._latitude._degrees, u._latitude._degrees),
                    Math.min(l._longitude._degrees, u._longitude._degrees));
            Geodetic2D upper = Geodetic2D.fromDegrees(
                    Math.max(l._latitude._degrees, u._latitude._degrees),
                    Math.max(l._longitude._degrees, u._longitude._degrees));

            return new Sector(lower,upper);
        }

        private static void generateHole(Sector holeSector, Planet p, G3MRenderContext rc,
                                         ElevationData elevData, MeshRenderer holeRenderer){

            Geodetic2D nw = holeSector.getNW();
            Geodetic2D ne = holeSector.getNE();
            Geodetic2D sw = holeSector.getSW();
            Geodetic2D se = holeSector.getSE();

            double hnw = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(nw) + 0.5 : 1;
            double hne = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(ne) + 0.5 : 1;
            double hsw = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(sw) + 0.5 : 1;
            double hse = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(se) + 0.5 : 1;

            double md = ((hnw + hne + hsw + hse) / 4) - HOLE_DEPTH;

            generateHoleGround(holeSector,md,p,holeRenderer,rc);
            generateHoleWall(nw,ne,hnw,hne,md,p,holeRenderer,rc);
            generateHoleWall(ne,se,hne,hse,md,p,holeRenderer,rc);
            generateHoleWall(se,sw,hse,hsw,md,p,holeRenderer,rc);
            generateHoleWall(sw,nw,hsw,hnw,md,p,holeRenderer,rc);

        }

        private static void generateHoleGround(Sector holeSector,double depth, Planet p, MeshRenderer holeRenderer, G3MRenderContext rc){
            Geodetic2D nw = holeSector.getNW();
            Geodetic2D ne = holeSector.getNE();
            Geodetic2D sw = holeSector.getSW();
            Geodetic2D se = holeSector.getSE();
            // Ground
            FloatBufferBuilderFromGeodetic fbb =
                    FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);

            double md = Math.max(depth,1);
            fbb.add(new Geodetic3D(nw,md));
            fbb.add(new Geodetic3D(ne,md));
            fbb.add(new Geodetic3D(se,md));
            fbb.add(new Geodetic3D(sw,md));
            fbb.add(new Geodetic3D(nw,md));

            Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
                    true,
                    fbb.getCenter(),
                    fbb.create(),
                    lineWidth,
                    pointWidth,
                    groundColor,
                    null, 0.0f, true);
            if (coverimage != null){
                String texName = "HOLETEXTURE_" + holeSector.description();
                TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(coverimage, GLFormat.rgba(),texName,false);
                FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
                texCoords.add(0,0);
                texCoords.add(0,1);
                texCoords.add(1,1);
                texCoords.add(1,0);
                TextureMapping tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
                TexturedMesh tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
                holeRenderer.addMesh(tMesh);
            }
            else {
                holeRenderer.addMesh(mesh);
            }
        }

        private static void generateHoleWall(Geodetic2D start, Geodetic2D end, double hStart, double hEnd,
                                             double depth, Planet p, MeshRenderer holeRenderer, G3MRenderContext rc){

            FloatBufferBuilderFromGeodetic fbb =
                    FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);

            double md = Math.max(depth,1);

            fbb.add(new Geodetic3D(start,hStart));
            fbb.add(new Geodetic3D(end,hEnd));
            fbb.add(new Geodetic3D(end,md));
            fbb.add(new Geodetic3D(start,md));
            fbb.add(new Geodetic3D(start,hStart));

            Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
                    true,
                    fbb.getCenter(),
                    fbb.create(),
                    lineWidth,
                    pointWidth,
                    wallColor,//color,
                    null, 0.0f, true);
            if (holeimage != null){
                String texName = "HOLETEXTURE_" + start.description() + end.description();
                TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(holeimage, GLFormat.rgba(),texName,false);
                FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
                texCoords.add(1,0);
                texCoords.add(0,0);
                texCoords.add(0,1);
                texCoords.add(1,1);

                TextureMapping tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
                TexturedMesh tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
                holeRenderer.addMesh(tMesh);
            }
            else {
                holeRenderer.addMesh(mesh);
            }



        }

        private static void generateOuterCover(Sector holeSector,Sector outerSector,
                                               Planet p, G3MRenderContext rc, ElevationData elevData, MeshRenderer holeRenderer){
            Geodetic2D nw = holeSector.getNW();
            Geodetic2D ne = holeSector.getNE();
            Geodetic2D sw = holeSector.getSW();
            Geodetic2D se = holeSector.getSE();

            double hnw = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(nw) + 0.5 : 1;
            double hne = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(ne) + 0.5 : 1;
            double hsw = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(sw) + 0.5 : 1;
            double hse = (elevData != null &&
                    elevData.getSector().fullContains(holeSector)) ?
                    elevData.getElevationAt(se) + 0.5 : 1;

            Geodetic2D onw = outerSector.getNW();
            Geodetic2D one = outerSector.getNE();
            Geodetic2D osw = outerSector.getSW();
            Geodetic2D ose = outerSector.getSE();

            double honw = (elevData != null &&
                    elevData.getSector().fullContains(outerSector)) ?
                    elevData.getElevationAt(onw) : 1;
            double hone = (elevData != null &&
                    elevData.getSector().fullContains(outerSector)) ?
                    elevData.getElevationAt(one) : 1;
            double hosw = (elevData != null &&
                    elevData.getSector().fullContains(outerSector)) ?
                    elevData.getElevationAt(osw) : 1;
            double hose = (elevData != null &&
                    elevData.getSector().fullContains(outerSector)) ?
                    elevData.getElevationAt(ose) : 1;

            Geodetic3D outerNW = new Geodetic3D(onw,honw);
            Geodetic3D outerNE = new Geodetic3D(one,hone);
            Geodetic3D outerSW = new Geodetic3D(osw,hosw);
            Geodetic3D outerSE = new Geodetic3D(ose,hose);
            Geodetic3D innerNW = new Geodetic3D(nw,hnw);
            Geodetic3D innerNE = new Geodetic3D(ne,hne);
            Geodetic3D innerSW = new Geodetic3D(sw,hsw);
            Geodetic3D innerSE = new Geodetic3D(se,hse);

            generateOuterGround(outerNW,outerNE,innerNW,innerNE,p,holeRenderer,rc);
            generateOuterGround(outerNE,outerSE,innerNE,innerSE,p,holeRenderer,rc);
            generateOuterGround(outerSE,outerSW,innerSE,innerSW,p,holeRenderer,rc);
            generateOuterGround(outerSW,outerNW,innerSW,innerNW,p,holeRenderer,rc);

        }

        private static void generateOuterGround(Geodetic3D outerStart, Geodetic3D outerEnd,Geodetic3D innerStart, Geodetic3D innerEnd,
                                                 Planet p, MeshRenderer holeRenderer, G3MRenderContext rc ){
            FloatBufferBuilderFromGeodetic fbb =
                    FloatBufferBuilderFromGeodetic.builderWithFirstVertexAsCenter(p);

            fbb.add(innerStart);
            fbb.add(innerEnd);
            fbb.add(outerEnd);
            fbb.add(outerStart);
            fbb.add(innerStart);

            Mesh mesh = new DirectMesh(GLPrimitive.triangleStrip(),
                    true,
                    fbb.getCenter(),
                    fbb.create(),
                    lineWidth,
                    pointWidth,
                    coverColor,//color,
                    null, 0.0f, true);
            if (coverimage != null){
                String texName = "HOLETEXTURE_" + outerStart.description() + outerEnd.description();
                TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(coverimage, GLFormat.rgba(),texName,false);
                FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
                texCoords.add(0,0);
                texCoords.add(1,0);
                texCoords.add(1,1);
                texCoords.add(0,1);
                TextureMapping tMapping = new SimpleTextureMapping(texId,texCoords.create(),true,true);
                TexturedMesh tMesh = new TexturedMesh(mesh,true,tMapping,true,false);
                holeRenderer.addMesh(tMesh);
            }
            else {
                holeRenderer.addMesh(mesh);
            }
        }
}
