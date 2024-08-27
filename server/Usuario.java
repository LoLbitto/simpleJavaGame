package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.Color;

import jogo.*;
import mapa.*;
import entidades.*;

public class Usuario{
	private int ip;
	private int iden;
	private Socket s;
	private Jogador jogador;
	private BufferedReader br;
	private EnviarMsg env;
	private PrintWriter pw;

	private int numSala = 0;

	private ReceberDado rec;

	private Salas salaAtual;

	private List<Usuario> lista;

	class EnviarMsg extends Thread {
		private int iden;
		private List<Usuario> lista;
		private BufferedReader br;

		public EnviarMsg(BufferedReader br, int iden, List<Usuario> lista){
			this.iden = iden;
			this.br = br;
			this.lista = lista;
		}

		@Override
		public void run(){
			while(true){
				try{
					for(Usuario e : lista){
						if(e.getIden() != iden && e.getPlayer() != null){
							String info = "user," + e.getIden() + "," + e.getPlayer().getX() + "," + e.getPlayer().getY() + "," + e.getPlayer().getFace0() + "," + e.getPlayer().getFace1();
							if (e.getSalaAtual() == salaAtual){
								info += "," + "visivel";
							} else {
								info += "," + "invisivel";
							}
							enviar(info);
						}
						if (!e.getSocket().isConnected()){
							e.stop();
						}
					}
					if (salaAtual != null){
						if(salaAtual.getInimigos() != null){
							String info = "inimigos";
							for (Inimigo i : salaAtual.getInimigos()){
								info += "," + i.getX() + "," + i.getY() + "," + i.get0() + "," + i.get1();
							}
							enviar(info);
						}
					}
				}catch (ConcurrentModificationException e){
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
					lista.remove(iden);
					break;
				}
				try{
					Thread.sleep(30/Constantes.FPS);
				} catch (InterruptedException e) {
                	e.printStackTrace();
            	}
			}
		}

		public void enviaSala(){
			String sala = "sala," + salaAtual.getPassado() + "," + salaAtual.getProx();
			if (salaAtual.getInimigos() != null){
				sala += "," + salaAtual.getInimigos().length;
				for (Inimigo i : salaAtual.getInimigos()){
					sala += "," + i.getX() + "," + i.getY();
				}
			} else {
				sala += "," + 0;
			}
			enviar(sala);
		}
	}

	public class ReceberDado extends Thread{
		private BufferedReader recebedor;
		private String[] dados;
		private EnviarMsg enviador;


		public ReceberDado(Socket s, EnviarMsg enviador){
			try{
				recebedor = new BufferedReader(new InputStreamReader(s.getInputStream()));
			}catch (Exception e){}
			this.enviador = enviador;
		}
		@Override
		public void run(){
			while(true){
				try{
					dados = (recebedor.readLine() + ", ").split(",");
				} catch (Exception e){}
				if (dados[0].equals("jogador")){ 
					 //[1] e [2] x e y
					Color cor;
					if (dados[5].equals("white")){
						cor = Color.white;
					} else {
						cor = Color.red;
					}
					if (jogador == null){
						jogador = new Jogador();
					}
					jogador.mudaTudo(Integer.parseInt(dados[1]), Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), Integer.parseInt(dados[4]));
					jogador.setCor(cor);
				}
				if (dados[0].equals("avanca")){
					numSala++;
					salaAtual = ThreadUp.avancaSala(numSala, iden);
					enviador.enviaSala();
				} 
				if (dados[0].equals("volta")){
					numSala--;
					salaAtual = ThreadUp.voltaSala(numSala, iden);
					enviador.enviaSala();
				} 
				if(dados[0].equals("mudalevel")){
					for(Usuario e : lista){
						e.enviar("somalevel");
					}
					ThreadUp.mudaLevel();
				}
				try{
					Thread.sleep(60/Constantes.FPS);
				} catch (InterruptedException e) {
                	e.printStackTrace();
            	}
			}
		}
	}

	public Usuario(ServerSocket ss, int iden, List<Usuario> lista, Salas salaAtual){
		this.iden = iden;
		this.lista = lista;
		this.salaAtual = salaAtual;
		try{
			s = ss.accept();

			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception e){
			System.out.println("ué");
		}
		env = new EnviarMsg(br, iden, lista);
		rec = new ReceberDado(s, env);
		try{
			pw = new PrintWriter(s.getOutputStream(), true);
		} catch(Exception e){
			e.printStackTrace();
		}
		enviar("" + iden);
		env.start();
		rec.start();
		env.enviaSala();
	}

	public int getIden(){
		return iden;
	}

	public void enviar(String mensagem){
		pw.println(mensagem);
	}

	public void enviarPassagem(String mensagem, int sala){
		if (sala == numSala){
			pw.println(mensagem);
		} else {
			pw.println("podepassar");
		}
	}

	public Socket getSocket(){
		return s;
	}

	public Jogador getPlayer(){
		return jogador;
	}

	public Salas getSalaAtual(){
		return salaAtual;
	}

	public void setSalaAtual(Salas sala){
		numSala = 0;
		salaAtual = sala;
		env.enviaSala();
	}

	public void stop(){
		env.stop();
		rec.stop();
	}
}