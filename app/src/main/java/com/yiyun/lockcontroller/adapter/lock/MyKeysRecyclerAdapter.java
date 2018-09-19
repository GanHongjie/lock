package com.yiyun.lockcontroller.adapter.lock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;

import java.util.List;

import static com.yiyun.lockcontroller.bean.lock.LockKeysBean.USER_TYPE_TEPORARY;
import static com.yiyun.lockcontroller.utils.DateUtil.daysBetween;

/**
 * Created by Layo on 2017-8-16.
 */

public class MyKeysRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<LockKeysBean> list;
    private static final int TYPE_OWNER = 0;
    private static final int TYPE_TEMPORARY = 1;

    //自定义监听事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, final int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public MyKeysRecyclerAdapter(Context context, List<LockKeysBean> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建viewholder
        if (viewType == TYPE_OWNER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_keys_owner_item, parent,false);
            view.setOnClickListener(this);
            return new OwerItemViewHolder(view);
        } else if (viewType == TYPE_TEMPORARY) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_keys_temporary_item, parent,false);
            view.setOnClickListener(this);
            return new TemporaryItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TemporaryItemViewHolder) {
            holder.itemView.setTag(position);
            LockKeysBean lockKeysBean = list.get(position);
            ((TemporaryItemViewHolder) holder).tvDepNo.setText(lockKeysBean.getAddress());
            int type = lockKeysBean.getAutoType();
            if (type == AuthorizeLogBean.AUTO_STATE_TIME) {
                ((TemporaryItemViewHolder) holder).llSurplusCount.setVisibility(View.GONE);
                ((TemporaryItemViewHolder) holder).llSurplusDay.setVisibility(View.VISIBLE);
                ((TemporaryItemViewHolder) holder).tvAutoDay.setText(String.valueOf(daysBetween(lockKeysBean.getEndTime())));
            }
            if (type == AuthorizeLogBean.AUTO_STATE_COUNT) {
                ((TemporaryItemViewHolder) holder).llSurplusCount.setVisibility(View.VISIBLE);
                ((TemporaryItemViewHolder) holder).llSurplusDay.setVisibility(View.GONE);
                ((TemporaryItemViewHolder) holder).tvAutoCount.setText(String.valueOf(lockKeysBean.getCount()));
            }
            if (type == AuthorizeLogBean.AUTO_STATE_TIME_COUNT) {
                ((TemporaryItemViewHolder) holder).llSurplusCount.setVisibility(View.VISIBLE);
                ((TemporaryItemViewHolder) holder).llSurplusDay.setVisibility(View.VISIBLE);
                ((TemporaryItemViewHolder) holder).tvAutoCount.setText(String.valueOf(lockKeysBean.getCount()));
                ((TemporaryItemViewHolder) holder).tvAutoDay.setText(String.valueOf(daysBetween(lockKeysBean.getEndTime())));
            }
        }
        if (holder instanceof OwerItemViewHolder) {
            holder.itemView.setTag(position);
            LockKeysBean lockKeysBean = list.get(position);
            ((OwerItemViewHolder) holder).tvDepNo.setText(lockKeysBean.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        int userType = list.get(position).getUserType();
        if (userType == USER_TYPE_TEPORARY) {
            return TYPE_TEMPORARY;
        } else {
            return TYPE_OWNER;
        }
    }

    private class OwerItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepNo;


        OwerItemViewHolder(View itemView) {
            super(itemView);
            tvDepNo = (TextView) itemView.findViewById(R.id.tv_depNo);

        }
    }

    private class TemporaryItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDepNo;
        private LinearLayout llSurplusCount;
        private TextView tvAutoCount;
        private LinearLayout llSurplusDay;
        private TextView tvAutoDay;

        TemporaryItemViewHolder(View itemView) {
            super(itemView);
            tvDepNo = (TextView) itemView.findViewById(R.id.tv_depNo);
            llSurplusCount = (LinearLayout) itemView.findViewById(R.id.ll_surplusCount);
            tvAutoCount = (TextView) itemView.findViewById(R.id.tv_auto_count);
            llSurplusDay = (LinearLayout) itemView.findViewById(R.id.ll_surplusDay);
            tvAutoDay = (TextView) itemView.findViewById(R.id.tv_auto_day);
        }
    }

}
