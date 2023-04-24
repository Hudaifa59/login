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
import java.util.UUID;

public class ProfileAdapter extends RecyclerView.Adapter<users.ProfileAdapter.MyViewHolder> {

        Context context;
        ArrayList<Profile> profileArrayList;
        private FirebaseServices fbs;

        public ProfileAdapter(Context context, ArrayList<Profile> profileArrayList) {
            this.context = context;
            this.profileArrayList = profileArrayList;
        }

        @NonNull
        @Override
        public users.ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.post, parent, false);
            return new MyViewHolder(v);
        }
    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.MyViewHolder holder, int position) {

            Profile profile=profileArrayList.get(position);
            fbs =FirebaseServices.getInstance();
            fbs.getStorage().getReference("listingPictures/"+ profile.getImage()).getBytes(5000).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                if (task.isSuccessful()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                    listingPicture.setImageBitmap(bitmap);
                    listingPicture.setRotation(90);
                }
                else{
                    Log.d("Download Image:", task.getException().toString());
                }
            }
            });
            holder.nickname.setText(profile.getNickname());
            holder.phone.setText(profile.getPhone());
            holder.name.setText(profile.getName());
            holder.gender.setText(profile.getGender());
            holder.post.setText(profile.getPosts());
        }

        @Override
        public int getItemCount() {
            return profileArrayList.size();
        }
        public static class MyViewHolder extends RecyclerView.ViewHolder{

            TextView nickname,name,gender,phone,post;
            ImageView profilepic;
            public MyViewHolder(@NonNull View itemView) {
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



