package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class jogo extends ApplicationAdapter {

    //Construção
    private SpriteBatch batch;    //tem metodo interno que assoscia as informações do conteudo que vai ser renderizado na tela.

    private Texture[] passaros;   //pega as imagens do passaro.
    private Texture fundo;        //pega a imagem do fundo.
    private Texture canoTopo;     //pega imagem do cano.
    private Texture canoBaixo;    //pega imagem do cano.
    private Texture gameOver;     //pega a imagem do game over.

    private int pontos = 0;       //pontos ao passar do cano.
    private int estadoJogo = 0;   //para depois alterar os estados do jogo.

    BitmapFont textoPontuacao;          //texto da pontuação.
    BitmapFont textoReiniciar;          //texto de reiniciar.
    BitmapFont textoMelhorPontuacao;    //texto de melhor pontuação.

    //Movimenta
    private int gravidade = 0;                        //para fazer o passaro cair.

    private float variacao = 0;                       //variação de altura pra animação.
    private float posicaoInicialVerticalPassaro = 0;  //posiçao que o passaro vai iniciar.
    private float posicaoCanoHorizontal = 0;          //posição do cano.
    private float posicaoCanoVertical;                //posição do cano
    private float espaçoEntreCanos;                   //espaço entre os canos.

    private boolean passouCano = false;               //se passou ou nao pelo cano.

    private Random random;                            //para randomizar os espaços dos canos.

    //Tela
    private float larguraDispositivo;  //para colocar a largura do celular.
    private float alturaDispositivo;   //para colocar a altura do celular.

    //Colisão
    private ShapeRenderer shapeRenderer;        //renderiza os colliders.
    private Circle circuloPassaro;              //formato collider do passaro.
    private Rectangle retanguloCanoCima;        //formato collider cano cima.
    private Rectangle retanguloCanoBaixo;       //formato collider cano baixo.


    @Override
    public void create() {


        inicializaTexturas();
        inicializaObjetos();

        //Metodo create: puxa e instancia objs na tela, monta a tela.
    }

    @Override
    public void render() {

        verificarEstadoJogo();
        validarPontos();
        desenharTexturas();
        detectarColisão();

        //Metodo render: imprime a parte de layout  e aplica as informações.
    }




    @Override
    public void dispose() {

        //Metodo dispose: entrega a aplicação, retorna dados.
    }

    private void inicializaTexturas() {

        passaros = new Texture[3];                             //instanciando a imagem na interface.
        passaros[0] = new Texture("passaro1.png"); //pega a imagem 1.
        passaros[1] = new Texture("passaro2.png"); //pega a imagem 2.
        passaros[2] = new Texture("passaro3.png"); //pega a imagem 3.

        fundo = new Texture("fundo.png");          //instanciando a imagem na interface.
        canoTopo = new Texture("cano_topo_maior.png");    //pega a imagem do cano.
        canoBaixo = new Texture("cano_baixo_maior.png");  //pega a imagem do cano.

        gameOver = new Texture("game_over.png");          //pega imagem do gameOver

    }

    private void inicializaObjetos() {

        batch = new SpriteBatch();                               //instanciando um obj a ser contruido.
        random = new Random();                                 //para randomizar os canos.

        larguraDispositivo = Gdx.graphics.getWidth();           //pega a largura do dispositivo.
        alturaDispositivo = Gdx.graphics.getHeight();           //pega a altura do dispositivo.
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;  //posiciona o passaro no meio da tela.
        posicaoCanoHorizontal = larguraDispositivo;             //iguala a posiçao do cano com o tamanho da tela.
        espaçoEntreCanos = 350;                                 //espaçamento entre os canos na tela.

        textoPontuacao = new BitmapFont();                      // falando que o texto é um bitmapFont.
        textoPontuacao.setColor(Color.WHITE);                   //colocando cor no texto.
        textoPontuacao.getData().setScale(10);                  //Tamanho da fonte do texto.

        textoMelhorPontuacao = new BitmapFont();                     // falando que o texto é um bitmapFont.
        textoMelhorPontuacao.setColor(Color.RED);                   //colocando cor no texto.
        textoMelhorPontuacao.getData().setScale(2);                 //Tamanho da fonte do texto.

        textoReiniciar = new BitmapFont();                      // falando que o texto é um bitmapFont.
        textoReiniciar.setColor(Color.GREEN);                   //colocando cor no texto.
        textoReiniciar.getData().setScale(2);                  //Tamanho da fonte do texto.

        shapeRenderer = new ShapeRenderer();                    //inicializa os render dos colliders
        circuloPassaro = new Circle();                          //imprime o collider circulo do passaro
        retanguloCanoCima = new Rectangle();                    //imprime o collider retangulo do cano
        retanguloCanoBaixo = new Rectangle();                  //imprime o collider retangulo do cano

    }

    private void verificarEstadoJogo() {

        boolean toqueTela = Gdx.input.justTouched();            //verifica se tocou na tela.

        if(estadoJogo == 0)   {                              //designa o estado inicial do jogo e muda com o toque na tela apenas uma vez

            if (Gdx.input.justTouched()) {                          //impulsiona pra cima.
                gravidade = -25;
                estadoJogo = 1;
            }
        } else if (estadoJogo == 1){                        //se o estado do jogo foi trocado no metodo de cima, deixa tocar mais vezes pra voar

            if (Gdx.input.justTouched()) {                          //impulsiona pra cima.
                gravidade = -25;
            }

            posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;       //movimentação e velocidade do cano que vai vir na direção do player.

            if(posicaoCanoHorizontal < - canoBaixo.getWidth()){               //ve a largura da tela pra avançar dps que acabar a tela.
                posicaoCanoHorizontal = larguraDispositivo;
                posicaoCanoVertical = random.nextInt(400) -200;            //randomiza os espaçamentos
                passouCano = false;                                           //volta o cano pra false.
            }

            if (posicaoInicialVerticalPassaro > 0 || toqueTela)                               //associa o touch com a gravidade.
                posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

            gravidade++;   //incrementa a gravidade.
        } else if (estadoJogo == 2){

        }
    }

    private void desenharTexturas() {

        batch.begin();   //inicializa a execução.

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);          //instancia o fundo no celular utilizando o tamanho da tela passado como parametro.
        batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);    //instacia o passaro no celular com a posição dele e animação.

        //batch.draw(canoBaixo, posicaoCanoHorizontal -100, alturaDispositivo/2 - canoBaixo.getHeight() - espaçoEntreCanos/2 + posicaoCanoVertical);  //instancia o cano na tela com espaço entre eles.
        //batch.draw(canoTopo, posicaoCanoHorizontal -100,alturaDispositivo/2 + espaçoEntreCanos + posicaoCanoVertical);                              //instancia o cano na tela com espaço entre eles.

        batch.draw(canoBaixo, posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);
		
        
        textoPontuacao.draw(batch,String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);  //n tem batch no começo pq esta vindo de um bitmapFont e desenha a pontuação na tela.

        if(estadoJogo == 2){                 //se o estado do jogo for 2
            batch.draw(gameOver, larguraDispositivo / 2 +200 - gameOver.getWidth(), alturaDispositivo / 2);                                                               // desenha o game over na tela com os parametros passados
            textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR!", larguraDispositivo / 2 -250, alturaDispositivo / 2 - gameOver.getHeight() / 2);               //inicia o escrito de reiniciar na tela com os parametros passados
            textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO É: 0 PONTOS", larguraDispositivo / 2 -250, alturaDispositivo / 2 - gameOver.getHeight() * 2);      //inicia o escrito de melhor pontuação com os parametros passados
        }

        batch.end();   //termina a execução.

    }
    private void validarPontos() {

        if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){                  //condicional ao passar do cano.
           if(!passouCano){                                                       // verifica se passou do cano.
               pontos++;                                                          //incrementa os pontos.
               passouCano = true;                                                 //se passou do cano, agora é verdadeiro.
           }
        }

        variacao += Gdx.graphics.getDeltaTime() * 10;
        if (variacao > 3)                                         //mexe com a variação para mudar a animação.
            variacao = 0;
    }
    private void detectarColisão() {

        circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);                                     //associando o circulo do collider ao passaro.
        retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espaçoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());     //associando o retangulo do collider ao cano baixo.
        retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 + espaçoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight() );                               //associando o retangulo do collider ao cano topo.

        boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);           //se bateu ou nao.
        boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);         //se bateu ou nao.

        if(colisaoCanoBaixo || colisaoCanoCima){                      //mensagem de bateu no cano.
            Gdx.app.log("log", "Colidiu");
            estadoJogo = 2;
        }
    }
}
