package agndul.gramiejska;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class
		Mapa extends Activity {
	
	GoogleMap googleMap;
	MarkerOptions marker;
	Button cel;
	Intent in;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapka);
        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("lat", 0);
        final double lon = intent.getDoubleExtra("lon", 0);
     
        final double lat2 = intent.getDoubleExtra("lat2", 0);
        final double lon2 = intent.getDoubleExtra("lon2", 0);
          
     //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        cel = (Button) findViewById(R.id.cel);
        try{
            // Loading map
        	googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
               	
		
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(12).build();
           
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    
				googleMap.setMyLocationEnabled(true);
            Location location = googleMap.getMyLocation();
       	 final double  myLat = location.getLatitude();
         final double  myLon = location.getLongitude();
         
       	cel.setOnClickListener(new OnClickListener(){
            	
    			@Override
    			public void onClick(View v){
    			in = new Intent(getApplicationContext(), Zagadka.class);
    			startActivity(in);
    			}
    			});
        	
        	
        	marker = new MarkerOptions().position(new LatLng(lat2,lon2));
			marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
     
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
	}

}
