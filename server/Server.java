package server;

import jogo.*;
import mapa.*;
import entidades.*;

import java.net.*;
import java.io.*;
import java.util.*;
import jogo.*;

public class Server{
	static ThreadUsers a;
	public static void main(String[] args) throws IOException{
		List<Usuario> lista = new ArrayList<Usuario>();
		ServerSocket ss = new ServerSocket(1212);

		ThreadUp threadJogo = new ThreadUp(true); 

		a = new ThreadUsers(lista, ss);
		a.start();
		threadJogo.start();
	}
}