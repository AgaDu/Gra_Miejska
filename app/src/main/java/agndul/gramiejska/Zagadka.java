package agndul.gramiejska;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Zagadka extends Activity implements TextToSpeech.OnInitListener{
	Button sprawdz;
	Button powtorz;
	Button dalej;
	EditText wynik;
	TextView spr;
	TextView zagadka_txt;
	TextToSpeech tts;

	DataBase db;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zagadka); 
   
        
        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("lat", 50);
        final double lon = intent.getDoubleExtra("lon", 19);
       
        tts = new TextToSpeech(this, this);
       
        db = new DataBase(this);
        db.open();
        db.insertSomeS();
        
        long minI = 1L;
        final Cursor min_odl = db.fetchStudent(3L);
        String maly=min_odl.getString(1);
       Cursor temp;
       	double DLat1, DLat2, DLon1, DLon2;
      
        for(long i=2L; i<5L; i++){
        	temp = db.fetchStudent(i);
        	DLat1=(temp.getDouble(2)-lat);
        	DLon1=(temp.getDouble(3)-lon);
        	DLat2=(min_odl.getDouble(2)-lat);
        	DLon2=(min_odl.getDouble(3)-lon);
             	  	
        	if((Math.sqrt(DLat1*DLat1 + DLon1*DLon1) < Math.sqrt(DLat2*DLat2 + DLon2*DLon2)) && (Math.sqrt(DLat1*DLat1 + DLon1*DLon1) < 500)){
        	maly = temp.getString(1);
				Log.i("maly", maly);
        	  minI = i;
        }
  
      }
              
        
        final Cursor krotka_podpowiedz = db.fetchTXTByName(maly);

        final double lat2 = 50.06195; //krotka_podpowiedz.getDouble(2);
        final double lon2 = 19.94011;//krotka_podpowiedz.getDouble(3);
      
   //     final String long_desc = krotka_podpowiedz.getString(5);
              		 
        zagadka_txt = (TextView)findViewById(R.id.zagadka_txt);
        dalej = (Button) findViewById(R.id.dalej);
        powtorz = (Button) findViewById(R.id.powtorz);       
        sprawdz = (Button) findViewById(R.id.sprawdz);
        
        zagadka_txt.setText(krotka_podpowiedz.getString(6));
                
        sprawdz.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v){
				wynik = (EditText)findViewById(R.id.wynik);
				spr = (TextView)findViewById(R.id.spr);
				if(wynik.getText().toString().matches("")){
					tts.speak("wpisz wynik", TextToSpeech.QUEUE_FLUSH, null);
					
				}else{
				if(Integer.parseInt(wynik.getText().toString()) == krotka_podpowiedz.getInt(5)){
					spr.setText(Double.toString(lat2) + ", " + Double.toString(lon2));
					
					dalej.setVisibility(1);
				 powtorz.setVisibility(1);   
					tts.speak("Gratulacje! Prawidłowo rozwiązałeś zagadkę, w nagrodę otrzymujesz cenne wskazówki. Pomogą ci one dotrzeć do " + krotka_podpowiedz.getString(4), TextToSpeech.QUEUE_FLUSH, null);
					
				}else{
					
					tts.speak("Żle, spróbuj jeszcze raz", TextToSpeech.QUEUE_FLUSH, null);
					
				}}
			}
		});
        
        powtorz.setOnClickListener(new OnClickListener(){
        	
    			@Override
    			public void onClick(View v){
    				
    				if(Integer.parseInt(wynik.getText().toString()) == krotka_podpowiedz.getInt(5)){
    					
    					tts.speak("miejsce, które " + krotka_podpowiedz.getString(4), TextToSpeech.QUEUE_FLUSH, null);
    					
    				}
    			}
    		});
            
        
       
        dalej.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v){
				
					Intent intent = new Intent(getApplicationContext(), Mapa.class);
					intent.putExtra("lat", lat);
					intent.putExtra("lon", lon);
					intent.putExtra("lat2", lat2);
					intent.putExtra("lon2", lon2);
					startActivity(intent);
				
			}
		});
        
        db.updateStudent(minI, 1000, 1000);
        final Cursor min_odl2 = db.fetchStudent(minI); 
        
       	
	}
	@Override
	public void onInit(int status) {

	        if (status == TextToSpeech.SUCCESS) {
	            int result = tts.setLanguage(Locale.getDefault());
	            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	                Log.e("TTS", "This Language is not supported");
	            } 
	        } else {
	            Log.e("TTS", "Initilization Failed!");
	        }
	    }

	public void onDestroy(){
		try{
			
				db.close();
			
		}catch(Exception e){
			
		}
	}
	
}
