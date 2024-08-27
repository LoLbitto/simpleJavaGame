package jogo;

import javax.swing.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class Botao{
		BufferedImage[] estado = new BufferedImage[3];
		BufferedImage atual;
		int x, y;
		Rectangle hitbox;
		int ordem;

		public Botao(int ordem){
			x = 282;
			y = ordem * 100 + 250;
			this.ordem = ordem;
			hitbox = new Rectangle(x, y, 192, 96);
			for (int i = 0; i < 2; i++){
				try{
					estado[i] = ImageIO.read(new File("textures/button" + (ordem+1) + "_" + (i+1) + ".png"));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			atual = estado[0];
		}

		public Rectangle getHitbox(){
			return hitbox;
		}

		public int getX(){
			return x;
		}

		public int getY(){
			return y;
		}

		public void setLocal(int y){
			this.y = y;
			hitbox = new Rectangle(x, y, 192, 96);
		}

		public int getOrdem(){
			return ordem;
		}

		public BufferedImage getImagem(){
			return atual;
		}

		public void chanceEstado(int estado){
			atual = this.estado[estado];
		}
	}