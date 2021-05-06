package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//Construção
	private SpriteBatch batch; //tem metodo interno que assoscia as informações do conteudo que vai ser renderizado na tela
	private Texture passaro;   //pega a imagem do passaro
	private Texture fundo;     //pega a imagem do fundo

	//Movimenta
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Tela
	private float larguraDispositivo;  //para colocar a largura do celular
	private float alturaDispositivo;   //para colocar a altura do celular

	
	@Override
	public void create () {

		batch = new SpriteBatch();                            //instanciando um obj a ser contruido
		passaro = new Texture("passaro1.png");    //instanciando a imagem na interface
		fundo = new Texture("fundo.png");         //instanciando a imagem na interface

		larguraDispositivo = Gdx.graphics.getWidth();         //pega a largura do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();         //pega a altura do dispositivo

		//Metodo create: puxa e instancia objs na tela, monta a tela
	}

	@Override
	public void render () {
		batch.begin(); //inicializa a execução

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);  //instancia o fundo no celular utilizando o tamanho da tela passado como paramentro
		batch.draw(passaro, 50, 50, movimentaX, movimentaY);             //instacia o passaro no celular e aplica a movimentação que vai ser criada                        //instancia o passaro na tela com o posicionamento passado no paremtro

		movimentaY++;  //incrementa a movimentação no eixo Y
		movimentaX++;  //incrementa a movimentação no eixo X


		batch.end();   //termina a execução

		//Metodo render: imprime a parte de layout  e aplica as informações
	}
	
	@Override
	public void dispose () {
		//Metodo dispose: entrega a aplicação, retorna dados
	}
}
