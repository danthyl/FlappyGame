package com.mygdx.game.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class jogo extends ApplicationAdapter {

    //Construção
    private SpriteBatch batch;    //tem metodo interno que assoscia as informações do conteudo que vai ser renderizado na tela
    private Texture[] passaros;   //pega as imagens do passaro
    private Texture fundo;        //pega a imagem do fundo
    private Texture canoTopo;     //pega imagem do cano
    private Texture canoBaixo;    //pega imagem do cano

    //Movimenta
    private int movimentaX = 0;
    private float variacao = 0;                         //variação de altura pra animação
    private int gravidade = 0;                        //pra fazer o passaro cair
    private float posicaoInicialVerticalPassaro = 0;   //posiçao que o passaro vai iniciar
    private float posicaoCanoHorizontal = 0;               //posição do cano
    private float espaçoEntreCanos;                    //espaço entre os canos
    private float posicaoCanoVertical;

    private Random random;                            //randomiza os espaços dos canos


    //Tela
    private float larguraDispositivo;  //para colocar a largura do celular
    private float alturaDispositivo;   //para colocar a altura do celular


    @Override
    public void create() {


        inicializaTexturas();
        inicializaObjetos();

        //Metodo create: puxa e instancia objs na tela, monta a tela
    }

    @Override
    public void render() {


        verificarEstadoJogo();
        desenharTexturas();



        //Metodo render: imprime a parte de layout  e aplica as informações
    }

    @Override
    public void dispose() {
        //Metodo dispose: entrega a aplicação, retorna dados
    }

    private void inicializaTexturas() {

        batch = new SpriteBatch();                             //instanciando um obj a ser contruido
        random = new Random();                                 //para randomizar os canos

        passaros = new Texture[3];                             //instanciando a imagem na interface
        passaros[0] = new Texture("passaro1.png"); //pega a imagem 1
        passaros[1] = new Texture("passaro2.png"); //pega a imagem 2
        passaros[2] = new Texture("passaro3.png"); //pega a imagem 3

        fundo = new Texture("fundo.png");          //instanciando a imagem na interface

        canoTopo = new Texture("cano_topo_maior.png");    //pega a imagem do cano
        canoBaixo = new Texture("cano_baixo_maior.png");  //pega a imagem do cano


    }

    private void inicializaObjetos() {


        larguraDispositivo = Gdx.graphics.getWidth();         //pega a largura do dispositivo
        alturaDispositivo = Gdx.graphics.getHeight();         //pega a altura do dispositivo
        posicaoInicialVerticalPassaro = alturaDispositivo / 2;//posiciona o passaro no meio da tela
        posicaoCanoHorizontal = larguraDispositivo;           //iguala a posiçao do cano com o tamanho da tela
        espaçoEntreCanos = 150;                               //espaçamento entre os canos na tela
    }

    private void verificarEstadoJogo() {

       posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;        //velocidade do cano que vai vir na direção do player
       if(posicaoCanoHorizontal < - canoBaixo.getWidth()){               //ve a largura da tela pra avançar dps que acabar a tela
           posicaoCanoHorizontal = larguraDispositivo;
           posicaoCanoHorizontal = random.nextInt(400) -200;
       }


        boolean toqueTela = Gdx.input.justTouched();            //verifica se tocou na tela

        if (Gdx.input.justTouched()) {                           //impulsiona pra cima
            gravidade = -25;
        }

        if (posicaoInicialVerticalPassaro > 0 || toqueTela)                               //associal o touch com a gravidade
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

        variacao += Gdx.graphics.getDeltaTime() * 10;
        if (variacao > 3)                                         //mexe com a variação para mudar a animação
            variacao = 0;

        gravidade++;   //incrementa a gravidade
        movimentaX++;  //incrementa a movimentação
    }

    private void desenharTexturas() {

        batch.begin();                                            //inicializa a execução
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);           //instancia o fundo no celular utilizando o tamanho da tela passado como paramentro
        batch.draw(passaros[(int) variacao], 0, posicaoInicialVerticalPassaro);     //instacia o passaro no celular com a posição dele

        batch.draw(canoBaixo, posicaoCanoHorizontal -100, alturaDispositivo/2 - canoBaixo.getHeight() - espaçoEntreCanos/2 + posicaoCanoVertical);  //instancia o cano na tela com espaço entre eles
        batch.draw(canoTopo, posicaoCanoHorizontal -100,alturaDispositivo/2 + espaçoEntreCanos + posicaoCanoVertical);                             //instancia o cano na tela com espaço entre eles

        batch.end();   //termina a execução

    }
}
