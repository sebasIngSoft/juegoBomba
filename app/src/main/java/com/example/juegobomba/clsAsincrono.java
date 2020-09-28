package com.example.juegobomba;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Johnny on 21/06/2019.
 */

/*
onPreExecute(). Se ejecutará antes del código principal de nuestra tarea. Se suele utilizar para
                preparar la ejecución de la tarea, inicializar la interfaz, etc.

doInBackground(). Contendrá el código principal de nuestra tarea.

onProgressUpdate(). Se ejecutará cada vez que llamemos al método publishProgress() desde el método
                    doInBackground().

onPostExecute(). Se ejecutará cuando finalice nuestra tarea, o dicho de otra forma, tras la
                 finalización del método doInBackground().

onCancelled(). Se ejecutará cuando se cancele la ejecución de la tarea antes de su finalización
               normal.
*/


/*Los parametros que estamos recibiendo al heredar del AsyncTask son:

      Parametro 1: El tipo de datos que recibiremos como entrada de la tarea en el método
                   doInBackground(). En este caso doInBackground() no recibirá ningún parámetro
                   de entrada (Void).
      Parametro 2: El tipo de datos con el que actualizaremos el progreso de la tarea, y que
                   recibiremos como parámetro del método onProgressUpdate() y que a su vez
                   tendremos que incluir como parámetro del método publishProgress(). En este
                   caso publishProgress() y onProgressUpdate() recibirán como parámetros datos de
                   tipo entero (Integer).
      Parametro 3: El tipo de datos que devolveremos como resultado de nuestra tarea, que será el
                   tipo de retorno del método doInBackground() y el tipo del parámetro recibido en
                   el método onPostExecute(). En este caso doInBackground() devolverá como retorno
                   un dato de tipo booleano y onPostExecute() también recibirá como parámetro un
                   dato del dicho tipo (Boolean).
*/
public class clsAsincrono extends AsyncTask<Void, Integer, Boolean> {
    TextView lblCrono;
    private Activity activity;
    private FrameLayout frmInicial,frmExplocion;
    private MediaPlayer audioExplocion;
    /*Variables globales con las cuales implemento los minutos y los segundos*/
    int minutes = 0;
    int seconds = 60;

    /*Constructor*/
    public clsAsincrono(Activity activity, TextView lblCrono, FrameLayout frmInicial, FrameLayout frmExplocion, MediaPlayer audioExplocion) {
        this.activity = activity;
        this.lblCrono = lblCrono;
        this.frmInicial = frmInicial;
        this.frmExplocion = frmExplocion;
        this.audioExplocion=audioExplocion;
    }

    private void detenerHilo()
    {
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {}
    }


    /*Antes de iniciar el proceso, reiniciamos los cronometros*/
    @Override
    protected void onPreExecute() {
        /*Antes de iniciar el proyecto le asigno el valor a la variable minutos*/
        /*Le seteo los valores al label de la ventana principal*/
        minutes = 2;
        String timeCrono;
        timeCrono = " "+minutes;
        timeCrono += " : 00";
        lblCrono.setText(timeCrono);
    }

    /*
    * El método doInBackground() se ejecuta en un hilo secundario (por tanto no podremos interactuar
    * con la interfaz), pero sin embargo todos los demás se ejecutan en el hilo principal, lo que
    * quiere decir que dentro de ellos podremos hacer referencia directa a nuestros controles de
    * usuario para actualizar la interfaz. Ademas si se llama al metodo publishProgress() este
    * automáticamente ejecuta el onProgressUpdate() que actualizara la interfaz si es necesario
    * */
    @Override
    protected Boolean doInBackground(Void... params) {
        /*Empieza en 0 para poder hacer que i siempre sea 1 y no se le aplica un auto incrementable*/
        for(int i=0; i<=minutes;) {
            detenerHilo();
            /*Si los segundos son iguales a 60 disminuye el valor de la variable minutes*/
            if(seconds==60)minutes-=1;
            /*Se llama a la funcion onProgressUpdate para actualizar la interfaz*/
            /*Siempre se le va a enviar i+1 ya que i siempre va a ser cero*/
            publishProgress(i+1);
            /*Se valida si se ha solicitado finalizar el proceso*/
            if(isCancelled())
                /*Se llama a la funcion onCancelled*/
                break;
        }

        /*Se le retorna true a la funcion onPostExecute*/
        /*Si no se cumple el for, se le asignan nuevos valores a minutes y seconds para finalizar el contador.*/
        minutes = 0;
        seconds = 1;
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        /*Se saca el progreso mandado y se actualiza el valor de la variable seconds restandole el progreso resivido en el método*/
        seconds = seconds-(values[0].intValue());
        String timeCrono;
        timeCrono = minutes+" : ";
        timeCrono+=seconds;
        if(seconds==0){
            if(minutes==0){
                minutes-=1;
            }else{
                seconds=60;
            }
        }
        lblCrono.setText(timeCrono);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        /*Tan pronto termine el proceso, se muestra un toast en la activity
        indincando que termino
        * el proceso*/
        if(result){
            frmInicial.setVisibility(View.GONE);
            frmExplocion.setVisibility(View.VISIBLE);
            audioExplocion.start();
        }
    }




    @Override
    protected void onCancelled() {
        /*Si se cancela el proceso se le indica al usuario*/
        Toast.makeText(activity, "Desactivado!",
                Toast.LENGTH_SHORT).show();
    }
}
