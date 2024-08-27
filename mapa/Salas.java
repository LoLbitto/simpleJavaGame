package mapa;

import java.util.List;
import java.awt.Color;
import jogo.*;
import entidades.*;
import entidades.Inimigo;
import java.util.Random;
import java.awt.Graphics2D;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

public class Salas{
	private int passado, prox;
	private Color cor;
	private Parede[] parede = new Parede[4];
	private Inimigo[] inimigos;
	private Random ran = new Random();
	private Porta porta1, porta2;
	private Alcapao saida;
	private boolean online = false;
	private int[] dados;
	private boolean podePassar = false;
	private BufferedImage fundo;

	private Jogador foco = PainelDoJogo.getJogador();

	public Salas(int passado, int prox){
		this.cor = cor;
		this.passado = passado;
		this.prox = prox;

		try{
			fundo = ImageIO.read(new File("textures/salas/sala" + passado + prox + ".png"));
		} catch (Exception e){}

		parede[0] = new Parede(76, Constantes.LARGURA, 0, Constantes.ALTURA - 78);
		parede[1] = new Parede(Constantes.ALTURA, 76, 0, 0);
		parede[2] = new Parede(76, Constantes.LARGURA, 0, 0);
		parede[3] = new Parede(Constantes.ALTURA, 76, Constantes.LARGURA - 78, 0);


		if(passado == 0 || prox == 0){
			inicialFinal(prox, passado);
		}
		else if((prox == 1 && passado == 3) || (passado == 1 && prox == 3)){
			retaVertical(prox, passado);
		}
		else if((prox == 2 && passado == 4) || (passado == 2 && prox == 4)){
			retaHorizontal(prox, passado);
		}
		else if ((prox == 3 && passado == 2) || (passado == 3 && prox == 2)){
			cimaEsquerda(prox, passado);
		}
		else if((prox == 3 && passado == 4) || (passado == 3 && prox == 4)){
			cimaDireita(prox, passado);
		}
		else if((prox == 1 && passado == 2) || (passado == 1 && prox == 2)){
			baixoEsquerda(prox, passado);
		}
		else if((prox == 1 && passado == 4) || (passado == 1 && prox == 4)){
			baixoDireita(prox, passado);
		}
	}

		public Salas(int passado, int prox, int inimigos, int[] dados){
		this.cor = cor;
		this.passado = passado;
		this.prox = prox;
		this.dados = dados;

		try{
			fundo = ImageIO.read(new File("textures/salas/sala" + passado + prox + ".png"));
		} catch (Exception e){}

		online = true;

		parede[0] = new Parede(76, Constantes.LARGURA, 0, Constantes.ALTURA - 78);
		parede[1] = new Parede(Constantes.ALTURA, 76, 0, 0);
		parede[2] = new Parede(76, Constantes.LARGURA, 0, 0);
		parede[3] = new Parede(Constantes.ALTURA, 76, Constantes.LARGURA - 78, 0);


		if(passado == 0 || prox == 0){
			inicialFinal(prox, passado);
		}
		else if((prox == 1 && passado == 3) || (passado == 1 && prox == 3)){
			retaVertical(prox, passado);
		}
		else if((prox == 2 && passado == 4) || (passado == 2 && prox == 4)){
			retaHorizontal(prox, passado);
		}
		else if ((prox == 3 && passado == 2) || (passado == 3 && prox == 2)){
			cimaEsquerda(prox, passado);
		}
		else if((prox == 3 && passado == 4) || (passado == 3 && prox == 4)){
			cimaDireita(prox, passado);
		}
		else if((prox == 1 && passado == 2) || (passado == 1 && prox == 2)){
			baixoEsquerda(prox, passado);
		}
		else if((prox == 1 && passado == 4) || (passado == 1 && prox == 4)){
			baixoDireita(prox, passado);
		}
	}

	public void inicialFinal(int prox, int passado){
		int i;
		if (prox == 0){
			i = passado;
		} else {
			i = prox;
		}
		if (prox != 0){
			switch(i){
			case 1:
				definePorta1(1);
				definePorta2(5);
				break;
			case 2:
				definePorta1(2);
				definePorta2(5);
				break;
			case 3:
				definePorta1(3);
				definePorta2(5);
				break;
			case 4:
				definePorta1(4);
				definePorta2(5);
				break;
			}
		} else {
			saida = new Alcapao(Constantes.LARGURA/2 - Constantes.TAMANHO/2, Constantes.ALTURA/2 - Constantes.TAMANHO/2, Constantes.TAMANHO, Constantes.TAMANHO);
			switch(i){
			case 1:
				definePorta2(1);
				definePorta1(5);
				break;
			case 2:
				definePorta2(2);
				definePorta1(5);
				break;
			case 3:
				definePorta2(3);
				definePorta1(5);
				break;
			case 4:
				definePorta2(4);
				definePorta1(5);
				break;
			}
		}
		podePassar = true;
	}

	public void retaHorizontal(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(4)+1];
			for(int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(200 + ran.nextInt(200), Constantes.ALTURA/2, true, ran.nextInt(16)+4);
			}
		}else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados [i * 2], dados [i * 2 + 1], true, 0);
			}
		}
		if (passado == 2){
			definePorta1(4);
			definePorta2(2);
		} else {
			definePorta2(4);
			definePorta1(2);
		}
	}

	public void retaVertical(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(4)+1];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(Constantes.LARGURA/2, 200 + ran.nextInt(200) , false, ran.nextInt(16)+4);
			}
		} else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados [i * 2], dados [i * 2 + 1], false, 0);
			}
		}
		if (passado == 3){
			definePorta1(1);
			definePorta2(3);
		} else {
			definePorta2(1);
			definePorta1(3);
		}
	}

	public void cimaEsquerda(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(3)+1];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(ran.nextInt(Constantes.LARGURA/4 - Constantes.TAMANHO) + Constantes.LARGURA*3/4, ran.nextInt(Constantes.ALTURA/4 - Constantes.TAMANHO) + Constantes.ALTURA*3/4, ran.nextInt(3)+2, foco);
			}
		} else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados[i * 2], dados [i * 2 + 1], 0, null);
			}
		}
		if (passado == 3){
			definePorta1(2);
			definePorta2(3);
		} else {
			definePorta2(2);
			definePorta1(3);
		}
	}

	public void cimaDireita(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(3)+1];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(ran.nextInt(Constantes.LARGURA/4)+ parede[1].largura, ran.nextInt(Constantes.ALTURA/4 - Constantes.TAMANHO) + Constantes.ALTURA*3/4, ran.nextInt(3)+2, foco);
			}
		} else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados[i * 2], dados [i * 2 + 1], 0, null);
			}
		}
		if (passado == 3){
			definePorta1(4);
			definePorta2(3);
		} else {
			definePorta2(4);
			definePorta1(3);
		}
	}

	public void baixoDireita(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(3)+1];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(ran.nextInt(Constantes.LARGURA/4) + parede[1].largura, ran.nextInt(Constantes.ALTURA/4) + parede[1].largura, ran.nextInt(3)+2, foco);
			}
		} else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados[i * 2], dados [i * 2 + 1], 0, null);
			}
		}
		if (passado == 1){
			definePorta1(4);
			definePorta2(1);
		} else {
			definePorta2(4);
			definePorta1(1);
		}
	}

	public void baixoEsquerda(int prox, int passado){
		if (!online){
			inimigos = new Inimigo[ran.nextInt(3)+1];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(ran.nextInt(Constantes.LARGURA/4 - Constantes.TAMANHO) + Constantes.LARGURA*3/4, ran.nextInt(Constantes.ALTURA/4) + parede[1].largura, ran.nextInt(3)+2, foco);
			}
		} else {
			inimigos = new Inimigo[dados.length/2];
			for (int i = 0; i < inimigos.length; i++){
				inimigos[i] = new Inimigo(dados[i * 2], dados [i * 2 + 1], 0, null);
			}
		}
		if (passado == 1){
			definePorta1(2);
			definePorta2(1);
		} else {
			definePorta2(2);
			definePorta1(1);
		}
	}

	public void definePorta1(int caso){
		switch (caso){
		case 1:
			porta1 = new Porta(Constantes.LARGURA/2 - Constantes.TAMANHO/2 - 4, Constantes.ALTURA - 80, 76, Constantes.TAMANHO + 8);
			break;
		case 2:
			porta1 = new Porta(0, Constantes.ALTURA/2 - Constantes.TAMANHO/2 - 4, Constantes.TAMANHO + 8, 76);
			break;
		case 3:
			porta1 = new Porta(Constantes.LARGURA/2 - Constantes.TAMANHO/2 - 4, 0, 76, Constantes.TAMANHO + 8);
			break;
		case 4:
			porta1 = new Porta(Constantes.LARGURA - 78, Constantes.ALTURA/2 - Constantes.TAMANHO/2 - 4, Constantes.TAMANHO + 8, 76);
			break;
		case 5:
			porta1 = new Porta(0, 0, 0, 0);
		}
	}
	public void definePorta2(int caso){
		switch (caso){
		case 1:
			porta2 = new Porta(Constantes.LARGURA/2 - Constantes.TAMANHO/2 - 4, Constantes.ALTURA - 80, 76, Constantes.TAMANHO + 8);
			break;
		case 2:
			porta2 = new Porta(0, Constantes.ALTURA/2 - Constantes.TAMANHO/2 - 4, Constantes.TAMANHO + 8, 76);
			break;
		case 3:
			porta2 = new Porta(Constantes.LARGURA/2 - Constantes.TAMANHO/2 - 4, 0, 76, Constantes.TAMANHO + 8);
			break;
		case 4:
			porta2 = new Porta(Constantes.LARGURA - 78, Constantes.ALTURA/2 - Constantes.TAMANHO/2 - 4, Constantes.TAMANHO + 8, 76);
			break;
		case 5:
			porta2 = new Porta(0, 0, 0, 0);
		}
	}

	public void drawn(Graphics2D g2){
		g2.setColor(Color.gray); //cor da parede
		for(Parede m : this.parede){
			if(m != null){
				g2.fillRect(m.x, m.y, m.largura, m.altura);
			}
		}
		if (inimigos != null){
			for(Inimigo i : this.inimigos){
				if(i != null){
					i.drawn(g2);
				}
			}
		}
		if (porta1 != null){
			g2.setColor(porta1.cor);
			g2.fillRect(porta1.x, porta1.y, porta1.largura, porta1.altura);
		}
		if (porta2 != null){
			g2.fillRect(porta2.x, porta2.y, porta2.largura, porta2.altura);
		}
	}

	public Alcapao getSaida(){
		return saida;
	}

	public Color getCor(){
		return cor;
	}

	public Inimigo[] getInimigos(){
		return inimigos;
	}

	public int getPassado(){
		return passado;
	}

	public int getProx(){
		return prox;
	}

	public Porta getPorta1(){
		return porta1;
	}

	public Porta getPorta2(){
		return porta2;
	}

	public Parede[] getParedes(){
		return parede;
	}

	public void podePassar(){
		podePassar = true;
	}

	public void naoPodePassar(){
		podePassar = false;
	}

	public boolean getPodePassar(){
		return podePassar;
	}

	public void setFoco(Jogador foco){
		this.foco = foco;
		for (Inimigo i : inimigos){
			i.setFoco(foco);
		}
	}

	public void anulaInimigos(){
		inimigos = null;
	}

	public boolean temSaida(){
		if (saida != null){
			return true;
		} else {
			return false;
		}
	}

	public BufferedImage getFundo(){
		return fundo;
	}
}
