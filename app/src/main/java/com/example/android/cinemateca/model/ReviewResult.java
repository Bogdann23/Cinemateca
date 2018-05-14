package com.example.android.cinemateca.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ReviewResult {

    /**
     * id : 100
     * page : 1
     * results : [{"author":"BradFlix","content":"I just plain love this movie!","id":"529bc23719c2957215011e7b","url":"https://www.themoviedb.org/review/529bc23719c2957215011e7b"},{"author":"Andres Gomez","content":"Far from being a good movie, with tons of flaws but already pointing to the pattern of the whole Ritchie's filmography.","id":"535856c30e0a26069400064c","url":"https://www.themoviedb.org/review/535856c30e0a26069400064c"},{"author":"David Perkins","content":"Genuinely one of my favorite movies of all time. Watched again recently and realised how well written, brilliantly shot, beautifully cast and cleverly produced this movie actually is!","id":"5873810f9251410e71009a68","url":"https://www.themoviedb.org/review/5873810f9251410e71009a68"}]
     * total_pages : 1
     * total_results : 3
     */

    private int id;
    private int page;
    private int total_pages;
    private int total_results;
    private List<ResultsBean> results;

    public ReviewResult(Context context, List<ResultsBean> review) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * author : BradFlix
         * content : I just plain love this movie!
         * id : 529bc23719c2957215011e7b
         * url : https://www.themoviedb.org/review/529bc23719c2957215011e7b
         */

        private String author;
        private String content;
        private String id;
        private String url;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
