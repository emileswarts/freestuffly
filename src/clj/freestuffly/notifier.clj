(ns freestuffly.notifier
  (:require [clj-http.client :as client])
  (:require [hickory.core :as h])
  (:require [clj-mailgun.core :as mailgun])
  (:require [clojure.string :as string])
  (:require [hickory.select :as s]))

(def my-group-urls "https://groups.freecycle.org/group/southwark-freecycle/posts/offer?page=1&resultsperpage=3&showall=off&include_offers=off&include_wanteds=off&include_receiveds=off&include_takens=off")

(def site-tree (-> (client/get my-group-urls) :body h/parse h/as-hickory))

(defn parsed-html
  [html]
  (-> (s/select
        (s/descendant
          (s/id "group_posts_table")
          (s/and (s/tag :tr))
          (s/and (s/tag :td) (s/nth-child 2))
          (s/and (s/tag :a) (s/nth-child 1)))
      html)))

(defn rubbish-value?
  [content-values]
  (not= (content-values :content) ["See details"]))

(defn content-for
  [html-vector]
  (filter rubbish-value? (map #(select-keys % [:attrs :content]) html-vector)))

(defn presentable
  [results]
  (string/join "\n\n" results))

(defn send-email
  [content]
  (let [credentials {:api-key (System/getenv "MAILGUN_API_KEY") :domain (System/getenv "YOUR_DOMAIN")}
        params {
                :from "emile@fierce-everglades-57947.herokuapp.com"
                :to "emile.swarts123@gmail.com"
                :subject "Free stuffly"
                :text "hello"}]
    (mailgun/send-email credentials params)))

(defn listen
  []
  (send-email
    (presentable (content-for (parsed-html site-tree)))))
