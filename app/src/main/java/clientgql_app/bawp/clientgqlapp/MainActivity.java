package clientgql_app.bawp.clientgqlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import clientgql_app.bawp.clientgqlapp.ui.UserRecyclerViewAdapter;
import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class MainActivity extends AppCompatActivity {
    UserRecyclerViewAdapter userRecyclerViewAdapter;
    private ViewGroup content;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        content = findViewById(R.id.content_holderId);
        progressBar = findViewById(R.id.progressBarId);

        //Recycler instantiations
        RecyclerView userRecyclerView = findViewById(R.id.rv_users_listId);
        userRecyclerViewAdapter = new UserRecyclerViewAdapter(this.getApplicationContext());
        userRecyclerView.setAdapter(userRecyclerViewAdapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        getUsers();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this, AddUserActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
       Apollo Client stuff
     */
    private void getUsers() {
        AplClient.getmApolloClient()
                .query(UsersQuery.builder().build())
                .enqueue(new ApolloCall.Callback<UsersQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull final Response<UsersQuery.Data> response) {

                       progressBar.setVisibility(View.VISIBLE);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                assert response.data() != null;
                                userRecyclerViewAdapter.setUsers(
                                        response.data().users
                                );
                                progressBar.setVisibility(View.GONE);
                                content.setVisibility(View.VISIBLE);

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                         Log.d(AplClient.TAG, e.getStackTrace().toString());

                    }
                });
    }
}
