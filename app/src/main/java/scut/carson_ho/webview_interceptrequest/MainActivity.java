package scut.carson_ho.webview_interceptrequest;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebview = (WebView) findViewById(R.id.webview);
        // 创建WebView对象

        mWebview.getSettings().setJavaScriptEnabled(true);
        // 支持与JS交互

        mWebview.loadUrl("http://ip.cn/");
        // 加载需要显示的网页

        mWebview.setWebViewClient(new WebViewClient() {

             // 复写shouldInterceptRequest
             //API21以下用shouldInterceptRequest(WebView view, String url)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
                // 此处网页里图片的url为:http://s.ip-cdn.com/img/logo.gif
                // 图片的资源文件名为:logo.gif

                if (url.contains("logo.gif")) {

                    InputStream is = null;
                    // 步骤2:创建一个输入流


                    try {
                        is =getApplicationContext().getAssets().open("images/error.png");
                        // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                        // 在app/src/main下创建一个assets文件夹
                        // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 步骤4:替换资源

                    WebResourceResponse response = new WebResourceResponse("image/png",
                            "utf-8", is);
                    // 参数1:http请求里该图片的Content-Type,此处图片为image/png
                    // 参数2:编码类型
                    // 参数3:替换资源的输入流

                    System.out.println("旧API");
                    return response;
                }

                return super.shouldInterceptRequest(view, url);
            }


            // API21以上用shouldInterceptRequest(WebView view, WebResourceRequest request)
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                // 步骤1:判断拦截资源的条件，即判断url里的图片资源的文件名
                // 此处图片的url为:http://s.ip-cdn.com/img/logo.gif
                // 图片的资源文件名为:logo.gif
                if (request.getUrl().toString().contains("logo.gif")) {

                    InputStream is = null;
                    // 步骤2:创建一个输入流

                    try {
                        is = getApplicationContext().getAssets().open("images/error.png");
                        // 步骤3:打开需要替换的资源(存放在assets文件夹里)
                        // 在app/src/main下创建一个assets文件夹
                        // assets文件夹里再创建一个images文件夹,放一个error.png的图片

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //步骤4:替换资源

                    WebResourceResponse response = new WebResourceResponse("image/png",
                            "utf-8", is);
                    // 参数1：http请求里该图片的Content-Type,此处图片为image/png
                    // 参数2：编码类型
                    // 参数3：存放着替换资源的输入流（上面创建的那个）

                    return response;
                }
                return super.shouldInterceptRequest(view, request);
            }

    });

}
}