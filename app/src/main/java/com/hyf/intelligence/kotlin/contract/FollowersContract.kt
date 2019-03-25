package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.contract.base.ListContract
import com.hyf.intelligence.kotlin.domain.Follower

/**
 * Created by L on 2017/9/22.
 */
interface FollowersContract {

    interface IView :  ListContract.IView<Follower>

    interface IPresenter : ListContract.IPresenter
}