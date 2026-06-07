package com.luckhost.lockscreen_notes.presentation.screens.userLogin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.luckhost.domain.useCases.github.SaveGithubUsernameUseCase
import org.koin.android.ext.android.inject

class GitHubCallbackActivity : AppCompatActivity() {
    private val saveGithubUsernameUseCase by inject<SaveGithubUsernameUseCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data
        when (uri?.host) {
            "github-success" -> {
                val username = uri.getQueryParameter("username")
                if (username != null) {
                    saveGithubUsernameUseCase.execute(username)
                    Toast.makeText(this, "GitHub: @$username подключён", Toast.LENGTH_SHORT).show()
                }
            }
            "github-error" -> {
                val message = uri.getQueryParameter("message") ?: "Неизвестная ошибка"
                Toast.makeText(this, "Ошибка GitHub: $message", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}
