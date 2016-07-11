# Freestuffly - Freecycle scraper by keywords

Clojure app for scraping freecycle to look for specific keywords.

It runs on Heroku as a hobby project and sends emails via Postmark.

## Setup
  - Install leinegen
  - run `lein deps`
  - add your preferred keywords and group to the yaml config file `config/gumtree.yml`

## TODO
  - Create an interface and store keywords in a database.
