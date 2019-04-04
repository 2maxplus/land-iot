package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.contract.base.ListContract
import com.hyf.intelligence.iot.domain.Follower

/**
 * Created by L on 2017/9/22.
 */
interface FollowersContract {

    interface IView :  ListContract.IView<Follower>

    interface IPresenter : ListContract.IPresenter
}