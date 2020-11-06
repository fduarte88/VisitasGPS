package com.visitasgps;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class DemoLocationListener implements LocationListener {

	private TextView latitud;
	private TextView longitud;

	public DemoLocationListener(TextView latitud, TextView longitud)
	{
		super();
		this.latitud = latitud;
		this.longitud = longitud;
		
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		arg0.getLatitude();
		arg0.getLongitude();
		this.setLatitud(Double.toString(arg0.getLatitude()));
		this.setLongitud(Double.toString(arg0.getLongitude()));
	}

	@Override
	public void onProviderDisabled(String provider) {
		this.setLatitud(""); //GPS Deshabilitado
		this.setLongitud(""); //GPS Deshabilitado
	}

	@Override
	public void onProviderEnabled(String provider) {
		this.setLatitud("");//GPS Habilitado
		this.setLongitud("");//GPS Habilitado

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	
	private void setLatitud(String str){
		this.latitud.setText(str);  //("Latitud: " + str)
	}
	
	private void setLongitud(String str){
		this.longitud.setText(str); //("Longitud: " + str)
	}

}
