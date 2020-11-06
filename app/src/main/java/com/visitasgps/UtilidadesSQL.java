package com.visitasgps;

import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UtilidadesSQL extends SQLiteOpenHelper {
	private SQLiteDatabase myDataBase; 
	public int versiondb=1;
	private static final String TABLE_LABELS = "Clientes";
	String SQL_CrearTabla_Usuarios = "CREATE TABLE Usuarios (_id INTEGER PRIMARY KEY AUTOINCREMENT, desusuario TEXT, passusuario TEXT,codusuario INTEGER,codvendedor INTEGER)";
	String SQL_CrearTabla_Clientes = "CREATE TABLE Clientes (_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT ,numcliente  TEXT ,codcliente int,nif TEXT,descuentos TEXT,codsitua TEXT,diasadicional TEXT)";
	String SQL_CrearTabla_Articulos = "CREATE TABLE Articulos (_id INTEGER PRIMARY KEY AUTOINCREMENT, codarticulo INTEGER ,descripcion  TEXT ,codigobarras TEXT, precio_pvp INTEGER)";
	String SQL_CrearTabla_Bancos ="CREATE TABLE Bancos (_id INTEGER PRIMARY KEY AUTOINCREMENT, desbanco TEXT, codbanco INTEGER)";
	String SQL_CrearTabla_FacturaCobrar = "CREATE TABLE facturacobrar (_id INTEGER PRIMARY KEY AUTOINCREMENT, codcliente integer,numcliente TEXT,codventa integer,fechavcto date ,saldocuota integer,importecuota integer ,numventa TEXT,yadescontado integer,porcentaje REAL )";
	
	String SQL_CrearTabla_Formas ="CREATE TABLE Formas (_id INTEGER PRIMARY KEY AUTOINCREMENT, desforcobro TEXT, codforcobro INTEGER)"; 
	
	public UtilidadesSQL(Context contexto, String nombre_db, CursorFactory factory, int version) {

		super(contexto, nombre_db, factory, version);
		// TODO Auto-generated constructor stub
	}

	public UtilidadesSQL(Context context) {
		super(context, "CursorDemo", null, 1);
	}

	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(SQL_CrearTabla_Usuarios);
		db.execSQL(SQL_CrearTabla_Clientes);
		db.execSQL(SQL_CrearTabla_Articulos);
		db.execSQL(SQL_CrearTabla_Bancos);
		db.execSQL(SQL_CrearTabla_Formas);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Usuarios");
		db.execSQL(SQL_CrearTabla_Usuarios);
		
		db.execSQL("DROP TABLE IF EXISTS Clientes");
		db.execSQL(SQL_CrearTabla_Clientes);
		
		
		db.execSQL("DROP TABLE IF EXISTS Articulos");
		db.execSQL(SQL_CrearTabla_Articulos);
		
		

		db.execSQL("DROP TABLE IF EXISTS Bancos");
		db.execSQL(SQL_CrearTabla_Bancos);
		
		db.execSQL("DROP TABLE IF EXISTS Formas");
		db.execSQL(SQL_CrearTabla_Formas);
		
		
	}
	
	 public Cursor getNombres(){
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor respuesta = db .rawQuery("select nombre from Clientes", null);

	        return respuesta;

	    }
	 
   

	 public List<String> getAllLabels(String nombre){		    
	    	List<String> labels = new ArrayList<String>();	    		      
	    	String Consultaaux = "SELECT nombre as otro,nombre,numcliente FROM Clientes where nombre like '%" ;	  
	    	String Consulta = Consultaaux+nombre+"%'";	    		        	     
	    	String selectQuery = Consulta ;
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);	     
	        if (cursor.moveToFirst()) {
	            do {
	            	labels.add(cursor.getString(1));
	            } while (cursor.moveToNext());
	        }
	        	        cursor.close();
	        db.close();	    	
	    	return labels;
	    }
	 
	 
	 
	 
	 
		private final String TABLE_CLIENTES = "Clientes";
		private final String TABLE_KEY_ID = "_id";
		private final String TABLE_KEY_CODCLIENTE = "codcliente";
		private final String TABLE_KEY_MOMBRE = "nombre";
		private final String TABLE_KEY_NUMCLIENTE = "numcliente";
	 
	
	public ArrayList<Cliente> LeerClientes() {
			String nombre ="ferre";
			List<Cliente> labels = new ArrayList<Cliente>();
			String Consultaaux = "SELECT nombre as otro,nombre,numcliente FROM Clientes where nombre like '%" ;	  
	    	String Consulta = Consultaaux+nombre+"%'";	    		        	     
	    	String selectQuery = Consulta ;
	    	SQLiteDatabase db = this.getReadableDatabase();
	        Cursor c = db.rawQuery(selectQuery, null);	 
	        
	     	ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();	     	
	     	//Cursor c = myDataBase.query(TABLE_CLIENTES, new String[] {TABLE_KEY_ID, TABLE_KEY_MOMBRE , TABLE_KEY_NUMCLIENTE,TABLE_KEY_CODCLIENTE}, 
	     	//		null, null, null, null, null);	     	
	     	//Iteramos a traves de los registros del cursor
	     	c.moveToFirst();
	         while (c.isAfterLast() == false) {
	         	Cliente clientes = new Cliente();	         	
	         	clientes.setNombre(c.getString(1));
	         	clientes.setNumcliente(c.getString(2));
	         	clientes.setCodcliente(c.getString(3));	         	
	         	listaClientes.add(clientes);
	        	    c.moveToNext();
	         }
	         c.close();	         
	         return listaClientes;
	}
	 public void abrirBaseDatos() throws SQLException{
	    //    String myPath = DB_PATH + DB_NAME;
	    //	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	 
	    }
	 	
	
}
