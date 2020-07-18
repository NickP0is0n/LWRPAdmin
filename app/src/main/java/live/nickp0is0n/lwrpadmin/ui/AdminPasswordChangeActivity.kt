/*
 * Это - исходный код приложения LWRP Admin v2.x.
 * Данное приложение является пропиетарным ПО и
 * его код не подлежит распространению без согласия автора.
 *
 * Copyright Mykola Chaikovskyi, 2020.
 */

package live.nickp0is0n.lwrpadmin.ui

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_admin_password_change.*
import kotlinx.android.synthetic.main.activity_main.*
import live.nickp0is0n.lwrpadmin.R
import live.nickp0is0n.lwrpadmin.models.User

class AdminPasswordChangeActivity : AppCompatActivity() {
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_password_change)
        user = intent.extras?.get("user") as User
    }

    fun onChangePasswordButtonClick(view: View) {
        if(!doFieldsContainErrors()) {
            progressBar3.visibility = VISIBLE
            sendRequestForAdminPasswordChange()
        }
    }

    private fun sendRequestForAdminPasswordChange() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://lwrp.ru/service/functions/hidden/fu3u8w2/changeAdminPassword.php"

        val adminPasswordChangeRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                run {
                    when(response) {
                        "success" -> {
                            val notification = Toast.makeText(this@AdminPasswordChangeActivity, "Ваш админ-пароль успешно изменен!", Toast.LENGTH_SHORT)
                            notification.show()
                        }
                        "fail" -> {
                            currentAdminPasswordField.error = "Серверная ошибка. Проверьте правильность админ-пароля!"
                        }
                        else -> {
                            val notification = Toast.makeText(this@AdminPasswordChangeActivity, response, Toast.LENGTH_SHORT)
                            notification.show()
                        }
                    }
                    progressBar3.visibility = INVISIBLE
                }
            },
            Response.ErrorListener {
                nameEdit.error = "Ошибка сервера, повторите позже"
                progressBar3.visibility = INVISIBLE
            }) {
            override fun getParams(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["username"] = user.nickname
                headers["password"] = currentPasswordField.text.toString()
                headers["old_admin_password"] = currentAdminPasswordField.text.toString()
                headers["new_admin_password"] = newAdminPasswordField.text.toString()
                return headers
            }
        }
        queue.add(adminPasswordChangeRequest)
    }

    private fun doFieldsContainErrors() : Boolean {
        when {
            currentPasswordField.text.toString() != user.password ->
                currentPasswordField.error = "Вы ввели неверный пароль!"
            newAdminPasswordField.text.toString() != repeatNewAdminPasswordField.text.toString() ->
                repeatNewAdminPasswordField.error = "Пароли не совпадают!"
            newAdminPasswordField.text.toString() == "qwerty" ->
                newAdminPasswordField.error = "Выберите другой пароль!"
            currentPasswordField.text.isNullOrEmpty() ->
                currentPasswordField.error = "Заполните это поле!"
            currentAdminPasswordField.text.isNullOrEmpty() ->
                currentAdminPasswordField.error = "Заполните это поле!"
            newAdminPasswordField.text.isNullOrEmpty() ->
                newAdminPasswordField.error = "Заполните это поле!"
            repeatNewAdminPasswordField.text.isNullOrEmpty() ->
                repeatNewAdminPasswordField.error = "Заполните это поле!"
            else -> return false
        }
        return true
    }
}