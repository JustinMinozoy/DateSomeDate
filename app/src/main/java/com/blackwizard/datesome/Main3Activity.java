package com.blackwizard.datesome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    private Cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth auth;
    private DatabaseReference usersDb;
    private String currentUId;
    ListView listView;
    List<Cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        //currentUId = auth.getCurrentUser().getUid();////17/04/2020

        auth = FirebaseAuth.getInstance();
        checkUserSex();

        rowItems = new ArrayList<Cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems);
        // just removed now al.add("javascript");
        arrayAdapter.notifyDataSetChanged();

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(oppositetUserSex).child("connections").child("no").child(currentUId).setValue(true);
               Toast.makeText(Main3Activity.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(oppositetUserSex).child("connections").child("yes").child(currentUId).setValue(true);
                Toast.makeText(Main3Activity.this,"Right",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) { }

            @Override
            public void onScroll(float scrollProgressPercent) {
                //something to be coded here
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
        Toast.makeText(Main3Activity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String userSex;
    private String oppositetUserSex;

    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userSex = "Male";
                    oppositetUserSex = "Female";
                    getOppositeSexUsers();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        DatabaseReference FemaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
        FemaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(user.getUid())){
                    userSex = "Female";
                    oppositetUserSex = "Male";
                    getOppositeSexUsers();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void getOppositeSexUsers(){

        DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child("oppositeUserSex");
        oppositeSexDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("no").hasChild(currentUId) && !dataSnapshot.child("connections").child("yes").hasChild(currentUId)){

                        Cards item = new Cards(dataSnapshot.getKey(), dataSnapshot.child("name").getValue().toString());
                        rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    public void LogoutUser(View view) {
        auth.signOut();
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}