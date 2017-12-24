package com.pear.lab9;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView user_list;
    Button clear_button;
    Button fetch_button;
    EditText name;

    UserAdapter userAdapter;
    GithubService mService;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = new GithubService();

        userAdapter = new UserAdapter();

        user_list = (RecyclerView)findViewById(R.id.list_item);
        user_list.setLayoutManager(new LinearLayoutManager(this));
        user_list.setHasFixedSize(true);
        user_list.setAdapter(userAdapter);

        clear_button = (Button)findViewById(R.id.clear_button);
        fetch_button = (Button)findViewById(R.id.fetch_button);
        name = (EditText)findViewById(R.id.search);
        progressBar = (ProgressBar)findViewById(R.id.fetch_progress);
        progressBar.setVisibility(View.INVISIBLE);

        fetch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchContent = name.getText().toString();
                if (searchContent.isEmpty()) {
                    MainActivity.this.toastInfo(R.string.username_can_not_empty);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mService.subscribeUser(searchContent, new Subscriber<User>() {
                        @Override
                        public void onCompleted() {
                            MainActivity.this.toastInfo(R.string.fetch_user_success);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("MainActivity", e.getMessage());
                            MainActivity.this.toastInfo(R.string.internal_error);
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onNext(User user) {
                            MainActivity.this.userAdapter.addItem(user);
                        }
                    });
                }

            }
        });

        userAdapter.setOnItemClickListener(new mOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // go to resp
                String userID = userAdapter.getUserID(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", userID);
                Intent intent = new Intent(MainActivity.this, RepositoryActivity.class);
                startActivity(intent);
            }
        });

        userAdapter.setOnItemLongClickListener(new mOnLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position) {
                // delete, use dialog to confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                userAdapter.removeItem(position);
                            }
                        })
                        .setNegativeButton("否", null);
                return false;
            }
        });
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        private List<User> data;
        private mOnClickListener mClickListener;
        private mOnLongClickListener mLongClickListener;
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.userName.setText(data.get(position).name);
            viewHolder.userBlog.setText(data.get(position).blog);
            viewHolder.userId.setText(data.get(position).id);
        }

        public String getUserID(int position) {
            return data.get(position).id;
        }
        @Override
        public int getItemCount() {
            return data.size();
        }

        public UserAdapter() {
            data = new ArrayList<>();
        }

        public void addItem(User newUser) {
            data.add(newUser);
            notifyDataSetChanged();
        }

        public void removeItem(int position) {
            data.remove(position);
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(mOnClickListener listener) {
            this.mClickListener = listener;
        }

        public void setOnItemLongClickListener(mOnLongClickListener listener) {
            this.mLongClickListener = listener;
        }
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            public TextView userName;
            public TextView userId;
            public TextView userBlog;
            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                userName = itemView.findViewById(R.id.username);
                userId = itemView.findViewById(R.id.userid);
                userBlog = itemView.findViewById(R.id.userblog);
            }

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, (int)v.getTag());
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null) {
                    return mLongClickListener.onItemLongClick(view,(int) view.getTag());
                } else {
                    return false;
                }
            }
        }
    }

    public static interface mOnClickListener {
        void onItemClick(View view, int position);
    }

    public static interface mOnLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public void toastInfo(int RID) {
        Toast.makeText(this, RID, Toast.LENGTH_SHORT).show();
    }

}
