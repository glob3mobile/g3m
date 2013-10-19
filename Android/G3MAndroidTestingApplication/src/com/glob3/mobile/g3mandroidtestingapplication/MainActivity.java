package com.glob3.mobile.g3mandroidtestingapplication;

import org.glob3.mobile.generated.AltitudeMode;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private G3MWidget_Android _g3mWidget;
	private RelativeLayout _placeHolder;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		final G3MBuilder_Android builder = new G3MBuilder_Android(this);
		builder.getPlanetRendererBuilder().setRenderDebug(true);

		if (false) { // Testing lights
			ShapesRenderer sr = new ShapesRenderer();
			sr.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 0, 0),
					AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(1000000,
							1000000, 1000000), (float) 1.0, Color.red(), Color
							.black(), true)); // With normals

			sr.addShape(new BoxShape(Geodetic3D.fromDegrees(0, 180, 0),
					AltitudeMode.RELATIVE_TO_GROUND, new Vector3D(1000000,
							1000000, 1000000), (float) 1.0, Color.blue(), Color
							.black(), true)); // With normals

			builder.addRenderer(sr);
		}

		_g3mWidget = builder.createWidget();
		_placeHolder = (RelativeLayout) findViewById(R.id.g3mWidgetHolder);
		_placeHolder.addView(_g3mWidget);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
