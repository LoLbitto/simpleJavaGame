package mapa;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import jogo.PainelDoJogo;

public class Gerador{
	int tamanho;
	int prox, antigo;
	Salas salaAtual;
	List<Salas> ordem;
	Random ran = new Random();
	Mapa mapa;

	public Gerador( Mapa mapa){
		this.mapa = mapa;
	}

	public void gerar(){ //1 - baixo, 2- lado esquerdo, 3- cima, 4- direita
		ordem = new ArrayList<Salas>(); //Cria array list para armazenar as salas
		tamanho = ran.nextInt(4) + 3;//gera um tamanho aleatório

		for (int i = 0; i < tamanho; i++){
			atualizar(i); //Atualiza todas as salas
			ordem.add(salaAtual); //adiciona a sala à List
		}
	}
	public void atualizar(int i){
		if(prox <= 2 && prox != 0){ //Se a próxima sala for menor ou igual a 2 (baixo ou esquerda), ele vai adicionar 2, transformando em direita ou cima
			antigo = prox + 2;
		} else if (prox != 0){ //o contrário do de cima, após isso, o "antigo" vai ter o valor inverso do próximo da sala passada
			antigo = prox - 2;
		}
		if (i != tamanho-1 && i != tamanho){ //verifica para não dar um valor prox à última sala
			do{
				prox = ran.nextInt(4) + 1; //gera a posição da próxima sala
			} while(prox == antigo); //se for igual ao valor da posição da sala antiga ele repete
		} else {
			prox = 0; //se for a última sala o valor do prox é 0, pq n tem mais salas
		}
		salaAtual = new Salas(antigo, prox); //gera um objeto da sala
	}
}