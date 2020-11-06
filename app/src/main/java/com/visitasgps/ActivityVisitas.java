package com.visitasgps;


import java.util.Calendar;
import java.util.Random;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitasgps.R;

public class ActivityVisitas extends Activity {

	public int versiondb=1;
	private int year;
	private int month;
	private int day,HORA,MINUTO,SEGUNDO;
	public String txtDatefecha,consultavisitas;
    public int idvisitaactual=0;
    public String Stringcodcliente="";
	public String  latitudutilizada="";
	public String  longitudutilizada="";
	public String  Stringrandon="";
	public String  Stringlatitudcliente="";
	public String  Stringlongitudcliente="";
	
	public String fechadehoy="";
	public String horaactual="";
	
	
	public int  tipoutilizado=0;
	
	public TextView lblclientevisitaavierta;
	public String Stringcodvendedor="";
	private ImageButton btnaceptar;
	private Button btniniciarvisita;
	private EditText txtNumcliente;
	public TextView lblultimavisita;
	private   ProgressDialog progressDialog ;
	private   ProgressDialog progressDialogTerminar ;
	private TextView txtLatitudGPS;
	private TextView txtLongitudGPS;
	private TextView txtLatitudNet;
	private TextView txtLongitudNet;
	
	private TextView lblfechainicio;
	private TextView lblhorainicio;
	
	
	private TextView txtnombrecliente;
	public String Stringnombrecliente="";
	public String auxiliarnumerocliente="";

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityvisita);
		Bundle bundler=getIntent().getExtras();
		
		
		
		Log.e("ERROR", "seguimiento sigueinte 1");
		
		Stringcodvendedor=bundler.getString("Stringcodvendedor");
		
		txtLatitudGPS = (TextView)findViewById(R.id.txtLatitudGPS);
		txtLongitudGPS = (TextView)findViewById(R.id.txtLongitudGPS);
		txtLatitudNet = (TextView)findViewById(R.id.txtLatitudNet);
		txtLongitudNet = (TextView)findViewById(R.id.txtLongitudNet);			
		txtnombrecliente=(TextView)  findViewById(R.id.textcliente);
		lblultimavisita=(TextView)  findViewById(R.id.lblultimavisita);		
		lblultimavisita=(TextView)  findViewById(R.id.lblultimavisita);
		txtNumcliente = (EditText) findViewById(R.id.txtNumcliente);
		btnaceptar  = (ImageButton) findViewById(R.id.btncomprobarcliente);			
		lblfechainicio = (TextView)findViewById(R.id.lblfechainicio);
		lblhorainicio = (TextView)findViewById(R.id.lblhorainicio);		
		lblclientevisitaavierta= (TextView)findViewById(R.id.lblclientevisitaavierta);
		btnaceptar.setOnClickListener(new View.OnClickListener() {
			@Override
 			public void onClick(View arg0) {
				txtNumcliente.getText().toString();
				if (!txtNumcliente.getText().toString().equals("")) {
 									 
 					auxiliarnumerocliente=txtNumcliente.getText().toString();
 					 UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
        					"DBUsuarios", null, 1);
        					final SQLiteDatabase db = sql.getWritableDatabase();				
        					if (db != null) 
        					{
        					    String consulta="SELECT  C.nombre ,C.numcliente,C.codcliente, case when C.Latitud  is null then 0 else C.Latitud  end as Latitud   , case when C.Longitud  is null then 0 else C.Longitud  end as Longitud  from clientes C where C.numcliente ="+auxiliarnumerocliente.trim();
        					//+ auxiliarnumerocliente.trim();					    
        						try {
        									Cursor c = db.rawQuery(consulta, null);					
        									if (c.moveToFirst()) 
        										{            											
        											Stringnombrecliente = c.getString(0);
        											Stringcodcliente= c.getString(2);
        											Stringlatitudcliente= c.getString(3);
        											Stringlongitudcliente= c.getString(4);           											
        											
        											if (Stringlatitudcliente=="0"|| Stringlatitudcliente.equals("0")||Stringlatitudcliente.equals(" ")){
        													Toast.makeText(getApplicationContext(), "El Cliente no fue Mapeado",
        		    		        						Toast.LENGTH_LONG).show();	
        											}
        											
        										}else{
        											Toast.makeText(getApplicationContext(), "Cliente no encontrado",
        					        				Toast.LENGTH_LONG).show();		   											       										        										
        											Stringnombrecliente = "";
        										//	Stringlatitudcliente="0";
        										//	Stringlongitudcliente="0";
        											Stringcodcliente="";
        											Stringnombrecliente = "";
        											
        											//PREGUNTAR PARA ACTUALIZAR
        											
        										}
        						}catch(Exception e){
        							Toast.makeText(getApplicationContext(), "error en la consulta 1",
    		        						Toast.LENGTH_SHORT).show();		
        						}
        					}          	    	      	    	
        					txtnombrecliente.setText(Stringnombrecliente);       					 				 
 			
 				 }				 				
 			}
 			});


		Button btnterminarvisita = (Button) findViewById(R.id.btnterminarvisita);
		
		btnterminarvisita.setOnClickListener(new View.OnClickListener() {
			
			@Override
	 		public void onClick(View arg0) {


				txtNumcliente.getText().toString();
				if (!txtNumcliente.getText().toString().equals("")) {
					 	if(idvisitaactual>0){
					 		terminarvisita();						 		
					 	}else{
					 		Toast.makeText(getApplicationContext(),
									"No hay Visitas Activas", Toast.LENGTH_LONG).show();
					 	}							
					}
			
							
				}
		    });


		Button btncancelarvisita = (Button) findViewById(R.id.btncancelarvisita);
		btncancelarvisita.setOnClickListener(new View.OnClickListener() {
				@Override
		 		public void onClick(View arg0) { 				 		
							if ( idvisitaactual>0 ) {   							 	
							 	preguntarcancelarvisita();
							}
						
					}
			    });


		Button bntreporte = (Button) findViewById(R.id.bntreporte);
		
		bntreporte.setOnClickListener(new View.OnClickListener() {
				@Override
		 		public void onClick(View arg0) { 
					
					llamarreporte();
					
					
				}}
		 );
		
		
		
		
	    btniniciarvisita=(Button)  findViewById(R.id.btniniciarvisita);
	    btniniciarvisita.setOnClickListener(new View.OnClickListener() {
		@Override
 		public void onClick(View arg0) { 				 		
				
			
			if (Stringlatitudcliente.equals("0")||Stringlatitudcliente.equals(" ")){
				Toast.makeText(getApplicationContext(), "El Cliente no fue Mapeado",
				Toast.LENGTH_LONG).show();	
		}else{
			
			try{
			LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
				boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				if (statusOfGPS){
					txtNumcliente.getText().toString();
					if (!txtNumcliente.getText().toString().equals("")) {
					 	abrirvisita();
					}else{
						Toast.makeText(getApplicationContext(),
								"Ingrese el Código del Cliente", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(),
							"Primero Active el GPS", Toast.LENGTH_LONG).show();				
				}
		
		
		 }catch(Exception e ){
				Toast.makeText(getApplicationContext(),
						"Error-Favor vuelva a intentarlo, o vuelva a mapear al cliente ", Toast.LENGTH_LONG)
						.show();	  
			}	
			
		
		}
			
		
		
		}
	    });
	    
	
	    Ubicarme(); 
	    ultimavisita();
	    averiguarvisitaabierta();	

	}
	
	public void llamarreporte(){
		
int mYear;
int mMonth;
int mDay;

txtDatefecha="";
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		 
		DatePickerDialog dpd = new DatePickerDialog(this,
		        new DatePickerDialog.OnDateSetListener() {
		 
		            @Override
		            public void onDateSet(DatePicker view, int year,
		                    int monthOfYear, int dayOfMonth) {
		            	
		            	     txtDatefecha=  year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		            	    
		            	     
		            	   //  consultavisitas="Select V._id,V.codcliente,V.horainicio,V.horafin , V.diainicio  ,transferido as numcliente,V._id as nombre  from Visitas AS V   LEFT JOIN  Clientes as C on V.codcliente=C.codcliente where V.diainicio='"+ txtDatefecha.trim() +"'";
		            	     consultavisitas="Select V._id,V.codcliente,V.horainicio,V.horafin , V.diainicio  ,C.numcliente,C.nombre  from Visitas AS V   LEFT JOIN  Clientes as C on V.codcliente=C.codcliente where V.diainicio='"+ txtDatefecha.trim() +"'";
		            	     
		            	     llamarmetodo();
		            	      

		            }
		        }, mYear, mMonth, mDay);
		dpd.show();
		
		//Intent c = new Intent(this, ActivityCargarUtilidades.class);		
		//startActivity(c);
		
		
		
	}
	public void llamarmetodo(){
		     Intent c = new Intent(this, ActivityListadoVisitas.class);
		 //  consultavisitas="Select _id,codcliente,horainicio,horafin,diainicio from Visitas";
		  //  c.putExtra("consultavisitas", consultavisitas);
			c.putExtra("txtDatefecha", txtDatefecha);
			c.putExtra("stringconsulta", consultavisitas);
			
			startActivity(c);
			
			
	}
	
	public void preguntarterminarvisita(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 	builder.setMessage("Desea Terminar la Vista de : "+ txtnombrecliente.getText().toString().trim() +"?"
			  			)
		 	       .setCancelable(false)
		 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		 	           public void onClick(DialogInterface dialog, int id) {		 	        	   	
		 	        	   terminarvisita();		 	        	 
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
	
	public void preguntarcancelarvisita(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 	builder.setMessage("Desea Cancelar la Vista de : "+ txtnombrecliente.getText().toString().trim() +"?"
			  			)
		 	       .setCancelable(false)
		 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		 	           public void onClick(DialogInterface dialog, int id) {		 	        	   	
		 	        	   cancelarvisita();		 	        	 
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
	
	public void terminarvisita(){
				    AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 	builder.setMessage("Terminar Visita de : "+ txtnombrecliente.getText().toString().trim() +"?"
					  			)
				 	       .setCancelable(false)
				 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				 	           public void onClick(DialogInterface dialog, int id) {
				             	  tipoutilizado=1;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
				             	       	 terminarvisitaengeneral();
				 	        	  
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
	public void cancelarvisita(){
		
		try{   
     	   UtilidadesSQL sql= new UtilidadesSQL(getApplicationContext(),
						"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();			
						db.execSQL("update Visitas  set  estado=3,horafin='Cancelado' where  _id="+ String.valueOf(idvisitaactual).toString().trim() );
						Toast.makeText(getApplicationContext(),
										"Visita Cancelada", Toast.LENGTH_LONG).show();

						
						btniniciarvisita.setEnabled(true);
						btnaceptar.setEnabled(true);											
						btniniciarvisita.setBackgroundColor(Color.rgb(0,128,0));
						btnaceptar.setBackgroundColor(Color.rgb(0,128,0));
						txtNumcliente.setEnabled(true);
						lblfechainicio.setText("");
						lblhorainicio.setText("");
						txtNumcliente.setText("");
						txtnombrecliente.setText("");
						idvisitaactual=0;
						Stringcodcliente="";
					
						
						
						    ultimavisita();
						    averiguarvisitaabierta();
						
						
        }catch(Exception e ){
			Toast.makeText(getApplicationContext(),
					"Problemas para Cancelar la Visita", Toast.LENGTH_LONG)
					.show();	  
		}	
		
		
		
	}
	 public void abrirvisita(){
		 try{
		 // hay ubicacion por gps?		 		 
			  if ( ( txtLatitudGPS.getText().toString() != null) && (!txtLatitudGPS.getText().toString().equals("")) ) {
		 // 	se supone que cliente ya se verifico
			  // guardar la visita
				    AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 	builder.setMessage("Iniciar Visita de : "+ txtnombrecliente.getText().toString().trim() +"?"
					  			)
				 	       .setCancelable(false)
				 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				 	           public void onClick(DialogInterface dialog, int id) {
				 	        	  //guardarvisita_uno();
				 	        	 

				             	  latitudutilizada=txtLatitudGPS.getText().toString();
				             	  longitudutilizada=txtLongitudGPS.getText().toString();
				             	  tipoutilizado=1;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
				             	
				 	        	   // llamar al que guarda en general...
				             	  guardarvisitaengeneral();
				             	 
				 	        	   
				 	           }
				 	       })
				 	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				 	           public void onClick(DialogInterface dialog, int id) {
				 	                dialog.cancel();
				 	           }
				 	       });
				 	AlertDialog alert = builder.create();    	
				 	alert.show();	
				  
					
				  
				  
				  
			  }else {
				  lanzar_espera();
				  
			  }
		 
			  
			  
			  
	 }catch(Exception e ){
			Toast.makeText(getApplicationContext(),
					"Error-Favor vuelva a intentarlo, o vuelva a mapear al cliente ", Toast.LENGTH_LONG)
					.show();	  
		}	
			  
			  
			  
			  
			  
	 }
	 
	 
		private Handler puente = new Handler() {
			  @Override
			  public void handleMessage(Message msg) {
			   progressDialog.setProgress((Integer)msg.obj);
			   if ((Integer)msg.obj>=99) 
				{
				  			
				         progressDialog.dismiss();
				         //guardar_fin_esperando();
				        
			    }else{
			    	
			    	 
			    }
			  
			  
			  }
			 };
	 
			 class mihiloesperar extends Thread{
					public void run(){
						  try{
				            	 //  Thread.sleep (1000); 					            	   
				              
							  Message msg = new Message();
				   			  msg.obj = 1;
							  puente.sendMessage(msg);
							  
							  for(int a=1;a<=100;a++){ // intenta durante  60 segundos
								  
								  try{
									 // Thread.sleep (1000);
								  } catch (Exception e) {
									  
								  }
								  
								  int contador = a;			
						   	      //Message msg = new Message();
								  msg.obj = a;
								  puente.sendMessage(msg);								  
						       }
							  msg.obj = 100;
							  puente.sendMessage(msg);
							 
						  
						  
						  
						  }catch (Exception e) {
						      Message msg = new Message();
							  msg.obj = 100;
							  puente.sendMessage(msg);
							
							  
				          }  
			        
						  
				      //   
	          
					}
	}
			 
	
		 

public void lanzar_espera(){
	  
	 progressDialog = new ProgressDialog(this);
	 
	 
	 progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 progressDialog.setMessage("Por Favor, Espere..  (maximo 1 minuto)");

     //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
     //progressDialog.setMessage("Estableciendo su Ubicación...");
     progressDialog.setMax(60);
     progressDialog.setProgress(60);
     progressDialog.setCancelable(false);
     progressDialog.setTitle("Estableciendo su Ubicación");
     progressDialog.show();
       	
     //mihiloesperar hiloesperar = new mihiloesperar();
     //hiloesperar.start();
     
     MiTareaAsincronaDialog  tarea2 = new MiTareaAsincronaDialog();
     tarea2.execute();
  }


public void lanzar_espera_terminar(){
	  
	progressDialogTerminar = new ProgressDialog(this);
	progressDialogTerminar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressDialogTerminar.setMessage("Estableciendo su Ubicación...");

    //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    //progressDialog.setMessage("Estableciendo su Ubicación...");
	progressDialogTerminar.setMax(100);
	progressDialogTerminar.setProgress(100);
	progressDialogTerminar.setCancelable(true);
	progressDialogTerminar.show();
      	
    //mihiloesperar hiloesperar = new mihiloesperar();
    //hiloesperar.start();
    
    MiTareaAsincronaDialogTerminar  tarea3 = new MiTareaAsincronaDialogTerminar();
    tarea3.execute();
 }

			 
	public void Ubicarme(){	
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
		DemoLocationListener gpsListener = new DemoLocationListener(txtLatitudGPS, txtLongitudGPS);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);		
		DemoLocationListener netListener = new DemoLocationListener(txtLatitudNet, txtLongitudNet);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, netListener);		
	}
	public void averiguarvisitaabierta(){

		try {
		UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null, 1);
				final SQLiteDatabase db = sql.getWritableDatabase();				
				if (db != null) 
				{
					try {
						//
						
								String consulta="select V.diainicio,V.horainicio,C.numcliente,C.nombre ,V._id,C.codcliente, C.latitud, C.longitud from Visitas AS V LEFT JOIN  Clientes C on V.codcliente=C.codcliente where V.estado=1 order by V._id desc limit 1";					    
								//String consulta="SELECT DISTINCT(P.idpedido)as idpedido ,P.numcliente,P.nombre,P.enviado,P.tipoventa,P.codcliente,P.codvendedor,P.desvendedor,P.cerrado,C.codsitua  ,P.horacierre,P.horaenvio, P.numeropresupuesto,P.numventa,P.situacion, P.faltantes from Pedidos  AS P LEFT JOIN  Clientes C on P.codcliente=C.codcliente  order by P.enviado asc,P.idpedido desc";
				    
								Cursor c = db.rawQuery(consulta, null);					
								if (c.moveToFirst()) 
									{            											
											//si hay abierto
											btniniciarvisita.setBackgroundColor(Color.rgb(12, 183, 242));										
											btniniciarvisita.setEnabled(false);
											btnaceptar.setEnabled(false);
											btnaceptar.setBackgroundColor(Color.rgb(12, 183, 242));
											
											txtNumcliente.setEnabled(false);
											lblfechainicio.setText(c.getString(0));
											lblhorainicio.setText(c.getString(1));
											txtNumcliente.setText(c.getString(2));
											txtnombrecliente.setText(c.getString(3));
											idvisitaactual=c.getInt(4);
											Stringcodcliente=c.getString(5);
											Stringlatitudcliente=c.getString(6);
											Stringlongitudcliente=c.getString(7);
											lblclientevisitaavierta.setText(String.valueOf(c.getInt(3)));
										
											Toast.makeText(getApplicationContext(), "La Visita esta Iniciada",
			        						Toast.LENGTH_SHORT).show();			        												
									}else{
										//si no hay abierto
											btniniciarvisita.setEnabled(true);
											btnaceptar.setEnabled(true);											
											btniniciarvisita.setBackgroundColor(Color.rgb(0,128,0));
											btnaceptar.setBackgroundColor(Color.rgb(0,128,0));
											
											txtNumcliente.setEnabled(true);
											lblfechainicio.setText("");
											lblhorainicio.setText("");
											txtNumcliente.setText("");
											txtnombrecliente.setText("");
											idvisitaactual=0;
											Stringcodcliente="";										
										 Toast.makeText(getApplicationContext(), "Sin Visitas Iniciadas",
					        						Toast.LENGTH_SHORT).show();
					        					
									}
										
					}catch(Exception e){
						

						Toast.makeText(getApplicationContext(), "error en la consulta 34",
								Toast.LENGTH_SHORT).show();
					}
		
			}	
		
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "error en la consulta 33",
					Toast.LENGTH_SHORT).show();
		
			
		}
	}
	public void ultimavisita(){
		
		UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null, 1);
				final SQLiteDatabase db = sql.getWritableDatabase();				
				if (db != null) 
				{
				    String consulta="select C.numcliente,C.nombre from Visitas AS V LEFT JOIN  Clientes C on V.codcliente=C.codcliente where V.estado=2 order by V._id desc limit 1";					    
				
				    
				    try {
								Cursor c = db.rawQuery(consulta, null);					
								if (c.moveToFirst()) 
									{            											
									   lblultimavisita.setText("Visita Anterior:"+ c.getString(0).toString().trim()+"-"+ c.getString(1).toString().trim());
									   
									}else{
										lblultimavisita.setText("Sin Visitas");
									}
										
					}catch(Exception e){
						Toast.makeText(getApplicationContext(), "error en la consulta 3",
        						Toast.LENGTH_SHORT).show();{
        						
        							lblultimavisita.setText("Error en Visitas 2");
						
					}
					}
		
			}	
		
		
	}
	
	
	private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {
		 
        @Override
        protected Boolean doInBackground(Void... params) {
 
            for(int i=1; i<=60; i++) {
            tareaLarga();
 
            // aqui cortar si es que ya se encontro...
            if ( ( txtLatitudGPS.getText().toString() != null) && (!txtLatitudGPS.getText().toString().equals("")) ) {            
            	  latitudutilizada=txtLatitudGPS.getText().toString();
            	  longitudutilizada=txtLongitudGPS.getText().toString();
            	  tipoutilizado=1;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
                  i=60;
            }
             publishProgress(i*1);
 
            if(isCancelled())
                break;
        }
        // aqui preguntar, si sigue sin datos, preguntar por antena, caso contrario buscar y asignar el ultimo
            if ( ( txtLatitudGPS.getText().toString() != null) && (!txtLatitudGPS.getText().toString().equals("")) ) {
            		latitudutilizada=txtLatitudGPS.getText().toString();
          	  		longitudutilizada=txtLongitudGPS.getText().toString();
          	  		tipoutilizado=1;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
            }else {
            	if ( ( txtLatitudNet.getText().toString() != null) && (!txtLatitudNet.getText().toString().equals("")) ) {
                	  latitudutilizada=txtLatitudNet.getText().toString();
                	  longitudutilizada=txtLongitudNet.getText().toString();
                	  tipoutilizado=2;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
                  }else { // NO HAY CASO, BUSCAR la ubicacion del cliente guardado
                	  
                	  		
                	  if ( ( txtNumcliente.getText().toString() != null) && (!txtNumcliente.getText().toString().equals("")) ) {
                	    UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
         						"DBUsuarios", null, 1);
         						final SQLiteDatabase db = sql.getWritableDatabase();				
         						if (db != null) 
         						{
         						String consulta="";
         						consulta="Select latitud,Longitud  from Clientes where numcliente="+txtNumcliente.getText().toString();
         						    
         						  try{
         						      Cursor c = db.rawQuery(consulta, null);					
         										if (c.moveToFirst()) {									
         											
         									do {
         				       				
         									if ( ( c.getString(0).toString() != null) && (!c.getString(0).toString().equals("")) ) {
         									// 
         										
         									//	Toast.makeText(ActivityVisitas.this, "Localizado!"+c.getString(0).toString(),
             								//	          Toast.LENGTH_LONG).show();
             											
         										
         										latitudutilizada=c.getString(0);
           				                	  	longitudutilizada=c.getString(1);
           				                	  	tipoutilizado=3;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido
         									
         									}else{
         									//	no hay ubicacion disponible de ningun tipo
         									
         									//	Toast.makeText(ActivityVisitas.this, "El Cliente no se encuentra mapeado!",
         									//			Toast.LENGTH_LONG).show();
           											
         										
         										latitudutilizada="";
             				                	 longitudutilizada="";
             				                	 tipoutilizado=0;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido            				       				 
         									
         										
         									}
         				       				  	 
         				       				     
         									} while(c.moveToNext());									
         									
         										}else{        										         											
         											// no hay ubicacion disponible de ningun tipo
               				       				  
         											//Toast.makeText(ActivityVisitas.this, "El Cliente no se encuentra mapeado!",
         									         // Toast.LENGTH_LONG).show();
         											
         										  latitudutilizada="";
               				                	  longitudutilizada="";
               				                	  tipoutilizado=0;  //1 es por  gps , 2 es por antena, 3 es el ultimo conocido               				       				  		
         										}
         						  }catch(Exception e){
         						  }
         						}
                	  
                  }
                	  
                  	
                  
                  }            	
            }            
            return true;
        }
 
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
 
            progressDialog.setProgress(progreso);
        }
 
        @Override
        protected void onPreExecute() {
 
        	progressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MiTareaAsincronaDialog.this.cancel(true);
            }
        });
 
        	progressDialog.setProgress(0);
        	progressDialog.show();
        }
 
        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
            	progressDialog.dismiss();
                Toast.makeText(ActivityVisitas.this, "Ubicación Establecida!",
                Toast.LENGTH_SHORT).show();
                guardarvisitaengeneral();
                
            }
        }
 
        @Override
        protected void onCancelled() {
            Toast.makeText(ActivityVisitas.this, "Tarea cancelada!",
                Toast.LENGTH_SHORT).show();
        }
    }
	
	private void tareaLarga()
	{
	    try {
	        
	    	// aqui espera
	    	Thread.sleep(1000); // tardará 1 minuto en total
	        
	        
	        
	        
	    } catch(InterruptedException e) {}
	}	

public void terminarvisitaengeneral(){
	

	if ( Integer.valueOf(Stringcodcliente)>0 ){ 
		
			fechadehoy=hoy();
			horaactual=ahora();
	
			
			UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
			final SQLiteDatabase db3 = sql3.getWritableDatabase();
			try
			{
			String Stringsql ="Update Visitas set estado=2, latitudfin=latitudinicio, longitudfin=longitudinicio,diafin='"+ fechadehoy.toString().trim() +"',horafin='"+ horaactual.toString().trim() +"'  where _id="+String.valueOf(idvisitaactual).toString().trim();
			db3.execSQL(Stringsql);		
							
			
			Toast.makeText(getApplicationContext(), "Visita Terminada Correctamente",
					Toast.LENGTH_LONG).show();
			
			btniniciarvisita.setBackgroundColor(Color.rgb(0, 128, 0));
			btnaceptar.setBackgroundColor(Color.rgb(12, 183, 242));
			
			
			
			btniniciarvisita.setEnabled(true);
			btnaceptar.setEnabled(true);											
			btniniciarvisita.setBackgroundColor(Color.rgb(0,128,0));
			btnaceptar.setBackgroundColor(Color.rgb(0,128,0));
			txtNumcliente.setEnabled(true);
			lblfechainicio.setText("");
			lblhorainicio.setText("");
			txtNumcliente.setText("");
			txtnombrecliente.setText("");
			idvisitaactual=0;
			Stringcodcliente="";
			
			ultimavisita();
		    averiguarvisitaabierta();
		    /*
			 * limpiar todos los campos de textos y variables
			 * 
			 */
			
			
		}catch (Exception e){
			Toast.makeText(getApplicationContext(),
	   				"No pude actualizar como Terminado", Toast.LENGTH_SHORT).show();			   	    
		}
	}
}
public void guardarvisitaengeneral(){
	
	
	
	
	  String distancia="";	
	  Float lau2 =Float.parseFloat(latitudutilizada.toString());
	  Float lou2=Float.parseFloat(longitudutilizada.toString());		
	  Float lac2=Float.parseFloat(Stringlatitudcliente.toString());
	  Float loc2=Float.parseFloat(Stringlongitudcliente.toString());		  
	
	  
	  Float a =distFrom(lau2 ,lou2,lac2,loc2)*1000;
	  
	  distancia = String.valueOf(a);
	  Toast.makeText(getApplicationContext(), "Distancia del punto:"+distancia.toString()+ "mts.",
				Toast.LENGTH_LONG).show();
	
	
int habilitar =0;
if ( Integer.valueOf(tipoutilizado)>0 ){ 

	if ( Integer.valueOf(Stringcodcliente)>0 ){ 
	
	if ( (latitudutilizada.toString() != null) && (!latitudutilizada.toString().equals("")) ) {
	
	  
	  
	  // aqui preguntar si esta cerca de la ferreteria
	  
	  if (tipoutilizado==1){
		  
		   distancia="";	
		  Float lau =Float.parseFloat(latitudutilizada.toString());
		  Float lou=Float.parseFloat(longitudutilizada.toString());		
		  Float lac=Float.parseFloat(Stringlatitudcliente.toString());
		  Float loc=Float.parseFloat(Stringlongitudcliente.toString());		  
		  a =distFrom(lau ,lou,lac, loc)*1000;
		 
		  distancia = String.valueOf(a); // paso a metros
		
		  
		  
		  /// uso gps, entonces ,menos de 100 metros, paso de millas a metros
		  // medir distancia 
		  if (a <  150) { // menor a 50 mts.			  
		  // esta a menos de 100 metros, habilitar		  
			  habilitar=1;
		  } else{
			  habilitar=0;
		  }
		  //distFrom(float lat1, float lng1, float lat2, float lng2) {
	  }else
	  {
		  if (tipoutilizado==2){
			  // usó antena, entonces ampliar a 500 mts.
			  

			  distancia="";	
			  Float lau =Float.parseFloat(latitudutilizada.toString());
			  Float lou=Float.parseFloat(longitudutilizada.toString());		
			  Float lac=Float.parseFloat(Stringlatitudcliente.toString());
			  Float loc=Float.parseFloat(Stringlongitudcliente.toString());		  
			   a =distFrom(lau ,lou,lac, loc)*1000; // paso a metros
			  distancia = String.valueOf(a);
			  
			  if(a <  800) {
				  
				  // esta a menos de 500 metros, habilitar
					  
					  habilitar=1;
				  } else{
					  habilitar=0;
				  }
			  
		  }else{
			  // no uso antena ni gps, por lo tanto no preguntar
			  habilitar=1;
			  
		  }
		  
	  }
	  
	  
	  if ( ( Stringlatitudcliente.toString() != null) && (!Stringlatitudcliente.toString().equals("")) ) {
			 
		  // el cliente no se encuentra mapeado
		//  Stringlatitudcliente="0";
		 // Stringlongitudcliente="0";
		  
	  }else{
		  
		  habilitar=0;
	  }
	  
	  
	  
	  if (habilitar>0){
	  
	Random r = new Random();
	int sortear = r.nextInt(10000);
	Stringrandon= String.valueOf(sortear);
	sortear = r.nextInt(9000);
	Stringrandon=Stringrandon+String.valueOf(sortear);	
	fechadehoy=hoy();
	horaactual=ahora();
	

	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null, 1);
		final SQLiteDatabase db = sql.getWritableDatabase();	
	    if (db != null) {			
	    try {
	    		
	    	ContentValues nuevoRegistro = new ContentValues();
			nuevoRegistro.put("codvendedor",Integer.valueOf(Stringcodvendedor));
			nuevoRegistro.put("random", Stringrandon.toString());
			nuevoRegistro.put("codcliente",Integer.valueOf(Stringcodcliente));			
			nuevoRegistro.put("diainicio", fechadehoy.toString());
			nuevoRegistro.put("horainicio", horaactual.toString());		
			nuevoRegistro.put("diafin", " ");
			nuevoRegistro.put("horafin", " ");			
			nuevoRegistro.put("latitudinicio", latitudutilizada.toString().trim());
			nuevoRegistro.put("longitudinicio",longitudutilizada.toString().trim());
			nuevoRegistro.put("latitudfin", "0");
			nuevoRegistro.put("longitudfin", "0");			
			nuevoRegistro.put("latitudcliente", Stringlatitudcliente.toString().trim());
			nuevoRegistro.put("longitudcliente", Stringlongitudcliente.toString().trim());
			nuevoRegistro.put("tipo", tipoutilizado);
			nuevoRegistro.put("estado", 1);
			nuevoRegistro.put("transferido", 0);
			// 1 es abierto
			
			
			
				db.insert("Visitas", null, nuevoRegistro);
				Toast.makeText(getApplicationContext(), "Visita Iniciada Correctamente",
						Toast.LENGTH_LONG).show();
				
				btniniciarvisita.setBackgroundColor(Color.rgb(12, 183, 242));
				ultimavisita();
			    averiguarvisitaabierta();
				
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"No se puede Iniciar la visita 4", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
	
		}
		
	    
	  }else{
		    // hasta aqui preguntar si esta cerca de la ferreteria
		
		  
				  Toast.makeText(getApplicationContext(),
					"Usted, no se encuentra en un punto Cercano, o el Cliente no fué Mapeado:" , Toast.LENGTH_LONG).show();
		  
	  }

	    

	}else{
		Toast.makeText(getApplicationContext(),
				"No se puede Iniciar la visita 3 ", Toast.LENGTH_LONG).show();
	}

		
	}	else{
		Toast.makeText(getApplicationContext(),
				"No se puede Iniciar la visita 2", Toast.LENGTH_LONG).show();
	}
	
	}	else{
			Toast.makeText(getApplicationContext(),
					"No se puede Iniciar la visita 1", Toast.LENGTH_LONG).show();
		}
}


public String hoy(){
	String hoy="";
	final Calendar calendario2 = Calendar.getInstance();
    year = calendario2.get(Calendar.YEAR);
    month = calendario2.get(Calendar.MONTH)+1;
    day =calendario2.get(Calendar.DAY_OF_MONTH);  
    hoy= String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
    return hoy;
}


public String ahora(){
	String ahora="";
	final Calendar calendario2 = Calendar.getInstance();
    HORA = calendario2.get(Calendar.HOUR_OF_DAY );
    MINUTO = calendario2.get(Calendar.MINUTE);
    SEGUNDO =calendario2.get(Calendar.SECOND); 
    
    
    
    ahora= String.valueOf(HORA)+":"+String.valueOf(MINUTO)+":"+String.valueOf(SEGUNDO);
    return ahora;
}







private class MiTareaAsincronaDialogTerminar extends AsyncTask<Void, Integer, Boolean> {
	 
    @Override
    protected Boolean doInBackground(Void... params) {

        for(int i=1; i<=60; i++) {
        tareaLarga();

        // aqui cortar si es que ya se encontro...
        if ( ( txtLatitudGPS.getText().toString() != null) && (!txtLatitudGPS.getText().toString().equals("")) ) {            
        	  latitudutilizada=txtLatitudGPS.getText().toString();
        	  longitudutilizada=txtLongitudGPS.getText().toString();
        
        i=60;
        }
         publishProgress(i*1);

        if(isCancelled())
            break;
    }
    // aqui preguntar, si sigue sin datos, preguntar por antena, caso contrario buscar y asignar el ultimo
        if ( ( txtLatitudGPS.getText().toString() != null) && (!txtLatitudGPS.getText().toString().equals("")) ) {
        		latitudutilizada=txtLatitudGPS.getText().toString();
      	  		longitudutilizada=txtLongitudGPS.getText().toString();
      	  		
        }else {
        	if ( ( txtLatitudNet.getText().toString() != null) && (!txtLatitudNet.getText().toString().equals("")) ) {
            	  latitudutilizada=txtLatitudNet.getText().toString();
            	  longitudutilizada=txtLongitudNet.getText().toString();
            	  
              }else { // NO HAY CASO, BUSCAR EL ULTIMO CONOCIDO
            	  
            	  
              	
            		 UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
     						"DBUsuarios", null, 1);
     						final SQLiteDatabase db = sql.getWritableDatabase();				
     						if (db != null) 
     						{
     							String consulta="";
     						consulta="Select latitud,Longitud  from Ubicacion  order by _id desc  limit 1";
     						    
     						  try{
     						      Cursor c = db.rawQuery(consulta, null);					
     										if (c.moveToFirst()) {									
     											
     									do {
     				       				  	 
     				       				  	 
     				       				  latitudutilizada=c.getString(0);
     				                	  longitudutilizada=c.getString(1);
     				                	 
     				                	 
     				       				  	 
     				       				     
     									} while(c.moveToNext());									
     										}else{
     										
     											
     											// no hay ubicacion disponible de ningun tipo
           				       				  latitudutilizada="";
           				                	  longitudutilizada="";
           				                	
           				       				  	 
           				       				  	 
     											
     											
     										}
     						  }catch(Exception e){
     						  }
     						}
            	  
            	  
            	  
              	
              
              }            	
        }            
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progreso = values[0].intValue();

        progressDialogTerminar.setProgress(progreso);
    }

    @Override
    protected void onPreExecute() {

    	progressDialogTerminar.setOnCancelListener(new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            MiTareaAsincronaDialogTerminar.this.cancel(true);
        }
    });

    	progressDialogTerminar.setProgress(0);
    	progressDialogTerminar.show();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result)
        {
        	progressDialogTerminar.dismiss();
            Toast.makeText(ActivityVisitas.this, "Ubicación Establecida!",
            Toast.LENGTH_SHORT).show();
            terminarvisitaengeneral();
            
        }
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(ActivityVisitas.this, "Tarea cancelada!",
            Toast.LENGTH_SHORT).show();
    }






}

public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
    double earthRadius = 6371; //kilometers
    double dLat = Math.toRadians(lat2-lat1);
    double dLng = Math.toRadians(lng2-lng1);
    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLng/2) * Math.sin(dLng/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    float dist = (float) (earthRadius * c);

    return dist;
    }

}