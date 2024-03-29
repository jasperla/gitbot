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
            [gitbot.commands :as cmd]
            [clojure.string :only (blank? split) :as string]
            [xmpp-clj :as xmpp]))

(defn- message-handler
  [message]
  (let [body (:body message)
        from-user (:from-name message)]
    (when (not (string/blank? body))
      (let [msg (string/split body #" ")]
        (case (first msg)
          "get" (cmd/get (rest msg))
          "login" (cmd/login (rest msg))
          (str "Invalid command: " msg))))))

;; Helper so we don't have to restart with every change,
;; just (use 'gitbot.core :reload) and (reload) does the trick.
(defn- reload-helper
  [message]
  (try
    (message-handler message)
    (catch Exception e (println e))))

(declare gitbot)

(defn reload
  [config]
  (xmpp/stop-bot gitbot)
  (def gitbot (xmpp/start-bot config reload-helper)))

(defn start
  [config]
  (def gitbot (xmpp/start-bot config reload-helper)))

(defn -main
  [args]
  (let [parsed-args (cli args
                         ["--password" "password to authenticate with on the remote server"]
                         ["--username" "username (default: gitbot-clj@jabber.org"]
                         ["--host" "default: jabber.org"]
                         ["--domain" "default: jabber.org"])
        options (first parsed-args)]
    (start (conf/get-connection-config options))))
