package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.contract.base.ListContract
import com.hyf.intelligence.iot.domain.Starred

/**
 * Created by L on 2017/9/21.
 */
interface StarredContract {

    interface IView : ListContract.IView<Starred>

    interface IPresenter : ListContract.IPresenter
}