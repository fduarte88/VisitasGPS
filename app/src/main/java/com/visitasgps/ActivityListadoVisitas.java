package com.visitasgps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitasgps.R;

public class ActivityListadoVisitas extends Activity {
public String txtDatefecha;
public String stringconsulta="";
TableRow.LayoutParams layoutFila;
TableRow.LayoutParams layoutnumcliente;
TableRow.LayoutParams  layoutnombre;
TableRow.LayoutParams layouttipoventa;
TableRow.LayoutParams layoutenviado;
TableRow.LayoutParams layoutespacio;

TableRow.LayoutParams layoutcerrado;
TableRow.LayoutParams layoutabierto;

TableRow.LayoutParams layoutBoton;
TableRow.LayoutParams layoutfechahora;
TableRow.LayoutParams layoutBotonEnviar; 
TableLayout tablacobros;


@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listado_visitas);
		Bundle bundle=getIntent().getExtras();	       
		txtDatefecha=bundle.getString("txtDatefecha");
		stringconsulta=bundle.getString("stringconsulta");
		
		

		
		
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

    	
 	   //Toast.makeText(getApplicationContext(),
    	//		"Tamaño de Pantalla="+display.getHeight(), Toast.LENGTH_LONG).show();
    	
    	
    	//cargarlistaserviciodeentrega();
    	
    	
		if (display.getHeight() < 500) {
	//	 para pantallas chicas
			
			// 320  son las pantallas pequeñas
			
			
			if (display.getHeight() < 350) {
        tablacobros = (TableLayout)findViewById(R.id.tabla);
        
        //layoutFila = new TableRow.LayoutParams(72, 27);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 100);
        layoutnumcliente= new TableRow.LayoutParams(50,TableRow.LayoutParams.WRAP_CONTENT);
        layoutnombre= new TableRow.LayoutParams(180,TableRow.LayoutParams.WRAP_CONTENT);
        layouttipoventa= new TableRow.LayoutParams(70,TableRow.LayoutParams.WRAP_CONTENT);	      
        layoutenviado= new TableRow.LayoutParams(71,27);
        
        layoutespacio= new TableRow.LayoutParams(10,27);
       	layoutBoton= new TableRow.LayoutParams(72,27); //59,27
        layoutBotonEnviar= new TableRow.LayoutParams(71,27); //59,27
        layoutfechahora = new TableRow.LayoutParams(79,30); //149,70
        layoutcerrado= new TableRow.LayoutParams(72,27);//(110,70);
        layoutabierto= new TableRow.LayoutParams(72,27); //110,70)
        
			}else{
				// para pantallas medianas
			
				tablacobros = (TableLayout)findViewById(R.id.tabla);
		        //layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);		        
				layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,430);
		        layoutnumcliente= new TableRow.LayoutParams(100,TableRow.LayoutParams.WRAP_CONTENT);
		        layoutnombre= new TableRow.LayoutParams(260,TableRow.LayoutParams.WRAP_CONTENT);
		        layouttipoventa= new TableRow.LayoutParams(170,TableRow.LayoutParams.WRAP_CONTENT);		        
		        layoutenviado= new TableRow.LayoutParams(140,100);//(139,70)		       
		        layoutcerrado= new TableRow.LayoutParams(100,100);//(110,70);
		        layoutabierto= new TableRow.LayoutParams(140,100); //110,70)		        
		        layoutBoton= new TableRow.LayoutParams(140,100); //149,70		        
		        layoutfechahora = new TableRow.LayoutParams(80,100); //149,70	        	        
		        layoutBotonEnviar= new TableRow.LayoutParams(140,100); //150,60
		        layoutespacio= new TableRow.LayoutParams(10,27);
			     
			}
		}else{
			
			
			// para pantallas mas grandes
			tablacobros = (TableLayout)findViewById(R.id.tabla);
	        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
	        layoutnumcliente= new TableRow.LayoutParams(100,TableRow.LayoutParams.WRAP_CONTENT);
	        layoutnombre= new TableRow.LayoutParams(350,TableRow.LayoutParams.WRAP_CONTENT);
	        layouttipoventa= new TableRow.LayoutParams(170,TableRow.LayoutParams.WRAP_CONTENT);
	        layoutenviado= new TableRow.LayoutParams(139,70);		       
	        layoutcerrado= new TableRow.LayoutParams(110,70);
	        layoutabierto= new TableRow.LayoutParams(110,70);
	        layoutBoton= new TableRow.LayoutParams(149,70); //59,27
	        layoutBotonEnviar= new TableRow.LayoutParams(150,60); //59,27
	        layoutfechahora = new TableRow.LayoutParams(120,100); //149,70
	        layoutespacio= new TableRow.LayoutParams(10,27);
		}
        
		
		consultar_visitas();
	       
 
 
		
		
	}


	
	public void consultar_visitas(){
		 UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
					"DBUsuarios", null, 1);
					final SQLiteDatabase db = sql.getWritableDatabase();				
					if (db != null) 
					{
				
				
					try{
					      Cursor c = db.rawQuery(stringconsulta, null);					
							if (c.moveToFirst()) {									
										
								do {// _id,codcliente,horainicio,horafin,diainicio 
									
			       				  	         int idvisita = c.getInt(0);			       				      	 
			       				             int intcodcliente =c.getInt(1) ;
			       				             String stringhorainicio =c.getString(2) ;
			       				             String stringhorafin =c.getString(3) ;
			       				             String stringdiainicio =c.getString(4) ;
			       				         
			       				             
			       				             String stringnumcliente =c.getString(5) ;
			       				             String stringnombre =c.getString(6) ;
			       				             
			       				        if  (stringhorafin.toString() == null){
			       				        	stringhorafin ="Cancelado";
			       				        }
			       				        if (stringhorafin.toString().equals("")){
			       				        	stringhorafin ="Cancelado";
			       				        }
			       				             
			       				             if ( ( stringhorafin.toString() != null) && (!stringhorafin.toString().equals("")) ) {
			       				        	  
			       				        	  
			       				          }else{
			       				        	stringhorafin ="Cancelado";
			       				        	  
			       				          }
			       				            


			       				          try{
			       				           insertar_linea(idvisita,intcodcliente,stringhorainicio,stringhorafin , stringdiainicio, stringnumcliente,stringnombre);
			       				               //   insertar_linea(1,123,"stringhorainicio","stringhorafin" , "stringdiainicio", "stringnumcliente","stringnombre");
			       				       }catch(Exception e){
			     						  Toast.makeText(getApplicationContext(),
			     				        				"ccc-Primero Debe Sincronizar los Pedidos, contacte con Dpto. Informatica",
			     				        				Toast.LENGTH_SHORT).show(); 
			     					  } 
			       				         		    	 
			       				       
								} while(c.moveToNext());
											 
										
										
										
									}
					  }catch(Exception e){
						  Toast.makeText(getApplicationContext(),
				        				"bbbb-Primero Debe Sincronizar los Pedidos, contacte con Dpto. Informatica",
				        				Toast.LENGTH_SHORT).show(); 
					  }
					}
		 
		
		
	
}
	
	
public void insertar_linea(int idvisita,int intcodcliente,String stringhorainicio, String stringhorafin , String stringdiainicio, String stringnumcliente, String stringnombre)
	//insertar_linea(1,123,"stringhorainicio","stringhorafin" , "stringdiainicio");
	{
						final String stringhorainiciorecibido = stringhorainicio;
						final String stringhorafinrecibido =stringhorafin ;
						final String stringtxtdiainiciorecibido=stringdiainicio;	
						final String stringidvisitarecibido=String.valueOf(idvisita);
					    final String stringnumclienterecibido=stringnumcliente;
					    final String stringnombrerecibido=stringnombre;
					    final TableRow filacobro;
					
						filacobro = new TableRow(this);				          
						 
						filacobro.setLayoutParams(layoutFila); 
						
						TextView txtnombre;
						TextView txtnumcliente;
						TextView txthorainicio;
				        TextView txthorafin;
				        TextView txtdiainicio;
				        TextView txtcodcliente;
				        TextView txtidbvisita;
				        TextView txtespacio;
				        
				        txthorainicio=new TextView(this);
				        txthorafin=new TextView(this);
				        txtdiainicio=new TextView(this);        
				        txtcodcliente=new TextView(this);
				        txtidbvisita=new TextView(this);
				        
				        txtnumcliente=new TextView(this);
				        txtnombre=new TextView(this);
				        txtespacio=new TextView(this);
				        
				        
				        txtnumcliente.setText(stringnumclienterecibido);
				        txtnumcliente.setGravity(Gravity.LEFT);
				        txtnumcliente.setTextAppearance(this,R.style.etiqueta);				    	
				        txtnumcliente.setLayoutParams(layoutnumcliente);
				     
				     
				        
				        txtnombre.setText(stringnumclienterecibido+"-"+stringnombrerecibido);
				        txtnombre.setGravity(Gravity.LEFT);
				        txtnombre.setTextAppearance(this,R.style.etiqueta);				    	
				        txtnombre.setLayoutParams(layoutnombre);
				     
				        
				        txthorainicio.setText(stringhorainiciorecibido);
				        txthorainicio.setGravity(Gravity.LEFT);
				        txthorainicio.setTextAppearance(this,R.style.etiqueta);				    	
				        txthorainicio.setLayoutParams(layoutcerrado);
				     
				        txthorafin.setText(stringhorafinrecibido);
				        txthorafin.setGravity(Gravity.CENTER);
				        txthorafin.setTextAppearance(this,R.style.etiqueta);				    	
				        txthorafin.setLayoutParams(layoutcerrado);
				      
				        txtdiainicio.setText(stringtxtdiainiciorecibido);
				        txtdiainicio.setGravity(Gravity.CENTER);
				        txtdiainicio.setTextAppearance(this,R.style.etiqueta);				    	
				        txtdiainicio.setLayoutParams(layoutcerrado);
				      
				        
				        
				        
				      
				        txtidbvisita.setText(stringidvisitarecibido);
				        txtidbvisita.setGravity(Gravity.CENTER);
				        txtidbvisita.setTextAppearance(this,R.style.etiqueta);				    	
				        txtidbvisita.setLayoutParams(layoutfechahora);
				      
				        
				        txtespacio.setText(" ");
				        txtespacio.setGravity(Gravity.CENTER);
				        txtespacio.setTextAppearance(this,R.style.etiqueta);				    	
				        txtespacio.setLayoutParams(layoutespacio);
				        
				        filacobro.addView(txtidbvisita);
				        
				        filacobro.addView(txtnombre);
				        filacobro.addView(txtdiainicio);
				        filacobro.addView(txtespacio);
				        filacobro.addView(txthorainicio);
				        filacobro.addView(txthorafin);
				        
				        tablacobros.addView(filacobro);   
		
	}
	
}
