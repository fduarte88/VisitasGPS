package com.visitasgps;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import android.R.attr;
import android.R.integer;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import android.widget.ImageButton;

import com.example.visitasgps.R;

public class OpcionPedidosActivity extends Activity {
	public  String usuario ="";
	public String password ="";
	public String codusuario ="";
	public String resultadodevuelto ="";	
	EditText txusuario;
	EditText txpassword;	
	public int versiondb=1;
	
	 private final static String ETIQUETA_ERROR = "ERROR";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceso); // acceso para pedidos
		txusuario = (EditText) findViewById(R.id.txtNombre);
		txpassword = (EditText) findViewById(R.id.txtClave);
		
		txusuario.requestFocus();
		
		
		
}
	public void Acceder(View view){
		try{
			usuario =txusuario.getText().toString();						  				 
		}catch(Exception e){
			Toast.makeText(getApplicationContext(),
					"Ingrese el Usuario", Toast.LENGTH_SHORT)
					.show();
		}
		try{
			password = txpassword .getText().toString();						  				 
		}catch(Exception e){
			Toast.makeText(getApplicationContext(),
					"Ingrese el Password", Toast.LENGTH_SHORT)
					.show();
		}
			
		
		
		
		if (usuario.equalsIgnoreCase("++++")){
			// si ingreso ***** asteriscos ingresa directamente a la parte de sincronizacion			
			Intent c = new Intent(this, Sincronizar.class );
			 startActivity(c);
			Toast.makeText(getApplicationContext(), "Ingresando a la sección de Sincronización!",
					Toast.LENGTH_SHORT).show();

			
		}else{
						UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
						"DBUsuarios", null, versiondb);
						final SQLiteDatabase db = sql.getWritableDatabase();
					
						if (db != null) {				  
							String consulta="SELECT * from Usuarios where desusuario='"+usuario+"' AND PASSUSUARIO='"+password+"'";
							try {
								
								
							Cursor c = db.rawQuery(consulta, null);						
							if (c.moveToFirst()) {
							
							 Toast.makeText(getApplicationContext(), "Acceso Correcto",
											Toast.LENGTH_SHORT).show();

							
							 String Stringdesusuario =c.getString(1);
							 String Stringcodusuario=c.getString(3);							 
							 String Stringcodvendedor=c.getString(4);
							 String Stringcodcobrador=c.getString(5);
							 String  Stringdescobrador=c.getString(6);							
							 String Stringdesvendedor=c.getString(7);
							
							 Intent p = new Intent(this, MainActivity.class );
							 
							  p.putExtra("Stringcodusuario",Stringcodusuario);     
							  p.putExtra("Stringdesusuario",Stringdesusuario);							  
							  p.putExtra("Stringcodvendedor",Stringcodvendedor);							 
							  p.putExtra("Stringcodcobrador",Stringcodcobrador);
							  p.putExtra("Stringdescobrador",Stringdescobrador);
							  p.putExtra("Stringdesvendedor",Stringdesvendedor);
							 
							  Log.e("ERROR", "seguimiento 0");
							  startActivity(p);		
							
							 
							  Log.e("ERROR", "seguimiento 1");
							  finish();
							
								} else {
								
									
									
									Toast.makeText(getApplicationContext(),
											"Usuario o Clave no Valida ", Toast.LENGTH_SHORT)
											.show();
								
								
							
								
								
								
								
								
								}
							} catch (Exception e) {
								
								
								
								
								
								
									
								
							
								Toast.makeText(getApplicationContext(),
										"Error al acceder a la DB"+e.getMessage(), Toast.LENGTH_SHORT)
										.show();
								e.printStackTrace();
								
							
							
								
							}
						}			
		}
	}
	public void Cerrar(View view){
		 finish();
	}
}