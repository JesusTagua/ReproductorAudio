package com.example.tarea3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    // private VideoView vv = null;
    MediaPlayer mp;

    Button BtPlay, BtPause, BtParar,btAvanzar,btRetroceder;
    //Atributos para gestionar el grabador
    MediaRecorder mr = null;
    //Atributo para guardar el identificador del permiso (peligroso )de grabar el audio
    private static final int ID_REQUEST_RECORD_AUDIO_PERMISSION = 2222;
    //Atributo para saber si tengo los permisos
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    //Atributo con el layout donde voy a poner el boton
    LinearLayout ll = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Recuperacion del layout donde
        ll = findViewById(R.id.layout);
        //Creo el botón de grabación
        RecordButton recordButton = new RecordButton(this);

        //Añado el boton al layout
        ll.addView(recordButton);

        //Centro el botón en el layout poniendo la propiedad gravity a 1
        ll.setGravity(1);

        // vincular al objeto media player e elemento a reproducir
        mp = new MediaPlayer().create(this,R.raw.grabacion);

        //Recuperar los botones de la interfaz
        BtPlay =findViewById(R.id.BtPlay);
        BtPause =findViewById(R.id.BtPausar);
        BtParar =findViewById(R.id.BtParar);

        //Establecer el Listener
        BtPlay.setOnClickListener(this);
        BtPause.setOnClickListener(this);
        BtParar.setOnClickListener(this);



        //Recuperar los botones de la interdaz
        btAvanzar = findViewById(R.id.btAvanzar);
        btRetroceder = findViewById(R.id.btRetroceder);

        //Establecer los listener del avanzar y retroceder
        btAvanzar.setOnClickListener(this);
        btRetroceder.setOnClickListener(this);


        //Deshabilito todos los botones
        enableButton(true,false,false);


    }

    private void enableButton(boolean BtPlay,boolean BtPause,boolean BtParar) {
        this.BtPlay.setEnabled(BtPlay);
        this.BtPause.setEnabled(BtPause);
        this.BtParar.setEnabled(BtParar);

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.BtPlay) play();
        else if (v.getId()==R.id.BtPausar) pause();
        else if (v.getId()==R.id.BtParar) stop();

        // hacer la logica del retroceder y avanzar
        else if (v.getId()==R.id.btRetroceder) retrocede();
        else if (v.getId()==R.id.btAvanzar) avanza();

    }

    public void play(){
        //iniciar la reproducir
        mp.start();
        enableButton(false,true,true);
    }

    public void pause(){
        mp.pause();
        enableButton(true,false,true);
    }

    public void stop(){
        mp.stop();
        enableButton(true,false,false);
    }

    //Metodo para avanzar la reproduccion 5s
    public void avanza(){
        if(mp.getCurrentPosition()+5000<mp.getDuration()) {
            mp.seekTo(mp.getCurrentPosition() + 10000);
        }
        else{
            mp.seekTo(mp.getDuration());
        }
    }
    //Metodo para retroceder la reproduccion 5s
    public void retrocede(){
        if (mp.getCurrentPosition() - 5000 > 0) {
            mp.seekTo(mp.getCurrentPosition() - 5000);
        }else{
            mp.seekTo(0);
        }
    }

    public void startRecording() {
        // tengo que  comprobar si tengo el permiso para grabar
        if(ContextCompat.checkSelfPermission(this,permissions[0])== PackageManager.PERMISSION_GRANTED){
            mr = new MediaRecorder(this);
        //Seleccionar la fuente de audio (microfono)
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            //Selecionono formato de salida, en este caso 3gp
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //Ruta para guardar la grabación , este caso sería el almacenamiento Interno
            mr.setOutputFile(getFilesDir().getAbsolutePath()+ File.separator + "grabacion.3gp");
            //Establecer el codificador AMR que comprime de manera óptima el habla humana
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //preparamos la grabación
            try {
                mr.prepare();
            }
            catch (IOException e){
                e.printStackTrace();
                    }

                //iniciar la grabación
                mr.start();

        }
        else // si no tengo el permiso
            ActivityCompat.requestPermissions(this,permissions,ID_REQUEST_RECORD_AUDIO_PERMISSION);

    }
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode){
                case ID_REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
            }
            if(permissionToRecordAccepted) finish();
            else startRecording();
        }

    public void stopRecording() {
        //Detener la grabacion
        mr.stop();
        //vaciarlo
        mr.reset();
        //libero el objeto en memoria
        mr.release();
        mr = null;

    }
}