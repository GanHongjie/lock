package com.yiyun.lockcontroller.presenter.defend;

import com.yiyun.lockcontroller.presenter.defend.contract.DefendCollectContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;

/**
 * 云卫一号的收集p层
 * Created by Layo on 2018-2-2.
 */

public class DefendCollectPresenter extends BaseMvpPresenter<DefendCollectContract.View>
        implements DefendCollectContract.Presenter {
    public DefendCollectPresenter(DefendCollectContract.View mView) {
        super(mView);
    }

    @Override
    public void collectMsg() {

    }
}
