package server;

import jogo.*;
import mapa.*;
import entidades.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class ThreadUsers extends Thread{
	private int users;
	private static List<Usuario> lista;
	private ServerSocket server;

	private static Salas salaInicial;

	public ThreadUsers(List<Usuario> lista, ServerSocket server){
		this.lista = lista;
		this.server = server;
		users = 0;
	}

	@Override
	public void run(){
		while (true){
			try{
				add();
			}
			catch(IOException e){
				System.out.println("ué");
			}
		}
	}

	private void add() throws IOException{
		lista.add(new Usuario(server, users, lista, salaInicial));
		users++;
		System.out.println("System joined.");
	}

	public static void updSala(Salas sala){
		salaInicial = sala;
		if (lista != null){
			for (Usuario e : lista){
				e.setSalaAtual(salaInicial);
			}
		}
	}

	public static List<Usuario> getLista(){
		return lista;
	}
}