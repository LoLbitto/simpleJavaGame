package jogo;

import javax.swing.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class Menu extends JPanel implements Runnable{

	private static PainelDoJogo painel;
	public static BufferedImage fundo;

	class MouseInputs implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e){
			for(Botao b : botoes){
				if(b.getHitbox().contains(e.getX(),e.getY())){
					if (b.getOrdem() == 0){
						painel = new PainelDoJogo(false);
						Jogo.getJanela().add(painel);
						painel.iniciaThread();
						Jogo.getJanela().revalidate();
						sair = true;
					}

					if (b.getOrdem() == 1){
						painel = new PainelDoJogo(true);
						Jogo.getJanela().add(painel);
						painel.iniciaThread();
						Jogo.getJanela().revalidate();
						sair = true;
					}
				}
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

	ArrayList<Botao> botoes;
	MouseInputs mouse;
	BufferedImage titulo;
	boolean sair = false;
	public static Thread thread;
	public Menu(){
		load();
	}

	private void load(){
		mouse = new MouseInputs();
		this.setPreferredSize(new Dimension(768, 576)); //coloca tamanho
		this.setBackground(Color.black); //cor de fundo
		this.setFocusable(true);
		this.addMouseListener(mouse);
		botoes = new ArrayList<Botao>();
		thread = new Thread(this);
		try{
			for(int i = 0; i < 3; i++){
				botoes.add(new Botao(i));
			}
			titulo = ImageIO.read(new File("textures/titulo.png"));
			fundo = ImageIO.read(new File("textures/fundo.png"));
		} catch (Exception e){
			System.out.println("a");
		}
		thread.start();
	}

	@Override
	public void run(){
		while(true){
			if (sair){
				thread.interrupt();
				this.removeMouseListener(mouse);
				Jogo.getJanela().remove(this);
				break;
			}
			repaint();
		}
	}

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(fundo, 0, 0, null);
		g2.drawImage(titulo, 201, 25, null);
		for (Botao b : botoes){
			g2.drawImage(b.getImagem(), b.getX(), b.getY(), 192, 90, null);
		}
		g2.dispose();
	}

	public static void paraJogo(){
		painel.para();
	}

	public static void voltaJogo(){
		painel = new PainelDoJogo(false);
		Jogo.getJanela().add(painel);
		painel.iniciaThread();
		Jogo.getJanela().revalidate();
	}
}