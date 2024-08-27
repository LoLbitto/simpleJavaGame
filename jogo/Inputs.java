package jogo;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import entidades.Jogador;


public class Inputs implements KeyListener{

	public boolean cima, baixo, esquerda, direita, dash, esc;
	public Jogador jogador;


	@Override
	public void keyTyped(KeyEvent e){
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_SPACE){
			dash = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent e){
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_SPACE){
			dash = true;
		}

		if (code == KeyEvent.VK_W){
			cima = true;
		}
		if (code == KeyEvent.VK_A){
			esquerda = true;
		}
		if (code == KeyEvent.VK_S){
			baixo = true;
		}
		if (code == KeyEvent.VK_D){
			direita = true;
		}
		if (code == KeyEvent.VK_ESCAPE){
			esc = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e){
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W){
			cima = false;
		}
		if (code == KeyEvent.VK_A){
			esquerda = false;
		}
		if (code == KeyEvent.VK_S){
			baixo = false;
		}
		if (code == KeyEvent.VK_D){
			direita = false;
		}
		if (code == KeyEvent.VK_ESCAPE){
			esc = false;
		}
	}

	public void tudoFalso(){
		cima = false;
		baixo = false;
		esquerda = false;
		direita = false;
	}
}