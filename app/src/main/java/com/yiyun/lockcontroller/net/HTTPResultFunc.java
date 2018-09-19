package com.yiyun.lockcontroller.net;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by joker on 2017/7/17.
 */

public class HTTPResultFunc<T> implements Function<HTTPResult<T>, T> {

    @Override
    public T apply(@NonNull HTTPResult<T> tHTTPResult) throws Exception {
        if (isError(tHTTPResult.getState())) {
           
            throw new ApiException(tHTTPResult.getErrCode());
        }

        return tHTTPResult.getData();
    }

    private boolean isDecodeError(int errCode) {
        return errCode == ApiException.LOCK_SERVER_DENCRYPT_EXCEPTION;
    }

    private boolean isError(int code) {
        return code != StateCode.NORMAL_STATE;
    }
}
