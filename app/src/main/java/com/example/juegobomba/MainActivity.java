package com.example.juegobomba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

        //hilos a usar
        clsAsincrono tarea1;
        clsAsincrono tarea2;
        //cronometros de las bombas
        TextView progressBom1;
        TextView progressBom2;
        //sonidos para las interfaz
        private MediaPlayer audioExplocion,audioReloj,audioGanaste;
        //botones de colores
        Button btnNegro1,btnNegro2,btnRojo1,btnRojo2,btnAzul1,btnAzul2;
        //imagenes que se van a mostrar al desactivar la bomba
        ImageView imgDesactivado1, imgDesactivado2;
        //frames que se van a ir rotan entre ellos dependiendo del suceso
        FrameLayout frmInicial,frmExplocion,frmGanaste,frmBtnBomba1,frmBtnBomba2;
        //variables para saber que bomba se ha desactivado
        private boolean validar1,validar2;
        //numeros aleatorios para asignarles a los colores de los botones
        int bomba1;
        int bomba2;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            progressBom1 = (TextView) findViewById(R.id.progressBom1);
            progressBom2 = (TextView) findViewById(R.id.progressBom2);

            btnNegro1 = (Button) findViewById(R.id.btnNegro1);
            btnNegro2 = (Button) findViewById(R.id.btnNegro2);
            btnRojo1 = (Button) findViewById(R.id.btnRojo1);
            btnRojo2 = (Button) findViewById(R.id.btnRojo2);
            btnAzul1 = (Button) findViewById(R.id.btnAzul1);
            btnAzul2 = (Button) findViewById(R.id.btnAzul2);

            imgDesactivado1 = (ImageView) findViewById(R.id.imgDesactivado1);
            imgDesactivado2 = (ImageView) findViewById(R.id.imgDesactivado2);

            frmInicial = (FrameLayout) findViewById(R.id.frmInicial);
            frmExplocion = (FrameLayout) findViewById(R.id.frmExplocion);
            frmGanaste = (FrameLayout) findViewById(R.id.frmGanaste);
            frmBtnBomba1 = (FrameLayout) findViewById(R.id.frmBtnBomba1);
            frmBtnBomba2 = (FrameLayout) findViewById(R.id.frmBtnBomba2);

            audioExplocion = MediaPlayer.create(this,R.raw.explosion);
            audioReloj = MediaPlayer.create(this,R.raw.reloj);
            audioGanaste = MediaPlayer.create(this,R.raw.youwin);

            validar1=false;
            validar2=false;

            bomba1=(int)(Math.random()*3)+1;
            bomba2=(int)(Math.random()*3)+1;

            System.out.println("---------------------------BOMBA 1 ="+bomba1+" y BOMBA 2 ="+bomba2);

            clickHilosMultiples();
        }

        public void reiniciar(View view){
            Intent intent = new Intent(this , MainActivity.class);
            //llamamos a la actividad
            startActivity(intent);
            //finalizamos la actividad actual
            finish();

        }

        public void DetenerHilo(View view){
            Button boton = view.findViewById(view.getId());

                if(boton==btnNegro1 || boton==btnRojo1 || boton==btnAzul1){
                    validar1=validarBomba1(boton);
                    /*llamo el metodo para saber si ya desactivo las dos bombas y gano*/
                    isGanador();
                }else{
                    validar2=validarBomba2(boton);
                    isGanador();
                }
        }
        /*Se detiene el hilo*/
        public void clickCancelarClaseAsincrona(clsAsincrono tarea) {
            tarea.cancel(true);
        }

        public boolean validarBomba1(Button boton){
            if (bomba1==1 && boton == btnNegro1){
                imgDesactivado1.setVisibility(View.VISIBLE);
                frmBtnBomba1.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea1);
                return true;
            }else if(bomba1 == 2 &&  boton == btnRojo1){
                imgDesactivado1.setVisibility(View.VISIBLE);
                frmBtnBomba1.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea1);
                return true;
            }else if(bomba1 == 3 && boton == btnAzul1){
                imgDesactivado1.setVisibility(View.VISIBLE);
                frmBtnBomba1.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea1);
                return true;
            }else{
                frmExplocion.setVisibility(View.VISIBLE);
                frmInicial.setVisibility(View.GONE);
                audioReloj.stop();
                audioExplocion.start();
                return false;
            }
        }

        public boolean validarBomba2(Button boton){
            if (bomba2==1 && boton == btnNegro2){
                imgDesactivado2.setVisibility(View.VISIBLE);
                frmBtnBomba2.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea2);
                return true;
            }else if(bomba2 == 2 &&  boton == btnRojo2){
                imgDesactivado2.setVisibility(View.VISIBLE);
                frmBtnBomba2.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea2);
                return true;
            }else if(bomba2 == 3 && boton == btnAzul2){
                imgDesactivado2.setVisibility(View.VISIBLE);
                frmBtnBomba2.setVisibility(View.INVISIBLE);
                clickCancelarClaseAsincrona(tarea2);
                return true;
            }else{
                frmExplocion.setVisibility(View.VISIBLE);
                frmInicial.setVisibility(View.GONE);
                audioReloj.stop();
                audioExplocion.start();
                return false;
            }
        }

        public void isGanador(){
            if (validar1==true && validar2==true){
                audioReloj.stop();
                audioGanaste.start();
                frmInicial.setVisibility(View.GONE);
                frmGanaste.setVisibility(View.VISIBLE);
            }
        }

        /**************END USO DE LA CLASE ASYNCTASK, OPCION RECOMENDADA*****************************/


        public void clickHilosMultiples(){
            try {
                tarea1.onCancelled();
                tarea2.onCancelled();
            }catch (Exception e){}

            /*Se definen los hilos*/
            tarea1 = new clsAsincrono(MainActivity.this, progressBom1,frmInicial,frmExplocion,audioExplocion);
            tarea2 = new clsAsincrono(MainActivity.this, progressBom2,frmInicial,frmExplocion,audioExplocion);

            /*Se valida si la version es mayor a honeycomb (3.0)*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                /*Si es mayor lo metemos al pool de hilos, (Son maximo 5)*/
                /*executeOnExecutor(PoolDeHilos, parametros si se desean mandar)*/
                tarea1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
                tarea2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);
                audioReloj.start();
            } else {
                /*Si no ejecute los hilos normalmente.*/
                tarea1.execute();
                tarea2.execute();
                audioReloj.start();
            }
        }
    }