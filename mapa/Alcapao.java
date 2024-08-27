package mapa;

import java.awt.Rectangle;


public class Alcapao{
	int x, y, largura, altura;
	Rectangle hitbox;

	public Alcapao(int x, int y, int largura, int altura){
		this.x = x;
		this.y = y;
		this.largura = largura;
		this.altura = altura;
		hitbox = new Rectangle(x, y, largura, altura);
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getAltura(){
		return altura;
	}
	public int getLargura(){
		return largura;
	}

	public Rectangle getHitbox(){
		return hitbox;
	}
}