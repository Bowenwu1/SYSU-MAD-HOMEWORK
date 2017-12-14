package com.pear.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    RecyclerView user_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_list = (RecyclerView)findViewById(R.id.list_item);
        user_list.setLayoutManager(new LinearLayoutManager(this));
        user_list.setHasFixedSize(true);
        user_list.setAdapter(new UserAdapter());
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.userName.setText("wubowen");
            viewHolder.userBlog.setText("bowen.wu@163.com");
            viewHolder.userId.setText("122432");
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView userName;
            public TextView userId;
            public TextView userBlog;
            public ViewHolder(View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.username);
                userId = itemView.findViewById(R.id.userid);
                userBlog = itemView.findViewById(R.id.userblog);
            }
        }
    }
}
