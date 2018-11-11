/*
 *  Copyright 2016 Zoraida Callejas, Michael McTear and David Griol
 *
 *  This file is part of the Conversandroid Toolkit, from the book:
 *  The Conversational Interface, Michael McTear, Zoraida Callejas and David Griol
 *  Springer 2016 <https://github.com/zoraidacallejas/ConversationalInterface/>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *   along with this program. If not, see <http://www.gnu.org/licenses/>. 
 */

package com.p207.npi.Museo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RichASR: App with a rich control of the ASR using the SpeechRecognizer class
 * and implementing the RecognitionListener interface
 * (it is an improvement over the SimpleASR app)
 * 
 * Simple demo in which the user speaks and the recognition results
 * are showed in a list along with their confidence values
 *
 * @author Zoraida Callejas, Michael McTear, David Griol
 * @version 3.0, 09/25/18
 *
 */

public class RichASR extends Activity implements RecognitionListener{


	private SpeechRecognizer myASR;

	// Default values for the language model and maximum number of recognition results
	// They are shown in the GUI when the app starts, and they are used when the user selection is not valid
	private final static int DEFAULT_NUMBER_RESULTS = 10;
	private final static String DEFAULT_LANG_MODEL = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;

	private String languageModel = RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH;

	private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 22;
	private final static String LOGTAG = "RICHASR";
    static int RESULT_OK=0;
    static int RESULT_NOT_OK=-1;
    final static int REQUEST_RECORD = 98;
    private long startListeningTime = 0; // To skip errors (see onError method)
	
	/**
	 * Sets up the activity initializing the GUI and the ASR
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_asr);
        //Initialize ASR
        initASR();
        Locale locale = new Locale("spa", "ESP");
        listen(locale,languageModel, 4);
	}

	/* ***************************************************************************************
	 MANAGE ASR
	 */

	/**
	 * Creates the speech recognizer instance if it is available
	 * */
	public void initASR() {

		// find out whether speech recognition is supported
		List<ResolveInfo> intActivities = this.getPackageManager().queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        ////To avoid running on a simulated device
        //if("generic".equals(Build.BRAND.toLowerCase())){
        //	Toast toast = Toast.makeText(getApplicationContext(),"Virtual device: "+R.string.asr_notsupported, Toast.LENGTH_SHORT);
        //	toast.show();
        //	Log.d(LOGTAG, "ASR attempt on virtual device");
        // }

        // Another way of finding out whether ASR is supported (compared with the SimpleASR project)
        if (intActivities.size() != 0) {
            myASR = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
            myASR.setRecognitionListener(this);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.asr_no_soportado), Toast.LENGTH_SHORT);
            toast.show();
            Log.d(LOGTAG, "ASR not supported");
        }

        Log.i(LOGTAG, "ASR initialized");
	}

	/**
	 * Starts speech recognition after checking the ASR parameters
	 *
	 * @param language Language used for speech recognition (e.g. Locale.ENGLISH)
	 * @param languageModel Type of language model used (free form or web search)
	 * @param maxResults Maximum number of recognition results
	 */
	public void listen(final Locale language, final String languageModel, final int maxResults)
	{
		if((languageModel.equals(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) || languageModel.equals(RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH)) && (maxResults>=0))
		{
			// Check we have permission to record audio
			checkASRPermission();

			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

			// Specify the calling package to identify the application
			intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
			//Caution: be careful not to use: getClass().getPackage().getName());

			// Specify language model
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, languageModel);

			// Specify how many results to receive. Results listed in order of confidence
			intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);

			// Specify recognition language
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);

            Log.i(LOGTAG, "Going to start listening...");
            this.startListeningTime = System.currentTimeMillis();
			myASR.startListening(intent);

		}
		else {
			Log.e(LOGTAG, "Invalid params to listen method");
		}

	}

	/**
	 * Checks whether the user has granted permission to the microphone. If the permission has not been provided,
	 * it is requested. The result of the request (whether the user finally grants the permission or not)
	 * is processed in the onRequestPermissionsResult method.
	 *
	 * This is necessary from Android 6 (API level 23), in which users grant permissions to apps
	 * while the app is running. In previous versions, the permissions were granted when installing the app
	 * See: http://developer.android.com/intl/es/training/permissions/requesting.html
	 */
	public void checkASRPermission() {
		if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
				!= PackageManager.PERMISSION_GRANTED) {

			// If  an explanation is required, show it
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))
				Toast.makeText(getApplicationContext(), R.string.asr_permission, Toast.LENGTH_SHORT).show();

			// Request the permission.
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
					MY_PERMISSIONS_REQUEST_RECORD_AUDIO); //Callback in "onRequestPermissionResult"
		}
	}

	/**
	 * Processes the result of the record audio permission request. If it is not granted, the
	 * abstract method "onRecordAudioPermissionDenied" method is invoked. Such method must be implemented
	 * by the subclasses of VoiceActivity.
	 * More info: http://developer.android.com/intl/es/training/permissions/requesting.html
	 * */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		if(requestCode == MY_PERMISSIONS_REQUEST_RECORD_AUDIO) {
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				Log.i(LOGTAG, "Record audio permission granted");
			else {
				Log.i(LOGTAG, "Record audio permission denied");
				Toast.makeText(getApplicationContext(), R.string.asr_permission_notgranted, Toast.LENGTH_SHORT).show();
				Intent ReturnIntent = new Intent();
				setResult(RESULT_NOT_OK,ReturnIntent);
			}
		}
	}

	/**
	 * Stops listening to the user
	 */
	public void stopListening(){
        myASR.stopListening();
		Log.i(LOGTAG, "Stopped listening");
	}

	/* *******************************************************************************************************
	 * Process ASR events
	 */

	/*
	 * (non-Javadoc)
	 *
	 * Invoked when the ASR provides recognition results
	 *
	 * @see android.speech.RecognitionListener#onResults(android.os.Bundle)
	 */
	@Override
	public void onResults(Bundle results) {
        stopListening();
        if(results!=null){
            Log.i(LOGTAG, "ASR results received ok");

			//Retrieves the N-best list and the confidences from the ASR result
			ArrayList<String> nBestList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

			//Attention: confidence scores are supported only from API level 14
			//Creates a collection of strings, each one with a recognition result and its confidence
			//following the structure "Phrase matched (conf: 0.5)"
            if (nBestList!=null){
                Bundle b = new Bundle();
                b.putString("Reconocido", nBestList.get(0));

                Intent returnIntent = new Intent();
                returnIntent.putExtras(b);
                setResult(RESULT_OK, returnIntent);
                finish();

            }

		}
		else{
            Log.e(LOGTAG, "ASR results null");
			//There was a recognition error
			Toast.makeText(getApplicationContext(),"Error en el reconocimiento de voz", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            setResult(RESULT_NOT_OK, returnIntent);
            finish();
		}
	}

	/*
     * (non-Javadoc)
     *
     * Invoked when the ASR is ready to start listening
     *
     * @see android.speech.RecognitionListener#onReadyForSpeech(android.os.Bundle)
     */
	@Override
	public void onReadyForSpeech(Bundle arg0) {
		Toast.makeText(getApplicationContext(),"Puedes hablar", Toast.LENGTH_SHORT).show();

	}

	/*
     * (non-Javadoc)
     *
     * Invoked when the ASR encounters an error
     *
     * @see android.speech.RecognitionListener#onError(int)
     */
	@Override
	public void onError(final int errorCode) {

        //Possible bug in Android SpeechRecognizer: NO_MATCH errors even before the the ASR
        // has even tried to recognized. We have adopted the solution proposed in:
        // http://stackoverflow.com/questions/31071650/speechrecognizer-throws-onerror-on-the-first-listening
        long duration = System.currentTimeMillis() - startListeningTime;
        String errorMsg="";
        if (duration < 500 && errorCode == SpeechRecognizer.ERROR_NO_MATCH) {
            Log.e(LOGTAG, "Doesn't seem like the system tried to listen at all. duration = " + duration + "ms. Going to ignore the error");
            stopListening();
        }
        else {
            switch (errorCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    errorMsg = getResources().getString(R.string.asr_error_audio);
					break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    errorMsg = getResources().getString(R.string.asr_error_permissions);
					break;
                case SpeechRecognizer.ERROR_NETWORK:
                    errorMsg = getResources().getString(R.string.asr_error_network);
					break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    errorMsg = getResources().getString(R.string.asr_error_networktimeout);
					break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    errorMsg = getResources().getString(R.string.asr_error_nomatch);
					break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    errorMsg = getResources().getString(R.string.asr_error_recognizerbusy);
					break;
                case SpeechRecognizer.ERROR_SERVER:
                    errorMsg = getResources().getString(R.string.asr_error_server);
					break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    errorMsg = getResources().getString(R.string.asr_error_speechtimeout);
					break;
                default:
                    break;
            }
            if (errorCode == 5 && errorMsg.equals("")) {
                Log.e(LOGTAG, "Going to ignore the error");
                //Another frequent error that is not really due to the ASR
            } else {
            	String error = getString(R.string.asr_error, errorMsg);
				Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();

                Log.e(LOGTAG, "Error -> " + error);
                stopListening();
            }
        }
        finish();
	}

	/*
     * (non-Javadoc)
     * @see android.speech.RecognitionListener#onBeginningOfSpeech()
     */
	@Override
	public void onBeginningOfSpeech() {
	}

	/*
     * (non-Javadoc)
     * @see android.speech.RecognitionListener#onBufferReceived(byte[])
     */
	@Override
	public void onBufferReceived(byte[] buffer) {
	}

	/*
     * (non-Javadoc)
     * @see android.speech.RecognitionListener#onBeginningOfSpeech()
     */
	@Override
	public void onEndOfSpeech() {
	}

	/*
     * (non-Javadoc)
     * @see android.speech.RecognitionListener#onEvent(int, android.os.Bundle)
     */
	@Override
	public void onEvent(int arg0, Bundle arg1) {}

	/*
     * (non-Javadoc)
     * @see android.speech.RecognitionListener#onPartialResults(android.os.Bundle)
     */
	@Override
	public void onPartialResults(Bundle arg0) {
	}

	/*
 	* (non-Javadoc)
 	* @see android.speech.RecognitionListener#onRmsChanged(float)
 	*/
	@Override
	public void onRmsChanged(float arg0) {}



    @Override
    public void onDestroy() {
        super.onDestroy();
        myASR.destroy();
        Log.i(LOGTAG, "ASR destroyed");
    }

}