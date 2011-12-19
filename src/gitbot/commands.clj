;  Copyright (c) 2011 Jasper Lievisse Adriaanse <jasper@humppa.nl>
;  The use and distribution terms for this software are covered by the
;  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;  which can be found in the file epl-v10.html at the root of this distribution.
;  By using this software in any fashion, you are agreeing to be bound by
;  the terms of this license.
;  You must not remove this notice, or any other, from this software.

(ns ^{:author "Jasper Lievisse Adriaanse"}
  gitbot.commands
  (:refer-clojure :exclude [get])
  (:use clojure.tools.cli)
  (:require [gitbot.config :as config]
            [clojure.string :only (blank? split) :as string]
            [clojure.pprint :only (pprint) :as pp]
            [tentacles.users :as u]
            ;[gitbot.parser :as parser]
            ))

(defn- do-cmd
  "Actually perform the commands and check for valid responses along the way."
  [action]
  (let [response action]
    (if (not (valid? response))
      (str "Command received invalid argument or response.")
      (p-str response))))

(defn- p-str
  "Pretty print s to a string, instead of *out*."
  [s]
  (with-out-str (pp/pprint s)))

(defn- valid?
  "Quick check to see if our request was valid and got a proper response."
  [r]
  (when (not= (:status r) 404)
    true))

(defn- get-user
  [s]
  (when (not (empty? s))
    (-> (first s) u/user do-cmd)))

;; Dispatch the command to the appropriate function. Most stuff will be returned pprinted.
;; The dashboard command to show recent news will need to go through the parser, also to
;; remember what's already been pushed to the user.
(defn get
  [s]
  ;(str "Got the GET command: " s)
  (case (first s)
    "user" (get-user (rest s))
    "credentials" (str config/credentials)
    (str "Invalid GET command: " s)))

(defn login
  "Allow a user to login (or currently rather store their credentials in memory).
   Note that the provided credentials are not checked right now."
  [s]
  (if (= (count s) 2)
    (do
      (config/save-credentials {:auth (str (first s) ":" (second s))})
      (str "Stored login credentials."))
    (str "Invalid number of arguments passed. 2 are required (username password).")))
