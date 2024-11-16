package clientgql_app.bawp.clientgqlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId, name;
    private Button yesButton, noButton;
    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = findViewById(R.id.delete_user_txt);
        yesButton = findViewById(R.id.delete_yes);
        noButton = findViewById(R.id.delete_no);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            name = intent.getStringExtra("name");
            userName.setText(name);
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
            case R.id.delete_no:
                //go back to Main
                startActivity(new Intent(DeleteActivity.this,
                        MainActivity.class));
                DeleteActivity.this.finish();
                break;
            case R.id.delete_yes:
                //delete user
                deleteUser(userId);
                break;
        }

    }

    private void deleteUser(String userId) {
        AplClient.getmApolloClient()
                .mutate(DeleteUserMutation
                .builder()
                .userId(userId)
                .build())
                .enqueue(new ApolloCall.Callback<DeleteUserMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<DeleteUserMutation.Data> response) {
                            DeleteActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    assert response.data() != null;
                                    assert response.data().RemoveUser != null;
                                    Toast.makeText(DeleteActivity.this,
                                            response.data().RemoveUser.name,
                                            Toast.LENGTH_LONG)
                                            .show();
                                     startActivity(new Intent(DeleteActivity.this,
                                             MainActivity.class));
                                     DeleteActivity.this.finish();
                                }
                            });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }
}
