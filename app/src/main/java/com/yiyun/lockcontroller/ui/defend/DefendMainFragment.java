package com.yiyun.lockcontroller.ui.defend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.presenter.defend.DefendCollectPresenter;
import com.yiyun.lockcontroller.presenter.defend.contract.DefendCollectContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpFragment;

/**
 * Created by Layo on 2018-2-2.
 */

public class DefendMainFragment extends BaseMvpFragment<DefendCollectContract.Presenter>
        implements DefendCollectContract.View {
    private TextView tvShow;

    @Override
    protected DefendCollectContract.Presenter initPresenter() {
        return new DefendCollectPresenter(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_defend_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvShow = (TextView) view.findViewById(R.id.tv_show);
        tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void showError(CharSequence msg) {

    }

    @Override
    public void showData() {

    }


}
