# Freestuffly - Freecycle scraper by keywords

Clojure app for scraping freecycle to look for specific keywords.

It runs on Heroku as a hobby project and sends emails via Postmark.

## Setup
  - Clone this repo
  - Install leinegen `http://leiningen.org`
  - run `lein deps`
  - add your preferred keywords and groups to configuration file `config/gumtree.yml`
  - To run the actual scraper:
    - `lein repl`
    - `(ns freestuffly.gumtree.groups-scraper)`
    - `(results)`
  - Run the tests `lein test`
  - To have it notify you, set up a postmark account and grab the details
  - Set the required postmark details as environment variables on your machine (or wherever you decide to host it)
  `POSTMARK_API_KEY` and `POSTMARK_SENDER_SIGNATURE`

## Todo
  - Expose a customisable interface to input keywords and groups.  This can then be persisted to a database.
