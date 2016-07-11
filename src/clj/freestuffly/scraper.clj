(ns freestuffly.scraper
  (:require [clj-http.client :as client])
  (:require [hickory.core :as h])
  (:require [clojure.string :as string])
  (:require [clojure.set :as cset])
  (:require [clj-yaml.core :as yaml])
  (:require [hickory.select :as s]))

(def ^:private config (yaml/parse-string (slurp "config/gumtree.yml")))

(defn- interesting-keywords [] (:keywords config))

(defn- interesting-keywords-regex [] (re-pattern (str "(?i)" (string/join "|" (interesting-keywords)))))

(def ^:private freecycle-group "southwark-freecycle")

(def ^:private freecycle-results-per-page 100)

(defn- interesting-finds
  [results]
  (cset/select
    (fn [result]
      (re-find
        (re-matcher (interesting-keywords-regex)
                    (first (:content (into {} result))))))
    (set results)))

(def ^:private my-group-urls
  (str "https://groups.freecycle.org/group/"
       freecycle-group
       "/posts/offer?page=1&resultsperpage="
       freecycle-results-per-page
       "&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off"))

(defn- site-tree
  [scraped-html]
  (h/as-hickory (h/parse scraped-html)))

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
  ([] (scraped-content (:body (client/get my-group-urls))))
  ([scraped-html] (presentable
                    (interesting-finds
                      (content-for
                        (parsed-html
                          (site-tree scraped-html)))))))
