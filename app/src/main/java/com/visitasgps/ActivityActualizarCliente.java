package com.visitasgps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.visitasgps.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ActivityActualizarCliente extends Activity {
	
	private final static String ETIQUETA_ERROR = "ERROR";
	private static final String namespace = "http://mail.amanecer.com.py/lservicios";
	private static final String url = "http://mail.amanecer.com.py/lservicios.php";
	private static final String MetodoFiltrarClientesObras = "verificarcliente3Obras";
			private static final String accionSoapFiltrarClientesObras = "http://mail.amanecer.com.py/lservicios.php/verificarcliente3Obras";
	public int sincronizocorrectamente =0;	
	public CheckBox chopcion;
	private EditText txtNumclienteRuta;
	private TextView txtnombrecliente;
	private TextView txtruccliente;
	private TextView txtdireccion;
	private Button btnaceptar;
	private Button btnSalir;
	public 	String Stringnumcliente="";	 
	public String Stringruccliente="";
	public 	String Stringnombre="";
	public String Stringdireccion="";
	public String codvendedor="";
	private   ProgressDialog progressDialog ;
	private ImageButton btncomprobarcliente;
	public int versiondb=1;
	public String Stringcodcliente ="";
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityactualizarcliente);
        
        Bundle bundler=getIntent().getExtras();

		assert bundler != null;
		codvendedor=bundler.getString("Stringcodvendedor");
        
        
        chopcion =(CheckBox)findViewById(R.id.checkBox1);
        
        txtNumclienteRuta = (EditText) findViewById(R.id.txtNumclienteRuta);
         txtnombrecliente = (TextView) findViewById(R.id.txtnombrecliente);
         txtruccliente = (TextView) findViewById(R.id.txtruccliente);
         
         txtdireccion = (TextView) findViewById(R.id.txtdireccion);
         
         ImageButton  btncomprobarcliente =(ImageButton) findViewById(R.id.btncomprobarcliente);
         
         Button  btnaceptar  =(Button) findViewById(R.id.btnaceptar);
         Button  btnSalir  =(Button) findViewById(R.id.btnSalir);
         
         
         btnSalir .setOnClickListener(new View.OnClickListener() {
     		public void onClick(View v) {      
     			finish();
     		}
        });
         
         
         btnaceptar.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View arg0) {
				txtNumclienteRuta.getText().toString();
				if (!txtNumclienteRuta.getText().toString().equals("")) {
 					 preguntar_sincronizar(); 				 
 			 }	else {	
 				 // preguntar si esta chequeado el checbox
 				if (chopcion.isChecked() ){
 					preguntar_sincronizar(); 		
 				}	
 			 }	 				
 			}
 			});
        
	
         
         btncomprobarcliente.setOnClickListener(new View.OnClickListener() {
            	String auxiliarnumerocliente ="";               	
           	@Override
            	public void onClick(View v) {      
            		try{ 
           			auxiliarnumerocliente=txtNumclienteRuta.getText().toString();      	       
           		}catch(Exception e){
            			System.out.println(e);
           			
           		}
           		 if ( (auxiliarnumerocliente!= null) && (!auxiliarnumerocliente.equals("")) ) {      	             	    	               	    
           		   	    	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
         					"DBUsuarios", null, 1);
         					final SQLiteDatabase db = sql.getWritableDatabase();				
         					if (db != null) 
         					{
         					   String consulta="SELECT codcliente,numcliente,nombre,nif ,direccion from clientes where numcliente = "+ auxiliarnumerocliente;					    
         					   
         					    try {
         									Cursor c = db.rawQuery(consulta, null);					
         									if (c.moveToFirst()) 
         										{
         											Stringcodcliente = c.getString(0);
         											Stringnumcliente  = c.getString(1);
         											Stringnombre = c.getString(2); 
         											Stringruccliente= c.getString(3); 
         											Stringdireccion= c.getString(4);
         										}else{
         											Toast.makeText(getApplicationContext(), "Cliente no encontrado",
         					        						Toast.LENGTH_LONG).show();		   											
         										
         											Stringcodcliente = "";
         											Stringnumcliente  = "";
         											Stringnombre = "";  
         											Stringruccliente="";
         											Stringdireccion="";
         										}
         						}catch(Exception e){
         							Toast.makeText(getApplicationContext(), "1error en la consulta",
     		        						Toast.LENGTH_SHORT).show();		
         						}
         					}
           	    	      	    	
         			txtnombrecliente.setText(Stringnombre);     
         			txtruccliente.setText(Stringruccliente);
         			txtdireccion.setText(Stringdireccion);
            	    }else{
            	    	txtnombrecliente.setText("");
            	    	Stringcodcliente ="";  
            	    	Stringnumcliente  = "";
            	    	Stringruccliente="";
            	    }
           		 
           	}
           	}); 
	
}

	public void preguntar_sincronizar(){
		 
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 	builder.setMessage("Desea Actualizar Cliente? "		)
	 	       .setCancelable(false)
	 	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 	           public void onClick(DialogInterface dialog, int id) {
	 	        	  
	 	         //sincronizar(); 
	 	        	  sincronizocorrectamente=0;
	 	        sincronizar_clientes_thread(); 
	 	        
	 	       Log.e(ETIQUETA_ERROR, "obras ATENCION 123" );	
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
	//public void sincronizar(){
		
		
	public void sincronizar_clientes_thread(){
		
		 progressDialog = new ProgressDialog(this);
	     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     progressDialog.setMessage("Sincronizando...");
	     progressDialog.setMax(100);
	     progressDialog.setProgress(10);
	     progressDialog.setCancelable(false);
	     progressDialog.show();
	     mihiloactualizarcliente hiloactualizacliente = new mihiloactualizarcliente();
	     hiloactualizacliente.start();
		
	}
		
		class mihiloactualizarcliente extends Thread{
			public void run(){
		
		
				
				  int bbb;
		          bbb=20;
		          Message msg2 = new Message();
		          msg2.obj = bbb;
		          puente.sendMessage(msg2);
				
		
		try{
		
			
			
			
			 for(int a=20;a<50;a++){
		         int contador = a;				       
		         Message msg = new Message();
		         msg.obj = a;
		         puente.sendMessage(msg);
		         
		        }
			
			 String sql ="";
			 if (chopcion.isChecked() ){
				 sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional ,IFNULL(latitud,'0') AS latitud,IFNULL(longitud,'0') AS longitud ,IFNULL(ubicacionenmapa,'0') AS ubicacionenmapa , IFNULL(direccion,'0') AS direccion FROM clientes where codvendedor='"+ codvendedor.toString() +"';" ;
				}else{
				 sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional ,IFNULL(latitud,'0') AS latitud,IFNULL(longitud,'0') AS longitud ,IFNULL(ubicacionenmapa,'0') AS ubicacionenmapa , IFNULL(direccion,'0') AS direccion  FROM clientes where numcliente= '"+txtNumclienteRuta.getText().toString() +"';" ;					
				}
			 
			 Log.e(ETIQUETA_ERROR, "obras ATENCION 0" );	  
			 //String sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional ,IFNULL(latitud,'0') AS latitud,IFNULL(longitud,'0') AS longitud   FROM clientes WHERE codvendedor=8 ;" ;
			 SoapObject request_Clientes = new SoapObject(namespace, MetodoFiltrarClientesObras);
		   	 request_Clientes.addProperty("Parametro",sql); 		
		   	 SoapSerializationEnvelope envelope_Clientes =  new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		   	 envelope_Clientes.dotNet = true;
		   	 envelope_Clientes.setOutputSoapObject(request_Clientes);         
		   	 HttpTransportSE transporte_Clientes = new HttpTransportSE(url);             
		     transporte_Clientes.call(accionSoapFiltrarClientesObras, envelope_Clientes);             		 		        
		   	 SoapObject resultsRequestSOAP_Clientes = (SoapObject) envelope_Clientes.bodyIn;
		   	 Vector vectordeClientes = (Vector) resultsRequestSOAP_Clientes.getProperty("return");
		   	 int count = vectordeClientes.size(); // contiene la cantidad de registros(objetos bancos) devueltos 
		   	 Log.e(ETIQUETA_ERROR, "obras ATENCION .0.00" );
		   	UtilidadesSQL sql2 = new UtilidadesSQL(getApplicationContext(),
					"DBUsuarios", null, versiondb);
					final SQLiteDatabase db2 = sql2.getWritableDatabase();				
					if (db2 != null) 
					{								
						db2.execSQL("delete from Clientes  where numcliente='"+txtNumclienteRuta.getText().toString()+"';");					
					}
		   	 	   	 
		   	 
	    	 for (int i = 0; i <count; i++)
	    	 {
	    		 Log.e(ETIQUETA_ERROR, "obras ATENCION 0 adentro" );
	    		 SoapObject test = (SoapObject) vectordeClientes.get(i);      
	    		 String Stringnombre = (String) test.getProperty("nombre");  
	    		 String Stringcodcliente = (String) test.getProperty("codcliente");    // se recibe como string                 	
	    		 String Stringnumcliente = (String) test.getProperty("numcliente");    // se recibe como string	    		 	    		
	    		
	    		 String Strinnif = (String) test.getProperty("nif");    // se recibe como string
	    		 String Stringdescuentos = (String) test.getProperty("descuentos");    // se recibe como string
	    		 String Stringcodsitua = (String) test.getProperty("codsitua");    // se recibe como string
	    		 String diasadicional = (String) test.getProperty("diasadicional");    // se recibe como string
	    		
	    		 String latitudcliente=(String) test.getProperty("latitud");    // se recibe como string
	    		 String longitudcliente=(String) test.getProperty("longitud");    // se recibe como string
	    		 String direccion=(String) test.getProperty("direccion");
	    		 
	    		 Log.e(ETIQUETA_ERROR, "obras ATENCION1" );
	    		 String Stringubicacionenmapa=(String) test.getProperty("ubicacionenmapa");    // se recibe como string
	    		 
	    		 Log.e(ETIQUETA_ERROR, "obras ATENCION 1.1"+Stringubicacionenmapa.toString() );
	    		 
	    		 
	    		 int codcliente = Integer.parseInt(Stringcodcliente); // se convierte a integer        	    		
	    		 UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
							"DBUsuarios", null, versiondb);
					final SQLiteDatabase db = sql3.getWritableDatabase();
					if (db != null) {
						ContentValues nuevoRegistro = new ContentValues();
						nuevoRegistro.put("nombre", Stringnombre.trim());
						nuevoRegistro.put("numcliente", Stringnumcliente.trim());
						nuevoRegistro.put("codcliente", codcliente);
						
						nuevoRegistro.put("nif", Strinnif);
						nuevoRegistro.put("descuentos", Stringdescuentos);
						nuevoRegistro.put("codsitua", Stringcodsitua);
						nuevoRegistro.put("diasadicional", diasadicional);						
						nuevoRegistro.put("latitud", latitudcliente);
						nuevoRegistro.put("longitud", longitudcliente);
						
						
						
						nuevoRegistro.put("enviadoubicacion",0);					 
						nuevoRegistro.put("fototransferir",0);
						nuevoRegistro.put("ubicaciontransferir",0);
					 	nuevoRegistro.put("clienteactualizado",0);
					 	nuevoRegistro.put("ubicacionenmapa",Stringubicacionenmapa);
					 	nuevoRegistro.put("direccion",direccion);
						
						
						
						
						
						
						
						try {
							db.insert("Clientes", null, nuevoRegistro);		
							
							 for(int a=50;a<100;a++){
						         int contador = a;				       
						         Message msg = new Message();
						         msg.obj = a;
						         puente.sendMessage(msg);
						         
						        }
					    	 
							 sincronizocorrectamente=10;
							
						} catch (Exception e) {
							sincronizocorrectamente=-5;
							 int aa = -99;
					         Message msg = new Message();
					         msg.obj = aa;
					         puente.sendMessage(msg);
							
							//Toast.makeText(getApplicationContext(),
								//	"Error al Actualizar el cliente", Toast.LENGTH_SHORT).show();
							//e.printStackTrace();
						}
						db.close();
					}
					
					
	    	 }  
	    	 //   Toast.makeText(getApplicationContext(),
	    		//		"Cliente Actualizado!", Toast.LENGTH_SHORT)
	    			//	.show();
	    
		}catch (Exception e){
			sincronizocorrectamente=-5;
			 int aa = -99;
	         Message msg = new Message();
	         msg.obj = aa;
	         puente.sendMessage(msg);
			
		   //Toast.makeText(getApplicationContext(),
   			//	"Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
   				//.show();    	
		}
		}
		
	}
		
		
		private Handler puente = new Handler() {
			  @Override
			  public void handleMessage(Message msg) {
			   progressDialog.setProgress((Integer)msg.obj);
			   if ((Integer)msg.obj>=99) 
				{
				   			
				   if (sincronizocorrectamente>1) {
				   
					   		Toast.makeText(getApplicationContext(),
			    			"Sincronizado Correctamente", Toast.LENGTH_LONG).show();
				           msg.obj=100;
				           progressDialog.dismiss();
				   }
			    }else{
			    	
			    	 if (sincronizocorrectamente<1) {
			    	if ((Integer)msg.obj==-99) {
			    		  progressDialog.dismiss();
			    			Toast.makeText(getApplicationContext(),
			    			"Error de Conexion, Favor vuelva a intentarlo", Toast.LENGTH_LONG).show();
			    			
			    	  }
			    	 }
			    }
			  
			  
			  }
			 };
		
}
