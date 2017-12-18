package com.pear.lab9;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAdapter = new UserAdapter();

        user_list = (RecyclerView)findViewById(R.id.list_item);
        user_list.setLayoutManager(new LinearLayoutManager(this));
        user_list.setHasFixedSize(true);
        user_list.setAdapter(userAdapter);

        clear_button = (Button)findViewById(R.id.clear_button);
        fetch_button = (Button)findViewById(R.id.fetch_button);
        name = (EditText)findViewById(R.id.search);

        fetch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchContent = name.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .baseUrl("https://api.github.com")
                        .client(new OkHttpClient())
                        .build();
                GithubService githubService = retrofit.create(GithubService.class);

                githubService.getUser(searchContent)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(User value) {
                                userAdapter.addItem(value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

            }
        });

        userAdapter.setOnItemClickListener(new mOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // go to resp
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


}
