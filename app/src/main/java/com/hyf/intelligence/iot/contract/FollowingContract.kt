package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.contract.base.ListContract
import com.hyf.intelligence.iot.domain.Following

/**
 * Created by L on 2017/10/9.
 */
interface FollowingContract {

    interface IView : ListContract.IView<Following>

    interface IPresenter : ListContract.IPresenter
}