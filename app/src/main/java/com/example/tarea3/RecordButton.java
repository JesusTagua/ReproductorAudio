package com.example.tarea3;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

public class RecordButton extends androidx.appcompat.widget.AppCompatButton {
    //Variable de una clase para saber si estoy grabando
    boolean estaGrabando = true;
    //Atributo para la actividad deonde voy a crear dinámicamente el boton
    MainActivity activity;
    // Atributo para el listener del evento de click que inicia la grabacion o la detiene
    OnClickListener clicker = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (estaGrabando){
                //Cambio el texto si esta grabando solo permito que se haga click
                //para detener la creacion
                setText("Parar grabacion");
                estaGrabando=false;
                //Poner a grabar
                activity.startRecording();
            }else{
                setText("Comenzar grabación");
                estaGrabando=true;
                activity.stopRecording();
            }
        }
    };

    public RecordButton(@NonNull Context context) {
        super(context);
        activity = (MainActivity) context;
        setText("Comenzar grabación");
        setOnClickListener(clicker);
    }
}
