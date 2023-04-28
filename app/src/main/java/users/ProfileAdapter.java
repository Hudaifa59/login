package users;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class  ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Bitmap bitmap1;
    private FirebaseServices fbs;
    private List<Profile> data;

    public ProfileAdapter(List<Profile> data) {
        this.data = data;
    }

    public void setData(List<Profile> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Profile profile=data.get(position);
        fbs =FirebaseServices.getInstance();
        fbs.getStorage().getReference(profile.getImage()).getBytes(50000).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    bitmap1=bitmap;
                }
                else{
                    Log.d("Download Image:", task.getException().toString());
                }
            }
        });
        holder.profilepic.setImageBitmap(bitmap1);
        holder.profilepic.setRotation(90);
        holder.nickname.setText(profile.getNickname());
        holder.phone.setText(profile.getPhone());
        holder.name.setText(profile.getName());
        holder.gender.setText(profile.getGender());
        holder.post.setText(profile.getPosts());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname,name,gender,phone,post;
        ImageView profilepic;

        public ViewHolder(View itemView) {
            super(itemView);
            nickname=itemView.findViewById(R.id.nicknameadap);
            name=itemView.findViewById(R.id.nameadap);
            gender=itemView.findViewById(R.id.genderadap);
            phone=itemView.findViewById(R.id.phoneadap);
            post=itemView.findViewById(R.id.postadap);
            profilepic=itemView.findViewById(R.id.profileadap);
        }
    }
}

