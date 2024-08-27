package jogo;

import javax.swing.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class MenuMorte extends JPanel implements Runnable{

	private boolean tipo;
	private long cooldown;

	class MouseInputs implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e){
			for(Botao b : botoes){
				if(b.getHitbox().contains(e.getX(),e.getY())){
					if (b.getOrdem() == 3){
						if (tipo){
							ClienteOnline.getEnviador().enviaSinal("renasceu");
						} else {
							ThreadUp.reseta();
						}
						System.out.println("clicou");
						sair = true;
					}

					if (b.getOrdem() == 2){
						System.out.println("clicou");
						Jogo.criaTudo();
						sair = true;
					}
				}
				System.out.println("hmm");
			}
		}
		@Override
		public void mouseExited(MouseEvent e){

		}
		@Override
		public void mouseEntered(MouseEvent e){

		}
		@Override
		public void mouseReleased(MouseEvent e){

		}
		@Override
		public void mouseClicked(MouseEvent e){
		}
	}

	private ArrayList<Botao> botoes;
	private MouseInputs mouse;
	private BufferedImage titulo;
	private boolean sair = false;
	private Thread thread;

	public MenuMorte(boolean tipo){
		this.tipo = tipo;
		load();
	}

	public void load(){
		mouse = new MouseInputs();
		this.setPreferredSize(new Dimension(768, 576)); //coloca tamanho
		this.setBackground(Color.black); //cor de fundo
		this.setFocusable(true);
		this.addMouseListener(mouse);
		botoes = new ArrayList<Botao>();
		thread = new Thread(this);
		Jogo.getJanela().add(this);
		Jogo.getJanela().revalidate();
		try{
			for(int i = 2; i < 4; i++){
				botoes.add(new Botao(i));
				System.out.println("botão novo");
			}
			botoes.get(1).setLocal(250);
			botoes.get(0).setLocal(350);
			titulo = ImageIO.read(new File("textures/gameover.png"));
		} catch (Exception e){
			System.out.println("a");
		}
		thread.start();
	}

	@Override
	public void run(){
		while(true){
			repaint();
			if (sair){
				this.removeMouseListener(mouse);
				Jogo.getJanela().remove(this);
				break;
			}
		}
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(titulo, 220, 25, 320, 150, null);
		for (Botao b : botoes){
			if (b.getOrdem() == 0 && tipo){
				if (cooldown - System.nanoTime() != 10000000000f){
					g2.drawString( "tempo: " + ((cooldown - System.nanoTime())/1000000000f - 10), b.getX(), b.getY() );
				} else {
					g2.drawImage(b.getImagem(), b.getX(), b.getY(), 192, 90, null);
				}
			} else {
				g2.drawImage(b.getImagem(), b.getX(), b.getY(), 192, 90, null);
			}
		}
		g2.dispose();
	}
} 