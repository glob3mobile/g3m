/**
 *
 */


package org.glob3.mobile.demo;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * @author mdelacalle
 *
 */

public class DataSourceAdapter
         extends
            ArrayAdapter<String> {

   private final Activity     _context;
   private final List<String> _itemList;


   /**
    * @param context
    * @param textViewResourceId
    */
   public DataSourceAdapter(final Activity context,
                            final List<String> dataSourceList) {
      super(context, R.layout.adapterrow);
      this._context = context;
      this._itemList = dataSourceList;
   }


   @Override
   public View getView(final int position,
                       final View convertView,
                       final ViewGroup parent) {

      final LayoutInflater inflater = _context.getLayoutInflater();
      final View row = inflater.inflate(R.layout.adapterrow, null);
      // final LinearLayout rowLy = (LinearLayout) row.findViewById(R.id.rowLy);
      //  rowLy.setBackgroundResource(R.color.navigation_bg);
      final TextView var = (TextView) row.findViewById(R.id.layername);
      var.setText(_itemList.get(position));
      var.setGravity(Gravity.CENTER);
      var.setTextSize(20);
      var.setTextColor(_context.getResources().getColor(R.color.g3mGreenDark));
      var.setBackgroundColor(Color.WHITE);
      return row;
   }


   @Override
   public int getCount() {
      return _itemList.size();
   }


   @Override
   public View getDropDownView(final int position,
                               final View convertView,
                               final ViewGroup parent) {

      final LayoutInflater inflater = _context.getLayoutInflater();
      final View row = inflater.inflate(R.layout.adapterrow, null);
      final TextView var = (TextView) row.findViewById(R.id.layername);
      var.setText(_itemList.get(position));

      var.setGravity(Gravity.CENTER);
      var.setTextSize(20);
      var.setTextColor(_context.getResources().getColor(R.color.g3mGreenDark));
      var.setBackgroundColor(Color.WHITE);

      return row;
   }


   @Override
   public String getItem(final int position) {
      return _itemList.get(position);
   }
}
