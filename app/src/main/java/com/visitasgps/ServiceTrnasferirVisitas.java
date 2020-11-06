package com.visitasgps;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceTrnasferirVisitas extends Service {
public int idvisitasenhilo,codclienteenhilovisita;
public String randonenhilovisitas="";
public String cadenainsertarvisita;
public String cadenainsertarfoto="";
public String codclientetransferirfotoenhilo="";
public String latitudenhilocliente="";
	public String longitudenhilocliente="";;
	public int  codclienteenhilocliente=0;
	  public Bitmap bMapenhilo;
	  public int codclientefotoenhilo;
	  
public String	latitudenhiloubicacion="";
public String longitudenhiloubicacion="";
public String fechaenhiloubicacion="";
public String horaenhiloubicacion="";
public int  idubicaionenhilo=0;
public int entero=0;
public int entero_transferir_clientes=0;
public int entero_transferir_visitas=0;
public int  usuarioenhiloubicacion=0;
public String randomicoenhiloubicacion="";

		
		
	    public int idpedidohiloestado=0;		
		public String randomenhiloestado="";
		public int codclienteenhiloestado=0;
	
	
		public int idpedidohiloestadodetalle=0;		
   		public String randomenhiloestadodetalle="";
   		public int codclienteenhiloestadodetalle=0;
   		
		
		
	public int versiondb=1;
	public int presionoenviar =0;
	public int presionopreguntar =0;
	public int idpedidohilo=0;
	public int idpedidohilofactura=0;
	public int codclienteenhilo=0;
	public int codclienteenhilofactura=0;
	
	public String randomenhilo="";
	public String randomenhilofactura="";
	
	
	public int timeout=90000;
	public int cuantasvecesenvieelpedido=0;
	public int verificarquesefueelpedido =0;
	public int publicointpedidoyaenviado=0;
	public int nuevocontrolenviarunasolavez=0;	
	public String CADENAINSERTARENHILO;
	public int enteroenhilo;
    public String Stringcodpedidoaserinsertadoenhilo;
	public  int testBytes =0;
	public String Stringcodvendedor="";
	public String Stringdesusuario="";
	public String Stringcodusuario="";
	public String StringOpcionVizualizar="";
	public String Stringdesvendedor="";
    public String Stringdescobrador="";
	public String Stringcodcobrador="";
	private ImageView img;
	private static final String namespace = "http://mail.amanecer.com.py:88/lservicios";
	private static final String url = "http://mail.amanecer.com.py:88/lservicios.php";
	
	private static final String MetodoControlNumeropedido = "MetodoControlNumeropedido";
	private static final String accionSoapControlNumeropedido = "http://mail.amanecer.com.py:88/lservicios.php/MetodoControlNumeropedido";
	
	
	
	private static final String MetodoUbicacion = "MetodoUbicacion";
	private static final String accionSoapUbicacion = "http://mail.amanecer.com.py:88/lservicios.php/MetodoUbicacion";
	
	
	

	private Timer mTimer = null; 
	 @Override
	 public IBinder onBind(Intent arg0) {
	  return null;
	 }
	 
	 @Override
	 public void onCreate(){
	  super.onCreate();
	  this.mTimer = new Timer();
	  this.mTimer.scheduleAtFixedRate(
	    new TimerTask(){
	     public void run() {
	      ejecutarTarea();
	     }      
	    }
	    , 0, 1000 * 60);
	 }
	
	
	 private void ejecutarTarea(){
	   	 
		     versiondb=1;
			 presionoenviar =0;
			 presionopreguntar =0;
			 idpedidohilo=0;
			 timeout=90000;
			 cuantasvecesenvieelpedido=0;
			 verificarquesefueelpedido =0;
			 publicointpedidoyaenviado=0;
			 nuevocontrolenviarunasolavez=0;	
			 CADENAINSERTARENHILO="";
			 enteroenhilo=0;
		     Stringcodpedidoaserinsertadoenhilo="";
			 testBytes =0;
			 Stringcodvendedor="";
			 Stringdesusuario="";
			 Stringcodusuario="";
			 StringOpcionVizualizar="";
			 Stringdesvendedor="";
		     Stringdescobrador="";
			 Stringcodcobrador="";		 
		   
		     transferirvisitas(); 
		     
		     
	 }

	 
	 
	 
	 
	 
	 
	 private void transferirvisitas(){
		 
		 UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
					"DBUsuarios", null, 1);
					final SQLiteDatabase db = sql.getWritableDatabase();				
					if (db != null) 
					{						//ubicaciontransferir",ubicaciontransferir				
						String consulta="";					
						//_id INTEGER PRIMARY KEY AUTOINCREMENT,codvendedor INTEGER, random TEXT, codcliente INTEGER,diainicio TEXT ,horainicio text, diafin  text,horafin text,latitudinicio text,longitudinicio text,latitudfin text,longitudfin text, latitudcliente text,longitudcliente text,tipo INTEGER, estado INTEGER, transferido INTEGER
						consulta="SELECT  _id  ,codvendedor, random , codcliente,diainicio ,horainicio,diafin,horafin,latitudinicio,longitudinicio ,latitudfin ,longitudfin ,latitudcliente ,longitudcliente ,tipo,estado  FROM Visitas where (transferido=0 and estado =2) or (transferido=0 and estado =3)";					
					  try{
					      Cursor c = db.rawQuery(consulta, null);					
									if (c.moveToFirst()) {																			
								do {
			       				  	int _id = c.getInt(0);	
			       				  	int codvendedor = c.getInt(1);
			       				  	String random= c.getString(2);
			       				  	int codcliente = c.getInt(3);
			       				  	String diainicio=c.getString(4);
			       				    String horainicio=c.getString(5);			       				 
			       				    String diafin=c.getString(6);
			       				    String horafin=c.getString(7);
			       				    String latitudinicio=c.getString(8);
			       				    String longitudinicio=c.getString(9);
			       				    String latitudfin=c.getString(10);
			       				    String longitudfin =c.getString(11);
			       				    String latitudcliente =c.getString(12);
			       				    String longitudcliente =c.getString(13);
			       				    int tipo=c.getInt(14);
			       				    int estado=c.getInt(15);
			       				 
			       				  
			       				     transferir_visitas_vendedores(_id,codvendedor,random,codcliente,diainicio,horainicio,diafin,horafin,latitudinicio,longitudinicio,latitudfin,longitudfin,latitudcliente,longitudcliente,tipo,estado);			       				     
								} while(c.moveToNext());									
									}
					  }catch(Exception e){
					  	System.out.println("Error "+e);
					  }
					}	
		 
		 
	 }
	 
	 
	 
	 private void transferir_visitas_vendedores(int idtelefono, int codvendedor,String random, int codcliente, String diainicio, String horainicio,String diafin, String horafin, String latitudinicio, String longitudinicio, String  latitudfin,String  longitudfin, String  latitudcliente,String  longitudcliente,  int tipo, int estado){
		 
		 idvisitasenhilo=idtelefono;
		 codclienteenhilovisita=codcliente;
		 randonenhilovisitas=random;
		 
		 
		 cadenainsertarvisita ="Insert into marcaciones (codvendedor,codcliente,random,idtelefono,diainicio,horainicio,diafin,horafin,latitudinicio,longitudinicio,latitudfin,longitudfin,latitudcliente,longitudcliente,tipo,estado,fecgra) values ("+ codvendedor +", "+ codcliente +",'"+ random.toString().trim() +"'," + idtelefono +",'" + diainicio.toString().trim() +"','"+ horainicio.toString().trim() +"','" + diafin.toString().trim() +"','"+ horafin.toString().trim() +"','"+ latitudinicio.toString().trim() +"','" + longitudinicio.toString().trim() +"','" + latitudfin.toString().trim() +"','"+ longitudfin.toString().trim()+"','" +latitudcliente.toString().trim()+"','"+ longitudcliente.toString().trim()+"'," + tipo +", " + estado +",now())";
		 mihiloactualizarvisitas  hiloactualizarvisitas = new mihiloactualizarvisitas();
		 hiloactualizarvisitas.start();



}
	 class mihiloactualizarvisitas extends Thread{
		 public void run(){				
			try{				
				
			String sql_guardar =cadenainsertarvisita;
			 
			SoapObject respuestaguardarrecibo = new SoapObject(namespace, MetodoUbicacion);
			respuestaguardarrecibo.addProperty("Parametro",sql_guardar);    		
	      	SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        sobre.dotNet = true;
	        sobre.setOutputSoapObject(respuestaguardarrecibo);         
	        HttpTransportSE transporte_guardar = new HttpTransportSE(url);
	        try{
				transporte_guardar.call(accionSoapUbicacion, sobre);   
				Object resultado_guardar = (Object)sobre.getResponse();
				String resultadodevuelto=resultado_guardar.toString();
				entero_transferir_visitas=0;
				entero_transferir_visitas = Integer.parseInt(resultadodevuelto);	
				
				if (entero_transferir_visitas>1) {
				
	    		UtilidadesSQL sql2 = new UtilidadesSQL(getApplicationContext(),
	 					"DBUsuarios", null, versiondb);
	 					final SQLiteDatabase db2 = sql2.getWritableDatabase();				
	 					if (db2 != null) 
	 					{									 							 					
	 						db2.execSQL("update Visitas  set transferido =1 where  _id="+ idvisitasenhilo+" and codcliente="+ codclienteenhilovisita+" and random= '" + randonenhilovisitas.toString().trim() + "';");
	 						entero_transferir_visitas=0;
	 					}
	    
	 					
				}
	        }catch (Exception e){ 
	        	entero_transferir_visitas=0;
	        }	
	
			  }catch (Exception e){ 
				  entero_transferir_visitas=0;
		 		}	

		 
	 }
	
	 }

		  

} 
