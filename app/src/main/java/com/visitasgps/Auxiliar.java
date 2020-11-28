package com.visitasgps;

public class Auxiliar {

    /*class mihiloformasdecobro extends Thread {
        public void run() {
            try {
                UtilidadesSQL sql66 = new UtilidadesSQL(getApplicationContext(),
                        "DBUsuarios", null, versiondb);
                final SQLiteDatabase db66 = sql66.getWritableDatabase();
                db66.execSQL("DROP TABLE IF EXISTS Formas");
                db66.execSQL("CREATE TABLE Formas (_id INTEGER PRIMARY KEY AUTOINCREMENT, desforcobro TEXT, codforcobro INTEGER)");
            } catch (Exception e) {
                progreso = -99;
                Message msg = new Message();
                puente.sendMessage(msg);
            }
            try {
                SoapObject request_forma = new SoapObject(namespace, MetodobuscarFormas);
                SoapSerializationEnvelope envelope_formas = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope_formas.dotNet = true;
                envelope_formas.setOutputSoapObject(request_forma);
                HttpTransportSE transporte_formas = new HttpTransportSE(url);
                transporte_formas.call(accionSoapFormas, envelope_formas);

                SoapObject resultsRequestSOAP_formas = (SoapObject) envelope_formas.bodyIn;
                Vector vectordeformas = (Vector) resultsRequestSOAP_formas.getProperty("return");
                int count = vectordeformas.size(); // contiene la cantidad de registros devueltos


                for (int i = 0; i < count; i++) {
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
                            int porcentaje = (i + 1) * 100 / count;
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
                progreso = 100;
                Message msg = new Message();
                puente.sendMessage(msg);

            } catch (Exception e) {
                progreso = 99;
                Message msg = new Message();
                puente.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }*/
}
