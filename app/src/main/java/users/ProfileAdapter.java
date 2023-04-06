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

import java.util.ArrayList;
public class ProfileAdapter extends RecyclerView.Adapter<users.ProfileAdapter.MyViewHolder> {

        Context context;
        ArrayList<Profile> profileArrayList;

        public ProfileAdapter(Context context, ArrayList<Profile> profileArrayList) {
            this.context = context;
            this.profileArrayList = profileArrayList;
        }

        @NonNull
        @Override
        public users.ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
            return new MyViewHolder(v);
        }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }
    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

            Profile profile=profileArrayList.get(position);

        }

        @Override
        public int getItemCount() {
            return profileArrayList.size();
        }
        public static class MyViewHolder extends RecyclerView.ViewHolder{

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                

            }
        }
    }


