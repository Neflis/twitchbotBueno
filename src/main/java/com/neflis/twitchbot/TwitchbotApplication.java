package com.neflis.twitchbot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.kraken.domain.KrakenUserList;

@SpringBootApplication
public class TwitchbotApplication {

	static Properties prop = new Properties();
	//static String cuenta = "neflisBot";
	//static String canal = "neflis";
	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.print("Nombre del canal a conectar: ");
		String canal = sc.nextLine();
		System.out.print("Usuario a conectar (neflis, neflisBot): ");
		String cuenta = sc.nextLine();
		
		try {
			prop.load(TwitchbotApplication.class.getResourceAsStream("/twitch.properties"));
		} catch(IOException e) {
			System.out.println(e.toString());
		}

		String username = prop.getProperty("twitch."+cuenta+".username");
		String token = prop.getProperty("twitch."+cuenta+".token");
		String refreshToken = prop.getProperty("twitch"+cuenta+".refreshtoken");
		/*String appClientId = prop.getProperty("twitch.app.clientid");
		String appSecreto = prop.getProperty("twitch.app.secreto");*/
		//OAuth2Credential credential = new OAuth2Credential("twitch", token);
		
		OAuth2Credential credential = new OAuth2Credential("twitch", token, refreshToken, null, username, null, null);
		
		TwitchClient twitchClient = initTwitchClient(credential);

		KrakenUserList misDatos = twitchClient.getKraken().getUsersByLogin(Arrays.asList(username)).execute();
		KrakenUserList datosDelCanal = twitchClient.getKraken().getUsersByLogin(Arrays.asList(canal)).execute();
		String myId = misDatos.getUsers().get(0).getId();
		String idCanal = datosDelCanal.getUsers().get(0).getId();
		
		twitchClient.getChat().leaveChannel(username);
		leerChat(twitchClient, canal);
	}

	private static void leerChat(TwitchClient twitchClient, String canal) {
		System.out.println("leemos el chat");
    	twitchClient.getChat().joinChannel(canal);
    	twitchClient.getChat().connect();
    	
    	SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
    	
    	eventHandler.onEvent(ChannelMessageEvent.class, event -> {
        	String nombre = event.getUser().getName();
        	String mensaje = event.getMessage();
        	if(mensaje.contains("https://clck")) {
        		twitchClient.getChat().ban(canal, nombre, mensaje + " / Posible bot...");
        	}
    		System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
    	});
		
	}
	
	private static TwitchClient initTwitchClient(OAuth2Credential credential) {
		
		TwitchClient twitchClient = TwitchClientBuilder.builder()
	            .withEnableChat(true)
	            .withEnableHelix(true)
	            .withEnableKraken(true)
	            //.withEnablePubSub(true)
	            .withChatAccount(credential)
	            .withDefaultEventHandler(SimpleEventHandler.class)
	            .build();
		
		return twitchClient;
	}

}