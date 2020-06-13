package com.jesusgarcia.practicadesensoresfutbolitopocket;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView equipoRealMadrid, equipoBarcelona, marcadorRealMadrid, marcadorBarcelona;
    ImageView balon;

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    int ancho = 0, alto = 0, puntajeRealMadrid = 0, puntajeBarcelona = 0;
    DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equipoRealMadrid = findViewById(R.id.lblRealMadrid);
        equipoBarcelona = findViewById(R.id.lblBarcelona);
        marcadorRealMadrid = findViewById(R.id.lblMarcadorRealMadrid);
        marcadorBarcelona = findViewById(R.id.lblMarcadorBarcelona);
        balon = findViewById(R.id.imgBalon);

        /* Obtenemos las medidas de la pantalla en que se está mostrando. */
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        /* Invocamos el sensor de aceleración por defecto. */
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if (sensor == null) {
            Toast.makeText(
                    this,
                    "El dispositivo no cuenta con sensor de acelerometro.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
        }

        /* TODO: Escuchador del sensor de acelerometro que interpreta los cambios de movimiento. */
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0]; // Valores en el eje X de movimiento.
                float y = event.values[1]; // Valores en el eje Y de movimiento.
                float z = event.values[2]; // Valores en el eje Z de movimiento.

                if (x < (-1)) {
                    if (balon.getX() < (ancho - balon.getWidth())) {
                        balon.setX(balon.getX() + 5);
                    }
                } else if (x > 1) {
                    if (balon.getX() > 1) {
                        balon.setX(balon.getX() - 5);
                    }
                }

                if (y < (-1)) {
                    if (balon.getY() > 0) {
                        balon.setY(balon.getY() - 5);
                    } else {
                        if ((balon.getX() > 400) && (balon.getX() < 580)) {
                            Gol();
                            puntajeBarcelona++;
                            marcadorBarcelona.setText(String.valueOf(puntajeBarcelona));
                        }
                    }
                } else if (y > 1) {
                    if (balon.getY() < ((ancho - balon.getHeight()) + 625)) {
                        balon.setY(balon.getY() + 5);
                    } else {
                        if ((balon.getX() > 400) && (balon.getX() < 580)) {
                            Gol();
                            puntajeRealMadrid++;
                            marcadorRealMadrid.setText(String.valueOf(puntajeRealMadrid));
                        }
                    }
                }

                if (z < (-1)) {
                    balon.setMaxWidth(100);
                    balon.setMaxHeight(100);
                } else if (z > 1) {
                    balon.setMaxWidth(100);
                    balon.setMaxHeight(100);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        /*TODO: registra el evento de movimiento, indica el sensor a usar y el delay del sensor. */
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                sensorManager.SENSOR_DELAY_FASTEST
        );
    }

    /**
     * Cuando se realiza un Gol, el balón vuelve a la posición de inicio (al centro de la pantalla).
     */
    private void Gol() {
        balon.setX(540);
        balon.setY(888);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Suspende el registro de movimiento del sensor de aceleración porque cambió el estado
     * a onPause.
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    /**
     * Reanuda nuevamente el registro del sensor de aceleración, llama al metodo Gol para
     * que inicie con la posición del balón en el centro de la pantalla.
     */
    @Override
    protected void onResume() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                sensorManager.SENSOR_DELAY_FASTEST
        );
        super.onResume();
        Gol();
    }
}