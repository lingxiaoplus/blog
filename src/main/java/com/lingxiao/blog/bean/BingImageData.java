package com.lingxiao.blog.bean;

import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
public class BingImageData implements Serializable {

    /**
     * images : [{"startdate":"20200402","fullstartdate":"202004021600","enddate":"20200403","url":"/th?id=OHR.PlaceofRainbows_ZH-CN7878813025_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.PlaceofRainbows_ZH-CN7878813025","copyright":"维多利亚瀑布上的彩虹，赞比亚 (© Dietmar Temps/Shutterstock)","copyrightlink":"https://www.bing.com/search?q=%E7%BB%B4%E5%A4%9A%E5%88%A9%E4%BA%9A%E7%80%91%E5%B8%83&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200402_PlaceofRainbows%22&FORM=HPQUIZ","wp":true,"hsh":"5ae21f50597d57a8df1719a73b77a35f","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200401","fullstartdate":"202004011600","enddate":"20200402","url":"/th?id=OHR.PascuaFlorida_ZH-CN7720904158_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.PascuaFlorida_ZH-CN7720904158","copyright":"从庞塞德莱昂河口向灯塔望去，佛罗里达州 (© Jason Sponseller/Shutterstock)","copyrightlink":"https://www.bing.com/search?q=%E5%BA%9E%E5%A1%9E%E5%BE%B7%E8%8E%B1%E6%98%82&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200401_PascuaFlorida%22&FORM=HPQUIZ","wp":true,"hsh":"a92a96ddd8d1afb58646a5fa67291c66","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200331","fullstartdate":"202003311600","enddate":"20200401","url":"/th?id=OHR.ShyGuy_ZH-CN7391687938_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.ShyGuy_ZH-CN7391687938","copyright":"马拉克勒国家公园中一只隐身的chia象，南非林波波河 (© Staffan Widstrand/Minden Pictures)","copyrightlink":"https://www.bing.com/search?q=%E9%A9%AC%E6%8B%89%E5%85%8B%E5%8B%92%E5%9B%BD%E5%AE%B6%E5%85%AC%E5%9B%AD&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200331_ShyGuy%22&FORM=HPQUIZ","wp":true,"hsh":"3508f1d2b052656f845a254adc1b00ea","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200330","fullstartdate":"202003301600","enddate":"20200331","url":"/th?id=OHR.CarrickSpring_ZH-CN7085146237_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.CarrickSpring_ZH-CN7085146237","copyright":"连接巴林托伊附近两处悬崖的Carrick-a-Rede索桥，北爱尔兰安特里姆 (© NordicMoonlight/iStock/Getty Images Plus)","copyrightlink":"https://www.bing.com/search?q=Carrick-a-Rede%E7%B4%A2%E6%A1%A5&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200330_CarrickSpring%22&FORM=HPQUIZ","wp":true,"hsh":"a3993e74d17f874407cefa4cb707ab90","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200329","fullstartdate":"202003291600","enddate":"20200330","url":"/th?id=OHR.WalkingCentral_ZH-CN6818231087_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.WalkingCentral_ZH-CN6818231087","copyright":"中央公园，纽约 (© Tony Shi Photography/Getty Images)","copyrightlink":"https://www.bing.com/search?q=%E4%B8%AD%E5%A4%AE%E5%85%AC%E5%9B%AD&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200329_WalkingCentral%22&FORM=HPQUIZ","wp":true,"hsh":"5a42fa3ad357f8673211f4a0bf5737e0","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200328","fullstartdate":"202003281600","enddate":"20200329","url":"/th?id=OHR.BorrowingDays_ZH-CN3558219803_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.BorrowingDays_ZH-CN3558219803","copyright":"一只经受暴风雨的冠山雀，苏格兰 (© Ben Hall/Minden Pictures)","copyrightlink":"https://www.bing.com/search?q=%E5%86%A0%E5%B1%B1%E9%9B%80&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200328_BorrowingDays%22&FORM=HPQUIZ","wp":true,"hsh":"d43b345a7ce721f5f22b8544be356a90","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200327","fullstartdate":"202003271600","enddate":"20200328","url":"/th?id=OHR.FormentorHolidays_ZH-CN3392936755_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.FormentorHolidays_ZH-CN3392936755","copyright":"悬崖边的福门托尔角灯塔，西班牙马略卡岛 (© Lasse Eklöf/DEEPOL by plainpicture)","copyrightlink":"https://www.bing.com/search?q=%E7%A6%8F%E9%97%A8%E6%89%98%E5%B0%94%E8%A7%92%E7%81%AF%E5%A1%94&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200327_FormentorHolidays%22&FORM=HPQUIZ","wp":true,"hsh":"0fe6452f6e3c9a3a0ea3a1bbff279c26","drk":1,"top":1,"bot":1,"hs":[]},{"startdate":"20200326","fullstartdate":"202003261600","enddate":"20200327","url":"/th?id=OHR.CharlestonAzaleas_ZH-CN3924268565_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp","urlbase":"/th?id=OHR.CharlestonAzaleas_ZH-CN3924268565","copyright":"在玉兰种植园里盛开的杜鹃花，南卡罗来纳州查尔斯顿 (© Joanne Wells/Danita Delimont)","copyrightlink":"https://www.bing.com/search?q=%E6%9D%9C%E9%B9%83%E8%8A%B1&form=hpcapt&mkt=zh-cn","title":"","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200326_CharlestonAzaleas%22&FORM=HPQUIZ","wp":true,"hsh":"e401bc7af8a345e98e8829ba9172e65a","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    private TooltipsBean tooltips;
    private List<ImagesBean> images;

    public TooltipsBean getTooltips() {
        return tooltips;
    }

    public void setTooltips(TooltipsBean tooltips) {
        this.tooltips = tooltips;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    @ToString
    public static class TooltipsBean implements Serializable{
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        private String loading;
        private String previous;
        private String next;
        private String walle;
        private String walls;

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getWalle() {
            return walle;
        }

        public void setWalle(String walle) {
            this.walle = walle;
        }

        public String getWalls() {
            return walls;
        }

        public void setWalls(String walls) {
            this.walls = walls;
        }
    }

    @ToString
    public static class ImagesBean implements Serializable{
        /**
         * startdate : 20200402
         * fullstartdate : 202004021600
         * enddate : 20200403
         * url : /th?id=OHR.PlaceofRainbows_ZH-CN7878813025_1920x1080.jpg&rf=LaDigue_1920x1080.jpg&pid=hp
         * urlbase : /th?id=OHR.PlaceofRainbows_ZH-CN7878813025
         * copyright : 维多利亚瀑布上的彩虹，赞比亚 (© Dietmar Temps/Shutterstock)
         * copyrightlink : https://www.bing.com/search?q=%E7%BB%B4%E5%A4%9A%E5%88%A9%E4%BA%9A%E7%80%91%E5%B8%83&form=hpcapt&mkt=zh-cn
         * title :
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20200402_PlaceofRainbows%22&FORM=HPQUIZ
         * wp : true
         * hsh : 5ae21f50597d57a8df1719a73b77a35f
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        private String startdate;
        private String fullstartdate;
        private String enddate;
        private String url;
        private String urlbase;
        private String copyright;
        private String copyrightlink;
        private String title;
        private String quiz;
        private boolean wp;
        private String hsh;
        private int drk;
        private int top;
        private int bot;

        public String getStartdate() {
            return startdate;
        }

        public void setStartdate(String startdate) {
            this.startdate = startdate;
        }

        public String getFullstartdate() {
            return fullstartdate;
        }

        public void setFullstartdate(String fullstartdate) {
            this.fullstartdate = fullstartdate;
        }

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlbase() {
            return urlbase;
        }

        public void setUrlbase(String urlbase) {
            this.urlbase = urlbase;
        }

        public String getCopyright() {
            return copyright;
        }

        public void setCopyright(String copyright) {
            this.copyright = copyright;
        }

        public String getCopyrightlink() {
            return copyrightlink;
        }

        public void setCopyrightlink(String copyrightlink) {
            this.copyrightlink = copyrightlink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getQuiz() {
            return quiz;
        }

        public void setQuiz(String quiz) {
            this.quiz = quiz;
        }

        public boolean isWp() {
            return wp;
        }

        public void setWp(boolean wp) {
            this.wp = wp;
        }

        public String getHsh() {
            return hsh;
        }

        public void setHsh(String hsh) {
            this.hsh = hsh;
        }

        public int getDrk() {
            return drk;
        }

        public void setDrk(int drk) {
            this.drk = drk;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBot() {
            return bot;
        }

        public void setBot(int bot) {
            this.bot = bot;
        }
    }
}
