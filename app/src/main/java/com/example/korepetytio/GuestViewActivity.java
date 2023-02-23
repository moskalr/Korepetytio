package com.example.korepetytio;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.korepetytio.client.Client;
import com.example.korepetytio.client.ClientRole;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GuestViewActivity extends AppCompatActivity {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private final List<Client> teachers = new ArrayList<>();

    private final List<String> teachersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_view);
        allTeachers();
        refresh();

        Button refreshButton = findViewById(R.id.refreshButtonGuest);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    public void allTeachers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("teachers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (!documentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : documentSnapshots.getDocuments()) {
                                Log.d(TAG, "TAAAAA" + String.valueOf(document.getData().get("username")));
//                                teachers.add(String.valueOf(document.getData().get("username")));

                                teachers.add(new Client(String.valueOf(document.getData().get("username")), String.valueOf(document.getData().get("password")),
                                        String.valueOf(document.getData().get("email")), ClientRole.TEACHER, (Double) document.getData().get("grade")));
                                teachersList.add("Teacher:  " + document.getData().get("username") + "\nUsers' rating: " + document.getData().get("grade")
                                        + "\nPrice per hour: " + document.getData().get("price") + "\nTeaches: " + document.getData().get("subject")
                                        + "\nDysfunction: " + document.getData().get("dysfunctions"));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Log.d(TAG, "CALA LISTA" + teachers.get(0).getUsername() + " " + teachers.get(0).getGrade());
                        } else {
                            Log.d(TAG, "Error getting documents: ");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void refresh() {
        ListView lv = (ListView) findViewById(R.id.listView1Guest);
        Log.d(TAG, "duuuuupa" + teachers);
        lv.setAdapter(new MyListAdapterGuest(this, R.layout.single_teacher_guest, teachersList));
    }

    public void backtoMain(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private class MyListAdapterGuest extends ArrayAdapter<String> {

        public Client teacher;
        private int layout;
        public MyListAdapterGuest(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            GuestViewActivity.ViewHolder mainViewholder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                //viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.RowGuest);
//                RatingBar ratingBar = findViewById(R.id.rating_bar2);
//                double grade = client.getGrade();
//                ratingBar.setRating((float) grade);
                convertView.setTag(viewHolder);
            }else{
                mainViewholder = (GuestViewActivity.ViewHolder) convertView.getTag();
                mainViewholder.title.setText(getItem(position));
            }
            return convertView;
        }
    }
    public class ViewHolder {
        ImageView thumbnail;
        TextView title;
        Button button;
    }
}