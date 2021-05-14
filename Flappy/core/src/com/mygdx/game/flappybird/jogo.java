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
    private int pontos = 0;       //pontos ao passar do cano.
    BitmapFont textoPontuacao;    //texto da pontuação.

    //Movimenta
    private float variacao = 0;                       //variação de altura pra animação.
    private int gravidade = 0;                        //para fazer o passaro cair.
    private float posicaoInicialVerticalPassaro = 0;  //posiçao que o passaro vai iniciar.
    private float posicaoCanoHorizontal = 0;          //posição do cano.
    private float posicaoCanoVertical;
    private float espaçoEntreCanos;                   //espaço entre os canos.
    private boolean passouCano = false;               //se passou ou nao pelo cano.
    private Random random;                            //para randomizar os espaços dos canos.

    //Tela
    private float larguraDispositivo;  //para colocar a largura do celular.
    private float alturaDispositivo;   //para colocar a altura do celular.

    //Colisão
    private ShapeRenderer shapeRenderer;        // ?????????????????????????
    private Circle circuloPassaro;              //formato collider do passaro
    private Rectangle retanguloCanoCima;        //formato collider cano cima
    private Rectangle retanguloCanoBaixo;       //formato collider cano baixo


    @Override
    public void create() {


        inicializaTexturas();
        inicializaObjetos();

        //Metodo create: puxa e instancia objs na tela, monta a tela.
    }

    @Override
    public void render() {

        verificarEstadoJogo();
        desenharTexturas();
        validarPontos();
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

    }

    private void verificarEstadoJogo() {

       posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;       //movimentação e velocidade do cano que vai vir na direção do player.

       if(posicaoCanoHorizontal < - canoBaixo.getWidth()){               //ve a largura da tela pra avançar dps que acabar a tela.
           posicaoCanoHorizontal = larguraDispositivo;
           posicaoCanoVertical = random.nextInt(400) -200;            //randomiza os espaçamentos
           passouCano = false;                                           //volta o cano pra false.
       }

        boolean toqueTela = Gdx.input.justTouched();            //verifica se tocou na tela.

        if (Gdx.input.justTouched()) {                          //impulsiona pra cima.
            gravidade = -25;
        }

        if (posicaoInicialVerticalPassaro > 0 || toqueTela)                               //associa o touch com a gravidade.
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

        variacao += Gdx.graphics.getDeltaTime() * 10;
        if (variacao > 3)                                         //mexe com a variação para mudar a animação.
            variacao = 0;

        gravidade++;   //incrementa a gravidade.
    }

    private void desenharTexturas() {

        batch.begin();   //inicializa a execução.

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);          //instancia o fundo no celular utilizando o tamanho da tela passado como parametro.
        batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);    //instacia o passaro no celular com a posição dele e animação.

        batch.draw(canoBaixo, posicaoCanoHorizontal -100, alturaDispositivo/2 - canoBaixo.getHeight() - espaçoEntreCanos/2 + posicaoCanoVertical);  //instancia o cano na tela com espaço entre eles.
        batch.draw(canoTopo, posicaoCanoHorizontal -100,alturaDispositivo/2 + espaçoEntreCanos + posicaoCanoVertical);                              //instancia o cano na tela com espaço entre eles.

        textoPontuacao.draw(batch,String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);  //n tem batch no começo pq esta vindo de um bitmapFont e desenha a pontuação na tela.

        batch.end();   //termina a execução.

    }
    private void validarPontos() {

        if(posicaoCanoHorizontal < 50 - passaros[0].getWidth()){                  //condicional ao passar do cano.
           if(!passouCano){                                                       // verifica se passou do cano.
               pontos++;                                                          //incrementa os pontos.
               passouCano = true;                                                 //se passou do cano, agora é verdadeiro.
           }

        }

    }
    private void detectarColisão() {

        circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);                                     //associando o circulo do collider ao passaro.
        retanguloCanoCima.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoTopo.getHeight() - espaçoEntreCanos / 2 + posicaoCanoVertical, canoTopo.getWidth(), canoTopo.getHeight() );        //associando o retangulo do collider ao cano topo.
        retanguloCanoBaixo.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espaçoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());     //associando o retangulo do collider ao cano baixo.

        boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);           //se bateu ou nao.
        boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);         //se bateu ou nao.

        if(colisaoCanoBaixo || colisaoCanoCima){                      //mensagem de bateu no cano.
            Gdx.app.log("log", "bateu");
        }
    }
}
