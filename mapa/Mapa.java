package mapa;
import jogo.*;
import entidades.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.ListIterator;
import entidades.Inimigo;
import server.*;


public class Mapa{
	private Salas atualSala;
	private Random ran = new Random();
	private Gerador gerar;
	private List<Salas> salas;
	private int tam;
	private ListIterator<Salas> navegador;
	private boolean online;

	public Mapa(boolean online){
		this.online = online;
	}

	public Salas criar(){
		gerar = new Gerador(this); //cria um novo Gerador (do Gerador.java)
		gerar.gerar(); //gera um mundo
		tam = gerar.tamanho;//pega o valor do tamanho
		salas = gerar.ordem; //pega a list com as salas
		navegador = salas.listIterator(); //adiciona o Iterator da List ao Iterator local
		atualSala = navegador.next();
		return atualSala;
	}

	public void update(){
		int j = 0;
		if (atualSala != null){
			if(atualSala.getInimigos() != null){
				for(Inimigo i : atualSala.getInimigos()){
					if(i != null){
						i.update();
						if (i.getMorto()){
							j++;
						}
					}
				}
				if (j == atualSala.getInimigos().length){
					atualSala.podePassar();
				} else {
					atualSala.naoPodePassar();
				}
			} else {
				atualSala.podePassar();
			}
			if (atualSala.getInimigos() != null){
				for (Inimigo m : atualSala.getInimigos()){
					if (m != null){
						if (!online && PainelDoJogo.getJogador().getHitbox().intersects(m.getHitbox())){
							if (!PainelDoJogo.getJogador().getColor().equals("red")){
								PainelDoJogo.getJogador().morrer();
							} else {
								m.setCor(Color.black);
								m.setVel(0);
								m.morrer();

							}
						}
					}
				}
			}
		}
	}

	public Salas getSalaAtual(){
		return atualSala;
	}

	public void voltaSala(){
		atualSala = navegador.previous();
		atualSala = navegador.previous();
		navegador.next();
	}

	public void avancaSala(){
		atualSala = navegador.next();
	}

	public List<Salas> getSalas(){
		return salas;
	}
}