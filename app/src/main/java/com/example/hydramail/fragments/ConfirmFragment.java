package com.example.hydramail.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hydramail.R;
import com.example.hydramail.sentmails.database.model.DatabaseHelper;
import com.example.hydramail.sentmails.database.model.Mails;
import com.example.hydramail.sentmails.view.MailAdapter;
import com.example.hydramail.sentmails.view.SentMailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ConfirmFragment extends Fragment {
    private TextToSpeech tts;
    private boolean IsInitialVoiceFinished;
    private List<Mails> mailList = new ArrayList<>();
    private MailAdapter mailAdapter;
    private Context mContext;
    DatabaseHelper dbHelper;


    public ConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);


        Bundle bundle = getArguments();

        final String email = bundle.getString("Email");
        final String password = bundle.getString("Password");

        final String recipient = bundle.getString("Recipient");
        final String subject = bundle.getString("Subject");
        final String message = bundle.getString("Message");


        TextView toTextView = view.findViewById(R.id.to_text_view);
        TextView subjectTextView = view.findViewById(R.id.subject_text_view);
        TextView messageTextView = view.findViewById(R.id.message_text_view);
        Button sendButton = view.findViewById(R.id.sendButton);


        toTextView.setText(email);
        subjectTextView.setText(password);
        messageTextView.setText(message);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendMail(recipient, message, subject);
                dbHelper = new DatabaseHelper(getActivity());
                dbHelper.addBook(recipient, message, subject, getContext());

                Intent i = new Intent(getActivity(), SentMailsActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);



//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                SuccessFragment successFragment = new SuccessFragment();
//
//                fragmentTransaction.replace(R.id.fragment_container, successFragment);
//                fragmentTransaction.commit();

            }
        });

        IsInitialVoiceFinished = false;

        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                   speak("Please confirm message, you are sending to\n\n\n\n"+ recipient+ "\n\n\n\nyour subject is \n\n\n\n"+subject+ "\n\n\n\n\nYour message is\n\n\n\n "+message+ "\n\n\n\nTap below screen to send");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IsInitialVoiceFinished=true;
                        }
                    }, 3000);
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });


        return view;

    }




    private void speak(String text){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




    @Override
    public void onStop()
    {
        super.onStop();

        if(tts != null){
            tts.shutdown();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(tts != null){
            tts.shutdown();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
         dbHelper =  new DatabaseHelper(context);
    }
}

