package com.lvhui.lvaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String url = "https://image.baidu.com/search/detail?z=0&word=%E7%8B%97&hs=0&pn=10&spn=0&di=7498023338351001601&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&lm=&cs=1016313609%2C3808885012&os=333002965%2C20611733&simid=3499921119%2C259944943&adpicid=0&lpn=0&fr=click-pic&fm=&ic=&hd=&latest=&copyright=&isImgSet=&commodity=&hot=&imgratio=&imgformat=&sme=&width=0&height=0&cg=&bdtype=0&oriquery=&objurl=https%3A%2F%2Fq9.itc.cn%2Fimages01%2F20241213%2F75c18fcbeeff4e7ca37f04a46c1781a5.jpeg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bf5i7_z%26e3Bv54AzdH3FwAzdH3Fbnmdcmln0_8d8n9lm0b&gsm=1e&islist=&querylist=&lid=8353597879474812645";
        String file_name = "dog.jpg";
        String result = resourceDownloadTool.downloadResource(url,file_name);
        Assertions.assertNotNull(result);

    }
}