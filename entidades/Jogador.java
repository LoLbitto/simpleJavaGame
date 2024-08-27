package entidades;

import jogo.Inputs;
import jogo.Constantes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import java.awt.Rectangle;

import jogo.*;

public class Jogador extends Entidade{
	private Inputs keyH;
	private long ultDash;
	private boolean emDash;
	private long tEmDash;
	private Color cor = Color.white;
	private boolean online;
	private boolean morto = false;

	private BufferedImage[][] texturas;
	private BufferedImage atual;

	private boolean visivel = true;

	private long cooldown;

	private int face[] = {0, 0};

	public Jogador(Inputs keyH){
		this.keyH = keyH;
		valores();
		online = false;
		morto = false;
		texturas = new BufferedImage[4][3];
		for(int i = 0; i < 4; i++){
			for (int j = 0; j < 3; j++){
				try{
					texturas[i][j] = ImageIO.read(new File("textures/player/offline" + i + j + ".png"));
				} catch (Exception e){
					System.out.println("a");
				}
			}
		}
		atual = texturas[face[1]][0];
	}

	public Jogador(){
		online = true;
		morto = false;
		texturas = new BufferedImage[4][3];
		for(int i = 0; i < 4; i++){
			for (int j = 0; j < 3; j++){
				try{
					texturas[i][j] = ImageIO.read(new File("textures/player/offline" + i + j + ".png"));
				} catch (Exception e){
					System.out.println("a");
				}
			}
		}
		atual = texturas[face[1]][0];
	}

	public void valores(){
		x = Constantes.LARGURA/2 - Constantes.TAMANHO;
		y = Constantes.ALTURA/2 - Constantes.TAMANHO;
		vel = 4;
		hitbox = new Rectangle(x, y, Constantes.TAMANHO, Constantes.TAMANHO);
	}

	private long ultB = 0, ultE = 0, ultC = 0, ultD = 0;

	public void update(long tempo){
		cooldown = tempo - ultDash;
		if(keyH.cima){
			face[1] = 2;
			atual = texturas[face[1]][face[0]];
			if (ultC != 0){
				if (System.nanoTime() - ultC > 250000000f){
					atual = texturas[face[1]][face[0]];
					if (face[0] == 1) face[0] = 2;
					else face[0] = 1;
					ultC = System.nanoTime();
				}
			} else {
				ultC = System.nanoTime();
			}
			y -= vel;
			face[1] = 2;
		} else if(face[1] == 2){
			atual = texturas[face[1]][0];
			face[0] = 0;
			ultC = 0;
		}
		if(keyH.baixo){
			face[1] = 0;
			atual = texturas[face[1]][face[0]];
			if (ultB != 0){
				if (System.nanoTime() - ultB > 250000000f){
					atual = texturas[face[1]][face[0]];
					if (face[0] == 1) face[0] = 2;
					else face[0] = 1;
					ultB = System.nanoTime();
				}
			} else {
				ultB = System.nanoTime();
			}
			y += vel;
			face[1] = 0;
		} else if(face[1] == 0){
			atual = texturas[face[1]][0];
			face[0] = 0;
			ultB = 0;
		}
		if(keyH.esquerda){
			face[1] = 1;
			atual = texturas[face[1]][face[0]];
			if (ultE != 0){
				if (System.nanoTime() - ultE > 250000000f){
					atual = texturas[face[1]][face[0]];
					if (face[0] == 1) face[0] = 2;
					else face[0] = 1;
					ultE = System.nanoTime();
				}
			} else {
				ultE = System.nanoTime();
			}
			x -= vel;
			face[1] = 1;
		} else if(face[1] == 1){
			atual = texturas[face[1]][0];
			face[0] = 0;
			ultE = 0;
		}
		if(keyH.direita){
			face[1] = 3;
			atual = texturas[face[1]][face[0]];
			if (ultD != 0){
				if (System.nanoTime() - ultD > 250000000f){
					atual = texturas[face[1]][face[0]];
					if (face[0] == 1) face[0] = 2;
					else face[0] = 1;
					ultD = System.nanoTime();
				}
			} else {
				ultD = System.nanoTime();
			}
			x += vel;
			face[1] = 3;
		} else if(face[1] == 3){
			atual = texturas[face[1]][0];
			face[0] = 0;
			ultD = 0;
		}
		if(keyH.dash){
			if(tempo - ultDash > 2000000000f){
				if (emDash){
					switch (face[1]){
					case 0: 
						y += vel *3;
						break;
					case 1:
						x -= vel *3;
						break;
					case 2:
						y -= vel *3;
						break;
					case 3:
						x += vel *3;
						break;
					}
				} 
				if(tempo - tEmDash > 300000000f && emDash){
					emDash = false;
					cor = Color.white;
					ultDash = tempo;
				} else if (!emDash) {
					tEmDash = tempo;
					emDash = true;
					cor = Color.red;
				}
			} else {
				keyH.dash = false;
				cor = Color.white;
			}
		}
		updateHitbox();
	}

	public void updateHitbox() {
		hitbox.x = x;
		hitbox.y = y;
	}
	public void drawn(Graphics2D g2){
		if (!online){
			g2.setColor(Color.black);

			if(cooldown < 1000000000){
				g2.setColor(new Color(153, 153, 153));
				g2.fillRect(Constantes.LARGURA - 140, 5, 135, 60);
				g2.setColor(new Color(153, 0, 0));
				g2.fillRect(Constantes.LARGURA - 130, 15, 115, 40);
			}
			else if(cooldown < 2000000000){
				g2.setColor(new Color(153, 153, 153));
				g2.fillRect(Constantes.LARGURA - 140, 5, 135, 60);
				g2.setColor(Color.red);
				g2.fillRect(Constantes.LARGURA - 130, 15, 57, 40);
				g2.setColor(new Color(153, 0, 0));
				g2.fillRect(Constantes.LARGURA - 75, 15, 60, 40);
			}
			if(cooldown > 2000000000){
				g2.setColor(new Color(153, 153, 153));
				g2.fillRect(Constantes.LARGURA - 140, 5, 135, 60);
				g2.setColor(Color.green);
				g2.fillRect(Constantes.LARGURA - 130, 15, 115, 40);
			}
		} 
		if (!morto){
			g2.drawImage(atual, x, y, 42, 48, null);
		}
	}
	public void resetaCooldown(){
		ultDash = 0;
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	public String getColor(){
		if (cor.getRGB() == Color.white.getRGB()){
			return "white";
		} else {
			return "red";
		}
	}

	public void mudaTudo(int x, int y, int lado0, int lado1){
		this.x = x;
		this.y = y;
		this.cor = cor;
		this.face[0] = lado0;
		this.face[1] = lado1;
		hitbox = new Rectangle(x, y, Constantes.TAMANHO, Constantes.TAMANHO);
		atual = texturas[lado1][lado0];
	}

	public Rectangle getHitbox(){
		return hitbox;
	}

	public void morrer(){
		if(!online){
			hitbox = new Rectangle(0, 0, 0, 0);
			ThreadUp.morre();
		} else {
			morto = true;
		}
	}

	public int getFace0(){
		return face[0];
	}

	public int getFace1(){
		return face[1];
	}



	public void setVisivel(boolean a){
		visivel = a;
	}

	public boolean getVisivel(){
		return visivel;
	}

	public boolean getVida(){
		return morto;
	}

	public void setCor(Color cor){
		this.cor = cor;
	}

}