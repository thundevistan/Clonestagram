package com.kotdev99.android.clonestagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kotdev99.android.clonestagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

	private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
	private var auth: FirebaseAuth? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		auth = FirebaseAuth.getInstance()

		initView()
	}

	private fun initView() = with(binding) {
		btnSignIn.setOnClickListener {
			signInAndSignup()
		}
	}

	// 회원가입 or 로그인 에러 or 로그인 분기
	private fun signInAndSignup() {
		auth?.createUserWithEmailAndPassword(    // 이메일, 패스워드 입력
			binding.edtEmail.text.toString(),
			binding.edtPassword.text.toString()
		)?.addOnCompleteListener { task ->       // 회원가입한 결과값 획득
			if (task.isSuccessful) {
				// Creating a user account
				moveMainPage(task.result.user)
			} else if (task.exception?.message.isNullOrEmpty()) {
				// Show the error message
				showToast(task.exception?.message.toString())
			} else {
				// Login if you have account
				signInEmail()
			}
		}
	}

	// 로그인
	private fun signInEmail() {
		auth?.signInWithEmailAndPassword(
			binding.edtEmail.text.toString(),
			binding.edtPassword.text.toString()
		)?.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				// Login
				moveMainPage(task.result.user)
			} else {
				// Login Failed
				showToast(task.exception?.message.toString())
			}
		}
	}

	// 로그인 성공 시 다음 페이지로 이동
	private fun moveMainPage(user: FirebaseUser?) {     // Firebase 유저 상태 전달
		if (user != null) {
			startActivity(Intent(this, MainActivity::class.java))
		}
	}
}