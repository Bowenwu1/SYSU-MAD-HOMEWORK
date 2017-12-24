package com.pear.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class RepositoryActivity extends AppCompatActivity {
    private ProgressBar fetchProgress;
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    private GithubService githubService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        repoAdapter = new RepoAdapter();
        githubService = new GithubService();
        fetchProgress = (ProgressBar)findViewById(R.id.fetch_progress);
        recyclerView = (RecyclerView)findViewById(R.id.repository_list);

        recyclerView.setAdapter(repoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchProgress.setVisibility(View.INVISIBLE);

        String userName = this.getIntent().getStringExtra("username");
        Log.d("username", (new Boolean(userName == null)).toString());
        githubService.subscribeRepository(userName, new Subscriber<List<Repository>>() {
            @Override
            public void onCompleted() {
                fetchProgress.setVisibility(View.INVISIBLE);
                RepositoryActivity.this.toastInfo(R.string.fetch_repository_success);
            }

            @Override
            public void onError(Throwable e) {
                fetchProgress.setVisibility(View.VISIBLE);
                RepositoryActivity.this.toastInfo(R.string.internal_error);
                Log.e("repository get", e.getMessage());
            }

            @Override
            public void onNext(List<Repository> repositoryList) {
                repoAdapter.addAllItems(repositoryList);
            }
        });
    }

    public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {
        private List<Repository> data;
        private mOnClickListener mClickListener;
        private mOnLongClickListener mLongClickListener;
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.e("debug", "onCreateViewHolder");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resp_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
//            viewHolder.RepositoryName    = view.findViewById(R.id.resp_name);
//            viewHolder.Language          = view.findViewById(R.id.resp_lang);
//            viewHolder.ShortIntroduction = view.findViewById(R.id.resp_intro);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Log.e("debug", "onBindViewHolder");
            viewHolder.Language.setText(data.get(position).language);
            viewHolder.RepositoryName.setText(data.get(position).name);
            viewHolder.ShortIntroduction.setText(data.get(position).description);
            viewHolder.position = position;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public RepoAdapter() {
            data = new ArrayList<>();
        }

        public void addAllItems(List<Repository> repositoryList) {
            data = repositoryList;
            notifyDataSetChanged();
        }

        public void addItem(Repository newRepo) {
            data.add(newRepo);
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
            public ViewHolder(View itemView) {
                super(itemView);
                RepositoryName = itemView.findViewById(R.id.resp_name);
                Language = itemView.findViewById(R.id.resp_lang);
                ShortIntroduction = itemView.findViewById(R.id.resp_intro);
            }
            public int position;
            public TextView RepositoryName;
            public TextView Language;
            public TextView ShortIntroduction;
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, position);
                }
            }

            @Override
            public boolean onLongClick(View view) {
                if (mLongClickListener != null) {
                    return mLongClickListener.onItemLongClick(view,position);
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
