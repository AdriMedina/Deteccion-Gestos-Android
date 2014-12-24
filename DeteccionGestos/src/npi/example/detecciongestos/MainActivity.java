package npi.example.detecciongestos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;



public class MainActivity extends Activity {

	//Etiquetas para la salida de datos.
	private static final String DEBUG_TAG = "SalidaEvento";
	private static final String COORD_TAG = "Coordenadas";
	
	//Variables necesarias para comprobar gestos.
	float x_inicial, y_inicial, x_actual, y_final;
	boolean correcto = true;

	
	/**
	 * Función Android para la creación de la actividad principal de la aplicación
	 *
	 * @param savedInstanceState 
	 *
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//ImageView apagado = (ImageView) findViewById(R.id.off);
		//ImageView encendido = (ImageView) findViewById(R.id.on);

	}
	
	
	/**
	 * Función propia de Android que captura los eventos táctiles de la pantalla.
	 * Obtiene la acción de la función MotionEventCompat.getActionMasked().
	 * Si el dedo está pulsando la pantalla (ACTION_DOWN):
	 * 		guardamos las coordenadas de la pantalla en la que se ha pulsado.
	 * Si el dedo se está deslizando sobre la pantalla (ACTION_MOVE):
	 * 		comprobamos que el movimiento es correcto, es decir, que el dedo se desliza 
	 * 		hacia arriba o hacia abajo.
	 * Si el dedo se ha levantado de la pantalla (ACTION_UP):
	 * 		si el movimiento ha sido correcto en ACTION_MOVE, entonces comprobaremos que ha habido un
	 * 		deslizamiento lo suficientemente grande sobre el eje Y.
	 * 
	 * 
	 * @param evento Objeto que captura el evento sobre la pantalla tactil
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 * @return true 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evento)
	{
		// Obtengo la acciÃ³n que se ha detectado en la pantalla.
		int accion = MotionEventCompat.getActionMasked(evento);
		
		// Referencio el fondo de la aplicaciÃ³n en un objeto tipo View.
		View vista_actual = findViewById(R.id.fondo);

		// SegÃºn la acciÃ³n que se ha detectado en la pantalla...
		switch(accion){
			// Si se acaba de pulsar la pantalla...
			case(MotionEvent.ACTION_DOWN):
				// Obtengo las coordenadas X,Y donde se produjo la pulsaciÃ³n.
				x_inicial = evento.getX(evento.getPointerId(0));
				y_inicial = evento.getY(evento.getPointerId(0));
				// Suponemos que el movimiento es correcto.
				correcto = true;
				
				// Salidas por pantalla.
				Log.d(DEBUG_TAG,"Se ha pulsado la pantalla");
				Log.d(COORD_TAG, Float.toString(x_inicial));
				Log.d(COORD_TAG, Float.toString(y_inicial));
				
			// Si el dedo se estÃ¡ moviendo por la pantalla...
			case (MotionEvent.ACTION_MOVE) :
				// Obtengo la X actual del dedo.
				x_actual = evento.getX(evento.getPointerId(0));
				// Me aseguro de que se hace una linea recta con el dedo dentro
				// de un margen de error. En caso de que sobrepase el margen
				// el gesto se marcarÃ¡ como invÃ¡lido hasta que se vuelva a pulsar
				// la pantalla para iniciar un nuevo gesto.
				if(x_actual>=x_inicial+50 || x_actual <= x_inicial-50)
					correcto = false;
				
				// Salidas por pantalla.
				if(correcto)
					Log.d(DEBUG_TAG,"Correcto.");
				else
					Log.d(DEBUG_TAG,"No-Correcto.");
				
			// Si la acciÃ³n detectada es que se ha dejado de tocar la pantalla...
			case(MotionEvent.ACTION_UP):
				// Salida por pantalla
				Log.d(DEBUG_TAG,"Se ha dejado de pulsar la pantalla");

				// Si el movimiento ha sido correcto...
				if(correcto){
					// Obtengo la coordenada Y final del gesto.
					y_final = evento.getY(evento.getPointerId(0));
					
					// Me aseguro de que la linea recta ha sido lo suficientemente larga.
					// Dependiendo de la direcciÃ³n, se visualizarÃ¡ la acciÃ³n de encender
					// o apagar la luz.
					if(y_final >= y_inicial+200){
						vista_actual.setBackgroundColor(getResources().getColor(R.color.encendido));
						ImageView interruptor = (ImageView) findViewById(R.id.interruptor);
						interruptor.setImageResource(R.drawable.on);
					}
				
					if(y_final <= y_inicial-200){
						vista_actual.setBackgroundColor(getResources().getColor(R.color.apagado));
						ImageView interruptor = (ImageView) findViewById(R.id.interruptor);
						interruptor.setImageResource(R.drawable.off);
					}
				}
		}

		return true;
	}
	
}
