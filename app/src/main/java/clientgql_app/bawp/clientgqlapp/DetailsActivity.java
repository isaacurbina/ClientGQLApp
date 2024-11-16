package clientgql_app.bawp.clientgqlapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import clientgql_app.bawp.clientgqlapp.ui.DetsRecyclerViewAdapter;
import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class DetailsActivity extends AppCompatActivity {

    private String userId;
    private DetsRecyclerViewAdapter detsRecyclerViewAdapter;
    private RecyclerView detsRecyclerView;

    private List<GetUserQuery.Post> postList;
    private List<GetUserQuery.Hobby> hobbyList;
    private boolean isHobbySelected;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView nameTextView = findViewById(R.id.dets_name);
        TextView ageTextView = findViewById(R.id.dets_age);
        TextView professionTextView = findViewById(R.id.dets_profession);
        Button hobbiesButton = findViewById(R.id.dets_hobbies_btn);
        Button postsButton = findViewById(R.id.dets_posts_btn);
        isHobbySelected = true;
        bottomLayout = findViewById(R.id.dets_ll_bottom);

        postList = Collections.emptyList();
        hobbyList = Collections.emptyList();

        Intent intent = this.getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            int age = intent.getIntExtra("age", 0);
            String profession = intent.getStringExtra("profession");
            String name = intent.getStringExtra("name");


            nameTextView.setText(name);
            ageTextView.setText("Age: " + age);
            professionTextView.setText("Profession: " + profession);

            //RecyclerView setup
            detsRecyclerView = findViewById(R.id.dets_rv);
            detsRecyclerViewAdapter = new DetsRecyclerViewAdapter(this.getApplicationContext());
            detsRecyclerView.setAdapter(detsRecyclerViewAdapter);
            detsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            detsRecyclerView.setVisibility(View.INVISIBLE);

            hobbiesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isHobbySelected = true;
                    detsRecyclerView.setVisibility(View.VISIBLE);
                    detsRecyclerView.removeAllViews();
                    getUser();

                }
            });

            postsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isHobbySelected = false;
                    detsRecyclerView.setVisibility(View.VISIBLE);
                    detsRecyclerView.removeAllViews();
                    getUser();
                }
            });


            //getUser();

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getUser() {
        AplClient.getmApolloClient()
                .query(GetUserQuery.builder()
                        .userId(userId)
                        .build())
                .enqueue(new ApolloCall.Callback<GetUserQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<GetUserQuery.Data> response) {


                        DetailsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                assert response.data() != null;
                                hobbyList = Objects.requireNonNull(response.data().user()).hobbies();
                                postList = Objects.requireNonNull(response.data().user()).posts();

                                if (isHobbySelected){
                                    if (hobbyList.isEmpty()) {
                                        showMessage("No Hobbies Available for this User.");
                                    }
                                }else {
                                    assert postList != null;
                                    if (postList.isEmpty()) {
                                        showMessage("No Posts Available for this User.");
                                    }
                                }

                                detsRecyclerViewAdapter.setUserData(
                                        postList,
                                        hobbyList,
                                        isHobbySelected
                                );

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                        Log.d(AplClient.TAG, e.getStackTrace().toString());

                    }
                });
    }

    public void showMessage(String message) {
        bottomLayout.removeView(detsRecyclerView);
        bottomLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams =
                 new LinearLayout.LayoutParams(
                         LinearLayout.LayoutParams.WRAP_CONTENT,
                         LinearLayout.LayoutParams.WRAP_CONTENT
                 );
        layoutParams.gravity = Gravity.CENTER;

        TextView textView = new TextView(DetailsActivity.this);
        textView.setText(message);
        textView.setTextColor(Color.GRAY);
        textView.setTextSize((float) 18.9);
        textView.setLayoutParams(layoutParams);

        bottomLayout.addView(textView);
    }

}
