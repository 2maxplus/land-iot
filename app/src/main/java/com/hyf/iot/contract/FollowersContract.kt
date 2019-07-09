package com.hyf.iot.contract

import com.hyf.iot.contract.base.ListContract
import com.hyf.iot.domain.Follower


interface FollowersContract {

    interface IView :  ListContract.IView<Follower>

    interface IPresenter : ListContract.IPresenter
}