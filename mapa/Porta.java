package mapa;

import java.awt.Rectangle;
import java.awt.Color;

public class Porta{
	Color cor = new Color(102, 51, 0);
	Rectangle hitbox;
	int x, y, altura, largura;

	public Porta(int x, int y, int altura, int largura){
		this.x = x;
		this.y = y;
		this.altura = altura;
		this.largura = largura;
		hitbox = new Rectangle(x, y, largura, altura);
	}

	public Rectangle getHitbox(){
		return hitbox;
	}
}