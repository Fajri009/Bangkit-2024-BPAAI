package com.example.bangkit_2024_bpaai.AdvanceUI.WebView

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bangkit_2024_bpaai.R

class MainWebView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_web_view)

        val webView = findViewById<WebView>(R.id.webView)
        // untuk mengaktifkan JavaScript pada sebuah website
        webView.settings.javaScriptEnabled = true


        // untuk melakukan konfigurasi pada WebViewClient
        // WebViewClient merupakan client yang ada pada WebView
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
//                Toast.makeText(this@MainWebView, "Web Dicoding berhasil dimuat", Toast.LENGTH_LONG).show()
                view.loadUrl("javascript:alert('Web Dicoding berhasil dimuat')")
            }
        }

        // untuk menampilkan sebuah alert dibutuhkan WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView, url: String, message: String, result: android.webkit.JsResult): Boolean {
                Toast.makeText(this@MainWebView, message, Toast.LENGTH_LONG).show()
                result.confirm()
                return true
            }
        }

        // untuk memuat sebuah url dari website
        webView.loadUrl("https://www.dicoding.com")
    }
}