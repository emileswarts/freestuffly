(ns freestuffly.notifier
  (:require [clj-http.client :as client])
  (:require [postmark.core :as p])
  (:require [freestuffly.gumtree.groups-scraper :as scraper]))

(def ^:private my-email "emile.swarts123+heroku@gmail.com")
(def ^:private subject "Free stuffly finds")
(def ^:private postmark
  (p/postmark
    (System/getenv "POSTMARK_API_KEY")
    (System/getenv "POSTMARK_SENDER_SIGNATURE")))

(defn- send-email
  [content]
  (postmark {:to my-email
             :subject subject
             :html content}))

(defn notify-scrape-results
  []
  (send-email (scraper/results)))
