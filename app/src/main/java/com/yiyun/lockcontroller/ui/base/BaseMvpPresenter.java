package com.yiyun.lockcontroller.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by layo on 2017/7/17.
 */

public abstract class BaseMvpPresenter<T extends BaseView> implements BasePresenter {
    private Reference<T> mViewRef;
    private CompositeDisposable mDisposable;

    public BaseMvpPresenter(T mView) {
        mViewRef = new WeakReference<>(mView);
        mDisposable = new CompositeDisposable();
    }

    protected T getView(){
        return mViewRef.get();
    }

    protected void addSubscription(Disposable disposable) {
        mDisposable.add(disposable);
    }

    public void disposableSubscription() {
        if (mDisposable != null) {
            mDisposable.clear();
        }
    }

}
