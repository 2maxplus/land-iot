package com.hyf.intelligence.iot.protocol.dao.impl

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.hyf.intelligence.iot.common.Constant.DBProvider.TABLE_USERS
import com.hyf.intelligence.iot.db.DatabaseProvider
import com.hyf.intelligence.iot.domain.User
import com.hyf.intelligence.iot.protocol.dao.IUserDaoProtocol
import com.hyf.intelligence.iot.protocol.dao.base.BaseDaoProtocol

/**
 * Created by L on 2017/7/17.
 */
class UserDaoProtocol : BaseDaoProtocol(), IUserDaoProtocol {

    override fun saveUser(context: Context, user: User) = createObservable {
        saveUserImpl(context, user)
    }

    private fun saveUserImpl(context: Context, user: User): Boolean =
            if (findUserByUserIdImpl(context, user.id) == null) {
                insertUserImpl(context, user)
            } else {
                updateUserImpl(context, user)
            }


    override fun insertUser(context: Context, user: User) = createObservable {
        insertUserImpl(context, user)
    }

    private fun insertUserImpl(context: Context, user: User): Boolean {
        var result = false
        val values = ContentValues()
        values.put(TABLE_USERS.COLUMN_LOGIN, user.login)
        values.put(TABLE_USERS.COLUMN_USER_ID, user.id)
        values.put(TABLE_USERS.COLUMN_AVATAR_URL, user.avatar_url)
        values.put(TABLE_USERS.COLUMN_GRAVATAR_ID, user.gravatar_id)
        values.put(TABLE_USERS.COLUMN_URL, user.url)
        values.put(TABLE_USERS.COLUMN_HTML_URL, user.html_url)
        values.put(TABLE_USERS.COLUMN_FOLLOWERS_URL, user.followers_url)
        values.put(TABLE_USERS.COLUMN_FOLLOWING_URL, user.following_url)
        values.put(TABLE_USERS.COLUMN_GISTS_URL, user.gists_url)
        values.put(TABLE_USERS.COLUMN_STARRED_URL, user.starred_url)
        values.put(TABLE_USERS.COLUMN_SUBSCRIPTIONS_URL, user.subscriptions_url)
        values.put(TABLE_USERS.COLUMN_ORGANIZATIONS_URL, user.organizations_url)
        values.put(TABLE_USERS.COLUMN_REPOS_URL, user.repos_url)
        values.put(TABLE_USERS.COLUMN_EVENTS_URL, user.events_url)
        values.put(TABLE_USERS.COLUMN_RECEIVED_EVENTS_URL, user.received_events_url)
        values.put(TABLE_USERS.COLUMN_TYPE, user.type)
        values.put(TABLE_USERS.COLUMN_SITE_ADMIN, if (user.site_admin) 1 else 0)
        values.put(TABLE_USERS.COLUMN_NAME, user.name)
        values.put(TABLE_USERS.COLUMN_COMPANY, user.company)
        values.put(TABLE_USERS.COLUMN_BLOG, user.blog)
        values.put(TABLE_USERS.COLUMN_LOCATION, user.location)
        values.put(TABLE_USERS.COLUMN_EMAIL, user.email)
        values.put(TABLE_USERS.COLUMN_HIREABLE, user.hireable)
        values.put(TABLE_USERS.COLUMN_BIO, user.bio)
        values.put(TABLE_USERS.COLUMN_PUBLIC_REPOS, user.public_repos)
        values.put(TABLE_USERS.COLUMN_PUBLIC_GISTS, user.public_gists)
        values.put(TABLE_USERS.COLUMN_FOLLOWERS, user.followers)
        values.put(TABLE_USERS.COLUMN_FOLLOWING, user.following)
        values.put(TABLE_USERS.COLUMN_CREATED_AT, user.created_at)
        values.put(TABLE_USERS.COLUMN_UPDATED_AT, user.updated_at)
        try {
            val uri = context.contentResolver.insert(Uri.parse(DatabaseProvider.USER_CONTENT_URI), values)
            if (uri != null && ContentUris.parseId(uri) > 0) {
                result = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    override fun updateUser(context: Context, user: User) = createObservable {
        updateUserImpl(context, user)
    }

    private fun updateUserImpl(context: Context, user: User): Boolean {
        var result = false
        val values = ContentValues()
        values.put(TABLE_USERS.COLUMN_LOGIN, user.login)
        values.put(TABLE_USERS.COLUMN_USER_ID, user.id)
        values.put(TABLE_USERS.COLUMN_AVATAR_URL, user.avatar_url)
        values.put(TABLE_USERS.COLUMN_GRAVATAR_ID, user.gravatar_id)
        values.put(TABLE_USERS.COLUMN_URL, user.url)
        values.put(TABLE_USERS.COLUMN_HTML_URL, user.html_url)
        values.put(TABLE_USERS.COLUMN_FOLLOWERS_URL, user.followers_url)
        values.put(TABLE_USERS.COLUMN_FOLLOWING_URL, user.following_url)
        values.put(TABLE_USERS.COLUMN_GISTS_URL, user.gists_url)
        values.put(TABLE_USERS.COLUMN_STARRED_URL, user.starred_url)
        values.put(TABLE_USERS.COLUMN_SUBSCRIPTIONS_URL, user.subscriptions_url)
        values.put(TABLE_USERS.COLUMN_ORGANIZATIONS_URL, user.organizations_url)
        values.put(TABLE_USERS.COLUMN_REPOS_URL, user.repos_url)
        values.put(TABLE_USERS.COLUMN_EVENTS_URL, user.events_url)
        values.put(TABLE_USERS.COLUMN_RECEIVED_EVENTS_URL, user.received_events_url)
        values.put(TABLE_USERS.COLUMN_TYPE, user.type)
        values.put(TABLE_USERS.COLUMN_SITE_ADMIN, if (user.site_admin) 1 else 0)
        values.put(TABLE_USERS.COLUMN_NAME, user.name)
        values.put(TABLE_USERS.COLUMN_COMPANY, user.company)
        values.put(TABLE_USERS.COLUMN_BLOG, user.blog)
        values.put(TABLE_USERS.COLUMN_LOCATION, user.location)
        values.put(TABLE_USERS.COLUMN_EMAIL, user.email)
        values.put(TABLE_USERS.COLUMN_HIREABLE, user.hireable)
        values.put(TABLE_USERS.COLUMN_BIO, user.bio)
        values.put(TABLE_USERS.COLUMN_PUBLIC_REPOS, user.public_repos)
        values.put(TABLE_USERS.COLUMN_PUBLIC_GISTS, user.public_gists)
        values.put(TABLE_USERS.COLUMN_FOLLOWERS, user.followers)
        values.put(TABLE_USERS.COLUMN_FOLLOWING, user.following)
        values.put(TABLE_USERS.COLUMN_CREATED_AT, user.created_at)
        values.put(TABLE_USERS.COLUMN_UPDATED_AT, user.updated_at)
        try {
            val count = context.contentResolver.update(
                    Uri.parse(DatabaseProvider.USER_CONTENT_URI),
                    values,
                    "${TABLE_USERS.COLUMN_USER_ID}=?",
                    arrayOf("${user.id}"))
            result = count > 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    override fun findUserByUserId(context: Context, userId: Long) = createObservable {
        findUserByUserIdImpl(context, userId)
    }

    private fun findUserByUserIdImpl(context: Context, userId: Long): User? {
        var user: User? = null
        var c: Cursor? = null
        try {
            c = context.contentResolver.query(Uri.parse(DatabaseProvider.USER_CONTENT_URI),
                    null,
                    "${TABLE_USERS.COLUMN_USER_ID}=?",
                    arrayOf("$userId"),
                    null)
            if (c != null && c.moveToNext()) {
                val login = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_LOGIN))
                val userID = c.getLong(c.getColumnIndex(TABLE_USERS.COLUMN_USER_ID))
                val avatar_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_AVATAR_URL))
                val gravatar_id = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_GRAVATAR_ID))
                val url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_URL))
                val html_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_HTML_URL))
                val followers_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWERS_URL))
                val following_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWING_URL))
                val gists_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_GISTS_URL))
                val starred_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_STARRED_URL))
                val subscriptions_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_SUBSCRIPTIONS_URL))
                val organizations_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_ORGANIZATIONS_URL))
                val repos_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_REPOS_URL))
                val events_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_EVENTS_URL))
                val received_events_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_RECEIVED_EVENTS_URL))
                val type = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_TYPE))
                val site_admin = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_SITE_ADMIN))
                val name = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_NAME))
                val company = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_COMPANY))
                val blog = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_BLOG))
                val location = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_LOCATION))
                val email = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_EMAIL))
                val hireable = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_HIREABLE))
                val bio = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_BIO))
                val public_repos = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_PUBLIC_REPOS))
                val public_gists = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_PUBLIC_GISTS))
                val followers = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWERS))
                val following = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWING))
                val created_at = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_CREATED_AT))
                val updated_at = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_UPDATED_AT))
                user = User(login, userID, avatar_url, gravatar_id, url, html_url, followers_url, following_url, gists_url, starred_url, subscriptions_url, organizations_url, repos_url, events_url, received_events_url, type, site_admin == 1, name, company, blog, location, email, hireable, bio, public_repos, public_gists, followers, following, created_at, updated_at)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }
        return user
    }

    override fun findUserByName(context: Context, userName: String) = createObservable {
        findUserByNameImpl(context, userName)
    }

    private fun findUserByNameImpl(context: Context, userName: String): User? {
        var user: User? = null
        var c: Cursor? = null
        try {
            c = context.contentResolver.query(Uri.parse(DatabaseProvider.USER_CONTENT_URI),
                    null,
                    "${TABLE_USERS.COLUMN_LOGIN}=?",
                    arrayOf(userName),
                    null)
            if (c != null && c.moveToNext()) {
                val login = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_LOGIN))
                val userID = c.getLong(c.getColumnIndex(TABLE_USERS.COLUMN_USER_ID))
                val avatar_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_AVATAR_URL))
                val gravatar_id = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_GRAVATAR_ID))
                val url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_URL))
                val html_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_HTML_URL))
                val followers_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWERS_URL))
                val following_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWING_URL))
                val gists_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_GISTS_URL))
                val starred_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_STARRED_URL))
                val subscriptions_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_SUBSCRIPTIONS_URL))
                val organizations_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_ORGANIZATIONS_URL))
                val repos_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_REPOS_URL))
                val events_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_EVENTS_URL))
                val received_events_url = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_RECEIVED_EVENTS_URL))
                val type = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_TYPE))
                val site_admin = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_SITE_ADMIN))
                val name = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_NAME))
                val company = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_COMPANY))
                val blog = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_BLOG))
                val location = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_LOCATION))
                val email = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_EMAIL))
                val hireable = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_HIREABLE))
                val bio = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_BIO))
                val public_repos = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_PUBLIC_REPOS))
                val public_gists = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_PUBLIC_GISTS))
                val followers = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWERS))
                val following = c.getInt(c.getColumnIndex(TABLE_USERS.COLUMN_FOLLOWING))
                val created_at = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_CREATED_AT))
                val updated_at = c.getString(c.getColumnIndex(TABLE_USERS.COLUMN_UPDATED_AT))
                user = User(login, userID, avatar_url, gravatar_id, url, html_url, followers_url, following_url, gists_url, starred_url, subscriptions_url, organizations_url, repos_url, events_url, received_events_url, type, site_admin == 1, name, company, blog, location, email, hireable, bio, public_repos, public_gists, followers, following, created_at, updated_at)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }
        return user
    }

}