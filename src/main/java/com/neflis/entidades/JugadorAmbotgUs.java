package com.neflis.entidades;

public class JugadorAmbotgUs {

	private String nombre;
	private String rol;
	private String localizacion;
	private boolean vigila;
	private int fuerzas;
	
	public JugadorAmbotgUs(String nombre) {
		this.nombre = nombre;
		this.rol = "ciudadano";
		this.localizacion = "casa";
		this.vigila = false;
		this.fuerzas = 0;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	
	public boolean isVigila() {
		return vigila;
	}

	public void setVigila(boolean vigila) {
		this.vigila = vigila;
	}

	public int getFuerzas() {
		return fuerzas;
	}

	public void setFuerzas(int fuerzas) {
		this.fuerzas = fuerzas;
	}
}
