package com.visitasgps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitasgps.R;

import org.jetbrains.annotations.NotNull;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Vector;

// hasta aqui inserte para la pruebas
////////////////////////////
//import android.util.Printer;

public class Sincronizar extends Activity {
	String Stringcodvendedor= "";

	private final static String ETIQUETA_ERROR = "ERROR";
	private static final String namespace = "http://mail.amanecer.com.py/lservicios";
	private static final String url = "http://mail.amanecer.com.py:9091/lservicios.php";

	private static final String Metodosincronizarusuarios = "Metodosincronizarusuarios";
	private static final String accionSoapsincronizarusuarios = "http://mail.amanecer.com.py/lservicios.php/Metodosincronizarusuarios";
	private static final String MetodoFiltrarClientesObras = "verificarcliente3obras";
	private static final String accionSoapFiltrarClientesObras = "http://mail.amanecer.com.py/lservicios.php/verificarcliente3obras";
	private static final String Metodosincronizarproductos = "Metodosincronizarproductos";
	private static final String accionSoapsincronizarproductos = "http://mail.amanecer.com.py/lservicios.php/Metodosincronizarproductos";
	private static final String Metodosincronizarsafiliados="Metodosincronizarsafiliados";
	private static final String accionSoapsincronizarafiliados="http://mail.amanecer.com.py/lservicios.php/Metodosincronizarsafiliados";
	private static final String consultabancos = "consultabancos";
	private static final String accionSoapconsultabancos = "http://mail.amanecer.com.py/lservicios.php/consultabancos";
	private static final String MetodobuscarFormas = "consultaformas";
	private static final String accionSoapFormas = "http://mail.amanecer.com.py/lservicios.php/consultaformas";
	private static final String MetodobuscarTipoSolicitud = "consultatiposolicitud";
	private static final String accionSoapTipoSolicitud  = "http://mail.amanecer.com.py/lservicios.php/consultatiposolicitud";
	private static final String MetodobuscarNumerosRecibos = "MetodobuscarNumerosRecibos";
	private static final String accionSoapNumerosRecibos = "http://mail.amanecer.com.py/lservicios.php/MetodobuscarNumerosRecibos";

	public int versiondb=1;
	private   ProgressDialog progressDialog ;
	ProgressBar pb;
	TextView txt;
	int progreso = 0;
	int i =0;
	Boolean isActivo = false;
	//Handler puente = new Handler();

	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizar);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        txt = (TextView)findViewById(R.id.porcentaje);
    }


	@SuppressLint("HandlerLeak")
	Handler puente = new Handler() {

		  public void handleMessage(@NotNull Message msg) {
		  	//msg.obj = progreso;
		  	//pb.setProgress(progreso);
		   if (progreso==100){
			   Toast.makeText(getApplicationContext(),
		    			"Sincronizado Correctamente ", Toast.LENGTH_SHORT).show();
		      			//pb.setVisibility(View.GONE);
		    }else{
			   Toast.makeText(getApplicationContext(),
			   "Error de Conexion, vuelva a intentarlo!", Toast.LENGTH_SHORT).show();
			   pb.setVisibility(View.GONE);
			   txt.setText(0+" % realizado!");

		   }
		  }
		 };

	public void salir(View view){
		finish();
	}

	public void SincronizarUsuarios(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Iniciar sincronización de Usuarios?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					new sincronizar_usuarios().start();
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

	// Conexión HTTP, android solo permite a través de hilos
	 class sincronizar_usuarios extends Thread {
	 	@Override
	 	public void run() {
			try {
				SoapObject request = new SoapObject(namespace, Metodosincronizarusuarios);
				SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				System.out.println(url);
				HttpTransportSE transporte = new HttpTransportSE(url);
				try
				{
					transporte.call(accionSoapsincronizarusuarios, envelope);
				}catch(Exception e){
					System.out.println("Error al llamar al web service: " + e);
					progreso = 99;
					Message msg = new Message();
					puente.sendMessage(msg);
				}
				SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
				Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
				int count = vectordeusuarios.size(); // Cantidad de registros

				UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
						"DBUsuarios", null,versiondb);
				final SQLiteDatabase db3 = sql3.getWritableDatabase();

				try
				{
					db3.execSQL("delete from  Usuarios");
					db3.execSQL("DROP TABLE IF EXISTS Usuarios");
				}catch (Exception e){
					progreso = 99;
					Message msg = new Message();
					puente.sendMessage(msg);
				}

				try
				{
					db3.execSQL("CREATE TABLE Usuarios (_id INTEGER PRIMARY KEY AUTOINCREMENT, desusuario TEXT, passusuario TEXT,codusuario INTEGER,codvendedor INTEGER,codcobrador INTEGER,desvendedor TEXT,descobradror TEXT, usuarioactual INTEGER)");
				}catch (Exception e){
					//int aa = -99;
					//Message msg = new Message();
					//msg.obj = aa;
					//puente.sendMessage(msg);
				}
				for (int i = 0; i <count; i++)
				{
					UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
							"DBUsuarios", null,versiondb);
					final SQLiteDatabase db = sql.getWritableDatabase();
					SoapObject test = (SoapObject) vectordeusuarios.get(i);
					String Stringdesusuario = (String) test.getProperty("desusuario");
					String Stringcodusuario = (String) test.getProperty("codusuario");    // se recibe como string
					String Stringpassusuario = (String) test.getProperty("passusuario");    // se recibe como string
					String Stringcodvendedor = (String) test.getProperty("codvendedor");    // se recibe como string
					String Stringdesvendedor = (String) test.getProperty("desvendedor");    // se recibe como string
					String Stringcodcobrador = (String) test.getProperty("codcobrador");    // se recibe como string
					String Stringdescobrador = (String) test.getProperty("descobrador");    // se recibe como string

					int codusuario = Integer.parseInt(Stringcodusuario); // se convierte a integer
					int codvendedor =Integer.parseInt(Stringcodvendedor); // se convierte a integer
					int codcobrador =Integer.parseInt(Stringcodcobrador); // se convierte a integer
					if (db != null) {
						ContentValues nuevoRegistro = new ContentValues();
						nuevoRegistro.put("desusuario", Stringdesusuario.trim());
						nuevoRegistro.put("passusuario", Stringpassusuario.trim());
						nuevoRegistro.put("codusuario", codusuario);
						nuevoRegistro.put("codvendedor", codvendedor);
						nuevoRegistro.put("desvendedor", Stringdesvendedor.trim());
						nuevoRegistro.put("codcobrador", codcobrador);
						nuevoRegistro.put("descobradror", Stringdescobrador.trim());
						nuevoRegistro.put("usuarioactual",0);

						try {
							db.insert("Usuarios", null, nuevoRegistro);
							// Se obtiene el porcentaje y se actualiza la barra por cada registro
							int porcentaje= (i+1)*100/count;
							pb.setProgress(porcentaje);
							txt.setText(porcentaje + " %");
							Thread.sleep(30);
						} catch (Exception e) {
							progreso = 99;
							Message msg = new Message();
							puente.sendMessage(msg);
						}
						db.close();
					}
				}
				progreso=100;
				Message msg = new Message();
				puente.sendMessage(msg);

			} catch (Exception e) {
				progreso = 99;
				Message msg = new Message();
				puente.sendMessage(msg);
			}
		}
	 }

	class Mihilobancos extends Thread {
	  public void run() {

		  try{
				UtilidadesSQL sql33 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db33 = sql33.getWritableDatabase();
				db33.execSQL("DROP TABLE IF EXISTS Bancos");
				db33.execSQL("CREATE TABLE Bancos (_id INTEGER PRIMARY KEY AUTOINCREMENT, desbanco TEXT, codbanco INTEGER)");
				}catch(Exception e){
						System.out.println(e);
						e.printStackTrace();
				}

			try{

				 SoapObject request = new SoapObject(namespace, consultabancos);
		    	 SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    	 envelope.dotNet = true;
		    	 envelope.setOutputSoapObject(request);
		    	 HttpTransportSE transporte = new HttpTransportSE(url);

		    	 transporte.call(accionSoapconsultabancos, envelope);

		    	 SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
		    	 Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
		    	 int count = vectordeusuarios.size(); // contiene la cantidad de registros(objetos usuarios) devueltos
		    	 for (int i = 0; i <count; i++)
		    	 {
		    		 SoapObject test = (SoapObject) vectordeusuarios.get(i);
		    		 String Stringdesbanco = (String) test.getProperty("desbanco");
		    		 String Stringcodbanco = (String) test.getProperty("codbanco");    // se recibe como string

		    		 int codbanco = Integer.parseInt(Stringcodbanco); // se convierte a integer
		    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
								"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();
						if (db != null) {
							ContentValues nuevoRegistro = new ContentValues();
							nuevoRegistro.put("desbanco", Stringdesbanco.trim());
							nuevoRegistro.put("codbanco", codbanco);

							try {
								db.insert("Bancos", null, nuevoRegistro);
							} catch (Exception e) {
								System.out.println(e);
								e.printStackTrace();
							}
							db.close();
						}

		    	 }
		    	   // Toast.makeText(getApplicationContext(),
		    		//		"Tabla de Bancos  sincronizada!", Toast.LENGTH_SHORT)
		    		//		.show();


		    	 for(int a=50;a<80;a++){
			         int contador = a;
			         Message msg = new Message();
			         msg.obj = a;
			         puente.sendMessage(msg);

			        }




		    	 for(int a=80;a<100;a++){
			         int contador = a;
			         Message msg = new Message();
			         msg.obj = a;
			         puente.sendMessage(msg);

			        }
			}catch (Exception e){

				progreso = 99;
				Message msg = new Message();
				puente.sendMessage(msg);
			}
	  }

}

class mihiloformasdecobro extends Thread{
	public void run(){
		try{

			 int bbb;
	          bbb=20;
	          Message msg2 = new Message();
	          msg2.obj = bbb;
	          puente.sendMessage(msg2);

			UtilidadesSQL sql66 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db66 = sql66.getWritableDatabase();
			db66.execSQL("DROP TABLE IF EXISTS Formas");
			db66.execSQL("CREATE TABLE Formas (_id INTEGER PRIMARY KEY AUTOINCREMENT, desforcobro TEXT, codforcobro INTEGER)");
			}catch(Exception e){
			     int aa = -99;
		         Message msg = new Message();
		         msg.obj = aa;
		         puente.sendMessage(msg);

				//	Toast.makeText(getApplicationContext(),
				//	"Error al crear la tabla de Formas de Cobro", Toast.LENGTH_SHORT).show();					e.printStackTrace();
			}


				try{
				      	 SoapObject request_forma = new SoapObject(namespace, MetodobuscarFormas);
				      	 SoapSerializationEnvelope envelope_formas =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
				         envelope_formas.dotNet = true;
				      	 envelope_formas.setOutputSoapObject(request_forma);
				      	 HttpTransportSE transporte_formas = new HttpTransportSE(url);
				      	 transporte_formas.call(accionSoapFormas, envelope_formas);

				      	 SoapObject resultsRequestSOAP_formas = (SoapObject) envelope_formas.bodyIn;
				      	 Vector vectordeformas = (Vector) resultsRequestSOAP_formas.getProperty("return");
				      	 int count_formas = vectordeformas.size(); // contiene la cantidad de registros(objetos bancos) devueltos



					  	 for(int a=50;a<80;a++){
						         int contador = a;
						         Message msg = new Message();
						         msg.obj = a;
						         puente.sendMessage(msg);

						        }

				      	 for (int i = 0; i <count_formas; i++)
				      	 {
				      		 SoapObject test_formas = (SoapObject) vectordeformas.get(i);
				      		 String Stringdesforcobro = (String) test_formas.getProperty("desforcobro");
				      		 String Stringcodforcobro = (String) test_formas.getProperty("codforcobro");    // se recibe como string
				      		 int codforcobro = Integer.parseInt(Stringcodforcobro); // se convierte a integer

				    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
										"DBUsuarios", null, versiondb);
								final SQLiteDatabase db = sql.getWritableDatabase();

								if (db != null) {
									ContentValues nuevoRegistro = new ContentValues();
									nuevoRegistro.put("desforcobro", Stringdesforcobro.trim());
									nuevoRegistro.put("codforcobro", codforcobro);

									try {
										db.insert("Formas", null, nuevoRegistro);
									} catch (Exception e) {

									     int aa = -99;
								         Message msg = new Message();
								         msg.obj = aa;
								         puente.sendMessage(msg);

										//Toast.makeText(getApplicationContext(),
										//		"Error al Insertar las formas", Toast.LENGTH_SHORT).show();
										//e.printStackTrace();
									}
									db.close();


								   }
						 }


				      	 for(int a=80;a<100;a++){
					         int contador = a;
					         Message msg = new Message();
					         msg.obj = a;
					         puente.sendMessage(msg);

					        }

				       	} catch (Exception e) {
				            int aa = -99;
					         Message msg = new Message();
					         msg.obj = aa;
					         puente.sendMessage(msg);

				       		//  Toast.makeText(getApplicationContext(),
								//"Error de conexion1 "+e.getMessage() , Toast.LENGTH_SHORT)
								//.show();
				      	}
	}
}

//MetodobuscarTipoSolicitud
//accionSoapTipoSolicitud

class mihilotiposolicitud extends Thread{
	public void run(){
		try{
			int bbb;
	        bbb=20;
	        Message msg2 = new Message();
	        msg2.obj = bbb;
	        puente.sendMessage(msg2);

			UtilidadesSQL sql66 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db66 = sql66.getWritableDatabase();
			db66.execSQL("DROP TABLE IF EXISTS Tiposolicitud");
			db66.execSQL("CREATE TABLE Tiposolicitud (_id INTEGER PRIMARY KEY AUTOINCREMENT, dessolicitud TEXT, codtiposolicitud INTEGER)");
			}catch(Exception e){
			     int aa = -99;
		         Message msg = new Message();
		         msg.obj = aa;
		         puente.sendMessage(msg);

				//	Toast.makeText(getApplicationContext(),
				//	"Error al crear la tabla de Formas de Cobro", Toast.LENGTH_SHORT).show();					e.printStackTrace();
			}

		//MetodobuscarTipoSolicitud
		//accionSoapTipoSolicitud

				try{
				      	 SoapObject request_tiposolicitud = new SoapObject(namespace,MetodobuscarTipoSolicitud);
				      	 SoapSerializationEnvelope envelope_tiposolicitud =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
				      	 envelope_tiposolicitud.dotNet = true;
				         envelope_tiposolicitud.setOutputSoapObject(request_tiposolicitud);
				      	 HttpTransportSE transporte_tiposolicitud = new HttpTransportSE(url);
				       	transporte_tiposolicitud.call(accionSoapTipoSolicitud, envelope_tiposolicitud);

				      	 SoapObject resultsRequestSOAP_tiposolicitud = (SoapObject) envelope_tiposolicitud.bodyIn;
				      	 Vector vectordetiposolicitud = (Vector) resultsRequestSOAP_tiposolicitud.getProperty("return");
				      	 int count_tiposolicitud = vectordetiposolicitud.size(); // contiene la cantidad de registros(objetos bancos) devueltos



					  	 for(int a=50;a<80;a++){
						         int contador = a;
						         Message msg = new Message();
						         msg.obj = a;
						         puente.sendMessage(msg);

						        }

				      	 for (int i = 0; i <count_tiposolicitud; i++)
				      	 {
				      		 SoapObject test_tiposolicitud = (SoapObject) vectordetiposolicitud.get(i);
				      		 String Stringdestiposolicitud = (String) test_tiposolicitud.getProperty("destiposolicitud");
				      		 String Stringcodtiposolicitud = (String) test_tiposolicitud.getProperty("codtiposolicitud");    // se recibe como string

				      		 int codtiposolicitud = Integer.parseInt(Stringcodtiposolicitud); // se convierte a integer

				    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
										"DBUsuarios", null, versiondb);
								final SQLiteDatabase db = sql.getWritableDatabase();

								if (db != null) {
									ContentValues nuevoRegistro = new ContentValues();
									nuevoRegistro.put("dessolicitud", Stringdestiposolicitud.trim());
									nuevoRegistro.put("codtiposolicitud", codtiposolicitud);

									try {
										db.insert("Tiposolicitud", null, nuevoRegistro);
									} catch (Exception e) {

									     int aa = -99;
								         Message msg = new Message();
								         msg.obj = aa;
								         puente.sendMessage(msg);

										//Toast.makeText(getApplicationContext(),
										//		"Error al Insertar las formas", Toast.LENGTH_SHORT).show();
										//e.printStackTrace();
									}
									db.close();


								   }
						 }


				      	 for(int a=80;a<100;a++){
					         int contador = a;
					         Message msg = new Message();
					         msg.obj = a;
					         puente.sendMessage(msg);

					        }

				       	} catch (Exception e) {
				            int aa = -99;
					         Message msg = new Message();
					         msg.obj = aa;
					         puente.sendMessage(msg);

				       		//  Toast.makeText(getApplicationContext(),
								//"Error de conexion1 "+e.getMessage() , Toast.LENGTH_SHORT)
								//.show();
				      	}
	}
}


class Mihiloclientes extends Thread{
	  @Override
		   public void run() {
			 Log.e(ETIQUETA_ERROR, "clientes en hilo 1");

			try{
				UtilidadesSQL sql44 = new UtilidadesSQL(getApplicationContext(),
						"DBUsuarios", null,versiondb);
				final SQLiteDatabase db44 = sql44.getWritableDatabase();
				db44.execSQL("DROP TABLE IF EXISTS Clientes");
				db44.execSQL("CREATE TABLE Clientes (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT ,numcliente  TEXT ,codcliente int,nif TEXT,descuentos TEXT,codsitua TEXT,diasadicional TEXT ,latitud TEXT ,longitud TEXT, enviadofoto int, enviadoubicacion int     , fototransferir int,ubicaciontransferir int ,clienteactualizado int , ubicacionenmapa TEXT , direccion TEXT  )");

			}catch(Exception e){
				Log.e(ETIQUETA_ERROR, "clientes en hilo errro al crear la tabla");
			//	Toast.makeText(getApplicationContext(),
			//	"Error al crear la tabla de Clientes", Toast.LENGTH_SHORT).show();					e.printStackTrace();

		}

	try{

		String sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional,IFNULL(latitud,'')AS latitud ,IFNULL(longitud,'')AS longitud ,ubicacionenmapa  ,IFNULL(direccion ,'')as  direccion FROM clientes where codvendedor=29";

		// String sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional,IFNULL(latitud,'0') AS latitud,IFNULL(longitud,'0') AS longitud   FROM clientes  ";
		 SoapObject request_Clientes = new SoapObject(namespace, MetodoFiltrarClientesObras);
	   	 request_Clientes.addProperty("Parametro",sql);
	   	 SoapSerializationEnvelope envelope_Clientes =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
	   	 envelope_Clientes.dotNet = true;
	   	 envelope_Clientes.setOutputSoapObject(request_Clientes);

	   	Log.e(ETIQUETA_ERROR, "clientes en hilo 2");
	   	 HttpTransportSE transporte_Clientes = new HttpTransportSE(url);
	   	Log.e(ETIQUETA_ERROR, "clientes en hilo 3");
	   	 transporte_Clientes.call(accionSoapFiltrarClientesObras, envelope_Clientes);
	   	Log.e(ETIQUETA_ERROR, "clientes en hilo 4");
	   	 SoapObject resultsRequestSOAP_Clientes = (SoapObject) envelope_Clientes.bodyIn;
	   	Log.e(ETIQUETA_ERROR, "clientes en hilo 5");
	   	 Vector vectordeClientes = (Vector) resultsRequestSOAP_Clientes.getProperty("return");
	   	Log.e(ETIQUETA_ERROR, "clientes en hilo 6");
	   	 int count = vectordeClientes.size(); // contiene la cantidad de registros(objetos bancos) devueltos


	   	// for(int a=0;a<90;a++){
	    //     int contador = a;
	    ///     Message msg = new Message();
	     //    msg.obj = a;
	     //    puente.sendMessage(msg);

	      //  }


  	 for (int i = 0; i <count; i++)
  	 {
  		int a=(i*100/count);
  	    Message msg = new Message();
        msg.obj = a;
        puente.sendMessage(msg);



  		 SoapObject test = (SoapObject) vectordeClientes.get(i);
  		 String Stringnombre = (String) test.getProperty("nombre");
  		 String Stringcodcliente = (String) test.getProperty("codcliente");    // se recibe como string
  		 String Stringnumcliente = (String) test.getProperty("numcliente");    // se recibe como string

  		 String Strinnif = (String) test.getProperty("nif");    // se recibe como string
  		 String Stringdescuentos = (String) test.getProperty("descuentos");    // se recibe como string
  		 String Stringcodsitua = (String) test.getProperty("codsitua");    // se recibe como string
  		 String diasadicional = (String) test.getProperty("diasadicional");    // se recibe como string
  		String ubicacionenmapa = (String) test.getProperty("ubicacionenmapa");    // se recibe como string
  		 String direccion= (String) test.getProperty("direccion");


  		 String latitudcliente = (String) test.getProperty("latitud");    // se recibe como string
  		 String longitudcliente = (String) test.getProperty("longitud");    // se recibe como string


  		 String latitud  = "0";//(String) test.getProperty("latitud");    // se recibe como string
  		 String longitud  = "0";//(String) test.getProperty("longitu");    // se recibe como string
  		 String enviadofoto ="0";
  		 String enviadoubicacion="0";




  		 int codcliente = Integer.parseInt(Stringcodcliente); // se convierte a integer
  		 int fototransferir =0;
  		 int ubicaciontransferir=0;

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

					//nuevoRegistro.put("latitud",latitud);
					//nuevoRegistro.put("longitud",longitud);

					nuevoRegistro.put("enviadofoto",enviadofoto);
					nuevoRegistro.put("enviadoubicacion",enviadoubicacion);
					nuevoRegistro.put("fototransferir",fototransferir);
					nuevoRegistro.put("ubicaciontransferir",ubicaciontransferir);



					nuevoRegistro.put("latitud",latitudcliente);
					nuevoRegistro.put("longitud",longitudcliente);
					nuevoRegistro.put("clienteactualizado",0);
					nuevoRegistro.put("ubicacionenmapa",ubicacionenmapa);
					nuevoRegistro.put("direccion",direccion);



					//**
					try {
						db.insert("Clientes", null, nuevoRegistro);
						Log.e(ETIQUETA_ERROR, "clientes Insertado");
					} catch (Exception e) {
						Log.e(ETIQUETA_ERROR, "clientes error 2 "+e.toString());
					//	Toast.makeText(getApplicationContext(),
					//			"Error al Insertar un cliente", Toast.LENGTH_SHORT).show();
					//	e.printStackTrace();
					}
					db.close();
				}


  	 }

  //	 for(int a=90;a<100;a++){
    //     int contador = a;
    //     Message msg = new Message();
    //     msg.obj = a;
    //     puente.sendMessage(msg);

      //  }
  	 //Toast.makeText(getApplicationContext(),
  		//		"Tabla de Clientes sincronizada!", Toast.LENGTH_SHORT)
  			//	.show();

	}catch (Exception e){
		Log.e(ETIQUETA_ERROR, "clientes error 3"+e.toString());
		  int aa = -99;
	         Message msg = new Message();
	         msg.obj = aa;
	         puente.sendMessage(msg);
		//  Toast.makeText(getApplicationContext(),
		//		"Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
			//	.show();
	}


	//************
	}

}

class Mihiloafiliados extends Thread{
	 public void run() {
	 try{
			UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db55 = sql55.getWritableDatabase();
			db55.execSQL("DROP TABLE IF EXISTS Afiliados");
			db55.execSQL("CREATE TABLE Afiliados (_id INTEGER PRIMARY KEY AUTOINCREMENT, codafiliado INTEGER ,nombre  TEXT ,codciudad INTEGER, codprofesion INTEGER, celular TEXT)");
			}catch(Exception e){

			}


		try{

			 SoapObject request = new SoapObject(namespace, Metodosincronizarsafiliados);
	    	 SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	 envelope.dotNet = true;
	    	 envelope.setOutputSoapObject(request);
	    	 HttpTransportSE transporte = new HttpTransportSE(url);

	    	 transporte.call(accionSoapsincronizarafiliados, envelope);

	    	 SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
	    	 Vector vectordeafiliados = (Vector) resultsRequestSOAP.getProperty("return");
	    	 int count = vectordeafiliados.size(); // contiene la cantidad de registros(objetos usuarios) devueltos
	    	 for (int i = 0; i <count; i++)
	    	 {

	    		 int a=(i*100/count);
	    	  	    Message msg = new Message();
	    	        msg.obj = a;
	    	        puente.sendMessage(msg);



	    		 String Stringcodafiliado = "";
	    		 String Stringnombre ="";
	    		 String  Stringcodciudad ="";
	    		 String  Stringcodprofesion ="";
	    		 String Stringcelular="";
	    		 try{
	    		     SoapObject test = (SoapObject) vectordeafiliados.get(i);
	    		     Stringcodafiliado = (String) test.getProperty("codafiliado");
	    		     Stringnombre = (String) test.getProperty("nombre");    // se recibe como string
	    		     Stringcodciudad = (String) test.getProperty("codciudad");    // se recibe como string
	    		     Stringcodprofesion = (String) test.getProperty("codprofesion");    // se recibe como string
	    		     Stringcelular = (String) test.getProperty("celular");    // se recibe como string
	    		 }catch (Exception e){
		 		 }

	    		 int codafiliado = Integer.parseInt(Stringcodafiliado); // se convierte a integer
	    		 int codciudad = Integer.parseInt(Stringcodciudad); // se convierte a integer
	    		 int codprofesion = Integer.parseInt(Stringcodprofesion); // se convierte a integer

	    		 UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
							"DBUsuarios", null, versiondb);
					final SQLiteDatabase db = sql.getWritableDatabase();
					if (db != null) {


						ContentValues nuevoRegistro = new ContentValues();
						nuevoRegistro.put("codafiliado",  codafiliado);
						nuevoRegistro.put("codciudad", codciudad);
						nuevoRegistro.put("codprofesion", codprofesion);
						nuevoRegistro.put("nombre", Stringnombre.toString().trim());
						nuevoRegistro.put("celular", Stringcelular.toString().trim());
						try {
							db.insert("Afiliados", null, nuevoRegistro);
						} catch (Exception e) {
						}
						db.close();
					}

	    	 }

		}catch (Exception e){

			 int aa = -99;
	         Message msg = new Message();
	         msg.obj = aa;
	         puente.sendMessage(msg);
			}


}
}

class Mihiloproductos extends Thread{
		 public void run() {
		 try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Articulos");
				db55.execSQL("CREATE TABLE Articulos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codarticulo INTEGER ,descripcion  TEXT ,codigobarras TEXT, precio_pvp INTEGER)");
				}catch(Exception e){
			//			Toast.makeText(getApplicationContext(),
			//			"Error al crear la tabla de Articulos", Toast.LENGTH_SHORT).show();					e.printStackTrace();
				}


			try{

				 SoapObject request = new SoapObject(namespace, Metodosincronizarproductos);
		    	 SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    	 envelope.dotNet = true;
		    	 envelope.setOutputSoapObject(request);
		    	 HttpTransportSE transporte = new HttpTransportSE(url);

		    	 transporte.call(accionSoapsincronizarproductos, envelope);

		    	 SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
		    	 Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
		    	 int count = vectordeusuarios.size(); // contiene la cantidad de registros(objetos usuarios) devueltos

		    //	 Toast.makeText(getApplicationContext(),
		    //				"registros="+count, Toast.LENGTH_LONG).show();

		    	 for (int i = 0; i <count; i++)
		    	 {

		    		 int a=(i*100/count);
		    	  	    Message msg = new Message();
		    	        msg.obj = a;
		    	        puente.sendMessage(msg);



		    		 String Stringcodarticulo = "";
		    		 String Stringprecio_pvp ="";
		    		 String  Stringdescripcion ="";
		    		 String  Stringcodigobarras ="";
		    		 try{
		    		     SoapObject test = (SoapObject) vectordeusuarios.get(i);
		    		       Stringcodarticulo = (String) test.getProperty("codarticulo");
		    		      Stringdescripcion = (String) test.getProperty("descripcion");    // se recibe como string
		    		       Stringcodigobarras = (String) test.getProperty("codigobarras");    // se recibe como string
		    		    Stringprecio_pvp = (String) test.getProperty("precio_pvp");    // se recibe como string

		    		 }catch (Exception e){
			 			 ///   Toast.makeText(getApplicationContext(),
			 	    		//		"NO P�DO LEER", Toast.LENGTH_LONG)
			 	    		//		.show();
			 		 }

		    		 int codarticulo = Integer.parseInt(Stringcodarticulo); // se convierte a integer
		    		 int precio_pvp = Integer.parseInt(Stringprecio_pvp); // se convierte a integer

		    		UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
								"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();
						if (db != null) {


							ContentValues nuevoRegistro = new ContentValues();
							nuevoRegistro.put("codarticulo",  codarticulo);
							nuevoRegistro.put("descripcion", Stringdescripcion.trim());
							nuevoRegistro.put("codigobarras", Stringcodigobarras.trim());
							nuevoRegistro.put("precio_pvp", precio_pvp);

							try {
								db.insert("Articulos", null, nuevoRegistro);
							} catch (Exception e) {
							//	//Toast.makeText(getApplicationContext(),
							//			"Error al Insertar", Toast.LENGTH_SHORT).show();
							//	e.printStackTrace();
							}
							db.close();
						}

		    	 }
		    	// Toast.makeText(getApplicationContext(),
		    	//			"Actualizado con ="+count+" Articulos", Toast.LENGTH_SHORT)
		    	///			.show();

			}catch (Exception e){

				 int aa = -99;
		         Message msg = new Message();
		         msg.obj = aa;
		         puente.sendMessage(msg);


				// Toast.makeText(getApplicationContext(),
	    		//		"1-Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
	    			//	.show();
			    		//e.printStackTrace();
			}


	 }
}
class mihilocobranzas extends Thread{
	 public void run() {

			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS detallefacturaspagadas");  //11**
				db55.execSQL("CREATE TABLE detallefacturaspagadas (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_cobro INTEGER ,numventa  TEXT ,codcliente INTEGER, numcliente TEXT,codcobrador INTEGER,fechavcto TEXT,saldo INTEGER,importepagado INTEGER,codventa  INTEGER,rendido INTEGER,numerorecibo INTEGER,descobrador TEXT,nombre TEXT,fecharecibo TEXT,total INTEGER,ruc TEXT,enviado INTEGER, horarecibo TEXT)");


				db55.execSQL("DROP TABLE IF EXISTS detalleformadecobros");   //2
				db55.execSQL("CREATE TABLE detalleformadecobros (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_cobro INTEGER ,codcliente INTEGER,codcobrador INTEGER,fechadocumento TEXT,numerodocumento TEXT,monto INTEGER,codforcobro INTEGER,codbanco INTEGER,anulado INTEGER,rendido INTEGER,numerorecibo INTEGER,desforcobro TEXT,desbanco TEXT,descobrador TEXT)");



				db55.execSQL("DROP TABLE IF EXISTS numerorecibo");
				db55.execSQL("CREATE TABLE numerorecibo (_id INTEGER PRIMARY KEY AUTOINCREMENT, codcobrador INTEGER ,rango1 INTEGER,rango2 INTEGER,ultimo INTEGER)");



				db55.execSQL("DROP TABLE IF EXISTS numerosconfallos");
				db55.execSQL("CREATE TABLE numerosconfallos(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobro INTEGER, codcobrador INTEGER,numrecibo INTEGER )");



				db55.execSQL("DROP TABLE IF EXISTS cobranzaespecial");
			    db55.execSQL("CREATE TABLE cobranzaespecial(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobroespecial INTEGER, codbanco INTEGER,desbanco TEXT,numero TEXT,fecha TEXT, importe INTEGER, saldo INTEGER, codcobrador INTEGER, estado INTEGER,enviado INTEGER )");


				db55.execSQL("DROP TABLE IF EXISTS chequescobrosespeciales");
				db55.execSQL("CREATE TABLE chequescobrosespeciales(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobroespecial INTEGER, idcobro INTEGER,importe INTEGER , codcobrador INTEGER,numcobro INTEGER ,enviado INTEGER,codcliente INTEGER,numerorecibo INTEGER)");
				//db55.execSQL("CREATE TABLE chequescobrosespeciales(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobroespecial INTEGER, idcobro INTEGER,importe INTEGER , codcobrador INTEGER,enviado INTEGER,codcliente INTEGER,numerorecibo INTEGER)");

				try{
			      	 SoapObject request_forma = new SoapObject(namespace, MetodobuscarNumerosRecibos);
			      	 SoapSerializationEnvelope envelope_formas =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
			         envelope_formas.dotNet = true;
			      	 envelope_formas.setOutputSoapObject(request_forma);
			      	 HttpTransportSE transporte_formas = new HttpTransportSE(url);
			      	 transporte_formas.call(accionSoapNumerosRecibos, envelope_formas);

			      	 SoapObject resultsRequestSOAP_RECIBOS = (SoapObject) envelope_formas.bodyIn;
			      	 Vector vectordeRECIBOS = (Vector) resultsRequestSOAP_RECIBOS.getProperty("return");
			      	 int count_RECIBOS = vectordeRECIBOS.size(); // contiene la cantidad de registros(objetos bancos) devueltos


			      	 for (int i = 0; i <count_RECIBOS; i++)
			      	 {


			      		 int a=(i*100/count_RECIBOS);
			    	  	    Message msg = new Message();
			    	        msg.obj = a;
			    	        puente.sendMessage(msg);


			      		 SoapObject test_formas = (SoapObject) vectordeRECIBOS.get(i);
			      		 String Stringcodcobrador = (String) test_formas.getProperty("codcobrador");
			      		 String Stringrango1 = (String) test_formas.getProperty("rango1");    // se recibe como string
			      		 String Stringrango2 = (String) test_formas.getProperty("rango2");    // se recibe como string
			      		 String Stringultimo = (String) test_formas.getProperty("ultimo");    // se recibe como string


			    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
									"DBUsuarios", null, versiondb);
							final SQLiteDatabase db = sql.getWritableDatabase();

							if (db != null) {
								ContentValues nuevoRegistro = new ContentValues();
								nuevoRegistro.put("codcobrador", String.valueOf(Stringcodcobrador.trim()));
								nuevoRegistro.put("rango1", String.valueOf(Stringrango1));
								nuevoRegistro.put("rango2", String.valueOf(Stringrango2));
								nuevoRegistro.put("ultimo", String.valueOf(Stringultimo));

								try {
									db.insert("numerorecibo", null, nuevoRegistro);

								} catch (Exception e) {


									//Toast.makeText(getApplicationContext(),
										//	"Error al Insertar las Rangos", Toast.LENGTH_SHORT).show();
									//e.printStackTrace();
								}
								db.close();


							   }
					 }
			       	} catch (Exception e) {
			       	 int aa = -99;
			         Message msg = new Message();
			         msg.obj = aa;
			         puente.sendMessage(msg);
			       		//   Toast.makeText(getApplicationContext(),
						//	"Error de conexion1 "+e.getMessage() , Toast.LENGTH_SHORT)
						//	.show();
			      	}

				 int aa = 100;
		         Message msg = new Message();
		         msg.obj = aa;
		         puente.sendMessage(msg);


			}catch(Exception e){

				 int aa = -99;
		         Message msg = new Message();
		         msg.obj = aa;
		         puente.sendMessage(msg);


				}


	 }
}


public void sincronizar_bancos_thread(){
	Thread progresoBar = new Thread(new Runnable() {
		public void run() {
			for (i=0; i < 100; i++)
			{
				progreso += doWork();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
					progreso = 0;
				}
				puente.post(new Runnable() {
					@Override
					public void run() {
						pb.setProgress(i);
						txt.setText(i + " %");
					}
				});
				//progreso = 100;
			}
		}
		private int doWork() {
			return i * 5;
		}
	});
	progresoBar.start();


     Mihilobancos hilobanco = new Mihilobancos();
     hilobanco.start();
}


public void sincronizar_afiliados_thread(){
		 progressDialog = new ProgressDialog(Sincronizar.this);
	     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     progressDialog.setMessage("Sincronizando Afiliados...");
	     progressDialog.setMax(100);
	     progressDialog.setProgress(10);
	     progressDialog.setCancelable(false);
	     progressDialog.show();

	     Mihiloafiliados hiloafiliados = new Mihiloafiliados();
	     hiloafiliados.start();

}

public void sincronizar_productos_thread(){
	 progressDialog = new ProgressDialog(Sincronizar.this);
     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
     progressDialog.setMessage("Sincronizando...");
     progressDialog.setMax(100);
     progressDialog.setProgress(10);
     progressDialog.setCancelable(false);
     progressDialog.show();
     Mihiloproductos hiloproductos = new Mihiloproductos();
     hiloproductos.start();
}
public void sincronizar_clientes_thread(){
	 progressDialog = new ProgressDialog(Sincronizar.this);
     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
     progressDialog.setMessage("Sincronizando...");
     progressDialog.setMax(100);
     progressDialog.setProgress(10);
     progressDialog.setCancelable(false);
     progressDialog.show();
     Mihiloclientes hiloclientes = new Mihiloclientes();
     hiloclientes.start();
}

public void SincronizarBancos(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Desea Sincronizar los Bancos en el Sistema?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //sincronizar_bancos();
		        	   sincronizar_bancos_thread();
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
public void SincronizarFormasdeCobro(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Desea Sincronizar las formas de cobro?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {

		        	  sincronizar_formas_cobro_thread();

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

public void sincronizar_formas_cobro_thread(){

		 progressDialog = new ProgressDialog(Sincronizar.this);
	     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     progressDialog.setMessage("Sincronizando formas de cobro...");
	     progressDialog.setMax(100);
	     progressDialog.setProgress(10);
	     progressDialog.setCancelable(false);
	     progressDialog.show();
	     mihiloformasdecobro hiloformasdecobro = new mihiloformasdecobro();
	     hiloformasdecobro.start();

	}

public void sincronizar_tipo_solicitudes_thread(){

	 progressDialog = new ProgressDialog(Sincronizar.this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setMessage("Sincronizando los  tipo de Solicitudes ");
    progressDialog.setMax(100);
    progressDialog.setProgress(10);
    progressDialog.setCancelable(false);
    progressDialog.show();

    mihilotiposolicitud hilotipodesolicitudes = new mihilotiposolicitud();
    hilotipodesolicitudes.start();

}

public void sincronizar_formas_cobro(){
			try{
		UtilidadesSQL sql66 = new UtilidadesSQL(getApplicationContext(),
		"DBUsuarios", null,versiondb);
		final SQLiteDatabase db66 = sql66.getWritableDatabase();
		db66.execSQL("DROP TABLE IF EXISTS Formas");
		db66.execSQL("CREATE TABLE Formas (_id INTEGER PRIMARY KEY AUTOINCREMENT, desforcobro TEXT, codforcobro INTEGER)");
		}catch(Exception e){
				Toast.makeText(getApplicationContext(),
				"Error al crear la tabla de Formas de Cobro", Toast.LENGTH_SHORT).show();					e.printStackTrace();
		}


			try{
			      	 SoapObject request_forma = new SoapObject(namespace, MetodobuscarFormas);
			      	 SoapSerializationEnvelope envelope_formas =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
			         envelope_formas.dotNet = true;
			      	 envelope_formas.setOutputSoapObject(request_forma);
			      	 HttpTransportSE transporte_formas = new HttpTransportSE(url);
			      	 transporte_formas.call(accionSoapFormas, envelope_formas);

			      	 SoapObject resultsRequestSOAP_formas = (SoapObject) envelope_formas.bodyIn;
			      	 Vector vectordeformas = (Vector) resultsRequestSOAP_formas.getProperty("return");
			      	 int count_formas = vectordeformas.size(); // contiene la cantidad de registros(objetos bancos) devueltos


			      	 for (int i = 0; i <count_formas; i++)
			      	 {
			      		 SoapObject test_formas = (SoapObject) vectordeformas.get(i);
			      		 String Stringdesforcobro = (String) test_formas.getProperty("desforcobro");
			      		 String Stringcodforcobro = (String) test_formas.getProperty("codforcobro");    // se recibe como string
			      		 int codforcobro = Integer.parseInt(Stringcodforcobro); // se convierte a integer

			    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
									"DBUsuarios", null, versiondb);
							final SQLiteDatabase db = sql.getWritableDatabase();

							if (db != null) {
								ContentValues nuevoRegistro = new ContentValues();
								nuevoRegistro.put("desforcobro", Stringdesforcobro.trim());
								nuevoRegistro.put("codforcobro", codforcobro);

								try {
									db.insert("Formas", null, nuevoRegistro);
								} catch (Exception e) {
									Toast.makeText(getApplicationContext(),
											"Error al Insertar las formas", Toast.LENGTH_SHORT).show();
									e.printStackTrace();
								}
								db.close();


							   }
					 }
			       	} catch (Exception e) {
					         Toast.makeText(getApplicationContext(),
							"Error de conexion1 "+e.getMessage() , Toast.LENGTH_SHORT)
							.show();
			      	}


	}

public void SincronizarTipoSolicitudes (View view){




	try{
 	   UtilidadesSQL sql= new UtilidadesSQL(getApplicationContext(),
					"DBUsuarios", null, versiondb);
					final SQLiteDatabase db = sql.getWritableDatabase();
					db.execSQL("update  Visitas   set  transferido=0 ");
					Toast.makeText(getApplicationContext(),
							"La visita fue marcada como no sincronizada", Toast.LENGTH_LONG)
							.show();
    }catch(Exception e ){
		Toast.makeText(getApplicationContext(),
				"Problemas para anular el recibo", Toast.LENGTH_LONG)
				.show();}




	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Desea Sincronizar las Solicitudes?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	  sincronizar_tipo_solicitudes_thread();

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


public void SincronizarSolicitudes (View view){

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Desea Reiniciar  las  Solicitudes?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	  //sincronizar_tipo_solicitudes_thread();

	        	   UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
	        				"DBUsuarios", null,versiondb);
	        				final SQLiteDatabase db55 = sql55.getWritableDatabase();
	        				db55.execSQL("DROP TABLE IF EXISTS Solicitudes");
	        				db55.execSQL("CREATE TABLE Solicitudes (_id INTEGER PRIMARY KEY AUTOINCREMENT, codigo INTEGER ,codcobrador INTEGER ,codtiposolicitud INTEGER, observacion TEXT,enviado INTEGER, estado  INTEGER, fechasolicitud TEXT,fechacambioestado TEXT,fechacierre TEXT,codcliente INTEGER,randomico TEXT,cerrado INTEGER, fotouno TEXT, fotounoenviado INTEGER, fotodos TEXT, fotodosenviado INTEGER, fototres TEXT, fototresenviado INTEGER, transferido INTEGER )");


	        				  UtilidadesSQL sql15 = new UtilidadesSQL(getApplicationContext(),
	      	        				"DBUsuarios", null,versiondb);
	      	        				final SQLiteDatabase db15 = sql15.getWritableDatabase();
	      	        				db15.execSQL("DROP TABLE IF EXISTS Observacionsolicitudes");
	      	        				db15.execSQL("CREATE TABLE Observacionsolicitudes (_id INTEGER PRIMARY KEY AUTOINCREMENT, idsolicitud INTEGER , observacion TEXT ,idobservacion INTEGER )");
	      	        		     	Toast.makeText(getApplicationContext(),
									"Solicitudes re greadas", Toast.LENGTH_SHORT).show();

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


public void ReimprimirCobranzas(View view){
		//Intent c = new Intent(this, ActivityReimprimir.class);
		//startActivity(c);

	}
	public void  SincronizarCobranzas(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Desea Re-Crear la Tabla de Cobranzas?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //sincronizar_cobranzas();
		        	   sincronizar_cobranzas_thread();
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
		public void SincronizarClientes(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Desea Sincronizar los Clientes ?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //sincronizar_clientes();
		        	   sincronizar_clientes_thread();
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
		public void sincronizar_cobranzas_thread(){
		 progressDialog = new ProgressDialog(Sincronizar.this);
	     progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     progressDialog.setMessage("Sincronizando...");
	     progressDialog.setMax(100);
	     progressDialog.setProgress(10);
	     progressDialog.setCancelable(false);
	     progressDialog.show();
	     mihilocobranzas hilocobranzas = new mihilocobranzas();
	     hilocobranzas.start();
	}
	public void sincronizar_cobranzas(){



		try{
			UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db55 = sql55.getWritableDatabase();
			db55.execSQL("DROP TABLE IF EXISTS detallefacturaspagadas"); //222**
			db55.execSQL("CREATE TABLE detallefacturaspagadas (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_cobro INTEGER ,numventa  TEXT ,codcliente INTEGER, numcliente TEXT,codcobrador INTEGER,fechavcto TEXT,saldo INTEGER,importepagado INTEGER,codventa  INTEGER,rendido INTEGER,numerorecibo INTEGER,descobrador TEXT,nombre TEXT,fecharecibo TEXT,total INTEGER,ruc TEXT,enviado INTEGER, horarecibo TEXT)");


			db55.execSQL("DROP TABLE IF EXISTS detalleformadecobros"); //1
			db55.execSQL("CREATE TABLE detalleformadecobros (_id INTEGER PRIMARY KEY AUTOINCREMENT, id_cobro INTEGER ,codcliente INTEGER,codcobrador INTEGER,fechadocumento TEXT,numerodocumento TEXT,monto INTEGER,codforcobro INTEGER,codbanco INTEGER,anulado INTEGER,rendido INTEGER,numerorecibo INTEGER,desforcobro TEXT,desbanco TEXT,descobrador TEXT)");



			db55.execSQL("DROP TABLE IF EXISTS numerorecibo");
			db55.execSQL("CREATE TABLE numerorecibo (_id INTEGER PRIMARY KEY AUTOINCREMENT, codcobrador INTEGER ,rango1 INTEGER,rango2 INTEGER,ultimo INTEGER)");



			db55.execSQL("DROP TABLE IF EXISTS numerosconfallos");
			db55.execSQL("CREATE TABLE numerosconfallos(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobro INTEGER, codcobrador INTEGER,numrecibo INTEGER )");



			db55.execSQL("DROP TABLE IF EXISTS cobranzaespecial");
		    db55.execSQL("CREATE TABLE cobranzaespecial(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobroespecial INTEGER, codbanco INTEGER,desbanco TEXT,numero TEXT,fecha TEXT, importe INTEGER, saldo INTEGER, codcobrador INTEGER, estado INTEGER,enviado INTEGER )");


			db55.execSQL("DROP TABLE IF EXISTS chequescobrosespeciales");
			db55.execSQL("CREATE TABLE chequescobrosespeciales(_id INTEGER PRIMARY KEY AUTOINCREMENT, numcobroespecial INTEGER, idcobro INTEGER,importe INTEGER , codcobrador INTEGER,numcobro INTEGER ,enviado INTEGER,codcliente INTEGER,numerorecibo INTEGER)");

			try{
		      	 SoapObject request_forma = new SoapObject(namespace, MetodobuscarNumerosRecibos);
		      	 SoapSerializationEnvelope envelope_formas =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
		         envelope_formas.dotNet = true;
		      	 envelope_formas.setOutputSoapObject(request_forma);
		      	 HttpTransportSE transporte_formas = new HttpTransportSE(url);
		      	 transporte_formas.call(accionSoapNumerosRecibos, envelope_formas);

		      	 SoapObject resultsRequestSOAP_RECIBOS = (SoapObject) envelope_formas.bodyIn;
		      	 Vector vectordeRECIBOS = (Vector) resultsRequestSOAP_RECIBOS.getProperty("return");
		      	 int count_RECIBOS = vectordeRECIBOS.size(); // contiene la cantidad de registros(objetos bancos) devueltos


		      	 for (int i = 0; i <count_RECIBOS; i++)
		      	 {
		      		 SoapObject test_formas = (SoapObject) vectordeRECIBOS.get(i);
		      		 String Stringcodcobrador = (String) test_formas.getProperty("codcobrador");
		      		 String Stringrango1 = (String) test_formas.getProperty("rango1");    // se recibe como string
		      		 String Stringrango2 = (String) test_formas.getProperty("rango2");    // se recibe como string
		      		 String Stringultimo = (String) test_formas.getProperty("ultimo");    // se recibe como string


		    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
								"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();

						if (db != null) {
							ContentValues nuevoRegistro = new ContentValues();
							nuevoRegistro.put("codcobrador", String.valueOf(Stringcodcobrador.trim()));
							nuevoRegistro.put("rango1", String.valueOf(Stringrango1));
							nuevoRegistro.put("rango2", String.valueOf(Stringrango2));
							nuevoRegistro.put("ultimo", String.valueOf(Stringultimo));

							try {
								db.insert("numerorecibo", null, nuevoRegistro);

							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),
										"Error al Insertar las Rangos", Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
							db.close();


						   }
				 }
		       	} catch (Exception e) {
				         Toast.makeText(getApplicationContext(),
						"Error de conexion1 "+e.getMessage() , Toast.LENGTH_SHORT)
						.show();
		      	}
				Toast.makeText(getApplicationContext(),
					"Tablas de Cobranzas fueron  Creadas!", Toast.LENGTH_LONG).show();
		}catch(Exception e){
					Toast.makeText(getApplicationContext(),
					"Error al crear la tabla de Pedidos", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
			}

	}
	public void  sincronizar_bancos(){
		try{
			UtilidadesSQL sql33 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db33 = sql33.getWritableDatabase();
			db33.execSQL("DROP TABLE IF EXISTS Bancos");
			db33.execSQL("CREATE TABLE Bancos (_id INTEGER PRIMARY KEY AUTOINCREMENT, desbanco TEXT, codbanco INTEGER)");
			}catch(Exception e){
					Toast.makeText(getApplicationContext(),
					"Error al crear la tabla de bancos", Toast.LENGTH_SHORT).show();					e.printStackTrace();
			}

		try{

			 SoapObject request = new SoapObject(namespace, consultabancos);
	    	 SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    	 envelope.dotNet = true;
	    	 envelope.setOutputSoapObject(request);
	    	 HttpTransportSE transporte = new HttpTransportSE(url);

	    	 transporte.call(accionSoapconsultabancos, envelope);

	    	 SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
	    	 Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
	    	 int count = vectordeusuarios.size(); // contiene la cantidad de registros(objetos usuarios) devueltos
	    	 for (int i = 0; i <count; i++)
	    	 {
	    		 SoapObject test = (SoapObject) vectordeusuarios.get(i);
	    		 String Stringdesbanco = (String) test.getProperty("desbanco");
	    		 String Stringcodbanco = (String) test.getProperty("codbanco");    // se recibe como string

	    		 int codbanco = Integer.parseInt(Stringcodbanco); // se convierte a integer
	    		 	UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
							"DBUsuarios", null, versiondb);
					final SQLiteDatabase db = sql.getWritableDatabase();
					if (db != null) {
						ContentValues nuevoRegistro = new ContentValues();
						nuevoRegistro.put("desbanco", Stringdesbanco.trim());
						nuevoRegistro.put("codbanco", codbanco);

						try {
							db.insert("Bancos", null, nuevoRegistro);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"Error al Insertar", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
						db.close();
					}

	    	 }
	    	    Toast.makeText(getApplicationContext(),
	    				"Tabla de Bancos  sincronizada!", Toast.LENGTH_SHORT)
	    				.show();

		}catch (Exception e){
		    Toast.makeText(getApplicationContext(),
   				"Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
   				.show();
		}


		//*****
	}
			public void  sincronizar_clientes(){


				try{
			UtilidadesSQL sql44 = new UtilidadesSQL(getApplicationContext(),
			"DBUsuarios", null,versiondb);
			final SQLiteDatabase db44 = sql44.getWritableDatabase();
			db44.execSQL("DROP TABLE IF EXISTS Clientes");
			db44.execSQL("CREATE TABLE Clientes (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT ,numcliente  TEXT ,codcliente int,nif TEXT,descuentos TEXT,codsitua TEXT,diasadicional TEXT ,latitud TEXT ,longitud TEXT, fototransferir int,ubicaciontransferir int ,clienteactualizado int, ubicacionenmapa TEXT )");
			}catch(Exception e){
					Toast.makeText(getApplicationContext(),
					"Error al crear la tabla de Clientes", Toast.LENGTH_SHORT).show();					e.printStackTrace();
			}


		try{

			 String sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional,ubicacionenmapa   FROM clientes WHERE codvendedor=29 )";
//			 String sql ="SELECT codcliente,numcliente,nombre,nif,descuentos,codsitua,diasadicional   FROM clientes)";

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


	    	 for (int i = 0; i <count; i++)
	    	 {
	    		 SoapObject test = (SoapObject) vectordeClientes.get(i);
	    		 String Stringnombre = (String) test.getProperty("nombre");
	    		 String Stringcodcliente = (String) test.getProperty("codcliente");    // se recibe como string
	    		 String Stringnumcliente = (String) test.getProperty("numcliente");    // se recibe como string
	    		 String Strinnif = (String) test.getProperty("nif");    // se recibe como string
	    		 String Stringdescuentos = (String) test.getProperty("descuentos");    // se recibe como string
	    		 String Stringcodsitua = (String) test.getProperty("codsitua");    // se recibe como string
	    		 String diasadicional = (String) test.getProperty("diasadicional");    // se recibe como string
	    		 String ubicacionenmapa = (String) test.getProperty("ubicacionenmapa");    // se recibe como string

	    		 String latitudcliente="0";
	    		 String longitudcliente="0";

	    		 int codcliente = Integer.parseInt(Stringcodcliente); // se convierte a integer
	    		 int fototransferir =0;
	    		 int ubicaciontransferir=0;
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
						nuevoRegistro.put("longitud",longitudcliente);
						nuevoRegistro.put("fototransferir",fototransferir);
						nuevoRegistro.put("ubicaciontransferir",ubicaciontransferir);
						nuevoRegistro.put("clienteactualizado",0);
						nuevoRegistro.put("ubicacionenmapa",ubicacionenmapa);

						//****
						try {
							db.insert("Clientes", null, nuevoRegistro);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"Error al Insertar un cliente", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
						db.close();
					}


	    	 }
	    	    Toast.makeText(getApplicationContext(),
	    				"Tabla de Clientes sincronizada!", Toast.LENGTH_SHORT)
	    				.show();

		}catch (Exception e){
		    Toast.makeText(getApplicationContext(),
    				"Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
    				.show();
		}

		//************
		}
				      public void SincronizarSaldos(View view){
    	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Re-Crear la Tabla de Saldos?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   sincronizar_saldos();
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
      public void SincronizarNotasDeCredito(View view){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Re-Crear la Tabla de Notas de Credito?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   sincronizar_notas();
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
		public void SincronizarPedidos(View view){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Re-Crear la Tabla de Pedidos?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   sincronizar_pedidos();
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




		public void SincronizarPresupuestos(View view){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Re-Crear la Tabla de Presupuestos?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   sincronizar_presupuestos();
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














				public void sincronizar_notas(){

			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Notas");
				db55.execSQL("CREATE TABLE Notas (_id INTEGER PRIMARY KEY AUTOINCREMENT,   coddevolucion INTEGER ,numdevolucion TEXT ,totaldevolucion INTEGER ,fechadevolucion TEXT, texto TEXT,codcliente INTEGER)");

				Toast.makeText(getApplicationContext(),
						"Tabla de Notas Creada!", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Notas", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
				}







		}
		public void sincronizar_saldos(){

			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Saldos");
				db55.execSQL("CREATE TABLE Saldos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codventa INTEGER, saldocuota INTEGER , numventa TEXT,texto TEXT, vencimiento TEXT,yadescontado INTEGER,porcentaje INTEGER,numcliente TEXT,codcliente INTEGER )");

				db55.execSQL("DROP TABLE IF EXISTS AuxSaldos");
				db55.execSQL("CREATE TABLE AuxSaldos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codventa INTEGER, saldocuota INTEGER , numventa TEXT,texto TEXT, vencimiento TEXT,yadescontado INTEGER,porcentaje INTEGER,numcliente TEXT,codcliente INTEGER )");




				Toast.makeText(getApplicationContext(),
						"Tabla de Saldos Creada! ", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Saldos", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
			}






		}
		public void sincronizar_pedidos(){

			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Pedidos");
				db55.execSQL("CREATE TABLE Pedidos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codarticulo INTEGER ,descripcion  TEXT ,codigobarras TEXT, precio_pvp INTEGER,cantidad INTEGER,codcliente INTEGER,numcliente TEXT,nombre TEXT,codvendedor INTEGER,desvendedor TEXT,tipoventa INTEGER,enviado INT,idpedido INTEGER   ,numeropresupuesto INTEGER,numerofactura TEXT,cerrado INT,observaciones TEXT ,numventa TEXT, situacion TEXT,faltantes TEXT,serviciodeentrega TEXT,horacierre TEXT,horaenvio TEXT ,latitudpedido TEXT,longitudpedido TEXT)");
				Toast.makeText(getApplicationContext(),
				"Tabla de Pedidos Creada!", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Pedidos", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
			}



			// tambien creamos tabla de Ubicaciones

			try{
				UtilidadesSQL sql551 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db551 = sql551.getWritableDatabase();
				db551.execSQL("DROP TABLE IF EXISTS Ubicacion");
				db551.execSQL("CREATE TABLE Ubicacion (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitud Text ,Longitud TEXT ,fecha text, hora text,usuario INTEGER,transferido INTEGER ,randomico TEXT)");
				Toast.makeText(getApplicationContext(),
				"Tabla de Ubicaciones Creada!", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Ubicaciones", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
			}




			// Tambien creamos tabla de Visitas

			try{
				UtilidadesSQL sql551 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db551 = sql551.getWritableDatabase();
				db551.execSQL("DROP TABLE IF EXISTS Visitas");
				db551.execSQL("CREATE TABLE Visitas (_id INTEGER PRIMARY KEY AUTOINCREMENT,codvendedor INTEGER, random TEXT, codcliente INTEGER,diainicio TEXT ,horainicio text, diafin  text,horafin text,latitudinicio text,longitudinicio text,latitudfin text,longitudfin text, latitudcliente text,longitudcliente text,tipo INTEGER, estado INTEGER, transferido INTEGER)");
				Toast.makeText(getApplicationContext(),
				"Tabla de Visitas Creada!", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Visitas", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
			}


			/*

			try{
	        	   UtilidadesSQL sql= new UtilidadesSQL(getApplicationContext(),
							"DBUsuarios", null, versiondb);
							final SQLiteDatabase db = sql.getWritableDatabase();
							db.execSQL("update Visitas   set  transferido=0");
							Toast.makeText(getApplicationContext(),
									"El Recibo N� Fue Anulado", Toast.LENGTH_LONG)
									.show();
	           }catch(Exception e ){
	   			Toast.makeText(getApplicationContext(),
	   					"Problemas para anular el recibo", Toast.LENGTH_LONG)
	   					.show();}

			*/

			// tambien creamos la tabla de transportadoras




}

















public void sincronizar_presupuestos(){

			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Presupuestos");
				db55.execSQL("CREATE TABLE Presupuestos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codarticulo INTEGER ,descripcion  TEXT ,codigobarras TEXT, precio_pvp INTEGER,cantidad INTEGER,codcliente INTEGER,numcliente TEXT,nombre TEXT,codvendedor INTEGER,desvendedor TEXT,tipoventa INTEGER,enviado INT,idPresupuesto INTEGER   ,numeropresupuesto INTEGER,numerofactura TEXT,cerrado INT,observaciones TEXT ,numventa TEXT, situacion TEXT,faltantes TEXT,serviciodeentrega TEXT,horacierre TEXT,horaenvio TEXT ,latitudpedido TEXT,longitudpedido TEXT ,correodestino TEXT,preciodistribuidor INTEGER)");
				Toast.makeText(getApplicationContext(),
				"Tabla de Pedidos Creada!", Toast.LENGTH_LONG).show();
			}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Presupuestos", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
			}






}
		public void SincronizarAfiliados(View view){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Sincronizar los Afiliados?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //sincronizar_productos();
			        	  //sincronizar_afiliados_thread();

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

		public void SincronizarProductos(View view){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Desea Sincronizar los Productos?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   //sincronizar_productos();
			        	  sincronizar_productos_thread();
			        	   //desmarcar el de arriba
			        	  /*
			        		try{
			        	   UtilidadesSQL sql= new UtilidadesSQL(getApplicationContext(),
									"DBUsuarios", null, versiondb);
									final SQLiteDatabase db = sql.getWritableDatabase();
									db.execSQL("update Pedidos  set  codcliente=4298, numcliente=4358 ,nombre='LAS MERCEDES' where  idpedido=687");
									Toast.makeText(getApplicationContext(),
											"El Recibo N� Fue Anulado", Toast.LENGTH_LONG)
											.show();
			           }catch(Exception e ){
			   			Toast.makeText(getApplicationContext(),
			   					"Problemas para anular el recibo", Toast.LENGTH_LONG)
			   					.show();
			   		}	*/

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
		public void sincronizar_productos(){


			try{
				UtilidadesSQL sql55 = new UtilidadesSQL(getApplicationContext(),
				"DBUsuarios", null,versiondb);
				final SQLiteDatabase db55 = sql55.getWritableDatabase();
				db55.execSQL("DROP TABLE IF EXISTS Articulos");
				db55.execSQL("CREATE TABLE Articulos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codarticulo INTEGER ,descripcion  TEXT ,codigobarras TEXT, precio_pvp INTEGER)");
				}catch(Exception e){
						Toast.makeText(getApplicationContext(),
						"Error al crear la tabla de Articulos", Toast.LENGTH_SHORT).show();					e.printStackTrace();
				}


			try{

				 SoapObject request = new SoapObject(namespace, Metodosincronizarproductos);
		    	 SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    	 envelope.dotNet = true;
		    	 envelope.setOutputSoapObject(request);
		    	 HttpTransportSE transporte = new HttpTransportSE(url);

		    	 transporte.call(accionSoapsincronizarproductos, envelope);

		    	 SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
		    	 Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
		    	 int count = vectordeusuarios.size(); // contiene la cantidad de registros(objetos usuarios) devueltos

		    	 Toast.makeText(getApplicationContext(),
		    				"registros="+count, Toast.LENGTH_LONG).show();

		    	 for (int i = 0; i <count; i++)
		    	 {
		    		 String Stringcodarticulo = "";
		    		 String Stringprecio_pvp ="";
		    		 String  Stringdescripcion ="";
		    		 String  Stringcodigobarras ="";
		    		 try{
		    		     SoapObject test = (SoapObject) vectordeusuarios.get(i);
		    		       Stringcodarticulo = (String) test.getProperty("codarticulo");
		    		      Stringdescripcion = (String) test.getProperty("descripcion");    // se recibe como string
		    		       Stringcodigobarras = (String) test.getProperty("codigobarras");    // se recibe como string
		    		    Stringprecio_pvp = (String) test.getProperty("precio_pvp");    // se recibe como string

		    		 }catch (Exception e){
			 			    Toast.makeText(getApplicationContext(),
			 	    				"NO P�DO LEER", Toast.LENGTH_LONG)
			 	    				.show();
			 		 }

		    		 int codarticulo = Integer.parseInt(Stringcodarticulo); // se convierte a integer
		    		 int precio_pvp = Integer.parseInt(Stringprecio_pvp); // se convierte a integer

		    		UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
								"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();
						if (db != null) {


							ContentValues nuevoRegistro = new ContentValues();
							nuevoRegistro.put("codarticulo",  codarticulo);
							nuevoRegistro.put("descripcion", Stringdescripcion.trim());
							nuevoRegistro.put("codigobarras", Stringcodigobarras.trim());
							nuevoRegistro.put("precio_pvp", precio_pvp);

							try {
								db.insert("Articulos", null, nuevoRegistro);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),
										"Error al Insertar", Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
							db.close();
						}

		    	 }
		    	 Toast.makeText(getApplicationContext(),
		    				"Actualizado con ="+count+" Articulos", Toast.LENGTH_SHORT)
		    				.show();

			}catch (Exception e){
			    Toast.makeText(getApplicationContext(),
	    				"1-Problemas para conectar a la Base de Datos", Toast.LENGTH_SHORT)
	    				.show();
			    		e.printStackTrace();
			}


		}
}