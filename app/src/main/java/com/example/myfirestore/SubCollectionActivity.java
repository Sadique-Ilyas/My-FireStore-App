package com.example.myfirestore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SubCollectionActivity extends AppCompatActivity {
    private EditText firstName6, surName6, myPriority6, myTags6;
    private Button saveButton6, viewInfoButton6;
    private TextView viewInfo6;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_collection);

        firstName6 = findViewById(R.id.firstName6);
        surName6 = findViewById(R.id.surName6);
        myPriority6 = findViewById(R.id.myPriority6);
        myTags6 = findViewById(R.id.myTags6);
        saveButton6 = findViewById(R.id.saveButton6);
        viewInfoButton6 = findViewById(R.id.viewInfoButton6);
        viewInfo6 = findViewById(R.id.viewInfo6);

        saveButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstName6.getText().toString().trim();
                String surName = surName6.getText().toString().trim();

                if (myPriority6.length() == 0) {
                    myPriority6.setText("0");
                }
                int priority = Integer.parseInt(myPriority6.getText().toString());
                String tagInput = myTags6.getText().toString().trim();
                String []tagArray = tagInput.split("\\s*,\\s*");
                Map<String, Boolean> tags = new HashMap<>();
                for (String tag: tagArray)
                {
                    tags.put(tag, true);
                }

                NestedInfo info = new NestedInfo(firstName, surName, priority,tags);

                collectionReference.document("0el72TCXTvkLyTsIlnrP")
                        .collection("Child Note").add(info);
            }
        });

        viewInfoButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document("0el72TCXTvkLyTsIlnrP").collection("Child Note").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";

                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                {
                                    NestedInfo info = documentSnapshot.toObject(NestedInfo.class);
                                    info.setDocumentId(documentSnapshot.getId());

                                    String documentId = info.getDocumentId();
                                    data += "ID: " +documentId;

                                    for (String tag: info.getTags().keySet())
                                    {
                                        data += "\n- " +tag;
                                    }

                                    data += "\n\n";
                                }
                                viewInfo6.setText(data);
                            }
                        });
            }
        });
    }
}