package clientgql_app.bawp.clientgqlapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import clientgql_app.bawp.clientgqlapp.DeleteActivity;
import clientgql_app.bawp.clientgqlapp.DetailsActivity;
import clientgql_app.bawp.clientgqlapp.R;
import clientgql_app.bawp.clientgqlapp.UpdateUserActivity;
import clientgql_app.bawp.clientgqlapp.UsersQuery;
import clientgql_app.bawp.clientgqlapp.util.AplClient;

public class UserRecyclerViewAdapter
        extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {
    private Context context;
    private List<UsersQuery.User> users = Collections.emptyList();

    public UserRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setUsers(List<UsersQuery.User> users) {
         this.users = users;
         this.notifyDataSetChanged();
        Log.d(AplClient.TAG, "Updating users in adapter" + users.size());
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater
                .from(parent.getContext());

        final View itemView = layoutInflater.inflate(R.layout.user_row,
                parent, false);
        return new UserViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerViewAdapter.UserViewHolder userViewHolder, int position) {
         final UsersQuery.User user = this.users.get(position);
         userViewHolder.setUser(user);


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userTextView;
        TextView ageTextView;
        TextView professionTextView;
        CardView container;
        ImageButton deleteButton;
        String userId;
        Context context;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.userNameId);
            ageTextView = itemView.findViewById(R.id.ageId);
            professionTextView = itemView.findViewById(R.id.professionID);
            container = itemView.findViewById(R.id.cardViewId);
            deleteButton = itemView.findViewById(R.id.user_delete_btn);


            this.context = context;


        }

        void setUser(final UsersQuery.User user) {
             userTextView.setText(user.name());
             ageTextView.setText("Age: "+user.age().toString());
             professionTextView.setText("Profession: " + user.profession());
             userId = user.id();


             deleteButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(context, DeleteActivity.class);
                     intent.putExtra("userId", userId);
                     intent.putExtra("name", user.name());
                     context.startActivity(intent);


                 }
             });
             container.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     Intent intent = new Intent(context, UpdateUserActivity.class);
                     intent.putExtra("userId", userId);
                     intent.putExtra("name", user.name());
                     intent.putExtra("profession", user.profession());
                     intent.putExtra("age", user.age());

                     context.startActivity(intent);

                     return false;
                 }
             });
             container.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Intent intent = new Intent(context, DetailsActivity.class);
                     intent.putExtra("userId", userId);
                     intent.putExtra("name", user.name());
                     intent.putExtra("profession", user.profession());
                     intent.putExtra("age", user.age());

                     context.startActivity(intent);




                       Log.d(AplClient.TAG , userId );
                 }
             });
        }
    }
}
