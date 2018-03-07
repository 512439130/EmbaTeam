package com.fala.emba.team.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 * 协会列表
 */

public class AssociationComm {

    /**
     * statusCode : 1
     * message : 操作成功
     * associations : [{"introduce":"activityIntroduce/java.html","memberCount":1,"name":"牙买加别墅与公寓协会","logo":"http://wms.fala.cn/emba/logo/java.png","chairman":"何杰龙","id":1,"openTime":"2017-12-04 08:55:00","slogan":"you need coffee"},{"introduce":"activityIntroduce/tm.html","memberCount":1,"name":"Tunnel Mice","logo":"http://wms.fala.cn/emba/logo/TM.jpg","chairman":"何杰龙","id":2,"openTime":"2017-12-04 09:10:00","slogan":"i can not tell you anything until you were invited"},{"introduce":"activityIntroduce/golf.html","memberCount":0,"name":"高尔夫协会","logo":"http://wms.fala.cn/emba/logo/golf.jpg","chairman":"测试会长","id":3,"openTime":"2017-12-04 09:25:00","slogan":"最有意义的运动就是高尔夫，因为小朋友就是因为空间有限才会拿砖头扔破你家窗户，而你完全不必担心这个问题\u2014\u2014其实有钱人不玩这个"},{"introduce":"activityIntroduce/outdoor.html","memberCount":0,"name":"户外协会","logo":"http://wms.fala.cn/emba/logo/outdoor.jpg","chairman":"测试会长","id":4,"openTime":"2017-12-04 09:40:00","slogan":"你不必再当翻山越岭的征服者，不必再像沉默坚韧的苦行僧，现在参加户外协会，又晒又无聊，都来吧，一起浪费生命吧，来当自娱自乐的小伙伴吧"},{"introduce":"activityIntroduce/badminton.html","memberCount":0,"name":"羽毛球协会","logo":"http://wms.fala.cn/emba/logo/badminton.jpg","chairman":"测试会长","id":5,"openTime":"2017-12-04 09:55:00","slogan":"很普通的协会，可以治颈椎病"},{"introduce":"activityIntroduce/football.html","memberCount":0,"name":"足球协会","logo":"http://wms.fala.cn/emba/logo/football.jpg","chairman":"测试会长","id":6,"openTime":"2017-12-04 10:10:00","slogan":"球只有一个，里面有炸弹，快踢到敌人窝里，可以炸一堆"},{"introduce":"activityIntroduce/gt.html","memberCount":0,"name":"慧兰荟","logo":"http://wms.fala.cn/emba/logo/female.jpg","chairman":"测试会长","id":7,"openTime":"2017-12-04 10:25:00","slogan":"查了下是个女生协会，聚在一起平时也不知道干啥\u2014\u2014名利场"},{"introduce":"activityIntroduce/asdadcer","memberCount":0,"name":"网球协会","logo":"","chairman":"测试会长","id":8,"openTime":"2017-12-04 10:40:00","slogan":"网球协会"},{"introduce":"activityIntroduce/basketball.html","memberCount":0,"name":"篮球协会","logo":"http://wms.fala.cn/emba/logo/basketball.jpg","chairman":"测试会长","id":9,"openTime":"2017-12-04 10:55:00","slogan":"霸场子有经验者优先"},{"introduce":"activityIntroduce/awefanda","memberCount":0,"name":"排球协会","logo":"","chairman":"测试会长","id":10,"openTime":"2017-12-04 11:10:00","slogan":"不管在哪里打排球风景都不错"}]
     * hasNext : Y
     */

    private int statusCode;
    private String message;
    private String hasNext;
    private List<AssociationsBean> associations;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHasNext() {
        return hasNext;
    }

    public void setHasNext(String hasNext) {
        this.hasNext = hasNext;
    }

    public List<AssociationsBean> getAssociations() {
        return associations;
    }

    public void setAssociations(List<AssociationsBean> associations) {
        this.associations = associations;
    }

    public static class AssociationsBean {
        /**
         * introduce : activityIntroduce/java.html
         * memberCount : 1
         * name : 牙买加别墅与公寓协会
         * logo : http://wms.fala.cn/emba/logo/java.png
         * chairman : 何杰龙
         * id : 1
         * openTime : 2017-12-04 08:55:00
         * slogan : you need coffee
         */

        private String introduce;
        private int memberCount;
        private String name;
        private String logo;
        private String chairman;
        private int id;
        private String openTime;
        private String slogan;

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getChairman() {
            return chairman;
        }

        public void setChairman(String chairman) {
            this.chairman = chairman;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }
    }
}
