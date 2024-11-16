package clientgql_app.bawp.clientgqlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.text.BreakIterator;

import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    private String userId;
    private EditText name;
    private EditText age;
    private EditText profession;
    private Button updateUserButton;

    private EditText postComment;
    private EditText hobbyTitle;
    private EditText hobbyDescription;
    private Button savePostAndHobbyButton;

    private LinearLayout bottomLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.update_user_name);
        age = findViewById(R.id.update_user_age_edtxt);
        profession = findViewById(R.id.update_user_profession);

        updateUserButton = findViewById(R.id.update_user_btn);
        savePostAndHobbyButton = findViewById(R.id.update_post_hobby_btn);

        postComment = findViewById(R.id.update_add_post);
        hobbyTitle = findViewById(R.id.update_add_hobby_title);
        hobbyDescription = findViewById(R.id.update_add_hobby_description);

        updateUserButton.setOnClickListener(this);
        savePostAndHobbyButton.setOnClickListener(this);

        bottomLayout = findViewById(R.id.ll_bottomId);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");

            name.setText(intent.getStringExtra("name"));
            age.setText(String.valueOf(intent.getIntExtra("age", 0)));
            profession.setText(intent.getStringExtra("profession"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_user_btn:
                //call updateUser
                updateUser(userId);
                bottomLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.update_post_hobby_btn:
                //save post and hobby
                saveHobby(userId);
                savePost(userId);
                break;
        }
    }

    private void saveHobby(String userId) {
        AplClient.getmApolloClient()
                .mutate(AddHobbyMutation
                   .builder()
                .hobbyTitle(hobbyTitle.getText().toString().trim())
                .hobbyDescription(hobbyDescription.getText().toString().trim())
                .userId(userId)
                .build())
                .enqueue(new ApolloCall.Callback<AddHobbyMutation.Data>() {

                    @Override
                    public void onResponse(@NotNull Response<AddHobbyMutation.Data> response) {
                        assert response.data() != null;
                        assert response.data().CreateHobby != null;
                        final String id = response.data().CreateHobby.id();
                        UpdateUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("Added a Hobby", id);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    private void updateUser(String userId) {
        AplClient.getmApolloClient()
                .mutate(
                        UpdateUserMutation
                        .builder()
                        .id(userId)
                        .name(name.getText().toString().trim())
                        .age(Integer.parseInt(age.getText().toString()))
                        .profession(profession.getText().toString().trim())
                        .build()
                ).enqueue(new ApolloCall.Callback<UpdateUserMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<UpdateUserMutation.Data> response) {
                assert response.data() != null;
                assert response.data().UpdateUser != null;
                final String name = response.data().UpdateUser.name;

                UpdateUserActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateUserActivity.this,
                                name, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    private void savePost(final String userId) {
        AplClient.getmApolloClient()
                .mutate(AddPostMutation
                .builder()
                .postComment(postComment.getText().toString().trim())
                .userId(userId)
                .build())
                .enqueue(new ApolloCall.Callback<AddPostMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AddPostMutation.Data> response) {
                        UpdateUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UpdateUserActivity.this,
                                         userId, Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(UpdateUserActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
