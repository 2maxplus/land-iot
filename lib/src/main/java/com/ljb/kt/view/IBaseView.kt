package com.ljb.kt.view

import com.ljb.kt.contract.IPresenterContract

interface IBaseView<out P : IPresenterContract> {
    fun registerPresenter(): Class<out P>
}
