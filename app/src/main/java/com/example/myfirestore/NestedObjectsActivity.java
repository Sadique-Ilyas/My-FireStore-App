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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedObjectsActivity extends AppCompatActivity {
    private EditText firstName5, surName5, myPriority5, myTags5;
    private Button saveButton5, viewInfoButton5;
    private TextView viewInfo5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users Info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_objects);

        firstName5 = findViewById(R.id.firstName5);
        surName5 = findViewById(R.id.surName5);
        myPriority5 = findViewById(R.id.myPriority5);
        myTags5 = findViewById(R.id.myTags5);
        saveButton5 = findViewById(R.id.saveButton5);
        viewInfoButton5 = findViewById(R.id.viewInfoButton5);
        viewInfo5 = findViewById(R.id.viewInfo5);

        saveButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstName5.getText().toString().trim();
                String surName = surName5.getText().toString().trim();

                if (myPriority5.length() == 0) {
                    myPriority5.setText("0");
                }
                int priority = Integer.parseInt(myPriority5.getText().toString());
                String tagInput = myTags5.getText().toString().trim();
                String []tagArray = tagInput.split("\\s*,\\s*");
                Map<String, Boolean> tags = new HashMap<>();
                for (String tag: tagArray)
                {
                    tags.put(tag, true);
                }

                NestedInfo info = new NestedInfo(firstName, surName, priority,tags);
                collectionReference.add(info);
            }
        });

        viewInfoButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.whereEqualTo("tags.tag5", true).get()
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
                                viewInfo5.setText(data);
                            }
                        });
            }
        });
        updateArray();
    }

    private void updateArray()
    {
        collectionReference.document("QymqSUffQIzL1XtUswIj")
                //.update("tags.tag1", false);
                // .update("tags.tag1", FieldValue.delete());
                   .update("tags.tag1.nested1.nested", true);
    }

    public void subCollectionActivity(View view)
    {
        startActivity(new Intent(NestedObjectsActivity.this, SubCollectionActivity.class));
    }
}
