package org.eifer.eiferapp.g3mutils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.eifer.eiferapp.GlobeFragment;
import org.eifer.eiferapp.R;
import org.glob3.mobile.generated.CityGMLBuilding;
import org.glob3.mobile.generated.CityGMLBuildingTouchedListener;

/**
 * Created by chano on 7/11/17.
 */

public class MyCityGMLBuildingTouchedListener extends CityGMLBuildingTouchedListener {

    GlobeFragment gf;
    Context ctx;

    public MyCityGMLBuildingTouchedListener(GlobeFragment globeFragment){
        gf = globeFragment;
        this.ctx = globeFragment.getContext();
    }

    @Override
    public void dispose() {
        gf = null;
        ctx = null;
    }

    @Override
    public void onBuildingTouched(CityGMLBuilding building) {

        final CityGMLBuilding theBuilding = building;

        gf.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                // set title
                alertDialogBuilder.setTitle(ctx.getString(R.string.buildings_title));
                // Set message
                String msg = "ID: "+theBuilding._name+ "\n" + theBuilding.getPropertiesDescription();
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton(ctx.getString(R.string.buildings_ok),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                gf.loadSolarRadiationPointCloudForBuilding(theBuilding);
                            }
                        })
                        .setNegativeButton(ctx.getString(R.string.buildings_cancel),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

}