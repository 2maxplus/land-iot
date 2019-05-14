package com.hyf.iot.contract

import com.hyf.iot.domain.Starred
import com.hyf.iot.contract.base.ListContract

/**
 * Created by L on 2017/9/21.
 */
interface StarredContract {

    interface IView : ListContract.IView<Starred>

    interface IPresenter : ListContract.IPresenter
}