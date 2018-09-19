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
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Layo on 2017-8-2.
 */

public class  AutoValidFalseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<AuthorizeLogBean> list;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_BOTTOM = 2;

    //自定义监听事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, final int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public AutoValidFalseAdapter(Context context, List<AuthorizeLogBean> list) {
        mContext = context;
        this.list = list;
    }

    public void refresh(List<AuthorizeLogBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建viewholder
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_auto_invalid_item, parent,
                    false);
            view.setOnClickListener(this);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_open_log_foot, parent,
                    false);
            return new FootViewHolder(view);
        } else if (viewType == TYPE_BOTTOM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_open_log_bottom, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            holder.itemView.setTag(position);
            AuthorizeLogBean authorizeLogBean = list.get(position);
            ((ItemViewHolder) holder).tvAutoUsername.setText(authorizeLogBean.getAutoUsername());
            String startTime = dateFormat(authorizeLogBean.getStart());
            ((ItemViewHolder) holder).tvStartTime.setText(startTime);
            String endTime = dateFormat(authorizeLogBean.getEnd());
            ((ItemViewHolder) holder).tvEndTime.setText(endTime);
            ((ItemViewHolder) holder).tvSurplusCount.setText(String.valueOf(authorizeLogBean.getCount()));
            int type = authorizeLogBean.getType();
            if (type == AuthorizeLogBean.AUTO_STATE_TIME || type == AuthorizeLogBean.AUTO_STATE_TIME_COUNT) {
                ((ItemViewHolder) holder).llSurplusDay.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).llSurplusDay.setVisibility(View.GONE);
            }
            if (type == AuthorizeLogBean.AUTO_REASON_COUNT || type == AuthorizeLogBean.AUTO_STATE_TIME_COUNT) {
                ((ItemViewHolder) holder).llSurplusCount.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).llSurplusCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1 == getItemCount())) {
            if (position % QueryAutoBean.addRecord == 0)
                return TYPE_FOOTER;
            else
                return TYPE_BOTTOM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAutoUsername;
        private LinearLayout llSurplusDay;
        private TextView tvStartTime;
        private TextView tvEndTime;
        private LinearLayout llSurplusCount;
        private TextView tvSurplusCount;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvAutoUsername = (TextView) itemView.findViewById(R.id.tv_auto_username);
            llSurplusDay = (LinearLayout) itemView.findViewById(R.id.ll_surplusDay);
            tvStartTime = (TextView) itemView.findViewById(R.id.tv_startTime);
            tvEndTime = (TextView) itemView.findViewById(R.id.tv_endTime);
            llSurplusCount = (LinearLayout) itemView.findViewById(R.id.ll_surplusCount);
            tvSurplusCount = (TextView) itemView.findViewById(R.id.tv_surplusCount);

        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String dateFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(time);

        return sdf.format(date);
    }

}
