package com.hyf.iot.protocol.dao

import com.hyf.iot.protocol.dao.impl.UserDaoProtocol

/**
 * Author:Ljb
 * Time:2018/8/10
 * There is a lot of misery in life
 **/
object DaoFactoryConfig {

    //TODO  在此处注册DAO接口
    @Suppress("UNCHECKED_CAST")
    fun <T> configProtocol(clazz: Class<T>): T = when (clazz) {
        IUserDaoProtocol::class.java -> UserDaoProtocol()
        else -> throw IllegalStateException("NotFound Dao Interface Object  : ${clazz.name}")
    } as T
}
