package com.example.administrator.testnotification;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author : Administrator
 * @time : 13:41
 * @for : 第三方应用列表页
 */
public class ThirdPartActivity extends AppCompatActivity{

    RecyclerView mRv;
    List<ResolveInfo> mInfos;
    PackageManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_part_app);
        mInfos = NotificationsUtils.getAllThirdPartApp(this);
        manager = getPackageManager();
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolde>{

        @NonNull
        @Override
        public MyHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_third_part_app,parent,false);
            return new MyHolde(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolde holder, int position) {
            try {
                holder.bind(mInfos.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mInfos.size();
        }
    }

    class MyHolde extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;

        public MyHolde(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }

        public void bind(ResolveInfo resolveInfo) {
            if (resolveInfo!=null) {
                final AppInfoBean bean = NotificationsUtils.getInfo(resolveInfo,manager);
                img.setImageDrawable(bean.mdrawable);
                name.setText(bean.nName);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(ThirdPartActivity.this, MainActivity.class);
//                        intent.putExtra("packageName",bean.mPackageName);
//                        intent.putExtra("appName",bean.nName);
//                        startActivity(intent);
                        NotificationsUtils.jumpToAPPDetailInfo(ThirdPartActivity.this, 11, bean.mPackageName);
                    }
                });
            }
        }
    }

    public static class AppInfoBean{
        Drawable mdrawable;
        String nName;
        String mPackageName;
    }

}
