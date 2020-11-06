package com.visitasgps;


import android.Manifest;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.io.*;
import java.util.Locale;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitasgps.R;

public class MainActivityAmasoft extends Activity {
	private final static String ETIQUETA_ERROR = "ERROR";
	public String codcliente;
	public int versiondb = 1;
	TextView distancia;
	TextView messageTextView;
	TextView messageTextView2;
	TextView txtlatituddelclienteguardado;
	TextView txtlongituddelclienteguardado;
	String stringlatituddelclienteguardado = "";
	String stringlongituddelclienteguardado = "";
	String stringposicionenelmapa = "";
	private EditText txtNumclienteRuta;
	private TextView txtnombrecliente;
	public Button btnaceptar;
	public Button btnverenmapa;
	private ImageButton btncomprobarcliente;
	public int ubicado;
	private Button bt_hacerfoto, btnSalir;
	public String stringlatitudactual;
	public String stringlongitudactual;
	private ImageView img;

	public String Stringcodvendedor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_amasoft);


		Bundle bundle = getIntent().getExtras();
		Stringcodvendedor = bundle.getString("Stringcodvendedor");


		messageTextView = (TextView) findViewById(R.id.message_id);
		messageTextView2 = (TextView) findViewById(R.id.message_id2);

		distancia = (TextView) findViewById(R.id.distancia);

		distancia.setText("Distancia de lo guardado=");


		txtNumclienteRuta = (EditText) findViewById(R.id.txtNumclienteRuta);
		txtnombrecliente = (TextView) findViewById(R.id.txtnombrecliente);

		txtlatituddelclienteguardado = (TextView) findViewById(R.id.latitudcliente);
		txtlongituddelclienteguardado = (TextView) findViewById(R.id.longitudcliente);


		txtlatituddelclienteguardado.setText("0");
		txtlongituddelclienteguardado.setText("0");


		ImageButton btncomprobarcliente = (ImageButton) findViewById(R.id.btncomprobarcliente);
		Button btnaceptar = (Button) findViewById(R.id.btnaceptar);
		Button btnverenmapa = (Button) findViewById(R.id.btnverenmapa);

		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		MyLocationListener mlocListener = new MyLocationListener();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				(LocationListener) mlocListener);

		messageTextView.setText("Esperando Ubicación");
		ubicado =0;
		messageTextView2.setText("");
		codcliente="";
		
		final Drawable drawable = this.getResources().getDrawable(R.drawable.sinfoto);
	
		
		
		
		img = (ImageView)this.findViewById(R.id.imageView1);
	      bt_hacerfoto = (Button) this.findViewById(R.id.btnfoto);
	      btnSalir= (Button) this.findViewById(R.id.btnSalir);
	      
	      
	      btnSalir.setOnClickListener(new View.OnClickListener() {
		      @Override
			     
		      public void onClick(View v) {
		    	  finish();
		      }});
	      
	      
	      
	       bt_hacerfoto.setOnClickListener(new View.OnClickListener() {
	      @Override
	     
	      public void onClick(View v) {
	    	  
	      
	    	  // todo esto si es que hay codigo_cliente
	    	  
	    	  if ( ( txtNumclienteRuta.getText().toString() != null) && (!txtNumclienteRuta.getText().toString().equals("")) ) {
	    	  
	    		  if ( ( codcliente.toString() != null) && (! codcliente.toString().equals("")) ) {
	    	    	  
	    	  
	    	  Intent cameraIntent = new Intent(
	            android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	    	  
	             File imagesFolder = new File( Environment.getExternalStorageDirectory(), ".fotos_fachadas.");
	         
	             imagesFolder.mkdirs();   
	         
	         File image = new File(imagesFolder, codcliente.toString() +".jpg"); 
	         
	         Uri uriSavedImage = Uri.fromFile(image);	         
	         
	         cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);	         
	         
	         startActivityForResult(cameraIntent, 1);
	    		 
	    		  
	    		  
	    		  }else{
	    			  Toast.makeText(getApplicationContext(), "Primero Ingrese el Cliente",
	  						Toast.LENGTH_SHORT).show();
	    			  
	    		  } 
	      }else {
	    	  Toast.makeText(getApplicationContext(), "Primero Ingrese el Cliente",
						Toast.LENGTH_SHORT).show();	
	      }
	      
	      // hasta aqui
	      
	      }});
		
	       btnverenmapa.setOnClickListener(new View.OnClickListener() {
		 	      @Override
			 	    
		 	      public void onClick(View v) {
		 	    	  
		 	    	 if ( ( stringposicionenelmapa.toString() != null) && (!stringposicionenelmapa.toString().equals("")) ) {
		 	    		 if ( stringposicionenelmapa.toString().equals("0") )
		 	    		 {
		 	    			 Toast.makeText(getApplicationContext(), "No disponible",
			 	  						Toast.LENGTH_LONG).show();
		 	    			 
		 	    		 } else{
		 	    		Log.e(ETIQUETA_ERROR, "obras ATENCION "+stringposicionenelmapa.toString()  );
		 	    		 	Intent browserIntent = new Intent("android.intent.action.VIEW",
		 	    			//Uri.parse("https://www.google.com/maps/place/25%C2%B017'16.0%22S+57%C2%B037'19.2%22W/@-25.2877966,-57.6232208,18.06z/DATA=!4m5!3m4!1s0x0:0x0!8m2!3d-25.2877688!4d-57.6219882?hl=es"));
		 	    		 	Uri.parse(stringposicionenelmapa.toString().trim()));
		 	    		 	startActivity(browserIntent);
		 	    	  
		 	    	 }}
		 	    	  
		 	      }});
		       
	       
	       
	       
	       
	       
	       
	       
	       
	       btnaceptar.setOnClickListener(new View.OnClickListener() {
	 	      @Override
	 	     
	 	      public void onClick(View v) {
	 	    	  
	 	      
	 	    	  // todo esto si es que hay codigo_cliente
	 	    	  
	 	    	  if ( ( txtNumclienteRuta.getText().toString() != null) && (!txtNumclienteRuta.getText().toString().equals("")) ) {
	 	    	  
	 	    		  if ( ( codcliente.toString() != null) && (! codcliente.toString().equals("")) ) {
	 	    	  if (ubicado>0){
	 	    		  //desea guardar
	 	    	  
	 	    		  	actualizar();	 	    		  
	 	    		  
	 	    		  
	 	    			
	 	    	  
	 	    	  // fin de guardar
	 	    	  
	 	    	  
	 	    	  
	 	    	  }else {
	 	    		  Toast.makeText(getApplicationContext(), "Ubicación no disponible",
	 	  						Toast.LENGTH_LONG).show();
	 	    	  	}
	 	    	    }else{
	 	    			  Toast.makeText(getApplicationContext(), "Primero Ingrese el Cliente",
	 	  						Toast.LENGTH_LONG).show();	 	    			  
	 	    		  } 
	 	      }else {
	 	    	 	
	 	      }	 	      
	 	      }});
	       
		
	       btncomprobarcliente.setOnClickListener(new View.OnClickListener() {
	 			@Override
	 			public void onClick(View arg0) { 				 				
	 				 if ( ( txtNumclienteRuta.getText().toString() != null) && (!txtNumclienteRuta.getText().toString().equals("")) ) {   				
	 					 //preguntar_sincronizar(); 		
	 					 
	 					UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
	         					"DBUsuarios", null, 1);
	         					final SQLiteDatabase db = sql.getWritableDatabase();				
	         					if (db != null) 
	         					{
	         					    String consulta="SELECT codcliente,nombre,latitud,longitud ,ubicacionenmapa  from clientes where numcliente = "+ txtNumclienteRuta.getText().toString();					    
	         						try {
	         									Cursor c = db.rawQuery(consulta, null);					
	         									if (c.moveToFirst()) 
	         										{
	         										codcliente=c.getString(0).toString();
	         										txtnombrecliente.setText(c.getString(1).toString());
	         										
	         										txtlatituddelclienteguardado.setText( "LA:"  +c.getString(2).toString());
	         										txtlongituddelclienteguardado.setText("LO:"+c.getString(3).toString());
	         												
	         										
	         										stringlatituddelclienteguardado =c.getString(2).toString();
	         										stringlongituddelclienteguardado =c.getString(3).toString();
	         										stringposicionenelmapa=c.getString(4).toString();
	         									          
	         									           	String sFichero = Environment.getExternalStorageDirectory()+
		         									                "/.fotos_fachadas./"+codcliente.toString()+".jpg";
		         									                
	         									           	File fichero = new File(sFichero);	         									           		
	         									            if (fichero.exists()){
	         									        	//  System.out.println("El fichero " + sFichero + " existe");
	         									            	Bitmap bMap = BitmapFactory.decodeFile(
	       	         									             Environment.getExternalStorageDirectory()+
	       	         									                "/.fotos_fachadas./"+codcliente.toString()+".jpg");	         									           
	       	         									           		img.setImageBitmap(bMap);    	       	         									    
	         									            }
	         									        	else{
	         									        	  //System.out.println("Pues va a ser que no")
	         									        	
	         									        		//Drawable drawable = this.getResources().getDrawable(R.drawable.sinfoto);	         									        	
	         									        		img.setImageDrawable(drawable);	         									        		
	         									        		//Bitmap bMap = drawable;
	         									        		//R.drawable.sinfoto;
	         									        	};	 
	         									        	
	         									        	
	         									        	Button btnverenmapa=(Button) findViewById(R.id.btnverenmapa);
	         									        	         									        	
	         									        	//stringposicionenelmapa
	         									        	
	         									        	 Log.e(ETIQUETA_ERROR, "obras ATENCION posicion="+stringposicionenelmapa.toString().trim() );
	         										}else{
	         										
	         											stringposicionenelmapa="";
	         										//	Toast.makeText(getApplicationContext(), "Cliente no encontrado, Desea Actualizarlo?",
	         					        				//		Toast.LENGTH_LONG).show();		   											
	         										
	         											
	         											txtnombrecliente.setText("");
	         											codcliente="";
	         										
	         											preguntarsincronizar();
	         											
	         										
	         											
	         										
	         										
	         										}
	         						}catch(Exception e){
	         							stringposicionenelmapa="";
	         							Toast.makeText(getApplicationContext(), "error en la consulta",
	     		        						Toast.LENGTH_SHORT).show();		
	         						}
	         					}	           	    	 
	 			 }				 				
	 			}
	 			});	       		
	}
public void llamaractualizarfichacliente(){
	 
	  Intent c = new Intent(this, ActivityActualizarCliente.class );
	  c.putExtra("Stringcodvendedor", Stringcodvendedor);
	  startActivity(c);	
  	 
}
public void preguntarsincronizar(){
	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setMessage( "Cliente no encontrado, Desea Actualizarlo?"		)
	 	       .setCancelable(false)
	 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	        	  
	 	       
	 	        	// LLAMAR A  ActivityActualizarCliente
	 	        	  llamaractualizarfichacliente();
	 	        	   
	 	        	
	 	           }
	 	       })
	 	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	                dialog.cancel();
	 	           }
	 	       });
	 	AlertDialog alert = builder.create();    	
	 	alert.show();					
		
		
}

	public void actualizar(){
	
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Actualizar la Ubicación del Cliente?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	  UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
			 						"DBUsuarios", null,versiondb);
			 				final SQLiteDatabase db3 = sql3.getWritableDatabase();
			 				try
			 				{
			 					//  
			 					

							
			 					
			 					String Stringsql ="Update Clientes  set ubicaciontransferir=1,   latitud='"+ stringlatitudactual + "',longitud='" + stringlongitudactual + "'  where codcliente="+codcliente.toString();
			 					db3.execSQL(Stringsql);
			 					Toast.makeText(getApplicationContext(),
			 			   				"Ubicación Actualizada", Toast.LENGTH_SHORT).show();
			 					
			 					
			 				}catch (Exception e){
			 					Toast.makeText(getApplicationContext(),
			 			   				"No se pudo actualizar LA Ubicación", Toast.LENGTH_SHORT).show();			   	    
			 				}	 
			 	    	  
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });			
			AlertDialog alert = builder.create();    	
			alert.show();	 
	}
	
	public void setLocation(Location loc) {
		//Obtener la direcci—n de la calle a partir de la latitud y la longitud 
		if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
			try {
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				List<Address> list = geocoder.getFromLocation(
						loc.getLatitude(), loc.getLongitude(), 1);
				if (!list.isEmpty()) {
					Address address = list.get(0);
					messageTextView2.setText("Mi direcci—n es: \n"
							+ address.getAddressLine(0));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener {
		MainActivity mainActivity;

		public MainActivity getMainActivity() {
			return mainActivity;
		}

		public void setMainActivity(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
		}

		@Override
		public void onLocationChanged(Location loc) {
			// Este mŽtodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
			// debido a la detecci—n de un cambio de ubicacion
			loc.getLatitude();
			loc.getLongitude();
			
			String Text = "Ubicación Actual : " + "\n Lat = "
					+ loc.getLatitude() + "\n Long = " + loc.getLongitude();
			messageTextView.setText(Text);
			// aqui encontro
			ubicado=1;
			
			stringlatitudactual=String.valueOf(loc.getLatitude()) ;
			stringlongitudactual=String.valueOf( loc.getLongitude());
		
		}

		@Override
		public void onProviderDisabled(String provider) {
			// Este mŽtodo se ejecuta cuando el GPS es desactivado
			messageTextView.setText("GPS Desactivado");
			// aqui no disponible la ubicacion
			ubicado=0;
		}

		@Override
		public void onProviderEnabled(String provider) {
			// Este mŽtodo se ejecuta cuando el GPS es activado
			messageTextView.setText("GPS Activado");
			// aqui buscando
			ubicado=0;
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Este mŽtodo se ejecuta cada vez que se detecta un cambio en el
			// status del proveedor de localizaci—n (GPS)
			// Los diferentes Status son:
			// OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
			// TEMPORARILY_UNAVAILABLE -> Temp˜ralmente no disponible pero se
			// espera que este disponible en breve
			// AVAILABLE -> Disponible
		}

	}/* End of Class MyLocationListener */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {  
 //Creamos un bitmap con la imagen recientemente 
        //almacenada en la memoria
           Bitmap bMap = BitmapFactory.decodeFile(
             Environment.getExternalStorageDirectory()+
     "/.fotos_fachadas./"+codcliente.toString()+".jpg");
           //Añadimos el bitmap al imageView para 
           //mostrarlo por pantalla
           img.setImageBitmap(bMap);  
           //foto actualizada
           
           
           
           Toast.makeText(getApplicationContext(), "fotografia actualizada",
						Toast.LENGTH_LONG).show();
		
           
           
           UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
					"DBUsuarios", null,versiondb);
			final SQLiteDatabase db3 = sql3.getWritableDatabase();
			try
			{
				//  
				//nuevoRegistro.put("fototransferir",fototransferir);
				//nuevoRegistro.put("ubicaciontransferir",ubicaciontransferir);
				
				String Stringsql ="Update Clientes  set fototransferir=1 where codcliente="+codcliente.toString();
				db3.execSQL(Stringsql);
				Toast.makeText(getApplicationContext(),
		   				"Fotografía  Actualizada", Toast.LENGTH_SHORT).show();
				
				
			}catch (Exception e){
				Toast.makeText(getApplicationContext(),
		   				"No se pudo actualizar La Fotografía", Toast.LENGTH_SHORT).show();			   	    
			}	 
    	  
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
           
} 
	} 
	
	//** desde aqui
	 
	
	
	
	
	
	//  hasta aqui
}
