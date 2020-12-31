package com.neflis.twitchbot;

import java.util.ArrayList;
import java.util.List;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.neflis.entidades.JugadorAmbotgUs;

public class AmbotgUs {

	private String estado;
	private List<JugadorAmbotgUs> jugadores;
	
	public AmbotgUs() {
		this.estado = "inicial";
		this.jugadores = new ArrayList<JugadorAmbotgUs>();
	}
	
	public void init(TwitchClient twitchClient, String canal) {
		unirse(twitchClient, canal);
		if(jugadores.size() < 4) {
			twitchClient.getChat().sendMessage(canal, "No se puede iniciar la partida con tan pocos jugadores");
		}else {
			repartirJugadores();
		}
	}
		
	public void unirse(TwitchClient twitchClient, String canal) {
    			
    	if(!twitchClient.getChat().isChannelJoined(canal)) {
    		twitchClient.getChat().joinChannel(canal);
    	}

		System.out.println("inicio");
		IDisposable eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
        	boolean noexiste = true;
			String nombre = event.getUser().getName();
        	String mensaje = event.getMessage();
        	if(mensaje.equals("!unirse")) {
        		for(int i = 0;i<jugadores.size();i++) {
        			if(jugadores.get(i).getNombre().equals(nombre)) {
        				noexiste = false;
        				break;
        			}
        		}
        		
        		if(noexiste) {
	        		jugadores.add(new JugadorAmbotgUs(nombre));
	        		twitchClient.getChat().sendMessage(canal, nombre + " se ha unido a la partida");
        		}else {
        			twitchClient.getChat().sendMessage(canal, nombre + " ya estas unido");
        		}
        	}
    	});
    	try {
    		twitchClient.getChat().sendMessage(canal, "Empieza la partida, unanse a la cola para jugar usando\n '!unirse' (tiempo 1 minuto)");
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		twitchClient.getChat().sendMessage(canal, "Cerramos las inscripciones.");
		eventHandler.dispose();
	}
	
	public void repartirJugadores() {
		
		for(int i=0;i<(jugadores.size()/10)+1; i++){
			if(jugadores.get(i).getRol().equals("lobo")) {
				i--;
			}else {
				jugadores.get(i).setRol("lobo");
			}
			System.out.print((int)(Math.random()*jugadores.size()) + ", ");
		}
		
	}
	
	
	
	
	
	/**
	 * GETTERS Y SETTERS
	 */
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<JugadorAmbotgUs> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<JugadorAmbotgUs> jugadores) {
		this.jugadores = jugadores;
	}
	
	
	
	
}
