package com.fala.emba.team.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/2.
 * 用户信息
 */

public class UserComm {


    /**
     * statusCode : 1
     * message : 操作成功
     * user : {"birthday":"1994-02-07","sex":"男","headUrl":"http://wms.fala.cn/emba/userHead/13160677911.png?timestamp=20171207223824","className":"北理4","phone":"13160677911","identity":"MBA","clothesSize":"XL","name":"杨阳","company":"化工中云","job":"Android工程师","height":"176cm","registerDate":"2017-12-05 09:11:15","associations":[{"name":"acm协会","id":19},{"name":"acm条毛协会","id":20},{"name":"假装13协会","id":21}]}
     */

    private int statusCode;
    private String message;
    private UserBean user;
    /**
     * token : 3A67D0EBECD54477BB67BC3211A64950
     */

    private String token;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserBean {
        /**
         * birthday : 1994-02-07
         * sex : 男
         * headUrl : http://wms.fala.cn/emba/userHead/13160677911.png?timestamp=20171207223824
         * className : 北理4
         * phone : 13160677911
         * identity : MBA
         * clothesSize : XL
         * name : 杨阳
         * company : 化工中云
         * job : Android工程师
         * height : 176cm
         * registerDate : 2017-12-05 09:11:15
         * associations : [{"name":"acm协会","id":19},{"name":"acm条毛协会","id":20},{"name":"假装13协会","id":21}]
         */

        private String birthday;
        private String sex;
        private String headUrl;
        private String className;
        private String phone;
        private String identity;
        private String clothesSize;
        private String name;
        private String company;
        private String job;
        private String height;
        private String registerDate;
        private List<AssociationsBean> associations;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getClothesSize() {
            return clothesSize;
        }

        public void setClothesSize(String clothesSize) {
            this.clothesSize = clothesSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
        }

        public List<AssociationsBean> getAssociations() {
            return associations;
        }

        public void setAssociations(List<AssociationsBean> associations) {
            this.associations = associations;
        }

        public static class AssociationsBean {
            /**
             * name : acm协会
             * id : 19
             */

            private String name;
            private int id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
