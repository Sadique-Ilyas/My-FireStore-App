package com.example.myfirestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class ArraysActivity extends AppCompatActivity {
    private EditText firstName4, surName4, myPriority4, myTags;
    private Button saveButton4, viewInfoButton4;
    private TextView viewInfo4;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrays);

        firstName4 = findViewById(R.id.firstName4);
        surName4 = findViewById(R.id.surName4);
        myPriority4 = findViewById(R.id.myPriority4);
        myTags = findViewById(R.id.myTags);
        saveButton4 = findViewById(R.id.saveButton4);
        viewInfoButton4 = findViewById(R.id.viewInfoButton4);
        viewInfo4 = findViewById(R.id.viewInfo4);

        saveButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstName4.getText().toString().trim();
                String surName = surName4.getText().toString().trim();

                if (myPriority4.length() == 0) {
                    myPriority4.setText("0");
                }
                int priority = Integer.parseInt(myPriority4.getText().toString());
                String tagInput = myTags.getText().toString().trim();
                String []tagArray = tagInput.split("\\s*,\\s*");
                List<String> tags = Arrays.asList(tagArray);

                MyArraysInfo info = new MyArraysInfo(firstName, surName, priority,tags);
                collectionReference.add(info);
            }
        });

        viewInfoButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.whereArrayContains("tags", "tag5").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String data = "";

                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots)
                                {
                                    MyArraysInfo info = documentSnapshot.toObject(MyArraysInfo.class);
                                    info.setDocumentId(documentSnapshot.getId());

                                    String documentId = info.getDocumentId();
                                    data += "ID: " +documentId;

                                    for (String tag: info.getTags())
                                    {
                                        data += "\n- " +tag;
                                    }

                                    data += "\n\n";
                                }
                                viewInfo4.setText(data);
                            }
                        });
            }
        });

        updateArray();
    }

    private void updateArray()
    {
        collectionReference.document("Xs5ofGCJ1w5tgj0AokBo")
                //.update("tags", FieldValue.arrayUnion("New Tag"));
                  .update("tags", FieldValue.arrayRemove("New Tag"));
    }

    public void nestedActivity(View view)
    {
        startActivity(new Intent(ArraysActivity.this, NestedObjectsActivity.class));
    }
}