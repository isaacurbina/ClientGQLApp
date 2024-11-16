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

import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name;
    private EditText age;
    private EditText profession;
    private EditText postComment;
    private EditText hobbyTitle;
    private EditText hobbyDescription;

    private Button savePostAndHobbyButton;
    private Button saveUserButton;
    private LinearLayout bottomLayout;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.add_user_name);
        age = findViewById(R.id.add_user_age);
        profession = findViewById(R.id.add_user_profession);

        postComment = findViewById(R.id.add_user_post_comment);
        hobbyTitle = findViewById(R.id.add_user_hobby_title);
        hobbyDescription = findViewById(R.id.add_user_hobby_description);

        bottomLayout = findViewById(R.id.ll_bottomView);

        //setup buttons
        saveUserButton = findViewById(R.id.save_user_button);
        savePostAndHobbyButton = findViewById(R.id.save_post_hobby);

        saveUserButton.setOnClickListener(this);
        savePostAndHobbyButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.save_user_button:
                 //save new User
                saveUser();
                break;
            case R.id.save_post_hobby:
                 //save post and hobby
                 saveHobby(userId);
                 savePost(userId);
                break;
        }

    }

    private void saveUser() {
        AplClient.getmApolloClient()
                .mutate(AddUserMutation
                .builder()
                .userName(name.getText().toString().trim())
                .userAge(Integer.parseInt(String.valueOf(age.getText())))
                .userProfession(profession.getText().toString().trim())
                .build())
                .enqueue(new ApolloCall.Callback<AddUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AddUserMutation.Data> response) {
                        assert response.data() != null;
                        assert response.data().CreateUser != null;
                        final String id = response.data().CreateUser.id;

                        AddUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 if (id != null) {
                                     userId = id;
                                     bottomLayout.setVisibility(View.VISIBLE);
                                     Toast.makeText(AddUserActivity.this, "id: "
                                     + userId, Toast.LENGTH_LONG)
                                             .show();
                                 }
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });


    }

    public void saveHobby(String id){

        AplClient.getmApolloClient()
                .mutate(AddHobbyMutation
                .builder()
                .hobbyTitle(hobbyTitle.getText().toString().trim())
                .hobbyDescription(hobbyDescription.getText().toString().trim())
                .userId(id)
                .build())
                .enqueue(new ApolloCall.Callback<AddHobbyMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AddHobbyMutation.Data> response) {

                        assert response.data() != null;
                        assert response.data().CreateHobby != null;
                        String title = response.data().CreateHobby.title;


                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });


    }
    public void savePost(String id) {

        AplClient.getmApolloClient()
                .mutate(AddPostMutation
                        .builder()
                        .postComment(postComment.getText().toString().trim())
                        .userId(id)
                        .build())
                .enqueue(new ApolloCall.Callback<AddPostMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<AddPostMutation.Data> response) {

                        assert response.data() != null;
                         assert response.data().CreatePost != null;
                        String comment = response.data().CreatePost.comment;


                        startActivity(new Intent(AddUserActivity.this,
                                MainActivity.class));
//                        Toast.makeText(AddUserActivity.this,
//                                comment +
//                                        " has been saved! ",
//                                Toast.LENGTH_LONG)
//                                .show();

                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK){
              startActivity(new Intent(AddUserActivity.this,
                       MainActivity.class));
              return  true;
         }

         return super.onKeyDown(keyCode, event);
    }
}
