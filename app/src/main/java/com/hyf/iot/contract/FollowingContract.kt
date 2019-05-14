package com.hyf.iot.contract

import com.hyf.iot.contract.base.ListContract
import com.hyf.iot.domain.Following

/**
 * Created by L on 2017/10/9.
 */
interface FollowingContract {

    interface IView : ListContract.IView<Following>

    interface IPresenter : ListContract.IPresenter
}