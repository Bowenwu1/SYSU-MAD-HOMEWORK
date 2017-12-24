package com.pear.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        fetchProgress = (ProgressBar)findViewById(R.id.fetch_progress);
        recyclerView = (RecyclerView)findViewById(R.id.repository_list);

        recyclerView.setAdapter(repoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchProgress.setVisibility(View.INVISIBLE);

        String userName = this.getIntent().getStringExtra("username");

        githubService.subscribeRepository(userName, new Subscriber<List<Repository>>() {
            @Override
            public void onCompleted() {
                fetchProgress.setVisibility(View.INVISIBLE);
                RepositoryActivity.this.toastInfo(R.string.fetc);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Repository> repositoryList) {

            }
        });
    }

    public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {
        private List<Repository> data;
        private mOnClickListener mClickListener;
        private mOnLongClickListener mLongClickListener;
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.Language.setText(data.get(position).programmingLanguage);
            viewHolder.RepositoryName.setText(data.get(position).name);
            viewHolder.ShortIntroduction.setText(data.get(position).getShortIntroduction());
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
