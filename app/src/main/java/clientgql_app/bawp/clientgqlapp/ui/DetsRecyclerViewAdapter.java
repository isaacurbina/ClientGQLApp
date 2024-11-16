package clientgql_app.bawp.clientgqlapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import clientgql_app.bawp.clientgqlapp.GetUserQuery;
import clientgql_app.bawp.clientgqlapp.R;

public class DetsRecyclerViewAdapter extends
        RecyclerView.Adapter<DetsRecyclerViewAdapter.DetsViewHolder> {
    private Context context;
    private List<GetUserQuery.Post> userPosts = Collections.emptyList();
    private List<GetUserQuery.Hobby> userHobbies = Collections.emptyList();
    private boolean isHobby;


    public DetsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setUserData(List<GetUserQuery.Post> userPosts,
                            List<GetUserQuery.Hobby> userHobbies,
                            boolean isHobby) {
        this.userPosts = userPosts;
        this.userHobbies = userHobbies;
        this.isHobby = isHobby;

        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DetsRecyclerViewAdapter.DetsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater
                .from(viewGroup.getContext());
        final View viewItem = layoutInflater.inflate(
                R.layout.row,
                viewGroup,
                false
        );

        return new DetsViewHolder(viewItem, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DetsRecyclerViewAdapter.DetsViewHolder detsViewHolder, int position) {
        if (isHobby) {
            final GetUserQuery.Hobby userHobby = this.userHobbies.get(position);
            detsViewHolder.setHobby(userHobby);
        }else {
            final GetUserQuery.Post userPost = this.userPosts.get(position);
            detsViewHolder.setPost(userPost);
        }
    }

    @Override
    public int getItemCount() {
        return isHobby ? userHobbies.size() : userPosts.size() ;
    }

    public class DetsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        Context context;

        public DetsViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            title = itemView.findViewById(R.id.dets_row_title);
            description = itemView.findViewById(R.id.dets_row_description);
            this.context = context;
        }

        public void setHobby(GetUserQuery.Hobby hobby) {
             title.setText(hobby.title());
             description.setText(hobby.description());
        }

        public void setPost(final GetUserQuery.Post post) {
            title.setText("Comments");
            description.setText(post.comment());
        }
    }
}
