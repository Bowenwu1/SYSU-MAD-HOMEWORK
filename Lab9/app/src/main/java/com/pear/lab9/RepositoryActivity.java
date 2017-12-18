package com.pear.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RepositoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
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

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public RepoAdapter() {
            data = new ArrayList<>();
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
