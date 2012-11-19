

package org.glob3.mobile.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class G3MDemoActivity
         extends
            Activity {

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_g3_mdemo);

      final Button simpleG3MButton = (Button) findViewById(R.id.simpleG3MButton);


      simpleG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MSimplestGlob3Activity.class);
            startActivity(intent);
         }
      });

      final Button doubleG3MButton = (Button) findViewById(R.id.doubleGlob3G3MButton);


      doubleG3MButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(final View v) {
            final Intent intent = new Intent(getApplicationContext(), G3MDoubleGlob3Activity.class);
            startActivity(intent);
         }
      });

   }
}
