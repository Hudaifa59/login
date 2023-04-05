package users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;

    public MyAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Post post=postArrayList.get(position);

        holder.
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView post;
        private ImageView user,comments,likes,shares;
        private TextView comment,like,share,username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.ivpost);
            comments=itemView.findViewById(R.id.ivcommentpost);
            user=itemView.findViewById(R.id.ivpostuser);
            likes=itemView.findViewById(R.id.ivlikepost);
            shares=itemView.findViewById(R.id.ivsharepost);
            username=itemView.findViewById(R.id.tvpostuser);
            share=itemView.findViewById(R.id.tvsharepost);
            like=itemView.findViewById(R.id.tvlikespost);
            comment=itemView.findViewById(R.id.tvcommentspost);
        }
    }
}
