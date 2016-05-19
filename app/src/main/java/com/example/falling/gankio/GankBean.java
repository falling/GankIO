package com.example.falling.gankio;

import com.example.falling.gankio.db.GankDatabase;

import java.util.List;

/**
 * Created by falling on 16/5/8.
 */
public class GankBean {
    private String error;
    private List<Result> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        private String _id;
        private String createdAt;
        private String publishedAt;
        private String desc;
        private String type;
        private String url;
        private String who;

        public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public String getInsertSql() {
            return "insert into " + GankDatabase.TableName
                    + " (_id,publishedAt,desc,type,url,who)  values( '"
                    + this._id + "' , '" + this.publishedAt + "' , '" + this.desc + "' , '" + this.type + "' , '" + this.url + "' , '" + this.who + "' )";
        }
    }
}
