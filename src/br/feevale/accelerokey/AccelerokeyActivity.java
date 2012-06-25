package br.feevale.accelerokey;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class AccelerokeyActivity extends Activity {
    
	private Equilibrist equilibrist;
	
    private SensorManager mSensorManager;
    private PowerManager mPowerManager;
    private WakeLock mWakeLock;
	private EquilibristSensorEventListener equilibristSensorEventListener;
	private Gallery gallery;
	private TextView word;
	
	/* Text to speech code */
	public final static int TTS_CODE = 823832;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* sensor manager */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        equilibristSensorEventListener = new EquilibristSensorEventListener(getEquilibrist());
		mSensorManager.registerListener(equilibristSensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI);


        /* wake lock */
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        
        /* layout */
        setContentView(R.layout.main);

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight() / 3;
        
        gallery = (Gallery) findViewById(R.id.charactersGallery);
        gallery.setAdapter(new CharacterListAdapter(this, height, (display.getHeight() / 5)));
        
        word = (TextView) findViewById(R.id.word);
        
        Button backspace = (Button) findViewById(R.id.backspace);
        backspace.setTextSize(display.getHeight() / 5);
        backspace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = (String) word.getText();
				if (text.length() > 0){
					word.setText(text.substring(0, text.length() -1));
				}
			}
		});
        
        /* getting an available text to speech resource */
        Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_CODE);
        
        Button speak = (Button) findViewById(R.id.speak);
        speak.setTextSize(display.getHeight() / 5);
        speak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTts.setLanguage(Locale.US);
				mTts.speak((String) word.getText(), TextToSpeech.QUEUE_ADD, null);
			}
		});
    }
    
    private TextToSpeech mTts;
    
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == TTS_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                mTts = new TextToSpeech(this, new OnInitListener() {
					@Override
					public void onInit(int status) {
						if(status == TextToSpeech.SUCCESS){
							
						}else{
							
						}
					}
				});
            } else {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                    TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	mSensorManager.unregisterListener(equilibristSensorEventListener);
    }
    
    private Equilibrist getEquilibrist(){
    	if (equilibrist == null){
    		equilibrist = new Equilibrist(){
    			public void fall(CardinalDirection cardinalDirection) {
    				switch (cardinalDirection) {
					case EAST:
						moveOnXAxis(Direction.POSITIVE);
						break;
					case NORTH:
						moveOnYAxis(Direction.NEGATIVE);
						break;
					case SOUTH:
						moveOnYAxis(Direction.POSITIVE);
						break;
					case WEST:
						moveOnXAxis(Direction.NEGATIVE);
						break;
					default:
						break;
					}
    			}
    		};
    	}
    	return equilibrist;
    }

	public void moveOnXAxis(Direction xDirection) {
		int selectedItemPosition = gallery.getSelectedItemPosition();
		int newPosition = selectedItemPosition;
		if ( xDirection == Direction.NEGATIVE ){
			newPosition -= 1;
		}else{
			newPosition += 1;
		}
		if (newPosition < 0){
			newPosition = 0;
		}else if(newPosition >= gallery.getAdapter().getCount()){
			newPosition = gallery.getAdapter().getCount() - 1;
		}
		
		gallery.setSelection(newPosition, true);
		gallery.requestChildFocus(gallery.getChildAt(newPosition), gallery.getFocusedChild());
	}

	public void moveOnYAxis(Direction yDirection) {
		if(yDirection == Direction.NEGATIVE){
			String text = (String) gallery.getSelectedItem();
			word.setText(word.getText() + text);
		}
	}
    
}
