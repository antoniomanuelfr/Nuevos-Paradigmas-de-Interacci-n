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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;



/**
 * RichTTS: App with a rich control of text to speech synthesis using the TextToSpeech class
 * and implementing the OnInitListener
 * (it is an improvement over the SimpleTTS app)
 * <p/>
 * Simple demo in which the user writes a text in a text field
 * and it is synthesized by the system when pressing a button.
 * <p/>
 *
 * @author Zoraida Callejas, Michael McTear, David Griol
 * @version 3.0, 25/09/18
 *
 */
public class RichTTS extends Activity implements TextToSpeech.OnInitListener{

    private final static int TTS_DATA_CHECK = 12;    // Request code to identify the intent that looks for a TTS Engine in the device
    private final static String LOGTAG = "RichTTS";
    static String TEXTTAG = "TextoTTS";
    static int REQUESTSPEACH = 32;
    public static int RESULT_OK = 1;
    public static int NO_LANGUAGE =- 1;

    private TextToSpeech mytts;


    /**
     * Sets up the activity initializing the text to speech engine
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the speak button
        // Invoke the method to initialize text to speech
        initTTS();
    }

    private void speakText (String text) {
        Locale espa = new Locale("spa", "ESP");
        if (mytts.isLanguageAvailable(espa)>=0) {
            mytts.setLanguage(espa);
            mytts.setPitch(0.6544f);
            Bundle tts_params = new Bundle();
            tts_params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1);
            mytts.speak(text, TextToSpeech.QUEUE_ADD, tts_params, "msg");

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            //No se finaliza la activity aqui
        }else{

            Intent returnIntent = new Intent();
            setResult(NO_LANGUAGE, returnIntent);
            finish();
        }
    }


    /**
     * Checks whether there is a TTS Engine in the device, when done, it invokes the
     * onActivityResult method which actually initializes the TTS
     */
    private void initTTS() {
        //Check if the engine is installed, when the check is finished, the
        //onActivityResult method is automatically invoked
        Intent checkIntent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, TTS_DATA_CHECK);
    }

    /**
     * Callback from check for text to speech engine installed
     * If positive, then creates a new <code>TextToSpeech</code> instance which will be called when user
     * clicks on the 'Speak' button
     * If negative, creates an intent to install a <code>TextToSpeech</code> engine
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check that the result received is from the TTS_DATA_CHECK action
        if (requestCode == TTS_DATA_CHECK) {

            // If the result of the action is CHECK_VOICE_DATA_PASS, there is a TTS Engine
            //available in the device
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                // Create the TextToSpeech instance
                setTTS();
            } else {
                // The TTS is not available, we will try to install it:
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);

                PackageManager pm = getPackageManager();
                ResolveInfo resolveInfo = pm.resolveActivity(installIntent, PackageManager.MATCH_DEFAULT_ONLY);

                //If the install can be started automatically we launch it (startActivity), if not, we
                //ask the user to install the TTS from Google Play (toast)
                if (resolveInfo != null) {
                    startActivity(installIntent);
                } else {
                    Toast.makeText(RichTTS.this, R.string.please_install_tts, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * Starts the TTS engine. It is work-around to avoid being a subclass of the UtteranceProgressListener abstract class.
     *
     * @author Method by Greg Milette (comments incorporated by us). Source: https://github.com/gast-lib/gast-lib/blob/master/library/src/root/gast/speech/voiceaction/VoiceActionExecutor.java
     * See the problem here: http://stackoverflow.com/questions/11703653/why-is-utteranceprogresslistener-not-an-interface
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void setTTS() {
        mytts = new TextToSpeech(this, this);

		/*
		 * The listener for the TTS events varies depending on the Android version used:
		 * the most updated one is UtteranceProgressListener, but in SKD versions
		 * 15 or earlier, it is necessary to use the deprecated OnUtteranceCompletedListener.
		 */
        mytts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) //TTS finished synthesizing
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RichTTS.this, R.string.tts_done, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onError(String utteranceId, int errorCode) //TTS encountered an error while synthesizing
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RichTTS.this, R.string.tts_error, Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onError(String utteranceId) //TTS encountered an error while synthesizing
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RichTTS.this, R.string.tts_error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onStart(String utteranceId) //TTS has started synthesizing
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RichTTS.this, R.string.tts_started, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /*
     * A <code>TextToSpeech</code> instance can only be used to synthesize text once
     * it has completed its initialization.
     * (non-Javadoc)
     * @see android.speech.tts.TextToSpeech.OnInitListener#onInit(int)
     */
    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            Toast.makeText(RichTTS.this, R.string.tts_initialized, Toast.LENGTH_LONG).show();
            Bundle b = getIntent().getExtras();
            if (b!=null) speakText(b.getString(TEXTTAG));
        } else {
            Log.e(LOGTAG, "Error initializing the TTS");
        }

    }

    /**
     * Shuts down the TTS when finished
     */
    @Override
    public void onDestroy() {
        if (mytts.isSpeaking())
            mytts.stop();
        mytts.shutdown();
        mytts = null;			/*
		 						This is necessary in order to force the creation of a new TTS instance after shutdown.
		 						It is useful for handling runtime changes such as a change in the orientation of the device,
		 						as it is necessary to create a new instance with the new context.
		 						See here: http://developer.android.com/guide/topics/resources/runtime-changes.html
							*/
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}

