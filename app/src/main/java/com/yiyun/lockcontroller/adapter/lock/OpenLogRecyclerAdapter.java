package com.yiyun.lockcontroller.adapter.lock;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.LockOpenBean;
import com.yiyun.lockcontroller.bean.lock.QueryOpenLogBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.yiyun.lockcontroller.bean.lock.LockOpenBean.OPEN_COMMON;
import static com.yiyun.lockcontroller.bean.lock.LockOpenBean.OPEN_FAIL;

/**
 * 查询历史记录里的列表
 * Created by Layo on 2017-7-27.
 */
public class OpenLogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<LockOpenBean> list;
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

    public OpenLogRecyclerAdapter(Context context, List<LockOpenBean> list) {
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
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_open_log_item_, parent,
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
            LockOpenBean lockOpenBean = list.get(position);
            switch (lockOpenBean.getOpenType()){
                case OPEN_COMMON:
                    ((ItemViewHolder) holder).tvOpenStuName.setText("自身开锁");
                    break;
                case OPEN_FAIL:
                    ((ItemViewHolder) holder).tvOpenStuName.setText("开锁失败");
                    break;
                default:
                    ((ItemViewHolder) holder).tvOpenStuName.setText("他人开锁");

            }
            String date = dateFormat(lockOpenBean.getDate());
            ((ItemViewHolder) holder).tvOpenDate.setText(date);
        }

    }

    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1 == getItemCount())) {
            if (position % QueryOpenLogBean.addRecord == 0)
                return TYPE_FOOTER;
            else
                return TYPE_BOTTOM;
        } else {
            return TYPE_ITEM;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOpenStuName;
        private TextView tvOpenDate;
        private CardView cvOpen;

        ItemViewHolder(View itemView) {
            super(itemView);
            cvOpen = (CardView) itemView.findViewById(R.id.cv_open);
            tvOpenStuName = (TextView) itemView.findViewById(R.id.tv_open_stuName);
            tvOpenDate = (TextView) itemView.findViewById(R.id.tv_open_date);

        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String dateFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        Date date = new Date(time);

        return sdf.format(date);

    }
}
