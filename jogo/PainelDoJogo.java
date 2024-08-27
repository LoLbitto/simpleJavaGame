package jogo;

import mapa.*;
import entidades.*;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.Thread;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.Font;
import java.util.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import entidades.Jogador;

public class PainelDoJogo extends JPanel implements Runnable{
	//Configuraçõs de tela
	private static int level = 1;

	private Inputs keyH; //cria um objeto para ver teclas clicadas

	private static Salas atualSala;

	Thread threadJogo;//cria uma thread para o jogo
	//Básicamente, uma thread é uma ação de acontece por tempo indefinido, nesse caso é necessário para manter o fps do jogo
	//Caso contrário o jogo só ia ficar numa imagem estática

	public static Jogador jogador;

	private boolean online;

	public int limiteEsquerdo = 76;
	public int limiteCima = 76;
	public int limiteDireito = Constantes.LARGURA - Constantes.TAMANHO - 78;
	public int limiteBaixo = Constantes.ALTURA - Constantes.TAMANHO - 80;

	private static ArrayList<Jogador> jogadores;

	public PainelDoJogo(boolean online) {
		keyH = new Inputs();
		jogador = new Jogador(keyH);
		this.setPreferredSize(new Dimension(Constantes.LARGURA, Constantes.ALTURA)); //coloca tamanho
		this.setBackground(Color.black); //cor de fundo
		this.setDoubleBuffered(true); //renderiza uma segunda tela pelo oq entendi, é bom ter no jogo
		this.addKeyListener(keyH);
		this.setFocusable(true);
		keyH.tudoFalso();
		this.online = online;
		this.requestFocusInWindow();
	}

	ClienteOnline updateOnline;
	ThreadUp update;

	public void iniciaThread() {
		threadJogo = new Thread(this);
		if(!online){
			update = new ThreadUp(online);
			update.start();
		} else {
			jogadores = new ArrayList<Jogador>();
			updateOnline = new ClienteOnline();
			try{
				updateOnline.inicia();
			}catch (Exception e){}
		}
		threadJogo.start();
	}

	@Override
	public void run() {
		//método da interface "Runnable" q é usada em conjunto com o Thread
		while(threadJogo != null){
			long tempo = System.nanoTime();
			repaint();
			update(tempo);
			try{
				Thread.sleep(1000/Constantes.FPS);
			} catch (InterruptedException e) {
                e.printStackTrace();
            }
			
		}
	}

	public void update(long tempo){
		if (atualSala != null){
			jogador.update(tempo);
			//Aqui tudo se repete, mas basicamente a primeira condição é sempre "se tu sair da borda faça isso", o if sempre vai ver se
			//Existe uma próxima sala na direção (ou uma sala já passada), caso tenha, ele carrega a sala na "atualSala", fazendo
			//Com q ela seja carregada visualmente, após isso te teleporta para o lado contrário da sala "se tu entrou na direita
			// tu sai pela esquerda obviamente", caso não tenha sala, ele só te proibe de sair fora da tela
			if(atualSala.getSaida() != null){
				if (jogador.getHitbox().intersects(atualSala.getSaida().getHitbox())){
					if (!online){
						ThreadUp.mudaLevel();
					} else {
						ClienteOnline.getEnviador().enviaSinal("mudalevel");
					}
				}
			}
			if((atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox()) &&  atualSala.getProx() == 2) && atualSala.getPodePassar() || (atualSala.getPassado() == 2 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())) && atualSala.getPodePassar()){
				if(atualSala.getPassado() == 2 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.voltaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("volta");
					}
					jogador.x = limiteDireito - 10;
					
				} else if(atualSala.getProx() == 2 && atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.avancaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("avanca");
					}
					jogador.x = limiteDireito - 10;
					
				}
			} else if (jogador.x < limiteEsquerdo){
				jogador.x = limiteEsquerdo;
			}
			if((atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox()) &&  atualSala.getProx() == 4) && atualSala.getPodePassar() || (atualSala.getPassado() == 4 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())) && atualSala.getPodePassar()){
				if(atualSala.getPassado() == 4 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.voltaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("volta");
					}
					jogador.x = limiteEsquerdo + 10;
					
				} else if(atualSala.getProx() == 4 && atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.avancaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("avanca");
					}
					jogador.x = limiteEsquerdo + 10;
					
				}
			} else if (jogador.x > limiteDireito){
				jogador.x = limiteDireito;
			}
			if((atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox()) &&  atualSala.getProx() == 3) && atualSala.getPodePassar() || (atualSala.getPassado() == 3 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())) && atualSala.getPodePassar()){
				if(atualSala.getPassado() == 3 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.voltaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("volta");
					}
					jogador.y = limiteBaixo - 10;
					
				} else if(atualSala.getProx() == 3 && atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.avancaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("avanca");
					}
					jogador.y = limiteBaixo - 10;
					
				}
			} else if (jogador.y < limiteCima){
				jogador.y = limiteCima;
			}
			if ((atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox()) &&  atualSala.getProx() == 1) && atualSala.getPodePassar() || (atualSala.getPassado() == 1 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())) && atualSala.getPodePassar()){
				if(atualSala.getPassado() == 1 && atualSala.getPorta2().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.voltaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("volta");
					}
					jogador.y = limiteCima + 10;
					
				} else if(atualSala.getProx() == 1 && atualSala.getPorta1().getHitbox().intersects(jogador.getHitbox())){
					if (!online){
						ThreadUp.avancaSala();
					} else {
						atualSala.anulaInimigos();
						ClienteOnline.getEnviador().enviaSinal("avanca");
					}
					jogador.y = limiteCima + 10;
					
				}
			} else if (jogador.y > limiteBaixo) {
				jogador.y = limiteBaixo;
			}
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		if (atualSala != null){
			g2.drawImage(atualSala.getFundo(), 0, 0, null);
			if(atualSala.getSaida() != null){
				g2.setColor(new Color(51, 0, 0));
				g2.fillRect(atualSala.getSaida().getX(), atualSala.getSaida().getY(), atualSala.getSaida().getLargura(), atualSala.getSaida().getAltura());
				g2.setColor(new Color(120, 51, 0));
				g2.fillRect(atualSala.getSaida().getX() + 10, atualSala.getSaida().getY() + 10, atualSala.getSaida().getLargura() - 20, atualSala.getSaida().getAltura() - 20);
			}
			if (atualSala.getInimigos() != null){
				for(Inimigo temp : atualSala.getInimigos()){
					temp.drawn(g2);
				}
			}
		}
		jogador.drawn(g2);
		if (online && jogadores != null){
			for (Jogador player : jogadores){
				if (player.getVisivel()){
					player.drawn(g2);
				}
			}
		}

		g2.setColor(new Color(153, 153, 153));
		g2.fillRect(5, 5, 135, 60);

		g2.setColor(new Color(102, 102, 102));
		g2.fillRect(15, 15, 115, 40);

		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.setColor(Color.white); 

		g2.drawString("Level:  " + level, 15, 45);

		g2.dispose();
	} 

	public static void updSala(Salas sala){
		atualSala = sala;
	}

	public static Salas getSala(){
		return atualSala;
	}

	public static Jogador getJogador(){
		return jogador;
	}

	public static void aumentaLvl(){
		level++;
	}

	public static void resetaLvl(){
		level = 1;
	}

	public static void attLista(int index, Jogador jogador){
		if (index >= 0 && index < jogadores.size()) {
        	jogadores.add(index, jogador);
    	} else {
    		while(jogadores.size() <= index){
    			if (index != jogadores.size()){
        			jogadores.add(new Jogador());
        			jogadores.get(jogadores.size() - 1).morrer();
        		} else {
        			jogadores.add(jogador);
        		}
        	}
        }
	}

	public static ArrayList<Jogador> getJogadores(){
		return jogadores;
	}

	public void para(){
		threadJogo = null;
		if (online){
			updateOnline = null;
		} else {
			update = null;
		}
		Jogo.getJanela().remove(this);
	}
}