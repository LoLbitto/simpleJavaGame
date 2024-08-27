package jogo;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.lang.*;
import java.awt.Color;

import entidades.*;
import mapa.*;

public class ClienteOnline{
	static int identificacao;
	public static class EnviarDado extends Thread{
		PrintWriter pw;
		public EnviarDado(Socket s){
			try{
				pw = new PrintWriter(s.getOutputStream());
			} catch (Exception e){}
		}

		@Override
		public void run(){
			while (true){
				Jogador temp = PainelDoJogo.getJogador();
				pw.println("jogador," + temp.getX() +"," + temp.getY() + "," + temp.getFace0() + "," + temp.getFace1() + "," + temp.getColor());
				pw.flush();
				try{
					EnviarDado.sleep(60/Constantes.FPS);
				} catch (InterruptedException e) {
                	e.printStackTrace();
            	}
			}
		}

		public void enviaSinal(String sinal){
			pw.println(sinal);
			pw.flush();
		}
	}

	public static class ReceberDado extends Thread{
		BufferedReader recebedor;
		String[] dados;

		public ReceberDado(Socket s){
			try{
				recebedor = new BufferedReader(new InputStreamReader(s.getInputStream()));
			}catch (Exception e){}
		}
		@Override
		public void run(){
			try{
				identificacao = Integer.parseInt(recebedor.readLine());
			} catch (Exception e){

			}
			while(true){
				try{
					dados = recebedor.readLine().split(",");
				} catch (Exception e){
					e.printStackTrace();
				}
				if (dados[0].equals("sala")){
					//[1] e [2], salas passadas e próximas
					//[3], quantidade de inimigos
					//[4], [5] e [6], cores do chão
					//[7] quantidade de inimigos
					//[8+] coisas dos inimigos, cada um vai ocupar 2
					int[] inimigos = {0};
					if (dados.length > 4){
						inimigos = new int[Integer.parseInt(dados[3]) * 2];
						for (int i = 0; i < Integer.parseInt(dados[3]); i++ ){
							inimigos [i * 2] = Integer.parseInt(dados[4 + i * 2]);
							inimigos [i * 2 + 1] = Integer.parseInt(dados[5 + i * 2]);
						}
					}
					System.out.println(dados[1]);
					Salas temp = new Salas(Integer.parseInt(dados[1]), Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), inimigos);
					if (dados.length > 4){
						temp.naoPodePassar();
					}
					PainelDoJogo.updSala(temp);

				}
				if (dados[0].equals("user")){ //dados[1] == identificação / posição na lista
					if(PainelDoJogo.getJogadores().size() <= Integer.parseInt(dados[1]) || PainelDoJogo.getJogadores().size() == 0){
						Jogador temp = new Jogador(); //[2] e [3] x e y
						temp.mudaTudo(Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), Integer.parseInt(dados[4]), Integer.parseInt(dados[5]));
						PainelDoJogo.attLista(Integer.parseInt(dados[1]), temp);
					} else {
						PainelDoJogo.getJogadores().get(Integer.parseInt(dados[1])).mudaTudo(Integer.parseInt(dados[2]), Integer.parseInt(dados[3]), Integer.parseInt(dados[4]), Integer.parseInt(dados[5]));
						if (dados[6].equals("visivel")){
							PainelDoJogo.getJogadores().get(Integer.parseInt(dados[1])).setVisivel(true);
						} else {
							PainelDoJogo.getJogadores().get(Integer.parseInt(dados[1])).setVisivel(false);
						}
					}
				}
				if (dados[0].equals("inimigos")){
					int j = 0;
					if (PainelDoJogo.getSala().getInimigos() != null){
						for (Inimigo i : PainelDoJogo.getSala().getInimigos()){
							i.mudaTudo(Integer.parseInt(dados[j * 4 + 1]), Integer.parseInt(dados[j * 4 + 2]), Integer.parseInt(dados[j * 4 + 3]), Integer.parseInt(dados[j * 4 + 4]));
							j++;
						}
					}
				}
				if(dados[0].equals("morreu")){
					PainelDoJogo.getJogador().morrer();
				}
				if(dados[0].equals("podepassar")){
					PainelDoJogo.getSala().podePassar();
				}

				try{
					ReceberDado.sleep(30/Constantes.FPS);
				} catch (InterruptedException e) {
                	e.printStackTrace();
            	} 
			}
		}
	}

	private String ip;
	private int porta;

	public ClienteOnline(){
		ip = JOptionPane.showInputDialog("Insira o ip do servidor\n`localhost` para server local");
		porta = Integer.parseInt(JOptionPane.showInputDialog("Insira a porta do servidor"));
	}

	static private EnviarDado enviador;

	public void inicia() throws IOException{
		Socket s = new Socket (ip, porta);
		enviador = new EnviarDado(s);
		ReceberDado recebedor = new ReceberDado(s);
		enviador.start();
		recebedor.start();
	}

	public static EnviarDado getEnviador(){
		return enviador;
	}
}