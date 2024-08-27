package mapa;

public class Parede{
	int altura, largura, x, y;
	public Parede(int altura, int largura, int x, int y){
		this.altura = altura;
		this.largura = largura;
		this.x = x;
		this.y = y;	
	}

	public int getAltura(){
		return altura;
	}

	public int getLargura(){
		return largura;
	}
}