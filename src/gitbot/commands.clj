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
  (:require [clojure.string :only (blank? split) :as string]
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
    (do-cmd (u/user (first s)))))

;; Dispatch the command to the appropriate function. Most stuff will be returned pprinted.
;; The dashboard command to show recent news will need to go through the parser, also to
;; remember what's already been pushed to the user.
(defn get
  [s]
  ;(str "Got the GET command: " s)
  (case (first s)
    "user" (get-user (rest s))
    (str "Invalid GET command: " s)))
