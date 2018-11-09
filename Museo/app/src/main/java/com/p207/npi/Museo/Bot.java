package com.p207.npi.Museo;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

import static android.webkit.URLUtil.isValidUrl;

public class Bot extends Fragment implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getName();
    private Gson gson = GsonFactory.getGson();
    private AIDataService aiDataService;
    private ChatView chatView;
    User myAccount;
    private User TyrionBot;
    private View vista = null;


    public Bot(){
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (vista==null) {
            vista = inflater.inflate(R.layout.fragment_bot, container, false);
            initChatView(vista);

            //Language, Dialogflow Client access token
            final LanguageConfig config = new LanguageConfig("es", "4ba0ae40437d4ec3bbbfeecb293c4ba0");
            initService(config);
            setRetainInstance(true);

        }
        return vista;

    }

    @Override
    public void onClick(View v) {
        if (!chatView.getInputText().equals("")){
            //new message
            final Message message = new Message.Builder()
                    .setUser(myAccount)
                    .setRightMessage(true)
                    .setMessageText(chatView.getInputText())
                    .hideIcon(true)
                    .build();
            //Set to chat view
            chatView.send(message);
            sendRequest(chatView.getInputText());
            //Reset edit text
            chatView.setInputText("");
        }
    }


    /*
     * AIRequest should have query OR event
     */
     void sendRequest(String text) {
        Log.d(TAG, text);
        final String queryString = String.valueOf(text);
        final String eventString = null;
        final String contextString = null;

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        new AiTask().execute(queryString, eventString, contextString);

     }

    @SuppressLint("StaticFieldLeak")
    public class AiTask extends AsyncTask<String, Void, AIResponse> {
        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            String event = params[1];

            String context = params[2];

            if (!TextUtils.isEmpty(query)){
                request.setQuery(query);
            }

            if (!TextUtils.isEmpty(event)){
                request.setEvent(new AIEvent(event));
            }

            RequestExtras requestExtras = null;
            if (!TextUtils.isEmpty(context)) {
                final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
                requestExtras = new RequestExtras(contexts, null);
            }

            try {
                return aiDataService.request(request, requestExtras);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }


    private void onResult(final AIResponse response) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Variables
                gson.toJson(response);
                final Status status = response.getStatus();
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();
                final Metadata metadata = result.getMetadata();
                final HashMap<String, JsonElement> params = result.getParameters();

                // Logging
                Log.d(TAG, "onResult");
                Log.i(TAG, "Received success response");
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
                Log.i(TAG, "Action: " + result.getAction());
                Log.i(TAG, "Speech: " + speech);

                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s",
                                entry.getKey(), entry.getValue().toString()));
                    }
                }

                //Update view to bot says
                final Message receivedMessage = new Message.Builder()
                        .setUser(TyrionBot)
                        .setRightMessage(false)
                        .setMessageText(speech)
                        .build();
                chatView.receive(receivedMessage);


            }
        });
    }

    private void onError(final AIError error) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,error.toString());
            }
        });
    }


    private void initChatView(View Vista) {
        int myId = 0;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.tyrion);
        String myName = "Visitante del Museo";
        myAccount = new User(myId, myName, icon);

        int botId = 1;
        String botName = "Tyrion Lannister";
        TyrionBot = new User(botId, botName, icon);


        chatView = Vista.findViewById(R.id.chat_view);
        chatView.setRightBubbleColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.lightBlue500));
        chatView.setLeftBubbleColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        chatView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blueGray500));
        chatView.setSendButtonColor(ContextCompat.getColor(getActivity(), R.color.lightBlue500));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("Nuevo mensaje...");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
        chatView.setOnClickSendButtonListener(this);
        chatView.setOnIconClickListener(new Message.OnIconClickListener() {
            @Override
            public void onIconClick(@NotNull Message message) {

            }
        });
        chatView.setOnBubbleClickListener(new Message.OnBubbleClickListener() {
            @Override
            public void onClick(@NotNull Message message) {
                String entrada = message.getMessageText();
                if (isValidUrl(entrada)){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(entrada));
                    message.getStatusTextFormatter();
                    startActivity(i);

                }

            }
        });
        chatView.setOnBubbleLongClickListener(new Message.OnBubbleLongClickListener() {
            @Override
            public void onLongClick(@NotNull Message message) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Mensaje", message.getMessageText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Texto copiado al portapapeles", Toast.LENGTH_LONG).show();
            }

        });
        addMessage("Hola.\nPara hablar conmigo necesitas estar conectado a internet.", 0, TyrionBot);

    }

    private void initService(final LanguageConfig languageConfig) {
        final AIConfiguration.SupportedLanguages lang =
                AIConfiguration.SupportedLanguages.fromLanguageTag(languageConfig.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(languageConfig.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(Objects.requireNonNull(getActivity()), config);
    }

    void addMessage(String message, int side, User user){
        Message StartMessage = new Message.Builder()
                .setUser(user)
                .setRightMessage(side!=0)
                .setMessageText(message)
                .build();
        chatView.receive(StartMessage);


    }

}