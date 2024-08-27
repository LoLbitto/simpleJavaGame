package jogo;

import java.lang.Thread;
import java.awt.*;
import java.awt.event.KeyListener;
import mapa.*;
import entidades.*;
import server.*;
import java.util.*;
import server.*;

public class ThreadUp extends Thread{
	private static Mapa mapa;
	private static boolean tipo;
	private static Map<Integer, Salas> salasAtivas;

	final static int FPS = 60;

	public ThreadUp(boolean tipo){
		this.tipo = tipo;
		criaNovoMapa();
	}

	@Override
	public void run(){
		while(true){
			if(!tipo){
				mapa.update();
			} else {
				for(int k = 0; k < salasAtivas.size(); k++){
					Salas sala = salasAtivas.get(k);
					int j = 0;
					if(sala.getInimigos() != null){
						for(Inimigo i : sala.getInimigos()){
							if(i != null){
								i.update();
								if (i.getMorto()){
									j++;
								}
							}
							for(Usuario u : ThreadUsers.getLista()){
								Jogador temp = u.getPlayer();
								if (temp.getHitbox().intersects(i.getHitbox()) && u.getSalaAtual() == sala && !temp.getVida()){
									if (!temp.getColor().equals("red")){
										u.enviar("morreu, ");
										temp.morrer();
										System.out.println("morreu");
									} else {
										i.setVel(0);
										i.morrer();
									}
								}
							}
						}
						for (Usuario u : ThreadUsers.getLista()){
							if (j == sala.getInimigos().length && u.getSalaAtual() == sala){
								u.enviarPassagem("podepassar", salasAtivas.size());
							} else{
								u.enviarPassagem("naopodepassar", salasAtivas.size());
							}
						}
					}
				}
			}
			try{
				Thread.sleep(1000/Constantes.FPS);
			} catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
	}


	public static void criaNovoMapa(){
		if (!tipo){
			mapa = new Mapa(false);
			PainelDoJogo.updSala(mapa.criar());
		} else {
			mapa = new Mapa(true);
			salasAtivas = new HashMap<Integer, Salas>();
			ThreadUsers.updSala(mapa.criar());
		}
	}

	public static void mudaLevel(){
		if (!tipo){
			PainelDoJogo.aumentaLvl();
			criaNovoMapa();
		} else {
			criaNovoMapa();
		}
	}

	public static void avancaSala(){
		mapa.avancaSala();
		PainelDoJogo.updSala(mapa.getSalaAtual());
	}

	public static void voltaSala(){
		mapa.voltaSala();
		PainelDoJogo.updSala(mapa.getSalaAtual());
	}

	static MenuMorte morte;

	public static void reseta(){
		Jogo.resetaJogo(tipo);
	}

	public static void morre(){
		Menu.paraJogo();
		morte = new MenuMorte(tipo);
		mapa = null;
	}

	public static Salas avancaSala(int sala, int player){
		if (salasAtivas.get(sala) == null){
			salasAtivas.put(salasAtivas.size(), mapa.getSalas().get(sala));
			if (mapa.getSalas().get(sala).getInimigos() != null){
				mapa.getSalas().get(sala).setFoco(ThreadUsers.getLista().get(player).getPlayer());
			}
		}
		
		return mapa.getSalas().get(sala);
	}

	public static Salas voltaSala(int sala, int player){
		salasAtivas.put(salasAtivas.size(), mapa.getSalas().get(sala));
		return mapa.getSalas().get(sala);
	}
}
