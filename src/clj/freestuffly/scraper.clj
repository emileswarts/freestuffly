(ns freestuffly.scraper
  (:require [clj-http.client :as client])
  (:require [hickory.core :as h])
  (:require [clojure.string :as string])
  (:require [clojure.set :as cset])
  (:require [hickory.select :as s]))

(def ^:private interesting-keywords
  #"(?i)paint|playstation|camp|emulsion|lamp|ladder|roller|xbox|mac|fish|marine|kitchen|new|unused|ergodox|cherry|programming|roller|suitcase")

(def ^:private freecycle-group "southwark-freecycle")
(def ^:private freecycle-results-per-page 100)

(defn- interesting-finds
  [results]
  (cset/select
    (fn [result]
      (re-find
        (re-matcher interesting-keywords
                    (first (:content (into {} result))))))
    (set results)))

(def ^:private my-group-urls
  (str "https://groups.freecycle.org/group/"
       freecycle-group
       "/posts/offer?page=1&resultsperpage="
       freecycle-results-per-page
       "&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off"))

(defn- site-tree
  []
  (-> (client/get my-group-urls) :body h/parse h/as-hickory))

(defn- parsed-html
  [html]
  (-> (s/select
        (s/descendant
          (s/id "group_posts_table")
          (s/and (s/tag :tr))
          (s/and (s/tag :td) (s/nth-child 2))
          (s/and (s/tag :a) (s/nth-child 1)))
      html)))

(defn- interesting-value?
  [content-values]
  (not= (content-values :content) ["See details"]))

(defn- content-for
  [html-vector]
  (filter interesting-value? (map #(select-keys % [:attrs :content]) html-vector)))

(defn- presentable
  [results]
  (str "FOUND\n\n\n\n"
       (string/join "\n\n\n"
                    (map vals results))))

(defn scraped-content
  ([] (scraped-content site-tree))
  ([content-hash] (presentable (interesting-finds (content-for (parsed-html (content-hash)))))))
