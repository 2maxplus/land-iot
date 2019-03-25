package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.contract.base.ListContract
import com.hyf.intelligence.kotlin.domain.Following

/**
 * Created by L on 2017/10/9.
 */
interface FollowingContract {

    interface IView : ListContract.IView<Following>

    interface IPresenter : ListContract.IPresenter
}