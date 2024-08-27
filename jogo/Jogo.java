package jogo; //javac jogo/*.java mapa/*.java entidades/*.java
// java jogo/Jogo

import javax.swing.JFrame;
import java.util.List;
import java.util.ArrayList;

public class Jogo{
	static JFrame janela;
	static Menu menuInicial;

	public static void main(String[] args){
		janela = new JFrame(); //Cria um objeto de janela
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Faz a janela fechar normalmente quando o usuário clica no X
		janela.setResizable(false);
		janela.setTitle("Goblin Slayer");
		criaTudo();
	}

	public static JFrame getJanela(){
		return janela;
	}

	public static void criaTudo(){
		menuInicial = new Menu(); //cria um painel para o jogo

		janela.add(menuInicial); //adiciona o painel no frame

		janela.pack(); //dá o tamanho do painel

		janela.setLocationRelativeTo(null); //serve para especificar o local da janela, como não é necessário vamos deixar vazio, criando assim no meio da tela.
		janela.setVisible(true); //Roda a janela
	}
	public static void resetaJogo(boolean tipo){
		PainelDoJogo.resetaLvl();
		Menu.voltaJogo();
	}
}
