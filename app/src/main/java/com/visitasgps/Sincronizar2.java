package com.visitasgps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.visitasgps.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Vector;

public class Sincronizar2 extends Activity {

    private static final String namespace = "http://mail.amanecer.com.py/lservicios/";
    private static final String url = "http://mail.amanecer.com.py:9091/lservicios.php";
    private static final String Metodosincronizarusuarios = "Metodosincronizarusuarios";
    private static final String accionSoapsincronizarusuarios = "http://mail.amanecer.com.py/lservicios.php/Metodosincronizarusuarios";

    int versiondb = 1;
    int pirulito = 0;
    ProgressDialog progressDialog;
    Handler puente = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar2);
    }

    public void SincronizarUsuarios99(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea Sincronizar los Usuarios del Sistema?....")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sincronizar_usuarios_thread();
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

    public void sincronizar_usuarios_thread() {

        Toast.makeText(getApplicationContext(),
                "estoy en el hilo de Sincro usr", Toast.LENGTH_SHORT).show();
        MiThread hilo = new MiThread();
        hilo.start();
    }

    class MiThread extends Thread {
        @Override
        public void run() {
            try {
                int bbb;
                bbb = 20;
                Message msg2 = new Message();
                msg2.obj = bbb;
                puente.sendMessage(msg2);

                SoapObject request = new SoapObject(namespace, Metodosincronizarusuarios);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                System.out.println(url);
                HttpTransportSE transporte = new HttpTransportSE(url);
                try {
                    transporte.call(accionSoapsincronizarusuarios, envelope);

                } catch (Exception e) {
                    System.out.println(e);
                    int aa = -99;
                    Message msg = new Message();
                    msg.obj = aa;
                    puente.sendMessage(msg);

                }
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Vector vectordeusuarios = (Vector) resultsRequestSOAP.getProperty("return");
                int count = vectordeusuarios.size();
                UtilidadesSQL sql3 = new UtilidadesSQL(getApplicationContext(),
                        "DBUsuarios", null, versiondb);
                final SQLiteDatabase db3 = sql3.getWritableDatabase();

                try {
                    db3.execSQL("delete from  Usuarios");
                    db3.execSQL("DROP TABLE IF EXISTS Usuarios");

                } catch (Exception e) {
                    int aa = -99;
                    Message msg = new Message();
                    msg.obj = aa;
                    puente.sendMessage(msg);

                }
                try {
                    db3.execSQL("CREATE TABLE Usuarios (_id INTEGER PRIMARY KEY AUTOINCREMENT, desusuario TEXT, passusuario TEXT,codusuario INTEGER,codvendedor INTEGER,codcobrador INTEGER,desvendedor TEXT,descobradror TEXT, usuarioactual INTEGER)");
                } catch (Exception e) {
                    int aa = -99;
                    Message msg = new Message();
                    msg.obj = aa;
                    puente.sendMessage(msg);
                }
                for (int i = 0; i < count; i++) {


                    UtilidadesSQL sql = new UtilidadesSQL(getApplicationContext(),
                            "DBUsuarios", null, versiondb);
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
                    int codvendedor = Integer.parseInt(Stringcodvendedor); // se convierte a integer
                    int codcobrador = Integer.parseInt(Stringcodcobrador); // se convierte a integer
                    if (db != null) {
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("desusuario", Stringdesusuario.trim());
                        nuevoRegistro.put("passusuario", Stringpassusuario.trim());
                        nuevoRegistro.put("codusuario", codusuario);
                        nuevoRegistro.put("codvendedor", codvendedor);
                        nuevoRegistro.put("desvendedor", Stringdesvendedor.trim());
                        nuevoRegistro.put("codcobrador", codcobrador);
                        nuevoRegistro.put("descobradror", Stringdescobrador.trim());
                        nuevoRegistro.put("usuarioactual", 0);

                        try {
                            db.insert("Usuarios", null, nuevoRegistro);
                            pirulito = 1;

                        } catch (Exception e) {
                            int aa = -99;
                            Message msg = new Message();
                            msg.obj = aa;
                            puente.sendMessage(msg);
                        }
                        db.close();
                    }
                }
                for (int a = 10; a < 100; a++) {
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
            }

        }
    }
}

