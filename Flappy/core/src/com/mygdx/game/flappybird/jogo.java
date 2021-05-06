package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//Construção
	private SpriteBatch batch;     //tem metodo interno que assoscia as informações do conteudo que vai ser renderizado na tela
	private Texture [] passaros;   //pega as imagens do passaro
	private Texture fundo;         //pega a imagem do fundo

	//Movimenta
	private int movimentaY = 0;
	private int movimentaX = 0;
	private float variacao = 0;                         //variação de altura pra animação
	private float gravidade = 0;                        //pra fazer o passaro cair
	private  float posicaoInicialVerticalPassaro = 0;   //posiçao que o passaro vai iniciar

	//Tela
	private float larguraDispositivo;  //para colocar a largura do celular
	private float alturaDispositivo;   //para colocar a altura do celular



	@Override
	public void create () {

		batch = new SpriteBatch();                             //instanciando um obj a ser contruido
		passaros = new Texture[3];                             //instanciando a imagem na interface
		passaros[0] = new Texture("passaro1.png"); //pega a imagem 1
		passaros[1] = new Texture("passaro2.png"); //pega a imagem 2
		passaros[2] = new Texture("passaro3.png"); //pega a imagem 3
		fundo = new Texture("fundo.png");          //instanciando a imagem na interface

		larguraDispositivo = Gdx.graphics.getWidth();         //pega a largura do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();         //pega a altura do dispositivo
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;//posiciona o passaro no meio da tela

		//Metodo create: puxa e instancia objs na tela, monta a tela
	}

	@Override
	public void render () {
		batch.begin(); //inicializa a execução

		if(variacao > 3)       //mexe com a variação para mudar a animação
			variacao = 0;

		boolean toqueTela = Gdx.input.justTouched(); //verifica se tocou na tela

		if(Gdx.input.justTouched()){                 //impulsiona pra cima
			gravidade = -25;
		}

		if(posicaoInicialVerticalPassaro > 0 || toqueTela)                              //diminui a gravidade ao tocar na tela
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);    //instancia o fundo no celular utilizando o tamanho da tela passado como paramentro
		batch.draw(passaros[(int) variacao], 30, posicaoInicialVerticalPassaro);  //instacia o passaro no celular com a posição dele

		variacao += Gdx.graphics.getDeltaTime() * 10;      //pega os graficos do gdx e associa com a variaçao

		gravidade++;   //incrementa a gravidade
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
