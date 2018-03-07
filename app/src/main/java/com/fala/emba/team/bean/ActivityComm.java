package com.fala.emba.team.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ActivityComm {


    /**
     * statusCode : 1
     * message : 操作成功
     * activities : [{"coverUrl":"http://images.amcnetworks.com/amc.com/wp-content/uploads/2010/12/logo_bb_stacked-C.png","isOverActivity":"N","memberCount":1,"name":"save Walter White!","orientedAssociations":"所有协会","startTime":"2017-12-10 09:30:00","isOverApply":"N","id":1,"place":"GZ","endTime":"2017-12-10 12:30:00","detailUrl":"http://www.savewalterwhite.com/","memberLimit":"500"}]
     * hasNext : N
     */

    private int statusCode;
    private String message;
    private String hasNext;
    private List<ActivitiesBean> activities;

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

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class ActivitiesBean {
        /**
         * coverUrl : http://images.amcnetworks.com/amc.com/wp-content/uploads/2010/12/logo_bb_stacked-C.png
         * isOverActivity : N
         * memberCount : 1
         * name : save Walter White!
         * orientedAssociations : 所有协会
         * startTime : 2017-12-10 09:30:00
         * isOverApply : N
         * id : 1
         * place : GZ
         * endTime : 2017-12-10 12:30:00
         * detailUrl : http://www.savewalterwhite.com/
         * memberLimit : 500
         */

        private String coverUrl;
        private String isOverActivity;
        private int memberCount;
        private String name;
        private String orientedAssociations;
        private String startTime;
        private String isOverApply;
        private int id;
        private String place;
        private String endTime;
        private String detailUrl;
        private String memberLimit;

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getIsOverActivity() {
            return isOverActivity;
        }

        public void setIsOverActivity(String isOverActivity) {
            this.isOverActivity = isOverActivity;
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

        public String getOrientedAssociations() {
            return orientedAssociations;
        }

        public void setOrientedAssociations(String orientedAssociations) {
            this.orientedAssociations = orientedAssociations;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getIsOverApply() {
            return isOverApply;
        }

        public void setIsOverApply(String isOverApply) {
            this.isOverApply = isOverApply;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }

        public String getMemberLimit() {
            return memberLimit;
        }

        public void setMemberLimit(String memberLimit) {
            this.memberLimit = memberLimit;
        }
    }
}
