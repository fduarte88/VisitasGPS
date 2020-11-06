package com.visitasgps;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.visitasgps.R;

public class MainActivity extends Activity {

	public String clientefiltrar="0";
	public Button btnvisita;
	public int versiondb=1;
	public String OpcionVizualizacion="1"; 
	public String[] listaElementos =    {"Pendientes", "Todos", "Cliente"};

	
	
	public String[] listaElementosopciones =    {"Nuevo Presupuesto", "Ver Presupuestos"};
	private final static String ETIQUETA_ERROR = "ERROR";

	String Stringcodvendedor = "";
	String Stringcodcobrador = "";
	String Stringcodusuario = "";
	String Stringdescobrador = "";
	String Stringdesvendedor = "";
	String Stringdesusuario = "";
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.e("ERROR", "seguimiento siguiente 1");
        
        
        
        Bundle bundle = getIntent().getExtras();
        
        
        Log.e("ERROR", "seguimiento  siguiente 2");


		assert bundle != null;
		Stringcodvendedor = bundle.getString("Stringcodvendedor");
		Log.e("ERROR", "seguimiento  siguiente 3");
        
		Stringcodcobrador = bundle.getString("Stringcodcobrador");
		Log.e("ERROR", "seguimiento  siguiente 4");
        
		Stringcodusuario = bundle.getString("Stringcodusuario");
		Log.e("ERROR", "seguimiento  siguiente 5");
        
		Stringdesusuario = bundle.getString("Stringdesusuario");
		Log.e("ERROR", "seguimiento  siguiente 6");
        
		//lbldescobrador.setText(Stringcodcobrador + "-"+ bundle.getString("Stringdescobrador"));
		Log.e("ERROR", "seguimiento  siguiente 7");
        
		//lbldesvendedor.setText(Stringcodvendedor + "-"+ bundle.getString("Stringdesvendedor"));
		Stringdescobrador = bundle.getString("Stringdescobrador");
		Stringdesvendedor = bundle.getString("Stringdesvendedor");
		//lbldesusuario.setText(Stringcodusuario + "-" + Stringdesusuario);
        
		
    	Intent service4 = new Intent(this, ServiceTrnasferirVisitas.class);  // trnasfiere las visitas
		startService(service4);
       
		
		
		
		

		
		
		
    }

//public void   lanzarconfiguraciones(View v){ 
	//Intent c = new Intent(this, Sincronizar.class );
	 //startActivity(c);		
//}

public void   lanzarmapeo(View v){ 
	Intent c = new Intent(this, MainActivityAmasoft.class );
	c.putExtra("Stringcodvendedor",Stringcodvendedor);
	startActivity(c);		
	 
	 
}

public void   lanzaractualizacion(View v){ 
	Intent c = new Intent(this, ActivityActualizarCliente.class );
	c.putExtra("Stringcodvendedor",Stringcodvendedor);
	startActivity(c);		
	 
	 
}


public void   visitar(View v){ 
	Intent c = new Intent(this, ActivityVisitas.class );
	c.putExtra("Stringcodvendedor", Stringcodvendedor);
	 startActivity(c);		
	 
	 
	 
}


public void Cerrar(View v) {
	
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Desea Salir del Sistema Movil?")
	.setCancelable(false)
	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int id) {
    		stopService(new Intent(MainActivity.this, ServiceTrnasferirVisitas.class));
    	   	  finish();   
	  
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




public boolean onKeyDown(int keyCode, KeyEvent event)
{	
if ((keyCode == KeyEvent.KEYCODE_BACK))
{	
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("Desea Salir del  Sistema ?")
					.setCancelable(false)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int id) {
    	              	            	cerrardefinitivamente();
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

		return super.onKeyDown(keyCode, event);
}
public void cerrardefinitivamente(){



AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setMessage("Seguro que Desea Cerrar el Sistema Móvil?")
.setCancelable(false)
.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
	
	finish();
  
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


















}