package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.ByteBufferBuilder;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GEO2DLineRasterStyle;
import org.glob3.mobile.generated.GEO2DLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiLineStringGeometry;
import org.glob3.mobile.generated.GEO2DMultiPolygonGeometry;
import org.glob3.mobile.generated.GEO2DPointGeometry;
import org.glob3.mobile.generated.GEO2DPolygonData;
import org.glob3.mobile.generated.GEO2DPolygonGeometry;
import org.glob3.mobile.generated.GEO2DSurfaceRasterStyle;
import org.glob3.mobile.generated.GEOGeometry;
import org.glob3.mobile.generated.GEOPolygonRasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbol;
import org.glob3.mobile.generated.GEORasterSymbolizer;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.MapBoxLayer;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.PointCloudMesh;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.StrokeCap;
import org.glob3.mobile.generated.StrokeJoin;
import org.glob3.mobile.generated.TiledVectorLayer;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class G3MWebGLTestingApplication implements EntryPoint {

	private final String _g3mWidgetHolderId = "g3mWidgetHolder";

	private G3MWidget_WebGL _widget = null;

	private final boolean _markersParsed = false;
	private MarksRenderer _markersRenderer;
	private final ShapesRenderer _shapesRenderer = new ShapesRenderer();

	private native void runUserPlugin() /*-{
		$wnd.onLoadG3M();
	}-*/;

	@Override
	public void onModuleLoad() {
		// initCustomizedWithBuilder();

		final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

		final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

		// builder.getPlanetRendererBuilder().setLayerSet(createLayerSet());
		//
		// final String proxy = null; // "http://galileo.glob3mobile.com/" +
		// "proxy.php?url="
		// builder.setDownloader(new Downloader_WebGL( //
		// 8, // maxConcurrentOperationCount
		// 10, // delayMillis
		// proxy));
		
	      final LayerSet layerSet = new LayerSet();
	      final MapBoxLayer mboxTerrainLayer = new MapBoxLayer("examples.map-qogxobv1", TimeInterval.fromDays(30), true, 2);
	      layerSet.addLayer(mboxTerrainLayer);
	      builder.getPlanetRendererBuilder().setLayerSet(layerSet);


		boolean pointCloud = true;
		if (pointCloud) { // POINT-CLOUD-MESH

			FloatBufferBuilderFromGeodetic fbb = FloatBufferBuilderFromGeodetic
					.builderWithoutCenter(builder.getPlanet());
			ByteBufferBuilder bbb = new ByteBufferBuilder();

			for (int i = 0; i < 10000; i++) {
				// Point
				double lat = (Random.nextInt(18000) - 9000) / 100.0;
				double lon = (Random.nextInt(36000) - 18000) / 100.0;
				double h = Math.random() * 10000;
				fbb.add(Angle.fromDegrees(lat), Angle.fromDegrees(lon), h);

				// Color
				int r = Random.nextInt(256);
				int g = Random.nextInt(256);
				int b = Random.nextInt(256);

				bbb.add((byte) 255); // R
				bbb.add((byte) 0); // G
				bbb.add((byte) 0); // B
			}

			IFloatBuffer points = fbb.create();
			IByteBuffer colors = bbb.create();

			MeshRenderer mr = new MeshRenderer();

			PointCloudMesh pcm = new PointCloudMesh(points, true, colors, true,
					(float) 10.0, false);
			mr.addMesh(pcm);

			builder.addRenderer(mr);

			// Changing point size
			class PointCloudTask extends GTask {
				PointCloudMesh _mesh;
				int _size = 1;
				boolean _growing = true;

				public PointCloudTask(PointCloudMesh mesh) {
					_mesh = mesh;
				}

				@Override
				public void run(G3MContext context) {
					// TODO Auto-generated method stub
					if (_growing) {
						_size++;
					} else {
						_size--;
					}

					if (_size == 1) {
						_growing = true;
					} else if (_size == 10) {
						_growing = false;
					}

					_mesh.setPointSize(_size);
				}

			}
			;

			builder.addPeriodicalTask(new PeriodicalTask(TimeInterval
					.fromSeconds(1.0), new PointCloudTask(pcm)));
		}

		_widget = builder.createWidget();
		
		_widget.getG3MWidget().getPlanetRenderer().setEnable(false);
		
		g3mWidgetHolder.add(_widget);
	}

	private LayerSet createLayerSet() {
		final LayerSet layerSet = new LayerSet();
		// layerSet.addLayer(MapQuestLayer.newOSM(TimeInterval.fromDays(30)));

		layerSet.addLayer(new MapBoxLayer("examples.map-9ijuk24y", TimeInterval
				.fromDays(30)));

		final String urlTemplate = "http://192.168.1.2/ne_10m_admin_0_countries-Levels10/{level}/{y}/{x}.geojson";
		final int firstLevel = 2;
		final int maxLevel = 10;

		final GEORasterSymbolizer symbolizer = new SampleRasterSymbolizer();

		final TiledVectorLayer tiledVectorLayer = TiledVectorLayer.newMercator( //
				symbolizer, //
				urlTemplate, //
				Sector.fullSphere(), // sector
				firstLevel, //
				maxLevel, //
				TimeInterval.fromDays(30), // timeToCache
				true, // readExpired
				1, // transparency
				null // condition
				);
		layerSet.addLayer(tiledVectorLayer);

		return layerSet;
	}

	private static class SampleRasterSymbolizer extends GEORasterSymbolizer {

		private static final Color FROM_COLOR = Color
				.fromRGBA(0.7f, 0, 0, 0.5f);

		private static GEO2DLineRasterStyle createPolygonLineRasterStyle(
				final GEOGeometry geometry) {
			final JSONObject properties = geometry.getFeature().getProperties();
			final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
			final Color color = FROM_COLOR.wheelStep(7, colorIndex)
					.muchLighter().muchLighter();
			final float dashLengths[] = {};
			final int dashCount = 0;
			return new GEO2DLineRasterStyle(color, 2, StrokeCap.CAP_ROUND,
					StrokeJoin.JOIN_ROUND, 1, dashLengths, dashCount, 0);
		}

		private static GEO2DSurfaceRasterStyle createPolygonSurfaceRasterStyle(
				final GEOGeometry geometry) {
			final JSONObject properties = geometry.getFeature().getProperties();
			final int colorIndex = (int) properties.getAsNumber("mapcolor7", 0);
			final Color color = FROM_COLOR.wheelStep(7, colorIndex);
			return new GEO2DSurfaceRasterStyle(color);
		}

		@Override
		public GEORasterSymbolizer copy() {
			return new SampleRasterSymbolizer();
		}

		@Override
		public ArrayList<GEORasterSymbol> createSymbols(
				final GEO2DPointGeometry geometry) {
			return null;
		}

		@Override
		public ArrayList<GEORasterSymbol> createSymbols(
				final GEO2DLineStringGeometry geometry) {
			return null;
		}

		@Override
		public ArrayList<GEORasterSymbol> createSymbols(
				final GEO2DMultiLineStringGeometry geometry) {
			return null;
		}

		@Override
		public ArrayList<GEORasterSymbol> createSymbols(
				final GEO2DPolygonGeometry geometry) {
			final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();
			final GEOPolygonRasterSymbol symbol = new GEOPolygonRasterSymbol( //
					geometry.getPolygonData(), //
					createPolygonLineRasterStyle(geometry), //
					createPolygonSurfaceRasterStyle(geometry) //
			);
			symbols.add(symbol);
			return symbols;
		}

		@Override
		public ArrayList<GEORasterSymbol> createSymbols(
				final GEO2DMultiPolygonGeometry geometry) {
			final ArrayList<GEORasterSymbol> symbols = new ArrayList<GEORasterSymbol>();

			final GEO2DLineRasterStyle lineStyle = createPolygonLineRasterStyle(geometry);
			final GEO2DSurfaceRasterStyle surfaceStyle = createPolygonSurfaceRasterStyle(geometry);

			for (final GEO2DPolygonData polygonData : geometry
					.getPolygonsData()) {
				symbols.add(new GEOPolygonRasterSymbol(polygonData, lineStyle,
						surfaceStyle));
			}

			return symbols;
		}
	}

}
