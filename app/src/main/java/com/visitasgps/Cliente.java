package com.visitasgps;

public class Cliente {
	private String Nombre = "";
	private String Numcliente = "";
	private String Codcliente = "";
	
	public String getnNombre() {
		return Nombre;
	}

	public String getNumcliente() {
		return Numcliente;
	}

	public String getCodcliente() {
		return Codcliente;
	}
	
	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public void setNumcliente(String numcliente) {
		Numcliente = numcliente;
	}
	public void setCodcliente(String codcliente) {
		Codcliente = codcliente;
	}
}