package entidades;

import mapa.Mapa;
import jogo.PainelDoJogo;
import java.awt.Color;
import java.awt.Graphics2D;
import jogo.Constantes;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import java.awt.Rectangle;


public class Inimigo extends Entidade{
	private boolean lado;
	private boolean direcao = true;
	private int tipo;
	private Color cor;
	private boolean morto = false;

	private BufferedImage[][] texturas;
	private BufferedImage atual;

	private Jogador foco;

	private int face[] = {0, 0};

	public Inimigo(int x, int y, boolean lado, int vel){
		this.x = x;
		this.y = y;
		this.lado = lado;
		this.cor = cor;
		this.tipo = 1;
		this.vel = vel;

		texturas = new BufferedImage[4][3];

		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 3; j++){
				try{
					texturas[i][j] = ImageIO.read(new File("textures/goblin/goblin" + i + j + ".png"));
				} catch (Exception e){}
			}
		}
		atual = texturas[0][1];

		hitbox = new Rectangle(x, y, Constantes.TAMANHO, Constantes.TAMANHO);
	}
	public Inimigo(int x, int y, int vel, Jogador foco){
		this.tipo = 2;
		this.foco = foco;
		this.x = x;
		this.y = y;
		this.cor = cor;
		this.vel = vel;

		texturas = new BufferedImage[4][3];

		for (int i = 0; i < 4; i++){
			for (int j = 1; j < 3; j++){
				try{
					texturas[i][j] = ImageIO.read(new File("textures/esqueleto/esqueleto" + i + j + ".png"));
				} catch (Exception e){}
			}
		}
		atual = texturas[0][1];
		hitbox = new Rectangle(x, y, Constantes.TAMANHO, Constantes.TAMANHO);
	}

	private long ultU = 0;

	public void update(){
		switch(this.tipo){
		case 1:
			if(lado == true){ //se lado for vdd ent ele vai pra cima e pra baixo
				if(y < 76){
					direcao = true;
					face[1] = 0;
				} else if(y > Constantes.ALTURA - 78 - Constantes.TAMANHO){
					direcao = false;
					face[1] = 2;
				}

				if (direcao == false){
					if (System.nanoTime() - ultU > 2500000000f / vel/2){
						atual = texturas[2][face[0] + 1];
						if (face[0] == 0) face[0] = 1;
						else face[0] = 0;
						ultU = System.nanoTime();
					}
					y -= vel;
				} else {
					if (System.nanoTime() - ultU > 2500000000f / vel/2){
						atual = texturas[0][face[0] + 1];
						if (face[0] == 0) face[0] = 1;
						else face[0] = 0;
						ultU = System.nanoTime();
					}
					y += vel;
				}
			} else {
				if(x < 16){
					direcao = true;
					face[1] = 3;
				} else if(x > Constantes.LARGURA - 16 - Constantes.TAMANHO){
					direcao = false;
					face[1] = 1;
				}

				if (direcao == false){
					if (System.nanoTime() - ultU > 2500000000f / vel/2){
						atual = texturas[1][face[0] + 1];
						if (face[0] == 0) face[0] = 1;
						else face[0] = 0;
						ultU = System.nanoTime();
					}
					x -= vel;
				} else {
					if (System.nanoTime() - ultU > 2500000000f / vel/2){
						atual = texturas[3][face[0] + 1];
						if (face[0] == 0) face[0] = 1;
						else face[0] = 0;
						ultU = System.nanoTime();
					}
					x += vel;
				}
			}
			break;
		case 2:
			if (foco.getX() > this.x){
				if (System.nanoTime() - ultU > 2500000000f / vel/2){
					atual = texturas[3][face[0] + 1];
					if (face[0] == 0) face[0] = 1;
					else face[0] = 0;
					ultU = System.nanoTime();
				}
				x += vel;
			} else if (foco.getX() < this.x){
				if (System.nanoTime() - ultU > 2500000000f / vel/2){
					atual = texturas[1][face[0] + 1];
					if (face[0] == 0) face[0] = 1;
					else face[0] = 0;
					ultU = System.nanoTime();
				}
				x -= vel;
			}
			if (foco.getY() > this.y){
				if (System.nanoTime() - ultU > 2500000000f / vel/2){
					atual = texturas[0][face[0] + 1];
					if (face[0] == 0) face[0] = 1;
					else face[0] = 0;
					ultU = System.nanoTime();
				}
				y+= vel;
			} else if (foco.getY() < this.y){
				if (System.nanoTime() - ultU > 2500000000f / vel/2){
					atual = texturas[2][face[0] + 1];
					if (face[0] == 0) face[0] = 1;
					else face[0] = 0;
					ultU = System.nanoTime();
				}
				y -= vel;
			}
		}
		updateHitbox();
	}
	public void updateHitbox() {
		hitbox.x = x;
		hitbox.y = y;
	}
	public void drawn(Graphics2D g2){
		if (!morto){
			g2.drawImage(atual, x, y, null);
		}
	}
	public void morrer(){
		hitbox.height = 0;
		hitbox.width = 0;
		morto = true;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public void mudaTudo(int x, int y, int face0, int face1){
		this.x = x;
		this.y = y;
		atual = texturas[0][0];
	}

	public void setFoco(Jogador foco){
		this.foco = foco;
	}
	
	public boolean getMorto(){
		return morto;
	}

	public void setCor(Color cor){
		this.cor = cor;
	}

	public Rectangle getHitbox(){
		return hitbox;
	}

	public void setVel(int vel){
		this.vel = vel;
	}

	public int get0(){
		return face[0];
	}

	public int get1(){
		return face[1];
	}
}