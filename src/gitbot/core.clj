;  Copyright (c) 2011 Jasper Lievisse Adriaanse <jasper@humppa.nl>
;  The use and distribution terms for this software are covered by the
;  Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;  which can be found in the file epl-v10.html at the root of this distribution.
;  By using this software in any fashion, you are agreeing to be bound by
;  the terms of this license.
;  You must not remove this notice, or any other, from this software.

(ns ^{:author "Jasper Lievisse Adriaanse"}
  gitbot.core
  (:use clojure.tools.cli)
  (:require [gitbot.config :as conf]
            [xmpp-clj :as xmpp]
            [tentacles.core :as t]
            [tentacles.users :as u]))

(defn valid?
  "Quick check to see if our request was valid and got a proper response."
  [r]
  (when (not= (:status r) 404)
    true))

;; move the checking if it's a valid user from handle-message
;; to here so we can return either info or nil, which is easier
;; to check for later on.
(defn get-user-info
  [user]
  (let [response (u/user user)]
    response))

(defn handle-message [message]
  (let [body (:body message)
        from-user (:from-name message)
        response (if (empty? body) nil (get-user-info body))]
    (if (valid? response)
      response
      (str "User " body " is not a valid github user"))))

;; Helper so we don't have to restart with every change,
;; just (use 'gitbot.core :reload) and (reload) does the trick.
(defn reload-helper [message]
  (try
    (handle-message message)
    (catch Exception e (println e))))

(declare gitbot)

(defn reload [config]
  (xmpp/stop-bot gitbot)
  (def gitbot (xmpp/start-bot config reload-helper)))

(defn start [config]
  (def gitbot (xmpp/start-bot config reload-helper)))

(defn -main
  [& args]
  (let [parsed-args (cli args
                         ["--password" "password to authenticate with on the remote server"]
                         ["--username" "username (default: gitbot-clj@jabber.org"]
                         ["--host" "default: jabber.org"]
                         ["--domain" "default: jabber.org"])
        options (first parsed-args)]
    (start (conf/get-connection-config options))))
