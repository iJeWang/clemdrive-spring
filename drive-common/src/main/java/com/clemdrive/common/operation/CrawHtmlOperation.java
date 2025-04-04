package com.clemdrive.common.operation;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.clemdrive.common.util.StringUtil;

import java.net.URL;


public class CrawHtmlOperation {
    public static String crawHtmlByUrl(String url) {
        return crawHtmlByUrl(url, "GET");
    }

    public static String crawHtmlByUrl(String url, String requestMethod) {
        System.out.println("抓取开始，url：" + url);
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(true);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        HtmlPage page = null;
        try {
            if ("POST".equals(requestMethod)) {
                URL urlRequest = new URL(url);
                WebRequest requestSettings = new WebRequest(urlRequest, HttpMethod.POST);
                requestSettings.setAdditionalHeader("Content-Type", "application/json");
                requestSettings.setRequestBody("{}");
                page = webClient.getPage(requestSettings);
            } else {
                page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
            }
            webClient.waitForBackgroundJavaScript(50000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webClient.close();
        }

        System.out.println("抓取结束");
        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
        pageXml = pageXml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<!DOCTYPE html>");
        pageXml = StringUtil.replaceBlank(pageXml);
        return pageXml;
    }

    public static void main(String[] args) {
        System.out.println(crawHtmlByUrl("https://www.baidu.com/"));
    }
}
