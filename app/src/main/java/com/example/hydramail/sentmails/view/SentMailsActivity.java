package com.example.hydramail.sentmails.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hydramail.MainActivity;
import com.example.hydramail.R;
import com.example.hydramail.fragments.ToFragment;
import com.example.hydramail.sentmails.database.model.DatabaseHelper;
import com.example.hydramail.sentmails.database.model.Mails;

import java.util.ArrayList;
import java.util.List;

public class SentMailsActivity extends AppCompatActivity {
    private MailAdapter mailAdapter;
    private RecyclerView recyclerView;

    ArrayList<String> mail_recipient, mail_subject, mail_message, mail_timestamp;

    private DatabaseHelper databaseHelper;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_mails);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.mail_recycler_view);


        databaseHelper = new DatabaseHelper(SentMailsActivity.this);
        mail_recipient = new ArrayList<>();
        mail_subject = new ArrayList<>();
        mail_message = new ArrayList<>();
        mail_timestamp = new ArrayList<>();

        id = Mails.COLUMN_ID;


        storeDataInArray();

        mailAdapter = new MailAdapter(SentMailsActivity.this, mail_recipient, mail_subject, mail_message, mail_timestamp);
        recyclerView.setAdapter(mailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SentMailsActivity.this));

//        itemTouchHelperCallBack  = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                    int position = viewHolder.getAdapterPosition();
//                    mail_subject.remove(position);
//                    mailAdapter.notifyDataSetChanged();
//
//                    DatabaseHelper databaseHelper = new DatabaseHelper(SentMailsActivity.this);
//                    databaseHelper.deleteOneRow(id, SentMailsActivity.this);
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallBack);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
        


    }

    void storeDataInArray(){
        Cursor cursor = databaseHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                mail_recipient.add(cursor.getString(1));
                mail_subject.add(cursor.getString(2));
                mail_message.add(cursor.getString(3));
                mail_timestamp.add(cursor.getString(4));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_menu:
                Intent intent = new Intent(SentMailsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}