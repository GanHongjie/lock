package com.yiyun.lockcontroller.adapter.lock;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.utils.DateUtil;

import java.util.List;

import static com.yiyun.lockcontroller.utils.DateUtil.daysBetween;

/**
 * 授权人列表里查询有效授权人列表
 * Created by Layo on 2017-7-23.
 */
public class AutoValidTrueAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<AuthorizeLogBean> list;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_BOTTOM = 2;
    private String[] strPatters = {"", "时间段授权", "次数授权", "时间段与次数授权"};

    //自定义监听事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, final int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //适配器初始化
    public AutoValidTrueAdapter(Context context, List<AuthorizeLogBean> list) {
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.lock_auto_valid_item, parent,
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            holder.itemView.setTag(position);
            ((ItemViewHolder) holder).ivTrash.setTag(position);
            //给view((ItemViewHolder) holder)数据赋值
            ((ItemViewHolder) holder).swipeLayout.setTag(false);
            ((ItemViewHolder) holder).swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            ((ItemViewHolder) holder).swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    ((ItemViewHolder) holder).swipeLayout.setTag(true);
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    ((ItemViewHolder) holder).swipeLayout.setTag(false);
                }
            });

            ViewTreeObserver.OnGlobalLayoutListener swipeGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if ((boolean) (((ItemViewHolder) holder).swipeLayout.getTag())) {
                        // Opens the layout without animation
                        ((ItemViewHolder) holder).swipeLayout.open(false);
                    }
                }
            };
            ((ItemViewHolder) holder).swipeLayout.getViewTreeObserver().addOnGlobalLayoutListener(swipeGlobalLayoutListener);
            ((ItemViewHolder) holder).swipeLayout.getDragEdgeMap().clear();
            ((ItemViewHolder) holder).swipeLayout.addDrag(SwipeLayout.DragEdge.Left, ((ItemViewHolder) holder).swipeLayout.findViewById(R.id.ll_trash));
            ((ItemViewHolder) holder).ivTrash.setOnClickListener(this);

            //网络获得的数据
            AuthorizeLogBean authorizeLogBean = list.get(position);
            ((ItemViewHolder) holder).tvAutoUsername.setText(authorizeLogBean.getAutoUsername());
            int type = authorizeLogBean.getType();
            ((ItemViewHolder) holder).tvGrandType.setText(strPatters[type]);
            if (type == AuthorizeLogBean.AUTO_STATE_TIME) {
                ((ItemViewHolder) holder).llSurplusCount.setVisibility(View.GONE);
                ((ItemViewHolder) holder).llValidTime.setVisibility(View.VISIBLE);
                String start = DateUtil.getFormatTime(authorizeLogBean.getStart());
                String end = DateUtil.getFormatTime(authorizeLogBean.getEnd());
                ((ItemViewHolder) holder).tvAutoTime.setText((start + " ~ " + end));// String.valueOf(daysBetween(authorizeLogBean.getEnd()))
            } else if (type == AuthorizeLogBean.AUTO_STATE_COUNT) {
                ((ItemViewHolder) holder).llSurplusCount.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).llValidTime.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvAutoCount.setText(String.valueOf(authorizeLogBean.getCount()));
            } else if (type == AuthorizeLogBean.AUTO_STATE_TIME_COUNT) {
                ((ItemViewHolder) holder).llSurplusCount.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).llValidTime.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).tvAutoCount.setText(String.valueOf(authorizeLogBean.getCount()));
                String start = DateUtil.getFormatTime(authorizeLogBean.getStart());
                String end = DateUtil.getFormatTime(authorizeLogBean.getEnd());
                ((ItemViewHolder) holder).tvAutoTime.setText((start + " ~ " + end));// String.valueOf(daysBetween(authorizeLogBean.getEnd()))
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
        if (v.getId() == R.id.iv_trash) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
            }
        }

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout swipeLayout;
        private ImageView ivTrash;
        private CardView cvAuto;
        private TextView tvAutoUsername;
        private TextView tvAutoTime;
        private LinearLayout llSurplusCount;
        private TextView tvAutoCount;
        private LinearLayout llValidTime;
        private TextView tvGrandType;


        ItemViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            ivTrash = (ImageView) itemView.findViewById(R.id.iv_trash);
            cvAuto = (CardView) itemView.findViewById(R.id.cv_auto);
            tvAutoUsername = (TextView) itemView.findViewById(R.id.tv_username);
            llSurplusCount = (LinearLayout) itemView.findViewById(R.id.ll_surplusCount);
            tvAutoCount = (TextView) itemView.findViewById(R.id.tv_auto_count);
            llValidTime = (LinearLayout) itemView.findViewById(R.id.ll_valid_time);
            tvAutoTime = (TextView) itemView.findViewById(R.id.tv_auto_time);
            tvGrandType = (TextView) itemView.findViewById(R.id.tv_grand_type);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        FootViewHolder(View itemView) {
            super(itemView);
        }
    }


}
